<template>
  <div class="recipe-page" v-if="recipe">
    <!-- Изображения -->
    <div class="recipe-gallery">
      <img :src="recipe.images[0]" :alt="recipe.title" class="main-image" />
      <div class="thumbnails">
        <img v-for="img in recipe.images.slice(1, 4)" :key="img" :src="img" />
      </div>
    </div>
    
    <div class="recipe-header">
      <h1>{{ recipe.title }}</h1>
      
      <div class="recipe-meta">
        <span>👨‍🍳 {{ recipe.author.username }}</span>
        <span>⏱️ {{ recipe.cookingTime }} мин</span>
        <span>📅 {{ formatDate(recipe.createdAt) }}</span>
      </div>
      
      <!-- Рейтинг и избранное -->
      <div class="rating-favorite">
        <RecipeRating 
          :rating="recipe.rating || 0"
          :userRating="recipe.userRating"
          :recipeId="recipe.id"
          @rate="handleRate"
        />
        
        <button 
          class="favorite-btn" 
          :class="{ active: isFavorite }"
          @click="toggleFavorite"
        >
          {{ isFavorite ? '❤️ В избранном' : '🤍 В избранное' }}
        </button>
        
        <button v-if="canEdit" class="edit-btn" @click="goToEdit">
          ✏️ Редактировать
        </button>
        
        <button v-if="canDelete" class="delete-btn" @click="deleteRecipe">
          🗑️ Удалить
        </button>
      </div>
    </div>
    
    <!-- Перерасчет ингредиентов -->
    <IngredientCalculator 
      :ingredients="recipe.ingredients"
      :originalPortions="recipe.portions"
    />
    
    <!-- Описание из Quill.js -->
    <div class="recipe-description" v-html="recipe.description"></div>
    
    <!-- Теги -->
    <div class="recipe-tags">
      <span v-for="tag in recipe.tags" :key="tag" class="tag">
        #{{ tag }}
      </span>
    </div>
    
    <!-- Комментарии -->
    <CommentSection 
      :comments="recipe.comments"
      :recipeId="recipe.id"
      @comment-deleted="loadRecipe"
    />
    
    <!-- Кнопка жалобы -->
    <button class="report-btn" @click="reportRecipe">
      🚨 Пожаловаться на рецепт
    </button>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { useRecipeStore } from '../stores/recipeStore'
import { useAuthStore } from '../stores/authStore'
import RecipeRating from '../components/recipe/RecipeRating.vue'
import IngredientCalculator from '../components/recipe/IngredientCalculator.vue'
import CommentSection from '../components/comments/CommentSection.vue'

const route = useRoute()
const router = useRouter()
const recipeStore = useRecipeStore()
const authStore = useAuthStore()

const recipe = ref(null)
const isFavorite = ref(false)

const canEdit = computed(() => {
  return authStore.isAuthenticated && 
    (recipe.value?.author.id === authStore.user?.id || authStore.isAdmin)
})

const canDelete = computed(() => {
  return authStore.isAdmin || recipe.value?.author.id === authStore.user?.id
})

const loadRecipe = async () => {
  recipe.value = await recipeStore.getRecipeById(route.params.id)
  isFavorite.value = recipeStore.favorites.includes(recipe.value.id)
}

const handleRate = async (rating) => {
  await recipeStore.rateRecipe(recipe.value.id, rating)
  await loadRecipe()
}

const toggleFavorite = async () => {
  if (isFavorite.value) {
    await recipeStore.removeFromFavorites(recipe.value.id)
  } else {
    await recipeStore.addToFavorites(recipe.value.id)
  }
  isFavorite.value = !isFavorite.value
}

const goToEdit = () => {
  router.push(`/recipe/${recipe.value.id}/edit`)
}

const deleteRecipe = async () => {
  if (confirm('Удалить рецепт?')) {
    await recipeStore.deleteRecipe(recipe.value.id)
    router.push('/')
  }
}

const reportRecipe = () => {
  const reason = prompt('Опишите причину жалобы:')
  if (reason) {
    recipeStore.reportRecipe(recipe.value.id, reason)
  }
}

const formatDate = (date) => {
  return new Date(date).toLocaleDateString('ru-RU')
}

onMounted(() => {
  loadRecipe()
})
</script>

<style scoped>
.recipe-page {
  max-width: 1000px;
  margin: 0 auto;
  padding: 2rem;
  background: white;
  border-radius: 24px;
}

.recipe-gallery {
  margin-bottom: 2rem;
}

.main-image {
  width: 100%;
  height: 400px;
  object-fit: cover;
  border-radius: 16px;
}

.thumbnails {
  display: flex;
  gap: 1rem;
  margin-top: 1rem;
}

.thumbnails img {
  width: 100px;
  height: 100px;
  object-fit: cover;
  border-radius: 8px;
  cursor: pointer;
}

.recipe-header {
  margin-bottom: 2rem;
}

.recipe-meta {
  display: flex;
  gap: 1.5rem;
  color: #666;
  margin: 1rem 0;
}

.rating-favorite {
  display: flex;
  gap: 1rem;
  align-items: center;
  flex-wrap: wrap;
}

.favorite-btn, .edit-btn, .delete-btn {
  padding: 0.5rem 1rem;
  border: none;
  border-radius: 20px;
  cursor: pointer;
}

.favorite-btn {
  background: none;
  border: 1px solid #ff6b6b;
  color: #ff6b6b;
}

.favorite-btn.active {
  background: #ff6b6b;
  color: white;
}

.edit-btn {
  background: #4caf50;
  color: white;
}

.delete-btn {
  background: #f44336;
  color: white;
}

.recipe-description {
  line-height: 1.8;
  margin: 2rem 0;
}

.recipe-tags {
  display: flex;
  flex-wrap: wrap;
  gap: 0.5rem;
  margin: 1rem 0;
}

.tag {
  background: #f0f0f0;
  padding: 0.25rem 0.75rem;
  border-radius: 20px;
  font-size: 0.9rem;
}

.report-btn {
  margin-top: 2rem;
  padding: 0.5rem 1rem;
  background: none;
  border: 1px solid #999;
  border-radius: 8px;
  color: #666;
  cursor: pointer;
}
</style>