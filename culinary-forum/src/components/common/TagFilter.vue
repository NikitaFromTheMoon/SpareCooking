<template>
  <div class="tag-filter">
    <div class="tag-filter-header">
      <span class="tag-filter-title">🏷️ Фильтр по тегам</span>
      <button v-if="selectedTags.length" @click="clearAll" class="clear-all">
        Сбросить все
      </button>
    </div>
    
    <div class="tags-cloud">
      <span
        v-for="tag in availableTags"
        :key="tag"
        class="tag"
        :class="{
          selected: isSelected(tag),
          popular: getTagPopularity(tag) > 10
        }"
        @click="toggleTag(tag)"
      >
        {{ tag }}
        <span class="tag-count" v-if="getTagCount(tag)">({{ getTagCount(tag) }})</span>
      </span>
    </div>
    
    <div v-if="selectedTags.length" class="selected-tags">
      <div class="selected-tags-title">Выбранные теги:</div>
      <div class="selected-tags-list">
        <span v-for="tag in selectedTags" :key="tag" class="selected-tag">
          {{ tag }}
          <button @click="toggleTag(tag)" class="remove-tag">✕</button>
        </span>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'

const props = defineProps({
  selectedTags: {
    type: Array,
    default: () => []
  },
  availableTags: {
    type: Array,
    default: () => []
  }
})

const emit = defineEmits(['update:selectedTags'])

const tagPopularity = ref({})
const tagCounts = ref({})

const isSelected = (tag) => {
  return props.selectedTags.includes(tag)
}

const toggleTag = (tag) => {
  const newSelected = isSelected(tag)
    ? props.selectedTags.filter(t => t !== tag)
    : [...props.selectedTags, tag]
  emit('update:selectedTags', newSelected)
}

const clearAll = () => {
  emit('update:selectedTags', [])
}

const getTagPopularity = (tag) => {
  return tagPopularity.value[tag] || 0
}

const getTagCount = (tag) => {
  return tagCounts.value[tag] || 0
}

onMounted(async () => {
  // Загрузка популярности тегов
  try {
    const response = await fetch('/api/tags/stats')
    const data = await response.json()
    tagPopularity.value = data.popularity
    tagCounts.value = data.counts
  } catch (error) {
    console.error('Ошибка загрузки статистики тегов:', error)
  }
})
</script>

<style scoped>
.tag-filter {
  background: white;
  border-radius: 16px;
  padding: 1rem;
}

.tag-filter-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 1rem;
  padding-bottom: 0.5rem;
  border-bottom: 1px solid #f0f0f0;
}

.tag-filter-title {
  font-weight: 600;
  color: #333;
}

.clear-all {
  background: none;
  border: none;
  color: #999;
  font-size: 0.8rem;
  cursor: pointer;
}

.clear-all:hover {
  color: #ff6b6b;
}

.tags-cloud {
  display: flex;
  flex-wrap: wrap;
  gap: 0.5rem;
  margin-bottom: 1rem;
}

.tag {
  display: inline-flex;
  align-items: center;
  gap: 0.25rem;
  padding: 0.25rem 0.75rem;
  background: #f5f5f5;
  border-radius: 20px;
  font-size: 0.85rem;
  cursor: pointer;
  transition: all 0.2s;
}

.tag:hover {
  background: #fff5e6;
  transform: scale(1.05);
}

.tag.selected {
  background: linear-gradient(135deg, #ff6b6b, #ff8e53);
  color: white;
}

.tag.popular {
  font-weight: 600;
  background: #ffe0cc;
}

.tag-count {
  font-size: 0.7rem;
  opacity: 0.7;
}

.selected-tags {
  margin-top: 1rem;
  padding-top: 1rem;
  border-top: 1px solid #f0f0f0;
}

.selected-tags-title {
  font-size: 0.8rem;
  color: #999;
  margin-bottom: 0.5rem;
}

.selected-tags-list {
  display: flex;
  flex-wrap: wrap;
  gap: 0.5rem;
}

.selected-tag {
  display: inline-flex;
  align-items: center;
  gap: 0.25rem;
  padding: 0.25rem 0.5rem;
  background: #ff6b6b;
  color: white;
  border-radius: 20px;
  font-size: 0.85rem;
}

.remove-tag {
  background: none;
  border: none;
  color: white;
  cursor: pointer;
  font-size: 0.8rem;
  padding: 0 0.25rem;
}

.remove-tag:hover {
  opacity: 0.8;
}
</style>