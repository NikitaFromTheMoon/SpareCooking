<template>
  <div class="recipe-card">
    <div class="card-image">
      <img :src="recipe.images?.[0] || '/placeholder.jpg'" :alt="recipe.title" />
      <span class="category-badge">{{ recipe.category || 'Рецепт' }}</span>
    </div>
    <div class="card-content">
      <h3>{{ recipe.title }}</h3>
      <p class="description">{{ truncate(recipe.description, 100) }}</p>
      <div class="card-footer">
        <div class="stats">
          <span>⏱️ {{ recipe.cookingTime }} мин</span>
          <span>👤 {{ recipe.author?.username || 'Автор' }}</span>
        </div>
        <div class="rating">
          {{ getRatingStars() }}
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
const props = defineProps({
  recipe: Object
})

const getRatingStars = () => {
  const rating = props.recipe.rating || 0
  const fullStars = Math.round(rating)
  return '⭐'.repeat(fullStars) + (rating > 0 ? ` ${rating.toFixed(1)}` : '')
}

const truncate = (text, length) => {
  if (!text) return ''
  if (text.length <= length) return text
  return text.substring(0, length) + '...'
}
</script>

<style scoped>
.recipe-card {
  background: white;
  border-radius: 16px;
  overflow: hidden;
  cursor: pointer;
  transition: transform 0.3s, box-shadow 0.3s;
  box-shadow: 0 4px 12px rgba(0,0,0,0.08);
}

.recipe-card:hover {
  transform: translateY(-4px);
  box-shadow: 0 8px 24px rgba(0,0,0,0.12);
}

.card-image {
  position: relative;
  height: 200px;
  overflow: hidden;
}

.card-image img {
  width: 100%;
  height: 100%;
  object-fit: cover;
  transition: transform 0.3s;
}

.recipe-card:hover .card-image img {
  transform: scale(1.05);
}

.category-badge {
  position: absolute;
  top: 1rem;
  right: 1rem;
  background: rgba(255,255,255,0.95);
  padding: 0.25rem 0.75rem;
  border-radius: 20px;
  font-size: 0.85rem;
  font-weight: 500;
  color: #ff6b6b;
}

.card-content {
  padding: 1.25rem;
}

.card-content h3 {
  font-size: 1.25rem;
  margin-bottom: 0.5rem;
  color: #333;
}

.description {
  color: #666;
  font-size: 0.9rem;
  line-height: 1.4;
  margin-bottom: 1rem;
}

.card-footer {
  display: flex;
  justify-content: space-between;
  align-items: center;
  font-size: 0.85rem;
  color: #888;
}

.stats {
  display: flex;
  gap: 1rem;
}

.rating {
  color: #ffa500;
}
</style>