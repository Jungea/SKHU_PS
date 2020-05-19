<template>
  <div class="home">
        <center>    
            <b-container class="bv-row">
                <b-row class="m-4">
                    <b-col><h4>프로젝트 게시판</h4></b-col>
                </b-row>

                <b-row class="text-right mb-4">
                    <b-col><b-button v-b-modal.modal-xl variant="outline-secondary" style="height: 70px;">프로젝트 검색</b-button></b-col>
                </b-row>
                   <b-row cols-md="3" cols="1">
                    <b-col class="mb-5" :key="index" v-for="(item, index) in paginatedItems">
                        <b-card id="my-table"  @click="sendInfo(item)" v-b-modal.modal-xl align="left" bg-variant="dark" text-variant="white" style="height: 15rem;"> <!-- 30rem == 480px -->
                            <div>
                                <b-card-header style="padding: 0 0 10px 0">
                                    <table>
                                        <tr>
                                            <td style="width: 100%">{{item.project.projectName}}</td>
                                            <td><b-button @click="changeStar(item.project.projectId)" style="background-color: rgb(52,58,64) ; border-color: rgb(52,58,64) ; margin-top: -10px" ><b-icon scale=1.5 v-bind:icon="item.star==true?'heart-fill':'heart'"></b-icon></b-button></td>
                                        </tr>
                                    </table>
                                </b-card-header>
                            </div>
                            <b-card-text style="margin-top: 10px">
                                <p style="font-size:17px">인원 :{{item.project.memNum}}명</p>
                                <h6><b-badge variant="secondary" v-for="(tag,index) in item.project.tag.split(',')" :key="index" style="margin-right:5px">
                                    {{tag}}
                                </b-badge></h6>
                                <b-button variant="success" style="cursor:default;" v-show="item.project.progState==false">진행중</b-button>
                                <b-button variant="danger"  style="cursor:default" v-show="item.project.progState==true">진행완료</b-button>
                                <div style="margin-top:10px">
                                  <b-button variant="success" style="cursor:default;" v-show="item.project.rcrtState==false">모집중</b-button>
                                  <b-button variant="danger"  style="cursor:default" v-show="item.project.rcrtState==true">모집완료</b-button>
                                </div>
                            </b-card-text>    
                        </b-card>  
                    </b-col>
                </b-row>
                <b-pagination @change="onPageChanged" :total-rows="totalRows" :per-page="perPage" v-model="currentPage" class="my-0" />

            </b-container>  
              
        </center>  
        <b-modal id="modal-xl" size="lg" title="프로젝트 개요" 
            @ok="handleOk" ref="modal" data-backdrop="static">
          <table class="table table-bordered" id="ProjectSummary" v-bind="this.summaryData">
                    <tbody>
                        <tr>
                            <th scope="row" style="width:28%">프로젝트명</th>
                            <td>
                                {{this.summaryData.project.projectName }}
                            </td>
                        </tr>
                         <tr>
                            <th scope="row" style="width:28%">개설자</th>
                            <td>
                                {{ this.summaryData.createName}}
                            </td>
                        </tr>
                         <tr>
                            <th scope="row" style="width:28%">참여자 학년</th>
                            <td>
                                {{ this.summaryData.allMemGrade}}
                            </td>
                        </tr>
                         <tr>
                            <th scope="row" style="width:28%">멤버 수</th>
                            <td>
                                {{ this.summaryData.project.memNum }}
                            </td>
                        </tr>
                        <tr>
                            <th scope="row">주제</th>
                            <td>
                                {{ this.summaryData.project.theme }}
                            </td>
                        </tr>
                        <tr>
                            <th scope="row">내용</th>
                            <td>
                                {{ this.summaryData.project.content }}
                            </td>
                        </tr>
                        <tr>
                            <th scope="row">사용기술과 언어</th>
                            <td>
                                <b-badge variant="secondary" v-for="(tag,index) in this.summaryData.project.tag.split(',')" :key="index" style="margin-right:5px">{{tag}}</b-badge>
                            </td>
                        </tr>
                        <tr>
                            <th scope="row">깃허브 리포지토리 주소</th>
                            <td>
                              {{ this.summaryData.project.github }}
                            </td>
                        </tr>
                        <tr>
                            <th scope="row">인원 모집 상태</th>
                            <td>
                                {{this.summaryData.project.rcrtState==false?"모집중":"모집완료" }}
                            </td>
                        <tr>
                          <tr>
                            <th scope="row">진행 상태</th>
                            <td>
                                {{this.summaryData.project.progState==false?"진행중":"진행완료" }}
                            </td>
                        <tr>
                            <th scope="row">과목</th>
                            <td>
                                 {{this.summaryData.subjectName==null?"-":this.summaryData.subjectName }}
                            </td>
                        </tr>
                    </tbody>
                </table>
                <b-button variant="danger" v-if="this.summaryData.state==2" @click="projectJoin(this.summaryData.project.projectId)">참가 신청하기</b-button>
                <b-button variant="warning" v-else-if="this.summaryData.state==0" >승인 대기</b-button>
                <b-button variant="success" v-else-if="this.summaryData.state==1" >참가중</b-button>
        </b-modal>  
  </div>
</template>

<script>
import axios from 'axios';
export default {
  name: 'PojectBoard',
  data() {
    return {
      perPage: 6, // 각 페이지마다 보이는 카드 최대 수
      currentPage: 1,
      totalRows: null,
      paginatedItems:{},

      data:{},
      tagArray:[],
      rcrtState:false,
      
      summaryData:{},
      alertvariant:'danger'
    }
  },
  mounted() { 
        axios.get('/api/all/projects') // 내 프로젝트 모든 목록
        .then(response => {
            this.data = response.data 
            this.totalRows=this.data.length
            this.paginatedItems=this.data
            this.summaryData=this.data[0]
            this.paginate(this.perPage, 0)
        });
    },
  computed: {
    pageCount() {
      let l = this.totalRows,
        s = this.perPage;
      return Math.floor(l / s);
    }
  },
  methods: {
     paginate (page_size, page_number) {
      let itemsToParse = this.data
      this.paginatedItems = itemsToParse.slice(page_number * page_size, (page_number + 1) * page_size);
    },
    onPageChanged(page){
      this.paginate(this.perPage, page - 1)
    },
    changeStar(projectId) {
      axios.post('/api/changeStar', {
                projectId:projectId
            })
            .then(response => {
                this.data = response.data
            })
            
            event.stopPropagation()
    },
   //모달메소드
    handleOk(bvModalEvt) {
            // Prevent modal from closing
            bvModalEvt.preventDefault()
            // Trigger submit handler
            this.handleSubmit()
    },
    sendInfo(item) {
      this.summaryData=item
    },
    projectJoin(projectId) {
      alert('클릭')
       axios.post('/api/joinProject', {
                projectId:projectId
            })
            .then(() => {
                
                alert('참가 신청이 완료되었습니다.')
                // this.alertvariant='warning'
            })
      
    }
  },


}
</script>