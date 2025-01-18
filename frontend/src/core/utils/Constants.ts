
export const BACKEND_HOST = import.meta.env.VITE_BACKEND_HOST
export const BACKEND_PORT = import.meta.env.VITE_BACKEND_PORT
export const BACKEND_PROTOCOL = import.meta.env.VITE_BACKEND_PROTOCOL
export const BACKEND_URL = `${BACKEND_PROTOCOL}://${BACKEND_HOST}:${BACKEND_PORT}`
export const API_PATH = import.meta.env.VITE_API_PREFIX

export const FRONTEND_PROTOKOL = import.meta.env.VITE_FRONTEND_PROTOCOL
export const FRONTEND_HOST = import.meta.env.VITE_FRONTEND_HOST
export const FRONTEND_PORT = import.meta.env.VITE_FRONTEND_PORT
export const FRONTEND_URL = `${FRONTEND_PROTOKOL}://${FRONTEND_HOST}:${FRONTEND_PORT}`
