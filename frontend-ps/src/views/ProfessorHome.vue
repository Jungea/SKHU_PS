<template>
    <div class="home">
        <center>    
            <b-container class="bv-row">
                <b-row class="m-4">
                    <b-col><h4>프로젝트 과목 리스트</h4></b-col>
                </b-row>

                <b-row class="text-right mb-4">
                    <b-col><b-button v-b-modal.modal-xl variant="outline-secondary" style="height: 70px;">과목 생성</b-button></b-col>
                </b-row>

                <p style="color:silver; margin:300px auto;" v-if="data.length==0?true:false">과목이 없습니다.</p>
                <b-row cols-md="2" cols="1">
                    <b-col class="mb-5" :key="index" v-for="(item, index) in data">
                        <b-card align="left" bg-variant="dark" text-variant="white" style="height: 15rem;"> <!-- 30rem == 480px -->
                            <div>
                                <b-card-header style="padding: 0 0 10px 0">
                                    <table>
                                        <tr>
                                            <td style="width: 100%">{{item.title}}</td>
                                        </tr>
                                    </table>
                                </b-card-header>
                            </div>
                            <b-card-text style="margin-top: 10px">
                                <p style="font-size:17px">{{item.year}}년</p>
                                <p style="font-size:17px">{{item.semester}}학기</p>
                            </b-card-text>    
                        </b-card>  
                    </b-col>
                </b-row>    
            </b-container>    
        </center>    

         <b-modal id="modal-xl" size="lg" title="과목 생성" 
            @show="resetModal"
            @hidden="resetModal"
            @ok="submit" ref="modal" data-backdrop="static">
            <form ref="form" @submit.stop.prevent="handleSubmit">
            <b-form-group
                :state="subjectState"
                label=""
                label-for="createSubject"
                invalid-feedback="입력하지 않은 필수 입력 사항이 있습니다.">
                <table class="table table-bordered" id="createSubject">
                    <tbody>
                        <tr>
                            <th scope="row" style="width:28%">과목명</th>
                            <td>
                                <b-form-input v-model="title" id="input-default" required trim placeholder="과목 이름을 입력하세요"></b-form-input>
                            </td>
                        </tr>
                        <tr>
                            <th scope="row">연도</th>
                            <td>
                                <b-form-input type="number" id="input-default1" v-model="year" required trim placeholder="과목 생성 연도를 입력하세요"></b-form-input>
                            </td>
                        </tr>
                        <tr>
                            <th scope="row">학기</th>
                            <td>
                                <b-form-input type="number" id="input-default2" v-model="semester" required trim placeholder="과목 생성 학기를 입력하세요"></b-form-input>
                            </td>
                        </tr>
                        <tr>
                            <th scope="row">과목 비밀번호</th>
                            <td>
                                <b-form-input type="password" id="input-default3" v-model="authKey" required trim placeholder="과목 비밀번호를 입력하세요"></b-form-input>
                            </td>
                        </tr>
                    </tbody>
                </table>
            </b-form-group>
            </form>
        </b-modal>
    </div>
</template>

<script>
import axios from 'axios';
export default {
    name: 'professorHome',
    data() {
        return {
            data:{},
            title: '',
            year: '',
            semester: '',
            authKey: ''
        };
    },
    mounted() { 
        axios.get('/api/subjects') // 모든 과목
        .then(response => {
            this.data = response.data
        });
    },

     methods: {
        submit() {
            alert(this.authKey)
            if(!parseInt(this.year))
                alert("연도 항목에는 숫자만 작성 가능합니다.")
            else if(!parseInt(this.semester))
                alert("학기 항목에는 숫자만 작성 가능합니다.")
            else {
                axios.post('/api/url',{
                    title:this.subjectName,
                    year:this.year,
                    semester:this.semester,
                    authKey:this.authKey
                }).then(response => { 
                    this.subject = response.data;
                    location.reload()
                });
            }
        },
        resetModal() {
                this.title = '',
                this.year = '',
                this.semester = '',
                this.authKey = ''
        }
    },
    computed:{
        nameValidation(){        //과목명 체크
            return this.title.replace(/ /g,"").length>0
        },
        yearValidation(){        //년도 체크
            return this.year.replace(/ /g,"").length>0
        },
        semesterValidation(){        //학기 체크
            return this.semester.replace(/ /g,"").length>0
        },
        authKeyValidation(){        //비밀번호 체크
            return this.authKey.replace(/ /g,"").length>0
        },
        subjectState(){
             return this.nameValidation&&this.yearValidation&&this.semesterValidation&&this.authKeyValidation
        }
    }
}

</script>

<style>
.card-header { font-size: 20px }
.card-text { font-size: 13px }
</style>