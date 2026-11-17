<script setup>
import axios from 'axios'
import { ref, watch } from 'vue'
import DialogOfEmployee from '../dialog/DialogOfEmployee.vue'

const props = defineProps({
  visible: Boolean,
  record: Object,
  isEditMode: Boolean,
})

const emit = defineEmits(['updated', 'close'])

const localVisible = ref(props.visible)
const localRecord = ref({ ...props.record })
const isEditMode = ref(props.isEditMode)

function submit() {
  if (localRecord.value.id) {
    // 更新考勤记录
    axios
      .put('/api/attendance', localRecord.value)
      .then((response) => {
        localVisible.value = false
        emit('updated')
        emit('close')
        console.log(response)
      })
      .catch((error) => {
        console.log(error)
      })
  } else {
    // 创建考勤记录
    axios
      .post('/api/attendance', localRecord.value)
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
}

const visibleDialogOfEmployee = ref(false) // 是否显示员工详情对话框
function viewEmployeeDetail() {
  if (localRecord.value.id) {
    visibleDialogOfEmployee.value = true
  }
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

watch(
  () => props.isEditMode,
  (newIsEditMode) => {
    isEditMode.value = newIsEditMode
    if (isEditMode.value) {
      localRecord.value = {}
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
      <h3 class="text-lg font-semibold mb-4">编辑考勤记录</h3>
      <div class="space-y-4">
        <div @click="viewEmployeeDetail()">
          <label class="block text-sm font-medium text-gray-700">工号</label>
          <input
            v-model="localRecord.employeeId"
            class="w-full p-3 border border-blue-300 text-blue-500 underline cursor-pointer rounded-lg"
          />
        </div>
        <div>
          <label class="block text-sm font-medium text-gray-700">签到时间</label>
          <input
            v-model="localRecord.checkInTime"
            type="datetime-local"
            class="w-full p-3 border border-gray-300 rounded-lg"
          />
        </div>
        <div>
          <label class="block text-sm font-medium text-gray-700">签退时间</label>
          <input
            v-model="localRecord.checkOutTime"
            type="datetime-local"
            class="w-full p-3 border border-gray-300 rounded-lg"
          />
        </div>
        <div>
          <label class="block text-sm font-medium text-gray-700">状态</label>
          <select v-model="localRecord.status" class="w-full p-3 border border-gray-300 rounded-lg">
            <option value="出勤">出勤</option>
            <option value="迟到">迟到</option>
            <option value="缺勤">缺勤</option>
          </select>
        </div>
        <div class="flex justify-end text-center">
          <button @click="submit" class="bg-green-500 text-white px-5 py-2 rounded-lg mr-2">
            提交
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
