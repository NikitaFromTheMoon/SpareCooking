<template>
  <div class="comments-section">
    <h3>💬 Комментарии ({{ comments.length }})</h3>
    
    <!-- Форма комментария -->
    <div v-if="authStore.isAuthenticated" class="comment-form">
      <div class="rating-input">
        <span>Оценка:</span>
        <span 
          v-for="star in 5" 
          :key="star" 
          class="rating-star" 
          :class="{ active: star <= newComment.rating }"
          @click="newComment.rating = star"
        >
          ★
        </span>
      </div>
      <textarea v-model="newComment.text" rows="3" placeholder="Ваш комментарий..."></textarea>
      <button @click="submitComment">Отправить</button>
    </div>
    
    <div v-else class="login-prompt">
      <button @click="$keycloak.login()">Войдите</button>, чтобы оставить комментарий
    </div>
    
    <!-- Список комментариев -->
    <div class="comments-list">
      <CommentItem 
        v-for="comment in comments" 
        :key="comment.id"
        :comment="comment"
        :canDelete="canDeleteComment(comment)"
        @delete="deleteComment(comment.id)"
        @report="reportComment(comment.id)"
      />
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { useAuthStore } from '../../stores/authStore'
import api from '../../services/api'
import CommentItem from './CommentItem.vue'

const props = defineProps({
  comments: Array,
  recipeId: Number
})

const emit = defineEmits(['comment-deleted'])

const authStore = useAuthStore()
const newComment = ref({ rating: 5, text: '' })

const submitComment = async () => {
  if (!newComment.value.text.trim()) return
  
  try {
    await api.post(`/recipes/${props.recipeId}/comments`, newComment.value)
    newComment.value = { rating: 5, text: '' }
    emit('comment-deleted')
  } catch (error) {
    console.error('Ошибка отправки комментария:', error)
  }
}

const canDeleteComment = (comment) => {
  return authStore.isAdmin || comment.author.id === authStore.user?.id
}

const deleteComment = async (commentId) => {
  if (confirm('Удалить комментарий?')) {
    await api.delete(`/comments/${commentId}`)
    emit('comment-deleted')
  }
}

const reportComment = async (commentId) => {
  const reason = prompt('Опишите причину жалобы:')
  if (reason) {
    await api.post(`/comments/${commentId}/report`, { reason })
    alert('Жалоба отправлена')
  }
}
</script>