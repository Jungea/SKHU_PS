<template>
  <div class="sign-up">    
    <div class="inputbox" style="margin:100px auto; width:500px;">
      <b-jumbotron style="background:none; text-align:left">
        <center>
          <h2 style="margin-left: 60px">SignUp</h2>
          <hr style="border:2px solid black; width: 500px;">
          <b-form @submit="signUp">
            <table>
              <tr>
                <b-form-group id="input-group-1" label="학번" label-for="input-1">
                  <b-form-input id="input-1" v-model="form.id" type="text" required placeholder="Enter Id"></b-form-input>
                </b-form-group>
              </tr>
              <tr>
                <b-form-group id="input-group-2" label="비밀번호" label-for="input-2">
                  <b-form-input id="input-2" type="password" v-model="form.pass1" required placeholder="Enter Password"></b-form-input>
                </b-form-group>
              </tr>
              <tr>
                <b-form-group id="input-group-3" label="비밀번호 확인" label-for="input-3">
                  <b-form-input id="input-3" type="password" v-model="form.pass2" required placeholder="Enter Password"></b-form-input>
                </b-form-group>
              </tr>
              <tr>
                <b-form-group id="input-group-4" label="이름" lebel-for="input-4">
                  <b-form-input id="input-4" type="text" v-model="form.name" required placeholder="Enter Name"></b-form-input>
                </b-form-group>
              </tr>
              <tr>
                <b-form-group id="input-group-5" label="이메일" label-for="input-5">
                  <b-form-input id="input-5" type="email" v-model="form.email" required placeholder="Enter Email"></b-form-input>
                </b-form-group>
              </tr>
              <tr style="text-align: right">
                <b-button type="submit">가입</b-button>
              </tr>
            </table>
          </b-form>
        </center>
      </b-jumbotron>
      {{form.id}}
    </div>
  </div>

</template>
 
<script>
import axios from 'axios';
  export default {
    name: 'StudentSignUp',
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
        user:null,
      }
    },
    methods: {
      signUp(evt) {
        evt.preventDefault();
        axios.post('/api/studentSignUp', 
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
    }
  }
</script>
 
<style scoped>
  .sign-up { margin-top: 50px ;}
  .inputbox { padding: 0px auto ;}
  input { width: 500px; }
  button { margin-top: 10px; width: 80px; cursor: pointer; } 
</style>