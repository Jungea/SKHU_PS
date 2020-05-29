<template>
    <div class="containerStyle">
        <h3 style="margin-left: 5%">공지 게시판</h3>
        <hr style="width: 90%">
        <center>
            <div>
            <b-form-group label-for="SubjectNotice">
                <table class="table table-bordered" id="SubjectNotice" style="width: 90%">
                    <tr>
                        <th class="th1" style="width: 20%">제목</th>
                        <td>{{list.title}}</td>
                    </tr>
                    <tr>
                        <th class="th1" style="width: 20%">내용</th>
                        <td>{{list.content}}</td>
                    </tr>
                    <tr>
                        <th class="th1">작성일</th>
                        <td>{{ list.writeTime.substring(0,10)+" "+list.writeTime.substring(11,16) }}</td>
                    </tr>
                    <tr v-if="checkFile">
                        <th class="th1">첨부 파일</th> <!-- 교수님이 올리신 파일 -->
                        <td>
                            <div v-for="(item, index) in files" :key="index" style="cursor:pointer;color:blue" @click="download(item)">
                                {{ item.name }}
                            </div>
                        </td>
                    </tr>
                     <tr>
                        <th class="th1">제출 기한</th>
                        <td>{{ list.deadlineTime=='1000-01-01T00:00:00'?'-':list.deadlineTime.substring(0,10)+" "+list.deadlineTime.substring(11,16)}}</td>
                    </tr>
                    <tr>
                        <th class="th1">연장 기한</th>
                        <td>{{ list.extentionTime=='1000-01-01T00:00:00'?'-':list.extentionTime.substring(0,10)+" "+list.extentionTime.substring(11,16) }}</td>
                    </tr>
                    <tr>
                        <th class="th1">제출 여부</th>
                        <td v-if="removeFileId.length!=myProjectFiles.length">{{list.deadlineTime=='1000-01-01T00:00:00'?'-':myProjectFiles.length!=0?'O':'X'}}</td>
                        <td v-if="removeFileId.length==myProjectFiles.length">{{list.deadlineTime=='1000-01-01T00:00:00'?'-':'X'}}</td>
                    </tr>
                </table>
                 <table class="table table-bordered" style="width: 90%" v-if="!userType">
                    <tr>
                        <th class="th1" style="width: 15% ; vertical-align: middle">제출 파일</th>
                        <td>
                            <div style="float: left">
                                <div v-for="(item, index) in myProjectFiles" :key="index" style="cursor:pointer;color:blue"> <!--기존 파일-->
                                    <div v-if="!removeFileId.includes(item.fileId)">
                                        <span @click="download(item)">{{ item.name }}</span>
                                        <b-icon-x v-show="submitFilePossible" style="cursor:pointer;color:red" @click="removeFile(item.fileId,index)" font-scale="1.5"></b-icon-x>
                                    </div>
                                </div>
                                <b-form-file  v-show="submitFilePossible"  multiple v-model="myProjectNewFiles" class="mt-3" plain ></b-form-file>
                            </div>
                           
                        </td>
                    </tr>
                </table>
               <div style="text-align: left">
                        <div style="display: inline-block ; margin-left: 5%">
                            <b-button @click="viewComment()" v-if="!checkComment" variant="dark">댓글 보기</b-button>
                            <b-button @click="viewComment()" v-if="checkComment" variant="dark">댓글 접기</b-button>
                        </div>
                        <div style="display: inline-block ; margin-left: 10px">
                            <b-button v-if="userType" @click="viewFile()" variant="dark">제출물 보기</b-button>
                        </div>
                        <div style="display: inline-block ; margin-right: 5% ; float: right">
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
                        <td> <div> <b> {{ item.user.name }} </b> <span style="color: #9A9A9A"> ({{ item.writeTime.substring(0,10)+" "+item.writeTime.substring(11,16) }}) </span> </div>
                             <div style="margin-top: 3px"> {{ item.content }} </div>
                             <b-button variant="danger" v-if="item.user.userId==userId" @click="deleteComment(item.commentId)">삭제</b-button>
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
    name: 'StudentNoticeContent',
    data() {
        return {
            list:{},
            comment: {},
            checkComment: true,
            file1: '공지사항.docx',
            file2: [], // 내가 올린 파일
            content: '',
            checkFile: true,
            userType: '',
            userName: '',
            nFile: '', // 새로 올릴 파일
            nFileList: [], // 새로 올릴 파일 목록          
            userId:null,  
            files:null,
            myProjectFiles:null, // 프로젝트가 제출한 기존 파일
            myProjectNewFiles:null, // 프로젝트가 새롭게 제출할 파일
            removeFileId:[],
            removeItem:null,
            submitFilePossible:true, // 파일 제출 가능여부
        }
    },
    watch: {
        myProjectNewFiles() {
            let formData = new FormData();
            for(let i=0;i<this.myProjectNewFiles.length;i++) {
                    formData.append("file", this.myProjectNewFiles[i]);
                }
            axios.post('/api/file1/upload/'+this.$route.params.postId+'/'+this.$route.params.projectId, 
                    formData,
                    {
                        headers: {
                            'Content-Type': 'multipart/form-data'
                        }
                    }
                )
                .then(response => {
                    console.log(response.data)
            })
            this.$router.go()
            
        },
    },
    mounted() { 
         axios.get('/api/file1/list/'+this.$route.params.postId) 
        .then(response => {
            this.files=response.data
        });
        axios.get('/api/file1/list/'+this.$route.params.postId+'/'+this.$route.params.projectId) // 학생이 제출한 파일 목록 
        .then(response => {
            this.myProjectFiles=response.data
        });
        axios.get('/api/noticeBoard/post/'+this.$route.params.postId) // 모든 과목 정보
        .then(response => {
            this.list=response.data
            if(this.list.deadlineTime=='1000-01-01T00:00:00') { // 제출 기한이 없다면
                this.submitFilePossible=false
            } else if(this.list.deadlineTime!='1000-01-01T00:00:00' && this.list.extentionTime=='1000-01-01T00:00:00') { // 제출기한은 있는데 연장기한이 없다면
                let today=new Date()
                let deadline=new Date()
                deadline.setFullYear(Number(this.list.extentionTime.substring(0,4)),Number(this.list.extentionTime.substring(5,7))-1,Number(this.list.extentionTime.substring(8,10)))
                if(today.getFullYear()>deadline.getFullYear()) {
                    this.submitFilePossible=false
                } else if(today.getFullYear()==deadline.getFullYear()) {
                    if(today.getMonth()>deadline.getMonth()) {
                        this.submitFilePossible=false
                    } else if(today.getMonth()==deadline.getMonth()) {
                        if(today.getDate()>deadline.getDate()) {
                            this.submitFilePossible=false
                        } else if(today.getDate()==deadline.getDate()) {
                             this.submitFilePossible=false
                        }
                    }
                }
                
                
            } else if(this.list.deadlineTime!='1000-01-01T00:00:00' && this.list.extentionTime!='1000-01-01T00:00:00') { // 제출기한과 연장기한이 모두 있다면
                let today=new Date()
                let extention=new Date()
                extention.setFullYear(Number(this.list.extentionTime.substring(0,4)),Number(this.list.extentionTime.substring(5,7))-1,Number(this.list.extentionTime.substring(8,10)))
                if(today.getFullYear()>extention.getFullYear()) {
                    this.submitFilePossible=false
                } else if(today.getFullYear()==extention.getFullYear()) {
                    if(today.getMonth()>extention.getMonth()) {
                        this.submitFilePossible=false
                    } else if(today.getMonth()==extention.getMonth()) {
                        if(today.getDate()>extention.getDate()) {
                            this.submitFilePossible=false
                        } else if(today.getDate()==extention.getDate()) {
                             this.submitFilePossible=false
                        }
                    }
                }
                
            
                
            } 
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
        viewComment() {
            if(this.checkComment)
                this.checkComment = false;
            else
                this.checkComment = true;
        },
        viewFile() {
            if(this.file == '')
                alert("제출물이 없습니다.")
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
        leadingZeros(n, digits) {
            var zero = '';
            n = n.toString();

            if (n.length < digits) {
                for (var i = 0; i < digits - n.length; i++)
                    zero += '0';
            }
                return zero + n;
        },
        newNowDate() {
            let d = new Date();
            return this.leadingZeros(d.getFullYear(), 4) + '-' + this.leadingZeros(d.getMonth() + 1, 2) + '-' + this.leadingZeros(d.getDate(), 2) + ' ' + this.leadingZeros(d.getHours(), 2) + ':' +
                    this.leadingZeros(d.getMinutes(), 2);
        },
        viewList() {
            this.$router.push({
                path: '/project/' + this.$route.params.projectId+'/noticeBoard',
                quert:{page:1}
            })
        },
        addFile() {
            if(this.nFile == '')
                alert("파일을 등록해주세요.")
            else
                this.nFileList.push(this.nFile);
        },
        resetModal() {
            this.nFile = ''
            this.nFileList = []
        },
        save() {
            for(var i = 0 ; i < this.nFileList.length ; i++)
                this.file2.push(this.nFileList[i])
        },
        removeFile(fileId) {
            this.removeFileId.push(fileId)
            axios.post('/api/file1/delete/'+String(fileId), {
                }).then(response => {
                     response.data
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
    },
}
</script>