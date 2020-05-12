<template>
  <b-container fluid>
    <div style="margin:100px auto; min-width: 450px; max-width: 550px">
      <b-jumbotron style="text-align:center; background:none;">
        <h2>Professor SignUp</h2>
        <hr style="border:2px solid black; width: 200px;">
        <!--가입 폼-->
        <b-form @submit="signUp" style="text-align:left; margin-top:50px;">
          <b-form-group id="input-group-1" label="교번" label-for="input-1">
            <b-form-input id="input-1" v-model="form.id" type="text" required placeholder="Enter number"></b-form-input>
            <b-form-invalid-feedback :state="numValidation" v-if="form.id.length>0?true:false">
              이미 가입된 정보입니다.
            </b-form-invalid-feedback>
            <b-form-valid-feedback :state="numValidation" v-if="form.id.length>0?true:false">                   
            </b-form-valid-feedback>
          </b-form-group>
            
          <b-form-group id="input-group-2" label="비밀번호" label-for="input-2">
            <b-form-input id="input-2" type="password" v-model="form.pass1" required placeholder="Enter password"></b-form-input>
            <b-form-invalid-feedback :state="passValidation2" v-if="form.pass1.length>0?true:false">
              비밀번호는 8자 이상 12자 이하입니다.
            </b-form-invalid-feedback>
            <b-form-valid-feedback :state="passValidation2" v-if="form.pass1.length>0?true:false">
              사용가능한 비밀번호입니다.
            </b-form-valid-feedback>
          </b-form-group>
        
          <b-form-group id="input-group-3" label="비밀번호 확인" label-for="input-3">
            <b-form-input id="input-3" type="password" v-model="form.pass2" required placeholder="Check password"></b-form-input>
            <b-form-invalid-feedback :state="passValidation" v-if="form.pass2.length>0?true:false">
              입력한 비밀번호와 일치하지 않습니다.
            </b-form-invalid-feedback>
            <b-form-valid-feedback :state="passValidation" v-if="form.pass2.length>0?true:false">
              비밀번호가 일치합니다.
            </b-form-valid-feedback>
          </b-form-group>
        
          <b-form-group id="input-group-4" label="이름" lebel-for="input-4">
            <b-form-input id="input-4" type="text" v-model="form.name" required placeholder="Enter name"></b-form-input>
          </b-form-group>
        
          <b-form-group id="input-group-5" label="이메일" label-for="input-5">
            <b-form-input id="input-5" type="email" v-model="form.email" required placeholder="Enter Email"></b-form-input>
            <b-form-text id="input-live-help">입력한 이메일로 인증 링크가 전송됩니다. 정확하게 입력해주세요.</b-form-text>
            <b-form-invalid-feedback :state="emailValidation" v-if="form.email.length>0?true:false">
              이미 가입된 이메일입니다.
            </b-form-invalid-feedback>
            <b-form-valid-feedback :state="emailValidation" v-if="form.email.length>0?true:false">                   
            </b-form-valid-feedback>
          </b-form-group>

          <b-form-group id="input-group-7" label="학년" lebel-for="input-7">
                <b-form-select id="input-7" v-model="form.gradeSelected" :options="gradeOptions" value-field="item" text-field="value" ></b-form-select>
                </b-form-group>
        
          <b-button style="width:100%; margin:15px 0;" type="submit">가입</b-button>
            
        </b-form>
      </b-jumbotron>
    </div>
  </b-container>

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
 
<style>
</style>