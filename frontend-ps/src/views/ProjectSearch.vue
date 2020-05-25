<template>
  <div class="home">
      <center>
       <b-container class="bv-row">
          <b-row class="m-4">
                    <b-col><h4>프로젝트 게시판</h4></b-col>
                </b-row>
                
                 <b-row class="text-right mb-4">
                     <b-col v-if="$route.query.type!=null">{{$route.query.type==0?'프로젝트 이름으로':'조장 이름으로'}} "{{$route.query.text}}"를 검색한 결과입니다</b-col>
                     <b-col v-if="$route.query.type==null"></b-col>
                    <b-col><b-button v-b-modal.modal-xl2 variant="outline-secondary" style="height: 70px;">프로젝트 검색</b-button></b-col>
                </b-row>
                   <b-row cols-md="3" cols="1">
                        <b-col v-if="paginatedItems.length==0">검색 결과가 없습니다.</b-col>
                    <b-col class="mb-5" :key="index" v-for="(item, index) in paginatedItems">
                        <b-card id="my-table"  @click="sendInfo(item.project.projectId)" v-b-modal.modal-xl align="left" bg-variant="dark" text-variant="white" style="height: 15rem;"> <!-- 30rem == 480px -->
                            <div>
                                <b-card-header style="padding: 0 0 10px 0">
                                    <table>
                                        <tr>
                                            <td style="width: 100%">{{item.project.projectName}}</td>
                                            <td><b-button @click="changeStar(item.project.projectId,item.star)" style="background-color: rgb(52,58,64) ; border-color: rgb(52,58,64) ; margin-top: -10px" ><b-icon id="star" scale=1.5 v-bind:icon="item.star==true?'heart-fill':'heart'"></b-icon></b-button></td>
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
                 <b-form-select class="mt-4" v-model="selected" :options="options"></b-form-select>
                <b-form-input  v-model="text"></b-form-input>
                 <b-button @click="search()">검색</b-button>
            </b-container> 
            </center>   
            <div style="position:fixed;bottom:20px;right:20px;">
                <b-button ><a href="#" style="color:white">Top</a></b-button>
            </div>
  </div>
</template>

 <script>
import axios from 'axios';
export default {
  name: "ProjectSearch",
  data() {
    return {
    //   perPage: 6, // 각 페이지마다 보이는 카드 최대 수
    //   currentPage: 1,
    //   totalRows: null,
      paginatedItems:{},
      data:{},
      tagArray:[],
      rcrtState:false,
      summaryData:{},

       text:'',
       selected:null,
       options: [
          { value: 'projectName', text: '프로젝트 이름' },
          { value: 'captinName', text: '조장 이름' },
       ],
    }
  },
  mounted() { 
      this.text=''
        // console.log("query:"+this.$route.query.page)
        if(this.$route.query.type!=null) {
         axios.get('/api/projectBoard/search?type='+this.$route.query.type+'&text='+this.$route.query.text).then(response => { 
                this.paginatedItems=response.data
                this.summaryData=response.data[0]
              }).catch((erro) => {
              console.error(erro);
            });
        }
    },
    
  methods: {
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
    },
    sendInfo(projectId) {
      //this.summaryData=item
       axios.get('/api/projectBoard/modal/'+projectId).then(response => { // 프로젝트 이름 가져오기
                this.summaryData=response.data
                this.$refs['modal'].show()
              }).catch((erro) => {
              console.error(erro);
            });
    },
    projectJoin(projectId) {
       axios.post('/api/joinProject', {
                projectId:projectId
            })
            .then((response) => {
                 this.sendInfo(projectId)
                if(response.data) {
                    alert('참가 신청이 완료되었습니다.')
                   // this.summaryData.state=0;
                    // this.$refs['modal'].hide()
                    // this.$refs['modal'].show()
                   
                }
                else
                    alert('이미 초대받은 프로젝트입니다.\n 마이페이지를 확인하세요.')
                // this.alertvariant='warning'
            })
      
    },
    
  },
  
}
</script>