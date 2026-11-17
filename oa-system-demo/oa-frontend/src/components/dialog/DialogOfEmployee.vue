<script setup>
import { defineProps, ref, watch } from 'vue'
import axios from 'axios'

const props = defineProps({
  visible: Boolean,
  employeeId: Number, // 员工ID
})
const localVisible = ref(props.visible) // 是否显示对话框
const selectedEmployee = ref({}) // 选中的员工

// 获取员工详情
function fetchEmployeeDetail(id) {
  if (id === undefined) {
    return
  }
  axios
    .get(`/api/employee/${id}`)
    .then(function (response) {
      selectedEmployee.value = response.data
    })
    .catch(function (error) {
      console.log(error)
    })
}

// 监听员工ID变化
watch(() => {
  fetchEmployeeDetail(props.employeeId)
})

watch(
  () => props.employeeId,
  (id) => {
    if (id) {
      fetchEmployeeDetail(id)
    }
  },
)

watch(
  () => props.visible,
  (newVisible) => {
    localVisible.value = newVisible
  },
)
</script>
<template>
  <!-- 员工详细对话框 -->
  <div
    v-if="localVisible"
    class="fixed inset-0 bg-opacity-50 backdrop-blur-sm flex items-center justify-center"
  >
    <div class="bg-white p-6 rounded-lg shadow-lg max-w-md w-full">
      <h3 class="text-lg font-semibold mb-4">员工详情</h3>
      <div class="space-y-4">
        <div>
          <label class="block text-sm font-medium text-gray-700">工号</label>
          <p class="w-full p-3 border border-gray-300 rounded-lg">
            {{ selectedEmployee.id }}
          </p>
        </div>
        <div>
          <label class="block text-sm font-medium text-gray-700">姓名</label>
          <p class="w-full p-3 border border-gray-300 rounded-lg">
            {{ selectedEmployee.username }}
          </p>
        </div>
        <div>
          <label class="block text-sm font-medium text-gray-700">职位</label>
          <p class="w-full p-3 border border-gray-300 rounded-lg">
            {{ selectedEmployee.positionId }}
          </p>
        </div>
        <div>
          <label class="block text-sm font-medium text-gray-700">性别</label>
          <p class="w-full p-3 border border-gray-300 rounded-lg">
            {{ selectedEmployee.gender }}
          </p>
        </div>
        <div>
          <label class="block text-sm font-medium text-gray-700">出生日期</label>
          <p class="w-full p-3 border border-gray-300 rounded-lg">
            {{ selectedEmployee.birthDate }}
          </p>
        </div>
        <div>
          <label class="block text-sm font-medium text-gray-700">入职日期</label>
          <p class="w-full p-3 border border-gray-300 rounded-lg">
            {{ selectedEmployee.hireDate }}
          </p>
        </div>
        <div>
          <label class="block text-sm font-medium text-gray-700">手机号</label>
          <p class="w-full p-3 border border-gray-300 rounded-lg">
            {{ selectedEmployee.phone }}
          </p>
        </div>
        <div>
          <label class="block text-sm font-medium text-gray-700">状态</label>
          <p class="w-full p-3 border border-gray-300 rounded-lg">
            {{ selectedEmployee.email }}
          </p>
        </div>
        <div class="flex justify-end text-center">
          <button @click="$emit('close')" class="bg-blue-500 text-white px-5 py-2 rounded-lg">
            关闭
          </button>
        </div>
      </div>
    </div>
  </div>
</template>
