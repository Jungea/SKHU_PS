<template>
    <div class="findPassAlert" style="margin:100px auto; width:500px;">
        <b-jumbotron style="background:none; text-align:left">
            <h2>Certification</h2>
            <hr style="border: 1px solid black;">
            <div style="margin-top: 30px;" >
                <p style="font-size:10pt; color: grey; margin-bottom:30px;">
                    이메일 인증이 완료되었습니다.<br>
                    앞으로 로그인이 가능합니다.
                </p>
                
                <b-button @click="Login" style="width:100%; margin-top:15px;">Login</b-button>
            </div>
        </b-jumbotron>
    </div>
</template>
<script>
import axios from 'axios';
export default {
    name: 'AuthAlert',
    data() {
      return {
          user:null
      }
    },
    //화면 보이기 전에 email_check를 true로 바꿔기 위해서
    created() {
        axios.post('/api/authKeyChange/'+this.$route.params.authKey, 
        {
        }).then(response => { 
          this.user = response.data;
          
            console.log('bbb')
        })
    },
    methods: {
        Login() {
            this.$router.push({
                path: '/'
            })
        },
    },
}
</script>