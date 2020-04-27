<template>
  <div class="home h-100">
    <b-row class="h-100">
      <b-col class="myNav overflow-auto" cols="200px">
        <Sidebar/>
      </b-col>
      <b-col>
        <div class="profileContainer">
            <h3>마이페이지</h3>
            <hr style="margin:30px 0;">
            <div class="infoContainer">
                <h5>내 정보</h5>
                <div style="margin-top:30px; min-width:900px; padding: 30px; padding-right:120px">
                    <b-row>
                        <b-col cols="4">
                            <b-card class="profileCard" img-src="https://image.flaticon.com/icons/svg/2825/2825025.svg" 
                            img-alt="Image" img-top img-height="200px">
                                <b-card-text style="margin-top:20px">
                                    <h4>{{items.Name}}</h4>
                                </b-card-text>
                                <b-card-text class="small" style="height:50px;" v-if="(items.Intro==null)?false:true">
                                    <hr><span>{{items.Intro}}</span>
                                </b-card-text>
                                <b-button variant="dark" size="sm" @click="editProfile">정보 수정</b-button>
                            </b-card>
                        </b-col>
                        <b-col class="profileTable" cols="8">
                            <table class="table">
                                <tr>
                                    <th>{{fields[0]}}</th>
                                    <td>{{items.Major}}</td>
                                </tr>
                                <tr>
                                    <th>{{fields[1]}}</th>
                                    <td>{{items.Grade}}</td>
                                </tr>
                                <tr>
                                    <th>{{fields[2]}}</th>
                                    <td><a v-bind:href="gitUrl" target="_blank">{{gitUrl}}</a></td>
                                </tr>
                                <tr>
                                    <th>{{fields[3]}}</th>
                                    <td>{{items.Language}}</td>
                                </tr>
                            </table>
                        </b-col>
                    </b-row>
                </div>
            </div>
            <hr style="margin:30px 0;">
            <div class="infoContainer">
                <h5>다음내용</h5>
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
        gitUrl:''
      }
   },
   mounted() {
        axios.get('/api/user')
        .then(response => {
            let info={
                'Name':response.data.name,
                'Major':response.data.detDepartment.detName,
                'Grade':response.data.grade,
                'Github':response.data.github,
                'Intro':response.data.intro,
                'Language':response.data.language
            }
            //this.items.push(info);
            this.items=info;
            //깃 닉네임으로 링크 생성
            this.gitUrl='https://github.com/'+response.data.github
            console.log(this.items.Intro)
        }).catch((erro)=> {
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