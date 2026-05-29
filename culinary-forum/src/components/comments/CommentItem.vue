<template>
  <div class="comment-card">
    <div class="comment-header">
      <span class="comment-author">{{ comment.author?.username || 'Пользователь' }}</span>
      <span class="comment-rating">{{ '⭐'.repeat(comment.rating) }}</span>
    </div>
    <div class="comment-text">{{ comment.text }}</div>
    <div class="comment-date">{{ formatDate(comment.date) }}</div>
    <div class="comment-actions">
      <button v-if="canDelete" @click="$emit('delete')" class="delete-comment-btn">🗑️</button>
      <button @click="$emit('report')" class="report-comment-btn">🚨</button>
    </div>
  </div>
</template>

<script setup>
const props = defineProps({
  comment: Object,
  canDelete: Boolean
})

const emit = defineEmits(['delete', 'report'])

const formatDate = (date) => {
  if (!date) return 'Недавно'
  return new Date(date).toLocaleDateString('ru-RU')
}
</script>

<style scoped>
.comment-card {
  background: white;
  border: 1px solid #e0e0e0;
  border-radius: 12px;
  padding: 1rem;
}

.comment-header {
  display: flex;
  justify-content: space-between;
  margin-bottom: 0.5rem;
  padding-bottom: 0.5rem;
  border-bottom: 1px solid #f0f0f0;
}

.comment-author {
  font-weight: 600;
  color: #ff6b6b;
}

.comment-rating {
  color: #ffa500;
}

.comment-text {
  color: #555;
  line-height: 1.4;
  margin-bottom: 0.5rem;
}

.comment-date {
  font-size: 0.8rem;
  color: #999;
}

.comment-actions {
  display: flex;
  gap: 0.5rem;
  margin-top: 0.5rem;
  justify-content: flex-end;
}

.delete-comment-btn, .report-comment-btn {
  background: none;
  border: none;
  cursor: pointer;
  font-size: 1rem;
  opacity: 0.5;
  transition: opacity 0.2s;
}

.delete-comment-btn:hover {
  opacity: 1;
  color: #f44336;
}

.report-comment-btn:hover {
  opacity: 1;
  color: #ff9800;
}
</style>