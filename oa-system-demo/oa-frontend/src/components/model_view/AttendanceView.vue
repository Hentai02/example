<script setup>
import { ref } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import axios from 'axios'

const router = useRouter() // 路由
const route = useRoute() // 路由参数
const attendance = ref(JSON.parse(route.query.attendance) || {}) // 考勤记录

// 提交任务
function submit() {
  if (attendance.value.id) {
    axios
      .put('/api/attendance', attendance.value)
      .then(() => {
        close()
      })
      .catch((error) => {
        console.log(error)
      })
  } else {
    axios
      .post('/api/attendance', attendance.value)
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
  router.push('/dashboard/tab4/attendanceManagement')
}
</script>

<template>
  <div class="p-6 min-h-screen">
    <h1 class="text-3xl font-bold mb-6">编辑考勤记录</h1>
    <div class="bg-white p-6 rounded-lg shadow-lg">
      <div class="space-y-4">
        <div @click="viewEmployeeDetail()">
          <label class="block text-sm font-medium text-gray-700">工号</label>
          <input
            v-model="attendance.employeeId"
            class="w-full p-3 border border-blue-300 text-blue-500 underline cursor-pointer rounded-lg"
          />
        </div>
        <div>
          <label class="block text-sm font-medium text-gray-700">签到时间</label>
          <input
            v-model="attendance.checkInTime"
            type="datetime-local"
            class="w-full p-3 border border-gray-300 rounded-lg"
          />
        </div>
        <div>
          <label class="block text-sm font-medium text-gray-700">签退时间</label>
          <input
            v-model="attendance.checkOutTime"
            type="datetime-local"
            class="w-full p-3 border border-gray-300 rounded-lg"
          />
        </div>
        <div>
          <label class="block text-sm font-medium text-gray-700">状态</label>
          <select v-model="attendance.status" class="w-full p-3 border border-gray-300 rounded-lg">
            <option value="出勤">出勤</option>
            <option value="迟到">迟到</option>
            <option value="缺勤">缺勤</option>
          </select>
        </div>
        <div class="flex justify-end text-center">
          <button @click="submit" class="bg-green-500 text-white px-5 py-2 rounded-lg mr-2">
            提交
          </button>
          <button @click="close" class="bg-blue-500 text-white px-5 py-2 rounded-lg">关闭</button>
        </div>
      </div>
    </div>
  </div>
</template>
