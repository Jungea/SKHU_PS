<template>
  <div>
    <b-nav vertical>
      <p class="mt-4 ml-3">
        <b-icon-house-fill  style="cursor:pointer" class="mr-1" font-scale="1.4" @click="home"></b-icon-house-fill>
        {{name}} 님
        <b-icon-bell @click="viewTimeline()" style="cursor:pointer" class="ml-2" font-scale="1.3" v-b-modal.modal-timeLine></b-icon-bell>
        <b-icon-person-fill style="cursor:pointer" class="ml-2" font-scale="1.4" @click="profile"></b-icon-person-fill>
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
              <b-nav-item disabled><strong>{{ this.projectName }}</strong></b-nav-item>
              <li><b-nav-item @click="projectInfoChange('summary')">프로젝트 정보</b-nav-item></li>
              <li><b-nav-item>캘린더</b-nav-item></li>
              <li><b-nav-item @click="projectInfoChange('weekly')">주차별 목표(to-do-list)</b-nav-item></li>
              <li v-if="pinProjectSubjectId > 0 && isJoined"><b-nav-item @click="viewNotice()">공지 게시판</b-nav-item></li>
              <li><b-nav-item>토론 게시판</b-nav-item></li>
              <li><b-nav-item>자유 게시판</b-nav-item></li>
              <li v-if="this.name == this.capName"><b-nav-item @click="projectInfoChange('manage')">관리</b-nav-item></li>
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
              <b-nav-item @click="subjectInfo()"><strong>{{ this.subjectName }}</strong></b-nav-item>
              <li><b-nav-item @click="viewTeam()">팀별 확인</b-nav-item></li>
              <li><b-nav-item @click="viewScore()">점수 모아보기</b-nav-item></li>
              <li><b-nav-item @click="viewNotice()">공지 게시판</b-nav-item></li>
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

      <b-modal id="modal-timeLine" size="lg" ok-only @hide="closeTimelineModal">
        <center>
          <h3 class="mt-3">TimeLine</h3>
          <hr style="border: 1px solid black ; width: 60% ; margin-bottom: 30px">
          <b-container>
              <p style="color:silver; margin: 300px auto;" v-if="data.length == 0 ? true : false">현재 알림이 없습니다.</p>
              <b-row @click="move(item.url)" :key="index" v-for="(item, index) in data" style="width: 60% ; min-height: 70px ; margin: 10px; cursor:pointer;">
                  <b-col style=" text-align: left ; padding: 0">
                    <div class="timeline">{{item.content}}</div>
                    <div style="text-align: right ; font-size: 12px ; margin-top: 10px ; margin-bottom: -13px">{{item.time}}</div>
                  </b-col>
                  <hr style="width: 100% ; border-color: silver">
                  <b-button variant="dark" style="height: 25px ; width: 100% ; font-size: 8px" v-if="index==arrIndex-1" disabled>여기까지 읽으셨습니다.</b-button>
                  <hr v-if="index==arrIndex-1" style="width: 100% ; border-color: silver">
              </b-row>
          </b-container> 
        </center>
      </b-modal>

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
        data: [],
        checkTime1: '',
        beforeList: [],
        arrIndex: '',
        pinProjectSubjectId: '',
        capName: '',
        userId: '',

        checkSubject: false,
        isJoined:false,
      }
    },
   
    mounted() {
      axios.get('/api/user')
        .then(response => {
          this.type=response.data.userType
          this.name = response.data.name
          this.userId = response.data.userId

          if(this.type==false)  { // 학생이라면
            // axios.get('/api/user/checkJoinMember/'+this.pinProjectId).then(response => { // 현재 유저가 현재 프로젝트에 참여한 상태인지 확인
            //     this.isJoined = response.data
            //   }).catch((erro)=> {
            //   console.error(erro);
            // });
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

                 axios.get('/api/user/checkJoinMember/'+this.pinProjectId).then(response => { // 현재 유저가 현재 프로젝트에 참여한 상태인지 확인
                  this.isJoined = response.data
                }).catch((erro)=> {
                  console.error(erro);
                });

                this.capName = project.user.name
                if(project.subject == null)
                  this.pinProjectSubjectId = 0
                else
                  this.pinProjectSubjectId = project.subject.subjectId
                if(project.subject != null) {
                  this.checkSubject = true;
                } else
                  this.checkSubject = false;
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
      record() {
        let checkTime = this.checkTime1
        this.beforeList = this.data.filter(function(item) {
          return item.time < checkTime
        })
        this.arrIndex = this.data.length - this.beforeList.length
      },
      projectBoard(evt) {
        evt.preventDefault()

        if(this.type == false)
          this.pinProjectId = null;
        else
          this.pinSubjectId = null;

        this.$router.push({
            path: '/projectBoard',
            query:{page:1}
          })
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
          path: '/project/'+ this.pinProjectId +'/'+infoName
        })
      },
      management(){
        this.$router.push({
          path: '/project/'+this.$route.params.projectId+'/manager'
        })
      },
      viewScore(){
        this.$router.push({
          path: '/Score/'+this.pinSubjectId+'/'
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
            name: 'professorSummary',
            params: {
              subjectId: this.pinSubjectId
            }
          })
        }
      },
      viewNotice() {
        if(!this.type) { // 학생일때
          this.$router.push({
            path: '/project/' + this.pinProjectId +'/noticeBoard',
            query: {page:1} 
          })
        }
        else { // 교수일때
          this.$router.push({
            path: '/subject/' + this.pinSubjectId +'/noticeBoard',
            query: {page:1} 
          })
        }
      },
      viewTeam() {
        this.$router.push({
            path: '/subject/' + this.pinSubjectId + '/Team'
        })
      },
      subjectInfo() {
         this.$router.push({
            path: '/professor/' + this.pinSubjectId + '/summary'
            })
      },
      viewTimeline() {
        //유저의 마지막 타임라인 확인 시간 가져오기
        axios.get('/api/user/timelineTime')
        .then(response => {
          let a = new Date(response.data);

          //유저의 타임라인 목록
          axios.get('/api/user/' + this.userId + '/timeline')
            .then(res => {
              this.data = res.data
              console.log(a)

              this.checkTime1 = this.leadingZeros(a.getFullYear(), 4) + '-' + this.leadingZeros(a.getMonth() + 1, 2) + '-' + this.leadingZeros(a.getDate(), 2)
                    + ' ' + this.leadingZeros(a.getHours(), 2) + ':' + this.leadingZeros(a.getMinutes(), 2);

              for(let i = 0 ; i < res.data.length ; i++) {
                let d = new Date(res.data[i].time);
                this.data[i].time = this.leadingZeros(d.getFullYear(), 4) + '-' + this.leadingZeros(d.getMonth() + 1, 2) + '-' + this.leadingZeros(d.getDate(), 2)
                    + ' ' + this.leadingZeros(d.getHours(), 2) + ':' + this.leadingZeros(d.getMinutes(), 2);
              }

              this.record()
            })
        })
        
      },
      leadingZeros(n, digits) {
            var zero = '';
            n = n.toString();
            if (n.length < digits) {
                for (var i = 0; i < digits - n.length; i++)
                    zero += '0';
            }
                return zero + n;
      },
      move(url) {
        alert(url)
        axios.put('/api/user/timelineTime').then().catch(erro => console.error(erro))
        location.href = url
      },
      closeTimelineModal() {
        axios.put('/api/user/timelineTime').then().catch(erro => console.error(erro))
      }
    }
}
</script>

<style scoped>

li { color: black }
.timeline:hover { text-decoration: underline !important}

</style>