<script setup>
import { onMounted, ref } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import axios from 'axios'

const router = useRouter()
const route = useRoute()

const employee = ref(JSON.parse(route.query.employee) || {})
const password = ref('')

// 提交员工
function submit() {
  if (employee.value.id) {
    axios
      .put(`/api/employee?password=${password.value}`, employee.value)
      .then(() => {
        close()
      })
      .catch((error) => {
        console.log(error)
      })
  } else {
    axios
      .post(`/api/employee?password=${password.value}`, employee.value)
      .then(() => {
        close()
      })
      .catch((error) => {
        console.log(error)
      })
  }
}

const positions = ref([])
function fetchPosition() {
  axios
    .get('/api/employee/positions')
    .then((response) => {
      console.log(response.data)
      positions.value = response.data
    })
    .catch((error) => {
      console.error(error)
    })
}

const close = () => {
  // 关闭员工添加页面，导航回员工列表或其他页面
  router.push('/dashboard/tab4/employeeManagement')
}

onMounted(() => {
  fetchPosition()
})
</script>

<template>
  <div class="p-6 min-h-screen">
    <h1 class="text-3xl font-bold mb-6">{{ employee.id ? '编辑员工' : '新增员工' }}</h1>
    <div class="bg-white p-6 rounded-lg shadow-lg">
      <!-- <div class="mb-4" v-if="employee.id">
        <label class="block text-sm font-medium text-gray-700">工号</label>
        <input
          type="text"
          v-model="employee.id"
          class="w-full p-3 border border-gray-300 rounded-lg"
        />
      </div> -->
      <div class="mb-4">
        <label class="block text-sm font-medium text-gray-700">用户名</label>
        <input
          type="text"
          v-model="employee.username"
          class="w-full p-3 border border-gray-300 rounded-lg"
        />
      </div>
      <div class="mb-4">
        <label class="block text-sm font-medium text-gray-700">密码</label>
        <input
          type="text"
          v-model="password"
          class="w-full p-3 border border-gray-300 rounded-lg"
        />
      </div>
      <div class="mb-4">
        <label class="block text-sm font-medium text-gray-700">职位</label>
        <!-- <input
          type="text"
          v-model="employee.positionId"
          class="w-full p-3 border border-gray-300 rounded-lg"
        /> -->
        <select
          v-model="employee.positionId"
          id="position"
          class="w-full p-3 border border-gray-300 rounded-lg"
        >
          <option v-for="position in positions" :key="position.id" :value="position.id">
            {{ position.name }}
          </option>
        </select>
      </div>
      <div class="mb-4">
        <label class="block text-sm font-medium text-gray-700">性别</label>
        <!-- <input
          type="text"
          v-model="employee.gender"
          class="w-full p-3 border border-gray-300 rounded-lg"
        /> -->
        <select v-model="employee.gender" class="w-full p-3 border border-gray-300 rounded-lg">
          <option value="false">男</option>
          <option value="true">女</option>
        </select>
      </div>

      <div class="mb-4">
        <label class="block text-sm font-medium text-gray-700">出生日期</label>
        <input
          type="date"
          v-model="employee.birthDate"
          class="w-full p-3 border border-gray-300 rounded-lg"
        />
      </div>
      <div class="mb-4">
        <label class="block text-sm font-medium text-gray-700">入职日期</label>
        <input
          type="date"
          v-model="employee.hireDate"
          class="w-full p-3 border border-gray-300 rounded-lg"
        />
      </div>
      <div class="mb-4">
        <label class="block text-sm font-medium text-gray-700">手机号</label>
        <input
          type="text"
          v-model="employee.phone"
          class="w-full p-3 border border-gray-300 rounded-lg"
        />
      </div>
      <div class="mb-4">
        <label class="block text-sm font-medium text-gray-700">邮箱</label>
        <input
          type="text"
          v-model="employee.email"
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
