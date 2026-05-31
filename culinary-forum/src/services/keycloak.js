import { createApp } from 'vue'
import App from '../App.vue'
import Keycloak from 'keycloak-js'

let keycloak = null

export default async function initKeycloak() {
  keycloak = new Keycloak({
    url: 'http://localhost:8081',
    realm: 'sparecook',
    clientId: 'spare-cooking-frontend'
  })

  try {
    const authenticated = await keycloak.init({
      onLoad: 'check-sso',
      checkLoginIframe: false
    })

    const app = createApp(App)
    
    // Глобальные свойства
    app.config.globalProperties.$keycloak = keycloak
    app.provide('$keycloak', keycloak)
    app.provide('$apiUrl', 'http://localhost:8080')

    if (authenticated) {
      console.log('✅ Авторизован:', keycloak.tokenParsed?.preferred_username)
      
      // Получение ролей пользователя
      const roles = keycloak.tokenParsed?.realm_access?.roles || []
      app.config.globalProperties.$userRoles = roles
      
      // Автообновление токена
      setInterval(() => {
        keycloak.updateToken(70).catch(() => {
          console.log('❌ Сессия истекла')
        })
      }, 60000)
    }

    return { keycloak, app }
  } catch (err) {
    console.error('Ошибка Keycloak:', err)
    throw err
  }
}

export { keycloak }