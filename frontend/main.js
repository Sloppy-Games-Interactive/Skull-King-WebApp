// Start electron with the following command:
// electron .

import { app, BrowserWindow } from 'electron';
import { fileURLToPath } from 'url';
import path from 'path';
import { getEnv } from '@/core/utils/EnvLoader.js'
import { FRONTEND_URL } from '@/core/utils/Constants.js'

// Define __dirname
const __filename = fileURLToPath(import.meta.url);
const __dirname = path.dirname(__filename);

function createWindow() {
  const mainWindow = new BrowserWindow({
    width: 800,
    height: 600,
    webPreferences: {
      // for vite
      preload: path.join(__dirname, 'preload.js'),

      //maybe for local
      //preload: path.join(__dirname, 'dist', 'preload.cjs'),
      nodeIntegration: true,
      contextIsolation: false,
    },
  });

  // load it from the vite dev server

  mainWindow.loadURL(FRONTEND_URL).catch((err) => {
    console.error('Failed to load resource:', err);
  });

  // Load the index.html from the dist directory (NOT WORKING)
  // const indexPath = path.join(__dirname, 'dist', 'index.html');
  // mainWindow.loadFile(indexPath).catch((err) => {
  //   console.error('Failed to load index.html:', err);
  // });
}

app.on('ready', createWindow);

app.on('window-all-closed', () => {
  if (process.platform !== 'darwin') {
    app.quit();
  }
});

app.on('activate', () => {
  if (BrowserWindow.getAllWindows().length === 0) {
    createWindow();
  }
});
