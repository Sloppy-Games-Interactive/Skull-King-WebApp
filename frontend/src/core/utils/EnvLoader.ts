export function getEnv(env: string): string {
  // if vite or express
  if (import.meta.env) {
    return import.meta.env[env];
  } else {
    // if node
    return process.env[env] || '';
  }
}
