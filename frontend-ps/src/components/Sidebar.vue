<template>
<div>
  <b-nav vertical class="w-25" style="min-width: 200px">
    <p class="mt-4 ml-3">
      <b-icon-house-fill  style="cursor:pointer" class="mr-3" font-scale="1.5" @click="home"></b-icon-house-fill>
      {{name}} 님
      <b-icon-bell style="cursor:pointer" class="ml-3" font-scale="1.4" ></b-icon-bell>
      <b-icon-person-fill style="cursor:pointer" class="ml-3" font-scale="1.4" @click="profile"></b-icon-person-fill>
      </p>
    <b-nav-item disabled><hr></b-nav-item>
    <b-nav-item disabled>내가 pin한 프로젝트</b-nav-item>
    <b-nav-item> 
      <b-form-select style="width:200px" 
      v-model="pinProjectId"
      :options="this.myProjects"
      class="mb-1 "
      value-field="projectId"
      text-field="projectName"
      @change="changeSelect(pinProjectId)"
    ></b-form-select>

    </b-nav-item>
     <b-nav-item><hr></b-nav-item>
      <b-nav-item disabled v-show="this.pinProjectId" v-model="this.pinProjectId">
        <ul style="list-style-type: none ; padding: 0px">
            <b-nav-item><li><strong>{{ this.projectName }}</strong></li></b-nav-item>
            <b-nav-item><li>프로젝트 정보</li></b-nav-item>
            <b-nav-item><li>캘린더</li></b-nav-item>
            <b-nav-item><li>주차별 목표(to-do-list)</li></b-nav-item>
            <b-nav-item><li>토론 게시판</li></b-nav-item>
            <b-nav-item><li>자유 게시판</li></b-nav-item>
            <b-nav-item @click="management"><li>관리</li></b-nav-item>
        </ul>
        <hr>
      </b-nav-item>
    <b-nav-item>it 경진대회</b-nav-item>
    <b-nav-item @click="projectBoard">프로젝트 게시판</b-nav-item>
    <b-nav-item >커뮤니티 게시판</b-nav-item>
    <b-nav-item> <!-- 233px -->
      <hr>
      <b-button variant="dark" style="margin-left: 124px" @click="logout">Logout</b-button>
    </b-nav-item>
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
        projectName:null, // 현재 셀렉트 된 projectName
        pinProjectId:null, // 현재 셀렉트 된 projectId
        myProjects:[],
        allProjects:[],
      }
    },
   
    created() {
      if(this.$route.params.projectId!=null) { // 프로젝트를 선택한 상태일때
        this.pinProjectId=this.$route.params.projectId
        axios.get('/api/project/projectName/'+this.pinProjectId).then(response => { // 프로젝트 이름 가져오기
            let project = response.data
            this.projectName=project.projectName
          }).catch((erro)=> {
          console.error(erro);
        });
      } 
      axios.get('/api/user')
        .then(response => {
          this.name = response.data.name
        }).catch((erro)=> {
          console.error(erro);
        });
        axios.get('/api/pinList').then(response => {
            this.myProjects = response.data
          }).catch((erro)=> {
          console.error(erro);
        });


        // axios.get('/api/allProjects').then(response => {
        //     this.allProjects = response.data
        //   }).catch((erro)=> {
        //   console.error(erro);
        // });
          
    },
    watch: {
        
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

      logout() {
        axios.get('/api/user/logout')
        .then().catch((erro)=> {
          console.error(erro);
        });
        this.$router.push({
          path: '/'
        })
      },
      management(){
        this.$router.push({
          path: '/myprojectmanager'
        })
      },
      changeSelect(pinProjectId) {
        location.href='/project/'+pinProjectId+'/summary'
      }
    }
}
</script>

<style>

li { color: black }

</style>