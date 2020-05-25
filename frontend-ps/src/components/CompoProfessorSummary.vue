<template>
    <div class="containerStyle">
        <h3 style="margin-left: 10%">과목 개요</h3>
        <hr style="width: 80%">        
        <form ref="form">
                <center>
                    <b-form-group style="width: 80%">
                        <table class="table table-bordered" id="ProfessorSummary" v-bind="this.data">
                            <tbody>
                                <tr>
                                    <th scope="row" style="width:28%">과목명</th>
                                    <td>
                                        {{ this.data.title }}
                                        
                                    </td>
                                </tr>
                                <tr>
                                    <th scope="row" style="width:28%">연도</th>
                                    <td>
                                        {{ this.data.year }}
                                    </td>
                                </tr>
                                <tr>
                                    <th scope="row" style="width:28%">학기</th>
                                    <td>
                                        {{ this.data.semester }}
                                    </td>
                                </tr>
                                <tr>
                                    <th scope="row" style="width:28%">총 인원</th>
                                    <td>
                                        nn명
                                    </td>
                                </tr>
                                <tr>
                                    <th scope="row" style="width:28%">프로젝트 수</th>
                                    <td>
                                        8개
                                    </td>
                                </tr>
                                <tr>
                                    <th scope="row" style="width:28%">인증번호</th>
                                    <td>
                                        {{ this.data.authKey }}
                                    </td>
                                </tr>

                            </tbody>
                        </table>
                        <div class="mb-4" style="text-align: right">
                            <b-button variant="dark" @click="viewTeam()">팀별 확인</b-button>
                        </div>
                    </b-form-group>
                </center>
            </form>
    </div>
</template>

<script>
import axios from 'axios';
export default {
    name: 'professorSummary',
    watch: {
        '$route' () {
            axios.get('/api/subject/subjectName/'+this.$route.params.subjectId)
            .then(response => {
                this.data = response.data
            })    
        }
    },
    mounted() {
        axios.get('/api/subject/subjectName/'+this.$route.params.subjectId)
        .then(response => {
            this.data = response.data
        })        
    },
    methods: {
        viewTeam() {
            this.$router.push({
                path: '/Team'
            })
        }
    },
    data() {
        return { 
            data: {}
        }
    }
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