<template>
    <div class="containerStyle">
        <h3 style="margin-left: 5%">자유 게시판</h3>
        <hr style="width: 90%">
        <!--게시글 목록 테이블-->
        <center>
            <b-form-group label-for="SubjectNotice">
                <table class="table table-bordered table-hover" id="SubjectNotice" style="width: 90%">
                    <thead>
                        <tr>
                            <th class="th1">제목</th>
                            <th class="th1">작성자</th>
                            <th class="th1">좋아요</th>
                            <th class="th1">작성일</th>
                        </tr>
                    </thead>
                    <tbody>
                        <tr v-for="(item, index) in paginatedItems" :key="index" @click="viewContent(item.postId)">
                            <td class="td2" style="width: 40%; cursor:pointer"> <b> {{ item.title }}[{{commentNum[index]}}] </b> </td>
                            <td class="td1" style="width: 10%; cursor:pointer"> <b> {{item.user.name}}</b> </td>
                            <td class="td1" style="width: 10%; cursor:pointer"> <b> {{likeNum[index]}}</b> </td>
                            <td class="td1" style="width: 20%"> {{ item.writeTime.substring(0,10)+" "+item.writeTime.substring(11,16) }} </td>
                        </tr>
                    </tbody>
                </table>
                <div style="text-align: right ; margin-right: 5%">
                    <b-button variant="dark" v-if="isjoinMember" v-b-modal.modal-newBoard>게시글 작성</b-button>
                </div>
            </b-form-group>
            <b-pagination style="float: right ; margin-right: 5%" @change="onPageChanged" :total-rows="totalRows" :per-page="perPage" v-model="$route.query.page" class="my-0"></b-pagination>
        </center>

        <!--게시글 작성 모달-->
        <b-modal id="modal-newBoard" size="lg"  @show="resetModal" @hidden="resetModal" title="게시글 작성" :ok-disabled="!checkForm() || checkTime()" @ok="newBoard">
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
                        <th>파일 업로드</th>
                        <td>
                            <b-form-file multiple v-model="files" class="mt-3" plain ></b-form-file>          
                            <!-- <input type="file" id="files" ref="files" multiple v-on:change="handleFilesUpload()"/> -->
                        </td>
                    </tr>
                </table>
            </b-form-group>
        </b-modal>

        <!--날짜선택-->
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
    name: 'freeBoard',
    data() {
        return {
            perPage: 6, // 각 페이지마다 보이는 리스트
            currentPage: 1,
            totalRows: null,
            paginatedItems: {},
            title: '',
            content: '',
            nowDate: '',
            userType: '',
            files: [],
            isjoinMember:true,
            commentNum:[],
            likeNum:[],
        } 
    },
    watch: {
        '$route'(){
            //   console.log('to:'+parseInt(to.params.page)+" from:"+parseInt(from.params.page))
            console.log('query111:'+this.$route.query.page)
             axios.get('/api/freeBoard?page='+this.$route.query.page+'&projectId='+this.$route.params.projectId).then(response => { // 프로젝트 이름 가져오기
                this.paginatedItems=response.data
                }).catch((erro) => {
                console.error(erro);
             });
            axios.get('/api/freeBoard/commentNum?page='+this.$route.query.page+'&projectId='+this.$route.params.projectId).then(response => { // 프로젝트 이름 가져오기
                this.commentNum=response.data
                }).catch((erro) => {
                console.error(erro);
            });
        }
    },
    mounted() {
        this.loadPage();
    },
    methods: {
        loadPage(){
            axios.get('/api/user/checkJoinMember/'+this.$route.params.projectId) 
            .then(response => {
                this.isjoinMember=response.data
            });
            if(this.currentPage==1) {
                this.$router.push({
                    path: '/project/'+this.$route.params.projectId+'/freeBoard',
                    query:{page:1}
                }).catch(error => {       //일단 네비게이션 중복 에러 뜨는 부분은 throw해둠..
                    if(error.name != "NavigationDuplicated" ){
                        throw error;
                    }
                });
            }
            // axios.get('/api/user')
            // .then(response => {
            //     this.userType = response.data.userType
            // });
            axios.get('/api/freeBoard?page='+this.$route.query.page+'&projectId='+this.$route.params.projectId).then(response => { // 프로젝트 이름 가져오기
                    this.paginatedItems=response.data
                    }).catch((erro) => {
                    console.error(erro);
            });
            axios.get('/api/freeListNum?projectId='+this.$route.params.projectId).then(response => { // 프로젝트 이름 가져오기
                    this.totalRows=response.data
                    }).catch((erro) => {
                    console.error(erro);
            });
            axios.get('/api/freeBoard/commentNum?page='+this.$route.query.page+'&projectId='+this.$route.params.projectId).then(response => { // 프로젝트 이름 가져오기
                    this.commentNum=response.data
                    }).catch((erro) => {
                    console.error(erro);
            });
            axios.get('/api/freeBoard/likeNum?page='+this.$route.query.page+'&projectId='+this.$route.params.projectId).then(response => { // 프로젝트 이름 가져오기
                    this.likeNum=response.data
                    }).catch((erro) => {
                    console.error(erro);
            });
        },
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
            
            axios.post('/api/writeFree', {
                    projectId:this.$route.params.projectId,
                    title:this.title,
                    content:this.content,
                }).then(response => {
                //alert('id:'+response.data)
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
                    this.loadPage();
                })
            
                // if(this.$route.query.page!=1) {
                //     this.$router.push({
                //         path: '/project/'+this.$route.params.projectId+'/freeBoard',
                //         query:{page:1}
                //     })
                // } else {
                //     this.$router.go()
                // }
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
                path: '/project/'+this.$route.params.projectId+'/freeBoard/' + postId
            })
        },
        paginate (page_size, page_number) {
            this.$router.push({
                path: '/project/'+this.$route.params.projectId+'/freeBoard',
                query:{page:page_number+1}
            }).catch(error => {       //일단 네비게이션 중복 에러 뜨는 부분은 throw해둠..
                if(error.name != "NavigationDuplicated" ){
                    throw error;
                }
            });
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

<style scoped>
</style>