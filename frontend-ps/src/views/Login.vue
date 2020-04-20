<template>
  <div class="login">
    <h3>로그인</h3>
    <input type="text" v-model="number" placeholder="학번"><br>
    <input type="password" v-model="password" placeholder="비밀번호"><br>
    <button @click="signUp">회원가입</button> 
    <button @click="findPass">비밀번호 찾기</button> 
    <br>
    <button @click="login">로그인</button>
  </div>
</template>
 
<script>
  export default {
    name: 'Login',
    data() {
      return {
          number: '',
          password: ''
      };
    },
    methods: {
        login() {
            this.$router.push({
                path: 'home'
            })
        },
        signUp() {
            this.$router.push({
                path: 'signUp'
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
 
<style scoped>  /* "scoped" attribute limit the CSS to this component only */
  .login { margin-top: 40px; }
  input { margin: 10px 0; width: 200px; padding: 5px; }
  button { margin-top: 20px; width: 100px; cursor: pointer; padding: 5px; }
</style>
