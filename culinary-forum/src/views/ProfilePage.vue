<template>
  <div class="profile-page" v-if="user">
    <div class="profile-header">
      <div class="profile-avatar">
        {{ user.username?.[0]?.toUpperCase() || '👤' }}
      </div>
      <div class="profile-info">
        <h1>{{ user.username }}</h1>
        <p>{{ user.email }}</p>
        <p>Рецептов: {{ user.recipes?.length || 0 }} | Комментариев: {{ user.comments?.length || 0 }}</p>
        <p>Дата регистрации: {{ formatDate(user.createdAt) }}</p>
      </div>
    </div>
    
    <div class="profile-tabs">
      <button :class="{ active: activeTab === 'recipes' }" @click="activeTab = 'recipes'">
        Мои рецепты
      </button>
      <button :class="{ active: activeTab === 'favorites' }" @click="activeTab = 'favorites'">
        Избранное
      </button>
      <button :class="{ active: activeTab === 'comments' }" @click="activeTab = 'comments'">
        Мои комментарии
      </button>
      <button :class="{ active: activeTab === 'settings' }" @click="activeTab = 'settings'">
        Настройки
      </button>
    </div>
    
    <div class="profile-content">
      <div v-if="activeTab === 'recipes'" class="recipes-grid">
        <RecipeCard 
          v-for="recipe in user.recipes" 
          :key="recipe.id"
          :recipe="recipe"
          @click="$router.push(`/recipe/${recipe.id}`)"
        />
      </div>
      
      <div v-else-if="activeTab === 'favorites'" class="recipes-grid">
        <RecipeCard 
          v-for="recipe in favorites" 
          :key="recipe.id"
          :recipe="recipe"
          @click="$router.push(`/recipe/${recipe.id}`)"
        />
      </div>
      
      <div v-else-if="activeTab === 'comments'" class="comments-list">
        <div v-for="comment in user.comments" :key="comment.id" class="comment-item">
          <p><strong>Рецепт:</strong> {{ comment.recipeTitle }}</p>
          <p>{{ comment.text }}</p>
          <small>{{ formatDate(comment.date) }}</small>
        </div>
      </div>
      
      <div v-else-if="activeTab === 'settings'" class="settings-panel">
        <button class="danger-btn" @click="deleteAccount">
          🗑️ Удалить аккаунт
        </button>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRoute } from 'vue-router'
import { useAuthStore } from '../stores/authStore'
import { useRecipeStore } from '../stores/recipeStore'
import RecipeCard from '../components/recipe/RecipeCard.vue'
import api from '../services/api'

const route = useRoute()
const authStore = useAuthStore()
const recipeStore = useRecipeStore()

const user = ref(null)
const favorites = ref([])
const activeTab = ref('recipes')

const loadProfile = async () => {
  const userId = route.params.id || authStore.user?.id
  if (!userId) return
  
  try {
    const response = await api.get(`/users/${userId}`)
    user.value = response.data
  } catch (error) {
    console.error('Ошибка загрузки профиля:', error)
    user.value = authStore.user
  }
}

const loadFavorites = async () => {
  try {
    const response = await api.get('/users/favorites')
    favorites.value = response.data
  } catch (error) {
    console.error('Ошибка загрузки избранного:', error)
  }
}

const deleteAccount = async () => {
  if (confirm('Вы уверены? Все ваши данные будут удалены безвозвратно.')) {
    await authStore.deleteAccount()
  }
}

const formatDate = (date) => {
  if (!date) return '—'
  return new Date(date).toLocaleDateString('ru-RU')
}

onMounted(() => {
  loadProfile()
  loadFavorites()
})
</script>

<style scoped>
.profile-page {
  max-width: 1000px;
  margin: 0 auto;
  background: white;
  border-radius: 24px;
  overflow: hidden;
}

.profile-header {
  display: flex;
  gap: 2rem;
  padding: 2rem;
  background: linear-gradient(135deg, #fff5e6, #ffe0cc);
}

.profile-avatar {
  width: 100px;
  height: 100px;
  background: linear-gradient(135deg, #ff6b6b, #ff8e53);
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 3rem;
  color: white;
}

.profile-info h1 {
  margin-bottom: 0.5rem;
}

.profile-info p {
  color: #666;
  margin-bottom: 0.25rem;
}

.profile-tabs {
  display: flex;
  gap: 1rem;
  padding: 0 2rem;
  border-bottom: 1px solid #f0f0f0;
}

.profile-tabs button {
  padding: 1rem 0;
  background: none;
  border: none;
  cursor: pointer;
  font-weight: 500;
  color: #666;
  position: relative;
}

.profile-tabs button.active {
  color: #ff6b6b;
}

.profile-tabs button.active::after {
  content: '';
  position: absolute;
  bottom: -1px;
  left: 0;
  right: 0;
  height: 2px;
  background: #ff6b6b;
}

.profile-content {
  padding: 2rem;
}

.recipes-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(300px, 1fr));
  gap: 1.5rem;
}

.comments-list {
  display: flex;
  flex-direction: column;
  gap: 1rem;
}

.comment-item {
  padding: 1rem;
  background: #f9f9f9;
  border-radius: 12px;
}

.settings-panel {
  text-align: center;
}

.danger-btn {
  padding: 0.75rem 1.5rem;
  background: #f44336;
  color: white;
  border: none;
  border-radius: 8px;
  cursor: pointer;
}
</style>