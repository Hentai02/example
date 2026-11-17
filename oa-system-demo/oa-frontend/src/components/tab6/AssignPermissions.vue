<script setup>
import { ref, onMounted } from 'vue'
import { useRoute } from 'vue-router'
import axios from 'axios'

const route = useRoute()

const file = ref(JSON.parse(route.query.file) || {})
const employees = ref([])
const selectedEmployee = ref({})
const permissions = ref({ read: false, write: false })

function fetchEmployees() {
  axios
    .get('/api/employee/all')
    .then((response) => {
      console.log(response.data)
      employees.value = response.data.content
    })
    .catch((error) => {
      console.error(error)
    })
}

const assignedEmployees = ref([])
function fetchAssignedEmployees() {
  axios
    .get(`/api/files/file-employees?file_id=${file.value.id}`)
    .then((response) => {
      response.data.forEach((employeePermission) => {
        fetchEmployee(employeePermission.employeeId, employeePermission)
      })
    })
    .catch((error) => {
      console.error(error)
    })
}

function fetchEmployee(id, employeePermission) {
  axios
    .get(`/api/employee/${id}`)
    .then((response) => {
      const assignedEmployee = {
        id: employeePermission.id,
        username: response.data.username,
        read: employeePermission.read,
        write: employeePermission.write,
      }
      assignedEmployees.value.push(assignedEmployee)
      // console.log(assignedEmployees.value)
    })
    .catch((error) => {
      console.error(error)
    })
}

function fetchEmployeePermissions() {
  if (!selectedEmployee.value.id) return

  axios
    .get(
      `/api/files/file-permissions?employee_id=${selectedEmployee.value.id}&file_id=${file.value.id}`,
    )
    .then((response) => {
      // console.log(response.data)
      permissions.value = response.data || { read: false, write: false }
    })
    .catch((error) => {
      console.error(error)
    })
}

function assignPermissions() {
  const payload = {
    id: permissions.value.id,
    fileId: file.value.id,
    employeeId: selectedEmployee.value.id,
    read: permissions.value.read,
    write: permissions.value.write,
  }

  axios
    .post('/api/files/assign-permissions', payload)
    .then(() => {
      assignedEmployees.value = []
      fetchAssignedEmployees()
      alert('权限分配成功')
    })
    .catch((error) => {
      alert('权限不足，请联系管理员')
      console.error(error)
    })
}

onMounted(() => {
  fetchEmployees()
  fetchAssignedEmployees()
})
</script>
<template>
  <div class="p-6 bg-white shadow rounded-lg">
    <h2 class="text-2xl font-bold mb-4">分配权限</h2>
    <div class="mb-4">
      <label class="block text-sm font-medium text-gray-700">文件名</label>
      <input
        type="text"
        :disabled="file.id"
        v-model="file.name"
        class="w-full p-3 border border-gray-300 rounded-lg"
      />
    </div>
    <div class="mb-4">
      <label class="block text-sm font-medium text-gray-700">分配用户</label>
      <select
        v-model="selectedEmployee"
        @change="fetchEmployeePermissions"
        class="w-full p-3 border border-gray-300 rounded-md shadow-sm focus:ring-indigo-500 focus:border-indigo-500 sm:text-sm"
      >
        <option v-for="item in employees" :key="item.id" :value="item">{{ item.username }}</option>
      </select>
    </div>
    <div class="mb-4">
      <label class="block text-sm font-medium text-gray-700">设置权限</label>
      <div class="mt-2">
        <label class="inline-flex items-center">
          <input type="checkbox" v-model="permissions.read" class="form-checkbox" />
          <span class="ml-2">读</span>
        </label>
        <label class="inline-flex items-center ml-4">
          <input type="checkbox" v-model="permissions.write" class="form-checkbox" />
          <span class="ml-2">写</span>
        </label>
      </div>
    </div>
    <div class="flex justify-end">
      <button
        @click="assignPermissions"
        class="inline-flex items-center px-4 py-2 border border-transparent text-sm font-medium rounded-md text-white bg-indigo-600 hover:bg-indigo-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-indigo-500"
      >
        分配权限
      </button>
    </div>
    <div class="mt-6">
      <h3 class="text-lg font-semibold">已分配的员工</h3>
      <ul class="mt-2">
        <li v-for="item in assignedEmployees" :key="item.id" class="py-2 border-b border-gray-200">
          <span>{{ item.username }}</span>
          <span class="ml-4 text-sm text-gray-500">读: {{ item.read ? '是' : '否' }}</span>
          <span class="ml-4 text-sm text-gray-500">写: {{ item.write ? '是' : '否' }}</span>
        </li>
      </ul>
    </div>
  </div>
</template>
