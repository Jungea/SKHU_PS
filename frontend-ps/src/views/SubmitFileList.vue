<template>
    <div class="scoreStyle">
        <b-row>
            <b-col style="background ;margin-right: -100px">
            <center>
                <h4 style="text-align: left ; margin-left: 20%">{{ title }}의 과제 제출 현황</h4>
                <hr style="width: 60%">
                <form ref="form">
                    <b-form-group label-for="SubjectStudent">
                        <table class="table table-bordered" id="SubjectStudent" v-bind="data" style="width: 60%">
                            <tbody>            
                                <tr>
                                    <th class="th1" style="width: 30%">프로젝트 이름</th>
                                    <th class="th1" style="width: 13%">팀장</th>
                                    <th class="th1">제출 파일</th>
                                    <th class="th1" style="width: 12%">점수</th>
                                    <th class="th1" style="width: 5%">개별</th>
                                </tr>
                                <tr :key="index" v-for="(item, index) in data">
                                    <td class="td1" style="vertical-align: middle">{{ item.project.projectName }}</td>
                                    <td class="td1" style="vertical-align: middle">{{ item.project.user.name }}</td>
                                    <td class="td1" style="vertical-align: middle">
                                        <div v-for="(file, index) in item.files" :key="index" class="fileItem" style="cursor:pointer ; color:blue" @click="download(file)">
                                            <div>{{ file.name }}</div>
                                        </div>
                                    </td>
                                    <td class="td1">
                                        <center><b-input style="text-align: center ; width: 70%"></b-input></center>
                                    </td>
                                    <td class="td1" rowspan="99" v-if="index == 0" style="vertical-align: middle">
                                        <div style="padding-left: 10px ; padding-top: 5px">
                                            <b-checkbox size="lg" v-model="viewToggle"></b-checkbox>
                                        </div>
                                    </td>
                                </tr>
                            </tbody>
                        </table>
                        <div v-if="data" style="text-align: right ; margin-right: 20%"><b-button variant="dark" @click="save()">저장</b-button></div>
                    </b-form-group>                
                </form>
            </center>
            </b-col>
            <b-col v-if="viewToggle" style="margin-top: 4%; margin-left: -10% ; background" cols="3">
                <b-collapse id="member"  v-model="viewToggle" style="float: right ; overflow-y: scroll ; max-height: 490px ; border: 1px solid lightgray">
                <b-card>
                    <table class="table table-bordered">
                        <tr v-for="(item, index) in memberList" :key="index">
                            <td class="th1" style="vertical-align: middle">{{ item.project.projectName }}</td>
                            <td style="vertical-align: middle ; text-align: center ; width: 30%">{{ item.user.name }}</td>
                            <td style="width: 25% ; vertical-align: middle"><b-input style="; text-align: center"></b-input></td>
                        </tr>
                    </table>
                    <div style="text-align: right">
                        <b-button @click="personal()" variant="dark">개별 저장</b-button>
                    </div>
                </b-card>
            </b-collapse>
            </b-col>
        </b-row>
    </div>
</template>

<script>
import axios from 'axios';
export default {
    name: 'score',
    data() {
        return {
            data: [],
            files: [],
            title: '',
            viewToggle: false,
            memberList: []
        }
    },
     mounted() {
         axios.get('/api/noticeBoard/submitFiles/' + this.$route.params.subjectId + '/'+this.$route.params.postId)
        .then(response => {
            this.data=response.data
        }),
        axios.get('/api/subject/' + this.$route.params.subjectId + '/member?sort=project')
        .then(respon => {
            this.memberList = respon.data
        }) 
        // axios.get('/api/subject/' + this.$route.params.subjectId + '/projects')
        // .then(response => {
        //     this.data = response.data
        //     for(let i = 0 ;i < response.data.length ; i++)
        //         this.files.push('2017320 ' + this.data[i].user.name + '.txt')
        // }),
        axios.get('/api/noticeBoard/post/'+this.$route.params.postId) // 게시글 정보
        .then(response => {
            this.title = response.data.title
        });
    },
    methods: {
        download(file) {
            // axios.get('/api/file1/download/'+file.fileId).then(response => {
            //     var fileURL = window.URL.createObjectURL(new Blob([response.data]));
            //     var fileLink = document.createElement('a');
            //     fileLink.href = fileURL;
            //     fileLink.setAttribute('download', file.name);
            //     document.body.appendChild(fileLink);
            //     fileLink.click();
            // })
            axios({
                method: 'GET',
                url: '/api/file1/download/'+file.fileId,                 
                responseType: 'blob' // 가장 중요함
            }).then(response => {
                var fileURL = window.URL.createObjectURL(new Blob([response.data]));
                var fileLink = document.createElement('a');
                fileLink.href = fileURL;
                fileLink.setAttribute('download', file.name);
                document.body.appendChild(fileLink);
                fileLink.click();
            })    
        },
        save() {
            alert("점수가 저장되었습니다.")
            // axios.get()
            // .then()
        },
        personal() {
            alert("개별 점수가 저장 되었습니다.")
        }
    }
}
</script>

<style>
    .th1 { background-color: rgb(243,243,243) ; text-align: center }
    .td1 { text-align: center }
    .fileItem:hover { text-decoration: underline }
    .scoreStyle {
        margin: 100px 100px 100px 0;
        min-width: 350px;
    }
</style>