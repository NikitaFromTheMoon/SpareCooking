import { defineStore } from 'pinia'
import { ref, computed } from 'vue'
import api from '../services/api'

export const useRecipeStore = defineStore('recipe', () => {
  const recipes = ref([])
  const currentRecipe = ref(null)
  const favorites = ref([])
  const pagination = ref({ page: 1, limit: 12, total: 0 })
  const filters = ref({ search: '', tags: [], ingredients: [] })
  
  // Поиск с пагинацией
  const searchRecipes = async (params = {}) => {
    try {
      const response = await api.get('/recipes/search', {
        params: {
          page: pagination.value.page,
          limit: pagination.value.limit,
          search: filters.value.search,
          tags: filters.value.tags.join(','),
          ingredients: filters.value.ingredients.join(','),
          ...params
        }
      })
      recipes.value = response.data.items
      pagination.value.total = response.data.total
      return response.data
    } catch (error) {
      console.error('Ошибка поиска:', error)
    }
  }
  
  // Получение рецепта по ID
  const getRecipeById = async (id) => {
    try {
      const response = await api.get(`/recipes/${id}`)
      currentRecipe.value = response.data
      return response.data
    } catch (error) {
      console.error('Ошибка получения рецепта:', error)
    }
  }
  
  // Создание рецепта
  const createRecipe = async (recipeData) => {
    try {
      const formData = new FormData()
      
      // Основные поля
      formData.append('title', recipeData.title)
      formData.append('description', recipeData.description)
      formData.append('cookingTime', recipeData.cookingTime)
      formData.append('portions', recipeData.portions)
      
      // Ингредиенты
      formData.append('ingredients', JSON.stringify(recipeData.ingredients))
      
      // Теги
      recipeData.tags.forEach(tag => formData.append('tags', tag))
      
      // Изображения
      recipeData.images.forEach(image => {
        if (image instanceof File) {
          formData.append('images', image)
        } else {
          formData.append('existingImages', image)
        }
      })
      
      const response = await api.post('/recipes', formData, {
        headers: { 'Content-Type': 'multipart/form-data' }
      })
      
      recipes.value.unshift(response.data)
      return response.data
    } catch (error) {
      console.error('Ошибка создания:', error)
      throw error
    }
  }
  
  // Обновление рецепта
  const updateRecipe = async (id, recipeData) => {
    try {
      const response = await api.put(`/recipes/${id}`, recipeData)
      const index = recipes.value.findIndex(r => r.id === id)
      if (index !== -1) recipes.value[index] = response.data
      return response.data
    } catch (error) {
      console.error('Ошибка обновления:', error)
      throw error
    }
  }
  
  // Удаление рецепта
  const deleteRecipe = async (id) => {
    if (!confirm('Удалить рецепт? Это действие необратимо.')) return
    
    try {
      await api.delete(`/recipes/${id}`)
      recipes.value = recipes.value.filter(r => r.id !== id)
      return true
    } catch (error) {
      console.error('Ошибка удаления:', error)
      throw error
    }
  }
  
  // Оценка рецепта
  const rateRecipe = async (id, rating) => {
    try {
      const response = await api.post(`/recipes/${id}/rate`, { rating })
      if (currentRecipe.value && currentRecipe.value.id === id) {
        currentRecipe.value.rating = response.data.averageRating
        currentRecipe.value.userRating = rating
      }
      return response.data
    } catch (error) {
      console.error('Ошибка оценки:', error)
    }
  }
  
  // Избранное
  const addToFavorites = async (id) => {
    try {
      await api.post(`/recipes/${id}/favorite`)
      if (!favorites.value.includes(id)) favorites.value.push(id)
    } catch (error) {
      console.error('Ошибка добавления в избранное:', error)
    }
  }
  
  const removeFromFavorites = async (id) => {
    try {
      await api.delete(`/recipes/${id}/favorite`)
      favorites.value = favorites.value.filter(favId => favId !== id)
    } catch (error) {
      console.error('Ошибка удаления из избранного:', error)
    }
  }
  
  // Репорт на рецепт
  const reportRecipe = async (id, reason) => {
    try {
      await api.post(`/recipes/${id}/report`, { reason })
      alert('Жалоба отправлена администратору')
    } catch (error) {
      console.error('Ошибка отправки жалобы:', error)
    }
  }
  
  return {
    recipes,
    currentRecipe,
    favorites,
    pagination,
    filters,
    searchRecipes,
    getRecipeById,
    createRecipe,
    updateRecipe,
    deleteRecipe,
    rateRecipe,
    addToFavorites,
    removeFromFavorites,
    reportRecipe
  }
})