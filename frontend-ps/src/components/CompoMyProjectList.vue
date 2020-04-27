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
                    <b-card align="left" bg-variant="dark" :header="item.projectName" text-variant="white" style="width: 25rem; height: 10rem; margin: 60px;">    
                        
                        <b-card-text>   
                            {{item.content}}   
                        </b-card-text>    
                    </b-card>    
                    </b-col>
                </b-row>    
            </b-container>    
        </center>    
    
        <!--프로젝트 생성 모달 -->
        <b-modal id="modal-xl" size="lg" title="프로젝트 생성" @ok="submit">
            <table class="table table-bordered">
                <tbody>
                    <tr>
                        <th scope="row" style="width:28%">프로젝트명</th>
                        <td><b-form-input  :state="projectName.length <= 10 && projectName.length>0" v-model="projectName" id="input-default" placeholder="프로젝트 이름을 입력하세요"></b-form-input></td>
                    </tr>
                    <tr>
                        <th scope="row">주제</th>
                        <td><b-form-input id="input-default" v-model="theme" placeholder="주제를 입력하세요"></b-form-input></td>
                    </tr>
                    <tr>
                        <th scope="row">내용</th>
                        <td><b-form-textarea id="textarea-state" v-model="content" placeholder="내용을 입력하세요"></b-form-textarea></td>
                    </tr>
                    <tr>
                        <th scope="row">사용기술과 언어</th>
                        <td>
                            <b-form-tags
                                input-id="tags-remove-on-delete"
                                :input-attrs="{ 'aria-describedby': 'tags-remove-on-delete-help' }"
                                v-model="tag"
                                separator=" ,;"
                                placeholder="태그를 입력하세요"
                                remove-on-delete
                                add-on-enter
                                class="mb-2"
                            ></b-form-tags>
                        </td>
                    </tr>
                    <tr>
                        <th scope="row">인원 모집 상태</th>
                        <td><b-form-select v-model="rcrtState" :options="options"></b-form-select></td>
                    <tr>
                        <th scope="row">과목 인증키 </th>
                        <td><b-form-input id="input-default" v-model="authKey" placeholder="수강 과목의 인증키를 입력하세요"></b-form-input></td>
                    </tr>
                </tbody>
 
            </table>
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
            data:{},
            
            // 프로젝트 생성 모달 창
            rcrtState:false,
            projectName:'',
            theme:'',
            content:'',
            options: [
                { value: false, text: '모집중' },
                { value: true, text: '모집 완료' },
            ],
            tag:[],
            authKey:'',
        };
    },
    methods: {
        submit() {
            axios.post('/api/makeProject'),{

            }.then(response => { 
            this.user = response.data;
                
            });
        }
    }
}
</script>

<style>

.card-header { font-size: 20px }
.card-text { font-size: 13px }

</style>