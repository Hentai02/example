<script setup>
import { ref, onMounted } from 'vue'
import axios from 'axios'

const employee = ref({})

// 获取员工信息
function fetchEmployeeProfiles() {
  const username = JSON.parse(localStorage.getItem('user')).username
  axios
    .get(`/api/employee/username?username=${username}`)
    .then((response) => {
      employee.value = response.data
      // console.log(`${employee.value.id},jjjjjjjj`)
      localStorage.setItem('employee', JSON.stringify(employee.value))
      fetchPositionName(employee.value.positionId)
    })
    .catch((error) => {
      console.error(error)
    })
}

// 职位名称
const positionName = ref('')

// 获取职位名称
async function fetchPositionName(positionId) {
  // console.log(employee.value)
  axios
    .get(`/api/employee/position?id=${positionId}`)
    .then((response) => {
      console.log(response.data)
      positionName.value = response.data.name
    })
    .catch((error) => {
      console.error(error)
    })
}

onMounted(() => {
  fetchEmployeeProfiles()
})
</script>
<template>
  <div class="p-6 bg-white shadow rounded-lg">
    <h2 class="text-2xl font-bold mb-4">员工信息</h2>
    <div class="mb-2 flex justify-between items-center">
      <span class="font-semibold text-gray-700">姓名:</span>
      <span class="text-gray-900">{{ employee.username }}</span>
    </div>
    <div class="mb-2 flex justify-between items-center">
      <span class="font-semibold text-gray-700">工号:</span>
      <span class="text-gray-900">{{ employee.id }}</span>
    </div>
    <div class="mb-2 flex justify-between items-center">
      <span class="font-semibold text-gray-700">职位:</span>
      <span class="text-gray-900">{{ positionName }}</span>
    </div>
    <div class="mb-2 flex justify-between items-center">
      <span class="font-semibold text-gray-700">入职日期:</span>
      <span class="text-gray-900">{{ employee.hireDate }}</span>
    </div>
    <div class="mb-2 flex justify-between items-center">
      <span class="font-semibold text-gray-700">联系方式:</span>
      <span class="text-gray-900">{{ employee.phone }}</span>
    </div>
  </div>
</template>
