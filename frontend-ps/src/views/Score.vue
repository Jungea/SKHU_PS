<template>
    <div class="scoreContainerStyle">
        <center>
            <h3 style="display: inline-block">프로젝트 점수</h3>
            <hr style="width: 50%">
            <form ref="form">
                <b-form-group label-for="SubjectStudent" style="width: 50%">
                    <table v-if="!view" class="table table-bordered" id="SubjectStudent" v-bind="this.data">
                        <tbody>
                            <tr>
                                <th class="th1">학번</th>
                                <th class="th1">학년</th>
                                <th class="th1">이름</th>
                                <th class="th1">학과</th>
                            </tr>
                            <tr @click="studentScore(item.user)" :key="index" v-for="(item, index) in data">
                                <td class="td1"> {{ item.user.userNum }} </td>
                                <td class="td1"> {{ item.user.grade }} </td>
                                <td class="td1"> {{ item.user.name }} </td>
                                <td class="td1"> {{ item.user.detDepartment.detName }} </td>
                            </tr>
                        </tbody>
                    </table>
                    <table v-if="view" class="table table-bordered" id="SubjectStudent" v-bind="this.data">
                        <tbody>
                            <tr>
                                <th class="th1">프로젝트 이름</th>
                                <th class="th1">팀장</th>
                            </tr>
                            <tr @click="teamReport(item)" :key="index" v-for="(item, index) in team">
                                <td class="td1"> {{ item.projectName }} </td>
                                <td class="td1"> {{ item.user.name }} </td>
                            </tr>
                        </tbody>
                    </table>
                    <!-- <div style="text-align: right">
                        <b-button v-if="!view" variant="dark" @click="viewTeam()">팀별 제출물 확인</b-button>
                        <b-button v-if="view" variant="dark" @click="viewTeam()">개인별 점수 확인</b-button>
                    </div> -->
                </b-form-group>
            </form>
        </center>

    <b-modal id="modal-score" size="lg" ok-only @show="resetModal" @hidden="resetModal">
        <center>
            <h3 class="mt-3"> {{ userData.userNum }} | {{ userData.name }} | 점수 </h3>
            <hr style="border: 1px solid black ; width: 60% ; margin-bottom: 30px">
            <b-container style="width: 70%">
                <b-row>
                    <table class="table table-bordered">
                        <tr>
                            <th class="th1">제출한 공지 게시글</th>
                            <th class="th1">배점</th>
                            <th class="th1">점수</th>
                        </tr>
                        <tr style="text-align: center" :key="index" v-for="(data, index) in s">
                            <td style="width: 60% ; text-align: center"> {{ data.name }} </td>
                            <td class="td1"> {{ data.max }} </td>
                            <td class="td1" v-if="!editVisible"> {{ data.score }} </td>
                            <td class="td1" v-if="editVisible" style="padding: 5px 0 0 0 ; width: 20%">
                                <center>
                                    <b-input v-model="data.score" style="width: 50% ; text-align: center"></b-input>
                                </center>
                            </td>
                        </tr>
                    </table>

                    <div style="text-align: right ; width: 100%">
                        <b-button v-if="!editVisible" variant="dark" @click="editScore()">입력</b-button>
                        <b-button v-if="editVisible" variant="dark" @click="editScore()">저장</b-button>
                    </div>
                </b-row>
            </b-container>
        </center>
    </b-modal>

    <b-modal id="modal-team" size="lg" ok-only>
        <center>
            <h3 class="mt-3"> <b> {{ teamData.projectName }} </b> 팀 제출물</h3>
            <hr style="border: 1px solid black ; width: 80% ; margin-bottom: 30px">
            <b-container style="width: 90%">
                <b-row>
                    <table class="table table-bordered">
                        <tr>
                            <th class="th1">제출한 공지 게시글</th>
                            <th class="th1">제출 파일</th>
                            <th class="th1">점수</th>
                        </tr>
                        <tr style="text-align: center" :key="index" v-for="(data, index) in s2">
                            <td style="text-align: center"> {{ data.name }} </td>
                            <td class="td1" style="width: 44%"> {{ data.file }} </td>
                            <td class="td1" style="width: 12%">{{data.score}}/{{data.max}}</td>
                        </tr>
                    </table>
                </b-row>
            </b-container>
        </center>
      </b-modal>


    </div>
</template>

<script>
import axios from 'axios';
export default {
    name: 'score',
    data() {
        return {
            data: [],
            userData: [],
            s: [ { name: '게시글이름1(해당 게시글로 이동?)', max: 20, score: 20 }, { name: '게시글이름2', max: 10, score: 9 }, { name: '3주차 진행상황 제출하세요.', max: 10, score: 9 }, { name: '4주차 진행상황 제출', max: 10, score: 8 },
            { name: '5주차 진행상황', max: 10, score: 10 }, { name: '6주차 ㅈㅎㅅㅎ', max: 10, score: 9 }, { name: '7주차 ㅈㅎ', max: 10, score: 8 }, { name: '8차 ㅈㅎ', max: 10, score: 9 },
            { name: '9 ㅈㅊ', max: 10, score: 10 }, { name: '10', max: 15, score: 14 }],
            editVisible: false,
            view: false,
            team: [],
            teamData: [],
            s2: [ { name: '공지1', max: 20, score: 20, file: 'SKHU-PS_계획서.docx' }, { name: '공지2', max: 10, score: 9, file: 'SKHU-PS_1주차_진행률.xlsx' }, { name: '공지3', max: 10, score: 9, file: 'SKHU-PS_2주차_진행률.xlsx' }, { name: '공지4', max: 10, score: 8, file: 'SKHU-PS_3주차_진행률.xlsx' },
            { name: '공지5', max: 10, score: 10, file: 'SKHU-PS_릴리즈1_중간점검.xlsx' } ]
        }
    },
    mounted() {
        axios.get('/api/subject/' + this.$route.params.subjectId + '/member?sort=project')
        .then(response => {
            this.data = response.data
        }),
        axios.get('/api/subject/' + this.$route.params.subjectId + '/projects')
        .then(response => {
            this.team = response.data
        });
    //   axios.get('/api/subject/' + this.$route.params.subjectId + '/projects')
    //   .then(response => {
    //       this.data = response.data
    //       for(var i = 0 ; i < response.data.length ; i++) {
    //         axios.get('/api/project/' + response.data[i].projectId + '/allMember')
    //         .then(res => {
    //             if(res.data.length != 0) {
    //                 for(let j = 0 ; j < res.data.length ; j++)
    //                     this.a.push(res.data[j].user)
    //             }
    //         })
    //       }
    //   })  
    },
    methods: {
        studentScore(item) {
            this.userData = item;
            // alert(this.userData.name)
            this.$root.$emit('bv::show::modal', 'modal-score') 
        },
        editScore() {
            if(this.editVisible) {
                this.editVisible = false;
                alert("저장되었습니다.")
            }
            else
                this.editVisible = true;
        },
        resetModal() {
            this.editVisible = false;
        },
        // viewTeam() {
        //     if(this.view)
        //         this.view = false;
        //     else   
        //         this.view = true;
        // },
        teamReport(item) {
            this.teamData = item;
            alert(this.teamData.projectName)
            this.$root.$emit('bv::show::modal', 'modal-team')
        },
    }
}
</script>

<style>
    .th1 { background-color: rgb(243,243,243) ; text-align: center }
    .td1 { text-align: center }
    .scoreContainerStyle {
        margin: 50px;
        min-width: 350px;
    }
    .scoreContainerStyle h3{
        font-weight: bold;
    }
</style>