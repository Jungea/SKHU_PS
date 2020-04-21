<template>
    <div>
        <center>    
            <b-container class="bv-row">    
                <b-row class="m-4">    
                    <b-col>    
                        <center>    
                            <h4>내 프로젝트</h4>    
                        </center>    
                    </b-col>    
                </b-row>    
                <b-row>    
                    <b-button v-b-modal.modal-xl variant="outline-secondary" style="width: 100px ; height: 70px;">프로젝트 생성</b-button>    
                </b-row>    
                <b-row>    
                    <b-card align="left" bg-variant="dark" :header="item.name" text-variant="white" :key="item" v-for="item in data" style="width: 25rem; height: 10rem; margin: 60px;">    
                        <b-card-text>    
                            {{item.word}}    
                        </b-card-text>    
                    </b-card>    
                </b-row>    
            </b-container>    
        </center>    
    
        <!-- 예시용 모달 -->
        <b-modal id="modal-xl" size="lg" title="프로젝트 생성">
            <b-table stacked :items="items" :fields="fields">
            <template v-slot:cell(프로젝트이름)="row">
                <b-form-input v-model="row.item.프로젝트이름"/>
            </template>
            <template v-slot:cell(사용언어)="row">
                <b-form-input v-model="row.item.사용언어"/>
            </template>
            <template v-slot:cell(사용기술)="row">
                <b-form-input v-model="row.item.사용언어"/>
            </template>
            <template v-slot:cell(주제)="row">
                <b-form-input v-model="row.item.사용언어"/>
            </template>
            <template v-slot:cell(내용)="row">
                <b-form-textarea v-model="row.item.사용언어"/>
            </template>
        </b-table>
        </b-modal>
    </div>

</template>

<script>
export default {
    data() {
        return {
            fields: { 프로젝트이름: "", 사용언어 : "",사용기술:"",주제:"",내용:"" },
      items: [{ 프로젝트이름: "", 사용언어:"" ,사용기술:"",주제:"",내용:""}],
            items2: [
          {'플젝 이름': '파이썬 프로젝트', '사용 언어': '자바','사용 도구':'스프링','주제':'자바로 계산기 만들기','내용':'자바를 이용해서 배운걸 토대로 만들어볼 예정입니다.','모집여부':'모집중'},
        ],
            data: 
            [{
                    name: '디비 플젝',
                    word: '내용1'
                },
                {
                    name: '파이썬 플젝',
                    word: '내용2'
                },
                {
                    name: '소프 플젝',
                    word: '내용3'
                },
                {
                    name: '자바 플젝',
                    word: '내용4'
                },
                {
                    name: '제목',
                    word: '내용5'
                }
            ]
        };
    }
}
</script>

<style>

</style>