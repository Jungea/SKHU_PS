<template>
    <div class="containerStyle">
        <h3 style="margin-left: 5%">{{ this.$route.params.subjectId }} 공지 게시판</h3>
        <hr style="width: 90%">
        <center>
            <b-form-group label-for="SubjectNotice">
                <table class="table table-bordered" id="SubjectNotice" style="width: 90%">
                    <tr>
                        <th class="th1">번호</th>
                        <th class="th1">제목</th>
                        <th class="th1">작성일</th>
                        <th class="th1">제출 마감일</th>
                        <th class="th1">제출 연장일</th>
                    </tr>
                    <tr v-for="(item, index) in paginatedItems" :key="index" @click="viewContent(item.postId)">
                        <td class="td1" style="width: 8%"> {{index}} </td>
                        <td style="width: 40%"> <b> {{ item.title }} </b> </td>
                        <td class="td1" style="width: 20%"> {{ item.writeTime.substring(0,10)+" "+item.writeTime.substring(11,16) }} </td>
                        <td class="td1"> {{ item.deadlineTime=='1000-01-01T00:00:00'?'-':item.deadlineTime.substring(0,10)+" "+item.deadlineTime.substring(11,16)}} </td>
                        <td class="td1"> {{ item.extentionTime=='1000-01-01T00:00:00'?'-':item.extentionTime.substring(0,10)+" "+item.extentionTime.substring(11,16) }} </td>
                    </tr>
                </table>
                <div style="text-align: right ; margin-right: 5%">
                    <b-button v-if="userType" variant="dark" v-b-modal.modal-newBoard>게시글 작성</b-button>
                </div>
            </b-form-group>
            <b-pagination style="float: right ; margin-right: 5%" @change="onPageChanged" :total-rows="totalRows" :per-page="perPage" v-model="$route.query.page" class="my-0"></b-pagination>
        </center>


     <b-modal id="modal-newBoard" size="lg"  @show="resetModal" @hidden="resetModal" title="게시글 작성" :ok-disabled="!checkForm() || checkTime()" @ok="newBoard">
        <center>
            <b-form-group label-for="newNotice" enctype="multipart/form-data">
                <table class="table table-bordered" id="newNotice" >
                    <tr>
                       <th style="width:20%">제목</th>
                            <td>
                                <b-form-input v-model.trim="title" placeholder="게시글 제목을 입력하세요."></b-form-input>
                            </td>
                    </tr>
                    <tr>
                       <th>내용</th>
                            <td>
                                <b-form-textarea rows="9" v-model="content" placeholder="게시글 내용을 입력하세요."></b-form-textarea>
                            </td>
                            
                    </tr>
                     <tr>
                       <th>파일 제출</th>
                            <td>
                                <b-form-file multiple v-model="files" class="mt-3" plain ></b-form-file>          
                                <!-- <input type="file" id="files" ref="files" multiple v-on:change="handleFilesUpload()"/> -->
                            </td>
                    </tr>
                    <tr>
                        <th>과제 제출 기한</th>
                        <td>
                            <b-input v-model="deadline" :disabled="true" style="width: 60% ; float: left ; margin-right: 15px"></b-input>
                            <b-button variant="dark" style="margin-right: 15px" v-b-modal.calendar1 @click="newNowDate()">제출 기한 선택</b-button>
                            <b-button v-if="deadline.length!=0" variant="danger" @click="deleteTime(1)">삭제</b-button>
                        </td>
                    </tr>
                    <tr>
                        <th>연장 기한</th>
                        <td>
                            <b-input v-model="extention" :disabled="true" style="width: 60% ; float: left ; margin-right: 15px"></b-input>
                            <b-button v-if="deadline.length!=0" style="margin-right: 15px" variant="dark" v-b-modal.calendar2 @click="newNowDate()">연장 기한 선택</b-button>
                            <b-button v-if="deadline.length!=0" variant="danger" @click="deleteTime(2)">삭제</b-button>
                        </td>
                    </tr>
                </table>
            </b-form-group>
        </center>
    </b-modal>

    <b-modal id="calendar1" ok-only no-close-on-backdrop>
        <b-calendar block @context="onContext1" :date-disabled-fn="dateDisabled1"></b-calendar>
    </b-modal>
    <b-modal id="calendar2" ok-only no-close-on-backdrop>
        <b-calendar block @context="onContext2" :date-disabled-fn="dateDisabled2"></b-calendar>
    </b-modal>

    </div>    
</template>
<script>
import axios from 'axios'
export default {
    name: 'noticeBoard',
    data() {
        return {
            perPage: 6, // 각 페이지마다 보이는 리스트
            currentPage: 1,
            totalRows: null,
            paginatedItems: {},
            title: '',
            content: '',
            nowDate: '',
            deadline: '',
            extention: '',
            userType: '',
            files: ''
        } 
    },
    watch: {
        '$route'(){
            //   console.log('to:'+parseInt(to.params.page)+" from:"+parseInt(from.params.page))
            console.log('query111:'+this.$route.query.page)
             axios.get('/api/noticeBoard?page='+this.$route.query.page+'&subjectId='+this.$route.params.subjectId).then(response => { // 프로젝트 이름 가져오기
                this.paginatedItems=response.data
                }).catch((erro) => {
                console.error(erro);
             });
        }
    },
    mounted() {
        if(this.currentPage==1) {
            this.$router.push({
            path: '/subject/'+this.$route.params.subjectId+'/noticeBoard',
            query:{page:1}
            })
        }
        axios.get('/api/user')
        .then(response => {
            this.userType = response.data.userType
        });
        axios.get('/api/noticeBoard?page='+this.$route.query.page+'&subjectId='+this.$route.params.subjectId).then(response => { // 프로젝트 이름 가져오기
                this.paginatedItems=response.data
                }).catch((erro) => {
                console.error(erro);
        });
        axios.get('/api/noticeListNum?subjectId='+this.$route.params.subjectId).then(response => { // 프로젝트 이름 가져오기
                this.totalRows=response.data
                }).catch((erro) => {
                console.error(erro);
        });
    },
    methods: {
        resetModal() {
            this.title = '',
            this.content = '',
            this.deadline = '',
            this.extention = '',
            this.nowDate = ''
        },
        checkForm() {
            if(this.title && this.content)
                return true
            else
                return false
        },
        checkTime() {
            let date1 = new Date(this.deadline)
            let date2 = new Date(this.extention)
            if(date1 >= date2)
                return true
            else
                return false
        },
        newBoard() {
            let formData = new FormData();
                // for( var i = 0; i < this.files.length; i++ ){
                //     alert('i:'+i)
                //     let f = this.files[i];
                //     formData.append('file[' + i + ']', f);
                // }
            
            axios.post('/api/writeNotice', {
                    subjectId:this.$route.params.subjectId,
                    title:this.title,
                    content:this.content,
                    deadlineTime:this.deadline,
                    extensionTime:this.extention,
                }).then(response => {
                alert('id:'+response.data)
                let newPostId=response.data
                for(let i=0;i<this.files.length;i++) {
                    formData.append("file", this.files[i]);
                }
                axios.post('/api/file1/upload/'+newPostId, 
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
            
                if(this.$route.query.page!=1) {
                    this.$router.push({
                        path: '/subject/'+this.$route.params.subjectId+'/noticeBoard',
                        query:{page:1}
                    })
                } else {
                    this.$router.go()
                }
            })
        },
        handleFilesUpload(){
            this.files  = this.$refs.files.files;
        },
        dateDisabled1(ymd, date) {
            let d = date;
            let date2 = this.leadingZeros(d.getFullYear(), 4) + '-' + this.leadingZeros(d.getMonth() + 1, 2) + '-' + this.leadingZeros(d.getDate(), 2);
            return date2 < this.nowDate
        },
        dateDisabled2(ymd, date) {
            let d = date;
            let date2 = this.leadingZeros(d.getFullYear(), 4) + '-' + this.leadingZeros(d.getMonth() + 1, 2) + '-' + this.leadingZeros(d.getDate(), 2);
            return date2 <= this.deadline
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
            this.nowDate = this.leadingZeros(d.getFullYear(), 4) + '-' + this.leadingZeros(d.getMonth() + 1, 2) + '-' + this.leadingZeros(d.getDate(), 2);
            if(this.deadline == '')
                this.deadline = this.nowDate
        },
        onContext1(ctx) {
            this.deadline = ctx.activeYMD;
        },
        onContext2(ctx) {
            this.extention = ctx.activeYMD;
        },
        viewContent(postId) {
            this.$router.push({
                path: '/subject/'+this.$route.params.subjectId+'/noticeBoard/' + postId
            })
        },
        paginate (page_size, page_number) {
         this.$router.push({
            path: '/subject/'+this.$route.params.subjectId+'/noticeBoard',
            query:{page:page_number+1}
          })
        },
        onPageChanged(page){
            this.currentPage=this.$route.query.page
            this.paginate(this.perPage, page - 1)
        },
        deleteTime(id) {
            if(id == 1) {
                this.deadline = ''
                this.extention = ''
            }
            else
                this.extention = ''
        }
    }
}
</script> 