<template>
    <div class="containerStyle">
        <h3 style="margin-left: 5%">자유 게시판</h3>
        <hr style="width: 90%">
        <!--게시글-->
        <center>
            <div v-if="loading">
            <!--본문-->
            <table class="table table-bordered" id="SubjectNotice" style="width: 90%">
                <tr>
                    <th class="th1" style="width: 14%">제목</th>
                    <td>{{post.title}}</td>
                </tr>
                <tr>
                    <th class="th1" style="width: 14%">작성자</th>
                    <td>{{post.user.name}}</td>
                </tr>
                <tr>
                    <th class="th1" style="width: 14%">좋아요</th>
                    <td>{{likeNum}}</td>
                </tr>
                <tr>
                    <th class="th1">작성일</th>
                    <td>{{post.writeTime}}</td>
                </tr>
                <tr>
                    <td colspan="2" style="height: 300px">{{post.content}}</td>
                </tr>
                
                <tr>
                    <th class="th1" style="vertical-align: middle">첨부 파일</th>
                    <td>
                        <div v-for="(item, index) in files" :key="index">
                            <span class="file" style="cursor:pointer;color:blue" @click="download(item)">{{ item.name }}</span>
                        </div>
                    </td>
                </tr>
            </table>
            <!--게시글 하단 메뉴-->
            <div style="text-align: left">
                <div style="display: inline-block ; margin-left: 5%">
                    <b-button @click="viewComment()" v-if="!checkComment" variant="dark">댓글 보기</b-button>
                    <b-button @click="viewComment()" v-if="checkComment" variant="dark">댓글 접기</b-button>
                </div>
                <div style="display: inline-block ; margin-right: 5% ; float: right">
                    <b-button class="mr-2" v-if="admin=='1' ||userId==post.user.userId" @click="deletePost()" variant="danger">삭제</b-button>
                    <b-button class="mr-2" v-if="userId==post.user.userId" @click="edit()" variant="dark">수정</b-button>
                    <b-button class="mr-2" @click="viewList()" variant="dark">목록으로</b-button>
                    <b-button @click="postLike()" :variant="isPostLiked?'danger':'primary'">좋아요<span v-if="isPostLiked">취소</span></b-button>
                </div>
            </div>
            
            <hr style="width: 90% ; border: 1px solid #ccc">

            <!--댓글 목록-->
            <table class="table table-bordered" style="width: 90%" v-show="checkComment">
                <tr v-for="(item, index) in comment" :key="index" :class="{selected:item.choice}">
                    <td> 
                        <div> 
                            <b> {{ item.user.name }} </b> 
                            <span style="color: #9A9A9A"> ({{ item.writeTime.substring(0,10)+" "+item.writeTime.substring(11,16) }}) </span>
                            <span v-if="admin=='1' || item.user.userId==userId" @click="deleteComment(item.commentId)" style="margin-left:10px; cursor:pointer">삭제</span>
                            <!--채택 버튼: 작성자에게만 보이게/채택된 댓글이 없을 때 보이게-->
                            <b-button v-if="post.user.userId==userId&&!isThereSelected" @click="commentSelect(item.commentId)" size="sm" style="float:right">채택</b-button>
                        </div>
                        <!--댓글 내용-->
                        <div style="margin-top: 3px">
                            {{item.content}}
                            <!--채택 마크: 채택된 댓글에 보이게-->
                            <span v-if="item.choice" style="float:right;">
                                <b-icon-check-circle scale="1.7" style="margin-right:20px"></b-icon-check-circle><b>채택된 댓글</b>
                            </span>
                        </div>
                    </td>
                </tr>
                <tr>
                    <td>
                        <b-textarea trim v-model="content" rows="2" style="height: 80px ; min-height: 80px ; max-height: 150px"></b-textarea>
                        <b-button :disabled="content.length==0" @click="addComment()" style="margin-top: 10px ; float: right ; background: #9A9A9A ; border-color: #9A9A9A">작성</b-button>
                    </td>
                </tr>
            </table>
            </div>
        </center>
    </div>
</template>

<script>
import axios from 'axios'
export default {
    name: 'freeContent',
    data() {
        return {
            loading:false, //렌더링 오류 해결
            post:{}, //현 게시글 정보
            comment: [],
            checkComment: true, //댓글 접기/펴기
            content: '',
            isPostLiked:null, // 현재 게시글 좋아요 상태표시

            files:null,

            //deadlineTime: '',
            //extentionTime: '',
            
            //현재 유저 정보
            userId:null,
            userType: '',
            userName: '',
            admin:null,
            likeNum:null,
        }
    },
    mounted() { 
        this.loadPage();
    },

    computed:{
        isThereSelected(){
            let s=false;
            for(let i=0;i<this.comment.length;i++){
                if(this.comment[i].choice==1){
                    s=true;
                    break
                }
            }
            return s;
        }
    },
    methods: {
        loadPage(){
            axios.get('/api/user')
            .then(response => {
                this.userType = response.data.userType
                this.userName = response.data.name
                this.userId=response.data.userId
                this.admin=response.data.admin
            })
            axios.get('/api/file1/list/'+this.$route.params.postId) 
            .then(response => {
                this.files=response.data
            });

            //현재 게시글 정보
            axios.get('/api/noticeBoard/post/'+this.$route.params.postId)
            .then(response => {
                this.post=response.data
                console.log(this.post.user)
                this.post.writeTime = this.post.writeTime.substring(0,10)+" "+this.post.writeTime.substring(11,16)
                this.loading=true
            });
            
            //현재 게시글에 대한 댓글 정보
            axios.get('/api/noticeBoard/comment/'+this.$route.params.postId)
            .then(response => {
                this.comment=response.data
            })

            axios.get('/api/freeBoard/postLike/'+this.$route.params.postId)
            .then(response => {
                this.isPostLiked=response.data
            })
            this.postId = this.$route.params.postId
             axios.get('/api/community/content/likeNum?postId='+this.$route.params.postId).then(response => { // 프로젝트 이름 가져오기
                    this.likeNum=response.data
                    }).catch((erro) => {
                    console.error(erro);
            });

        },
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
        edit() {
            this.$router.push({
                path: '/project/'+ this.$route.params.projectId+'/freeBoard/'+ this.postId + '/edit'
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
                this.loadPage()
                this.content=''
            })
        },
        viewList() {
            this.$router.push({
                path: '/project/' + this.$route.params.projectId+'/freeBoard',
                quert:{page:1}
            })
        },

        deleteComment(commentId) {
            let result=confirm('삭제하시겠습니까?')
            if(result) {
                axios.post('/api/noticeBoard/deleteComment/'+commentId, {})
                .then(response => {
                    console.log(response.data)
                    this.loadPage()
                })
            }
        },
        deletePost() {
            let result=confirm('삭제하시겠습니까?')
            if(result) {
                axios.post('/api/board/deletePost', {
                    postId:this.$route.params.postId
                })
                .then(response => {
                    console.log(response.data)
                    this.$router.push({
                        path: '/project/'+this.$route.params.projectId+'/freeBoard',
                        query:{page:1}
                    })
                })
            }
        },

        //댓글 채택
        commentSelect(commentId) {
            if(confirm('해당 댓글을 채택합니까? 채택 후에는 취소할 수 없습니다.')){
                axios.post('/api/freeBoard/commentCheck/'+commentId,{
                    postId:this.$route.params.postId,
                }).then(response=>{
                    console.log(response.data)
                    alert('채택되었습니다.')
                    this.loadPage()
                })
                
            }
        },
        postLike() {
            axios.post('/api/freeBoard/postLike/'+this.$route.params.postId)
                .then(response => {
                    console.log(response.data)
                    this.isPostLiked=!this.isPostLiked
                    this.loadPage()
            })
        },
        
    },
}
</script>

<style scoped>
    .file:hover { text-decoration: underline }
    .selected{
        background: #f3f3f3;
    }
</style>