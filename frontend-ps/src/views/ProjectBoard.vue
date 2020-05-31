<template>
    <div class="containerStyle">
        <b-row>
            <h4>프로젝트 게시판</h4>
        </b-row>

        <!--필터검색-->
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
        <!--전체 프로젝트 목록-->
        <b-row cols-md="3" cols="1">
            <b-col class="mb-5" :key="index" v-for="(item, index) in paginatedItems">
                <b-card align="left" bg-variant="dark" text-variant="white" style="height:250px">
                    <template v-slot:header>
                        <h5 style="display:inline"><b>{{item.project.projectName}}</b></h5>
                    </template>
                    <b-card-text>
                        <p style="font-size:17px">
                            <b-icon-people-fill scale=1.1 style="margin-right:10px"></b-icon-people-fill><b>{{item.project.memNum}}</b>
                        </p>
                        <h6><b-badge variant="secondary" v-for="(tag,index) in item.project.tag.split(',')" :key="index" style="margin-right:5px">
                            {{tag}}
                        </b-badge></h6>
                        <div style="margin-top:10px">
                            <h5 style="display:inline-block;"><b-badge variant="success" v-if="item.project.progState==false">진행중</b-badge></h5>
                            <h5 style="display:inline-block;"><b-badge variant="danger" v-if="item.project.progState==true">진행완료</b-badge></h5>
                            <h5 style="display:inline-block; margin-left:10px"><b-badge variant="success" v-if="item.project.rcrtState==false">모집중</b-badge></h5>
                            <h5 style="display:inline-block; margin-left:10px"><b-badge variant="danger" v-if="item.project.rcrtState==true">모집완료</b-badge></h5>
                        </div>
                    </b-card-text>
                    <template v-slot:footer>
                        <b-icon @click="changeStar(item.project.projectId,item.star)" style="cursor:pointer;" scale=1.3 :icon="item.star==true?'heart-fill':'heart'"></b-icon>
                        <b-button @click="sendInfo(item.project.projectId)" v-b-modal.modal-xl size='sm' style="float:right">자세히</b-button>
                    </template>
                </b-card>  
            </b-col>
        </b-row>
        <!--페이지네이션, 검색-->
        <b-pagination  @change="onPageChanged" :total-rows="totalRows" :per-page="perPage" v-model="$route.query.page" class="my-0"></b-pagination>
        <b-row class="mt-4" style="">
            <b-col><b-form-select v-model="selected" :options="options"></b-form-select></b-col>
            <b-col><b-form-input v-model="text"></b-form-input></b-col>
            <b-col><b-button @click="search()">검색</b-button></b-col>
        </b-row>

        <!--프로젝트 정보 모달-->
        <b-modal id="modal-xl" size="lg" title="프로젝트 개요" ref="modal" data-backdrop="static">
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
                <b-button variant="danger" v-if="this.summaryData.state==2&& this.summaryData.project.rcrtState==false" @click="projectJoin(summaryData.project.projectId)">참가 신청하기</b-button>
                <b-button variant="warning" v-else-if="this.summaryData.state==0" >승인 대기</b-button>
                <b-button variant="success" v-else-if="this.summaryData.state==1" >참가중</b-button>

            <template v-slot:modal-footer="{cancel}">
                <b-button size="sm" @click="cancel()">닫기</b-button>
            </template>
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
    }
  },
   watch: {
      '$route'(){
          this.tags=[]
            this.selectedGrade=''
            this.selectedYear=''
            this.selectedSubject=''
        //   console.log('to:'+parseInt(to.params.page)+" from:"+parseInt(from.params.page))
          console.log('query111:'+this.$route.query.page)
          axios.get('/api/projectBoard?page='+this.$route.query.page).then(response => { // 프로젝트 이름 가져오기
                this.paginatedItems=response.data
              }).catch((erro) => {
              console.error(erro);
            });
      }
  },
  mounted() {
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
       axios.get('/api/projectBoard/subjects') // 모든 과목 정보
        .then(response => {
            for(let i=0;i<response.data.length;i++) {
                let obj={}
                obj.value=String(response.data[i].subjectId)
                obj.text=String(response.data[i].year)+"-"+String(response.data[i].semester)+" "+response.data[i].title
                this.subjects.push(obj)
            }
            
        });
        axios.get('/api/all/projectsNum') // 내 프로젝트 모든 목록
        .then(response => {
            // this.data = response.data 
            // this.totalRows=this.data.length
            // this.paginatedItems=this.data
            this.totalRows=response.data
            console.log("totalRows:"+this.totalRows)
            //this.paginate(this.perPage, 0)
        });
        console.log("query:"+this.$route.query.page)
        axios.get('/api/projectBoard?page='+this.$route.query.page).then(response => { // 프로젝트 이름 가져오기
            this.paginatedItems=response.data
            this.summaryData=response.data[0]
            }).catch((erro) => {
            console.error(erro);
        });
    },
  computed: {
    // pageCount() {
    //   let l = this.totalRows,
    //     s = this.perPage;
    //   return Math.floor(l / s);
    // },
    
  },
  methods: {
     paginate (page_size, page_number) {
         this.$router.push({
            path: '/projectBoard',
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
                axios.get('/api/projectBoard?page='+this.$route.query.page).then(response => { // 프로젝트 이름 가져오기
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