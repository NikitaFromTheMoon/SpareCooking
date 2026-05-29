<template>
  <div class="forum-container">
    <ForumHeader />
    
    <main class="forum-main">
      <router-view />
    </main>
  </div>
</template>

<script setup>
import ForumHeader from './components/ForumHeader.vue'
</script>

<style scoped>
.forum-container {
  min-height: 100vh;
  background: linear-gradient(135deg, #f5f7fa 0%, #fff5e6 100%);
  font-family: system-ui, -apple-system, 'Segoe UI', Roboto, sans-serif;
}

.forum-main {
  max-width: 1400px;
  margin: 0 auto;
  padding: 2rem;
}

@media (max-width: 768px) {
  .forum-main {
    padding: 1rem;
  }
}
</style>