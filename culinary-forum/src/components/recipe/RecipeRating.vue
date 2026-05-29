<template>
  <div class="recipe-rating">
    <div class="stars">
      <span 
        v-for="star in 5" 
        :key="star"
        class="star"
        :class="{ active: star <= (userRating || rating) }"
        @click="rate(star)"
      >
        ★
      </span>
    </div>
    <span class="rating-value">{{ rating?.toFixed(1) || 0 }}</span>
    <span class="rating-count" v-if="ratingCount">({{ ratingCount }} оценок)</span>
  </div>
</template>

<script setup>
const props = defineProps({
  rating: Number,
  userRating: Number,
  ratingCount: Number,
  recipeId: [String, Number]
})

const emit = defineEmits(['rate'])

const rate = (value) => {
  emit('rate', value)
}
</script>

<style scoped>
.recipe-rating {
  display: flex;
  align-items: center;
  gap: 0.5rem;
}

.stars {
  display: flex;
  gap: 0.25rem;
}

.star {
  font-size: 1.25rem;
  color: #ddd;
  cursor: pointer;
  transition: color 0.2s;
}

.star.active {
  color: #ffa500;
}

.rating-value {
  font-weight: 600;
  color: #333;
}

.rating-count {
  color: #999;
  font-size: 0.85rem;
}
</style>