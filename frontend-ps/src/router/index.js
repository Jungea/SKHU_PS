import Vue from 'vue'
import VueRouter from 'vue-router'
import Login from '../views/Login.vue';
import SignUp from '../views/SignUp.vue';
import FindPass from '../views/FindPass.vue';
import Home from '../views/Home.vue';
import FindPassAlert from '../views/FindPassAlert.vue'; 
import Profile from '../views/Profile.vue';
import EditProfile from '../views/EditProfile.vue'; 
import ProjectBoard from '../views/ProjectBoard.vue'; 
import MyProjectManager from '../views/MyProjectManager.vue';

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
    path: '/findPassAlert',
    name: 'FindPassAlert',
    component: FindPassAlert
  },
  {
    path: '/profile',
    name: 'Profile',
    component: Profile
  },
  {
    path: '/editProfile',
    name: 'EditProfile',
    component: EditProfile
  },
  {
    path: '/projectBoard',
    name: 'ProjectBoard',
    component: ProjectBoard
  },
  {
    path: '/myProjectManager',
    name: 'MyProjectManager',
    component: MyProjectManager
  }
]

const router = new VueRouter({
  mode: 'history',
  base: process.env.BASE_URL,
  routes
})

export default router
