<template>
  <div style="margin:100px auto; width:500px">
    <b-jumbotron style="text-align:center; background:none;">
      <h2>Login</h2>
      <hr style="border:2px solid black; width: 90px;">
      <div style="margin-top: 50px;">
        <b-form @submit="onSubmit" v-if="show">
        <!--아디-->
          <b-form-group style="text-align:left"
            id="input-group-1"
            label="Number"
            label-for="number"
          >
            <b-form-input 
              id="number" 
              v-model="form.number" 
              :state="state" 
              type="text"
              required
              trim
              placeholder="Enter your number"
              ></b-form-input>
          </b-form-group>

          <!--비번-->
          <b-form-group style="text-align:left"
            id="input-group-2"
            label="Password"
            label-for="password"
          >
            <b-form-input 
              id="password" 
              v-model="form.password" 
              :state="state2" 
              type="password" 
              required
              trim
              placeholder="Enter Password"
              ></b-form-input>
          </b-form-group>

          <b-button style="width:100%; margin-top:15px;" type="submit">Login</b-button>

          <br><br>

        </b-form>
        <span style="margin-right: 40px"><a style="text-decoration:none; color:black" href="" @click="signUp">회원가입</a></span> 
        <span><a style="text-decoration:none; color:black;" href="" @click="findPass">비밀번호 찾기</a></span> 
      </div>
    </b-jumbotron>
  </div>
</template>

<script>
  export default {
    data() {
      return {
        form: {
          number: '',
          password: ''
        },
        show: true
      }
    },
    methods: {
      onSubmit(evt) {
        evt.preventDefault()
        location.href="/home"
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
</style>