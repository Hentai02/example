<template>
  <div class="grid grid-cols-1 lg:grid-cols-2 gap-4">
    <!-- 工资详情 -->
    <SalaryCardView></SalaryCardView>

    <!-- 员工信息 -->
    <EmployeeProfileCardView></EmployeeProfileCardView>

    <!-- 任务 -->
    <TaskCardView></TaskCardView>

    <!-- 日历任务 -->
    <CalendarTasksCardView></CalendarTasksCardView>

    <div class="p-6 bg-white shadow rounded-lg">
      <h2 class="text-2xl font-bold mb-4">申请操作</h2>
      <div class="flex flex-col space-y-4">
        <button
          @click="toLeaveRequest"
          class="flex items-center bg-blue-500 hover:bg-blue-700 text-white font-bold py-2 px-4 rounded"
        >
          <font-awesome-icon :icon="['fas', 'plane-departure']" class="mr-2" />
          请假申请
        </button>
        <button
          @click="toRe"
          class="flex items-center bg-green-500 hover:bg-green-700 text-white font-bold py-2 px-4 rounded"
        >
          <font-awesome-icon :icon="['fas', 'file-invoice-dollar']" class="mr-2" />
          报销申请
        </button>
        <button
          @click="too"
          class="flex items-center bg-yellow-500 hover:bg-yellow-700 text-white font-bold py-2 px-4 rounded"
        >
          <font-awesome-icon :icon="['fas', 'clock']" class="mr-2" />
          加班申请
        </button>
      </div>
    </div>

    <div class="p-6 bg-white shadow rounded-lg">
      <div class="mb-4 border-b border-gray-200">
        <nav class="flex space-x-4" aria-label="Tabs">
          <a
            href="#"
            @click.prevent="activeTab = 'overtime'"
            :class="{
              'border-indigo-500 text-indigo-600': activeTab === 'overtime',
              'border-transparent text-gray-500 hover:text-gray-700 hover:border-gray-300':
                activeTab !== 'overtime',
            }"
            class="whitespace-nowrap py-4 px-1 border-b-2 font-medium text-sm"
          >
            加班
          </a>
          <a
            href="#"
            @click.prevent="activeTab = 'leave'"
            :class="{
              'border-indigo-500 text-indigo-600': activeTab === 'leave',
              'border-transparent text-gray-500 hover:text-gray-700 hover:border-gray-300':
                activeTab !== 'leave',
            }"
            class="whitespace-nowrap py-4 px-1 border-b-2 font-medium text-sm"
          >
            请假
          </a>
          <a
            href="#"
            @click.prevent="activeTab = 'reimbursement'"
            :class="{
              'border-indigo-500 text-indigo-600': activeTab === 'reimbursement',
              'border-transparent text-gray-500 hover:text-gray-700 hover:border-gray-300':
                activeTab !== 'reimbursement',
            }"
            class="whitespace-nowrap py-4 px-1 border-b-2 font-medium text-sm"
          >
            报销
          </a>
        </nav>
      </div>

      <div v-if="activeTab === 'overtime'">
        <!-- 加班报销记录内容 -->
        <OvertimeRecords />
      </div>

      <div v-if="activeTab === 'leave'">
        <!-- 请假申请记录内容 -->
        <!-- 11112 -->
        <LeaveRecords />
      </div>
      <div v-if="activeTab === 'reimbursement'">
        <!-- 报销申请记录内容 -->
        <ReimbursementRecords />
      </div>
    </div>
  </div>
</template>

<script setup>
import SalaryCardView from './card/SalaryCardView.vue'
import EmployeeProfileCardView from './card/EmployeeProfileCardView.vue'
import TaskCardView from './card/TaskCardView.vue'
import CalendarTasksCardView from './card/CalendarTasksCardView.vue'

import OvertimeRecords from './OvertimeRecords.vue'
import ReimbursementRecords from './ReimbursementRecords.vue'
import LeaveRecords from './LeaveRecords.vue'

import { useRouter } from 'vue-router'
import { ref } from 'vue'
const router = useRouter()
const activeTab = ref('overtime')

function toLeaveRequest() {
  router.push('/dashboard/tab2/leave?leave={}')
}

function toRe() {
  router.push('/dashboard/tab2/reimbursementRequest')
}

function too() {
  router.push('/dashboard/tab2/overtimeRequest')
}
</script>
