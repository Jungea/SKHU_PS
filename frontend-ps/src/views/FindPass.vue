<template>
    <div class="findPass" style="margin:100px auto; width:500px;">
        <b-jumbotron style="background:none; text-align:left">
            <h2>Find Password</h2>
            <hr style="border: 1px solid black;">
            <div style="margin-top: 30px;">
                <p style="font-size:10pt; color: grey; margin-bottom:30px;">가입할 때 입력한 이메일로 비밀번호 재설정 링크를 보내드립니다.</p>
                <b-form @submit="onSubmit" v-if="show">
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

                    <b-button style="width:100%; margin-top:15px;" type="submit">Send Link</b-button>

                </b-form>
            </div>
        </b-jumbotron>
    </div>
</template>
<script>
export default {
    name: 'FindPass',
    data() {
      return {
        form: {
          email: ''
        },
        show: true
      }
    },
    methods: {
        onSubmit(evt) {
            evt.preventDefault()
            location.href="/findPassAlert"
        },
        /*
        findPassalert() {
            this.$router.push({
                path: '/findPassAlert'
            })
        }
        */
    }
}
</script>