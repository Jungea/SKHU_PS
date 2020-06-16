<template>
    <div class="profileContainer">
        <h3>마이페이지</h3>
        <hr style="margin:30px 0;">
        <b-tabs pills align="right">
            <b-tab title="내 정보" title-link-class="text-dark bg-white" active>
                <div class="infoContainer">
                    <!-- <h5>내 정보</h5> -->
                    <div style="margin-top:30px; padding: 30px;">
                        <b-row>
                            <b-col style="min-width:350px; max-width:400px; margin-bottom:10px" class="mt-0">
                                <b-card img-src="https://image.flaticon.com/icons/svg/2825/2825025.svg" 
                                img-alt="Image" img-top img-height="200px" style="padding:20px">
                                    <b-card-text style="margin-top:20px">
                                        <h4>{{data.name}} 교수님</h4>
                                        <h6 style="color:#A4A4A4">{{data.userNum}}</h6>
                                    </b-card-text>
                                    <hr>
                                    <b-card-text style="height:50px;" v-if="(data.intro==null)?false:true">
                                        <span>{{data.intro}}</span>
                                    </b-card-text>
                                    <b-button variant="dark" size="sm" @click="editProfile">정보 수정</b-button>
                                </b-card>
                            </b-col>
                            <b-col style="min-width:350px ;" class="mt-0">
                                <b-card style="padding:10px 20px">
                                    <b-media>
                                        <h5 class="mt-0">Name</h5>
                                        <p>{{data.name}}
                                        </p>
                                    </b-media>
                                    <b-media>
                                        <h5 class="mt-0">Email</h5>
                                        <p>{{data.email}}
                                        </p>
                                    </b-media>
                                    <b-media>
                                        <h5 class="mt-0">Github</h5>
                                        <a v-bind:href="gitUrl" target="_blank" v-if="data.github!=null">
                                            <p>{{gitUrl}}</p>
                                        </a>
                                        <p v-if="data.github==null" style="color:silver">
                                            등록된 정보가 없습니다.
                                        </p>
                                    </b-media>
                                </b-card>
                            </b-col>
                        </b-row>
                    </div>
                </div>
                <hr style="margin:30px 0;">
            </b-tab>
            <b-tab title="작성한 게시글" title-link-class="text-dark bg-white">
                <div class="mt-3">
                    <center>
                        <b-form-group label-for="SubjectNotice">
                            <table class="table table-bordered" id="SubjectNotice" style="width: 90%">
                                <tr>
                                    <th class="th1">제목</th>
                                    <th class="th1">작성일</th>
                                </tr>
                                <tr @click="viewPost(1)">
                                    <td style="width: 40%"> <b> 12번 과제 어케하는지 아는 사람...살려주라 [2] </b> </td>
                                    <td class="td1" style="width: 20%"> 2020-06-21 </td>
                                </tr>
                                <tr @click="viewPost(1)">
                                    <td style="width: 40%"> <b> 더워죽겟다 [5] </b> </td>
                                    <td class="td1" style="width: 20%"> 2020-06-20 </td>
                                </tr>
                                <tr @click="viewPost(1)">
                                    <td style="width: 40%"> <b> 아침이 밝았습니다 와 [1] </b> </td>
                                    <td class="td1" style="width: 20%"> 2020-06-19 </td>
                                </tr>
                            </table>
                        </b-form-group>
                    </center>
                </div>
            </b-tab>
            <b-tab title="작성한 댓글" title-link-class="bg-white text-dark">
                <center>
                    <b-form-group class="mt-3">
                        <div @click="viewComment(1)" style="border: 1px solid silver ; padding: 10px ; margin-bottom: 10px ; width: 80% ; text-align: left ; min-height: 80px">
                            <div> <b> {{ data.name }} </b> <span style="color: #9A9A9A"> (2020-06-19 16:13) </span>
                            </div>
                            <div style="margin-top: 3px"> 댓글 누르면 해당 게시글로 이동하는게 좋겠네 </div>
                        </div>
                        <div @click="viewComment(1)" style="border: 1px solid silver ; padding: 10px ; margin-bottom: 10px ; width: 80% ; text-align: left ; min-height: 80px">
                            <div> <b> {{ data.name }} </b> <span style="color: #9A9A9A"> (2020-06-19 16:12) </span>
                            </div>
                            <div style="margin-top: 3px"> 동해물과 백두산이 마르고 닳도록 하느님이 보우하사 우리나라 만세
                                무궁화 삼천리 화려강산 대한사람 대한으로 길이 보전하세
                                남산 위에 저 소나무 철갑을 두른 듯 바람서리 불변함은 우리 기상일세
                                무궁화 삼천리 화려강산 대한사람 대한으로 길이 보전하세
                                가을하늘 공활한데 높고 구름없이 밝은 달은 우리 가슴 일편 단심일세
                                무궁화 삼천리 화려강산 대한사람 대한으로 길이 보전하세
                                이 기상과 이 마음으로 충성을 다하여 괴로우나 즐거우나 나라 사랑하세
                                무궁화 삼천리 화려강산 대한사람 대한으로 길이 보전하세 </div>
                        </div>
                        <div @click="viewComment(1)" style="border: 1px solid silver ; padding: 10px ; margin-bottom: 10px ; width: 80% ; text-align: left ; min-height: 80px">
                            <div> <b> {{ data.name }} </b> <span style="color: #9A9A9A"> (2020-06-19 16:11) </span>
                            </div>
                            <div style="margin-top: 3px"> 교수님 혹시 과제 점수 언제 올라오나요? </div>
                        </div>
                        <div @click="viewComment(1)" style="border: 1px solid silver ; padding: 10px ; margin-bottom: 10px ; width: 80% ; text-align: left ; min-height: 80px">
                            <div> <b> {{ data.name }} </b> <span style="color: #9A9A9A"> (2020-06-19 16:10) </span>
                            </div>
                            <div style="margin-top: 3px"> 과제 제출 하였습니다. </div>
                        </div>
                        <div @click="viewComment(1)" style="border: 1px solid silver ; padding: 10px ; margin-bottom: 10px ; width: 80% ; text-align: left ; min-height: 80px">
                            <div> <b> {{ data.name }} </b> <span style="color: #9A9A9A"> (2020-06-15 12:01) </span>
                            </div>
                            <div style="margin-top: 3px"> 이번 학기 내내 한게 없네 망했ㄷㅏ... </div>
                        </div>                        
                    </b-form-group>
                </center>
            </b-tab>
            <b-tab title="좋아요" title-link-class="bg-white text-dark">
                <div class="mb-3">
                    <b-button @click="btnClick(0)" variant="none">좋아요한 프로젝트</b-button> | <b-button @click="btnClick(1)" variant="none">좋아요한 게시글</b-button>
                </div>
                <div v-if="selected[0]">
                    <center>    
                        <b-container class="bv-row">
                            <p style="color: silver ; margin:300px auto ;" v-if="false">좋아요한 프로젝트가 없습니다.</p>
                            <b-row cols-md="3" cols="1">
                                <b-col class="mb-5" :key="index" v-for="(item, index) in paginatedItems">
                                    <b-card align="left" bg-variant="dark" text-variant="white" style="height:250px;" :ref="`card${item.project.projectId}`">
                                        <template v-slot:header>
                                            <h5 style="display:inline"><b>{{item.project.projectName}}</b></h5>
                                        </template>
                                        <b-card-text>
                                            <p style="font-size:17px">
                                                <b-icon-people-fill scale=1.1 style="margin-right:10px"></b-icon-people-fill><b>{{item.project.memNum}}</b>
                                            </p>
                                        </b-card-text>
                                        <b-card-text>
                                            <h6><b-badge variant="secondary" v-for="(tag,index) in item.project.tag.split(',')" :key="index" style="margin-right:5px">
                                                {{tag}}
                                            </b-badge></h6>
                                        </b-card-text>
                                        <b-card-text>
                                            <div style="margin-top:10px">
                                                <h5 style="display:inline-block;"><b-badge variant="success" v-if="!item.project.progState">진행중</b-badge></h5>
                                                <h5 style="display:inline-block;"><b-badge variant="danger" v-if="item.project.progState">진행완료</b-badge></h5>
                                                <h5 style="display:inline-block; margin-left:10px"><b-badge variant="success" v-if="!item.project.rcrtState">모집중</b-badge></h5>
                                                <h5 style="display:inline-block;"><b-badge variant="danger" v-if="item.project.rcrtState">모집완료</b-badge></h5>
                                            </div>
                                        </b-card-text>
                                        <template v-slot:footer>
                                            <b-icon @click="changeStar(item.project.projectId,item.star)" style="cursor:pointer;" scale=1.3 :icon="item.star==true?'heart-fill':'heart'"></b-icon>
                                            <b-button @click="sendInfo(item.project.projectId)" v-b-modal.modal-xl size='sm' style="float:right">자세히</b-button>
                                        </template>
                                    </b-card>    
                                </b-col>
                            </b-row>
                            <b-pagination  @change="onPageChanged" :total-rows="totalRows" :per-page="perPage" v-model="$route.query.page" class="my-0"></b-pagination>

                            
                            <b-modal id="modal-xl" size="lg" title="프로젝트 개요">
                                <table class="table table-bordered" id="ProjectSummary" >
                                    <tbody>
                                        <tr>
                                            <th scope="row" style="width:28%">프로젝트명</th>
                                            <td>
                                                {{summaryData.project.projectName}}
                                                <b-button @click="moveToProject(summaryData.project.projectId)" style="margin-left:20px" size="sm">이동</b-button>
                                            </td>
                                        </tr>
                                            <tr>
                                            <th scope="row" style="width:28%">개설자</th>
                                            <td>
                                                {{summaryData.createName}}
                                            </td>
                                        </tr>
                                            <tr>
                                            <th scope="row" style="width:28%">참여자 학년</th>
                                            <td>
                                                {{summaryData.allMemGrade}}
                                            </td>
                                        </tr>
                                            <tr>
                                            <th scope="row" style="width:28%">멤버 수</th>
                                            <td>
                                                {{summaryData.project.memNum }}
                                            </td>
                                        </tr>
                                        <tr>
                                            <th scope="row">주제</th>
                                            <td>
                                                {{summaryData.project.theme }}
                                            </td>
                                        </tr>
                                        <tr>
                                            <th scope="row">내용</th>
                                            <td>
                                                {{summaryData.project.content }}
                                            </td>
                                        </tr>
                                        <tr>
                                            <th scope="row">사용기술과 언어</th>
                                            <td>
                                                <b-badge variant="secondary" v-for="(tag,index) in summaryData.project.tag.split(',')" :key="index" style="margin-right:5px">{{tag}}</b-badge>
                                            </td>
                                        </tr>
                                        <tr>
                                            <th scope="row">깃허브 리포지토리 주소</th>
                                            <td>
                                                {{summaryData.project.github }}
                                            </td>
                                        </tr>
                                        <tr>
                                            <th scope="row">인원 모집 상태</th>
                                            <td>
                                                {{summaryData.project.rcrtState==false?"모집중":"모집완료" }}
                                            </td>
                                        <tr>
                                            <tr>
                                            <th scope="row">진행 상태</th>
                                            <td>
                                                {{summaryData.project.progState==false?"진행중":"진행완료" }}
                                            </td>
                                        <tr>
                                            <th scope="row">과목</th>
                                            <td>
                                                {{summaryData.subjectName==null?"-":summaryData.subjectName }}
                                            </td>
                                        </tr>
                                    </tbody>
                                </table>
                                    <b-button variant="danger" v-if="userType!=1&& summaryData.state==2&& summaryData.project.rcrtState==false" @click="projectJoin(summaryData.project.projectId)">참가 신청하기</b-button>
                                    <b-button variant="warning" v-else-if="userType!=1&&summaryData.state==0" >승인 대기</b-button>
                                    <b-button variant="success" v-else-if="userType!=1&&summaryData.state==1" >참가중</b-button>

                                <template v-slot:modal-footer="{cancel}">
                                    <b-button size="sm" @click="cancel()">닫기</b-button>
                                </template>
                            </b-modal>
                        </b-container>    
                    </center>    
                </div>
                <div v-if="selected[1]">
                    <center>
                        <b-form-group>
                            <table class="table table-bordered" style="width: 90%">
                                <tr>
                                    <th class="th1">제목</th>
                                    <th class="th1" style="width: 10%">작성자</th>
                                    <th class="th1">작성일</th>
                                </tr>
                                <tr @click="viewPost(1)">
                                    <td style="width: 40%"> <b> 노래 듣고가라 https://www.youtube.com/watch?v=KA9J3WWCimo [7] </b> </td>
                                    <td class="td1"> 이영선 </td>
                                    <td class="td1" style="width: 10%"> 2020-06-21 </td>
                                </tr>
                                <tr @click="viewPost(1)">
                                    <td style="width: 40%"> <b> -메- [5] </b> </td>
                                    <td class="td1">이윤영</td>
                                    <td class="td1"> 2020-06-20 </td>
                                </tr>
                                <tr @click="viewPost(1)">
                                    <td style="width: 40%"> <b> 코로나 언제끝나...나 노래방가고싶어.... [10] </b> </td>
                                    <td class="td1">이영선</td>
                                    <td class="td1"> 2020-06-19 </td>
                                </tr>
                            </table>
                        </b-form-group>
                    </center>
                </div>
            </b-tab>
        </b-tabs>
    </div>
</template>

<script>
import axios from 'axios';

export default {
    name: 'ProfessorProfile',
    components: {
    },
    data() {
        return {
            data: {},
            gitUrl: '',
            selected: [ true, false ],

            perPage: 6, // 각 페이지마다 보이는 카드 최대 수
            currentPage: 1,
            totalRows: null,
            paginatedItems:{},
            rcrtState:false,
            userType: null,
            summaryData:{
                project: {
                    projectName:'',
                    memNum:null,
                    theme:'',
                    content:'',
                    tag:'',
                    github:null,
                    rcrtState:false,
                    progState:false,
                },
            }
        }
    },
    watch: {
      '$route'(){
          axios.get('/api/projectBoard?page='+this.$route.query.page).then(response => { // 프로젝트 이름 가져오기
                this.paginatedItems=response.data
              }).catch((erro) => {
              console.error(erro);
            });
      }
    },
    mounted() {
        axios.get('/api/user')
        .then(response => {
            this.data = response.data
            this.gitUrl='https://github.com/'+response.data.github
        });
        axios.get('/api/all/projectsNum') // 내 프로젝트 모든 목록 갯수
        .then(response => {
            this.totalRows=response.data
        });
        axios.get('/api/projectBoard?page='+this.$route.query.page).then(response => { // 프로젝트 이름 가져오기
            this.paginatedItems=response.data
            this.summaryData=response.data[0]
            }).catch((erro) => {
            console.error(erro);
        });
    },
   methods:{
       editProfile() {
            this.$router.push({
                path: 'editProfessorProfile'
            })
       },
               btnClick(num) {
            if(num==0) {
                this.selected[0] = true;
                this.selected[1] = false;
                this.$router.push({
                    query:{page:1}
                })
            }
            else {
                this.selected[1] = true;
                this.selected[0] = false;
            }
            this.$forceUpdate();
        },
        viewPost(id) {
            alert("게시글 이동" + id)
            // this.$router.push({
            //     path: '/post/' + id
            // })
        },
        viewComment(id) { // 댓글을 작성한 게시글로 이동
            alert("댓글을 작성한 게시글로 이동" + id)
            // this.$router.push({
            //     path: '/comment/' + id
            // })
        },
        moveToProject(pId){
            let Url='/project/'+pId+'/summary'
            this.$router.push({
                path: Url,
            })
        },
        paginate (page_size, page_number) {
         this.$router.push({
            path: '/professorProfile',
            query:{page:page_number+1}
          })
    //   let itemsToParse = this.data
    //   this.paginatedItems = itemsToParse.slice(page_number * page_size, (page_number + 1) * page_size);
        },
        onPageChanged(page){
            this.currentPage=this.$route.query.page
            this.paginate(this.perPage, page - 1)
        },
        changeStar(projectId) {
            axios.post('/api/changeStar', {
                    projectId:projectId
                })
                .then(response => {
                    this.data = response.data
                    axios.get('/api/projectBoard?page='+this.$route.query.page).then(response => {
                        this.paginatedItems=response.data
                    }).catch((erro) => {
                        console.error(erro);
                    });
                })
                
            event.stopPropagation()
        },
        //모달메소드
        handleOk(bvModalEvt) {
                // Prevent modal from closing
                bvModalEvt.preventDefault()
                // Trigger submit handler
        },
        sendInfo(projectId) {
        //this.summaryData=item
        axios.get('/api/projectBoard/modal/'+projectId).then(response => {
                    this.summaryData=response.data
                }).catch((erro) => {
                console.error(erro);
            });
        },
   }
}
</script>

<style scopped>
    .profileContainer {
        margin:100px;
    }

    .profileContainer h3{
        font-weight: bold;
    }

    .inviteList{
        margin-top:10px; 
        padding:10px;
        border-bottom:1px solid #E6E6E6
    }

    .inviteList:last-child{
        border-bottom:none;
    }
</style>