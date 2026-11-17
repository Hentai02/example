import './assets/main.css'

import { createApp } from 'vue'
import App from './App.vue'
import router from './router'
import { createPinia } from 'pinia'
import { library } from '@fortawesome/fontawesome-svg-core'
import { FontAwesomeIcon } from '@fortawesome/vue-fontawesome'
import { fas } from '@fortawesome/free-solid-svg-icons'
import { far } from '@fortawesome/free-regular-svg-icons'
import axios from 'axios'

// 添加请求拦截器
axios.interceptors.request.use(
  function (config) {
    const user = JSON.parse(localStorage.getItem('user'))
    if (user) {
      config.auth = {
        username: user.username,
        password: user.password,
      }
    }
    return config
  },
  function (error) {
    // 对请求错误做些什么
    return Promise.reject(error)
  },
)

// router.beforeEach((to, from, next) => {
//   const user_req = JSON.parse(localStorage.getItem('user-req'))
//   const authorities = user_req ? user_req.authorities : []

//   if (to.matched.some((record) => record.meta.requiresAuth)) {
//     const requiredRoles = to.meta.requiredRole
//     const hasRequiredRole = authorities.some((element) => requiredRoles.includes(element.authority))
//     if (!hasRequiredRole) {
//       return next('/dashboard/workbench')
//     }
//   }

//   next()
// })

router.beforeEach((to, from, next) => {
  const user_req = JSON.parse(localStorage.getItem('user-req'))
  const isAuthenticated = !!user_req
  const authorities = user_req ? user_req.authorities : []

  if (to.matched.some((record) => record.meta.requiresAuth)) {
    if (!isAuthenticated) {
      return next('/')
    }

    if (to.meta.requiredRole) {
      const requiredRoles = to.meta.requiredRole
      const hasRequiredRole = authorities.some((element) =>
        requiredRoles.includes(element.authority),
      )

      if (!hasRequiredRole) {
        return next('/dashboard/workbench')
      }
    }
  }

  next()
})

library.add(fas, far)

const app = createApp(App)
const pinia = createPinia()

app.use(router)
app.use(pinia)

app.component('font-awesome-icon', FontAwesomeIcon)

app.mount('#app')
