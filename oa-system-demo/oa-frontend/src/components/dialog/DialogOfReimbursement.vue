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

// 获取报销请求详情
function fetchRequestDetail() {
  axios
    .get(`/api/reimbursement/${props.requestId}`)
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

const showImageModal = ref(false) // 控制大图模态框的显示
const imageUrl = ref('') // 存储当前点击的图片 URL
// 查看大图
function viewImage(url) {
  imageUrl.value = url
  showImageModal.value = true
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
  <!-- 报销申请详情对话框 -->
  <div
    v-if="localVisible"
    class="fixed inset-0 bg-opacity-50 backdrop-blur-sm flex items-center justify-center"
  >
    <div class="bg-white p-6 rounded-lg shadow-lg max-w-md w-full">
      <h3 class="text-lg font-semibold mb-4">报销申请详情</h3>
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
          <label class="block text-sm font-medium text-gray-700">报销类型</label>
          <input
            :disabled="isDisabled"
            v-model="selectedRequest.reimbursementType"
            class="w-full p-3 border border-gray-300 rounded-lg"
          />
        </div>
        <div>
          <label class="block text-sm font-medium text-gray-700">金额</label>
          <input
            :disabled="isDisabled"
            v-model="selectedRequest.totalAmount"
            class="w-full p-3 border border-gray-300 rounded-lg"
          />
        </div>
        <div>
          <label class="block text-sm font-medium text-gray-700">原因</label>
          <input
            :disabled="isDisabled"
            v-model="selectedRequest.reimbursementReason"
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
        <div v-if="selectedRequest.attachmentUrl">
          <label class="block text-sm font-medium text-gray-700">附件</label>
          <img
            :src="`/api/file-upload/files/${selectedRequest.attachmentUrl}?path=rembursement`"
            alt="附件图片"
            class="w-32 h-32 object-cover border border-gray-300 rounded-lg cursor-pointer"
            @click="
              viewImage(`/api/file-upload/files/${selectedRequest.attachmentUrl}?path=rembursement`)
            "
          />
        </div>
        <div v-else>
          <p class="text-gray-500">暂无附件</p>
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

  <!-- 图片大图模态框 -->
  <div
    v-if="showImageModal"
    class="fixed inset-0 bg-black bg-opacity-75 flex items-center justify-center z-50"
  >
    <!-- 模态框容器 -->
    <div class="relative w-full h-full flex items-center justify-center">
      <!-- 大图 -->
      <img :src="imageUrl" alt="大图" class="max-w-full max-h-screen rounded-lg" />
    </div>

    <!-- 关闭按钮 -->
    <button
      @click="showImageModal = false"
      class="absolute top-4 right-4 bg-orange-600 text-white px-4 py-2 rounded-lg shadow-lg"
    >
      关闭
    </button>
  </div>
</template>
