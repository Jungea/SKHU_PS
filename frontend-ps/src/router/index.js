import Vue from 'vue'
import VueRouter from 'vue-router'
import Login from '../views/Login.vue';
import SignUp from '../views/SignUp.vue';
import FindPass from '../views/FindPass.vue';
import Home from '../views/Home.vue';

Vue.use(VueRouter)

  const routes = [
  {
    path: '/',
    name: 'Login',
    component: Login
  },
  {
    path: '/signUp',
    name: 'SignUp',
    component: SignUp
  },
  {
    path: '/findPass',
    name: 'FindPass',
    component: FindPass
  },
  {
    path: '/home',
    name: 'Home',
    component: Home
  },

  {
    path: '/login',
    name: 'form1',
    // route level code-splitting
    // this generates a separate chunk (about.[hash].js) for this route
    // which is lazy-loaded when the route is visited.
    component: () => import(/* webpackChunkName: "about" */ '../views/login.vue')
  }
]

const router = new VueRouter({
  mode: 'history',
  base: process.env.BASE_URL,
  routes
})

export default router
