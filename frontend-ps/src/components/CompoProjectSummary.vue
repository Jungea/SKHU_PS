<template>
    <div class="containerStyle">
        <h3>프로젝트 개요</h3>
        <hr>        
        <form ref="form" @submit.stop.prevent="handleSubmit">
            <b-form-group
                label-for="ProjectSummary"
                invalid-feedback="입력하지 않은 필수 입력 사항이 있습니다."
            >
                <table class="table table-bordered" id="ProjectSummary" v-bind="this.data">
                    <tbody>
                        <tr>
                            <th scope="row" style="width:28%">프로젝트명</th>
                            <td>
                                {{ this.data.projectName }}
                            </td>
                        </tr>
                         <tr>
                            <th scope="row" style="width:28%">멤버 수</th>
                            <td>
                                {{ this.data.memNum }}
                            </td>
                        </tr>
                        <tr>
                            <th scope="row">주제</th>
                            <td>
                                {{ this.data.theme }}
                            </td>
                        </tr>
                        <tr>
                            <th scope="row">내용</th>
                            <td>
                                {{ this.data.content }}
                            </td>
                        </tr>
                        <tr>
                            <th scope="row">사용기술과 언어</th>
                            <td>
                                {{ this.data.tag }}
                            </td>
                        </tr>
                        <tr>
                            <th scope="row">깃허브 리포지토리 주소</th>
                            <td>
                              {{ this.data.github }}
                            </td>
                        </tr>
                        <tr>
                            <th scope="row">인원 모집 상태</th>
                            <td>
                                {{ rcrtStateCheck()}}
                               {{ this.state }}
                            </td>
                        <tr>
                            <th scope="row">과목 여부</th>
                            <td>
                                {{ subjectCheck() }}
                                 {{ this.subject }}
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
     mounted() {
        axios.get('/api/project/'+this.$route.params.projectId)
        .then(response => {
            this.data = response.data
        })        
     },
     methods: {
        subjectCheck() {
            if(this.data.subjectId == null )
                this.subject = '과목없음'
            else
                this.subject = '과목있음'
        },
        rcrtStateCheck() {
           if(this.data.rcrtState == 0)
                this.state = '모집중'
            else
                this.state = '모집완료'           
        },
        edit(projectId) {
            this.$router.push({
                path: '/project/'+projectId+'/edit'
            })
        }
    },
    data() {
        return { 
            data: {},
            subject: '',
            state: ''
        }
    }
}
</script>

<style>
.containerStyle h3{
    font-weight: bold;
}

.containerStyle {
    margin: 100px
}
</style>