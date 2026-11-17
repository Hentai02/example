<script setup>
import { defineProps, defineEmits, ref, watch } from 'vue'
import axios from 'axios'
import DialogOfEmployee from './DialogOfEmployee.vue'

const props = defineProps({
  visible: Boolean,
  requestId: Number, // 请求ID
  isDisabled: Boolean,
})

const emit = defineEmits(['updated', 'close'])

const localVisible = ref(props.visible) // 是否显示对话框
const localIsDisabled = ref(props.isDisabled) // 是否禁用
const selectedRequest = ref({}) // 选中的请求

// 获取加班请求详情
function fetchRequestDetail() {
  axios
    .get(`/api/overtime/${props.requestId}`)
    .then(function (response) {
      selectedRequest.value = response.data
    })
    .catch(function (error) {
      console.log(error)
    })
}

const visibleDialogOfEmployee = ref(false) // 是否显示员工详情对话框
// 查看员工详情
function viewEmployeeDetail() {
  visibleDialogOfEmployee.value = true
}

// 更新报销请求
function update(request) {
  axios
    .put('/api/leave/update', request)
    .then(() => {
      localVisible.value = false
      emit('updated')
      emit('close')
    })
    .catch((error) => {
      console.log(error)
    })
}

watch(
  () => props.requestId,
  (id) => {
    if (id) {
      fetchRequestDetail(id)
    }
  },
)

watch(
  () => props.visible,
  (newVisiable) => {
    localVisible.value = newVisiable
  },
)

watch(
  () => props.isDisabled,
  (newIsDisabled) => {
    localIsDisabled.value = newIsDisabled
  },
)
</script>
<template>
  <!-- 加班申请详情对话框 -->
  <div
    v-if="localVisible"
    class="fixed inset-0 bg-opacity-50 backdrop-blur-sm flex items-center justify-center"
  >
    <div class="bg-white p-6 rounded-lg shadow-lg max-w-md w-full">
      <h3 class="text-lg font-semibold mb-4">加班申请详情</h3>
      <div class="space-y-4">
        <div @click="viewEmployeeDetail">
          <label class="block text-sm font-medium text-gray-700">工号</label>
          <input
            :disabled="isDisabled"
            v-model="selectedRequest.employeeId"
            class="w-full p-3 border border-blue-300 rounded-lg text-blue-500 cursor-pointer underline"
          />
        </div>
        <div>
          <label class="block text-sm font-medium text-gray-700">加班日期</label>
          <input
            :disabled="isDisabled"
            v-model="selectedRequest.overtimeDate"
            class="w-full p-3 border border-gray-300 rounded-lg"
          />
        </div>
        <div>
          <label class="block text-sm font-medium text-gray-700">开始时间</label>
          <input
            :disabled="isDisabled"
            v-model="selectedRequest.startTime"
            class="w-full p-3 border border-gray-300 rounded-lg"
          />
        </div>
        <div>
          <label class="block text-sm font-medium text-gray-700">结束时间</label>
          <input
            :disabled="isDisabled"
            v-model="selectedRequest.endTime"
            class="w-full p-3 border border-gray-300 rounded-lg"
          />
        </div>
        <div>
          <label class="block text-sm font-medium text-gray-700">原因</label>
          <input
            :disabled="isDisabled"
            v-model="selectedRequest.reason"
            class="w-full p-3 border border-gray-300 rounded-lg"
          />
        </div>
        <div>
          <label class="block text-sm font-medium text-gray-700">状态</label>
          <input
            :disabled="isDisabled"
            v-model="selectedRequest.status"
            class="w-full p-3 border border-gray-300 rounded-lg"
          />
        </div>
        <div class="flex justify-end text-center">
          <button
            @click="update(selectedRequest)"
            :hidden="isDisabled"
            class="bg-green-500 text-white px-5 py-2 rounded-lg ml-2"
          >
            更新
          </button>
          <button @click="$emit('close')" class="bg-blue-500 text-white px-5 py-2 rounded-lg ml-2">
            关闭
          </button>
        </div>
      </div>
    </div>

    <!-- 员工详情对话框 -->
    <DialogOfEmployee
      :visible="visibleDialogOfEmployee"
      :employeeId="selectedRequest.employeeId"
      @close="visibleDialogOfEmployee = false"
    ></DialogOfEmployee>
  </div>
</template>
