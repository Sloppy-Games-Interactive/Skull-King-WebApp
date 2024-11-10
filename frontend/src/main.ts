import './assets/main.css'

import { createApp } from 'vue'
import { createPinia } from 'pinia'

import App from './App.vue'
import router from './router'
import {API_INJECTION_KEY, ApiService} from "@/rest/api";

const app = createApp(App)

const api = new ApiService()
app.provide(API_INJECTION_KEY, api);

app.use(createPinia())
app.use(router)

app.mount('#app')
