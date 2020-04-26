<template>
    <div>
        <center>    
            <b-container class="bv-row">    
                <b-row class="m-4">    
                    <b-col>  
                            <h4>내 프로젝트</h4>      
                    </b-col>    
                </b-row>    
                <b-row style="padding-left: 94%">    
                    <b-button v-b-modal.modal-xl variant="outline-secondary" style="width: 100px ; height: 70px;">프로젝트 생성</b-button>    
                </b-row>    
                <b-row cols="2">    
                    <b-col :key="item" v-for="item in data">
                    <b-card @click="viewSummary" align="left" bg-variant="dark" :header="item.projectName" text-variant="white" style="width: 25rem; height: 10rem; margin: 60px;">    
                        
                        <b-card-text>   
                            {{item.content}}   
                        </b-card-text>    
                    </b-card>    
                    </b-col>
                </b-row>    
            </b-container>    
        </center>    

        <!--프로젝트 생성 모달 -->
        <b-modal id="modal-xl" size="lg" title="프로젝트 생성">
            <b-table stacked :items="items" :fields="fields">
            <template v-slot:cell(프로젝트이름)="row">
                <b-form-input v-model="row.item.프로젝트이름"/>
            </template>
            <template v-slot:cell(사용언어)="row">
                <b-form-input v-model="row.item.사용언어"/>
            </template>
            <template v-slot:cell(사용기술)="row">
                <b-form-input v-model="row.item.사용기술"/>
            </template>
            <template v-slot:cell(주제)="row">
                <b-form-input v-model="row.item.주제"/>
            </template>
            <template v-slot:cell(내용)="row">
                <b-form-textarea v-model="row.item.내용"/>
            </template>
        </b-table>
        </b-modal>
    </div>

</template>

<script>
import axios from 'axios';

export default {
    mounted() {
        axios.get('/api/user/projects')
        .then(response => {
            this.data = response.data }
        )
    },
    data() {
        return {
            fields: { 프로젝트이름: "", 사용언어 : "",사용기술:"",주제:"",내용:"" },
            items: [{ 프로젝트이름: "", 사용언어:"" ,사용기술:"",주제:"",내용:""}],
            items2: [{'플젝 이름': '파이썬 프로젝트', '사용 언어': '자바','사용 도구':'스프링','주제':'자바로 계산기 만들기','내용':'자바를 이용해서 배운걸 토대로 만들어볼 예정입니다.','모집여부':'모집중'},               ],
            data:{}
        };
    },
     methods: {
                viewSummary() {
                    
                    this.$router.push({
                    path: '/summary'
                    })
                }
            }
}
</script>

<style>

.card-header { font-size: 20px }
.card-text { font-size: 13px }

</style>