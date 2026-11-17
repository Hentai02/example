import { createRouter, createWebHistory } from 'vue-router'
import DashboardView from '../components/DashBoard.vue' // 主页
import UserLoginView from '../components/UserLogin.vue' // 登录

// 工作台
import EmployeeWorkbenchView from '../components/tab1/EmployeeWorkbench.vue' // 员工工作台

// 审批管理
import LeaveApproveView from '../components/tab2/LeaveApproval.vue' // 请假审批
import OvertimeApprovalView from '../components/tab2/OvertimeApproval.vue' // 加班审批
import ReimbursementApprovalView from '../components/tab2/ReimbursementApproval.vue' // 报销审批

// 日程与任务管理
import ScheduleManagementView from '../components/tab3/ScheduleManagement.vue' // 日程管理
import TaskManagementView from '../components/tab3/TaskManagement.vue' // 任务管理

// 人力资源管理
import AttendanceManagementView from '../components/tab4/AttendanceManagement.vue' // 考勤管理
import LeaveManagementView from '../components/tab4/LeaveManagement.vue' // 请假管理
import SalaryManagementView from '../components/tab4/SalaryManagement.vue' // 工资管理
import EmployeeManagementView from '../components/tab4/EmployeeManagement.vue' // 员工管理

// bean编辑，新建
import TaskView from '../components/model_view/TaskView.vue' // 任务
import ScheduleView from '../components/model_view/ScheduleView.vue' // 日程
import LeaveRequestView from '../components/model_view/LeaveRequestView.vue' // 请假申请
import ReimbursementRequestView from '../components/model_view/ReimbursementRequestView.vue' // 报销申请
import OvertimeRequestView from '../components/model_view/OvertimeRequestView.vue' // 加班申请
import AttendanceView from '../components/model_view/AttendanceView.vue' // 考勤
import PayrollView from '../components/model_view/PayrollView.vue' // 工资
import EmployeeView from '../components/model_view/EmployeeView.vue' // 员工
import MessageView from '../components/model_view/MessageView.vue' // 通知
import AssignPermissionsView from '../components/tab6/AssignPermissions.vue'

import LocalMessageView from '../components/tab5/LocalMessage.vue' // 本地消息
import FileManagementView from '../components/tab6/FileManagement.vue' // 文件管理
import SystemSettingstView from '../components/tab7/SystemSettings.vue' // 系统管理

import ForbiddenView from '../views/ForbiddenView.vue'

const routes = [
  {
    path: '/dashboard',
    name: 'dashboard',
    component: DashboardView,
    meta: { requiresAuth: true },
    children: [
      {
        path: 'workbench',
        component: EmployeeWorkbenchView,
        // children: [{ path: 'leaveRequest', component: LeaveRequestView }],
      },
      {
        path: 'tab2',
        children: [
          {
            path: 'leaveApprove',
            component: LeaveApproveView,
            meta: { requiresAuth: true, requiredRole: ['ROLE_生产主管', 'ROLE_管理员'] },
          },
          {
            path: 'reimbursementApproval',
            component: ReimbursementApprovalView,
            meta: { requiresAuth: true, requiredRole: ['ROLE_财务主管', 'ROLE_管理员'] },
          },
          {
            path: 'overtimeApproval',
            component: OvertimeApprovalView,
            meta: { requiresAuth: true, requiredRole: ['ROLE_生产主管', 'ROLE_管理员'] },
          },
        ],
      },
      {
        path: 'tab3',
        children: [
          {
            path: 'scheduleManagement',
            component: ScheduleManagementView,
            meta: { requiresAuth: true, requiredRole: ['ROLE_生产主管', 'ROLE_管理员'] },
          },
          {
            path: 'taskManagement',
            component: TaskManagementView,
            meta: { requiresAuth: true, requiredRole: ['ROLE_组长', 'ROLE_管理员'] },
          },
        ],
      },
      {
        path: 'tab4',

        children: [
          {
            path: 'attendanceManagement',
            component: AttendanceManagementView,
            meta: {
              requiresAuth: true,
              requiredRole: ['ROLE_人事主管', 'ROLE_组长', 'ROLE_管理员'],
            },
          },
          {
            path: 'leaveManagement',
            component: LeaveManagementView,
            meta: {
              requiresAuth: true,
              requiredRole: ['ROLE_人事主管', 'ROLE_组长', 'ROLE_管理员'],
            },
          },
          {
            path: 'salaryManagement',
            component: SalaryManagementView,
            meta: {
              requiresAuth: true,
              requiredRole: ['ROLE_人事主管', 'ROLE_管理员', 'ROLE_财务主管'],
            },
          },
          {
            path: 'employeeManagement',
            component: EmployeeManagementView,
            meta: { requiresAuth: true, requiredRole: ['ROLE_人事主管', 'ROLE_管理员'] },
          },
        ],
      },
      { path: 'localMessage', component: LocalMessageView },
      {
        path: 'fileManagement',
        component: FileManagementView,
      },
      { path: 'system', component: SystemSettingstView },

      {
        path: '/dashboard/tab2/schedule',
        name: 'ScheduleView',
        component: ScheduleView,
        props: (route) => ({
          schedule: route.query.schedule ? JSON.parse(route.query.schedule) : null,
        }),
      },
      {
        path: '/dashboard/tab2/task',
        component: TaskView,
        name: 'TaskView',
        props: (route) => ({ task: route.query.task ? JSON.parse(route.query.task) : null }),
      },
      {
        path: '/dashboard/tab2/leave',
        component: LeaveRequestView,
        name: 'LeaveRequestView',
        props: (route) => ({ leave: route.query.leave ? JSON.parse(route.query.leave) : null }),
      },
      {
        path: '/dashboard/tab2/attendance',
        component: AttendanceView,
        name: 'AttendanceView',
        props: (route) => ({
          attendance: route.query.attendance ? JSON.parse(route.query.attendance) : null,
        }),
      },
      {
        path: '/dashboard/tab2/payroll',
        component: PayrollView,
        name: 'PayrollView',
        props: (route) => ({
          payroll: route.query.payroll ? JSON.parse(route.query.payroll) : null,
        }),
      },
      {
        path: '/dashboard/tab2/employee',
        component: EmployeeView,
        name: 'EmployeeView',
        props: (route) => ({
          employee: route.query.employee ? JSON.parse(route.query.employee) : null,
        }),
      },
      {
        path: '/dashboard/tab2/message',
        component: MessageView,
        name: 'MessageView',
        props: (route) => ({
          message: route.query.message ? JSON.parse(route.query.message) : null,
        }),
      },
      { path: '/dashboard/tab2/overtimeRequest', component: OvertimeRequestView },
      { path: '/dashboard/tab2/reimbursementRequest', component: ReimbursementRequestView },
      {
        path: '/dashboard/tab7/assignPermissions',
        name: 'AssignPermissionsView',
        component: AssignPermissionsView,
        props: (route) => ({
          file: route.query.file ? JSON.parse(route.query.file) : null,
        }),
      },
    ],
  },
  { path: '/', name: 'login', component: UserLoginView, meta: { requiresGuest: true } },
  { path: '/403', name: '403', component: ForbiddenView },
]

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes,
})

// router.beforeEach((to, from, next) => {
//   const user = localStorage.getItem('user')
//   if (to.matched.some((record) => record.meta.requiresAuth)) {
//     if (!user) {
//       next({ name: 'login' })
//     } else {
//       next()
//     }
//   } else if (to.matched.some((record) => record.meta.requiresGuest)) {
//     if (user) {
//       next({ name: 'dashboard' })
//     } else {
//       next()
//     }
//   } else {
//     next()
//   }
// })

function getUserAuthorities() {
  const user = JSON.parse(localStorage.getItem('user-req'))
  return user ? user.authorities : []
}

// router.beforeEach((to, from, next) => {
//   const userAuthorities = getUserAuthorities() // 获取当前用户的权限列表
//   const requiresAuth = to.matched.some((record) => record.meta.requiresAuth)
//   let requiredRole = to.meta.requiredRole

//   // 如果目标路由需要认证且用户未登录
//   if (requiresAuth && !userAuthorities.length) {
//     return next({ name: 'login' }) // 重定向到登录页面
//   }

//   // 如果目标路由需要特定角色权限
//   if (requiredRole) {
//     // 确保 requiredRole 是数组
//     if (!Array.isArray(requiredRole)) {
//       requiredRole = [requiredRole] // 如果不是数组，将其转化为数组
//     }

//     // 判断用户权限是否匹配
//     if (!requiredRole.some((role) => userAuthorities.includes(role))) {
//       console.log(requiredRole)
//       console.log(userAuthorities)

//       return next({ name: '403' }) // 没有权限时跳转到无权限页面
//     }
//   }

//   next() // 继续导航
// })

router.beforeEach((to, from, next) => {
  const userAuthorities = getUserAuthorities().map((item) => item.authority) // 提取 authority 属性
  const requiresAuth = to.matched.some((record) => record.meta.requiresAuth)
  let requiredRole = to.meta.requiredRole

  // 如果目标路由需要认证且用户未登录
  if (requiresAuth && !userAuthorities.length) {
    return next({ name: 'login' }) // 重定向到登录页面
  }

  // 如果目标路由需要特定角色权限
  if (requiredRole) {
    // 确保 requiredRole 是数组
    if (!Array.isArray(requiredRole)) {
      requiredRole = [requiredRole] // 如果不是数组，将其转化为数组
    }

    // 判断用户权限是否匹配
    if (!requiredRole.some((role) => userAuthorities.includes(role))) {
      console.log('User Authorities:', userAuthorities)
      console.log('Required Roles:', requiredRole)
      return next({ name: '403' }) // 没有权限时跳转到无权限页面
    }
  }

  next() // 继续导航
})

export default router
