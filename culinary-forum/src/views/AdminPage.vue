<template>
  <div class="admin-page">
    <h1>👑 Панель администратора</h1>
    
    <div class="admin-tabs">
      <button :class="{ active: activeTab === 'reports' }" @click="activeTab = 'reports'">
        📋 Жалобы ({{ pendingReportsCount }})
      </button>
      <button :class="{ active: activeTab === 'tags' }" @click="activeTab = 'tags'">
        🏷️ Управление тегами
      </button>
      <button :class="{ active: activeTab === 'ingredients' }" @click="activeTab = 'ingredients'">
        🥬 Управление ингредиентами
      </button>
      <button :class="{ active: activeTab === 'users' }" @click="activeTab = 'users'">
        👥 Пользователи
      </button>
    </div>
    
    <!-- Жалобы -->
    <div v-if="activeTab === 'reports'" class="reports-list">
      <div v-for="report in reports" :key="report.id" class="report-card">
        <div class="report-header">
          <span>{{ report.type === 'recipe' ? '📖 Рецепт' : '💬 Комментарий' }}</span>
          <span :class="report.status">{{ report.status === 'pending' ? '⏳ На рассмотрении' : '✅ Закрыта' }}</span>
        </div>
        <div class="report-content">
          <p><strong>От:</strong> {{ report.reporterName }}</p>
          <p><strong>Причина:</strong> {{ report.reason }}</p>
          <div class="reported-content" v-html="report.content"></div>
        </div>
        <div v-if="report.status === 'pending'" class="report-actions">
          <button @click="resolveReport(report.id, 'dismiss')">Отклонить</button>
          <button @click="resolveReport(report.id, 'delete')">Удалить контент</button>
        </div>
      </div>
    </div>
    
    <!-- Управление тегами -->
    <div v-if="activeTab === 'tags'" class="tags-manager">
      <div class="add-form">
        <input v-model="newTag" placeholder="Новый тег" />
        <button @click="addTag">Добавить</button>
      </div>
      <div class="tags-list">
        <div v-for="tag in allTags" :key="tag.id" class="tag-item">
          <span>{{ tag.name }}</span>
          <button @click="deleteTag(tag.id)">🗑️</button>
        </div>
      </div>
    </div>
    
    <!-- Управление ингредиентами -->
    <div v-if="activeTab === 'ingredients'" class="ingredients-manager">
      <div class="add-form">
        <input v-model="newIngredient" placeholder="Новый ингредиент" />
        <button @click="addIngredient">Добавить</button>
      </div>
      <div class="ingredients-list">
        <div v-for="ing in allIngredients" :key="ing.id" class="ingredient-item">
          <span>{{ ing.name }}</span>
          <button @click="deleteIngredient(ing.id)">🗑️</button>
        </div>
      </div>
    </div>
    
    <!-- Пользователи -->
    <div v-if="activeTab === 'users'" class="users-list">
      <div v-for="user in users" :key="user.id" class="user-card">
        <div class="user-avatar">{{ user.username?.[0]?.toUpperCase() }}</div>
        <div class="user-info">
          <strong>{{ user.username }}</strong>
          <span>{{ user.email }}</span>
          <span>Рецептов: {{ user.recipesCount || 0 }}</span>
        </div>
        <div class="user-actions">
          <button v-if="!user.banned" @click="banUser(user)">⛔ Забанить</button>
          <button @click="deleteUser(user)">🗑️ Удалить</button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useAdminStore } from '../stores/adminStore'

const adminStore = useAdminStore()
const activeTab = ref('reports')
const newTag = ref('')
const newIngredient = ref('')

const reports = ref([])
const allTags = ref([])
const allIngredients = ref([])
const users = ref([])

const pendingReportsCount = computed(() => {
  return reports.value.filter(r => r.status === 'pending').length
})

const loadReports = async () => {
  reports.value = await adminStore.getReports()
}

const loadTags = async () => {
  allTags.value = await adminStore.getTags()
}

const loadIngredients = async () => {
  allIngredients.value = await adminStore.getIngredients()
}

const loadUsers = async () => {
  users.value = await adminStore.getAllUsers()
}

const resolveReport = async (reportId, action) => {
  if (confirm(action === 'dismiss' ? 'Отклонить жалобу?' : 'Удалить контент?')) {
    await adminStore.resolveReport(reportId, action)
    await loadReports()
  }
}

const addTag = async () => {
  if (newTag.value.trim()) {
    await adminStore.addTag(newTag.value.trim())
    newTag.value = ''
    await loadTags()
  }
}

const deleteTag = async (tagId) => {
  if (confirm('Удалить тег?')) {
    await adminStore.deleteTag(tagId)
    await loadTags()
  }
}

const addIngredient = async () => {
  if (newIngredient.value.trim()) {
    await adminStore.addIngredient(newIngredient.value.trim())
    newIngredient.value = ''
    await loadIngredients()
  }
}

const deleteIngredient = async (ingredientId) => {
  if (confirm('Удалить ингредиент?')) {
    await adminStore.deleteIngredient(ingredientId)
    await loadIngredients()
  }
}

const banUser = async (user) => {
  if (confirm(`Забанить пользователя ${user.username}?`)) {
    await adminStore.banUser(user.id)
    await loadUsers()
  }
}

const deleteUser = async (user) => {
  if (confirm(`Удалить пользователя ${user.username}?`)) {
    await adminStore.deleteUser(user.id)
    await loadUsers()
  }
}

onMounted(() => {
  loadReports()
  loadTags()
  loadIngredients()
  loadUsers()
})
</script>

<style scoped>
.admin-page {
  max-width: 1200px;
  margin: 0 auto;
  padding: 2rem;
}

.admin-page h1 {
  margin-bottom: 2rem;
}

.admin-tabs {
  display: flex;
  gap: 1rem;
  margin-bottom: 2rem;
  border-bottom: 2px solid #f0f0f0;
}

.admin-tabs button {
  padding: 0.75rem 1.5rem;
  background: none;
  border: none;
  cursor: pointer;
  font-weight: 500;
  color: #666;
}

.admin-tabs button.active {
  color: #ff6b6b;
  border-bottom: 2px solid #ff6b6b;
  margin-bottom: -2px;
}

.report-card, .tag-item, .ingredient-item, .user-card {
  background: white;
  padding: 1rem;
  border-radius: 12px;
  margin-bottom: 1rem;
  box-shadow: 0 2px 8px rgba(0,0,0,0.05);
}

.report-header {
  display: flex;
  justify-content: space-between;
  margin-bottom: 0.5rem;
  padding-bottom: 0.5rem;
  border-bottom: 1px solid #f0f0f0;
}

.report-content p {
  margin-bottom: 0.5rem;
}

.reported-content {
  background: #f9f9f9;
  padding: 1rem;
  border-radius: 8px;
  margin-top: 0.5rem;
}

.report-actions {
  display: flex;
  gap: 1rem;
  margin-top: 1rem;
}

.report-actions button {
  padding: 0.5rem 1rem;
  border: none;
  border-radius: 8px;
  cursor: pointer;
}

.report-actions button:first-child {
  background: #e0e0e0;
  color: #666;
}

.report-actions button:last-child {
  background: #f44336;
  color: white;
}

.add-form {
  display: flex;
  gap: 1rem;
  margin-bottom: 2rem;
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

.tag-item, .ingredient-item, .user-card {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.user-card {
  display: flex;
  gap: 1rem;
}

.user-avatar {
  width: 50px;
  height: 50px;
  background: linear-gradient(135deg, #ff6b6b, #ff8e53);
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 1.5rem;
  color: white;
}

.user-info {
  flex: 1;
}

.user-info span {
  display: block;
  color: #666;
  font-size: 0.9rem;
}

.user-actions {
  display: flex;
  gap: 0.5rem;
}

.user-actions button {
  padding: 0.5rem 1rem;
  border: none;
  border-radius: 8px;
  cursor: pointer;
}

.user-actions button:first-child {
  background: #ff9800;
  color: white;
}

.user-actions button:last-child {
  background: #f44336;
  color: white;
}
</style>