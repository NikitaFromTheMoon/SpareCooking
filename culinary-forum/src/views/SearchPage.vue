<template>
  <div class="search-page">
    <!-- Поисковая строка -->
    <SearchBar v-model="searchQuery" @search="handleSearch" />
    
    <div class="search-layout">
      <!-- Боковая панель фильтров -->
      <aside class="filters-sidebar">
        <TagFilter 
          :selectedTags="selectedTags"
          :availableTags="allTags"
          @update:selectedTags="selectedTags = $event"
        />
        
        <div class="filter-group">
          <h4>Ингредиенты</h4>
          <select v-model="selectedIngredients" multiple>
            <option v-for="ing in allIngredients" :key="ing.id" :value="ing.name">
              {{ ing.name }}
            </option>
          </select>
        </div>
        
        <div class="filter-group">
          <h4>Время приготовления</h4>
          <input type="range" v-model="maxTime" min="0" max="240" />
          <span>до {{ maxTime }} мин</span>
        </div>
        
        <button class="apply-filters-btn" @click="applyFilters">Применить</button>
        <button class="reset-filters-btn" @click="resetFilters">Сбросить</button>
      </aside>
      
      <!-- Результаты поиска -->
      <div class="search-results">
        <div class="results-header">
          <span>Найдено: {{ pagination.total }} рецептов</span>
          <select v-model="sortBy">
            <option value="relevance">По релевантности</option>
            <option value="rating">По рейтингу</option>
            <option value="date">По дате</option>
            <option value="time">По времени готовки</option>
          </select>
        </div>
        
        <div class="recipes-grid">
          <RecipeCard 
            v-for="recipe in recipes" 
            :key="recipe.id"
            :recipe="recipe"
            @click="goToRecipe(recipe.id)"
          />
        </div>
        
        <Pagination 
          :current="pagination.page"
          :total="pagination.total"
          :limit="pagination.limit"
          @page-change="changePage"
        />
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, watch, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { useRecipeStore } from '../stores/recipeStore'
import SearchBar from '../components/common/SearchBar.vue'
import TagFilter from '../components/common/TagFilter.vue'
import RecipeCard from '../components/recipe/RecipeCard.vue'
import Pagination from '../components/common/Pagination.vue'

const router = useRouter()
const recipeStore = useRecipeStore()

const searchQuery = ref('')
const selectedTags = ref([])
const selectedIngredients = ref([])
const maxTime = ref(60)
const sortBy = ref('relevance')

const { recipes, pagination, allTags, allIngredients } = recipeStore

const handleSearch = () => {
  recipeStore.filters.search = searchQuery.value
  recipeStore.searchRecipes()
}

const applyFilters = () => {
  recipeStore.filters.tags = selectedTags.value
  recipeStore.filters.ingredients = selectedIngredients.value
  recipeStore.filters.maxTime = maxTime.value
  recipeStore.filters.sortBy = sortBy.value
  recipeStore.pagination.page = 1
  recipeStore.searchRecipes()
}

const resetFilters = () => {
  selectedTags.value = []
  selectedIngredients.value = []
  maxTime.value = 60
  sortBy.value = 'relevance'
  searchQuery.value = ''
  applyFilters()
}

const changePage = (page) => {
  recipeStore.pagination.page = page
  recipeStore.searchRecipes()
}

const goToRecipe = (id) => {
  router.push(`/recipe/${id}`)
}

onMounted(() => {
  recipeStore.searchRecipes()
})
</script>

<style scoped>
.search-page {
  max-width: 1400px;
  margin: 0 auto;
  padding: 2rem;
}

.search-layout {
  display: flex;
  gap: 2rem;
  margin-top: 2rem;
}

.filters-sidebar {
  width: 280px;
  flex-shrink: 0;
  background: white;
  padding: 1.5rem;
  border-radius: 16px;
  position: sticky;
  top: 80px;
  height: fit-content;
}

.filter-group {
  margin-bottom: 1.5rem;
}

.filter-group h4 {
  margin-bottom: 0.75rem;
  color: #333;
}

.filter-group select[multiple] {
  width: 100%;
  min-height: 120px;
  padding: 0.5rem;
  border: 1px solid #e0e0e0;
  border-radius: 8px;
}

.apply-filters-btn, .reset-filters-btn {
  width: 100%;
  padding: 0.75rem;
  margin-top: 0.5rem;
  border-radius: 8px;
  cursor: pointer;
}

.apply-filters-btn {
  background: linear-gradient(135deg, #ff6b6b, #ff8e53);
  color: white;
  border: none;
}

.reset-filters-btn {
  background: none;
  border: 1px solid #ddd;
  color: #666;
}

.search-results {
  flex: 1;
}

.results-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 1.5rem;
  padding: 1rem;
  background: white;
  border-radius: 12px;
}

.recipes-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(300px, 1fr));
  gap: 1.5rem;
  margin-bottom: 2rem;
}

@media (max-width: 768px) {
  .search-layout {
    flex-direction: column;
  }
  
  .filters-sidebar {
    width: 100%;
    position: static;
  }
}
</style>