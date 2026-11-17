<script setup>
import axios from 'axios'
import { ref, onMounted } from 'vue'

import { useRouter } from 'vue-router'

const router = useRouter()

const attendanceRecords = ref([]) // 考勤记录
const page = ref(0)
const size = ref(5)
const sort = ref('id,asc')
const totalPages = ref(0)
const searchKeyword = ref('')

// 获取考勤记录
// function fetchAttendanceRecords() {
//   axios
//     .get('/api/attendance/all')
//     .then((res) => {
//       attendanceRecords.value = res.data
//     })
//     .catch((err) => {
//       console.error(err)
//     })
// }
async function fetchAttendanceRecords() {
  try {
    const response = await axios.get(`/api/attendance/all`, {
      params: {
        page: page.value,
        size: size.value,
        sort: sort.value,
        keyword: searchKeyword.value,
      },
    })
    attendanceRecords.value = response.data.content
    totalPages.value = response.data.totalPages
    if (searchKeyword.value) {
      page.value = 0
    }
  } catch (error) {
    console.error(error)
  }
}

// 上一页
function prevPage() {
  if (page.value > 0) {
    page.value--
    fetchAttendanceRecords()
  }
}

// 下一页
function nextPage() {
  page.value++
  fetchAttendanceRecords()
}

// 编辑考勤记录
function editAttendance(attendance) {
  router.push({
    name: 'AttendanceView',
    query: { attendance: JSON.stringify(attendance) },
  })
}

// 删除考勤记录
function deleteAttendance(id) {
  const confirmed = confirm('确定要删除这条考勤记录吗？')
  if (confirmed) {
    axios
      .delete(`/api/attendance/${id}`)
      .then(() => {
        fetchAttendanceRecords()
      })
      .catch((err) => {
        console.error(err)
      })
  }
}

const employees = ref([])
async function fetchEmployeeProfiles() {
  try {
    const response = await axios.get(`/api/employee/all`)
    employees.value = response.data.content
  } catch (error) {
    console.error(error)
  }
}

onMounted(() => {
  fetchAttendanceRecords()
  fetchEmployeeProfiles()
})
</script>

<template>
  <!-- 考勤管理 -->
  <div class="bg-white p-6 rounded-lg shadow-lg min-h-screen">
    <h2 class="text-2xl font-bold mb-6">考勤管理</h2>
    <div class="flex justify-end mb-4">
      <RouterLink
        to="/dashboard/tab2/attendance?attendance={}"
        class="bg-green-500 text-white px-4 py-2 rounded-lg"
      >
        新建考勤记录
      </RouterLink>
    </div>
    <!-- <input
      type="text"
      v-model="searchKeyword"
      @input="fetchAttendanceRecords"
      placeholder="搜索"
      class="w-full p-3 border border-gray-300 rounded-lg mb-4"
    /> -->

    <select
      v-model="searchKeyword"
      @change="fetchAttendanceRecords"
      class="w-full p-3 border border-gray-300 rounded-lg mb-4"
    >
      <option value="">所有员工</option>
      <option v-for="employee in employees" :key="employee.id" :value="employee.id">
        {{ employee.username }}
      </option>
    </select>
    <div v-if="attendanceRecords.length === 0" class="text-center text-gray-500">没有考勤记录</div>
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
              工号
            </th>
            <th
              class="py-2 px-4 border-b border-gray-200 bg-gray-50 text-left text-xs font-semibold text-gray-600 uppercase tracking-wider"
            >
              签到时间
            </th>
            <th
              class="py-2 px-4 border-b border-gray-200 bg-gray-50 text-left text-xs font-semibold text-gray-600 uppercase tracking-wider"
            >
              签退时间
            </th>
            <th
              class="py-2 px-4 border-b border-gray-200 bg-gray-50 text-left text-xs font-semibold text-gray-600 uppercase tracking-wider"
            >
              状态
            </th>
            <th
              class="py-2 px-4 border-b border-gray-200 bg-gray-50 text-left text-xs font-semibold text-gray-600 uppercase tracking-wider"
            >
              操作
            </th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="record in attendanceRecords" :key="record.id">
            <td class="py-2 px-4 border-b border-gray-200">{{ record.id }}</td>
            <td class="py-2 px-4 border-b border-gray-200">{{ record.employeeId }}</td>
            <td class="py-2 px-4 border-b border-gray-200">{{ record.checkInTime }}</td>
            <td class="py-2 px-4 border-b border-gray-200">{{ record.checkOutTime }}</td>
            <td class="py-2 px-4 border-b border-gray-200">{{ record.status }}</td>
            <td class="py-2 px-4 border-b border-gray-200">
              <button
                @click="editAttendance(record)"
                class="bg-blue-500 hover:bg-blue-700 text-white font-bold py-1 px-3 rounded focus:outline-none focus:shadow-outline mr-2"
              >
                编辑
              </button>
              <button
                @click="deleteAttendance(record.id)"
                class="bg-red-500 hover:bg-red-700 text-white font-bold py-1 px-3 rounded focus:outline-none focus:shadow-outline"
              >
                删除
              </button>
            </td>
          </tr>
        </tbody>
      </table>
    </div>
    <div class="mt-4">
      <button
        @click="prevPage"
        :disabled="page === 0"
        class="px-4 py-2 bg-blue-500 text-white rounded-lg"
      >
        上一页
      </button>
      <span class="m-4">第 {{ page + 1 }} 页，共 {{ totalPages }} 页</span>
      <button
        @click="nextPage"
        :disabled="page >= totalPages - 1"
        class="px-4 py-2 bg-blue-500 text-white rounded-lg"
      >
        下一页
      </button>
    </div>
  </div>
</template>
