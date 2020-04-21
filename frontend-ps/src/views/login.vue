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
            label="E-mail"
            label-for="email"
          >
            <b-form-input 
              id="email" 
              v-model="form.email" 
              :state="state" 
              type="email"
              required
              trim
              placeholder="Enter Email"
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
        </b-form>
      </div>
    </b-jumbotron>
  </div>
</template>

<script>
  export default {
    data() {
      return {
        form: {
          email: '',
          password: ''
        },
        show: true
      }
    },
    methods: {
      onSubmit(evt) {
        evt.preventDefault()
        alert(JSON.stringify(this.form))//나중에 삭제ㄱㄱ
        location.href="/"
      }
    }
  }
</script>

<style>
</style>