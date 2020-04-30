<template>
  <div class="home h-100">
    <b-row class="h-100">
      <b-col class="myNav overflow-auto" cols="200px">
        <Sidebar/>
      </b-col>
      <b-col>
        <div class="profileContainer">
            <h3>마이페이지</h3>
            <hr style="margin:30px 0;min-width:900px;">
            <div class="infoContainer">
                <h5>내 정보</h5>
                <div style="margin-top:30px; min-width:900px; padding: 30px; padding-right:120px;">
                    <b-row>
                        <b-col cols="4">
                            <b-card class="profileCard" img-src="https://image.flaticon.com/icons/svg/2825/2825025.svg" 
                            img-alt="Image" img-top img-height="200px">
                                <b-card-text style="margin-top:20px">
                                    <h4>{{items.name}}</h4>
                                </b-card-text>
                                <b-card-text class="small" style="height:50px;" v-if="(items.intro==null)?false:true">
                                    <hr><span>{{items.intro}}</span>
                                </b-card-text>
                                <b-button variant="dark" size="sm" @click="editProfile">정보 수정</b-button>
                            </b-card>
                        </b-col>
                        <b-col class="profileTable" cols="8">
                            <table class="table">
                                <tr>
                                    <th>{{fields[0]}}</th>
                                    <td>{{items.major}}</td>
                                </tr>
                                <tr>
                                    <th>{{fields[1]}}</th>
                                    <td>{{items.grade}}</td>
                                </tr>
                                <tr>
                                    <th>{{fields[2]}}</th>
                                    <td>
                                        <a v-bind:href="gitUrl" target="_blank" v-if="items.github!=null">{{gitUrl}}</a>
                                    </td>
                                </tr>
                                <tr>
                                    <th>{{fields[3]}}</th>
                                    <td><b-badge variant="secondary" v-for="(tag,index) in tagArray" :key="index" style="margin-right:5px">{{tag}}</b-badge></td>
                                </tr>
                            </table>
                        </b-col>
                    </b-row>
                </div>
            </div>
            <hr style="margin:30px 0; min-width:900px;">
            <div class="infoContainer">
                <h5>초대받은 이력</h5>
                <ul>
                    <li v-for="invit in inviteList" :key="invit.joinId">
                        {{invit.project.projectName}}(팀장: {{invit.project.user.name}}) - 수락/거절
                    </li>
               </ul>
                <br/>
            </div>
        </div>
      </b-col>
    </b-row>
  </div>
</template>

<script>
import axios from 'axios';
import Sidebar from '@/components/Sidebar.vue'

export default {
  name: 'Profile',
  components: {
    Sidebar
  },
  data() {
      return {
        fields:['Major', 'Grade', 'Github', 'Language'],
        //items: [],
        items: {},
        gitUrl:'',
        tagArray:[],
        myProjectPin:[],
        inviteList:[]
      }
   },
   mounted() {
        axios.get('/api/user')
        .then(response => {
            let info={
                'name':response.data.name,
                'major':response.data.detDepartment.detName,
                'grade':response.data.grade,
                'github':response.data.github,
                'intro':response.data.intro,
                'language':response.data.language
            }
            //this.items.push(info);
            this.items=info;
            //깃 닉네임으로 링크 생성
            this.gitUrl='https://github.com/'+response.data.github
            this.tagArray=this.items.language.split(',')
            console.log(this.items.Intro)
        }).catch((erro)=> {
          console.error(erro);
        });

        axios.get('/api/user/inviteList')
            .then(response => this.inviteList=response.data)
            .catch((erro)=> {
                console.error(erro);
            });

   },
   methods:{
        editProfile() {
            this.$router.push({
                path: '/editProfile'
            })
            let items=this.items
            console.log("정보수정으로 이동")
            console.log(items)
        },
   }
}
</script>

<style>
    .profileContainer {
        margin:100px;
    }

    .profileContainer h3{
        font-weight: bold;
    }

    .infoContainer h5{
        font-weight: bold;
    }
    .profileTable{
        max-width: 1000px;
    }
    .profileCard{
        max-width: 300px;
        min-width: 200px;
        padding: 20px;
    }
    .profileTable tr>td >a{
        text-decoration: none;
        color: black;
    }
</style>