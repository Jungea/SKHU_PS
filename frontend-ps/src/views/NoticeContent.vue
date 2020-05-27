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
                        <th class="th1">작성일</th>
                        <td>{{ list.startDate }}</td>
                    </tr>
                    <tr v-if="checkFile">
                        <th class="th1">첨부 파일</th> <!-- 교수님이 올리신 파일 -->
                        <td><a href="#" style="color: black">{{ file1 }}</a></td>
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
                        <td>{{list.deadlineTime=='1000-01-01T00:00:00'?'-':'X'}}</td>
                    </tr>
                </table>
                 <table class="table table-bordered" style="width: 90%" v-if="!userType">
                    <tr>
                        <th class="th1" style="width: 15% ; vertical-align: middle">제출 파일</th>
                        <td>
                            <div style="float: right">
                                <b-icon-plus style="cursor:pointer" v-b-modal.modal-file font-scale="1.5"></b-icon-plus>
                                <b-icon-x style="cursor:pointer" @click="resetFile()" font-scale="1.5"></b-icon-x>
                            </div>
                            <div v-for="(item, index) in file2" :key="index"><a href="#" style="color: black">{{ item.toString() }}</a></div>
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
                        <td> <div> <b> {{ item.name }} </b> <span style="color: #9A9A9A"> ({{ item.date }}) </span> </div>
                             <div style="margin-top: 3px"> {{ item.content }} </div>
                        </td>
                    </tr>
                    <tr>
                        <td>
                            <b-textarea trim v-model="content" rows="2" style="height: 80px ; min-height: 80px ; max-height: 150px"></b-textarea>
                            <b-button :disabled="content==''" @click="addComment()" style="margin-top: 10px ; float: right ; background: #9A9A9A ; border-color: #9A9A9A">작성</b-button>
                        </td>
                    </tr>
                </table>
            </b-form-group>
        </center>

       <b-modal id="modal-file" ref="modal" size="lg"  @show="resetModal" no-close-on-backdrop no-close-on-esc title="파일 첨부" @ok="save()">
                <form ref="form">
                    <b-form-group>
                        <table class="table table-bordered">
                            <tr>
                                <th class="th1" style="width: 20% ; vertical-align: middle">파일 첨부</th>
                                <td>
                                    <b-input class="mt-2" :disabled="false" trim style="width: 60% ; display: inline" v-model="nFile"></b-input>
                                    <div class="mt-2" style="float: right"><b-button @click="addFile()">추가</b-button></div>
                                    <div v-for="(item, index) in nFileList" :key="index" style="margin-top: 15px">{{ item.toString() }} <b-icon-x @click="removeFile(index)"></b-icon-x></div>
                                </td>
                            </tr>
                        </table>
                    </b-form-group>
                </form>
        </b-modal>
    </div>
</template>

<script>
import axios from 'axios'
export default {
    name: 'noticeContent',
    data() {
        return {
            list:{},
            comment: [
                { commentId: '1', name: '길보미', date: '2020-05-30 15:32', content: '알겠습니다.'},
                { commentId: '2', name: '김소연', date: '2020-05-30 15:35', content: '교수님 과제는 어디에다 제출해야 하나요?'},
                { commentId: '3', name: '이영선', date: '2020-05-31 05:21', content: '넵 알겠습니다.'},
                { commentId: '4', name: '이윤영', date: '2020-05-31 12:32', content: '교수님 메일로 보내드리려했는데 메일 주소가 없다고 나옵니다. 확인 부탁드립니다.'},
                { commentId: '5', name: '정은애', date: '2020-05-31 23:01', content: '교수님 강의 동영상은 언제 올려주시나요...?'},
            ],
            checkComment: true,
            file1: '공지사항.docx',
            file2: [], // 내가 올린 파일
            content: '',
            checkFile: true,
            userType: '',
            userName: '',
            nFile: '', // 새로 올릴 파일
            nFileList: [] // 새로 올릴 파일 목록            
        }
    },
    mounted() { 
        axios.get('/api/noticeBoard/post/'+this.$route.params.postId) // 모든 과목 정보
        .then(response => {
            this.list=response.data
            
        });
        axios.get('/api/user')
        .then(response => {
            this.userType = response.data.userType
            this.userName = response.data.name
        })
        this.postId = this.$route.params.postId
    },
    methods: {
        edit() {
            this.$router.push({
                path: '/noticeBoard/' + this.$route.params.subjectId + '/notice/' + this.postId + '/edit'
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
                let nowDate = this.newNowDate()
                let newComment = { commentId: this.comment.length + 1, name: this.userName, date: nowDate, content: this.content }
                this.comment.push(newComment)
                this.content = ''
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
                path: '/noticeBoard/' + this.$route.params.subjectId
            })
        },
        resetFile() {
            this.file2 = []
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
        removeFile(index) {
            alert(index)
            this.nFileList.splice(index, 1)
        }
    },
}
</script>