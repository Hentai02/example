<script setup>
import { reactive, ref } from 'vue'
import axios from 'axios'
import { useRouter } from 'vue-router'
import { useUserStore } from '../stores/user'

const form = reactive({
  username: 'admin123',
  password: 'admin',
})

const loginError = ref(false)
const router = useRouter()
const userStore = useUserStore()

function login() {
  axios
    .post('/api/user/login', {
      username: form.username,
      password: form.password,
    })
    .then(function (response) {
      if (response.status === 200) {
        loginError.value = false
        // 保存登录用户信息
        localStorage.setItem('user', JSON.stringify(form))
        localStorage.setItem('user-req', JSON.stringify(response.data))
        userStore.setUser(response.data)
        console.log('登录成功:', response.data)
        router.push('/dashboard/workbench')
      }
    })
    .catch(function (error) {
      loginError.value = true
      console.log(error)
    })
}
</script>

<template>
  <main>
    <div class="flex justify-center items-center pt-20">
      <p class="text-2xl">登录 OA-SYS</p>
    </div>
    <div class="flex justify-center items-center">
      <div class="bg-white p-8 rounded-lg shadow-lg w-full max-w-md">
        <form @submit.prevent="login">
          <div class="mb-4">
            <label for="username" class="block text-gray-700 text-sm font-bold mb-2">用户名:</label>
            <input
              type="text"
              id="username"
              v-model="form.username"
              class="shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline"
              placeholder="请输入用户名"
              required
            />
          </div>
          <div class="mb-6">
            <label for="password" class="block text-gray-700 text-sm font-bold mb-2">密码:</label>
            <input
              type="password"
              id="password"
              v-model="form.password"
              class="shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 mb-3 leading-tight focus:outline-none focus:shadow-outline"
              placeholder="请输入密码"
              required
            />
            <p v-if="loginError" class="text-[12px] text-red-800">
              用户名或密码错误。请检查用户名和密码。
            </p>
          </div>

          <div class="mb-6">
            <a class="underline hover:text-blue-500 text-blue-600" href="#">忘记密码？</a>
          </div>

          <div class="flex items-center justify-between">
            <button
              type="submit"
              class="w-full bg-blue-500 hover:bg-blue-700 text-white font-bold py-2 px-4 rounded focus:outline-none focus:shadow-outline"
            >
              登录
            </button>
          </div>

          <div class="flex justify-center pt-4">
            没有账号？<a class="underline hover:text-blue-500 text-blue-600" href="#">入职</a>
          </div>
        </form>
      </div>
    </div>
  </main>
</template>
