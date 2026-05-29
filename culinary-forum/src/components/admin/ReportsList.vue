<template>
  <div class="reports-list">
    <h3>📋 Жалобы пользователей</h3>
    
    <div v-for="report in reports" :key="report.id" class="report-card">
      <div class="report-header">
        <span class="report-type">{{ report.type === 'recipe' ? '📖 Рецепт' : '💬 Комментарий' }}</span>
        <span class="report-status" :class="report.status">
          {{ report.status === 'pending' ? '⏳ На рассмотрении' : '✅ Закрыта' }}
        </span>
      </div>
      
      <div class="report-content">
        <p><strong>От:</strong> {{ report.reporterName }}</p>
        <p><strong>На:</strong> {{ report.targetName }}</p>
        <p><strong>Причина:</strong> {{ report.reason }}</p>
        <div class="reported-content">
          <strong>Содержание жалобы:</strong>
          <div v-html="report.content"></div>
        </div>
      </div>
      
      <div v-if="report.status === 'pending'" class="report-actions">
        <button @click="resolveReport(report.id, 'dismiss')" class="dismiss-btn">
          Отклонить жалобу
        </button>
        <button @click="resolveReport(report.id, 'delete')" class="delete-content-btn">
          Удалить контент и закрыть жалобу
        </button>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useAdminStore } from '../../stores/adminStore'

const adminStore = useAdminStore()
const reports = ref([])

const loadReports = async () => {
  reports.value = await adminStore.getReports()
}

const resolveReport = async (reportId, action) => {
  if (confirm(action === 'dismiss' ? 'Отклонить жалобу?' : 'Удалить контент?')) {
    await adminStore.resolveReport(reportId, action)
    await loadReports()
  }
}

onMounted(() => {
  loadReports()
})
</script>

<style scoped>
.reports-list {
  display: flex;
  flex-direction: column;
  gap: 1.5rem;
}

.report-card {
  background: white;
  border-radius: 12px;
  padding: 1.5rem;
  box-shadow: 0 2px 8px rgba(0,0,0,0.05);
}

.report-header {
  display: flex;
  justify-content: space-between;
  margin-bottom: 1rem;
  padding-bottom: 0.5rem;
  border-bottom: 1px solid #f0f0f0;
}

.report-status.pending {
  color: #ff9800;
}

.report-status.resolved {
  color: #4caf50;
}

.report-content {
  margin-bottom: 1rem;
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

.dismiss-btn, .delete-content-btn {
  padding: 0.5rem 1rem;
  border: none;
  border-radius: 8px;
  cursor: pointer;
}

.dismiss-btn {
  background: #e0e0e0;
  color: #666;
}

.delete-content-btn {
  background: #f44336;
  color: white;
}
</style>