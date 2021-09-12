import Vue from 'vue'
import VueRouter from 'vue-router'

import LoginRouter from './LoginRouter'
import RegisterRouter from './RegisterRouter'

Vue.use(VueRouter)

const routes = [
  LoginRouter,
  RegisterRouter
]

const router = new VueRouter({
  mode: 'history',
  base: process.env.BASE_URL,
  routes
})

export default router
