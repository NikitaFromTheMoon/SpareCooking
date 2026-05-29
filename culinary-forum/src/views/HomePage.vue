<template>
  <div class="home-page">
    <section class="hero">
      <h1>Готовьте с удовольствием</h1>
      <p>Тысячи рецептов от лучших кулинаров со всего мира</p>
      <router-link to="/search" class="explore-btn">Изучить рецепты</router-link>
    </section>
    
    <section class="featured-recipes">
      <h2>🔥 Популярные рецепты</h2>
      <div class="recipes-grid">
        <RecipeCard 
          v-for="recipe in featuredRecipes" 
          :key="recipe.id"
          :recipe="recipe"
          @click="goToRecipe(recipe.id)"
        />
      </div>
    </section>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { useRecipeStore } from '../stores/recipeStore'
import RecipeCard from '../components/recipe/RecipeCard.vue'

const router = useRouter()
const recipeStore = useRecipeStore()
const featuredRecipes = ref([])

const goToRecipe = (id) => {
  router.push(`/recipe/${id}`)
}

onMounted(async () => {
  await recipeStore.searchRecipes({ page: 1, limit: 6, sortBy: 'rating' })
  featuredRecipes.value = recipeStore.recipes
})
</script>

<style scoped>
.home-page {
  max-width: 1200px;
  margin: 0 auto;
}

.hero {
  text-align: center;
  padding: 4rem 2rem;
  background: linear-gradient(135deg, #fff5e6, #ffe0cc);
  border-radius: 24px;
  margin-bottom: 3rem;
}

.hero h1 {
  font-size: 3rem;
  color: #333;
  margin-bottom: 1rem;
}

.hero p {
  font-size: 1.2rem;
  color: #666;
  margin-bottom: 2rem;
}

.explore-btn {
  display: inline-block;
  padding: 0.75rem 2rem;
  background: linear-gradient(135deg, #ff6b6b, #ff8e53);
  color: white;
  text-decoration: none;
  border-radius: 40px;
  font-weight: 600;
  transition: transform 0.3s;
}

.explore-btn:hover {
  transform: translateY(-2px);
}

.featured-recipes h2 {
  font-size: 1.8rem;
  color: #333;
  margin-bottom: 2rem;
}

.recipes-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(320px, 1fr));
  gap: 2rem;
}

@media (max-width: 768px) {
  .hero h1 {
    font-size: 2rem;
  }
}
</style>