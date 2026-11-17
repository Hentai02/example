<script setup>
import { ref } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import axios from 'axios'

const router = useRouter()
const route = useRoute()

const leave = ref(JSON.parse(route.query.leave) || {})

// 提交任务
function submit() {
  if (leave.value.id) {
    axios
      .put('/api/leave', leave.value)
      .then(() => {
        alert('提交成功！')
        close()
      })
      .catch((error) => {
        console.log(error)
      })
  } else {
    axios
      .post('/api/leave', leave.value)
      .then(() => {
        alert('提交成功！')
        close()
      })
      .catch((error) => {
        console.log(error)
      })
  }
}

const close = () => {
  // 关闭任务添加页面，导航回任务列表或其他页面
  router.push('/dashboard/workbench')
}
</script>
<template>
  <div class="p-6 min-h-screen">
    <h1 class="text-3xl font-bold mb-6">{{ leave.id ? '编辑请假信息' : '请假申请' }}</h1>
    <div class="bg-white p-6 rounded-lg shadow-lg">
      <div class="space-y-4">
        <div v-if="leave.id">
          <label class="block text-sm font-medium text-gray-700">工号</label>
          <input
            v-model="leave.employeeId"
            class="w-full p-3 border border-blue-300 text-blue-500 underline cursor-pointer rounded-lg"
          />
        </div>
        <div>
          <label class="block text-sm font-medium text-gray-700">开始日期</label>
          <input
            v-model="leave.startDate"
            type="date"
            class="w-full p-3 border border-gray-300 rounded-lg"
          />
        </div>
        <div>
          <label class="block text-sm font-medium text-gray-700">结束日期</label>
          <input
            v-model="leave.endDate"
            type="date"
            class="w-full p-3 border border-gray-300 rounded-lg"
          />
        </div>
        <div>
          <label class="block text-sm font-medium text-gray-700">请假类型</label>
          <select v-model="leave.leaveType" class="w-full p-3 border border-gray-300 rounded-lg">
            <option value="事假">事假</option>
            <option value="病假">病假</option>
            <option value="年假">年假</option>
          </select>
        </div>
        <div>
          <label class="block text-sm font-medium text-gray-700">请假原因</label>
          <input v-model="leave.reason" class="w-full p-3 border border-gray-300 rounded-lg" />
        </div>
        <div class="flex justify-end text-center">
          <button @click="submit()" class="bg-green-500 text-white px-5 py-2 rounded-lg ml-2">
            提交
          </button>
          <button @click="close" class="bg-blue-500 text-white px-5 py-2 rounded-lg ml-2">
            退出
          </button>
        </div>
      </div>
    </div>
  </div>
</template>
