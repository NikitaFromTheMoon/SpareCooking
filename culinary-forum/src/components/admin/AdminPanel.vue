<template>
  <div class="admin-panel">
    <h1>👑 Панель администратора</h1>
    
    <div class="admin-tabs">
      <button :class="{ active: activeTab === 'reports' }" @click="activeTab = 'reports'">
        Жалобы ({{ pendingReports }})
      </button>
      <button :class="{ active: activeTab === 'tags' }" @click="activeTab = 'tags'">
        Управление тегами
      </button>
      <button :class="{ active: activeTab === 'ingredients' }" @click="activeTab = 'ingredients'">
        Управление ингредиентами
      </button>
      <button :class="{ active: activeTab === 'users' }" @click="activeTab = 'users'">
        Пользователи
      </button>
    </div>
    
    <!-- Жалобы -->
    <ReportsList v-if="activeTab === 'reports'" @report-resolved="loadReports" />
    
    <!-- Управление тегами -->
    <TagsManager v-if="activeTab === 'tags'" />
    
    <!-- Управление ингредиентами -->
    <IngredientsManager v-if="activeTab === 'ingredients'" />
    
    <!-- Список пользователей -->
    <div v-if="activeTab === 'users'" class="users-list">
      <h3>Все пользователи</h3>
      <div v-for="user in users" :key="user.id" class="user-card">
        <img :src="user.avatar" :alt="user.username" class="user-avatar" />
        <div class="user-info">
          <strong>{{ user.username }}</strong>
          <span>{{ user.email }}</span>
          <span>Рецептов: {{ user.recipesCount }}</span>
        </div>
        <div class="user-actions">
          <button @click="banUser(user)" class="ban-btn">Забанить</button>
          <button @click="deleteUserAccount(user)" class="delete-user-btn">Удалить аккаунт</button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useAdminStore } from '../../stores/adminStore'
import ReportsList from './ReportsList.vue'
import TagsManager from './TagsManager.vue'
import IngredientsManager from './IngredientsManager.vue'

const adminStore = useAdminStore()
const activeTab = ref('reports')
const pendingReports = ref(0)
const users = ref([])

const loadReports = async () => {
  const reports = await adminStore.getReports()
  pendingReports.value = reports.filter(r => r.status === 'pending').length
}

const loadUsers = async () => {
  users.value = await adminStore.getAllUsers()
}

const banUser = async (user) => {
  if (confirm(`Забанить пользователя ${user.username}?`)) {
    await adminStore.banUser(user.id)
    await loadUsers()
  }
}

const deleteUserAccount = async (user) => {
  if (confirm(`Удалить аккаунт пользователя ${user.username}? Это действие необратимо.`)) {
    await adminStore.deleteUser(user.id)
    await loadUsers()
  }
}

onMounted(() => {
  loadReports()
  loadUsers()
})
</script>

<style scoped>
.admin-panel {
  max-width: 1200px;
  margin: 0 auto;
  padding: 2rem;
}

.admin-tabs {
  display: flex;
  gap: 1rem;
  margin: 2rem 0;
  border-bottom: 2px solid #f0f0f0;
}

.admin-tabs button {
  padding: 0.75rem 1.5rem;
  background: none;
  border: none;
  cursor: pointer;
  font-weight: 500;
  color: #666;
  position: relative;
}

.admin-tabs button.active {
  color: #ff6b6b;
}

.admin-tabs button.active::after {
  content: '';
  position: absolute;
  bottom: -2px;
  left: 0;
  right: 0;
  height: 2px;
  background: #ff6b6b;
}

.users-list {
  display: flex;
  flex-direction: column;
  gap: 1rem;
}

.user-card {
  display: flex;
  align-items: center;
  gap: 1rem;
  padding: 1rem;
  background: white;
  border-radius: 12px;
  box-shadow: 0 2px 8px rgba(0,0,0,0.05);
}

.user-avatar {
  width: 50px;
  height: 50px;
  border-radius: 50%;
  object-fit: cover;
}

.user-info {
  flex: 1;
  display: flex;
  flex-direction: column;
  gap: 0.25rem;
}

.user-actions {
  display: flex;
  gap: 0.5rem;
}

.ban-btn, .delete-user-btn {
  padding: 0.5rem 1rem;
  border: none;
  border-radius: 8px;
  cursor: pointer;
}

.ban-btn {
  background: #ff9800;
  color: white;
}

.delete-user-btn {
  background: #f44336;
  color: white;
}
</style>