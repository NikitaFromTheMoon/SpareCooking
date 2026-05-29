<template>
  <div class="ingredient-calculator">
    <div class="calculator-control">
      <label>Количество порций:</label>
      <input 
        type="number" 
        v-model.number="portions" 
        min="0.5" 
        step="0.5"
        @change="recalculate"
      />
      <span> (исходно: {{ originalPortions }})</span>
    </div>
    
    <ul class="ingredients-list-calculated">
      <li v-for="ing in calculatedIngredients" :key="ing.name">
        <strong>{{ ing.name }}</strong>
        <span>{{ formatAmount(ing.amount, ing.note) }}</span>
        <em v-if="ing.note">{{ ing.note }}</em>
      </li>
    </ul>
  </div>
</template>

<script setup>
import { ref, computed } from 'vue'

const props = defineProps({
  ingredients: Array,
  originalPortions: Number
})

const portions = ref(props.originalPortions)

const calculatedIngredients = computed(() => {
  const ratio = portions.value / props.originalPortions
  
  return props.ingredients.map(ing => ({
    ...ing,
    amount: parseAmount(ing.amount) * ratio
  }))
})

const parseAmount = (amountStr) => {
  // Парсинг строки типа "2 шт", "150 г", "1/2 стакана"
  const match = amountStr.match(/^(\d+(?:\.\d+)?(?:\/\d+)?)/)
  if (match) {
    const fraction = match[1]
    if (fraction.includes('/')) {
      const [num, den] = fraction.split('/')
      return parseFloat(num) / parseFloat(den)
    }
    return parseFloat(fraction)
  }
  return 1
}

const formatAmount = (amount, originalNote) => {
  const rounded = Math.round(amount * 10) / 10
  const unit = originalNote?.match(/(г|кг|мл|л|шт|стакан|ложка)/)?.[0] || ''
  return `${rounded} ${unit}`
}

const recalculate = () => {
  // Триггер пересчета
}
</script>

<style scoped>
.ingredient-calculator {
  background: #f9f9f9;
  padding: 1rem;
  border-radius: 12px;
  margin: 1rem 0;
}

.calculator-control {
  margin-bottom: 1rem;
  padding-bottom: 1rem;
  border-bottom: 1px solid #e0e0e0;
}

.calculator-control input {
  width: 80px;
  padding: 0.5rem;
  margin: 0 0.5rem;
  border: 1px solid #e0e0e0;
  border-radius: 8px;
}

.ingredients-list-calculated li {
  display: flex;
  justify-content: space-between;
  padding: 0.5rem 0;
  border-bottom: 1px solid #eee;
}
</style>