<template>
  <div class="home h-100">
    <b-row class="h-100">
      <b-col class="myNav overflow-auto" cols="200px">
        <Sidebar/>
      </b-col>
      <b-col>
         <b-button @click="click1">Button</b-button>
      </b-col>
    </b-row>
  </div>
</template>

<script>
//import Sidebar from '@/components/Sidebar.vue'
export default {
  name: 'PojectBoard',

  methods: {
    click1() {
        alert('pin2:'+this.$store.state.selectedPinProjectId)
    }
  }
}
</script>