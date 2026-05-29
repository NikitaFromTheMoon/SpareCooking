<template>
  <div class="pagination">
    <button 
      :disabled="current === 1" 
      @click="$emit('page-change', current - 1)"
      class="page-btn"
    >
      ←
    </button>
    
    <button 
      v-for="page in visiblePages" 
      :key="page"
      class="page-btn"
      :class="{ active: page === current }"
      @click="$emit('page-change', page)"
    >
      {{ page }}
    </button>
    
    <button 
      :disabled="current === totalPages" 
      @click="$emit('page-change', current + 1)"
      class="page-btn"
    >
      →
    </button>
  </div>
</template>

<script setup>
import { computed } from 'vue'

const props = defineProps({
  current: { type: Number, default: 1 },
  total: { type: Number, default: 0 },
  limit: { type: Number, default: 12 }
})

const emit = defineEmits(['page-change'])

const totalPages = computed(() => Math.ceil(props.total / props.limit))

const visiblePages = computed(() => {
  const pages = []
  const maxVisible = 5
  let start = Math.max(1, props.current - Math.floor(maxVisible / 2))
  let end = Math.min(totalPages.value, start + maxVisible - 1)
  
  if (end - start + 1 < maxVisible) {
    start = Math.max(1, end - maxVisible + 1)
  }
  
  for (let i = start; i <= end; i++) {
    pages.push(i)
  }
  return pages
})
</script>

<style scoped>
.pagination {
  display: flex;
  justify-content: center;
  gap: 0.5rem;
  margin-top: 2rem;
}

.page-btn {
  padding: 0.5rem 1rem;
  border: 1px solid #e0e0e0;
  background: white;
  border-radius: 8px;
  cursor: pointer;
  transition: all 0.3s;
}

.page-btn:hover:not(:disabled) {
  background: #fff5e6;
  border-color: #ff6b6b;
}

.page-btn.active {
  background: linear-gradient(135deg, #ff6b6b, #ff8e53);
  color: white;
  border-color: transparent;
}

.page-btn:disabled {
  opacity: 0.5;
  cursor: not-allowed;
}
</style>