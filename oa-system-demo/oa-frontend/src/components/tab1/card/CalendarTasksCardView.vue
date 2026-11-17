<script setup>
import { ref, onMounted } from 'vue'
import VueCal from 'vue-cal'
import 'vue-cal/dist/vuecal.css'
import axios from 'axios'

const events = ref([])

// 获取当前员工 ID
const currentEmployeeId = localStorage.getItem('employee')
  ? JSON.parse(localStorage.getItem('employee')).id.toString()
  : null

// 获取日程记录
function fetchEvents() {
  // console.log(`${currentEmployeeId}pppp`)
  axios
    .get('/api/schedule/all')
    .then((response) => {
      const schedules = response.data

      if (schedules.length > 0) {
        for (let i = 0; i < schedules.length; i++) {
          // 格式化 sharedWith 为列表
          const sharedWithList = schedules[i].sharedWith
            ? schedules[i].sharedWith.split(',').map((id) => id.trim())
            : []
          console.log(sharedWithList)
          console.log(currentEmployeeId)
          if (sharedWithList.includes(currentEmployeeId)) {
            console.log('包含当前员工 ID')
            events.value.push({
              start: formatDateTime(schedules[i].startTime),
              end: formatDateTime(schedules[i].endTime),
              title: schedules[i].title,
              content: `<i class="text-orange-700">${schedules[i].description}</i>`,
              class: 'leisure',
            })
          }
        }
      }
      console.log(schedules)
      console.log(events.value)
    })
    .catch((error) => {
      console.log(error)
    })

  // axios
  //   .get('/api/attendance/today')
  //   .then((response) => {
  //     const today_attendance = response.data
  //     if (today_attendance) {
  //       if (today_attendance.checkOutTime) {
  //         events.value.push({
  //           start: response.data.checkInTime,
  //           end: response.data.checkOutTime,
  //           title: response.data.status,
  //         })
  //       } else {
  //         events.value.push({
  //           start: response.data.checkInTime,
  //           end: response.data.checkInTime,
  //           title: response.data.status,
  //         })
  //       }
  //     }
  //     console.log(response.data)
  //     console.log(events.value)
  //   })
  //   .catch((error) => {
  //     console.log(error)
  //   })
}

onMounted(() => {
  fetchEvents()
})

// 格式化日期时间为 yyyy-MM-dd HH:mm
function formatDateTime(dateTime) {
  const date = new Date(dateTime)
  const pad = (n) => (n < 10 ? '0' + n : n)
  return `${date.getFullYear()}-${pad(date.getMonth() + 1)}-${pad(date.getDate())} ${pad(
    date.getHours(),
  )}:${pad(date.getMinutes())}`
}
</script>

<template>
  <div class="p-6 bg-white shadow rounded-lg">
    <h2 class="text-2xl font-bold mb-4">日历和日程</h2>
    <div class="flex flex-col min-h-90">
      <vue-cal
        class="flex-grow mt-2 vuecal--full-height-delete"
        xsmall
        locale="zh-cn"
        hide-weekends
        :time="false"
        active-view="month"
        :events="events"
      />

      <!-- <vue-cal
          class="vuecal--rounded-theme vuecal--green-theme mt-2"
          xsmall
          hide-weekends
          :time="false"
          active-view="month"
          :disable-views="['week']"
          :events="events"
          @day-click="onDayClick"
          style="width: 100%; height: 300px"
        /> -->
    </div>
  </div>
</template>
