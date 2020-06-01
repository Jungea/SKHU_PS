<template>
    <div class="containerStyle">
        <h3 style="margin-left: 5%">공지 게시판</h3>
        <hr style="width: 90%">
        <center>
            <b-form-group label-for="SubjectNotice">
                <table class="table table-bordered" id="SubjectNotice" style="width: 90%">
                    <tr>
                        <th class="th1">제목</th>
                        <th class="th1">작성일</th>
                        <th class="th1">제출 마감일</th>
                        <!-- <th class="th1">제출 연장일</th> -->
                        <th class="th1">제출 여부</th>
                    </tr>
                    <tr v-for="(item, index) in paginatedItems" :key="index" @click="viewContent(item.postId)">
                        <td style="width: 40%"> <b> {{ item.title }} [{{commentNum[index]}}]</b> </td>
                        <td class="td1" style="width: 20%"> {{ item.writeTime.substring(0,10)+" "+item.writeTime.substring(11,16) }} </td>
                        <td class="td1"> {{ item.deadlineTime=='1000-01-01T00:00:00'?'-':item.deadlineTime.substring(0,10)+" "+item.deadlineTime.substring(11,16)}} </td>
                        <!-- <td class="td1"> {{ item.extentionTime=='1000-01-01T00:00:00'?'-':item.extentionTime.substring(0,10)+" "+item.extentionTime.substring(11,16) }} </td> -->
                        <td class="td1">
                            <div v-if="item.deadlineTime=='1000-01-01T00:00:00'"> - </div>
                            <b-icon-check v-if="item.deadlineTime!='1000-01-01T00:00:00' && (isFile[index] != '0')" style="color: green" scale="1.5"></b-icon-check>
                            <b-icon-x v-if="item.deadlineTime!='1000-01-01T00:00:00' && (isFile[index] == '0')" style="color: red" scale="1.5"></b-icon-x>
                        </td>
                    </tr>
                </table>
                
            </b-form-group>
             <b-pagination style="float: right ; margin-right: 5%" @change="onPageChanged" :total-rows="totalRows" :per-page="perPage" v-model="$route.query.page" class="my-0"></b-pagination>

        </center>

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
    name: 'StudentNoticeBoard',
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
           files: '',
           subjectId:null,
           isFile:[],
           commentNum:[],
        } 
    },
    watch: {
        '$route'(){
            axios.get('/api/project/projectName/'+this.$route.params.projectId).then(response => { // 프로젝트 이름 가져오기
                this.subjectId = response.data.subject.subjectId
                console.log('query111:'+this.$route.query.page)

                axios.get('/api/noticeBoard/commentNum?page='+this.$route.query.page+'&subjectId='+ this.subjectId).then(response => { // 프로젝트 이름 가져오기
                    this.commentNum=response.data
                }).catch((erro) => {
                    console.error(erro);
                 });
                 
                axios.get('/api/noticeBoard?page='+this.$route.query.page+'&subjectId='+this.subjectId).then(response => {
                    this.paginatedItems=response.data
                    axios.get('/api/noticeBoard/fileSubmitList?page='+this.$route.query.page+'&projectId='+this.$route.params.projectId+'&subjectId='+this.subjectId).then(response3 => {
                            this.isFile=response3.data
                        }).catch((erro) => {
                            console.error(erro);
                    });
                }).catch((erro) => {
                    console.error(erro);
                });
              }).catch((erro) => {
              console.error(erro)
              });
        }
    },
    mounted() {
        axios.get('/api/project/projectName/'+this.$route.params.projectId).then(response => { // 프로젝트 이름 가져오기
                this.subjectId = response.data.subject.subjectId
                if(this.currentPage==1) {
                        this.$router.push({
                            path: '/project/'+this.$route.params.projectId+'/noticeBoard',
                            query:{page:1}
                        })
                }
                axios.get('/api/noticeBoard/commentNum?page='+this.$route.query.page+'&subjectId='+ this.subjectId).then(response => { // 프로젝트 이름 가져오기
                    this.commentNum=response.data
                }).catch((erro) => {
                    console.error(erro);
                 });
                axios.get('/api/noticeBoard?page='+this.$route.query.page+'&subjectId='+this.subjectId).then(response2 => {
                    this.paginatedItems=response2.data
                    axios.get('/api/noticeBoard/fileSubmitList?page='+this.$route.query.page+'&projectId='+this.$route.params.projectId+'&subjectId='+this.subjectId).then(response3 => { // 프로젝트 이름 가져오기
                        this.isFile=response3.data
                        }).catch((erro) => {
                        console.error(erro);
                    });
                    }).catch((erro) => {
                    console.error(erro);
                });
                axios.get('/api/noticeListNum?subjectId='+this.subjectId).then(response3 => {
                        this.totalRows=response3.data
                    }).catch((erro) => {
                    console.error(erro);
                });
              }).catch((erro) => {
              console.error(erro);
        });
        
    },
    methods: {
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
                path: '/project/'+this.$route.params.projectId+'/noticeBoard/' + postId
            })
        },
        paginate (page_size, page_number) {
         this.$router.push({
            path: '/project/'+this.$route.params.projectId+'/noticeBoard',
            query:{page:page_number+1}
          })
        //   let itemsToParse = this.data
        //   this.paginatedItems = itemsToParse.slice(page_number * page_size, (page_number + 1) * page_size);
        },
        onPageChanged(page){
            this.currentPage=this.$route.query.page
            this.paginate(this.perPage, page - 1)
        },
    }
}
</script> 