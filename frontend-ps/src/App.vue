<template>
  <b-container id="app" fluid>
      <b-row no-gutters class="h-100">
        <b-col xl="2" class="mySidebar" v-if="this.array.includes(this.$route.name) == false"><Sidebar/></b-col>
        <b-col class="content"><router-view/></b-col>
      </b-row>
  </b-container>
</template>

<script>
import Sidebar from '@/components/Sidebar'
export default {
  name: 'App',
  data() {
    return {
      array: ["Login", 'UserSelect', 'studentSignUp', 'ProfessorAuth', 'ProfessorSignUp' ,'AuthAlert','FindPass','FindPassAlert','ChangePw', 'Test', 'Test2'],
    }
  },
  components: {
    Sidebar
  }
}
</script>

<style>
html, body, #app {
  padding:0;
  height: 100%
}
.mySidebar {
  border:1px solid silver;
}
</style>
