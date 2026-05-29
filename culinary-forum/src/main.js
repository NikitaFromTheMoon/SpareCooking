import { createApp } from 'vue'
import { createPinia } from 'pinia'
import App from './App.vue'
import router from './router'
import './style.css'
import initKeycloak from './services/keycloak'

// Инициализация Keycloak перед запуском
initKeycloak().then(({ keycloak, app }) => {
  const pinia = createPinia()
  
  app.use(pinia)
  app.use(router)
  app.mount('#app')
}).catch((err) => {
  console.error('❌ Ошибка инициализации:', err)
})