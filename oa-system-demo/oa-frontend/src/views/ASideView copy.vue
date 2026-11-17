<template>
  <aside class="w-64 bg-gray-100 h-screen shadow-lg">
    <div class="p-4 text-2xl font-bold">Dashboard</div>
    <nav>
      <ul>
        <li @click="toggleMenu('menu0')">
          <RouterLink to="/dashboard/workbench" class="block py-3 px-4 hover:bg-gray-200">
            <font-awesome-icon class="pr-4" icon="fa-solid fa-hashtag" />
            <span>工作台</span>
          </RouterLink>
        </li>
        <li class="group">
          <button
            @click="toggleMenu('menu1')"
            class="w-full text-left px-4 py-3 flex justify-between items-center hover:bg-gray-200 focus:outline-none"
          >
            <div class="flex items-center">
              <font-awesome-icon icon="fa-solid fa-pen-fancy" class="pr-4" />
              <span>流程审批</span>
            </div>
            <font-awesome-icon :icon="['fas', 'chevron-down']" />
          </button>
          <ul v-if="openMenu === 'menu1'" class="pl-8">
            <li>
              <RouterLink
                to="/dashboard/tab2/leaveApprove"
                class="block px-4 py-3 hover:bg-gray-200"
                >请假审批</RouterLink
              >
            </li>
            <li>
              <RouterLink
                to="/dashboard/tab2/overtimeApproval"
                class="block px-4 py-3 hover:bg-gray-200"
                >加班审批</RouterLink
              >
            </li>
            <li>
              <RouterLink
                to="/dashboard/tab2/reimbursementApproval"
                class="block px-4 py-3 hover:bg-gray-200"
                >报销审批</RouterLink
              >
            </li>
          </ul>
        </li>

        <li class="group">
          <button
            @click="toggleMenu('menu2')"
            class="w-full text-left px-4 py-3 flex justify-between items-center hover:bg-gray-200 focus:outline-none"
          >
            <div class="flex items-center">
              <font-awesome-icon icon="fa-solid fa-pen-fancy" class="pr-4" />
              <span>日程与任务管理</span>
            </div>
            <font-awesome-icon :icon="['fas', 'chevron-down']" />
          </button>
          <ul v-if="openMenu === 'menu2'" class="pl-8">
            <li>
              <RouterLink
                to="/dashboard/tab3/scheduleManagement"
                class="block px-4 py-3 hover:bg-gray-200"
                >日程管理</RouterLink
              >
            </li>
            <li>
              <RouterLink
                to="/dashboard/tab3/taskManagement"
                class="block px-4 py-3 hover:bg-gray-200"
                >任务管理</RouterLink
              >
            </li>
          </ul>
        </li>

        <li class="group">
          <button
            @click="toggleMenu('menu3')"
            class="w-full text-left px-4 py-3 flex justify-between items-center hover:bg-gray-200 focus:outline-none"
          >
            <div class="flex items-center">
              <font-awesome-icon icon="fa-solid fa-pen-fancy" class="pr-4" />
              <span>人力资源管理</span>
            </div>
            <font-awesome-icon :icon="['fas', 'chevron-down']" />
          </button>
          <ul v-if="openMenu === 'menu3'" class="pl-8">
            <li>
              <RouterLink
                to="/dashboard/tab4/attendanceManagement"
                class="block px-4 py-3 hover:bg-gray-200"
                >考勤管理</RouterLink
              >
            </li>
            <li>
              <RouterLink
                to="/dashboard/tab4/leaveManagement"
                class="block px-4 py-3 hover:bg-gray-200"
                >请假管理</RouterLink
              >
            </li>
            <li>
              <RouterLink
                to="/dashboard/tab4/salaryManagement"
                class="block px-4 py-3 hover:bg-gray-200"
                >薪酬管理</RouterLink
              >
            </li>
            <li>
              <RouterLink
                to="/dashboard/tab4/employeeManagement"
                class="block px-4 py-3 hover:bg-gray-200"
                >员工档案管理</RouterLink
              >
            </li>
          </ul>
        </li>

        <RouterLink
          @click="toggleMenu('menu4')"
          to="/dashboard/localMessage"
          class="block py-3 px-4 hover:bg-gray-200"
        >
          <font-awesome-icon class="pr-4" icon="fa-solid fa-hashtag" />
          <span>站内信</span>
        </RouterLink>

        <RouterLink
          @click="toggleMenu('menu5')"
          to="/dashboard/fileManagement"
          class="block py-3 px-4 hover:bg-gray-200"
        >
          <font-awesome-icon class="pr-4" icon="fa-solid fa-hashtag" />
          <span>文档管理</span>
        </RouterLink>

        <!-- <RouterLink
          @click="toggleMenu('menu6')"
          to="/dashboard/system"
          class="block py-3 px-4 hover:bg-gray-200"
        >
          <font-awesome-icon class="pr-4" icon="fa-solid fa-hashtag" />
          <span>系统设置</span>
        </RouterLink> -->
      </ul>
    </nav>
  </aside>
</template>
<script setup>
import { ref } from 'vue'

const openMenu = ref('menu0')

function toggleMenu(menu) {
  openMenu.value = openMenu.value === menu ? 'menu0' : menu
}

const permissions = ref([])

const user_req = JSON.parse(localStorage.getItem('user-req'))
if (user_req && user_req.authorities) {
  const authorities = user_req.authorities

  authorities.forEach((element) => {
    permissions.value.push(element.authority)
  })
}

function hasPermission(requiredRoles) {
  return requiredRoles.some((role) => permissions.value.includes(role))
}

console.log(hasPermission(['ROLE_财务主管', 'ROLE_管理员']))
</script>
