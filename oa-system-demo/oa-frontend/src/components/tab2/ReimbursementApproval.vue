<script setup>
import { ref, onMounted } from 'vue'
import axios from 'axios'
import DialogOfApproveLeave from '../dialog/DialogOfApproval.vue'
import DialogOfReimbursement from '../dialog/DialogOfReimbursement.vue'

const selectedRequestId = ref(0) // 选中的请假请求ID
const visibleDialogOfApproveLeave = ref(false) // 是否显示审批对话框

// 审批
const approvalId = ref(0)
function approveRequest(id) {
  approvalId.value = id
  visibleDialogOfApproveLeave.value = true
  console.log(visibleDialogOfApproveLeave.value)
}

// 审批记录
const approvals = ref([])
function fetchApprovalRecords() {
  axios
    .get('/api/reimbursement/approvals')
    .then(function (response) {
      approvals.value = response.data
    })
    .catch(function (error) {
      console.log(error)
    })
}

const visibleDialogOfRequest = ref(false) // 是否显示报销请求对话框
const isDisabled = ref(false) // 是否禁用
// 查看请假请求
function viewRequest(requestId) {
  selectedRequestId.value = requestId
  visibleDialogOfRequest.value = true
  isDisabled.value = true
}

onMounted(() => {
  fetchApprovalRecords()
})
</script>

<template>
  <div class="bg-white p-6 rounded-lg shadow-lg my-6 min-h-screen">
    <h2 class="text-2xl font-bold mb-6">报销审批</h2>
    <!-- 报销审批列表 -->
    <div v-if="approvals.length === 0" class="text-center text-gray-500">没有待审批的报销申请</div>
    <div v-else>
      <table class="min-w-full bg-white">
        <thead>
          <tr>
            <th
              class="py-2 px-4 border-b border-gray-200 bg-gray-50 text-left text-xs font-semibold text-gray-600 uppercase tracking-wider"
            >
              ID
            </th>
            <th
              class="py-2 px-4 border-b border-gray-200 bg-gray-50 text-left text-xs font-semibold text-gray-600 uppercase tracking-wider"
            >
              请求ID
            </th>
            <th
              class="py-2 px-4 border-b border-gray-200 bg-gray-50 text-left text-xs font-semibold text-gray-600 uppercase tracking-wider"
            >
              审批人
            </th>
            <th
              class="py-2 px-4 border-b border-gray-200 bg-gray-50 text-left text-xs font-semibold text-gray-600 uppercase tracking-wider"
            >
              审批状态
            </th>
            <th
              class="py-2 px-4 border-b border-gray-200 bg-gray-50 text-left text-xs font-semibold text-gray-600 uppercase tracking-wider"
            >
              审批内容
            </th>
            <th
              class="py-2 px-4 border-b border-gray-200 bg-gray-50 text-left text-xs font-semibold text-gray-600 uppercase tracking-wider"
            >
              操作
            </th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="request in approvals" :key="request.id">
            <td class="py-2 px-4 border-b border-gray-200">{{ request.id }}</td>
            <td class="py-2 px-4 border-b border-gray-200">{{ request.requestId }}</td>
            <td class="py-2 px-4 border-b border-gray-200">{{ request.approverId }}</td>
            <td class="py-2 px-4 border-b border-gray-200">{{ request.approvalStatus }}</td>
            <td class="py-2 px-4 border-b border-gray-200">{{ request.approvalComments }}</td>
            <td class="py-2 px-4 border-b border-gray-200">
              <button
                @click="viewRequest(request.requestId)"
                class="bg-blue-500 hover:bg-blue-700 text-white font-bold py-1 px-3 rounded focus:outline-none focus:shadow-outline mr-2"
              >
                查看请求内容
              </button>
              <button
                @click="approveRequest(request.id)"
                class="bg-green-500 hover:bg-green-700 text-white font-bold py-1 px-3 rounded focus:outline-none focus:shadow-outline mr-2"
              >
                审批
              </button>
            </td>
          </tr>
        </tbody>
      </table>
    </div>
    <!-- 报销审批对话框 -->
    <DialogOfApproveLeave
      :type="'reimbursement'"
      :visible="visibleDialogOfApproveLeave"
      :id="approvalId"
      @updated="fetchApprovalRecords"
      @close="visibleDialogOfApproveLeave = false"
    ></DialogOfApproveLeave>

    <!-- 报销申请详情对话框 -->
    <DialogOfReimbursement
      :visible="visibleDialogOfRequest"
      :requestId="selectedRequestId"
      :isDisabled="isDisabled"
      @updated="fetchApprovalRecords"
      @close="visibleDialogOfRequest = false"
    ></DialogOfReimbursement>
  </div>
</template>
