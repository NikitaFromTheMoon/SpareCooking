<template>
  <div class="ingredient-input">
    <div v-for="(ing, idx) in modelValue" :key="idx" class="ingredient-row">
      <select v-model="ing.name" @change="updateIngredient(idx, 'name', $event.target.value)">
        <option v-for="available in availableIngredients" :key="available" :value="available">
          {{ available }}
        </option>
      </select>
      
      <input 
        type="text" 
        v-model="ing.amount" 
        placeholder="Количество"
        @input="updateIngredient(idx, 'amount', $event.target.value)"
      />
      
      <input 
        type="text" 
        v-model="ing.note" 
        placeholder="Примечание (опционально)"
        @input="updateIngredient(idx, 'note', $event.target.value)"
      />
      
      <button type="button" @click="removeIngredient(idx)" class="remove-btn">✕</button>
    </div>
    
    <button type="button" @click="addIngredient" class="add-btn">
      + Добавить ингредиент
    </button>
    
    <button type="button" @click="showNewIngredientModal" class="add-custom-btn">
      + Новый ингредиент (предложить)
    </button>
  </div>
</template>

<script setup>
const props = defineProps({
  modelValue: Array,
  availableIngredients: Array
})

const emit = defineEmits(['update:modelValue', 'addIngredient'])

const addIngredient = () => {
  const newList = [...props.modelValue, { name: '', amount: '', note: '' }]
  emit('update:modelValue', newList)
}

const removeIngredient = (index) => {
  const newList = props.modelValue.filter((_, i) => i !== index)
  emit('update:modelValue', newList)
}

const updateIngredient = (index, field, value) => {
  const newList = [...props.modelValue]
  newList[index][field] = value
  emit('update:modelValue', newList)
}

const showNewIngredientModal = () => {
  const name = prompt('Введите название нового ингредиента:')
  if (name) {
    emit('addIngredient', name)
  }
}
</script>

<style scoped>
.ingredient-input {
  display: flex;
  flex-direction: column;
  gap: 0.75rem;
}

.ingredient-row {
  display: grid;
  grid-template-columns: 2fr 1fr 2fr auto;
  gap: 0.5rem;
  align-items: center;
}

.ingredient-row select,
.ingredient-row input {
  padding: 0.5rem;
  border: 1px solid #e0e0e0;
  border-radius: 8px;
}

.remove-btn {
  background: #ff6b6b;
  color: white;
  border: none;
  width: 32px;
  height: 32px;
  border-radius: 8px;
  cursor: pointer;
}

.add-btn, .add-custom-btn {
  padding: 0.5rem;
  background: none;
  border: 1px solid #ff6b6b;
  border-radius: 8px;
  color: #ff6b6b;
  cursor: pointer;
  margin-top: 0.5rem;
}

.add-custom-btn {
  border-color: #4caf50;
  color: #4caf50;
}
</style>