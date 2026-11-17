<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import axios from 'axios'

const router = useRouter()

const request = ref({})

async function submit() {
  console.log(request)
  await uploadFile()
  axios
    .post('/api/reimbursement', request.value)
    .then(() => {
      // console.log(`${request.value.attachmentUrl},llll`)
      alert('提交成功！')
      close()
    })
    .catch((error) => {
      console.log(error)
      close()
    })
}

const selectedFile = ref(null) // 用于存储用户选择的文件
async function uploadFile() {
  const employee = localStorage.getItem('employee')
  // console.log(`${employee.id},dddddd`)

  const formData = new FormData()
  formData.append('file', selectedFile.value)
  formData.append('path', 'rembursement')
  formData.append('uploadEmployeeId', employee.id)
  // console.log(fileInput.value.files[0])

  await axios
    .post('/api/file-upload', formData, {
      headers: {
        'Content-Type': 'multipart/form-data',
      },
    })
    .then(() => {
      request.value.attachmentUrl = selectedFile.value.name
      // showUploadDialog.value = false
      // fetchFileItems(currentPath.value)
    })
    .catch((error) => {
      alert('权限不足，请联系管理员')

      console.error(error)
    })
}

// 处理文件选择
function handleFileChange(event) {
  selectedFile.value = event.target.files[0]
}

const close = () => {
  // 关闭任务添加页面，导航回任务列表或其他页面
  router.push('/dashboard/workbench')
}
</script>
<template>
  <!-- 报销申请详情对话框 -->
  <div class="p-6 min-h-screen">
    <h1 class="text-3xl font-bold mb-6">报销申请</h1>
    <div class="space-y-4">
      <div>
        <label class="block text-sm font-medium text-gray-700">报销类型</label>
        <!-- <input
          v-model="request.reimbursementType"
          class="w-full p-3 border border-gray-300 rounded-lg"
        /> -->
        <select
          v-model="request.reimbursementType"
          class="w-full p-3 border border-gray-300 rounded-lg"
        >
          <option value="差旅费">差旅费</option>
          <option value="通讯费">通讯费</option>
          <option value="培训费">培训费</option>
          <option value="业务招待费">业务招待费</option>
        </select>
      </div>
      <div>
        <label class="block text-sm font-medium text-gray-700">金额</label>
        <input v-model="request.totalAmount" class="w-full p-3 border border-gray-300 rounded-lg" />
      </div>
      <div>
        <label class="block text-sm font-medium text-gray-700">原因</label>
        <input
          v-model="request.reimbursementReason"
          class="w-full p-3 border border-gray-300 rounded-lg"
        />
      </div>
      <div>
        <!-- <label class="block text-sm font-medium text-gray-700">附件</label>
        <input
          v-model="request.attachmentUrl"
          class="w-full p-3 border border-gray-300 rounded-lg"
        /> -->

        <label class="block text-sm font-medium text-gray-700">附件</label>
        <input
          type="file"
          @change="handleFileChange"
          accept="image/*"
          class="w-full p-3 border border-gray-300 rounded-lg"
        />
        <!-- <p v-if="request.attachmentUrl" class="text-sm text-gray-500 mt-2">
          已上传文件：<a
            :href="request.attachmentUrl"
            target="_blank"
            class="text-blue-500 underline"
            >查看附件</a
          >
        </p> -->
      </div>
      <div class="flex justify-end text-center">
        <button @click="submit()" class="bg-green-500 text-white px-5 py-2 rounded-lg ml-2">
          提交
        </button>
        <button @click="close()" class="bg-blue-500 text-white px-5 py-2 rounded-lg ml-2">
          关闭
        </button>
      </div>
    </div>
  </div>
</template>
