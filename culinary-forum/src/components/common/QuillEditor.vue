<template>
  <div class="quill-editor-wrapper">
    <QuillEditor
      :content="content"
      :options="editorOptions"
      contentType="html"
      @update:content="onContentChange"
      @blur="onBlur"
      @focus="onFocus"
      :class="{ 'is-invalid': error }"
    />
    <div v-if="error" class="error-message">{{ error }}</div>
    <div class="editor-toolbar-info">
      <span>📝 Доступные форматы: жирный, курсив, списки, ссылки</span>
    </div>
  </div>
</template>

<script setup>
import { QuillEditor } from '@vueup/vue-quill'
import '@vueup/vue-quill/dist/vue-quill.snow.css'

const props = defineProps({
  content: {
    type: String,
    default: ''
  },
  placeholder: {
    type: String,
    default: 'Опишите рецепт...'
  },
  error: {
    type: String,
    default: ''
  }
})

const emit = defineEmits(['update:content', 'blur', 'focus'])

const editorOptions = {
  theme: 'snow',
  placeholder: props.placeholder,
  modules: {
    toolbar: [
      ['bold', 'italic', 'underline', 'strike'],
      [{ list: 'ordered' }, { list: 'bullet' }],
      ['link', 'clean'],
      [{ align: [] }],
      [{ header: [1, 2, 3, 4, 5, 6, false] }],
      [{ color: [] }, { background: [] }]
    ]
  }
}

const onContentChange = (value) => {
  emit('update:content', value)
}

const onBlur = () => {
  emit('blur')
}

const onFocus = () => {
  emit('focus')
}
</script>

<style scoped>
.quill-editor-wrapper {
  width: 100%;
}

.quill-editor-wrapper :deep(.ql-container) {
  min-height: 200px;
  font-size: 16px;
  font-family: inherit;
  border-radius: 0 0 8px 8px;
}

.quill-editor-wrapper :deep(.ql-toolbar) {
  border-radius: 8px 8px 0 0;
  background: #f9f9f9;
  border-color: #e0e0e0;
}

.quill-editor-wrapper :deep(.ql-editor) {
  min-height: 200px;
}

.quill-editor-wrapper.is-invalid :deep(.ql-container) {
  border-color: #f44336;
}

.quill-editor-wrapper.is-invalid :deep(.ql-toolbar) {
  border-color: #f44336;
}

.error-message {
  color: #f44336;
  font-size: 0.8rem;
  margin-top: 0.25rem;
}

.editor-toolbar-info {
  margin-top: 0.5rem;
  font-size: 0.75rem;
  color: #999;
  text-align: center;
}
</style>