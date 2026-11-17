<script setup>
import { ref } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import axios from 'axios'

const router = useRouter()
const route = useRoute()

const task = ref(JSON.parse(route.query.task) || {})

// 提交任务
function submit() {
  if (task.value.id) {
    axios
      .put('/api/task', task.value)
      .then(() => {
        close()
      })
      .catch((error) => {
        console.log(error)
      })
  } else {
    axios
      .post('/api/task', task.value)
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
  router.push('/dashboard/tab3/taskManagement')
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

fetchEmployeeProfiles()
</script>

<template>
  <div class="p-6 min-h-screen">
    <h1 class="text-3xl font-bold mb-6">{{ task.id ? '编辑任务' : '新增任务' }}</h1>
    <div class="bg-white p-6 rounded-lg shadow-lg">
      <div class="mb-4">
        <label class="block text-sm font-medium text-gray-700">标题</label>
        <input
          type="text"
          v-model="task.title"
          class="w-full p-3 border border-gray-300 rounded-lg"
        />
      </div>
      <div class="mb-4">
        <label class="block text-sm font-medium text-gray-700">描述</label>
        <textarea
          type="text"
          v-model="task.description"
          class="w-full p-3 border border-gray-300 rounded-lg"
        ></textarea>
      </div>
      <div class="mb-4">
        <label class="block text-sm font-medium text-gray-700">截止日期</label>
        <input
          type="datetime-local"
          v-model="task.dueDate"
          class="w-full p-3 border border-gray-300 rounded-lg"
        />
      </div>
      <div class="mb-4">
        <label class="block text-sm font-medium text-gray-700">优先级</label>
        <!-- <input
          type="text"
          v-model="task.priority"
          class="w-full p-3 border border-gray-300 rounded-lg"
        /> -->
        <select
          required
          v-model="task.priority"
          class="w-full p-3 border border-gray-300 rounded-lg"
        >
          <option value="Low">低</option>
          <option value="Medium">中</option>
          <option value="High">高</option>
        </select>
      </div>

      <div class="mb-4">
        <label class="block text-sm font-medium text-gray-700">状态</label>
        <!-- <input
          type="text"
          v-model="task.status"
          class="w-full p-3 border border-gray-300 rounded-lg"
        /> -->
        <select required v-model="task.status" class="w-full p-3 border border-gray-300 rounded-lg">
          <option value="Pending">待办</option>
          <option value="In Progress">进行中</option>
          <option value="Completed">已完成</option>
          <option value="Canceled">取消</option>
        </select>
      </div>
      <div class="mb-4">
        <label class="block text-sm font-medium text-gray-700">执行人</label>
        <!-- <input
          type="text"
          v-model="task.assigneeId"
          class="w-full p-3 border border-gray-300 rounded-lg"
        /> -->

        <select v-model="task.assigneeId" class="w-full p-3 border border-gray-300 rounded-lg mb-4">
          <option v-for="employee in employees" :key="employee.id" :value="employee.id">
            {{ employee.username }}
          </option>
        </select>
      </div>
      <div class="flex justify-end">
        <button @click="submit" class="bg-green-500 text-white px-5 py-2 rounded-lg ml-2">
          提交
        </button>
        <button @click="close" class="bg-blue-500 text-white px-5 py-2 rounded-lg ml-2">
          关闭
        </button>
      </div>
    </div>
  </div>
</template>
