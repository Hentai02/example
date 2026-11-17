<template>
  <div>
    <table class="min-w-full table-auto border-collapse border border-gray-200">
      <thead>
        <tr class="bg-gray-100">
          <th class="px-4 py-2 border-b text-left text-sm font-medium text-gray-600">开始时间</th>
          <th class="px-4 py-2 border-b text-left text-sm font-medium text-gray-600">结束时间</th>
          <th class="px-4 py-2 border-b text-left text-sm font-medium text-gray-600">加班日期</th>
          <th class="px-4 py-2 border-b text-left text-sm font-medium text-gray-600">原因</th>
          <th class="px-4 py-2 border-b text-left text-sm font-medium text-gray-600">状态</th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="record in approvals" :key="record.id" class="hover:bg-gray-50">
          <td class="px-4 py-2 border-b text-sm text-gray-700">{{ record.startTime }}</td>
          <td class="px-4 py-2 border-b text-sm text-gray-700">{{ record.endTime }}</td>
          <td class="px-4 py-2 border-b text-sm text-gray-700">{{ record.overtimeDate }}</td>
          <td class="px-4 py-2 border-b text-sm text-gray-700">{{ record.reason }}</td>
          <td class="px-4 py-2 border-b text-sm text-gray-700">{{ record.status }}</td>
        </tr>
      </tbody>
    </table>
  </div>
</template>

<script setup>
// filepath: /d:/Code/VSCodeProjects/oa-system/src/components/OvertimeRecords.vue
import { ref, onMounted } from 'vue'
import axios from 'axios'

// 获取加班报销记录
const approvals = ref([])
function fetchApprovalRecords() {
  axios
    .get('/api/overtime')
    .then(function (response) {
      console.log(response.data)
      approvals.value = response.data
    })
    .catch(function (error) {
      console.log(error)
    })
}

// 在组件挂载时获取加班报销记录
onMounted(() => {
  fetchApprovalRecords()
})
</script>
