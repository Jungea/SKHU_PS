<template>
  <div style="margin:100px auto; width: 80%">
    <b-card-group deck style="text-align:center;">
        <b-card
        title="학생 가입하기"
        img-src="https://image.flaticon.com/icons/svg/2825/2825025.svg"
        img-alt="Image"
        img-top
        img-height="170px"
        style="padding:30px;"
        class=""
    >
        <hr style="width:50px; border:1px solid black">
        <b-card-text class="cardText">
            성공회대 학생인 경우 학생으로 가입하기를 클릭하세요.
        </b-card-text>

        <b-button @click="signUp()" variant="">학생 가입하기</b-button>
    </b-card>

    <b-card
        title="교수 가입하기"
        img-src="https://image.flaticon.com/icons/svg/2825/2825051.svg"
        img-alt="Image"
        img-top
        img-height="170px"
        style="padding:30px;"
        class=""
    >
        <hr style="width:50px; border:1px solid black">
        <b-card-text class="cardText">
            성공회대 교수인 경우 교수 인증 후 가입할 수 있습니다.<br>
            교수인 경우 교수로 가입하기를 클릭하세요.
        </b-card-text>

        <b-button @click="proAuth()" variant="">교수 가입하기</b-button>
    </b-card>
  </b-card-group>
  </div>
</template>

<script>
  export default {
    name: 'UserSelect',

    methods: {

      signUp() {
        this.$router.push({
        path: 'studentSignUp'
        })
      },
      proAuth() {
        this.$router.push({
        path: 'professorAuth'
        })
      }
    }
  }
</script>

<style>
a{
  font-weight: bold;
}
.cardText{
    height:200px;
    padding-top: 70px;
}
</style>