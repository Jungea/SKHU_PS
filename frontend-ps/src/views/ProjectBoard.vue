<template>
  <div class="home h-100">
    <b-row class="h-100">
      <b-col class="myNav overflow-auto" cols="200px">
        <Sidebar/>
      </b-col>
      <b-col>
        <CompoProjectBoard/>
      </b-col>
    </b-row>
  </div>
</template>

<script>
import Sidebar from '@/components/Sidebar.vue'
import CompoProjectBoard from '@/components/CompoProjectBoard.vue'
export default {
  name: 'PojectBoard',
  components: {
    Sidebar, CompoProjectBoard
  }
}
</script>