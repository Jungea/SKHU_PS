import Vue from 'vue'
import VueRouter from 'vue-router'
import Login from '../views/Login.vue';
import StudentSignUp from '../views/StudentSignUp.vue';
import FindPass from '../views/FindPass.vue';
import Home from '../views/Home.vue';
import FindPassAlert from '../views/FindPassAlert.vue'; 
import Profile from '../views/Profile.vue';
import EditProfile from '../views/EditProfile.vue'; 
import ProjectBoard from '../views/ProjectBoard.vue'; 
import MyProjectManager from '../views/MyProjectManager.vue';
import ProfessorAuth from '../views/ProfessorAuth.vue';
import UserSelect from '../views/UserSelect.vue';
import ProfessorSignUp from '../views/ProfessorSignUp.vue';
import SignUpAlert from '../views/SignUpAlert.vue';
import AuthAlert from '../views/AuthAlert.vue';
import ChangePw from '../views/ChangePw.vue';

import MyProjectEdit from '../views/MyProjectEdit';
import Summary from '../views/summary.vue';


Vue.use(VueRouter)

  const routes = [
  {
    path: '/',
    name: 'Login',
    component: Login
  },
  {
    path: '/professorSignUp',
    name: 'ProfessorSignUp',
    component: ProfessorSignUp
  },
  {
    path: '/studentSignUp',
    name: 'studentSignUp',
    component: StudentSignUp
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
  },
  {
    path: '/professorAuth',
    name: 'ProfessorAuth',
    component: ProfessorAuth
  },
  {
    path: '/userSelect',
    name: 'UserSelect',
    component: UserSelect
  },
  {
    path: '/signUpAlert',
    name: 'SignUpAlert',
    component: SignUpAlert
  },  
  {
    path: '/authAlert/:authKey',
    name: 'AuthAlert',
    component: AuthAlert
  },
  {
    path: '/changePw/:authKey',
    name: 'ChangePw',
    component: ChangePw
  },
  {
    path: '/project/:projectId/summary',
    name: 'Summary',
    component: Summary
  },
  {
    path: '/project/:projectId/edit',
    name: 'ProjectEdit',
    component: MyProjectEdit
},
]

const router = new VueRouter({
  mode: 'history',
  base: process.env.BASE_URL,
  routes
})

export default router
