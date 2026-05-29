import { defineStore } from 'pinia'
import { ref } from 'vue'
import api from '../services/api'

export const useAdminStore = defineStore('admin', () => {
  const reports = ref([])
  const allTags = ref([])
  const allIngredients = ref([])
  const users = ref([])
  
  const getReports = async () => {
    try {
      const response = await api.get('/admin/reports')
      reports.value = response.data
      return response.data
    } catch (error) {
      console.error('Ошибка загрузки жалоб:', error)
      return []
    }
  }
  
  const resolveReport = async (reportId, action) => {
    try {
      await api.post(`/admin/reports/${reportId}/resolve`, { action })
    } catch (error) {
      console.error('Ошибка обработки жалобы:', error)
    }
  }
  
  const getTags = async () => {
    try {
      const response = await api.get('/admin/tags')
      allTags.value = response.data
      return response.data
    } catch (error) {
      console.error('Ошибка загрузки тегов:', error)
      return []
    }
  }
  
  const addTag = async (tagName) => {
    try {
      const response = await api.post('/admin/tags', { name: tagName })
      allTags.value.push(response.data)
      return response.data
    } catch (error) {
      console.error('Ошибка добавления тега:', error)
    }
  }
  
  const deleteTag = async (tagId) => {
    try {
      await api.delete(`/admin/tags/${tagId}`)
      allTags.value = allTags.value.filter(t => t.id !== tagId)
    } catch (error) {
      console.error('Ошибка удаления тега:', error)
    }
  }
  
  const getIngredients = async () => {
    try {
      const response = await api.get('/admin/ingredients')
      allIngredients.value = response.data
      return response.data
    } catch (error) {
      console.error('Ошибка загрузки ингредиентов:', error)
      return []
    }
  }
  
  const addIngredient = async (ingredientName) => {
    try {
      const response = await api.post('/admin/ingredients', { name: ingredientName })
      allIngredients.value.push(response.data)
      return response.data
    } catch (error) {
      console.error('Ошибка добавления ингредиента:', error)
    }
  }
  
  const deleteIngredient = async (ingredientId) => {
    try {
      await api.delete(`/admin/ingredients/${ingredientId}`)
      allIngredients.value = allIngredients.value.filter(i => i.id !== ingredientId)
    } catch (error) {
      console.error('Ошибка удаления ингредиента:', error)
    }
  }
  
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
  
  const banUser = async (userId) => {
    try {
      await api.post(`/admin/users/${userId}/ban`)
    } catch (error) {
      console.error('Ошибка бана пользователя:', error)
    }
  }
  
  const deleteUser = async (userId) => {
    try {
      await api.delete(`/admin/users/${userId}`)
      users.value = users.value.filter(u => u.id !== userId)
    } catch (error) {
      console.error('Ошибка удаления пользователя:', error)
    }
  }
  
  return {
    reports,
    allTags,
    allIngredients,
    users,
    getReports,
    resolveReport,
    getTags,
    addTag,
    deleteTag,
    getIngredients,
    addIngredient,
    deleteIngredient,
    getAllUsers,
    banUser,
    deleteUser
  }
})