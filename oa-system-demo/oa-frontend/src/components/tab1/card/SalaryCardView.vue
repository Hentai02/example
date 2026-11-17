<script setup>
import { ref, onMounted } from 'vue'
import axios from 'axios'

const payroll = ref({
  baseSalary: 0,
  overtimeSalary: 0,
  bonus: 0,
  deductions: 0,
  totalSalary: 0,
})

function fetchTodayPayroll() {
  axios
    .get('/api/payroll')
    .then((response) => {
      payroll.value = response.data
    })
    .catch((error) => {
      console.error(error)
    })
}

onMounted(() => {
  fetchTodayPayroll()
})
</script>
<template>
  <div class="p-6 bg-white shadow rounded-lg">
    <h2 class="text-2xl font-bold mb-4">工资详情</h2>
    <div class="mb-2 flex justify-between items-center">
      <span class="font-semibold text-gray-700">基础工资:</span>
      <span class="text-gray-900">{{ payroll.baseSalary }} 元</span>
    </div>
    <div class="mb-2 flex justify-between items-center">
      <span class="font-semibold text-gray-700">加班工资:</span>
      <span class="text-gray-900">{{ payroll.overtimeSalary }} 元</span>
    </div>
    <div class="mb-2 flex justify-between items-center">
      <span class="font-semibold text-gray-700">奖金:</span>
      <span class="text-gray-900">{{ payroll.bonus }} 元</span>
    </div>
    <div class="mb-2 flex justify-between items-center">
      <span class="font-semibold text-gray-700">扣除:</span>
      <span class="text-gray-900">{{ payroll.deductions }} 元</span>
    </div>
    <div class="mt-4 border-t pt-4 flex justify-between items-center">
      <span class="font-bold text-lg text-gray-800">总工资:</span>
      <span class="text-lg text-gray-900">{{ payroll.totalSalary }} 元</span>
    </div>
  </div>
</template>
