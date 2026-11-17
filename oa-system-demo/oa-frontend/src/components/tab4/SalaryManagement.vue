<script setup>
import { ref, onMounted } from 'vue'
import axios from 'axios'
import { useRouter } from 'vue-router'

const router = useRouter()

const payrolls = ref([]) // 薪酬记录

// 获取薪酬记录
function fetchPayrolls() {
  // axios
  //   .get('/api/payroll/all')
  //   .then((response) => {
  //     payrolls.value = response.data
  //   })
  //   .catch((error) => {
  //     console.error(error)
  //   })

  axios
    .get('/api/payroll/all', {
      params: {
        page: page.value,
        size: size.value,
        sort: sort.value,
        keyword: searchKeyword.value,
      },
    })
    .then((response) => {
      payrolls.value = response.data.content
      totalPages.value = response.data.totalPages
    })
    .catch((error) => {
      console.error(error)
    })
}

// 编辑薪酬记录
function editPayroll(payroll) {
  router.push({
    name: 'PayrollView',
    query: { payroll: JSON.stringify(payroll) },
  })
}

// 删除薪酬记录
function deletePayroll(id) {
  const confirmed = confirm('确定要删除这条薪酬记录吗？')
  if (!confirmed) {
    return
  }
  axios
    .delete(`/api/payroll/${id}`)
    .then(() => {
      fetchPayrolls()
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

const page = ref(0)
const size = ref(5)
const sort = ref('id,asc')
const totalPages = ref(0)
const searchKeyword = ref('')

// 上一页
function prevPage() {
  if (page.value > 0) {
    page.value--
    fetchPayrolls()
  }
}

// 下一页
function nextPage() {
  page.value++
  fetchPayrolls()
}

onMounted(() => {
  fetchPayrolls()
  fetchEmployeeProfiles()
})
</script>

<template>
  <!-- 薪酬管理 -->
  <div class="min-h-screen bg-white p-6 rounded-lg shadow-lg">
    <h2 class="text-2xl font-bold mb-6">薪酬管理</h2>
    <div class="flex justify-end mb-4">
      <RouterLink
        to="/dashboard/tab2/payroll?payroll={}"
        class="bg-green-500 text-white px-4 py-2 rounded-lg"
      >
        新增薪酬记录
      </RouterLink>
    </div>
    <select
      v-model="searchKeyword"
      @change="fetchPayrolls"
      class="w-full p-3 border border-gray-300 rounded-lg mb-4"
    >
      <option value="">所有员工</option>
      <option v-for="employee in employees" :key="employee.id" :value="employee.id">
        {{ employee.username }}
      </option>
    </select>
    <div v-if="payrolls.length === 0" class="text-center text-gray-500">没有薪酬记录</div>
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
              基础工资
            </th>
            <th
              class="py-2 px-4 border-b border-gray-200 bg-gray-50 text-left text-xs font-semibold text-gray-600 uppercase tracking-wider"
            >
              奖金
            </th>
            <th
              class="py-2 px-4 border-b border-gray-200 bg-gray-50 text-left text-xs font-semibold text-gray-600 uppercase tracking-wider"
            >
              加班工资
            </th>
            <th
              class="py-2 px-4 border-b border-gray-200 bg-gray-50 text-left text-xs font-semibold text-gray-600 uppercase tracking-wider"
            >
              扣除
            </th>
            <th
              class="py-2 px-4 border-b border-gray-200 bg-gray-50 text-left text-xs font-semibold text-gray-600 uppercase tracking-wider"
            >
              总工资
            </th>
            <th
              class="py-2 px-4 border-b border-gray-200 bg-gray-50 text-left text-xs font-semibold text-gray-600 uppercase tracking-wider"
            >
              月份
            </th>
            <th
              class="py-2 px-4 border-b border-gray-200 bg-gray-50 text-left text-xs font-semibold text-gray-600 uppercase tracking-wider"
            >
              操作
            </th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="record in payrolls" :key="record.id">
            <td class="py-2 px-4 border-b border-gray-200">{{ record.id }}</td>
            <td class="py-2 px-4 border-b border-gray-200">{{ record.employeeId }}</td>
            <td class="py-2 px-4 border-b border-gray-200">{{ record.baseSalary }}</td>
            <td class="py-2 px-4 border-b border-gray-200">{{ record.bonus }}</td>
            <td class="py-2 px-4 border-b border-gray-200">{{ record.overtimeSalary }}</td>
            <td class="py-2 px-4 border-b border-gray-200">{{ record.deductions }}</td>
            <td class="py-2 px-4 border-b border-gray-200">{{ record.totalSalary }}</td>
            <td class="py-2 px-4 border-b border-gray-200">{{ record.createdAt }}</td>
            <td class="py-2 px-4 border-b border-gray-200">
              <button
                @click="editPayroll(record)"
                class="bg-blue-500 text-white px-4 py-2 rounded-lg mr-2"
              >
                编辑
              </button>
              <button
                @click="deletePayroll(record.id)"
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
