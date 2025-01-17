import { getEnv } from '@/core/utils/EnvLoader'

export const BACKEND_HOST = getEnv("VITE_BACKEND_HOST")
export const BACKEND_PORT = getEnv("VITE_BACKEND_PORT")
export const BACKEND_PROTOCOL = getEnv("VITE_BACKEND_PROTOCOL")
export const BACKEND_URL = `${BACKEND_PROTOCOL}://${BACKEND_HOST}:${BACKEND_PORT}`

export const FRONTEND_PROTOKOL = getEnv("VITE_FRONTEND_PROTOCOL")
export const FRONTEND_HOST = getEnv("VITE_FRONTEND_HOST")
export const FRONTEND_PORT = getEnv("VITE_FRONTEND_PORT")
export const FRONTEND_URL = `${FRONTEND_PROTOKOL}://${FRONTEND_HOST}:${FRONTEND_PORT}`
