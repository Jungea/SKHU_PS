<template>
  <div>
    <b-nav vertical>
      <p class="mt-4 ml-1">
        <b-icon-house-fill  style="cursor:pointer" class="mr-1" font-scale="1.4" @click="home"></b-icon-house-fill>
        {{name}} 님
        <b-icon-bell style="cursor:pointer" class="ml-2" font-scale="1.3" ></b-icon-bell>
        <b-icon-person-fill style="cursor:pointer" class="ml-2" font-scale="1.3" @click="profile"></b-icon-person-fill>
      </p>
      <b-nav-item disabled><hr></b-nav-item>

      <div v-if="!this.type"> <!--학생일때-->
          <b-nav-item disabled>내가 pin한 프로젝트</b-nav-item>
          <b-nav-item> 
            <b-form-select 
            v-model="pinProjectId"
            :options="this.myProjects"
            class="mb-1"
            value-field="projectId"
            text-field="projectName"
            @change="changeSelect()"></b-form-select>
          </b-nav-item>
          <b-nav-item><hr></b-nav-item>

          <div disabled v-show="this.pinProjectId">
            <ul style="list-style-type: none ; padding: 0px">
              <b-nav-item><strong>{{ this.projectName }}</strong></b-nav-item>
              <li><b-nav-item @click="projectInfoChange('summary')">프로젝트 정보</b-nav-item></li>
              <li><b-nav-item>캘린더</b-nav-item></li>
              <li><b-nav-item @click="projectInfoChange('WG')">주차별 목표(to-do-list)</b-nav-item></li>
              <li><b-nav-item>토론 게시판</b-nav-item></li>
              <li><b-nav-item>자유 게시판</b-nav-item></li>
              <li><b-nav-item @click="projectInfoChange('manage')">관리</b-nav-item></li>
            </ul>
            <hr>
          </div>
      </div>
      <div v-else> <!--교수일때-->
          <b-nav-item disabled>내가 pin한 과목</b-nav-item>
          <b-nav-item> 
            <b-form-select 
            v-model="pinSubjectId"
            :options="this.mysubjects"
            class="mb-1"
            value-field="subjectId"
            text-field="title"
            @change="changeSelect()"></b-form-select>
          </b-nav-item>
          <b-nav-item><hr></b-nav-item>

          <div disabled v-show="this.pinSubjectId">
            <ul style="list-style-type: none ; padding: 0px">
              <b-nav-item><strong>{{ this.subjectName }}</strong></b-nav-item>
              
            </ul>
            <hr>
          </div>
      </div>
      <b-nav-item>it 경진대회</b-nav-item>
      <b-nav-item @click="projectBoard">프로젝트 게시판</b-nav-item>
      <b-nav-item >커뮤니티 게시판</b-nav-item>
      <b-nav-item> <!-- 233px -->
        <hr>
        <b-button variant="dark" @click="logout">Logout</b-button>
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
        type:null, // 현재 유저가 학생인지 교수인지 
        mysubjects:[],
        pinSubjectId:null,
        subjectName:null,
      }
    },
   
    mounted() {
      axios.get('/api/user')
        .then(response => {
          this.type=response.data.userType
          this.name = response.data.name
          
          if(this.type==false)  { // 학생이라면
              axios.get('/api/pinList').then(response => {
                this.myProjects = response.data
              }).catch((erro)=> {
              console.error(erro);
            });
          } else {
            axios.get('/api/subjects') 
            .then(response => {
                this.data = response.data
                for(let i=0;i<this.data.length;i++) {
                  if(this.data[i].pin==true) {
                    this.mysubjects.push(this.data[i])
                  }
                }
            });
        }
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
      '$route' () {
        if(this.type==false) { // 학생일때
          if(this.$route.params.projectId != undefined) { // 프로젝트를 선택한 상태일때
            this.pinProjectId = this.$route.params.projectId
            axios.get('/api/project/projectName/'+this.pinProjectId).then(response => { // 프로젝트 이름 가져오기
                let project = response.data
                this.projectName=project.projectName
              }).catch((erro) => {
              console.error(erro);
            });
          } 
        } else { // 교수일때 
          if(this.$route.params.subjectId != undefined) { 
            this.pinSubjectId = this.$route.params.subjectId
            axios.get('/api/subject/subjectName/'+this.pinSubjectId).then(response => { // 과목 이름 가져오기
                this.subjectName=response.data.title
              }).catch((erro) => {
              console.error(erro);
            });
          }
        }
      }   
    },
    methods: {
      projectBoard(evt) {
        evt.preventDefault()
        location.href="/projectBoard"
      },
      home(evt) {
        evt.preventDefault()
        // location.href="/home"
        this.pinProjectId = null;
        if(this.type==false) { // 학생이라면
          this.$router.push({
            path: '/home'
          })
        } else { // 교수라면
          this.$router.push({
            path: '/professorHome'
          })
        }
      },
      profile(evt){
        evt.preventDefault()
        if(this.type==false) { // 학생이라면
          this.$router.push({
              path: '/profile'
          })
        } else {
          this.$router.push({
              path: '/professorProfile'
          })
        }
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
      projectInfoChange(infoName) {
        this.$router.push({
          path: '/project/'+this.$route.params.projectId+'/'+infoName
        })
      },
      management(){
        this.$router.push({
          path: '/project/'+this.$route.params.projectId+'/manager'
        })
      },
      changeSelect() {
        if(this.type==false) { // 학생일때
          this.$router.push({
            name: 'Summary',
            params: {
              projectId: this.pinProjectId
            }
          })
        } else { // 교수일때
          this.$router.push({ // 교수 과목에 맞는 summary 라우터로 바꿔야함!!
            name: 'Summary',
            params: {
              projectId: this.pinSubjectId
            }
          })
        }
      }
    }
}
</script>

<style>

li { color: black }

</style>