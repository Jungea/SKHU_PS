<template>
    <div class="findPass" style="margin:100px auto; width:500px;">
        <b-jumbotron style="background:none; text-align:left">
            <h2>교수 인증</h2>
            <hr style="border: 1px solid black;">
            <div style="margin-top: 30px;">
                <p style="font-size:10pt; color: grey; margin-bottom:30px;">교수 인증을 위한 비밀번호를 입력해주세요.</p>
                <b-form @submit="onSubmit" v-if="show">
                    <b-form-group style="text-align:left"
                    id="input-group-1"
                    label="Password"
                    label-for="ProPass"
                    >
                        <b-form-input 
                        id="ProPass" 
                        v-model="form.password" 
                        :state="state" 
                        type="password"
                        required
                        trim
                        placeholder="Enter Password"
                        ></b-form-input>
                    </b-form-group>

                    <b-button style="width:100%; margin-top:15px;" type="submit">인증하기</b-button>
                </b-form>
            </div>
        </b-jumbotron>
    </div>
</template>
<script>
export default {
    name: 'ProfessorAuth',
    data() {
      return {
        form: {
          password: ''
        },
        show: true
      }
    },
    methods: {
        onSubmit(evt) {
            evt.preventDefault()
            location.href="/SignUp"
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