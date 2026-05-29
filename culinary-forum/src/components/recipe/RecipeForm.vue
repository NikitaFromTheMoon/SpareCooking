<template>
  <div class="recipe-form">
    <h2>{{ isEdit ? '✏️ Редактирование рецепта' : '🍳 Новый рецепт' }}</h2>
    
    <form @submit.prevent="submitForm">
      <div class="form-group">
        <label>Название блюда *</label>
        <input v-model="form.title" required placeholder="Например: Тирамису">
      </div>
      
      <div class="form-group">
        <label>Описание *</label>
        <QuillEditor v-model:content="form.description" />
      </div>
      
      <div class="form-group">
        <label>Ингредиенты *</label>
        <div v-for="(ing, idx) in form.ingredients" :key="idx" class="ingredient-row">
          <input v-model="ing.name" placeholder="Название" />
          <input v-model="ing.amount" placeholder="Количество" />
          <input v-model="ing.unit" placeholder="Ед. изм." />
          <input v-model="ing.note" placeholder="Примечание" />
          <button type="button" @click="removeIngredient(idx)" class="remove-btn">✕</button>
        </div>
        <button type="button" @click="addIngredient" class="add-btn">+ Добавить ингредиент</button>
      </div>
      
      <div class="form-group">
        <label>Количество порций</label>
        <input type="number" v-model.number="form.portions" min="1" />
      </div>
      
      <div class="form-group">
        <label>Время приготовления (мин) *</label>
        <input type="number" v-model.number="form.cookingTime" required min="1" />
      </div>
      
      <div class="form-group">
        <label>Теги</label>
        <div class="tags-input">
          <span v-for="tag in form.tags" :key="tag" class="tag">
            {{ tag }}
            <button type="button" @click="removeTag(tag)">✕</button>
          </span>
          <input 
            v-model="newTag" 
            @keyup.enter="addTag"
            placeholder="Введите тег"
          />
        </div>
      </div>
      
      <div class="form-group">
        <label>Изображения</label>
        <input type="file" multiple accept="image/*" @change="handleImages" />
        <div class="image-previews">
          <div v-for="(img, idx) in form.images" :key="idx" class="image-preview">
            <img :src="typeof img === 'string' ? img : URL.createObjectURL(img)" />
            <button type="button" @click="removeImage(idx)">✕</button>
          </div>
        </div>
      </div>
      
      <div class="form-actions">
        <button type="submit" class="submit-btn">
          {{ isEdit ? 'Сохранить изменения' : 'Опубликовать рецепт' }}
        </button>
        <button type="button" class="cancel-btn" @click="$router.back()">Отмена</button>
      </div>
    </form>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { QuillEditor } from '@vueup/vue-quill'
import '@vueup/vue-quill/dist/vue-quill.snow.css'
import { useRecipeStore } from '../../stores/recipeStore'

const props = defineProps({
  isEdit: { type: Boolean, default: false }
})

const route = useRoute()
const router = useRouter()
const recipeStore = useRecipeStore()
const newTag = ref('')

const form = reactive({
  title: '',
  description: '',
  ingredients: [{ name: '', amount: '', unit: '', note: '' }],
  portions: 2,
  cookingTime: 30,
  tags: [],
  images: []
})

const addIngredient = () => {
  form.ingredients.push({ name: '', amount: '', unit: '', note: '' })
}

const removeIngredient = (index) => {
  form.ingredients.splice(index, 1)
}

const addTag = () => {
  if (newTag.value.trim() && !form.tags.includes(newTag.value.trim())) {
    form.tags.push(newTag.value.trim())
    newTag.value = ''
  }
}

const removeTag = (tag) => {
  form.tags = form.tags.filter(t => t !== tag)
}

const handleImages = (event) => {
  const files = Array.from(event.target.files)
  form.images.push(...files)
}

const removeImage = (index) => {
  form.images.splice(index, 1)
}

const submitForm = async () => {
  if (!form.title || !form.description || form.ingredients.length === 0) {
    alert('Заполните все обязательные поля')
    return
  }
  
  try {
    if (props.isEdit) {
      await recipeStore.updateRecipe(route.params.id, form)
    } else {
      await recipeStore.createRecipe(form)
    }
    router.push('/')
  } catch (error) {
    console.error('Ошибка сохранения:', error)
    alert('Ошибка при сохранении рецепта')
  }
}

onMounted(async () => {
  if (props.isEdit) {
    const recipe = await recipeStore.getRecipeById(route.params.id)
    Object.assign(form, recipe)
  }
})
</script>

<style scoped>
.recipe-form {
  max-width: 800px;
  margin: 0 auto;
  background: white;
  padding: 2rem;
  border-radius: 24px;
}

.form-group {
  margin-bottom: 1.5rem;
}

.form-group label {
  display: block;
  margin-bottom: 0.5rem;
  font-weight: 500;
  color: #555;
}

.form-group input,
.form-group textarea {
  width: 100%;
  padding: 0.75rem;
  border: 1px solid #e0e0e0;
  border-radius: 8px;
  font-family: inherit;
}

.ingredient-row {
  display: grid;
  grid-template-columns: 2fr 1fr 1fr 2fr auto;
  gap: 0.5rem;
  margin-bottom: 0.5rem;
}

.ingredient-row input {
  padding: 0.5rem;
}

.remove-btn {
  background: #ff6b6b;
  color: white;
  border: none;
  padding: 0 0.75rem;
  border-radius: 8px;
  cursor: pointer;
}

.add-btn {
  background: none;
  color: #ff6b6b;
  border: 1px solid #ff6b6b;
  padding: 0.5rem;
  border-radius: 8px;
  margin-top: 0.5rem;
  cursor: pointer;
}

.tags-input {
  display: flex;
  flex-wrap: wrap;
  gap: 0.5rem;
  padding: 0.5rem;
  border: 1px solid #e0e0e0;
  border-radius: 8px;
}

.tag {
  background: #fff5e6;
  color: #ff6b6b;
  padding: 0.25rem 0.75rem;
  border-radius: 20px;
  display: flex;
  align-items: center;
  gap: 0.5rem;
}

.tag button {
  background: none;
  border: none;
  cursor: pointer;
}

.tags-input input {
  flex: 1;
  border: none;
  outline: none;
}

.image-previews {
  display: flex;
  flex-wrap: wrap;
  gap: 1rem;
  margin-top: 1rem;
}

.image-preview {
  position: relative;
  width: 100px;
  height: 100px;
}

.image-preview img {
  width: 100%;
  height: 100%;
  object-fit: cover;
  border-radius: 8px;
}

.image-preview button {
  position: absolute;
  top: -8px;
  right: -8px;
  background: #ff6b6b;
  color: white;
  border: none;
  border-radius: 50%;
  width: 24px;
  height: 24px;
  cursor: pointer;
}

.form-actions {
  display: flex;
  gap: 1rem;
  margin-top: 2rem;
}

.submit-btn, .cancel-btn {
  padding: 0.75rem 1.5rem;
  border-radius: 8px;
  cursor: pointer;
  font-weight: 600;
}

.submit-btn {
  background: linear-gradient(135deg, #ff6b6b, #ff8e53);
  color: white;
  border: none;
  flex: 1;
}

.cancel-btn {
  background: none;
  border: 1px solid #ddd;
  color: #666;
}
</style>