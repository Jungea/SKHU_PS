<template>
<div>
  <b-nav vertical class="w-25" style="min-width: 200px">
    <p class="mt-4 ml-3">
      <b-icon-house-fill  style="cursor:pointer" class="mr-3" font-scale="1.5" @click="home"></b-icon-house-fill>
      {{name}} 님
      <b-icon-bell style="cursor:pointer" class="ml-3" font-scale="1.4" ></b-icon-bell>
      <b-icon-gear style="cursor:pointer" class="ml-3" font-scale="1.4" @click="profile"></b-icon-gear>
      </p>
    <b-nav-item disabled><hr></b-nav-item>
    <b-nav-item disabled>내가 pin한 프로젝트</b-nav-item>
    <b-nav-item> 
      <b-form-select style="width:200px" 
      v-model="pinProjectId"
      :options="this.$store.state.myProjectPin"
      class="mb-1 "
      value-field="projectId"
      text-field="projectName"
    ></b-form-select>

    </b-nav-item>
     <b-nav-item disabled><hr></b-nav-item>
     <b-nav-item disabled v-show="this.pinProjectId" v-model="this.pinProjectId">
       <strong>{{ this.projectName }}</strong>
       <div>프로젝트 정보</div>
       <div>캘린더</div>
       <div>주차별 목표(to-do-list)</div>
       <div>토론 게시판</div>
       <div>자유 게시판</div>
       <div>관리</div>
       <hr>
    </b-nav-item>
    <b-nav-item>it 경진대회</b-nav-item>
    <b-nav-item @click="projectBoard">프로젝트 게시판</b-nav-item>
    <b-nav-item >커뮤니티 게시판</b-nav-item>
    </b-nav>
</div>
</template>
<script>
import axios from 'axios';
export default {
    name: "Sidebar",
    data() {
      return {
        name: '',
        projectName:null,
        pinProjectId:null,
      }
    },
    mounted() {
      this.pinProjectId=this.$store.state.selectedPinProjectId
      alert('pin:'+this.pinProjectId)
      axios.get('/api/user')
        .then(response => {
          this.name = response.data.name
        }).catch((erro)=> {
          console.error(erro);
        });
        
    },
    watch: {
        pinProjectId() { // this.selected변수가 바뀔때마다 실행됨
          for(let i=0;i<this.$store.state.myProjectPin.length;i++) {
            if(this.$store.state.myProjectPin[i].projectId==this.pinProjectId) {
              this.projectName=this.$store.state.myProjectPin[i].projectName
              
            }
          }
          this.$store.commit('changeSelectedPinProjectId',this.pinProjectId)
          alert('pin2:'+this.$store.state.selectedPinProjectId)
        },
        // myProjectList() { // 현재 셀렉션에서 셀렉트 된 프로젝트가 pin에서 false로 될 때
        //   let count=0
        //   for(let i=0;i<this.myProjectList.length;i++) {
        //     if(this.myProjectList[i].projectId!=this.pinProjectId) {
        //       count++
        //     }
        //   }
        //   if(count==this.myProjectList.length) {
        //     this.pinProjectId=null
        //   }
        // }
    },
    methods: {
      projectBoard(evt) {
        evt.preventDefault()
        location.href="/projectBoard"
      },
      home(evt) {
        evt.preventDefault()
        location.href="/home"
      },
      profile(evt){
        evt.preventDefault()
        location.href="/profile"
      },
    }
}
</script>