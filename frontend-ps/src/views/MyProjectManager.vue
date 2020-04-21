<template>
  <div class="home h-100">
    <b-row class="h-100">
      <b-col class="myNav overflow-auto" cols="200px">
        <Sidebar/>
      </b-col>
      <b-col>
        <CompoMyProjectManager/>
      </b-col>
    </b-row>
  </div>
</template>

<script>
import Sidebar from '@/components/Sidebar.vue'
import CompoMyProjectManager from '@/components/CompoMyProjectManager.vue'

export default {
  name: 'MyProjectManager',
  components: {
    Sidebar, CompoMyProjectManager
  }
}
</script>
