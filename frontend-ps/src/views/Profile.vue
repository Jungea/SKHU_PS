<template>
    <div class="profileContainer">
        <h3>마이페이지</h3>
        <hr style="margin:30px 0;">
        <div class="infoContainer">
            <h5>내 정보</h5>
            <div style="margin-top:30px; padding: 30px;">
                <b-row>
                    <b-col style="min-width:350px; max-width:400px; margin-bottom:10px" class="mt-0">
                        <b-card img-src="https://image.flaticon.com/icons/svg/2825/2825025.svg" 
                        img-alt="Image" img-top img-height="200px" style="padding:20px">
                            <b-card-text style="margin-top:20px">
                                <h4>{{userInfo.name}}</h4>
                                <h6 style="color:#A4A4A4">{{userInfo.number}}</h6>
                            </b-card-text>
                            <hr>
                            <b-card-text style="height:50px;" v-if="(userInfo.intro==null)?false:true">
                                <span>{{userInfo.intro}}</span>
                            </b-card-text>
                            <b-button variant="dark" size="sm" @click="editProfile">정보 수정</b-button>
                        </b-card>
                    </b-col>
                    <b-col style="min-width:350px;" class="mt-0">
                        <b-card style="padding:10px 20px">
                            <b-media>
                                <h5 class="mt-0">Department</h5>
                                <p>{{userInfo.dept}}
                                </p>
                            </b-media>
                            <b-media>
                                <h5 class="mt-0">Grade</h5>
                                <p>{{userInfo.grade}}학년
                                </p>
                            </b-media>
                            <b-media>
                                <h5 class="mt-0">Github</h5>
                                <a v-bind:href="gitUrl" target="_blank" v-if="userInfo.github!=null">
                                    <p>{{gitUrl}}</p>
                                </a>
                                <p v-if="userInfo.github==null" style="color:silver">
                                    등록된 정보가 없습니다.
                                </p>
                            </b-media>
                            <b-media>
                                <h5 class="mt-0">Major field</h5>
                                <h5><b-badge variant="secondary" v-for="(tag,index) in tagArray" :key="index" style="margin-right:5px">
                                    {{tag}}
                                </b-badge></h5>
                                <p v-if="userInfo.language==null" style="color:silver">
                                    등록된 정보가 없습니다.
                                </p>
                            </b-media>
                        </b-card>
                    </b-col>
                </b-row>
            </div>
        </div>
        <hr style="margin:30px 0;">
        <div class="infoContainer">
            <h5>초대/신청 이력</h5>
            <div style="padding:30px;">
                <b-card no-body header="초대 받은 이력">
                    <b-list-group flush>
                        <b-list-group-item class="inviteList" v-for="invit in inviteList" :key="invit.joinId">
                            <b-media>
                                <h5 style="display:inline;">{{invit.project.projectName}}</h5>
                                <span style="float:right">
                                    <b-button size="sm" @click="turnState(invit, 1)" class="mr-2" variant="success">수락</b-button>
                                    <b-button size="sm" @click="turnState(invit, 2)" variant="danger">거절</b-button>
                                </span>
                                <h6>From {{invit.project.user.name}}</h6>
                            </b-media>
                        </b-list-group-item>
                    </b-list-group>
                    <b-card-body v-if="inviteList.length==0">
                        프로젝트에 초대받은 이력이 없습니다.
                    </b-card-body>
                </b-card>

                <b-card no-body header="신청한 이력" class="mt-4">
                    <b-list-group flush>
                        <b-list-group-item class="inviteList" v-for="application in applicationList" :key="application.JoinId">
                            <span style="float:left">
                                <b>{{application.project.projectName}}</b><b-badge variant="warning" class="ml-2">{{state[application.state]}}</b-badge>
                            </span>
                            <span style="float:right">
                                <b-button @click="deleteWaiting(application)" size="sm" style="clear:both; margin-left:20px">신청 취소</b-button>
                            </span>
                        </b-list-group-item>
                    </b-list-group>
                    <b-card-body v-if="applicationList.length==0">
                        프로젝트에 신청한 이력이 없습니다.
                    </b-card-body>
                </b-card>
            </div>
        </div>
    </div>
</template>

<script>
import axios from 'axios';

export default {
  name: 'Profile',
  components: {
  },
  data() {
      return {
        userInfo: {},
        gitUrl:'',
        tagArray:[],
        myProjectPin:[],
        inviteList:[],
        applicationList:[],
        state: ["대기", "수락", "거절"],
      }
   },
   mounted() {
        axios.get('/api/user')
        .then(response => {
            let userInfo={
                'number':response.data.userNum,
                'name':response.data.name,
                'dept':response.data.detDepartment.detName,
                'grade':response.data.grade,
                'github':response.data.github,
                'intro':response.data.intro,
                'language':response.data.language
            }
            this.userInfo=userInfo;
            //깃 닉네임으로 링크 생성
            this.gitUrl='https://github.com/'+response.data.github
            if(this.userInfo.language)
                this.tagArray=this.userInfo.language.split(',')
            console.log(this.userInfo.intro)
        }).catch((erro)=> {
          console.error(erro);
        });

        axios.get('/api/user/inviteList')
            .then(response => {
                //this.inviteList=response.data;
                //초대 상태가 대기인 경우 보이게
                for(let i in response.data){
                    if(response.data[i].state==0){
                        this.inviteList.push(response.data[i])
                        console.log('상태')
                        console.log(this.inviteList[i].state)
                    }
                }
            })
            .catch((erro)=> {
                console.error(erro);
            });

        axios.get('/api/user/applicationList')
            .then(response => {console.log(response.data); this.applicationList=response.data})
            .catch((erro)=> {
                console.error(erro);
            });

   },
   methods:{
        editProfile() {
            this.$router.push({
                path: '/editProfile'
            })
            let userInfo=this.userInfo
            console.log("정보수정으로 이동")
            console.log(userInfo)
        },
        turnState(invite, state){
            axios.post('/api/turnjoinstate/'+invite.joinId+'/'+state)
            .then(() => location.reload())
            .catch((erro)=> {
                console.error(erro);
            });
        },
        //초대 취소
        deleteWaiting(application){
            if(confirm("신청을 취소합니까?")){
                axios.post('/api/deletejoin/'+application.joinId)
                .then(() => {
                    alert('취소되었습니다.');
                    location.reload();
                })
                .catch((erro)=>{
                    console.error(erro);
                })
                
                
            }         
            
        }
   }
}
</script>

<style scopped>
    .profileContainer {
        margin:100px;
    }

    .profileContainer h3{
        font-weight: bold;
    }

    .inviteList{
        margin:10px; 
        padding:10px;
        border-bottom:1px solid #E6E6E6
    }

    .inviteList:last-child{
        border-bottom:none;
    }
</style>