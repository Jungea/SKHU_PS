<template>
<div>
  <b-nav vertical class="w-25" style="min-width: 200px">
    <b-nav-item>🏚   길보미 님 📌👌</b-nav-item>
    <b-nav-item disabled><hr></b-nav-item>
    <b-nav-item> 
      <b-form-select style="width:200px" v-model="selected" :options="options"></b-form-select>
    </b-nav-item>
     <b-nav-item disabled><hr></b-nav-item>
     <b-nav-item disabled v-if="selected">
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
    <b-nav-item>프로젝트 게시판</b-nav-item>
    <b-nav-item>커뮤니티 게시판</b-nav-item>
    </b-nav>
</div>
</template>
<script>
export default {
    name: "Sidebar",
    data() {
      return {
        selected:'',
        options: [
          { value: '디비 캡스톤디자인', text: '디비 캡스톤디자인' },
          { value: '파이썬 프로젝트', text: '파이썬 프로젝트' },
          { value: '소프트웨어 캡스톤디자인', text: '소프트웨어 캡스톤디자인' },
        ]
      }
    }
}
</script>