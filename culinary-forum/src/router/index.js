import { createRouter, createWebHistory } from 'vue-router'
import HomePage from '../views/HomePage.vue'
import SearchPage from '../views/SearchPage.vue'
import RecipePage from '../views/RecipePage.vue'
import RecipeEditPage from '../views/RecipeEditPage.vue'
import ProfilePage from '../views/ProfilePage.vue'
import AdminPage from '../views/AdminPage.vue'

const routes = [
  { path: '/', name: 'Home', component: HomePage },
  { path: '/search', name: 'Search', component: SearchPage },
  { path: '/recipe/:id', name: 'Recipe', component: RecipePage },
  { path: '/recipe/:id/edit', name: 'RecipeEdit', component: RecipeEditPage, meta: { requiresAuth: true } },
  { path: '/profile/:id?', name: 'Profile', component: ProfilePage },
  { path: '/admin', name: 'Admin', component: AdminPage, meta: { requiresAdmin: true } }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

// Навигационные guards
router.beforeEach((to, from, next) => {
  const keycloak = window.$keycloak
  
  if (to.meta.requiresAuth && !keycloak?.authenticated) {
    keycloak?.login()
    return
  }
  
  if (to.meta.requiresAdmin) {
    const roles = keycloak?.tokenParsed?.realm_access?.roles || []
    if (!roles.includes('admin')) {
      next('/')
      return
    }
  }
  
  next()
})

export default router