<script setup>
import { ref } from 'vue'

const notifications = ref([
  {
    title: '系统更新',
    message: '系统将在今晚12点进行更新，请保存好您的工作。',
    date: '2023-10-10',
  },
  {
    title: '会议通知',
    message: '明天上午10点在会议室召开全体员工会议，请准时参加。',
    date: '2023-10-09',
  },
  {
    title: '会议通知',
    message: '明天上午10点在会议室召开全体员工会议，请准时参加。',
    date: '2023-10-09',
  },
])
</script>
<template>
  <div class="p-6 bg-white shadow rounded-lg">
    <h2 class="text-2xl font-bold mb-4">通知</h2>
    <div
      v-for="(notification, index) in notifications"
      :key="index"
      class="mb-4 p-4 bg-blue-50 border-l-4 border-blue-500"
    >
      <p class="text-blue-700 font-semibold">{{ notification.title }}</p>
      <p class="text-blue-700">{{ notification.message }}</p>
      <p class="text-sm text-gray-500">{{ notification.date }}</p>
    </div>
  </div>
</template>
