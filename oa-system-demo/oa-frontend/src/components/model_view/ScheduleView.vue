<script setup>
import { ref } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import axios from 'axios'

const router = useRouter()
const route = useRoute()

const schedule = ref(JSON.parse(route.query.schedule) || {})

// 提交日程
function submit() {
  if (!schedule.value.title) {
    alert('标题不能为空')
    return
  }
  if (!schedule.value.description) {
    alert('描述不能为空')
    return
  }
  if (!schedule.value.startTime) {
    alert('开始日期不能为空')
    return
  }
  if (!schedule.value.endTime) {
    alert('结束日期不能为空')
    return
  }
  if (!schedule.value.reminderTime) {
    alert('通知时间不能为空')
    return
  }
  if (!schedule.value.category) {
    alert('类别不能为空')
    return
  }
  if (!schedule.value.sharedWith) {
    alert('共享用户列表不能为空')
    return
  }

  if (schedule.value.id) {
    axios
      .put('/api/schedule', schedule.value)
      .then(() => {
        alert('提交成功！')
        close()
      })
      .catch((error) => {
        console.log(error)
      })
  }
  axios
    .post('/api/schedule', schedule.value)
    .then(() => {
      close()
    })
    .catch((error) => {
      console.log(error)
    })
}

const close = () => {
  // 关闭日程添加页面，导航回日程列表或其他页面
  router.push('/dashboard/tab3/scheduleManagement')
}
</script>

<template>
  <div class="p-6 min-h-screen">
    <h1 class="text-3xl font-bold mb-6">添加日程</h1>
    <div class="bg-white p-6 rounded-lg shadow-lg">
      <div class="mb-4">
        <label class="block text-sm font-medium text-gray-700">标题</label>
        <input
          type="text"
          v-model="schedule.title"
          required
          class="w-full p-3 border border-gray-300 rounded-lg"
        />
      </div>
      <div class="mb-4">
        <label class="block text-sm font-medium text-gray-700">描述</label>
        <textarea
          type="text"
          v-model="schedule.description"
          class="w-full p-3 border border-gray-300 rounded-lg"
          required
        ></textarea>
      </div>
      <div class="mb-4">
        <label class="block text-sm font-medium text-gray-700">开始日期</label>
        <input
          type="datetime-local"
          v-model="schedule.startTime"
          required
          class="w-full p-3 border border-gray-300 rounded-lg"
        />
      </div>

      <div class="mb-4">
        <label class="block text-sm font-medium text-gray-700">结束日期</label>
        <input
          v-model="schedule.endTime"
          type="datetime-local"
          required
          class="w-full p-3 border border-gray-300 rounded-lg"
        />
      </div>

      <div class="mb-4">
        <label class="block text-sm font-medium text-gray-700">通知时间</label>
        <input
          v-model="schedule.reminderTime"
          type="datetime-local"
          required
          class="w-full p-3 border border-gray-300 rounded-lg"
        />
      </div>

      <div class="mb-4">
        <label class="block text-sm font-medium text-gray-700">类别</label>
        <!-- <input
          type="text"
          v-model="schedule.category"
          required
          class="w-full p-3 border border-gray-300 rounded-lg"
        /> -->

        <select
          required
          v-model="schedule.category"
          class="w-full p-3 border border-gray-300 rounded-lg"
        >
          <option value="工作">工作</option>
          <option value="个人">会议</option>
          <option value="学习">学习</option>
        </select>
      </div>

      <div class="mb-4">
        <label class="block text-sm font-medium text-gray-700">共享用户列表，例如:23,21,20</label>
        <input
          type="text"
          v-model="schedule.sharedWith"
          required
          class="w-full p-3 border border-gray-300 rounded-lg"
        />
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
