<template>
  <!-- 薪酬管理 -->
  <div class="min-h-screen bg-white p-6 rounded-lg shadow-lg">
    <h1 class="text-3xl font-bold mb-6">文件管理</h1>
    <div class="mb-6">
      <ul class="divide-y divide-gray-200">
        <li v-for="file in files" :key="file.id" class="py-3 flex items-start space-x-4">
          <font-awesome-icon
            class="text-2xl"
            :icon="file.file ? ['fas', 'folder'] : ['fas', 'file']"
          />
          <div class="flex-1 cursor-pointer" @click="openfileDialog(file)">
            <p
              class="text-sm font-medium"
              :class="{ 'text-gray-900': !file.read, 'text-gray-500': file.read }"
            >
              {{ file }}
            </p>
            <!-- <p class="text-xs text-gray-600 truncate">上传者：{{ file.uploadEmployeeId }}</p> -->
            <p class="text-xs text-gray-400">{{ file.createdAt }}</p>
          </div>

          <div class="flex space-x-2">
            <button @click="markAsRead(file.id)" class="text-xs text-blue-500 hover:underline">
              分配权限
            </button>
            <button @click="deletefile(file.id)" class="text-xs text-red-500 hover:underline">
              删除
            </button>
          </div>
        </li>
      </ul>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import axios from 'axios'

const files = ref([])

function fetchFileItems() {
  axios
    .get('/api/file/all')
    .then((response) => {
      files.value = response.data

      console.log(response.data)
    })
    .catch((error) => {
      console.error(error)
    })
}

function openfileDialog(file) {
  axios
    .post(`/api/file/listFiles`, { name: file })
    .then((response) => {
      files.value = response.data

      console.log(response.data)
    })
    .catch((error) => {
      console.error(error)
    })
}

onMounted(() => {
  fetchFileItems()
})
</script>
