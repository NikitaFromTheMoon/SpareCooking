<template>
  <div class="search-bar">
    <div class="search-input-wrapper">
      <input
        type="text"
        v-model="searchQuery"
        :placeholder="placeholder"
        @input="onInput"
        @keyup.enter="onSearch"
        @focus="showSuggestions = true"
        @blur="handleBlur"
        class="search-input"
      />
      <button @click="onSearch" class="search-button">
        🔍
      </button>
    </div>
    
    <!-- Подсказки поиска -->
    <div v-if="showSuggestions && (searchHistory.length || suggestions.length)" class="suggestions-dropdown">
      <div v-if="searchHistory.length" class="suggestions-section">
        <div class="suggestions-header">История поиска</div>
        <div 
          v-for="item in searchHistory" 
          :key="item"
          class="suggestion-item"
          @mousedown.prevent="selectSuggestion(item)"
        >
          <span class="history-icon">🕐</span>
          <span>{{ item }}</span>
          <button @click.stop="removeFromHistory(item)" class="remove-history">✕</button>
        </div>
        <button @click="clearHistory" class="clear-history">Очистить историю</button>
      </div>
      
      <div v-if="suggestions.length" class="suggestions-section">
        <div class="suggestions-header">Популярные запросы</div>
        <div 
          v-for="suggestion in suggestions" 
          :key="suggestion"
          class="suggestion-item"
          @mousedown.prevent="selectSuggestion(suggestion)"
        >
          <span class="trending-icon">🔥</span>
          <span>{{ suggestion }}</span>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, watch, onMounted } from 'vue'
import { useUserStore } from '../../stores/userStore'

const props = defineProps({
  modelValue: String,
  placeholder: {
    type: String,
    default: 'Поиск рецептов...'
  }
})

const emit = defineEmits(['update:modelValue', 'search'])

const userStore = useUserStore()
const searchQuery = ref(props.modelValue)
const showSuggestions = ref(false)
const suggestions = ref(['борщ', 'пицца', 'салат', 'суп', 'десерт', 'паста'])

const onInput = () => {
  emit('update:modelValue', searchQuery.value)
}

const onSearch = () => {
  if (searchQuery.value.trim()) {
    userStore.addToSearchHistory(searchQuery.value.trim())
    emit('search', searchQuery.value)
  }
  showSuggestions.value = false
}

const selectSuggestion = (suggestion) => {
  searchQuery.value = suggestion
  emit('update:modelValue', suggestion)
  onSearch()
}

const removeFromHistory = (item) => {
  const history = userStore.searchHistory.filter(h => h !== item)
  userStore.searchHistory = history
  localStorage.setItem('searchHistory', JSON.stringify(history))
}

const clearHistory = () => {
  userStore.clearSearchHistory()
}

const handleBlur = () => {
  setTimeout(() => {
    showSuggestions.value = false
  }, 200)
}

watch(() => props.modelValue, (newVal) => {
  if (newVal !== searchQuery.value) {
    searchQuery.value = newVal
  }
})

onMounted(() => {
  userStore.loadSearchHistory()
})
</script>

<style scoped>
.search-bar {
  position: relative;
  width: 100%;
  max-width: 500px;
}

.search-input-wrapper {
  display: flex;
  align-items: center;
  background: white;
  border: 1px solid #e0e0e0;
  border-radius: 40px;
  overflow: hidden;
  transition: all 0.3s;
}

.search-input-wrapper:focus-within {
  border-color: #ff6b6b;
  box-shadow: 0 0 0 3px rgba(255, 107, 107, 0.1);
}

.search-input {
  flex: 1;
  padding: 0.75rem 1rem;
  border: none;
  outline: none;
  font-size: 1rem;
}

.search-button {
  padding: 0.75rem 1.25rem;
  background: none;
  border: none;
  cursor: pointer;
  font-size: 1.1rem;
  color: #ff6b6b;
  transition: background 0.3s;
}

.search-button:hover {
  background: #fff5e6;
}

.suggestions-dropdown {
  position: absolute;
  top: 100%;
  left: 0;
  right: 0;
  background: white;
  border-radius: 16px;
  box-shadow: 0 4px 20px rgba(0,0,0,0.15);
  margin-top: 0.5rem;
  z-index: 1000;
  overflow: hidden;
}

.suggestions-section {
  padding: 0.5rem 0;
}

.suggestions-section:not(:last-child) {
  border-bottom: 1px solid #f0f0f0;
}

.suggestions-header {
  padding: 0.5rem 1rem;
  font-size: 0.8rem;
  color: #999;
  font-weight: 500;
}

.suggestion-item {
  display: flex;
  align-items: center;
  gap: 0.75rem;
  padding: 0.5rem 1rem;
  cursor: pointer;
  transition: background 0.2s;
}

.suggestion-item:hover {
  background: #fff5e6;
}

.history-icon, .trending-icon {
  font-size: 0.9rem;
  opacity: 0.7;
}

.remove-history {
  margin-left: auto;
  background: none;
  border: none;
  cursor: pointer;
  color: #ccc;
  padding: 0.25rem;
}

.remove-history:hover {
  color: #ff6b6b;
}

.clear-history {
  width: 100%;
  padding: 0.5rem;
  background: none;
  border: none;
  color: #ff6b6b;
  cursor: pointer;
  font-size: 0.85rem;
  text-align: center;
}

.clear-history:hover {
  background: #fff5e6;
}
</style>