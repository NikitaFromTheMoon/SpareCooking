<template>
  <header class="forum-header">
    <div class="header-content">
      <h1 class="logo" @click="$router.push('/')">
        <span class="chef-hat">👨‍🍳</span>
        Кулинарный Форум
      </h1>
      
      <div class="search-wrapper">
        <input 
          type="text" 
          v-model="searchQuery" 
          placeholder="Поиск рецептов..."
          @keyup.enter="doSearch"
          class="search-input"
        />
        <button @click="doSearch" class="search-btn">🔍</button>
      </div>
      
      <nav class="nav-links">
        <router-link to="/search" class="nav-link">Поиск</router-link>
        
        <div v-if="!$keycloak?.authenticated" class="auth-buttons">
          <button class="login-btn" @click="$keycloak.login()">Войти</button>
          <button class="register-btn" @click="$keycloak.register()">Регистрация</button>
        </div>
        
        <div v-else class="user-info">
          <router-link to="/recipe/new" class="create-btn">➕ Создать</router-link>
          
          <div class="user-menu">
            <button class="user-avatar" @click="showMenu = !showMenu">
              👤 {{ userName }}
            </button>
            
            <div v-if="showMenu" class="dropdown-menu" @click.stop>
              <router-link to="/profile" @click="showMenu = false">📱 Профиль</router-link>
              <router-link v-if="isAdmin" to="/admin" @click="showMenu = false">👑 Админ-панель</router-link>
              <button @click="$keycloak.accountManagement()">⚙️ Управление аккаунтом</button>
              <button @click="logout">🚪 Выйти</button>
            </div>
          </div>
        </div>
      </nav>
    </div>
  </header>
</template>

<script setup>
import { ref, computed, inject, onMounted, onUnmounted } from 'vue'
import { useRouter } from 'vue-router'
import { useAuthStore } from '../stores/authStore'

const router = useRouter()
const authStore = useAuthStore()
const $keycloak = inject('$keycloak')

const searchQuery = ref('')
const showMenu = ref(false)

const userName = computed(() => {
  if ($keycloak?.authenticated && $keycloak.tokenParsed) {
    return $keycloak.tokenParsed.preferred_username || 
           $keycloak.tokenParsed.email || 
           'Пользователь'
  }
  return ''
})

const isAdmin = computed(() => authStore.isAdmin)

const doSearch = () => {
  if (searchQuery.value.trim()) {
    router.push({ path: '/search', query: { q: searchQuery.value } })
  }
}

const logout = () => {
  showMenu.value = false
  authStore.logout()
}

const handleClickOutside = (event) => {
  if (!event.target.closest('.user-menu')) {
    showMenu.value = false
  }
}

onMounted(() => {
  document.addEventListener('click', handleClickOutside)
})

onUnmounted(() => {
  document.removeEventListener('click', handleClickOutside)
})
</script>

<style scoped>
.forum-header {
  background: white;
  box-shadow: 0 2px 10px rgba(0,0,0,0.05);
  position: sticky;
  top: 0;
  z-index: 100;
}

.header-content {
  max-width: 1400px;
  margin: 0 auto;
  padding: 1rem 2rem;
  display: flex;
  justify-content: space-between;
  align-items: center;
  flex-wrap: wrap;
  gap: 1rem;
}

.logo {
  font-size: 1.5rem;
  font-weight: 600;
  background: linear-gradient(135deg, #ff6b6b, #ff8e53);
  -webkit-background-clip: text;
  background-clip: text;
  color: transparent;
  cursor: pointer;
  margin: 0;
}

.chef-hat {
  font-size: 1.8rem;
  background: none;
  -webkit-background-clip: unset;
  background-clip: unset;
  color: #ff6b6b;
}

.search-wrapper {
  display: flex;
  flex: 1;
  max-width: 400px;
  margin: 0 1rem;
}

.search-input {
  flex: 1;
  padding: 0.5rem 1rem;
  border: 1px solid #e0e0e0;
  border-radius: 24px 0 0 24px;
  font-size: 0.9rem;
  outline: none;
}

.search-input:focus {
  border-color: #ff6b6b;
}

.search-btn {
  padding: 0.5rem 1rem;
  background: linear-gradient(135deg, #ff6b6b, #ff8e53);
  border: none;
  border-radius: 0 24px 24px 0;
  color: white;
  cursor: pointer;
}

.nav-links {
  display: flex;
  gap: 1rem;
  align-items: center;
  flex-wrap: wrap;
}

.nav-link {
  padding: 0.5rem 1rem;
  color: #666;
  text-decoration: none;
  transition: color 0.3s;
}

.nav-link:hover {
  color: #ff6b6b;
}

.auth-buttons {
  display: flex;
  gap: 0.5rem;
}

.login-btn, .register-btn, .create-btn {
  padding: 0.5rem 1.5rem;
  border-radius: 24px;
  font-size: 0.9rem;
  cursor: pointer;
  transition: all 0.3s;
}

.login-btn {
  background: none;
  border: 1px solid #ff6b6b;
  color: #ff6b6b;
}

.login-btn:hover {
  background: #ff6b6b;
  color: white;
}

.register-btn, .create-btn {
  background: linear-gradient(135deg, #ff6b6b, #ff8e53);
  border: none;
  color: white;
  text-decoration: none;
}

.create-btn:hover {
  transform: translateY(-2px);
}

.user-info {
  display: flex;
  align-items: center;
  gap: 1rem;
}

.user-menu {
  position: relative;
}

.user-avatar {
  background: #fff5e6;
  border: none;
  padding: 0.5rem 1rem;
  border-radius: 24px;
  cursor: pointer;
  font-size: 0.9rem;
  color: #ff6b6b;
}

.dropdown-menu {
  position: absolute;
  top: 100%;
  right: 0;
  margin-top: 0.5rem;
  background: white;
  border-radius: 12px;
  box-shadow: 0 4px 20px rgba(0,0,0,0.15);
  overflow: hidden;
  min-width: 200px;
  z-index: 1000;
}

.dropdown-menu a, .dropdown-menu button {
  display: block;
  width: 100%;
  padding: 0.75rem 1rem;
  border: none;
  background: none;
  text-align: left;
  cursor: pointer;
  font-size: 0.9rem;
  color: #333;
  text-decoration: none;
}

.dropdown-menu a:hover, .dropdown-menu button:hover {
  background: #fff5e6;
  color: #ff6b6b;
}

@media (max-width: 768px) {
  .header-content {
    flex-direction: column;
  }
  
  .search-wrapper {
    max-width: 100%;
    width: 100%;
  }
  
  .nav-links {
    justify-content: center;
  }
}
</style>