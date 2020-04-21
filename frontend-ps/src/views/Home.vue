<template>
  <div class="home h-100">
    <b-row class="h-100">
      <b-col class="myNav overflow-auto" cols="200px">
        <Sidebar/>
      </b-col>
      <b-col>
        <MyProjectList/>
      </b-col>
    </b-row>
  </div>
</template>

<script>
import Sidebar from '@/components/Sidebar.vue'
import MyProjectList from '@/components/MyProjectList.vue'

export default {
  name: 'Home',
  components: {
    Sidebar, MyProjectList
  }
}
</script>
