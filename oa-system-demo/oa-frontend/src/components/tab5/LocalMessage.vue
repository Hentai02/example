<script setup>
import axios from 'axios'
import { ref, onMounted } from 'vue'

const messages = ref([]) //
const showMessageDialog = ref(false) //
const currentMessage = ref({})

function fetchMessages() {
  // axios
  //   .get('/api/message')
  //   .then((response) => {
  //     messages.value = response.data
  //   })
  //   .catch((error) => {
  //     console.error(error)
  //   })

  axios
    .get('/api/message', {
      params: {
        page: page.value,
        size: size.value,
        sort: sort.value,
        keyword: searchKeyword.value,
      },
    })
    .then((response) => {
      messages.value = response.data.content
      totalPages.value = response.data.totalPages
    })
    .catch((error) => {
      console.error(error)
    })
}

function openMessageDialog(message) {
  currentMessage.value = message
  showMessageDialog.value = true
  markAsRead(message.id)
}

function closeMessageDialog() {
  showMessageDialog.value = false
}

function markAsRead(id) {
  const message = messages.value.find((msg) => msg.id === id)
  if (message) {
    console.log(message)
    console.log(message.value)
    message.read = true
    axios
      .put('/api/message', message)
      .then(() => {
        close()
      })
      .catch((error) => {
        console.log(error)
      })
    console.log(message)
  }
}

function deleteMessage(id) {
  axios
    .delete(`/api/message/${id}`)
    .then(() => {
      fetchMessages()
    })
    .catch((error) => {
      console.error(error)
    })
}

const page = ref(0)
const size = ref(5)
const sort = ref('created_at,desc')
const totalPages = ref(0)
const searchKeyword = ref('')

// 上一页
function prevPage() {
  if (page.value > 0) {
    page.value--
    fetchMessages()
  }
}

// 下一页
function nextPage() {
  page.value++
  fetchMessages()
}

onMounted(() => {
  fetchMessages()
})
</script>
<template>
  <div class="p-6 min-h-screen">
    <h1 class="text-3xl font-bold mb-6">站内信</h1>
    <div class="flex justify-end mb-4">
      <RouterLink
        to="/dashboard/tab2/message?message={}"
        class="inline-flex items-center px-4 py-2 border border-transparent text-sm font-medium rounded-md text-white bg-indigo-600 hover:bg-indigo-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-indigo-500"
      >
        发送通知
      </RouterLink>
    </div>
    <input
      type="text"
      v-model="searchKeyword"
      @input="fetchMessages"
      placeholder="搜索"
      class="w-full p-3 border border-gray-300 rounded-lg mb-4"
    />
    <div class="bg-white shadow overflow-hidden sm:rounded-lg p-6">
      <ul class="divide-y divide-gray-200">
        <li v-for="message in messages" :key="message.id" class="py-3 flex items-start space-x-4">
          <div
            class="w-10 h-10 flex-shrink-0 bg-gray-200 rounded-full flex items-center justify-center text-gray-500"
          >
            {{ message.receiverId }}
          </div>
          <div class="flex-1 cursor-pointer" @click="openMessageDialog(message)">
            <p
              class="text-sm font-medium"
              :class="{ 'text-gray-900': !message.read, 'text-gray-500': message.read }"
            >
              {{ message.subject }}
            </p>
            <p class="text-sm text-gray-600 truncate">{{ message.content }}</p>
            <p class="text-xs text-gray-400">{{ message.createdAt }}</p>
          </div>

          <div class="flex space-x-2">
            <button
              v-if="!message.read"
              @click="markAsRead(message.id)"
              class="text-xs text-blue-500 hover:underline"
            >
              标记已读
            </button>
            <button @click="deleteMessage(message.id)" class="text-xs text-red-500 hover:underline">
              删除
            </button>
          </div>
        </li>
      </ul>
    </div>
    <div class="mt-4">
      <button
        @click="prevPage"
        :disabled="page === 0"
        class="px-4 py-2 bg-blue-500 text-white rounded-lg"
      >
        上一页
      </button>
      <span class="m-4">第 {{ page + 1 }} 页，共 {{ totalPages }} 页</span>
      <button
        @click="nextPage"
        :disabled="page >= totalPages - 1"
        class="px-4 py-2 bg-blue-500 text-white rounded-lg"
      >
        下一页
      </button>
    </div>

    <!-- 阅读通知对话框 -->
    <div
      v-if="showMessageDialog"
      class="fixed inset-0 bg-opacity-50 backdrop-blur-sm flex items-center justify-center"
    >
      <div class="bg-white rounded-lg shadow-lg p-6 w-full max-w-md">
        <h2 class="text-2xl font-semibold mb-4">{{ currentMessage.subject }}</h2>
        <p class="text-sm text-gray-700 mb-4">{{ currentMessage.content }}</p>
        <div class="flex justify-end">
          <button
            @click="closeMessageDialog"
            class="inline-flex items-center px-4 py-2 border border-transparent text-sm font-medium rounded-md text-gray-700 bg-gray-200 hover:bg-gray-300 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-gray-500"
          >
            关闭
          </button>
        </div>
      </div>
    </div>
  </div>
</template>
