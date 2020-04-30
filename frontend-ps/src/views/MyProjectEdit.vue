<template>
  <div class="home h-100">
    <b-row class="h-100">
      <b-col class="myNav overflow-auto" cols="200px">
        <Sidebar/>
      </b-col>
      <b-col>
        <CompoProjectEdit/>
      </b-col>
    </b-row>
  </div>
</template>

<script>
import Sidebar from '@/components/Sidebar.vue'
import CompoProjectEdit from '@/components/CompoProjectEdit.vue'

export default {
  name: 'MyProjectEdit',
  components: {
    Sidebar, CompoProjectEdit
  }
}
</script>
