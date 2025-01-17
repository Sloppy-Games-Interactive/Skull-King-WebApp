import process from 'node:process'

export function getEnv(env: string): string {

  if (process.env && process.env[env]) {
    return process.env[env] || '';
  }

  if (import.meta.env) {
    return import.meta.env[env];
  } else {
    // if node
    return process.env[env] || '';
  }
}
