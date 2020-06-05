<template>
    <div class="containerStyle">
        <b-row>
            <h4>프로젝트 게시판</h4>
        </b-row>
        <!--필터검색-->
        <b-row>
            <b-col v-if="$route.query.type!=null">{{$route.query.type==0?'프로젝트 이름으로':'조장 이름으로'}} "{{$route.query.text}}"를 검색한 결과입니다</b-col>
            <b-col v-if="$route.query.type==null">{{$route.query.grade!=''?'학년으로 "'+$route.query.grade+'"학년을':''}} 
                {{$route.query.year!=''?'연도로 "'+$route.query.year+'"년을':''}} 
                {{$route.query.subject!=''?'과목으로 "'+$route.query.subject+'"을(를)':''}} 
                {{$route.query.tag.length!=0?'태그로 "'+$route.query.tag+'"을(를)':''}} 검색한 결과입니다.
            </b-col>
        </b-row>
        <b-row class="text-right mb-4">
            <b-col><b-button v-b-toggle.my-collapse variant="outline-secondary" style="height: 70px;">프로젝트 검색</b-button></b-col>
        </b-row>
        <b-collapse id="my-collapse">
            <b-card title="필터 검색" class="mb-5">
                학년
                <b-form-select v-model="selectedGrade" :options="grades"></b-form-select>
                and 연도
                <b-form-select v-model="selectedYear" :options="years"></b-form-select>
                and 강좌
                <b-form-select v-model="selectedSubject" :options="subjects"></b-form-select>
                and 기술
                <b-form-tags
                    input-id="language"
                    :input-attrs="{ 'aria-describedby': 'tags-remove-on-delete-help' }"
                    v-model="tags"
                    separator=" ,;"
                    placeholder="태그를 입력하세요"
                    remove-on-delete
                    add-on-enter
                    class="mb-2"
                ></b-form-tags>
                <b-button @click="filter()">검색</b-button>
            </b-card>
        </b-collapse>
        <!--검색 결과-->
        <b-row cols-md="3" cols="1">
            <!--결과 없음-->
            <b-col v-if="paginatedItems.length==0">검색 결과가 없습니다.</b-col>
            <!--결과 목록-->
            <b-col class="mb-5" :key="index" v-for="(item, index) in paginatedItems">
                <!--프로젝트 카드-->
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
        <b-row class="mt-4" style="">
            <b-col><b-form-select v-model="selected" :options="options"></b-form-select></b-col>
            <b-col><b-form-input v-model="text"></b-form-input></b-col>
            <b-col><b-button @click="search()">검색</b-button></b-col>
        </b-row>
        
        <!--프로젝트 정보 모달-->
        <b-modal id="modal-xl" size="lg" title="프로젝트 개요" @hidden="resetModal">
            <table class="table table-bordered" id="ProjectSummary" >
                <tbody>
                    <tr>
                        <th scope="row" style="width:28%">프로젝트명</th>
                        <td>
                            {{summaryData.project.projectName}}
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
        
            <div style="position:fixed;bottom:20px;right:20px;">
                <a href="#" style="display:scroll;color:white"><b-button variant="info" style="width:100px;height:50px">Top</b-button></a>
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
          createName:'',
          allMemGrade:[],
          subjectName:'',

      },
       
       text:'',
       selected:null,
       options: [
          { value: '0', text: '프로젝트 이름' },
          { value: '1', text: '조장 이름' },
       ],
       grades: [
          { value: '1', text: '1학년' },
          { value: '2', text: '2학년' },
          { value: '3', text: '3학년' },
          { value: '4', text: '4학년' },
       ],
       years: [],
       subjects:[],
       tags:[],
       selectedGrade:'',
       selectedYear:'',
       selectedSubject:'',
       userType:null,
    }
  },
  watch: {
      '$route'(){
        this.tags=[]
        this.selectedGrade=''
        this.selectedYear=''
        this.selectedSubject=''
        this.text=''
        this.selected=''
        //   console.log('query111:'+this.$route.query.page)
          if(this.$route.query.type!=null) {
         axios.get('/api/projectBoard/search?type='+this.$route.query.type+'&text='+this.$route.query.text).then(response => { 
                this.paginatedItems=response.data
              }).catch((erro) => {
              console.error(erro);
            });
        } else {
            axios.get('/api/projectBoard/filter?grade='+this.$route.query.grade+'&year='+this.$route.query.year+'&subject='+this.$route.query.subject+'&tag='+this.$route.query.tag).then(response => { 
                this.paginatedItems=response.data
              }).catch((erro) => {
              console.error(erro);
            });
        }
      }
  },
  mounted() { 
      this.tags=[]
       this.selectedGrade=''
       this.selectedYear=''
       this.selectedSubject=''
      this.text=''
      this.selected=''
      for(let i=2019;i<=new Date().getFullYear();i++) {
          let obj={}
          obj.value=String(i)
          obj.text=String(i)
          this.years.push(obj)
      }
      this.text=''
      if(this.currentPage==1) {
          this.$router.push({
            path: '/projectBoard',
            query:{page:1}
          })
      }
      axios.get('/api/user').then(response => { // 프로젝트 이름 가져오기
               this.userType=response.data.userType
              }).catch((erro) => {
              console.error(erro);
        });
       axios.get('/api/projectBoard/subjects') // 모든 과목 정보
        .then(response => {
            for(let i=0;i<response.data.length;i++) {
                let obj={}
                obj.value=String(response.data[i].subjectId)
                obj.text=String(response.data[i].year)+"-"+String(response.data[i].semester)+" "+response.data[i].title
                this.subjects.push(obj)
            }
            
        });
        // console.log("query:"+this.$route.query.page)
        this.getResults();
    },
    
  methods: {
      getResults(){
          if(this.$route.query.type!=null) {
         axios.get('/api/projectBoard/search?type='+this.$route.query.type+'&text='+this.$route.query.text).then(response => { 
                this.paginatedItems=response.data
              }).catch((erro) => {
              console.error(erro);
            });
        } else {
            axios.get('/api/projectBoard/filter?grade='+this.$route.query.grade+'&year='+this.$route.query.year+'&subject='+this.$route.query.subject+'&tag='+this.$route.query.tag).then(response => { 
                this.paginatedItems=response.data
              }).catch((erro) => {
              console.error(erro);
            });
        }
      },
    resetModal(){
        this.summaryData={
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
            createName:'',
            allMemGrade:[],
            subjectName:'',
        }
    },
    search() {
        if(this.selected==null) {
            alert('셀렉트를 선택해주세요.')
            return;
        } else if(this.text.length==0) {
            alert('내용을 입력하세요')
            return;
        }
        this.$router.push({
            path: '/projectSearch',
            query:{type:this.selected,text:this.text}
        })
    },
    changeStar(projectId) {
        axios.post('/api/changeStar', {
            projectId:projectId
        }).then(response => {
            this.data = response.data
            this.getResults();
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
    filter() {
    //     tags:[],
    //    selectedGrade:'',
    //    selectedYear:'',
    //    selectedSubject:'',
        if(this.selectedGrade.length==0 && this.selectedYear.length==0 && this.selectedSubject.length==0 && this.tags.length==0) {
            alert('검색 필터 내용을 하나 이상 채우세요')
            return;
        } 
        this.$router.push({
                path: '/projectSearch',
                query:{
                    grade:String(this.selectedGrade),
                    year:String(this.selectedYear),
                    subject:String(this.selectedSubject),
                    tag:String(this.tags.toString())
                }
            })

    },
    
  },
  
}
</script>