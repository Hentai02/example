<script setup>
import { ref, watch } from 'vue'
import axios from 'axios'

const props = defineProps({
  visible: Boolean,
  record: Object,
})
const emit = defineEmits(['updated', 'close'])

const localVisible = ref(props.visible) // 是否显示对话框
const localRecord = ref({ ...props.record }) // 选中的薪酬记录

function updateRecord() {
  axios
    .put('/api/payroll', localRecord.value)
    .then((response) => {
      localVisible.value = false
      emit('updated')
      emit('close')
      console.log(response)
    })
    .catch((error) => {
      console.log(error)
    })
}

const visibleDialogOfEmployee = ref(false) // 是否显示员工详情对话框
// 查看员工详情
function viewEmployeeDetail() {
  visibleDialogOfEmployee.value = true
}

watch(
  () => props.visible,
  (newVisible) => {
    localVisible.value = newVisible
    if (newVisible) {
      localRecord.value = { ...props.record }
    }
  },
)
</script>

<template>
  <div
    v-if="localVisible"
    class="fixed inset-0 bg-opacity-50 backdrop-blur-sm flex items-center justify-center"
  >
    <div class="bg-white p-6 rounded-lg shadow-lg max-w-md w-full">
      <h3 class="text-lg font-semibold mb-4">编辑薪酬记录</h3>
      <div class="space-y-4">
        <div @click="viewEmployeeDetail">
          <label class="block text-sm font-medium text-blue-700">员工ID</label>
          <input
            v-model="localRecord.employeeId"
            class="w-full p-3 border border-blue-300 rounded-lg"
            disabled
          />
        </div>
        <div>
          <label class="block text-sm font-medium text-gray-700">基础工资</label>
          <input
            v-model="localRecord.baseSalary"
            type="number"
            class="w-full p-3 border border-gray-300 rounded-lg"
          />
        </div>
        <div>
          <label class="block text-sm font-medium text-gray-700">奖金</label>
          <input
            v-model="localRecord.bonus"
            type="number"
            class="w-full p-3 border border-gray-300 rounded-lg"
          />
        </div>
        <div>
          <label class="block text-sm font-medium text-gray-700">扣除</label>
          <input
            v-model="localRecord.deductions"
            type="number"
            class="w-full p-3 border border-gray-300 rounded-lg"
          />
        </div>
        <div>
          <label class="block text-sm font-medium text-gray-700">总工资</label>
          <input
            v-model="localRecord.totalSalary"
            type="number"
            class="w-full p-3 border border-gray-300 rounded-lg"
            disabled
          />
        </div>
        <div class="flex justify-end text-center">
          <button @click="updateRecord" class="bg-green-500 text-white px-5 py-2 rounded-lg mr-2">
            更新
          </button>
          <button @click="$emit('close')" class="bg-blue-500 text-white px-5 py-2 rounded-lg">
            关闭
          </button>
        </div>
      </div>
    </div>

    <!-- 员工详情对话框 -->
    <DialogOfEmployee
      :visible="visibleDialogOfEmployee"
      :employeeId="localRecord.employeeId"
      @close="visibleDialogOfEmployee = false"
    ></DialogOfEmployee>
  </div>
</template>
