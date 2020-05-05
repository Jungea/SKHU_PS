<template>
  <b-container id="app" fluid>
      <b-row class="h-100">
        <b-col xl="2" class="mySidebar" v-show="this.array.includes(this.$route.name) == false"><Sidebar/></b-col>
        <b-col class="content">{{this.$route.name}}<router-view/></b-col>
      </b-row>
  </b-container>
</template>

<script>
import Sidebar from '@/components/Sidebar'
export default {
  name: 'App',
  data() {
    return {
      array: ["Login", 'UserSelect', 'studentSignUp', 'ProfessorAuth', 'ProfessorSignUp'],
    }
  },
  components: {
    Sidebar
  }
}
</script>

<style>
html, body, #app {
  height: 100%
}
.mySidebar {
  border-right: 1px solid lightgray;
}
</style>
