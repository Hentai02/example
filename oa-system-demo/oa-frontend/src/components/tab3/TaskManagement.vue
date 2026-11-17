<script setup>
import { ref, onMounted } from 'vue'
import axios from 'axios'
import { useRouter } from 'vue-router'

const router = useRouter()

// 示例员工档案数据
const tasks = ref([])

function fetchtasks() {
  // axios
  //   .get('/api/task')
  //   .then((response) => {
  //     tasks.value = response.data
  //   })
  //   .catch((error) => {
  //     console.error(error)
  //   })
  axios
    .get('/api/task', {
      params: {
        page: page.value,
        size: size.value,
        sort: sort.value,
        keyword: searchKeyword.value,
      },
    })
    .then((response) => {
      tasks.value = response.data.content
      totalPages.value = response.data.totalPages
    })
    .catch((error) => {
      console.error(error)
    })
}

function editTask(task) {
  router.push({
    name: 'TaskView',
    query: { task: JSON.stringify(task) },
  })
}

function deleteTask(id) {
  axios
    .delete(`/api/task/${id}`)
    .then(() => {
      fetchtasks()
      console.log('删除成功')
    })
    .catch((error) => {
      console.error(error)
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
    fetchtasks()
  }
}

// 下一页
function nextPage() {
  page.value++
  fetchtasks()
}

onMounted(() => {
  fetchtasks()
})

// 优先级映射方法
function mapPriority(priority) {
  const priorityMap = {
    Low: '低',
    Medium: '中',
    High: '高',
  }
  return priorityMap[priority] || '未知' // 如果值不匹配，返回“未知”
}

function mapStatus(status) {
  const statusMap = {
    Pending: '待办',
    'In Progress': '进行中',
    Completed: '已完成',
    Cancelled: '已取消',
  }
  return statusMap[status] || '未知' // 如果值不匹配，返回“未知”
}
</script>

<template>
  <!-- 任务管理 -->
  <div class="min-h-screen bg-white p-6 rounded-lg shadow-lg">
    <h2 class="text-2xl font-bold mb-6">任务管理</h2>
    <div class="flex justify-end mb-4">
      <RouterLink
        to="/dashboard/tab2/task?task={}"
        class="bg-blue-500 hover:bg-blue-700 text-white font-bold py-2 px-4 rounded"
      >
        新建任务
      </RouterLink>
    </div>
    <input
      type="text"
      v-model="searchKeyword"
      @input="fetchtasks"
      placeholder="搜索"
      class="w-full p-3 border border-gray-300 rounded-lg mb-4"
    />
    <div v-if="tasks.length === 0" class="text-center text-gray-500">没有任务</div>
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
              创建者
            </th>
            <th
              class="py-2 px-4 border-b border-gray-200 bg-gray-50 text-left text-xs font-semibold text-gray-600 uppercase tracking-wider"
            >
              执行人
            </th>
            <th
              class="py-2 px-4 border-b border-gray-200 bg-gray-50 text-left text-xs font-semibold text-gray-600 uppercase tracking-wider"
            >
              优先级
            </th>
            <th
              class="py-2 px-4 border-b border-gray-200 bg-gray-50 text-left text-xs font-semibold text-gray-600 uppercase tracking-wider"
            >
              状态
            </th>
            <th
              class="py-2 px-4 border-b border-gray-200 bg-gray-50 text-left text-xs font-semibold text-gray-600 uppercase tracking-wider"
            >
              截至日期
            </th>
            <th
              class="py-2 px-4 border-b border-gray-200 bg-gray-50 text-left text-xs font-semibold text-gray-600 uppercase tracking-wider"
            >
              完成日期
            </th>
            <th
              class="py-2 px-4 border-b border-gray-200 bg-gray-50 text-left text-xs font-semibold text-gray-600 uppercase tracking-wider"
            >
              操作
            </th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="profile in tasks" :key="profile.id">
            <td class="py-2 px-4 border-b border-gray-200">{{ profile.id }}</td>
            <td class="py-2 px-4 border-b border-gray-200">{{ profile.title }}</td>
            <td class="py-2 px-4 border-b border-gray-200">{{ profile.description }}</td>
            <td class="py-2 px-4 border-b border-gray-200">{{ profile.creatorId }}</td>
            <td class="py-2 px-4 border-b border-gray-200">{{ profile.assigneeId }}</td>
            <td class="py-2 px-4 border-b border-gray-200">{{ mapPriority(profile.priority) }}</td>
            <td class="py-2 px-4 border-b border-gray-200">{{ mapStatus(profile.status) }}</td>
            <td class="py-2 px-4 border-b border-gray-200">{{ profile.dueDate }}</td>
            <td class="py-2 px-4 border-b border-gray-200">{{ profile.completionTime }}</td>
            <td class="py-2 px-4 border-b border-gray-200">
              <button
                @click="editTask(profile)"
                class="bg-blue-500 hover:bg-blue-700 text-white font-bold py-1 px-3 rounded focus:outline-none focus:shadow-outline mr-2"
              >
                编辑
              </button>
              <button
                @click="deleteTask(profile.id)"
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
