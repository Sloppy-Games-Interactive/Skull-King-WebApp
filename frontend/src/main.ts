import './assets/main.scss'

import { createApp } from 'vue'
import { createPinia } from 'pinia'

import App from './App.vue'
import router from './core/router'
import {API_INJECTION_KEY, ApiService} from "@/core/rest/api";

// Vuetify
import 'vuetify/styles'
import { createVuetify } from 'vuetify'
import * as components from 'vuetify/components'
import * as directives from 'vuetify/directives'
import {FontAwesomeIcon} from "@fortawesome/vue-fontawesome";
import {initFontAwesomeLibrary} from "@/core/icons/icons";

const vuetify = createVuetify({
  components,
  directives,
})

initFontAwesomeLibrary()

const app = createApp(App)
  .component('fa-icon', FontAwesomeIcon)

const api = new ApiService()
app.provide(API_INJECTION_KEY, api);
app.use(vuetify)
app.use(createPinia())
app.use(router)

app.mount('#app')
