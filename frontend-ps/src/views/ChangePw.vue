<template>
  <div class="sign-up">    
    <div class="inputbox" style="margin:100px auto; width:500px;">
      <b-jumbotron style="background:none; text-align:left">
        <h2>Change Password</h2>
            <hr style="border: 1px solid black;">
            <div style="margin-top: 30px;">
            <b-form @submit="signUp">
            <table>
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
              <b-button style="width:100%; margin-top:15px;" type="submit">수정</b-button>

            </table>
          </b-form>
          </div>
      </b-jumbotron>
    </div>
  </div>

</template>
 
<script>
import axios from 'axios';
  export default {
    name: 'ChangePw',
    data() {
    return {
          form: {
          pass1: '',
          pass2: '',
        },
        show: true,
        user:null,
      }
    },
    methods: {
      signUp(evt) {
        evt.preventDefault();
        axios.post('/api/changePw/'+this.$route.params.authKey, 
        {
          password:this.form.pass1,
        }).then(response => { 
          this.user = response.data;
          this.$router.push({
            path:'/'
          })
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