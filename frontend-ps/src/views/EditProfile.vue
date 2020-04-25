<template>
  <div class="home h-100">
    <b-row class="h-100">
      <b-col class="myNav overflow-auto" cols="200px">
        <Sidebar/>
      </b-col>
      <b-col>
        <div class="profileContainer">
            <h3>정보 수정</h3>
            <hr style="margin:30px 0;">
        </div>
      </b-col>
    </b-row>
  </div>
</template>

<script>
import Sidebar from '@/components/Sidebar.vue'

export default {
  name: 'Home',
  components: {
    Sidebar
  }
}
</script>
<style>
    .profileContainer {
        margin:100px;
    }
</style>
