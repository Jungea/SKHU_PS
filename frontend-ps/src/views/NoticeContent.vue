<template>
    <div class="containerStyle">
        <h3 style="margin-left: 5%">공지 게시판</h3>
        <hr style="width: 90%">
        <center>
            <div>
            <b-form-group label-for="SubjectNotice">
                <table class="table table-bordered" id="SubjectNotice" style="width: 90%">
                    <tr>
                        <th class="th1" style="width: 14%">제목</th>
                        <td>{{list.title}}</td>
                    </tr>
                    <tr>
                        <th class="th1">작성일</th>
                        <td>{{ list.writeTime }}</td>
                    </tr>
                    <tr>
                        <td colspan="2" style="height: 300px">{{ list.content }}</td>
                    </tr>
                    <tr>
                        <th class="th1" style="vertical-align: middle">첨부 파일</th> <!-- 교수님이 올리신 파일 -->
                        <td>
                            <div v-for="(item, index) in files" :key="index">
                                <span class="file" style="cursor: pointer ; color: blue" @click="download(item)">{{ item.name }}</span>
                            </div>
                        </td>
                    </tr>
                     <tr>
                        <th class="th1">제출 기한</th>
                        <td>{{ list.deadlineTime=='1000-01-01T00:00:00' ? '-' : deadlineTime }}</td>
                    </tr>
                    <tr>
                        <th class="th1">연장 기한</th>
                        <td>{{ list.extentionTime=='1000-01-01T00:00:00' ? '-' : extentionTime }}</td>
                    </tr>
                </table>
                <div style="text-align: left">
                        <div style="display: inline-block ; margin-left: 5%">
                            <b-button @click="viewComment()" v-if="!checkComment" variant="dark">댓글 보기</b-button>
                            <b-button @click="viewComment()" v-if="checkComment" variant="dark">댓글 접기</b-button>
                        </div>
                        <div style="display: inline-block ; margin-left: 10px">
                            <b-button v-if="userType" @click="viewFileList()" variant="dark">제출물 보기</b-button>
                        </div>
                        <div style="display: inline-block ; margin-right: 5% ; float: right">
                             <b-button class="mr-3" v-if="userType" @click="deletePost()" variant="danger">삭제</b-button>
                            <b-button style="margin-right: 10px" v-if="userType" @click="edit()" variant="dark">수정</b-button>
                            <b-button @click="viewList()" variant="dark">목록으로</b-button>
                        </div>
                </div>
            </b-form-group>
            </div>

            <b-form-group v-if="checkComment" style="margin-top: -15px">
                <hr style="width: 90% ; border: 1px solid #ccc">
                <table class="table table-bordered" style="width: 90%">
                    <tr v-for="(item, index) in comment" :key="index">
                        <td> <div> <b> {{ item.user.name }} </b> <span style="color: #9A9A9A"> ({{ item.writeTime.substring(0,10)+" "+item.writeTime.substring(11,16) }}) </span>
                        <b-icon-x scale="1.5" v-if="item.user.userId==userId" @click="deleteComment(item.commentId)" style="color: red ; float: right ; cursor: pointer"></b-icon-x>
                         </div>
                             <div style="margin-top: 3px"> {{ item.content }} </div>
                        </td>
                    </tr>
                    <tr>
                        <td>
                            <b-textarea trim v-model="content" rows="2" style="height: 80px ; min-height: 80px ; max-height: 150px"></b-textarea>
                            <b-button :disabled="content.length==0" @click="addComment()" style="margin-top: 10px ; float: right ; background: #9A9A9A ; border-color: #9A9A9A">작성</b-button>
                        </td>
                    </tr>
                </table>
            </b-form-group>
        </center>
    </div>
</template>

<script>
import axios from 'axios'
export default {
    name: 'noticeContent',
    data() {
        return {
            list:{},
            comment: {},
            checkComment: true,
            file2: [], // 내가 올린 파일
            content: '',
            userType: '',
            userName: '',
            nFile: '', // 새로 올릴 파일
            nFileList: [], // 새로 올릴 파일 목록          
            userId:null,  
            files:null,
            deadlineTime: '',
            extentionTime: ''
        }
    },
    mounted() { 
         axios.get('/api/file1/list/'+this.$route.params.postId) 
        .then(response => {
            this.files=response.data
        });
        axios.get('/api/noticeBoard/post/'+this.$route.params.postId) // 게시글 정보
        .then(response => {
            this.list=response.data
            this.list.writeTime = this.list.writeTime.substring(0,10)+" "+this.list.writeTime.substring(11,16)
            this.deadlineTime = this.list.deadlineTime.substring(0,10)+" "+this.list.deadlineTime.substring(11,16)
            this.extentionTime = this.list.extentionTime.substring(0,10)+" "+this.list.extentionTime.substring(11,16)
        });
        axios.get('/api/user')
        .then(response => {
            this.userType = response.data.userType
            this.userName = response.data.name
            this.userId=response.data.userId
        })
        axios.get('/api/noticeBoard/comment/'+this.$route.params.postId)
        .then(response => {
            this.comment=response.data
        })
        this.postId = this.$route.params.postId
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
        edit() {
            this.$router.push({
                path: '/subject/'+ this.$route.params.subjectId+'/noticeBoard/'+ this.postId + '/edit'
            })
        },
        viewComment() {
            if(this.checkComment)
                this.checkComment = false;
            else
                this.checkComment = true;
        },
        addComment() {
            axios.post('/api/noticeBoard/addComment/'+this.$route.params.postId, {
                content:this.content
            })
            .then(response => {
                console.log(response.data)
                this.$router.go()
            })
        },
        viewList() {
            this.$router.push({
                path: '/subject/' + this.$route.params.subjectId+'/noticeBoard',
                quert:{page:1}
            })
        },
        deleteComment(commentId) {
            let result=confirm('삭제하시겠습니까?')
            if(result) {
                axios.post('/api/noticeBoard/deleteComment/'+commentId, {})
                .then(response => {
                    console.log(response.data)
                    this.$router.go()
                })
            }
        },
        deletePost() {
            let result=confirm('삭제하시겠습니까?')
            if(result) {
                axios.post('/api/noticeBoard/deletePost', {
                    postId:this.$route.params.postId
                })
                .then(response => {
                    console.log(response.data)
                    this.$router.push({
                        path: '/subject/'+this.$route.params.subjectId+'/noticeBoard',
                        query:{page:1}
                    })
                })
            }
        },
        viewFileList() {
            this.$router.push({
                path: '/subject/' + this.$route.params.subjectId + '/noticeBoard/' + this.$route.params.postId + '/fileList'
            })
        }
    },
}
</script>

<style scoped>
    .file:hover { text-decoration: underline }
</style>