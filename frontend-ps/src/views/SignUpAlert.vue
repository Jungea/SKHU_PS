<template>
    <div class="findPassAlert" style="margin:100px auto; width:500px;">
        <b-jumbotron style="background:none; text-align:left">
            <h2>Sign up</h2>
            <hr style="border: 1px solid black;">
            <div style="margin-top: 30px;">
                <p style="font-size:10pt; color: grey; margin-bottom:30px;">
                    입력하신 이메일로 인증 메일을 발송했습니다.<br>
                    메일함에서 메일을 확인해주세요.
                </p>
                
                <b-button @click="Login" style="width:100%; margin-top:15px;">Login</b-button>
            </div>
        </b-jumbotron>
    </div>
</template>
<script>
export default {
    name: 'SignUpAlert',
    methods: {
        Login() {
            this.$router.push({
                path: '/'
            })
        }
    },
}
</script>