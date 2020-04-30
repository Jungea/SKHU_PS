<template>
  <div class="home h-100">
    <b-row class="h-100">
      <b-col class="myNav overflow-auto" cols="200px">
        <Sidebar/>
      </b-col>
      <b-col>
        <CompoProjectSummary/>
      </b-col>
    </b-row>
  </div>
</template>

<script>
import Sidebar from '@/components/Sidebar.vue'
import CompoProjectSummary from '@/components/CompoProjectSummary.vue'

export default {
  name: 'Summary',
  components: {
    Sidebar, CompoProjectSummary
  }
}
</script>