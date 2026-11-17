<script setup>
import { ref } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import axios from 'axios'

const router = useRouter()
const route = useRoute()

const payroll = ref(JSON.parse(route.query.payroll) || {})

// 提交酬薪
function submit() {
  if (payroll.value.id) {
    axios
      .put('/api/payroll', payroll.value)
      .then(() => {
        close()
      })
      .catch((error) => {
        console.log(error)
      })
  } else {
    axios
      .post('/api/payroll', payroll.value)
      .then(() => {
        close()
      })
      .catch((error) => {
        console.log(error)
      })
  }
}

function calculator(employeeId) {
  axios
    .post(`/api/payroll/calculate?employee_id=${employeeId}`)
    .then(() => {
      close()
    })
    .catch((error) => {
      console.error(error)
    })
}

const close = () => {
  // 关闭酬薪添加页面，导航回酬薪列表或其他页面
  router.push('/dashboard/tab4/salaryManagement')
}
</script>

<template>
  <div class="p-6 min-h-screen">
    <h1 class="text-3xl font-bold mb-6">{{ payroll.id ? '编辑酬薪' : '新增酬薪' }}</h1>
    <div class="bg-white p-6 rounded-lg shadow-lg">
      <div class="mb-4">
        <label class="block text-sm font-medium text-gray-700">工号</label>
        <input
          type="text"
          v-model="payroll.employeeId"
          class="w-full p-3 border border-gray-300 rounded-lg"
        />
      </div>
      <div class="mb-4">
        <label class="block text-sm font-medium text-gray-700">基础工资</label>
        <input
          type="double"
          v-model="payroll.baseSalary"
          class="w-full p-3 border border-gray-300 rounded-lg"
        />
      </div>
      <div class="mb-4">
        <label class="block text-sm font-medium text-gray-700">加班工资</label>
        <input
          type="double"
          v-model="payroll.overtimeSalary"
          class="w-full p-3 border border-gray-300 rounded-lg"
        />
      </div>
      <div class="mb-4">
        <label class="block text-sm font-medium text-gray-700">奖金</label>
        <input
          type="double"
          v-model="payroll.bonus"
          class="w-full p-3 border border-gray-300 rounded-lg"
        />
      </div>

      <div class="mb-4">
        <label class="block text-sm font-medium text-gray-700">扣除</label>
        <input
          type="double"
          v-model="payroll.deductions"
          class="w-full p-3 border border-gray-300 rounded-lg"
        />
      </div>
      <div class="mb-4">
        <label class="block text-sm font-medium text-gray-700">总工资</label>
        <input
          type="double"
          v-model="payroll.totalSalary"
          class="w-full p-3 border border-gray-300 rounded-lg"
        />
      </div>
      <div class="flex justify-end">
        <button @click="submit" class="bg-green-500 text-white px-5 py-2 rounded-lg ml-2">
          提交
        </button>
        <button
          @click="calculator(payroll.employeeId)"
          class="bg-green-500 text-white px-5 py-2 rounded-lg ml-2"
        >
          计算
        </button>
        <button @click="close" class="bg-blue-500 text-white px-5 py-2 rounded-lg ml-2">
          关闭
        </button>
      </div>
    </div>
  </div>
</template>
