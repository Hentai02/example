<script setup>
import { ref, onMounted, computed } from 'vue'
import axios from 'axios'
import { useRouter } from 'vue-router'
import DialogOfNewFolder from '../dialog/DialogOfNewFolder.vue'

const router = useRouter()

const files = ref([])
const currentPath = ref('/') // 当前路径
const visibleDialogOfNewFolder = ref(false)
const showUploadDialog = ref(false)
const fileInput = ref(null)

async function uploadFile() {
  const formData = new FormData()
  formData.append('file', fileInput.value.files[0])
  formData.append('path', currentPath.value)
  formData.append('uploadEmployeeId', 1)
  console.log(fileInput.value.files[0])

  await axios
    .post('/api/file-upload', formData, {
      headers: {
        'Content-Type': 'multipart/form-data',
      },
    })
    .then(() => {
      showUploadDialog.value = false
      fetchFileItems(currentPath.value)
    })
    .catch((error) => {
      alert('权限不足，请联系管理员')
      showUploadDialog.value = false
      fetchFileItems(currentPath.value)
      console.error(error)
    })
}

function fetchFileItems(path) {
  axios
    .get(`/api/files?path=${path}`)
    .then((response) => {
      files.value = response.data
      currentPath.value = path
      // console.log(currentPath.value)
    })
    .catch((error) => {
      console.error(error)
    })
}

// 打开文件或文件夹
function openFile(file) {
  if (file.isFile) {
    // 如果是文件，执行打开文件的逻辑
    console.log('打开文件:', file.name)
    axios
      .get(`/api/file-upload/files/${file.name}?path=${currentPath.value}`, {
        responseType: 'blob',
      })
      .then((response) => {
        const url = window.URL.createObjectURL(new Blob([response.data]))
        const link = document.createElement('a')
        link.href = url
        link.setAttribute('download', file.name)
        document.body.appendChild(link)
        link.click()
        document.body.removeChild(link)
      })
      .catch((error) => {
        console.error(error)
      })
  } else {
    // 如果是文件夹，获取该文件夹下的文件列表
    fetchFileItems(`${currentPath.value}/${file.name}`.replace(/\/+/g, '/'))
    // console.log(currentPath)
    // console.log(currentPath.value)
  }
}

function deletefile(file) {
  const confirmed = confirm('确定要删除这个文件吗？')
  if (!confirmed) {
    return
  }
  // console.log(file.id)
  if (file.id) {
    axios
      .delete(`/api/files/${file.id}`)
      .then(() => {
        fetchFileItems(currentPath.value)
      })
      .catch((error) => {
        alert('权限不足，请联系管理员')
        console.error(error)
      })
  } else {
    axios
      .delete(`/api/files?file-name=${file.name}&path=${currentPath.value}`)
      .then(() => {
        fetchFileItems(currentPath.value)
      })
      .catch((error) => {
        alert('权限不足，请联系管理员')
        console.error(error)
      })
  }
}

// 检查是否为根目录
const isRootDirectory = computed(() => {
  return currentPath.value === '/'
})

const pathSegments = computed(() => {
  return currentPath.value.split('/').filter((segment) => segment)
})

// 导航到指定路径段
function navigateTo(index) {
  const newPath = '/' + pathSegments.value.slice(0, index + 1).join('/')
  fetchFileItems(newPath)
}

// 导航到根目录
function navigateToRoot() {
  fetchFileItems('/')
}

function markAsRead(file) {
  router.push({
    name: 'AssignPermissionsView',
    query: { file: JSON.stringify(file) },
  })
}

onMounted(() => {
  fetchFileItems('/')
})
</script>

<template>
  <!-- 薪酬管理 -->
  <div class="min-h-screen bg-white p-6 rounded-lg shadow-lg">
    <h1 class="text-3xl font-bold mb-6">文件管理</h1>
    <div class="mb-6">
      <!-- 显示文件路径 -->
      <div class="flex justify-between">
        <div class="mb-4">
          <span class="cursor-pointer text-blue-500 hover:underline" @click="navigateToRoot"
            >根目录</span
          >
          <span v-if="pathSegments.length > 0"> / </span>
          <span v-for="(segment, index) in pathSegments" :key="index">
            <span class="cursor-pointer text-blue-500 hover:underline" @click="navigateTo(index)">
              {{ segment }}
            </span>
            <span v-if="index < pathSegments.length - 1"> / </span>
          </span>
        </div>
        <div class="mb-4">
          <button
            @click="visibleDialogOfNewFolder = true"
            class="px-4 py-2 mr-4 bg-blue-500 text-white rounded hover:bg-blue-700"
          >
            创建文件夹
          </button>
          <button
            v-if="currentPath !== '/'"
            @click="showUploadDialog = true"
            class="px-4 py-2 bg-green-500 text-white rounded hover:bg-green-700"
          >
            上传文件
          </button>
        </div>
      </div>
      <ul class="divide-y divide-gray-200">
        <li v-for="file in files" :key="file.id" class="py-3 flex items-start space-x-4">
          <font-awesome-icon
            class="text-2xl"
            :icon="file.isFile ? ['fas', 'file'] : ['fas', 'folder']"
          />
          <div class="flex-1 cursor-pointer" @click="openFile(file)">
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
            <button
              v-if="isRootDirectory"
              @click="markAsRead(file)"
              class="text-xs text-blue-500 hover:underline"
            >
              分配权限
            </button>
            <button @click="deletefile(file)" class="text-xs text-red-500 hover:underline">
              删除
            </button>
          </div>
        </li>
      </ul>
    </div>

    <!-- 上传文件对话框 -->
    <div
      v-if="showUploadDialog"
      class="fixed inset-0 flex items-center justify-center bg-opacity-50 backdrop-blur-sm"
    >
      <div class="bg-white p-6 rounded-lg shadow-lg">
        <h2 class="text-xl font-bold mb-4">上传文件</h2>
        <div class="mb-4">
          <input
            ref="fileInput"
            name="file"
            type="file"
            class="w-full p-3 border border-gray-300 rounded-lg"
          />
        </div>
        <div class="flex justify-end">
          <button
            @click="uploadFile"
            class="px-4 py-2 bg-green-500 text-white rounded hover:bg-green-700 mr-2"
          >
            上传
          </button>
          <button
            @click="showUploadDialog = false"
            class="px-4 py-2 bg-gray-500 text-white rounded hover:bg-gray-700"
          >
            取消
          </button>
        </div>
      </div>
    </div>

    <!-- 请假申请详情对话框 -->
    <DialogOfNewFolder
      :visible="visibleDialogOfNewFolder"
      :path="currentPath"
      @update="fetchFileItems(currentPath)"
      @close="visibleDialogOfNewFolder = false"
    ></DialogOfNewFolder>
  </div>
</template>
