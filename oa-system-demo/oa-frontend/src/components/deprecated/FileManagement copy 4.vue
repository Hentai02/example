<template>
  <!-- 薪酬管理 -->
  <div class="min-h-screen bg-white p-6 rounded-lg shadow-lg">
    <h1 class="text-3xl font-bold mb-6">文件管理</h1>
    <div class="mb-6">
      <!-- 显示文件路径 -->
      <div class="mb-4">
        <span v-for="(segment, index) in pathSegments" :key="index">
          <span class="cursor-pointer text-blue-500 hover:underline" @click="navigateTo(index)">
            {{ segment }}
          </span>
          <span v-if="index < pathSegments.length - 1"> / </span>
        </span>
      </div>
      <ul class="divide-y divide-gray-200">
        <li v-for="file in files" :key="file.id" class="py-3 flex items-start space-x-4">
          <font-awesome-icon
            class="text-2xl"
            :icon="file.file ? ['fas', 'file'] : ['fas', 'folder']"
          />
          <div class="flex-1 cursor-pointer" @click="openfile(file)">
            <p
              class="text-sm font-medium"
              :class="{ 'text-gray-900': !file.read, 'text-gray-500': file.read }"
            >
              {{ file.name }}
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
// const path = ref('')

function fetchFileItems() {
  axios
    .get('/api/file')
    .then((response) => {
      files.value = response.data

      console.log(response.data)
    })
    .catch((error) => {
      console.error(error)
    })
}

function openfile(file) {
  if (!file.file) {
    // path.value =
    axios
      .get(`/api/file?path=${file.filePath}`)
      .then((response) => {
        files.value = response.data

        console.log(response.data)
      })
      .catch((error) => {
        console.error(error)
      })
  }
}

onMounted(() => {
  fetchFileItems()
})
</script>
