import express from 'express';
import path from 'path';
import { fileURLToPath } from 'url';
import { dirname } from 'path';
import compression from 'compression';


const __filename = fileURLToPath(import.meta.url);
const __dirname = dirname(__filename);

const app = express();
const port = process.env.PORT || 5173;//3000;//todo settle for a port that both vite and this will use

const distPath = path.join(__dirname, 'dist');
console.log(`Serving static files from: ${distPath}`);

app.use(compression());

app.use(express.static(distPath, {
  maxAge: '30d',
}));

// Serve static files from the 'dist' directory
app.use(express.static(distPath));

// Handle all other routes by serving the index.html file
app.get('*', (req, res) => {
  const indexPath = path.join(distPath, 'index.html');
  console.log(`Serving index.html from: ${indexPath}`);
  res.sendFile(indexPath);
});

app.listen(port, () => {
  console.log(`Server is running on port ${port}`);
});
