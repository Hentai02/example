<script setup>
import { ref, onMounted } from 'vue'
import axios from 'axios'
import { useRouter } from 'vue-router'

const router = useRouter()

const schedules = ref([]) // 日程列表
// const visibleDialogOfEditEmployee = ref(false) // 是否显示编辑员工对话框

// 获取日程
function fetchSchedules() {
  // axios
  //   .get('/api/schedule')
  //   .then((response) => {
  //     schedules.value = response.data
  //   })
  //   .catch((error) => {
  //     console.error(error)
  //   })
  axios
    .get('/api/schedule', {
      params: {
        page: page.value,
        size: size.value,
        sort: sort.value,
        keyword: searchKeyword.value,
      },
    })
    .then((response) => {
      schedules.value = response.data.content
      totalPages.value = response.data.totalPages
    })
    .catch((error) => {
      console.error(error)
    })
}

// 删除日程
function deleteSchedule(id) {
  const confirmed = confirm('确定要删除该日程吗？')
  if (confirmed) {
    axios
      .delete(`/api/schedule/${id}`)
      .then(() => {
        fetchSchedules()
        alert('删除成功')
      })
      .catch((error) => {
        console.error(error)
      })
  }
}

// 编辑日程
function editSchedule(schedule) {
  // console.log('编辑日程:', schedule)
  // router.push({ name: 'ScheduleView', params: { schedule } })
  router.push({
    name: 'ScheduleView',
    query: { schedule: JSON.stringify(schedule) },
  })
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
    fetchSchedules()
  }
}

// 下一页
function nextPage() {
  page.value++
  fetchSchedules()
}

onMounted(() => {
  fetchSchedules()
})
</script>

<template>
  <!-- 日程管理 -->
  <div class="min-h-screen bg-white p-6 rounded-lg shadow-lg">
    <h2 class="text-2xl font-bold mb-6">日程管理</h2>
    <div class="flex justify-end mb-4">
      <RouterLink
        to="/dashboard/tab2/schedule?schedule={}"
        class="bg-blue-500 hover:bg-blue-700 text-white font-bold py-2 px-4 rounded inline-block"
      >
        新增日程
      </RouterLink>
    </div>
    <input
      type="text"
      v-model="searchKeyword"
      @input="fetchSchedules"
      placeholder="搜索"
      class="w-full p-3 border border-gray-300 rounded-lg mb-4"
    />
    <div v-if="schedules.length === 0" class="text-center text-gray-500">没有日程</div>
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
              标题
            </th>
            <th
              class="py-2 px-4 border-b border-gray-200 bg-gray-50 text-left text-xs font-semibold text-gray-600 uppercase tracking-wider"
            >
              描述
            </th>
            <th
              class="py-2 px-4 border-b border-gray-200 bg-gray-50 text-left text-xs font-semibold text-gray-600 uppercase tracking-wider"
            >
              开始时间
            </th>
            <th
              class="py-2 px-4 border-b border-gray-200 bg-gray-50 text-left text-xs font-semibold text-gray-600 uppercase tracking-wider"
            >
              结束时间
            </th>
            <th
              class="py-2 px-4 border-b border-gray-200 bg-gray-50 text-left text-xs font-semibold text-gray-600 uppercase tracking-wider"
            >
              提醒时间
            </th>
            <th
              class="py-2 px-4 border-b border-gray-200 bg-gray-50 text-left text-xs font-semibold text-gray-600 uppercase tracking-wider"
            >
              类别
            </th>
            <th
              class="py-2 px-4 border-b border-gray-200 bg-gray-50 text-left text-xs font-semibold text-gray-600 uppercase tracking-wider"
            >
              共享
            </th>
            <th
              class="py-2 px-4 border-b border-gray-200 bg-gray-50 text-left text-xs font-semibold text-gray-600 uppercase tracking-wider"
            >
              操作
            </th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="item in schedules" :key="item.id">
            <td class="py-2 px-4 border-b border-gray-200">{{ item.id }}</td>
            <td class="py-2 px-4 border-b border-gray-200">{{ item.employeeId }}</td>
            <td class="py-2 px-4 border-b border-gray-200">{{ item.title }}</td>
            <td class="py-2 px-4 border-b border-gray-200">{{ item.description }}</td>
            <td class="py-2 px-4 border-b border-gray-200">{{ item.startTime }}</td>
            <td class="py-2 px-4 border-b border-gray-200">{{ item.endTime }}</td>
            <td class="py-2 px-4 border-b border-gray-200">{{ item.reminderTime }}</td>
            <td class="py-2 px-4 border-b border-gray-200">{{ item.category }}</td>
            <td class="py-2 px-4 border-b border-gray-200">{{ item.sharedWith }}</td>
            <td class="py-2 px-4 border-b border-gray-200">
              <button
                @click="editSchedule(item)"
                class="bg-blue-500 hover:bg-blue-700 text-white font-bold py-1 px-3 rounded focus:outline-none focus:shadow-outline mr-2"
              >
                编辑
              </button>
              <button
                @click="deleteSchedule(item.id)"
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
