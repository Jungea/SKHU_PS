<template>
  <div class="sign-up">    
    <div class="inputbox" style="margin:100px auto; width:500px;">
      <b-jumbotron style="background:none; text-align:left">
        <center>
          <h2 style="margin-left: 60px">Professor SignUp</h2>
          <hr style="border:2px solid black; width: 500px; margin-bottom: 50px;">
          <b-form @submit="signUp">
            <table>
              <tr>
                <b-form-group id="input-group-1" label="교번" label-for="input-1">
                  <b-form-input id="input-1" v-model="form.id" type="text" required placeholder="Enter number"></b-form-input>
                  <b-form-invalid-feedback :state="numValidation" v-if="form.id.length>0?true:false">
                    이미 가입된 정보입니다.
                  </b-form-invalid-feedback>
                  <b-form-valid-feedback :state="numValidation" v-if="form.id.length>0?true:false">                   
                  </b-form-valid-feedback>
                </b-form-group>
              </tr>
              <tr>
                <b-form-group id="input-group-2" label="비밀번호" label-for="input-2">
                  <b-form-input id="input-2" type="password" v-model="form.pass1" required placeholder="Enter password"></b-form-input>
                  <b-form-invalid-feedback :state="passValidation2" v-if="form.pass1.length>0?true:false">
                    비밀번호는 8자 이상 12자 이하입니다.
                  </b-form-invalid-feedback>
                  <b-form-valid-feedback :state="passValidation2" v-if="form.pass1.length>0?true:false">
                    사용가능한 비밀번호입니다.
                  </b-form-valid-feedback>
                </b-form-group>
              </tr>
              <tr>
                <b-form-group id="input-group-3" label="비밀번호 확인" label-for="input-3">
                  <b-form-input id="input-3" type="password" v-model="form.pass2" required placeholder="Check password"></b-form-input>
                  <b-form-invalid-feedback :state="passValidation" v-if="form.pass2.length>0?true:false">
                    입력한 비밀번호와 일치하지 않습니다.
                  </b-form-invalid-feedback>
                  <b-form-valid-feedback :state="passValidation" v-if="form.pass2.length>0?true:false">
                    비밀번호가 일치합니다.
                  </b-form-valid-feedback>
                </b-form-group>
              </tr>
              <tr>
                <b-form-group id="input-group-4" label="이름" lebel-for="input-4">
                  <b-form-input id="input-4" type="text" v-model="form.name" required placeholder="Enter name"></b-form-input>
                </b-form-group>
              </tr>
              <tr>
                <b-form-group id="input-group-5" label="이메일" label-for="input-5">
                  <b-form-input id="input-5" type="email" v-model="form.email" required placeholder="Enter Email"></b-form-input>
                  <b-form-text id="input-live-help">입력한 이메일로 인증 링크가 전송됩니다. 정확하게 입력해주세요.</b-form-text>
                  <b-form-invalid-feedback :state="emailValidation" v-if="form.email.length>0?true:false">
                    이미 가입된 이메일입니다.
                  </b-form-invalid-feedback>
                  <b-form-valid-feedback :state="emailValidation" v-if="form.email.length>0?true:false">                   
                  </b-form-valid-feedback>
                </b-form-group>
              </tr>
              <tr>
                <b-button style="width:100%;" type="submit">가입</b-button>
              </tr>
            </table>
          </b-form>
        </center>
      </b-jumbotron>
    </div>
  </div>

<!--
    <b-card class="mt-3" header="Form Data Result">
      <pre class="m-0">{{ form }}</pre>
    </b-card>
-->
</template>
 
<script>
import axios from 'axios';
  export default {
    name: 'ProfessorSignUp',
    
    data() {
    return {
        form: {
          id: '',
          pass1: '',
          pass2: '',
          email: '',
          name: ''
        },
        show: true,
        userNums:[],
        userEmails:[],
      }
    },
    methods: {
      signUp(evt) {
        evt.preventDefault();
        axios.post('/api/professorSignUp', 
        {
          userNum: this.form.id,
          email:this.form.email,
          name:this.form.name,
          password:this.form.pass1,
        }).then(response => { 
          this.user = response.data;
          if(this.user=='userNum 중복입니다.') {
            alert('userNum 중복입니다.');
          } else if(this.user=='email 중복입니다.') {
            alert('email 중복입니다.');
          } else if(this.user=='userNum과 email 둘 다 중복입니다.') {
            alert('userNum과 email 둘 다 중복입니다.');
          } else {
            this.$router.push({
            path: '/signUpAlert'
            }) 
          }
        });
      },
      
    },
    computed:{
      passValidation(){
        return this.form.pass1==this.form.pass2
      },
      passValidation2(){
        return this.form.pass1.length>=8&&this.form.pass1.length<=12
      },
      numValidation(){
        return this.userNums.indexOf(parseInt(this.form.id))==-1
      },
      emailValidation(){
        return this.userEmails.indexOf(this.form.email)==-1
      }

    },
    mounted(){
      
      axios.get('/api/alluser')
        .then(response => {
            for (var i in response.data){
              this.userNums.push(response.data[i].userNum)
              this.userEmails.push(response.data[i].email)
            }
            console.log(this.userNums)
            console.log(this.userEmails)
        }).catch((erro)=> {
          console.error(erro);
        })

    },

  }
</script>
 
<style scoped>
  .sign-up { margin-top: 50px ;}
  .inputbox { padding: 0px auto ;}
  input { width: 500px; }
  button { margin-top: 10px; width: 80px; cursor: pointer; } 
</style>