<script setup>
import axios from 'axios'
import { ref, defineEmits } from 'vue'
import { useRouter } from 'vue-router'

const router = useRouter()

const request = ref({})

const emit = defineEmits(['updated', 'close'])

const submit = () => {
  axios
    .post('/api/overtime', request.value)
    .then(() => {
      request.value = {}
      alert('提交成功！')
      emit('updated')
      // emit('close') // 关闭对话框
      close()
    })
    .catch((error) => {
      console.log(error)
    })
}

const close = () => {
  // 关闭加班申请页面，导航回工作台或其他页面

  router.push('/dashboard/workbench')
}
</script>

<template>
  <div class="p-6 min-h-screen">
    <h1 class="text-3xl font-bold mb-6">加班申请</h1>
    <div class="bg-white p-6 rounded-lg shadow-lg">
      <div class="mb-4">
        <label class="block text-sm font-medium text-gray-700">加班日期</label>
        <input
          type="date"
          v-model="request.overtimeDate"
          class="w-full p-3 border border-gray-300 rounded-lg"
        />
      </div>
      <div class="mb-4">
        <label class="block text-sm font-medium text-gray-700">开始时间</label>
        <input
          type="time"
          v-model="request.startTime"
          class="w-full p-3 border border-gray-300 rounded-lg"
        />
      </div>
      <div class="mb-4">
        <label class="block text-sm font-medium text-gray-700">结束时间</label>
        <input
          type="time"
          v-model="request.endTime"
          class="w-full p-3 border border-gray-300 rounded-lg"
        />
      </div>
      <div class="mb-4">
        <label class="block text-sm font-medium text-gray-700">加班原因</label>
        <textarea
          v-model="request.reason"
          class="w-full p-3 border border-gray-300 rounded-lg"
          rows="3"
        ></textarea>
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
