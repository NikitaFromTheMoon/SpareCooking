<template>
  <div class="ingredients-manager">
    <div class="manager-header">
      <h3>🥬 Управление ингредиентами</h3>
      <div class="add-form">
        <input 
          v-model="newIngredient" 
          placeholder="Название нового ингредиента"
          @keyup.enter="addIngredient"
        />
        <button @click="addIngredient" :disabled="!newIngredient.trim()">Добавить</button>
      </div>
    </div>
    
    <div class="ingredients-stats">
      <div class="stat-card">
        <div class="stat-value">{{ allIngredients.length }}</div>
        <div class="stat-label">Всего ингредиентов</div>
      </div>
      <div class="stat-card">
        <div class="stat-value">{{ popularIngredients.length }}</div>
        <div class="stat-label">Популярных</div>
      </div>
      <div class="stat-card">
        <div class="stat-value">{{ categories.length }}</div>
        <div class="stat-label">Категорий</div>
      </div>
    </div>
    
    <div class="search-box">
      <input 
        v-model="searchQuery" 
        placeholder="Поиск ингредиентов..."
        class="search-input"
      />
    </div>
    
    <div class="ingredients-list">
      <div v-for="ingredient in filteredIngredients" :key="ingredient.id" class="ingredient-item">
        <div class="ingredient-info">
          <span class="ingredient-name">{{ ingredient.name }}</span>
          <span class="ingredient-category" :style="{ background: getCategoryColor(ingredient.category) }">
            {{ ingredient.category || 'Основные' }}
          </span>
          <span class="ingredient-usage">📊 {{ ingredient.usageCount || 0 }} рецептов</span>
        </div>
        <div class="ingredient-actions">
          <select v-model="ingredient.category" @change="updateCategory(ingredient)" class="category-select">
            <option value="vegetables">Овощи</option>
            <option value="fruits">Фрукты</option>
            <option value="meat">Мясо</option>
            <option value="fish">Рыба</option>
            <option value="dairy">Молочные</option>
            <option value="grains">Крупы</option>
            <option value="spices">Специи</option>
            <option value="other">Другое</option>
          </select>
          <button @click="editIngredient(ingredient)" class="edit-btn">✏️</button>
          <button @click="deleteIngredient(ingredient.id)" class="delete-btn">🗑️</button>
        </div>
      </div>
    </div>
    
    <div v-if="showSimilar" class="similar-ingredients">
      <h4>Возможно, вы искали:</h4>
      <div class="similar-list">
        <span 
          v-for="similar in similarIngredients" 
          :key="similar"
          class="similar-tag"
          @click="newIngredient = similar"
        >
          {{ similar }}
        </span>
      </div>
    </div>
    
    <!-- Модальное окно редактирования -->
    <div v-if="editingIngredient" class="modal" @click.self="editingIngredient = null">
      <div class="modal-content">
        <h3>Редактирование ингредиента</h3>
        <input v-model="editingIngredient.name" placeholder="Название" />
        <select v-model="editingIngredient.category">
          <option value="vegetables">Овощи</option>
          <option value="fruits">Фрукты</option>
          <option value="meat">Мясо</option>
          <option value="fish">Рыба</option>
          <option value="dairy">Молочные</option>
          <option value="grains">Крупы</option>
          <option value="spices">Специи</option>
          <option value="other">Другое</option>
        </select>
        <input v-model="editingIngredient.unit" placeholder="Единица измерения (г, мл, шт)" />
        <div class="modal-actions">
          <button @click="saveIngredientEdit" class="save-btn">Сохранить</button>
          <button @click="editingIngredient = null" class="cancel-btn">Отмена</button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useAdminStore } from '../../stores/adminStore'

const adminStore = useAdminStore()
const newIngredient = ref('')
const searchQuery = ref('')
const editingIngredient = ref(null)
const showSimilar = ref(false)
const similarIngredients = ref([])

const allIngredients = computed(() => adminStore.allIngredients)
const popularIngredients = computed(() => allIngredients.value.filter(i => i.isPopular))
const categories = computed(() => [...new Set(allIngredients.value.map(i => i.category).filter(Boolean))])

const filteredIngredients = computed(() => {
  if (!searchQuery.value) return allIngredients.value
  const query = searchQuery.value.toLowerCase()
  return allIngredients.value.filter(i => 
    i.name.toLowerCase().includes(query)
  )
})

const getCategoryColor = (category) => {
  const colors = {
    vegetables: '#4caf50',
    fruits: '#ff9800',
    meat: '#f44336',
    fish: '#2196f3',
    dairy: '#9c27b0',
    grains: '#795548',
    spices: '#607d8b',
    other: '#999'
  }
  return colors[category] || '#999'
}

const addIngredient = async () => {
  if (newIngredient.value.trim()) {
    // Проверка на похожие ингредиенты
    const similar = allIngredients.value.filter(i => 
      i.name.toLowerCase().includes(newIngredient.value.toLowerCase()) ||
      newIngredient.value.toLowerCase().includes(i.name.toLowerCase())
    )
    
    if (similar.length > 0) {
      similarIngredients.value = similar.map(i => i.name)
      showSimilar.value = true
      setTimeout(() => {
        showSimilar.value = false
      }, 3000)
      return
    }
    
    await adminStore.addIngredient(newIngredient.value.trim())
    newIngredient.value = ''
  }
}

const editIngredient = (ingredient) => {
  editingIngredient.value = { ...ingredient }
}

const saveIngredientEdit = async () => {
  if (editingIngredient.value.name.trim()) {
    await adminStore.updateIngredient(
      editingIngredient.value.id, 
      editingIngredient.value.name,
      editingIngredient.value.category,
      editingIngredient.value.unit
    )
    editingIngredient.value = null
  }
}

const deleteIngredient = async (ingredientId) => {
  if (confirm('Удалить этот ингредиент? Он будет удалён из всех рецептов.')) {
    await adminStore.deleteIngredient(ingredientId)
  }
}

const updateCategory = async (ingredient) => {
  await adminStore.updateIngredientCategory(ingredient.id, ingredient.category)
}

onMounted(async () => {
  await adminStore.getIngredients()
})
</script>

<style scoped>
.ingredients-manager {
  background: white;
  border-radius: 16px;
  padding: 1.5rem;
}

.manager-header {
  margin-bottom: 1.5rem;
}

.manager-header h3 {
  margin-bottom: 1rem;
}

.add-form {
  display: flex;
  gap: 1rem;
}

.add-form input {
  flex: 1;
  padding: 0.75rem;
  border: 1px solid #e0e0e0;
  border-radius: 8px;
}

.add-form button {
  padding: 0.75rem 1.5rem;
  background: linear-gradient(135deg, #ff6b6b, #ff8e53);
  color: white;
  border: none;
  border-radius: 8px;
  cursor: pointer;
}

.ingredients-stats {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 1rem;
  margin-bottom: 1.5rem;
}

.stat-card {
  background: #f9f9f9;
  padding: 1rem;
  border-radius: 12px;
  text-align: center;
}

.stat-value {
  font-size: 1.5rem;
  font-weight: 600;
  color: #ff6b6b;
}

.stat-label {
  font-size: 0.8rem;
  color: #666;
  margin-top: 0.25rem;
}

.search-box {
  margin-bottom: 1.5rem;
}

.search-input {
  width: 100%;
  padding: 0.75rem;
  border: 1px solid #e0e0e0;
  border-radius: 8px;
}

.ingredients-list {
  display: flex;
  flex-direction: column;
  gap: 0.75rem;
  max-height: 500px;
  overflow-y: auto;
}

.ingredient-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 0.75rem;
  background: #f9f9f9;
  border-radius: 12px;
  transition: background 0.2s;
}

.ingredient-item:hover {
  background: #fff5e6;
}

.ingredient-info {
  display: flex;
  gap: 1rem;
  align-items: center;
  flex-wrap: wrap;
}

.ingredient-name {
  font-weight: 600;
  color: #333;
}

.ingredient-category {
  padding: 0.25rem 0.5rem;
  border-radius: 20px;
  font-size: 0.7rem;
  color: white;
}

.ingredient-usage {
  font-size: 0.8rem;
  color: #999;
}

.ingredient-actions {
  display: flex;
  gap: 0.5rem;
  align-items: center;
}

.category-select {
  padding: 0.25rem 0.5rem;
  border: 1px solid #e0e0e0;
  border-radius: 6px;
  font-size: 0.8rem;
}

.edit-btn, .delete-btn {
  padding: 0.25rem 0.75rem;
  border: none;
  border-radius: 6px;
  cursor: pointer;
}

.edit-btn {
  background: #2196f3;
  color: white;
}

.delete-btn {
  background: #f44336;
  color: white;
}

.similar-ingredients {
  margin-top: 1rem;
  padding: 1rem;
  background: #fff5e6;
  border-radius: 12px;
}

.similar-ingredients h4 {
  margin-bottom: 0.5rem;
  font-size: 0.9rem;
}

.similar-list {
  display: flex;
  flex-wrap: wrap;
  gap: 0.5rem;
}

.similar-tag {
  padding: 0.25rem 0.75rem;
  background: white;
  border-radius: 20px;
  font-size: 0.85rem;
  cursor: pointer;
  transition: background 0.2s;
}

.similar-tag:hover {
  background: #ff6b6b;
  color: white;
}

.modal {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: rgba(0,0,0,0.5);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 1000;
}

.modal-content {
  background: white;
  padding: 2rem;
  border-radius: 16px;
  width: 400px;
  max-width: 90%;
}

.modal-content h3 {
  margin-bottom: 1rem;
}

.modal-content input,
.modal-content select {
  width: 100%;
  padding: 0.75rem;
  border: 1px solid #e0e0e0;
  border-radius: 8px;
  margin-bottom: 1rem;
}

.modal-actions {
  display: flex;
  gap: 1rem;
}

.save-btn, .cancel-btn {
  flex: 1;
  padding: 0.75rem;
  border: none;
  border-radius: 8px;
  cursor: pointer;
}

.save-btn {
  background: #4caf50;
  color: white;
}

.cancel-btn {
  background: #e0e0e0;
  color: #666;
}
</style>