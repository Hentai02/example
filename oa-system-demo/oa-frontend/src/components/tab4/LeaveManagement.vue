<script setup>
import { ref, onMounted } from 'vue'
import axios from 'axios'
import { useRouter } from 'vue-router'

const router = useRouter()

const leaves = ref([]) // 请假记录
const page = ref(0)
const size = ref(5)
const sort = ref('id,asc')
const totalPages = ref(0)
const searchKeyword = ref('')
// 获取请假记录
// function fetchleaves() {
//   axios
//     .get('/api/leave/all')
//     .then((response) => {
//       leaves.value = response.data
//     })
//     .catch((error) => {
//       console.error(error)
//     })
// }
async function fetchleaves() {
  try {
    const response = await axios.get(`/api/leave/all`, {
      params: {
        page: page.value,
        size: size.value,
        sort: sort.value,
        keyword: searchKeyword.value,
      },
    })
    leaves.value = response.data.content
    totalPages.value = response.data.totalPages
    if (searchKeyword.value) {
      page.value = 0
    }
  } catch (error) {
    console.error(error)
  }
}

// 编辑请假记录
function editLeave(leave) {
  router.push({
    name: 'LeaveRequestView',
    query: { leave: JSON.stringify(leave) },
  })
}

// 删除请假记录
function deleteLeave(id) {
  const confirmed = confirm('确定要删除这条请假记录吗？')
  if (!confirmed) {
    return
  }
  axios
    .delete(`/api/leave/${id}`)
    .then(() => {
      fetchleaves()
    })
    .catch((error) => {
      console.error(error)
    })
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

// 上一页
function prevPage() {
  if (page.value > 0) {
    page.value--
    fetchleaves()
  }
}

// 下一页
function nextPage() {
  page.value++
  fetchleaves()
}

onMounted(() => {
  fetchleaves()
  fetchEmployeeProfiles()
})
</script>

<template>
  <!-- 请假管理 -->
  <div class="min-h-screen bg-white p-6 rounded-lg shadow-lg">
    <h2 class="text-2xl font-bold mb-6">请假管理</h2>
    <div class="flex justify-end mb-4">
      <RouterLink
        to="/dashboard/tab2/leave?leave={}"
        class="bg-green-500 text-white px-4 py-2 rounded-lg"
      >
        新增请假请求
      </RouterLink>
    </div>
    <select
      v-model="searchKeyword"
      @change="fetchleaves"
      class="w-full p-3 border border-gray-300 rounded-lg mb-4"
    >
      <option value="">所有员工</option>
      <option v-for="employee in employees" :key="employee.id" :value="employee.id">
        {{ employee.username }}
      </option>
    </select>
    <div v-if="leaves.length === 0" class="text-center text-gray-500">没有请假记录</div>
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
              开始日期
            </th>
            <th
              class="py-2 px-4 border-b border-gray-200 bg-gray-50 text-left text-xs font-semibold text-gray-600 uppercase tracking-wider"
            >
              结束日期
            </th>
            <th
              class="py-2 px-4 border-b border-gray-200 bg-gray-50 text-left text-xs font-semibold text-gray-600 uppercase tracking-wider"
            >
              请假类型
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
          <tr v-for="record in leaves" :key="record.id">
            <td class="py-2 px-4 border-b border-gray-200">{{ record.id }}</td>
            <td class="py-2 px-4 border-b border-gray-200">{{ record.employeeId }}</td>
            <td class="py-2 px-4 border-b border-gray-200">{{ record.startDate }}</td>
            <td class="py-2 px-4 border-b border-gray-200">{{ record.endDate }}</td>
            <td class="py-2 px-4 border-b border-gray-200">{{ record.leaveType }}</td>
            <td class="py-2 px-4 border-b border-gray-200">{{ record.status }}</td>
            <td class="py-2 px-4 border-b border-gray-200">
              <button
                @click="editLeave(record)"
                class="bg-blue-500 text-white px-4 py-2 rounded-lg mr-2"
              >
                编辑
              </button>
              <button
                @click="deleteLeave(record.id)"
                class="bg-red-500 text-white px-4 py-2 rounded-lg"
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
