<template>
  <b-container id="app" fluid>
    <b-row class="h-100">
      <b-col class="myNav h-100 overflow-auto" cols="200px">
        <Sidebar/>
      </b-col>
      <b-col>
        <router-view/>
      </b-col>
    </b-row>
  </b-container>
</template>

<script>
import Sidebar from '@/components/Sidebar.vue'
export default {
  name: 'App',
  components: {
    Sidebar,
  }
}
</script>

<style>
html, body, #app {
  height: 100%
}

.myNav{
    border-right: 1px solid lightgray;
}
</style>
