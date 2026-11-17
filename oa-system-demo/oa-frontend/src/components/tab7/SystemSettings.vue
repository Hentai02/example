<template>
  <div class="p-6 bg-white rounded-lg shadow-lg">
    <h2 class="text-2xl font-semibold mb-4">更改员工信息</h2>
    <div class="mb-4">
      <label for="username" class="block text-sm font-medium text-gray-700">用户名</label>
      <input
        type="text"
        id="username"
        v-model="username"
        class="w-full p-3 border border-gray-300 rounded-lg"
      />
    </div>

    <div class="mb-4">
      <label for="password" class="block text-sm font-medium text-gray-700">密码</label>
      <input
        type="text"
        id="password"
        v-model="password"
        class="w-full p-3 border border-gray-300 rounded-lg"
      />
    </div>

    <button
      @click="updateEmployee"
      class="mt-2 inline-flex items-center px-4 py-2 border border-transparent text-sm font-medium rounded-md text-white bg-indigo-600 hover:bg-indigo-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-indigo-500"
    >
      更新员工信息
    </button>
  </div>
</template>

<script setup>
// filepath: /d:/Code/VSCodeProjects/oa-system/src/components/tab7/SystemSettings.vue
import { ref } from 'vue'
import axios from 'axios'
import { useRouter } from 'vue-router'
const router = useRouter()

// 定义表单数据
const password = ref('')

// 更新员工信息
function updateEmployee() {
  axios
    .put(`/api/user`, {
      password: password.value,
    })
    .then(() => {
      alert('员工信息更新成功')
      router.push('/dashboard/workbench')
    })
    .catch((error) => {
      console.error(error)
    })
}
</script>
