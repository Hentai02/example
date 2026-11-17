<script setup>
import { defineProps, ref, watch } from 'vue'
import axios from 'axios'

const props = defineProps({
  visible: Boolean,
  id: Number, // 请假请求ID
  type: String, // 请假类型
})

const emit = defineEmits(['updated', 'close'])
const localVisible = ref(props.visible) // 是否显示对话框
const localType = ref(props.type) // 请假类型
const approvalComments = ref('') // 审批回执
const approvalStatus = ref('拒绝') // 审批状态

// 审批
function confirmApproval() {
  let url = ''
  if (localType.value === 'leave') {
    url = '/api/leave/approve'
  } else if (localType.value === 'reimbursement') {
    url = '/api/reimbursement/approve'
  } else if (localType.value === 'overtime') {
    url = '/api/overtime/approve'
  } else {
    url = ''
    console.log('未知类型')
  }

  axios
    .post(url, null, {
      params: {
        id: props.id,
        approvalComments: approvalComments.value,
        approvalStatus: approvalStatus.value,
      },
    })
    .then(function (response) {
      localVisible.value = false
      emit('updated')
      emit('close')
      console.log(response)
    })
    .catch(function (error) {
      console.log(error)
    })
}

// 监听对话框显示状态
watch(
  () => props.visible,
  (newVisible) => {
    localVisible.value = newVisible
  },
)

// 监听请假类型变化
watch(
  () => props.type,
  (newType) => {
    localType.value = newType
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
      <h3 class="text-lg font-semibold mb-4">选择审批状态</h3>
      <div class="space-y-4">
        <div>
          <label class="block text-sm font-medium text-gray-700">审批状态</label>
          <select v-model="approvalStatus" class="w-full p-3 border border-gray-300 rounded-lg">
            <option value="批准">批准</option>
            <option value="拒绝">拒绝</option>
          </select>
        </div>
        <div v-if="approvalStatus === '拒绝'">
          <label class="block text-sm font-medium text-gray-700">回执</label>
          <input
            v-model="approvalComments"
            class="w-full p-3 border border-blue-300 rounded-lg text-blue-500 cursor-pointer underline"
          />
        </div>
        <div class="flex justify-end text-center">
          <button
            @click="confirmApproval"
            class="bg-green-500 text-white px-5 py-2 rounded-lg mr-2"
          >
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
