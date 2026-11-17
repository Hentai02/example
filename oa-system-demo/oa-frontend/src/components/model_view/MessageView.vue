<script setup>
import { ref, onMounted } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import axios from 'axios'

const router = useRouter()
const route = useRoute()

const message = ref(JSON.parse(route.query.message) || {})

// 提交任务
function submit() {
  if (message.value.id) {
    axios
      .put('/api/message', message.value)
      .then(() => {
        close()
      })
      .catch((error) => {
        console.log(error)
      })
  } else {
    axios
      .post('/api/message', message.value)
      .then(() => {
        close()
      })
      .catch((error) => {
        console.log(error)
      })
  }
}

const close = () => {
  // 关闭任务添加页面，导航回任务列表或其他页面
  router.push('/dashboard/localMessage')
}

const employees = ref([])
async function fetchEmployeeProfiles() {
  try {
    const response = await axios.get(`/api/employee/all`)
    employees.value = response.data.content
  } catch (error) {
    console.error(error)
  }
}

onMounted(() => {
  fetchEmployeeProfiles()
})
</script>

<template>
  <div class="p-6 min-h-screen">
    <h1 class="text-3xl font-bold mb-4">发送通知</h1>
    <div class="bg-white p-6 rounded-lg shadow-lg">
      <div class="mb-6">
        <label class="block text-sm font-medium text-gray-700">收信人</label>
        <!-- <select
          id="recipientType"
          v-model="recipientType"
          class="mt-1 block w-full border border-gray-300 rounded-md shadow-sm focus:ring-indigo-500 focus:border-indigo-500 sm:text-sm"
        >
          <option value="group">用户组</option>
          <option value="user">单个用户</option>
        </select> -->
        <!-- <input
          type="text"
          v-model="message.receiverId"
          class="w-full p-3 border border-gray-300 rounded-lg"
        /> -->

        <select
          v-model="message.receiverId"
          class="w-full p-3 border border-gray-300 rounded-lg mb-4"
        >
          <!-- <option value="">所有员工</option> -->
          <option v-for="employee in employees" :key="employee.id" :value="employee.id">
            {{ employee.username }}
          </option>
        </select>
      </div>

      <!-- <div v-if="recipientType === 'group'" class="mb-6">
        <label for="group" class="block text-sm font-medium text-gray-700">选择用户组</label>
        <select
          id="group"
          v-model="selectedGroup"
          class="mt-1 block w-full border border-gray-300 rounded-md shadow-sm focus:ring-indigo-500 focus:border-indigo-500 sm:text-sm"
        >
          <option v-for="group in userGroups" :key="group" :value="group">{{ group }}</option>
        </select>
      </div> -->

      <!-- <div v-if="recipientType === 'user'" class="mb-6">
        <label for="user" class="block text-sm font-medium text-gray-700">选择用户</label>
        <select
          id="user"
          v-model="selectedUser"
          class="mt-1 block w-full border border-gray-300 rounded-md shadow-sm focus:ring-indigo-500 focus:border-indigo-500 sm:text-sm"
        >
          <option v-for="user in users" :key="user.id" :value="user.id">{{ user.name }}</option>
        </select>
      </div> -->

      <div class="mb-6">
        <label for="subject" class="block text-sm font-medium text-gray-700">主题</label>
        <input
          type="text"
          id="subject"
          v-model="message.subject"
          class="w-full p-3 border border-gray-300 rounded-lg"
        />
      </div>
      <div class="mb-6">
        <label for="message" class="block text-sm font-medium text-gray-700">内容</label>
        <textarea
          id="message"
          v-model="message.content"
          rows="4"
          class="mt-1 block w-full border border-gray-300 rounded-md shadow-sm focus:ring-indigo-500 focus:border-indigo-500 sm:text-sm"
        ></textarea>
      </div>

      <div class="flex justify-end">
        <button
          @click="submit"
          class="mr-2 inline-flex items-center px-4 py-2 border border-transparent text-sm font-medium rounded-md text-gray-700 bg-gray-200 hover:bg-gray-300 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-gray-500"
        >
          发送通知
        </button>
        <button
          @click="close"
          class="inline-flex items-center px-4 py-2 border border-transparent text-sm font-medium rounded-md text-white bg-green-600 hover:bg-green-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-green-500"
        >
          关闭
        </button>
      </div>
    </div>
  </div>
</template>
