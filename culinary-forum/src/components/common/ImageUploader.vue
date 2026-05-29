<template>
  <div class="image-uploader">
    <div class="upload-area" :class="{ 'drag-over': isDragOver }" @dragover.prevent="isDragOver = true" @dragleave.prevent="isDragOver = false" @drop.prevent="handleDrop">
      <input type="file" ref="fileInput" multiple accept="image/jpeg,image/png,image/webp,image/gif" @change="handleFiles" style="display: none" />
      
      <div v-if="!modelValue.length" class="upload-placeholder" @click="triggerFileInput">
        <div class="upload-icon">📸</div>
        <div class="upload-text">Нажмите или перетащите изображения</div>
        <div class="upload-hint">PNG, JPG, WEBP до 10MB</div>
      </div>
      
      <div v-else class="image-grid">
        <div v-for="(image, index) in modelValue" :key="index" class="image-preview" :class="{ 'uploading': image.uploading }">
          <img :src="getImageUrl(image)" :alt="`Изображение ${index + 1}`" />
          
          <div class="image-overlay">
            <button type="button" class="remove-btn" @click="removeImage(index)" title="Удалить">✕</button>
            <button type="button" class="main-btn" v-if="index > 0 && !image.isMain" @click="setMain(index)" title="Сделать главным">★</button>
            <span v-if="image.isMain || index === 0" class="main-badge">Главное</span>
          </div>
          
          <div v-if="image.uploading" class="upload-progress">
            <div class="progress-bar" :style="{ width: image.progress + '%' }"></div>
            <span class="progress-text">{{ image.progress }}%</span>
          </div>
        </div>
        
        <div v-if="modelValue.length < maxFiles" class="add-more" @click="triggerFileInput">
          <div class="add-icon">+</div>
          <div class="add-text">Добавить ещё</div>
        </div>
      </div>
    </div>
    
    <div class="upload-info">
      <span>Загружено: {{ modelValue.length }}/{{ maxFiles }}</span>
      <button v-if="modelValue.length" type="button" @click="clearAll" class="clear-all">Очистить все</button>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'

const props = defineProps({
  modelValue: {
    type: Array,
    default: () => []
  },
  maxFiles: {
    type: Number,
    default: 5
  },
  maxSize: {
    type: Number,
    default: 10 * 1024 * 1024 // 10MB
  }
})

const emit = defineEmits(['update:modelValue'])
const fileInput = ref(null)
const isDragOver = ref(false)

const triggerFileInput = () => {
  fileInput.value.click()
}

const handleFiles = async (event) => {
  const files = Array.from(event.target.files)
  await processFiles(files)
  fileInput.value.value = ''
}

const handleDrop = async (event) => {
  const files = Array.from(event.dataTransfer.files)
  await processFiles(files)
  isDragOver.value = false
}

const processFiles = async (files) => {
  const imageFiles = files.filter(file => file.type.startsWith('image/'))
  
  if (props.modelValue.length + imageFiles.length > props.maxFiles) {
    alert(`Максимум ${props.maxFiles} изображений`)
    return
  }
  
  for (const file of imageFiles) {
    if (file.size > props.maxSize) {
      alert(`Файл ${file.name} превышает 10MB`)
      continue
    }
    
    const imageItem = {
      file,
      url: URL.createObjectURL(file),
      uploading: true,
      progress: 0,
      isMain: props.modelValue.length === 0
    }
    
    emit('update:modelValue', [...props.modelValue, imageItem])
    
    // Симуляция загрузки
    for (let i = 0; i <= 100; i += 10) {
      await new Promise(resolve => setTimeout(resolve, 50))
      const index = props.modelValue.findIndex(img => img.file === file)
      if (index !== -1) {
        const newValue = [...props.modelValue]
        newValue[index].progress = i
        if (i === 100) {
          newValue[index].uploading = false
        }
        emit('update:modelValue', newValue)
      }
    }
  }
}

const removeImage = (index) => {
  const newValue = props.modelValue.filter((_, i) => i !== index)
  // Если удалили главное изображение, делаем первое главным
  if (index === 0 && newValue.length > 0) {
    newValue[0].isMain = true
  }
  emit('update:modelValue', newValue)
}

const setMain = (index) => {
  const newValue = props.modelValue.map((img, i) => ({
    ...img,
    isMain: i === index
  }))
  emit('update:modelValue', newValue)
}

const clearAll = () => {
  if (confirm('Удалить все изображения?')) {
    emit('update:modelValue', [])
  }
}

const getImageUrl = (image) => {
  if (typeof image === 'string') return image
  return image.url || image.preview
}
</script>

<style scoped>
.image-uploader {
  width: 100%;
}

.upload-area {
  border: 2px dashed #e0e0e0;
  border-radius: 16px;
  transition: all 0.3s;
  background: #fafafa;
}

.upload-area.drag-over {
  border-color: #ff6b6b;
  background: #fff5e6;
}

.upload-placeholder {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 2rem;
  cursor: pointer;
}

.upload-icon {
  font-size: 3rem;
  margin-bottom: 0.5rem;
}

.upload-text {
  color: #666;
  margin-bottom: 0.25rem;
}

.upload-hint {
  font-size: 0.8rem;
  color: #999;
}

.image-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(150px, 1fr));
  gap: 1rem;
  padding: 1rem;
}

.image-preview {
  position: relative;
  aspect-ratio: 1;
  border-radius: 12px;
  overflow: hidden;
  background: #f0f0f0;
}

.image-preview img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.image-overlay {
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: rgba(0,0,0,0.5);
  display: flex;
  justify-content: center;
  align-items: center;
  gap: 0.5rem;
  opacity: 0;
  transition: opacity 0.3s;
}

.image-preview:hover .image-overlay {
  opacity: 1;
}

.remove-btn, .main-btn {
  width: 32px;
  height: 32px;
  border-radius: 50%;
  border: none;
  cursor: pointer;
  font-size: 1rem;
  transition: transform 0.2s;
}

.remove-btn {
  background: #f44336;
  color: white;
}

.main-btn {
  background: #ffa500;
  color: white;
}

.remove-btn:hover, .main-btn:hover {
  transform: scale(1.1);
}

.main-badge {
  position: absolute;
  top: 0.5rem;
  left: 0.5rem;
  background: #ffa500;
  color: white;
  padding: 0.25rem 0.5rem;
  border-radius: 20px;
  font-size: 0.7rem;
}

.upload-progress {
  position: absolute;
  bottom: 0;
  left: 0;
  right: 0;
  height: 4px;
  background: #e0e0e0;
}

.progress-bar {
  height: 100%;
  background: #4caf50;
  transition: width 0.3s;
}

.progress-text {
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  font-size: 0.7rem;
  color: white;
  background: rgba(0,0,0,0.7);
  padding: 0.25rem 0.5rem;
  border-radius: 20px;
}

.add-more {
  aspect-ratio: 1;
  border: 2px dashed #e0e0e0;
  border-radius: 12px;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  cursor: pointer;
  transition: all 0.3s;
}

.add-more:hover {
  border-color: #ff6b6b;
  background: #fff5e6;
}

.add-icon {
  font-size: 2rem;
  color: #999;
}

.add-text {
  font-size: 0.8rem;
  color: #999;
}

.upload-info {
  display: flex;
  justify-content: space-between;
  margin-top: 0.5rem;
  font-size: 0.8rem;
  color: #999;
}

.clear-all {
  background: none;
  border: none;
  color: #f44336;
  cursor: pointer;
  font-size: 0.8rem;
}
</style>