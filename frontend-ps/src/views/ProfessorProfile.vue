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
                                <h4>{{data.name}} 교수님</h4>
                                <h6 style="color:#A4A4A4">{{data.userNum}}</h6>
                            </b-card-text>
                            <hr>
                            <b-card-text style="height:50px;" v-if="(data.intro==null)?false:true">
                                <span>{{data.intro}}</span>
                            </b-card-text>
                            <b-button variant="dark" size="sm" @click="editProfile">정보 수정</b-button>
                        </b-card>
                    </b-col>
                    <b-col style="min-width:350px ;" class="mt-0">
                        <b-card style="padding:10px 20px">
                            <b-media>
                                <h5 class="mt-0">Name</h5>
                                <p>{{data.name}}
                                </p>
                            </b-media>
                            <b-media>
                                <h5 class="mt-0">Email</h5>
                                <p>{{data.email}}
                                </p>
                            </b-media>
                             <b-media>
                                <h5 class="mt-0">Github</h5>
                                <a v-bind:href="gitUrl" target="_blank" v-if="data.github!=null">
                                    <p>{{gitUrl}}</p>
                                </a>
                                <p v-if="data.github==null" style="color:silver">
                                    등록된 정보가 없습니다.
                                </p>
                            </b-media>
                        </b-card>
                    </b-col>
                </b-row>
            </div>
        </div>
         <hr style="margin:30px 0;">
    </div>
</template>

<script>
import axios from 'axios';

export default {
  name: 'ProfessorProfile',
  components: {
  },
  data() {
      return {
          data: {},
          gitUrl: ''
      }
   },
   mounted() {
        axios.get('/api/user')
        .then(response => {
            this.data = response.data
            this.gitUrl='https://github.com/'+response.data.github
        })
   },
   methods:{
       editProfile() {
            this.$router.push({
                path: 'editProfessorProfile'
            })
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
        margin-top:10px; 
        padding:10px;
        border-bottom:1px solid #E6E6E6
    }

    .inviteList:last-child{
        border-bottom:none;
    }
</style>