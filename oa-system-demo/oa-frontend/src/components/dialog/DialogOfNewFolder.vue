<script setup>
import axios from 'axios'
import { defineProps, ref, watch } from 'vue'

const props = defineProps({
  visible: Boolean,
  path: String,
})
const emit = defineEmits(['update', 'close'])

const localVisible = ref(props.visible) // 是否显示对话框
const localPath = ref(props.path) // 当前路径

const newFileName = ref('') // 新文件/文件夹名称

// 提交
function submit() {
  axios
    .post(`/api/files/create-file?fileName=${newFileName.value}&path=${localPath.value}`)
    .then(() => {
      localVisible.value = false
      emit('update')
    })
    .catch((error) => {
      alert('权限不足！')
      emit('close')
      console.log(error)
    })
}

watch(
  () => props.path,
  (newPath) => {
    localPath.value = newPath
  },
)

watch(
  () => props.visible,
  (newVisiable) => {
    localVisible.value = newVisiable
  },
)
</script>

<template>
  <!-- 增加文件/文件夹对话框 -->
  <div
    v-if="localVisible"
    class="fixed inset-0 bg-opacity-50 backdrop-blur-sm flex items-center justify-center shadow-md"
  >
    <div class="bg-white p-6 rounded-lg shadow-lg">
      <h2 class="text-xl font-bold mb-4">增加文件/文件夹</h2>
      <div class="mb-4">
        <label class="block text-sm font-medium text-gray-700">名称</label>
        <input
          v-model="newFileName"
          type="text"
          class="w-full p-3 border border-gray-300 rounded-lg"
        />
      </div>
      <div class="flex justify-end">
        <button
          @click="submit"
          class="px-4 py-2 bg-blue-500 text-white rounded hover:bg-blue-700 mr-2"
        >
          确定
        </button>
        <button
          @click="$emit('close')"
          class="px-4 py-2 bg-gray-500 text-white rounded hover:bg-gray-700"
        >
          取消
        </button>
      </div>
    </div>
  </div>
</template>
