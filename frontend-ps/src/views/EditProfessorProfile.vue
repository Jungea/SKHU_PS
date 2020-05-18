<template>
  <div>
    <div class="profileContainer">
      <h3>정보 수정</h3>
      <hr style="margin:30px 0;">
      <div>
        <div style="margin:auto; max-width:700px; padding: 30px;">
            <b-form @submit="onSubmit" v-if="show" class="editForm">
              <!--intro-->
              <!-- <b-form-group style="text-align:left"
                id="input-group-1"
                label="한 줄 소개"
                label-for="intro"
              >
                <b-form-input 
                id="intro" 
                v-model="input.intro" 
                type="text"
                placeholder="한 줄 소개 없음"
                ></b-form-input>
              </b-form-group> -->

              
              <!--github-->
              <b-form-group style="text-align:left"
                id="input-group-4"
                label="깃허브 닉네임"
                label-for="github"
              >
                <b-form-input 
                id="github" 
                v-model="input.github" 
                type="text"
                ></b-form-input>
                <b-form-text id="input-live-help">깃허브 계정이 있다면 입력하세요.</b-form-text>
              </b-form-group>


              <div style="text-align: right; margin-top:15px">
                <b-button style="margin-right:10px" type="submit">수정</b-button>
                <b-button variant="danger" @click="cancle">취소</b-button>
              </div>
            </b-form>

        </div>
      </div>
    </div>
  </div>
</template>

<script>
import axios from 'axios';
//import Sidebar from '@/components/Sidebar.vue'

export default {
  name: 'EditProfessorProfile',
  data() {
      return {
        show: true,
        input: {},
      }
   },  
  mounted() {
        axios.get('/api/user')
        .then(response => {
            let info={
                'github':response.data.github,
               //  'intro':response.data.intro,
            }
            this.input=info;
        }).catch((erro)=> {
          console.error(erro);
        });

   },
   methods:{
     onSubmit(evt) {
          evt.preventDefault()
          axios.post('/api/user/professorProfile', {
            github: this.input.github
          }).then()
          .catch((erro)=> {
            console.error(erro);
          });

          this.$router.push({
            path: '/professorProfile'
          })
        },
        cancle() {
            this.$router.push({
            path: '/professorProfile'
          })
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
    .editForm label{
      font-weight: bold;
    }
    .editForm input::placeholder{
      color:silver;
    }
</style>
