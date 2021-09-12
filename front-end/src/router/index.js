import Vue from 'vue'
import VueRouter from 'vue-router'

import LoginRouter from './LoginRouter'

Vue.use(VueRouter)

const routes = [
  LoginRouter
]

const router = new VueRouter({
  mode: 'history',
  base: process.env.BASE_URL,
  routes
})

export default router
