<template>
<center>
    <div class="containerStyle" style="width: 70%">
        <h3 style="margin-left: 10% ; text-align: left">과목 개요</h3>
        <hr style="width: 80%">        
        <form ref="form">
                <center>
                    <b-form-group style="width: 80%">
                        <table class="table table-bordered" id="ProfessorSummary" v-bind="this.data">
                            <tbody>
                                <tr>
                                    <th class="th2" scope="row" style="width:28%">과목명</th>
                                    <td>
                                        {{ data.title }}
                                        
                                    </td>
                                </tr>
                                <tr>
                                    <th class="th2" scope="row">연도</th>
                                    <td>
                                        {{ data.year }}
                                    </td>
                                </tr>
                                <tr>
                                    <th class="th2" scope="row">학기</th>
                                    <td>
                                        {{ data.semester }}
                                    </td>
                                </tr>
                                <tr>
                                    <th class="th2" scope="row">총 인원</th>
                                    <td>
                                        {{ memberCount }}명
                                    </td>
                                </tr>
                                <tr>
                                    <th class="th2" scope="row">프로젝트 수</th>
                                    <td>
                                        {{ teamLength }}개
                                    </td>
                                </tr>
                                <tr>
                                    <th class="th2" scope="row">인증번호</th>
                                    <td>
                                        {{ data.authKey }}
                                    </td>
                                </tr>

                            </tbody>
                        </table>
                    </b-form-group>
                </center>
            </form>
    </div>
</center>
</template>

<script>
import axios from 'axios';
export default {
    name: 'professorSummary',
    data() {
        return {
            data: [],
            teamLength: '',
            memberCount: 0
        }
    },
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
        axios.get('/api/subject/' + this.$route.params.subjectId + '/projects')
        .then(response => {
            this.teamLength = response.data.length
            for(let i = 0 ; i < response.data.length ; i++) {
                this.memberCount += response.data[i].memNum
            }
        })
    },
    methods: {
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