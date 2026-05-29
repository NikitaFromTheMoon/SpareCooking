<template>
  <div class="tags-manager">
    <div class="manager-header">
      <h3>🏷️ Управление тегами</h3>
      <div class="add-form">
        <input 
          v-model="newTag" 
          placeholder="Название нового тега"
          @keyup.enter="addTag"
        />
        <button @click="addTag" :disabled="!newTag.trim()">Добавить</button>
      </div>
    </div>
    
    <div class="tags-stats">
      <div class="stat-card">
        <div class="stat-value">{{ allTags.length }}</div>
        <div class="stat-label">Всего тегов</div>
      </div>
      <div class="stat-card">
        <div class="stat-value">{{ popularTags.length }}</div>
        <div class="stat-label">Популярных тегов</div>
      </div>
      <div class="stat-card">
        <div class="stat-value">{{ getTotalUsageCount }}</div>
        <div class="stat-label">Всего использований</div>
      </div>
    </div>
    
    <div class="tags-list">
      <div v-for="tag in paginatedTags" :key="tag.id" class="tag-item">
        <div class="tag-info">
          <span class="tag-name">#{{ tag.name }}</span>
          <span class="tag-usage">📊 {{ tag.usageCount || 0 }} рецептов</span>
          <span class="tag-created">📅 {{ formatDate(tag.createdAt) }}</span>
        </div>
        <div class="tag-actions">
          <button v-if="!tag.isPopular" @click="makePopular(tag)" class="popular-btn">
            🔥 Сделать популярным
          </button>
          <button @click="editTag(tag)" class="edit-btn">✏️</button>
          <button @click="deleteTag(tag.id)" class="delete-btn">🗑️</button>
        </div>
      </div>
    </div>
    
    <Pagination 
      v-if="totalPages > 1"
      :current="currentPage"
      :total="allTags.length"
      :limit="itemsPerPage"
      @page-change="currentPage = $event"
    />
    
    <!-- Модальное окно редактирования -->
    <div v-if="editingTag" class="modal" @click.self="editingTag = null">
      <div class="modal-content">
        <h3>Редактирование тега</h3>
        <input v-model="editingTag.name" placeholder="Название тега" />
        <div class="modal-actions">
          <button @click="saveTagEdit" class="save-btn">Сохранить</button>
          <button @click="editingTag = null" class="cancel-btn">Отмена</button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useAdminStore } from '../../stores/adminStore'
import Pagination from '../common/Pagination.vue'

const adminStore = useAdminStore()
const newTag = ref('')
const editingTag = ref(null)
const currentPage = ref(1)
const itemsPerPage = 20

const allTags = computed(() => adminStore.allTags)
const popularTags = computed(() => allTags.value.filter(t => t.isPopular))
const getTotalUsageCount = computed(() => allTags.value.reduce((sum, t) => sum + (t.usageCount || 0), 0))
const totalPages = computed(() => Math.ceil(allTags.value.length / itemsPerPage))
const paginatedTags = computed(() => {
  const start = (currentPage.value - 1) * itemsPerPage
  return allTags.value.slice(start, start + itemsPerPage)
})

const addTag = async () => {
  if (newTag.value.trim()) {
    await adminStore.addTag(newTag.value.trim())
    newTag.value = ''
  }
}

const editTag = (tag) => {
  editingTag.value = { ...tag }
}

const saveTagEdit = async () => {
  if (editingTag.value.name.trim()) {
    await adminStore.updateTag(editingTag.value.id, editingTag.value.name)
    editingTag.value = null
  }
}

const deleteTag = async (tagId) => {
  if (confirm('Удалить этот тег? Он будет удалён из всех рецептов.')) {
    await adminStore.deleteTag(tagId)
  }
}

const makePopular = async (tag) => {
  await adminStore.makeTagPopular(tag.id)
}

const formatDate = (date) => {
  if (!date) return '—'
  return new Date(date).toLocaleDateString('ru-RU')
}

onMounted(async () => {
  await adminStore.getTags()
})
</script>

<style scoped>
.tags-manager {
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

.tags-stats {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 1rem;
  margin-bottom: 2rem;
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

.tags-list {
  display: flex;
  flex-direction: column;
  gap: 0.75rem;
}

.tag-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 0.75rem;
  background: #f9f9f9;
  border-radius: 12px;
  transition: background 0.2s;
}

.tag-item:hover {
  background: #fff5e6;
}

.tag-info {
  display: flex;
  gap: 1.5rem;
  align-items: center;
  flex-wrap: wrap;
}

.tag-name {
  font-weight: 600;
  color: #ff6b6b;
}

.tag-usage, .tag-created {
  font-size: 0.8rem;
  color: #999;
}

.tag-actions {
  display: flex;
  gap: 0.5rem;
}

.popular-btn, .edit-btn, .delete-btn {
  padding: 0.25rem 0.75rem;
  border: none;
  border-radius: 6px;
  cursor: pointer;
}

.popular-btn {
  background: #ff9800;
  color: white;
}

.edit-btn {
  background: #2196f3;
  color: white;
}

.delete-btn {
  background: #f44336;
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

.modal-content input {
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