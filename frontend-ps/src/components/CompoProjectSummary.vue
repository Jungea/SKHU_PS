<template>
    <div class="containerStyle">
        <h3>프로젝트 정보</h3>
        <hr>        
        <form ref="form">
            <b-form-group label-for="ProjectSummary">
                <table class="table table-bordered" id="ProjectSummary" v-bind="this.data">
                    <tbody>
                        <tr>
                            <th scope="row" style="width:28%">프로젝트명</th>
                            <td>
                                {{ data.projectName }}
                            </td>
                        </tr>
                        <tr>
                            <th scope="row">멤버 수</th>
                            <td>
                                {{ data.memNum }}
                            </td>
                        </tr>
                        <tr>
                            <th scope="row">팀장</th>
                            <td>
                                {{ capName }}({{ grade }})
                            </td>
                        </tr>
                        <tr>
                            <th scope="row">팀원</th>
                            <td>
                                {{ memList.join(", ") }}
                            </td>
                        </tr>
                        <tr>
                            <th scope="row">주제</th>
                            <td>
                                {{ data.theme }}
                            </td>
                        </tr>
                        <tr>
                            <th scope="row">내용</th>
                            <td>
                                {{ data.content }}
                            </td>
                        </tr>
                        <tr>
                            <th scope="row">사용기술과 언어</th>
                            <td>
                                <b-badge variant="secondary" v-for="(tag,index) in tagArray" :key="index" style="margin-right:5px">{{tag}}</b-badge>
                            </td>
                        </tr>
                        <tr>
                            <th scope="row">깃허브 리포지토리 주소</th>
                            <td>
                              {{ data.github }}
                            </td>
                        </tr>
                        <tr>
                            <th scope="row">인원 모집 상태</th>
                            <td>
                               {{ state }}
                            </td>
                        </tr>
                        <tr>
                            <th scope="row">프로젝트 진행 상태</th>
                            <td>
                               {{ progState }}
                            </td>
                        </tr>
                        <tr v-if="data.subject">
                            <th scope="row">과목</th>
                            <td>
                                 {{ subject }}
                            </td>
                        </tr>

                    </tbody>
                </table>
                <div class="mb-4" style="text-align: right">
                    <b-button variant="dark" @click="edit(data.projectId)">수정</b-button>
                </div>
            </b-form-group>
            </form>
    </div>
</template>

<script>
import axios from 'axios';
export default {
    data() {
        return { 
            data: {},
            subject: '',
            state: '',
            progState: '',
            tagArray:[],
            memList: [],
            capName: '',
            grade: ''
        }
    },

    // url 파라미터 변화가 일어나는 것을 인지(사이드바 pin 선택 시) 
    watch: {
        '$route' () {
            this.reload();
        }
    },

    mounted() {
        this.reload();
    },

    methods: {
        reload() {
            // 프로젝트
            axios.get('/api/project/'+this.$route.params.projectId)
                .then(response => {
                    this.data = response.data;
                    this.capName = this.data.user.name
                    this.grade = this.data.user.grade
                    if(this.data.tag)
                        this.tagArray = this.data.tag.split(',');
                    if(this.data.subject == null)
                        this.subject = '과목없음'
                    else
                        this.subject = this.data.subject.title;
                    this.rcrtStateCheck();
                    this.progStateCheck(); 
                })

            // 팀원
            axios.get('/api/project/' + this.$route.params.projectId + '/member')
                .then(response => {
                    this.memList = [];
                    if(response.data.length != 0) {
                        for(var i = 0 ; i < response.data.length ; i++) {
                            this.memList.push(response.data[i].user.name + '(' + response.data[i].user.grade + ')');
                        }
                    }
                })
        },
        
        rcrtStateCheck() {
           if(this.data.rcrtState == 0)
                this.state = '모집중'
            else
                this.state = '모집완료'
        },
        progStateCheck() {
           if(this.data.progState == 0)
                this.progState = '진행중'
            else
                this.progState = '진행완료'
        },
        edit(projectId) {
            this.$router.push({
                path: '/project/'+ projectId +'/edit'
            })
        }
    },
    
}
</script>

<style>
.containerStyle h3{
    font-weight: bold;
}

.containerStyle {
    margin: 100px;
    min-width: 350px;
}
</style>