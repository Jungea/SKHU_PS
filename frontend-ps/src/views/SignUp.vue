<template>
  <div class="sign-up">    
    <div class="inputbox">
      <center>
        <h5>회원가입(학생)</h5>
        <hr>
        <b-form @submit="signUp">
          <table>
            <tr>
              <b-form-group id="input-group-1" label="아이디(학번)" label-for="input-1">
                <b-form-input id="input-1" v-model="form.id" type="text" required placeholder="아이디를 입력하세요."></b-form-input>
              </b-form-group>
            </tr>
            <tr>
              <b-form-group id="input-group-2" label="비밀번호" label-for="input-2">
                <b-form-input id="input-2" type="password" v-model="form.pass1" required placeholder="비밀번호를 입력하세요."></b-form-input>
              </b-form-group>
            </tr>
            <tr>
              <b-form-group id="input-group-3" label="비밀번호 확인" label-for="input-3">
                <b-form-input id="input-3" type="password" v-model="form.pass2" required placeholder="비밀번호를 한번 더 입력하세요."></b-form-input>
              </b-form-group>
            </tr>
            <tr>
              <b-form-group id="input-group-4" label="이름" lebel-for="input-4">
                <b-form-input id="input-4" type="text" v-model="form.name" required placeholder="이름을 입력하세요."></b-form-input>
              </b-form-group>
            </tr>
            <tr>
              <b-form-group id="input-group-5" label="이메일" label-for="input-5">
                <b-form-input id="input-5" type="email" v-model="form.email" required placeholder="이메일을 입력하세요."></b-form-input>
              </b-form-group>
            </tr>
            <tr style="text-align: right">
              <b-button type="submit" variant="primary">가입</b-button>
            </tr>
          </table>
        </b-form>
      </center>
    </div>
  </div>

<!--
    <b-card class="mt-3" header="Form Data Result">
      <pre class="m-0">{{ form }}</pre>
    </b-card>
-->
</template>
 
<script>
  export default {
    name: 'SignUp',
    data() {
    return {
        form: {
          id: '',
          pass1: '',
          pass2: '',
          email: '',
          name: ''
        },
        show: true
      }
    },
    methods: {
      signUp(evt) {
        evt.preventDefault()
        alert(JSON.stringify(this.form))
        alert("회원가입 완료")
        this.$router.push({
        path: '/'
        })
      },
    }
  }
</script>
 
<style scoped>
  .sign-up { margin-top: 50px ;}
  .inputbox { padding: 0px auto ;}
  input { width: 500px; background-color: rgb(235,235,235); }
  button { margin-top: 10px; width: 80px; cursor: pointer; } 
</style>