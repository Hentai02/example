<script setup>
// filepath: /d:/Code/VSCodeProjects/oa-system/src/components/tab7/SystemSettings.vue
import { ref, defineProps, watch } from 'vue'
import axios from 'axios'
import { useRouter } from 'vue-router'
const router = useRouter()

const props = defineProps({
  visible: Boolean,
})

const localVisible = ref(props.visible) // 是否显示对话框

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

// 监听对话框显示状态
watch(
  () => props.visible,
  (newVisible) => {
    localVisible.value = newVisible
  },
)
</script>

<template>
  <!-- 审批对话框 -->
  <div
    v-if="localVisible"
    class="fixed inset-0 bg-opacity-50 backdrop-blur-sm flex items-center justify-center"
  >
    <div class="bg-white p-6 rounded-lg shadow-lg max-w-md w-full">
      <h3 class="text-lg font-semibold mb-4">修改密码</h3>
      <div class="space-y-4">
        <!-- <div class="mb-4">
          <label for="username" class="block text-sm font-medium text-gray-700">用户名</label>
          <input
            type="text"
            id="username"
            v-model="username"
            class="w-full p-3 border border-gray-300 rounded-lg"
          />
        </div> -->

        <div class="mb-4">
          <label for="password" class="block text-sm font-medium text-gray-700">密码</label>
          <input
            type="text"
            id="password"
            v-model="password"
            class="w-full p-3 border border-gray-300 rounded-lg"
          />
        </div>

        <div class="flex justify-end text-center">
          <button @click="updateEmployee" class="bg-green-500 text-white px-5 py-2 rounded-lg mr-2">
            确认
          </button>
          <button @click="$emit('close')" class="bg-blue-500 text-white px-5 py-2 rounded-lg">
            关闭
          </button>
        </div>
      </div>
    </div>
  </div>
</template>
