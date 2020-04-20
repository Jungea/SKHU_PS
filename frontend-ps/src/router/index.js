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
  }
]

const router = new VueRouter({
  mode: 'history',
  base: process.env.BASE_URL,
  routes
})

export default router
