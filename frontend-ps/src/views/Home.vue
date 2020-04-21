<template>
  <div class="home h-100">
    <b-row class="h-100">
      <b-col class="myNav overflow-auto" cols="200px">
        <Sidebar/>
      </b-col>
      <b-col>
        <CompoMyProjectList/>
      </b-col>
    </b-row>
  </div>
</template>

<script>
import Sidebar from '@/components/Sidebar.vue'
import CompoMyProjectList from '@/components/CompoMyProjectList.vue'
export default {
  name: 'Home',
  components: {
    Sidebar, CompoMyProjectList
  }
}
</script>