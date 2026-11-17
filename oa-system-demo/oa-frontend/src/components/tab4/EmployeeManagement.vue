<script setup>
import { ref, onMounted } from 'vue'
import axios from 'axios'
import { useRouter } from 'vue-router'

const router = useRouter()

const employeeProfiles = ref([]) // 员工档案
const page = ref(0)
const size = ref(5)
const sort = ref('id,asc')
const totalPages = ref(0)
const searchKeyword = ref('')

// 上一页
function prevPage() {
  if (page.value > 0) {
    page.value--
    fetchEmployeeProfiles()
  }
}

// 下一页
function nextPage() {
  page.value++
  fetchEmployeeProfiles()
}

// 获取员工档案
// function fetchEmployeeProfiles() {
//   axios
//     .get(`/api/employee/all?page=${page.value}&size=${size.value}&sort=${sort.value}`)
//     .then((response) => {
//       employeeProfiles.value = response.data.content
//       console.log(response.data)
//       totalPages.value = response.data.totalPages
//     })
//     .catch((error) => {
//       console.error(error)
//     })
// }

async function fetchEmployeeProfiles() {
  try {
    const response = await axios.get(`/api/employee/all`, {
      params: {
        page: page.value,
        size: size.value,
        sort: sort.value,
        keyword: searchKeyword.value,
      },
    })
    employeeProfiles.value = response.data.content
    totalPages.value = response.data.totalPages
  } catch (error) {
    console.error(error)
  }
}

// 删除员工
function deleteEmployee(id) {
  const confirmed = confirm('确定要删除这条员工记录吗？')
  if (!confirmed) {
    return
  }
  axios
    .delete(`/api/employee/${id}`)
    .then(() => {
      fetchEmployeeProfiles()
    })
    .catch((error) => {
      console.error(error)
    })
}

// 编辑员工
function editEmployee(employee) {
  router.push({
    name: 'EmployeeView',
    query: { employee: JSON.stringify(employee) },
  })
}

onMounted(() => {
  fetchEmployeeProfiles()
})
</script>

<template>
  <!-- 员工档案管理 -->
  <div class="min-h-screen bg-white p-6 rounded-lg shadow-lg">
    <h2 class="text-2xl font-bold mb-6">员工档案管理</h2>
    <div class="flex justify-end mb-4">
      <RouterLink
        to="/dashboard/tab2/employee?employee={}"
        class="bg-blue-500 hover:bg-blue-700 text-white font-bold py-2 px-4 rounded"
      >
        添加员工
      </RouterLink>
    </div>
    <input
      type="text"
      v-model="searchKeyword"
      @input="fetchEmployeeProfiles"
      placeholder="搜索员工"
      class="w-full p-3 border border-gray-300 rounded-lg mb-4"
    />
    <div v-if="employeeProfiles.length === 0" class="text-center text-gray-500">没有员工档案</div>
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
              用户名
            </th>
            <th
              class="py-2 px-4 border-b border-gray-200 bg-gray-50 text-left text-xs font-semibold text-gray-600 uppercase tracking-wider"
            >
              部门
            </th>
            <th
              class="py-2 px-4 border-b border-gray-200 bg-gray-50 text-left text-xs font-semibold text-gray-600 uppercase tracking-wider"
            >
              职位
            </th>
            <th
              class="py-2 px-4 border-b border-gray-200 bg-gray-50 text-left text-xs font-semibold text-gray-600 uppercase tracking-wider"
            >
              性别
            </th>
            <th
              class="py-2 px-4 border-b border-gray-200 bg-gray-50 text-left text-xs font-semibold text-gray-600 uppercase tracking-wider"
            >
              出生日期
            </th>
            <th
              class="py-2 px-4 border-b border-gray-200 bg-gray-50 text-left text-xs font-semibold text-gray-600 uppercase tracking-wider"
            >
              入职日期
            </th>
            <th
              class="py-2 px-4 border-b border-gray-200 bg-gray-50 text-left text-xs font-semibold text-gray-600 uppercase tracking-wider"
            >
              手机号
            </th>
            <th
              class="py-2 px-4 border-b border-gray-200 bg-gray-50 text-left text-xs font-semibold text-gray-600 uppercase tracking-wider"
            >
              邮箱
            </th>
            <th
              class="py-2 px-4 border-b border-gray-200 bg-gray-50 text-left text-xs font-semibold text-gray-600 uppercase tracking-wider"
            >
              操作
            </th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="profile in employeeProfiles" :key="profile.id">
            <td class="py-2 px-4 border-b border-gray-200">{{ profile.id }}</td>
            <td class="py-2 px-4 border-b border-gray-200">{{ profile.username }}</td>
            <td class="py-2 px-4 border-b border-gray-200">{{ profile.departmentId }}</td>
            <td class="py-2 px-4 border-b border-gray-200">{{ profile.positionId }}</td>
            <td class="py-2 px-4 border-b border-gray-200">{{ profile.gender ? '女' : '男' }}</td>
            <td class="py-2 px-4 border-b border-gray-200">{{ profile.birthDate }}</td>
            <td class="py-2 px-4 border-b border-gray-200">{{ profile.hireDate }}</td>
            <td class="py-2 px-4 border-b border-gray-200">{{ profile.phone }}</td>
            <td class="py-2 px-4 border-b border-gray-200">{{ profile.email }}</td>
            <td class="py-2 px-4 border-b border-gray-200">
              <button
                @click="editEmployee(profile)"
                class="bg-blue-500 hover:bg-blue-700 text-white font-bold py-1 px-3 rounded focus:outline-none focus:shadow-outline mr-2"
              >
                编辑
              </button>
              <button
                @click="deleteEmployee(profile.id)"
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
