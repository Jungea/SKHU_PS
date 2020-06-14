<template>
    <div class="containerStyle">
        <center>
            <h4 style="text-align: left ; margin-left: 10%">{{ title }}의 과제 제출 현황</h4>
            <hr style="width: 80%">
            <form ref="form">
                <b-form-group label-for="SubjectStudent">
                    <table class="table table-bordered" id="SubjectStudent" v-bind="data" style="width: 80%">
                        <tbody>
                            <tr>
                                <th class="th1" style="width: 30%">프로젝트 이름</th>
                                <th class="th1" style="width: 15%">팀장</th>
                                <th class="th1">제출 파일</th>
                                <th class="th1" style="width: 10%">점수</th>
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
                                    <center><b-input value="10" style="text-align: center ; width: 70%"></b-input></center>
                                </td>
                            </tr>
                        </tbody>
                    </table>
                    <div v-if="data" style="text-align: right ; margin-right: 10%"><b-button variant="dark" @click="save()">저장</b-button></div>
                </b-form-group>
            </form>
        </center>
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
            title: ''
        }
    },
     mounted() {
         axios.get('/api/noticeBoard/submitFiles/' + this.$route.params.subjectId + '/'+this.$route.params.postId)
        .then(response => {
            this.data=response.data
        }),
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
        }
    }
}
</script>

<style>
    .th1 { background-color: rgb(243,243,243) ; text-align: center }
    .td1 { text-align: center }
    .fileItem:hover { text-decoration: underline }
</style>