<template>
  <b-container fluid>
    <div style="margin:100px auto; width: 500px">
      <b-jumbotron style="text-align:center; background:none;">
        <h2>Login</h2>
        <hr style="border:2px solid black; width: 90px;">
        <div style="margin-top: 50px;">
          <b-form @submit="onSubmit" v-if="show" class="loginForm">
            <!--아디-->
            <b-form-group id="input-group-1" label="학번/교번" label-for="number">
              <b-form-input id="number" v-model="form.number" type="text" required trim placeholder="Enter your number"></b-form-input>
            </b-form-group>

            <!--비번-->
            <b-form-group id="input-group-2" label="비밀번호" label-for="password">
              <b-form-input id="password" v-model="form.password" type="password" required trim placeholder="Enter Password"></b-form-input>
            </b-form-group>

            <b-button style="width:100%; margin:15px 0;" type="submit">로그인</b-button>

          </b-form>
          <span style="margin-right: 40px"><a style="text-decoration:none; color:black" href="" @click="signUp">회원가입</a></span> 
          <span><a style="text-decoration:none; color:black;" href="" @click="findPass">비밀번호 찾기</a></span> 
        </div>
      </b-jumbotron>
    </div>
  </b-container>
</template>

<script>
import axios from 'axios';
  export default {
    data() {
      return {
        form: {
          number: '',
          password: ''
        },
        show: true,
        user: null
      }
    },
    methods: {
      onSubmit(evt) {
        evt.preventDefault()
        
        if(!parseInt(this.form.number)){
          alert('학번/교번은 숫자 형식입니다. 숫자 형식으로 입력하세요.');
          this.form.number='';
          this.form.password='';
        } else{
            axios.post('/api/user/login', {
              userNum: this.form.number,
              password: this.form.password
            }).then(response => {
              this.user = response.data;
              if(typeof this.user == 'object') {
                //로그인한 유저 이름
                console.log(response.data.name)
                if(this.user.emailCheck==false) {
                  alert('이메일 인증을 완료해주세요.');
                } else {
                  location.href="/home" 
                }
              } else {        
                alert("학번/교번 또는 비밀번호를 확인해주세요.");
                this.form.number='';
                this.form.password='';
              }
        
            }).catch((erro)=> {
              console.error(erro);
            });
          }
        
      },
      signUp() {
        this.$router.push({
        path: 'userSelect'
        })
      },
      findPass() {
        this.$router.push({
        path: 'findPass'
        })
      }
    }
  }
</script>

<style>
a{
  font-weight: bold;
}
.loginForm{
  text-align: left;
}
</style>