<template>
<div>
  <b-nav vertical class="w-25" style="min-width: 200px">
    <p class="mt-4 ml-3">
      <b-icon-house-fill  style="cursor:pointer" class="mr-3" font-scale="1.5" @click="home"></b-icon-house-fill>
      길보미 님
      <b-icon-bell style="cursor:pointer" class="ml-3" font-scale="1.4" ></b-icon-bell>
      <b-icon-gear style="cursor:pointer" class="ml-3" font-scale="1.4" @click="profile"></b-icon-gear>
      </p>
    <b-nav-item disabled><hr></b-nav-item>
    <b-nav-item> 
      <b-form-select style="width:200px" v-model="selected" :options="options"></b-form-select>
    </b-nav-item>
     <b-nav-item disabled><hr></b-nav-item>
     <b-nav-item disabled v-show="selected">
       <strong>{{ selected }}</strong>
       <div>프로젝트 정보</div>
       <div>캘린더</div>
       <div>주차별 목표(to-do-list)</div>
       <div>토론 게시판</div>
       <div>자유 게시판</div>
       <div>관리</div>
       <hr>
    </b-nav-item>
    <b-nav-item>it 경진대회</b-nav-item>
    <b-nav-item @click="projectBoard">프로젝트 게시판</b-nav-item>
    <b-nav-item >커뮤니티 게시판</b-nav-item>
    </b-nav>
</div>
</template>
<script>
import axios from 'axios';
export default {
    name: "Sidebar",
    data() {
      return {
        selected: null,
        options: [ {value: null, text: '프로젝트를 선택하세요.'} ]
      }
    },
    mounted() {
      axios.get('/api/user/sidebar'
          ).then(response => {

            this.options = response.data;
            this.options.unshift({value: null, text: '프로젝트를 선택하세요.'})
        
        }).catch((erro)=> {
          console.error(erro);
        });
    },
    methods: {
      projectBoard(evt) {
        evt.preventDefault()
        location.href="/projectBoard"
      },
      home(evt) {
        evt.preventDefault()
        location.href="/home"
      },
      profile(evt){
        evt.preventDefault()
        location.href="/profile"
      }
    }
}
</script>