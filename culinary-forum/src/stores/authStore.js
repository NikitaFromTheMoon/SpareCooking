import { defineStore } from 'pinia'
import { ref, computed } from 'vue'

export const useAuthStore = defineStore('auth', () => {
  const user = ref(null)
  const roles = ref([])
  
  const isAuthenticated = computed(() => !!user.value)
  const isAdmin = computed(() => roles.value.includes('admin'))
  const isModerator = computed(() => roles.value.includes('moderator'))
  
  const setUser = (keycloak) => {
    if (keycloak?.authenticated && keycloak.tokenParsed) {
      user.value = {
        id: keycloak.tokenParsed.sub,
        username: keycloak.tokenParsed.preferred_username,
        email: keycloak.tokenParsed.email,
        firstName: keycloak.tokenParsed.given_name,
        lastName: keycloak.tokenParsed.family_name,
        avatar: keycloak.tokenParsed.avatar || null,
        createdAt: keycloak.tokenParsed.created_at,
        favorites: [],
        recipes: [],
        comments: []
      }
      roles.value = keycloak.tokenParsed.realm_access?.roles || []
    }
  }
  
  const logout = () => {
    user.value = null
    roles.value = []
    window.$keycloak?.logout({ redirectUri: window.location.origin })
  }
  
  const deleteAccount = async () => {
    if (confirm('Вы уверены? Все ваши рецепты и комментарии будут удалены.')) {
      // API вызов удаления аккаунта
      await fetch(`${window.$apiUrl}/users/${user.value.id}`, {
        method: 'DELETE',
        headers: { 'Authorization': `Bearer ${window.$keycloak.token}` }
      })
      logout()
    }
  }
  
  return { user, roles, isAuthenticated, isAdmin, isModerator, setUser, logout, deleteAccount }
})