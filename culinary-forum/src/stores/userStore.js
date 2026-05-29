import { defineStore } from 'pinia'
import { ref } from 'vue'
import api from '../services/api'

export const useUserStore = defineStore('user', () => {
  const users = ref([])
  const currentProfile = ref(null)
  const searchHistory = ref([])
  
  // Получение всех пользователей (для админа)
  const getAllUsers = async () => {
    try {
      const response = await api.get('/admin/users')
      users.value = response.data
      return response.data
    } catch (error) {
      console.error('Ошибка загрузки пользователей:', error)
      return []
    }
  }
  
  // Получение профиля пользователя
  const getUserProfile = async (userId) => {
    try {
      const response = await api.get(`/users/${userId}`)
      currentProfile.value = response.data
      return response.data
    } catch (error) {
      console.error('Ошибка загрузки профиля:', error)
    }
  }
  
  // Обновление профиля
  const updateProfile = async (userId, data) => {
    try {
      const response = await api.put(`/users/${userId}`, data)
      if (currentProfile.value?.id === userId) {
        currentProfile.value = response.data
      }
      return response.data
    } catch (error) {
      console.error('Ошибка обновления профиля:', error)
      throw error
    }
  }
  
  // Получение рецептов пользователя
  const getUserRecipes = async (userId) => {
    try {
      const response = await api.get(`/users/${userId}/recipes`)
      return response.data
    } catch (error) {
      console.error('Ошибка загрузки рецептов пользователя:', error)
      return []
    }
  }
  
  // Получение комментариев пользователя
  const getUserComments = async (userId) => {
    try {
      const response = await api.get(`/users/${userId}/comments`)
      return response.data
    } catch (error) {
      console.error('Ошибка загрузки комментариев пользователя:', error)
      return []
    }
  }
  
  // Добавление в историю поиска
  const addToSearchHistory = (query) => {
    if (!query.trim()) return
    searchHistory.value = [
      query,
      ...searchHistory.value.filter(q => q !== query)
    ].slice(0, 10)
    localStorage.setItem('searchHistory', JSON.stringify(searchHistory.value))
  }
  
  // Загрузка истории поиска
  const loadSearchHistory = () => {
    const saved = localStorage.getItem('searchHistory')
    if (saved) {
      searchHistory.value = JSON.parse(saved)
    }
  }
  
  // Очистка истории поиска
  const clearSearchHistory = () => {
    searchHistory.value = []
    localStorage.removeItem('searchHistory')
  }
  
  // Бан пользователя (админ)
  const banUser = async (userId, reason) => {
    try {
      await api.post(`/admin/users/${userId}/ban`, { reason })
      if (currentProfile.value?.id === userId) {
        currentProfile.value.isBanned = true
      }
    } catch (error) {
      console.error('Ошибка бана пользователя:', error)
    }
  }
  
  // Разбан пользователя (админ)
  const unbanUser = async (userId) => {
    try {
      await api.post(`/admin/users/${userId}/unban`)
      if (currentProfile.value?.id === userId) {
        currentProfile.value.isBanned = false
      }
    } catch (error) {
      console.error('Ошибка разбана пользователя:', error)
    }
  }
  
  return {
    users,
    currentProfile,
    searchHistory,
    getAllUsers,
    getUserProfile,
    updateProfile,
    getUserRecipes,
    getUserComments,
    addToSearchHistory,
    loadSearchHistory,
    clearSearchHistory,
    banUser,
    unbanUser
  }
})