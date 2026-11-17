<template>
  <header class="text-black p-4 bg-gray-100">
    <div class="container mx-auto flex justify-between items-center">
      <div class="text-2xl font-bold">OA-SYS</div>
      <div class="flex items-center">
        <div v-if="isLoggedIn" class="flex items-center space-x-4 relative">
          <div class="relative">
            <button @click="toggleNotifications" class="focus:outline-none relative">
              <font-awesome-icon :icon="['fas', 'bell']" />
              <span
                v-if="!hasNotifications"
                class="absolute top-0 right-0 block h-2 w-2 rounded-full ring-1 ring-white bg-red-500"
              ></span>
            </button>

            <div
              v-if="showNotifications"
              ref="notificationPanel"
              class="absolute right-0 mt-2 w-80 bg-white text-black rounded-md shadow-lg z-50"
            >
              <div class="p-4">
                <div class="flex justify-between items-center">
                  <h3 class="text-lg font-semibold">通知</h3>
                  <button @click="markAllAsRead" class="text-xs text-blue-500 hover:underline">
                    全部已读
                  </button>
                </div>
                <ul class="mt-2 max-h-64 overflow-y-auto">
                  <li
                    v-for="(notification, index) in notifications"
                    :key="index"
                    class="text-sm py-2 border-b border-gray-200 flex justify-between items-center"
                  >
                    <span>{{ notification.subject }}</span>
                    <button
                      v-if="!notification.read"
                      @click="markAsRead(notification)"
                      class="text-xs text-blue-500 hover:underline whitespace-nowrap"
                    >
                      已读
                    </button>
                  </li>
                </ul>
              </div>
            </div>
          </div>

          <button class="focus:outline-none">
            <font-awesome-icon :icon="['fas', 'magnifying-glass']" />
          </button>
          <div class="relative">
            <button @click="toggleMenu" class="flex items-center focus:outline-none">
              <div
                class="w-8 h-8 rounded-full bg-blue-500 text-white flex items-center justify-center mr-2"
              >
                <span>{{ user.username.charAt(0).toUpperCase() }}</span>
              </div>
              <span>{{ user.username }}</span>
            </button>
            <div
              v-if="menuOpen"
              class="absolute right-0 mt-2 w-48 bg-white text-black rounded-md shadow-lg z-10"
            >
              <a @click="visibleDialog = true" class="block px-4 py-2 hover:bg-gray-200">
                更改密码
              </a>
              <a href="#" @click="logout" class="block px-4 py-2 hover:bg-gray-200">注销</a>
            </div>
          </div>
        </div>
      </div>

      <DialogOfChangePassword
        :visible="visibleDialog"
        @close="visibleDialog = false"
      ></DialogOfChangePassword>
    </div>
  </header>
</template>

<script setup>
import { ref, onMounted, onUnmounted, watch } from 'vue'
import { useUserStore } from '../stores/user'
import { useRouter } from 'vue-router'
import axios from 'axios'
import DialogOfChangePassword from '../components/dialog/DialogOfChangePassword.vue'
// import axios from 'axios'

const isLoggedIn = ref(false) // 是否登录
const menuOpen = ref(false) // 是否打开菜单
const hasNotifications = ref(false) // 是否有通知
const visibleDialog = ref(false) // 是否显示对话框
const userStore = useUserStore() // 用户信息
const router = useRouter()
const user = ref({
  username: 'admin',
})

// 切换菜单
function toggleMenu() {
  menuOpen.value = !menuOpen.value
}

const notifications = ref([])
function fetchNotifications() {
  const user = localStorage.getItem('user')
  if (!user) return
  axios
    .get(`/api/message`)
    .then((response) => {
      notifications.value = response.data.content
      console.log(response.data.content)
      for (let i = 0; i < notifications.value.length; i++) {
        if (notifications.value[i].read) {
          hasNotifications.value = true
        }
      }
      // console.log(`${response.data} 1111`)
    })
    .catch((error) => {
      console.error(error)
    })
}

// 已读
function markAsRead(notification) {
  console.log(notification)
  notification.read = true
  axios
    .put('/api/message', notification)
    .then(() => {})
    .catch((error) => {
      console.log(error)
    })
}

// 全部已读
function markAllAsRead() {
  for (let i = 0; i < notifications.value.length; i++) {
    hasNotifications.value = true
    markAsRead(notifications.value[i])
  }
}

// 显示通知
const showNotifications = ref(false) // 控制通知面板的显示和隐藏
function toggleNotifications() {
  showNotifications.value = !showNotifications.value
}

// 登出
function logout() {
  // axios.post('/api/user/logout')
  isLoggedIn.value = false
  menuOpen.value = false
  localStorage.removeItem('user')
  router.push('/')
}
// 点击外部关闭菜单
function handleClickOutside(event) {
  const menu = document.querySelector('.relative')
  if (menu && !menu.contains(event.target)) {
    menuOpen.value = false
    showNotifications.value = false
  }
}

// 添加事件监听
onMounted(() => {
  document.addEventListener('click', handleClickOutside)

  const storedUser = localStorage.getItem('user')
  // 检查是否有用户信息
  if (storedUser) {
    const parsedUser = JSON.parse(storedUser)
    user.value.username = parsedUser.username
    isLoggedIn.value = true
  }

  // 模拟检测到有通知
  setTimeout(() => {
    fetchNotifications()
    console.log('fetchNotifications')
  }, 1000)

  fetchNotifications()
})

// 移除事件监听
onUnmounted(() => {
  document.removeEventListener('click', handleClickOutside)
})

watch(
  () => userStore.user,
  (newUser) => {
    if (newUser) {
      user.value.username = newUser.username
      isLoggedIn.value = true
    }
  },
)
</script>
