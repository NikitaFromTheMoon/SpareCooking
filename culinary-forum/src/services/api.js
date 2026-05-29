import axios from 'axios'

const api = axios.create({
  baseURL: 'http://localhost:8080/api'
})

// Интерсептор для добавления токена
api.interceptors.request.use((config) => {
  const keycloak = window.$keycloak
  if (keycloak?.authenticated && keycloak.token) {
    config.headers.Authorization = `Bearer ${keycloak.token}`
  }
  return config
})

// Интерсептор для обработки ошибок
api.interceptors.response.use(
  response => response,
  error => {
    if (error.response?.status === 401) {
      window.$keycloak?.login()
    }
    return Promise.reject(error)
  }
)

export default api