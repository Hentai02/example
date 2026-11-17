<script setup>
import { ref, onMounted } from 'vue'
import axios from 'axios'

const isClockedIn = ref(false)
const isClockedOut = ref(false)
const tasks = ref([])

// 获取任务列表
function fetchTasks() {
  axios
    .get('/api/task/assignee')
    .then((response) => {
      // console.log(`${response.data}kkkkk`)
      tasks.value = response.data
    })
    .catch((error) => {
      console.log(error)
    })
}

function showDetails(task) {
  console.log(task)
}

// 标记任务为已完成
function markAsCompleted(task) {
  task.completionTime = formatDate(new Date())
  task.status = 'Completed'
  axios
    .put('/api/task', task)
    .then(() => {})
    .catch((error) => {
      console.log(error)
    })
}

// 格式化日期
function formatDate(date) {
  if (!date) return ''
  return new Date(date).toISOString().slice(0, 19)
}

// 签到
function clockIn() {
  if (!isClockedIn.value) {
    const now = new Date()
    const currentHour = now.getHours()
    const currentMinute = now.getMinutes()

    // 检查是否超过 9:00
    if (currentHour > 9 || (currentHour === 9 && currentMinute > 0)) {
      alert('迟到！当前时间已超过 9:00')
      return
    }

    axios
      .post('/api/attendance/checkin')
      .then(() => {
        isClockedIn.value = true
        calculatePayroll()
        alert('打卡成功')
      })
      .catch((error) => {
        alert('打卡失败', error)
      })
  }
}

function calculatePayroll() {
  axios
    .post('/api/payroll/calculate')
    .then(() => {
      alert('工资计算成功')
    })
    .catch((error) => {
      alert('工资计算失败', error)
    })
}

// 签退
function clockOut() {
  if (!isClockedOut.value) {
    axios
      .put('/api/attendance/checkout')
      .then(() => {
        isClockedOut.value = true
        calculatePayroll()
        alert('签退成功')
      })
      .catch((error) => {
        alert('签退失败', error)
      })
  }
}

// 获取当日考勤
function getTodayAttendance() {
  axios
    .get('/api/attendance/today')
    .then((response) => {
      const today_attendance = response.data
      console.log(`today_attendance: ${response}`)
      if (today_attendance) {
        if (today_attendance.checkInTime) isClockedIn.value = true
        if (today_attendance.checkOutTime) isClockedOut.value = true
      }
    })
    .catch((error) => {
      console.log(error)
    })
}

onMounted(() => {
  fetchTasks()
  getTodayAttendance()
})
</script>

<template>
  <div class="p-6 bg-white shadow rounded-lg">
    <h2 class="text-2xl font-bold mb-4">当日任务</h2>
    <div class="flex flex-col h-full">
      <div class="mt-2">
        <button
          :disabled="isClockedIn"
          @click="clockIn"
          class="text-white px-4 py-2 rounded-lg shadow mr-2"
          :class="{
            'cursor-not-allowed bg-blue-300': isClockedIn,
            'bg-blue-500': !isClockedIn,
          }"
        >
          打卡
        </button>
        <button
          :disabled="isClockedOut"
          @click="clockOut"
          :class="{
            'cursor-not-allowed bg-red-300': isClockedOut,
            'bg-red-500': !isClockedOut,
          }"
          class="text-white px-4 py-2 rounded-lg shadow"
        >
          签退
        </button>
      </div>
      <ul class="list-disc text-gray-700 mt-2">
        <li
          v-for="(task, index) in tasks"
          :key="index"
          @click="showDetails(task)"
          class="p-4 flex justify-between items-center cursor-pointer hover:bg-gray-100"
        >
          <div>
            <p class="text-base font-medium text-gray-900">{{ task.title }}</p>
            <p class="text-xs text-gray-500">截至日期: {{ task.dueDate }}</p>
            <p v-if="task.completionTime" class="text-xs text-green-500">
              完成时间: {{ task.completionTime }}
            </p>
          </div>
          <div v-if="!task.completionTime">
            <button
              @click.stop="markAsCompleted(task)"
              class="text-xs text-green-500 hover:underline"
            >
              完成
            </button>
          </div>
        </li>
      </ul>
    </div>
  </div>
</template>
