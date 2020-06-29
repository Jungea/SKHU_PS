<!--경진대회 수상작-->
<template>
    <div>
        <b-jumbotron class="board-header">
            <p class="page-title">IT 경진대회</p>
            <hr class="my-3" style="width:150px;">
            <p><b>성공회대 IT 경진대회 수상작을 만나보세요.</b></p>
            <!--검색 토글-->
            <p style="text-align:right; margin:10px 20px">
                <b-icon v-b-toggle.collapse-1 :icon="!searchShow?'search':'x'" scale="1.7" style="cursor:pointer;" @click="searchToggle"></b-icon>
            </p>
            <!--검색 폼-->
            <b-collapse id="collapse-1">
                <!-- <b-card>I should start open!</b-card> -->
                <form class="">
                    <div class="form-wrapper">
                        <b-icon id="tooltip-filter" v-b-toggle.collapse-2 icon="filter" scale="1.4" class="search-box-icon" :variant="filter?'success':''" @click="filterToggle"></b-icon>
                        <b-tooltip triggers="hover" target="tooltip-filter">필터 설정</b-tooltip>
                        <!--필터 설정이 꺼진 경우-->
                        <input v-if="!filter" v-model="keyWord" class="search-box" type="text" placeholder="IT 경진대회 내에서 키워드 검색..."/>
                        <!--필터 설정이 켜진 경우-->
                        <input v-if="filter" class="search-box" placeholder="IT 경진대회 내에서 필터 검색..." disabled/>
                        <b-icon id="tooltip-search" icon="search" scale="1.4" class="search-box-icon"></b-icon>
                        <b-tooltip triggers="hover" target="tooltip-search">검색</b-tooltip>
                    </div>
                    <!--필터 collapse-->
                    <b-collapse id="collapse-2">
                        <div class="filter-wrapper">
                            <b-row style="margin: 0; padding:30px 50px" cols-lg="2" cols="1">
                                <b-col class="filters">
                                    <b-form-group>
                                        <label for="filter-title"><b>프로젝트 제목</b></label>
                                        <input class="filter-form" id="filter-title" type="search" v-model="filterTitle" placeholder="프로젝트 제목으로 찾기..." autocomplete="off"/>
                                    </b-form-group>
                                </b-col>
                                <b-col class="filters">
                                    <b-form-group>
                                        <label for="filter-captain"><b>팀장 이름</b></label>
                                        <input class="filter-form" id="filter-captain" type="search" v-model="filterCaptain" placeholder="팀장명으로 찾기..." autocomplete="off"/>
                                    </b-form-group>
                                </b-col>
                                <b-col class="filters">
                                    <b-form-group>
                                        <label for="filter-year"><b>연도</b></label>
                                        <b-form-select class="filter-form" id="filter-year" v-model="filterYear" :options="years" style="padding:0px"></b-form-select>
                                    </b-form-group>
                                </b-col>
                                <b-col class="filters">
                                    <b-form-group>
                                        <label for="filter-contest"><b>대회명</b></label>
                                        <b-form-select class="filter-form" id="filter-contest" v-model="filterContest" :options="contests" style="padding:0px"></b-form-select>
                                    </b-form-group>
                                </b-col>
                            </b-row>
                        </div>
                    </b-collapse>
                </form>
            </b-collapse>
        </b-jumbotron>
        <!--본문 영역-->
        <div class="containerStyle">
            <!--프로젝트 목록-->
            <div class="row" 
                v-infinite-scroll="loadMore" 
                infinite-scroll-disabled="busy" 
                infinite-scroll-distance="10"
                infinite-scroll-throttle-delay="50"
            >
                <div class="column mb-5" :key="index" v-for="(p, index) in filtered">
                    <div class="wrapper">
                        <b-card class="card" draggable="true">
                            <b-card-title class="cardTitle">
                                <h4 class="etc"><b>{{p.project.projectName}}</b></h4>
                            </b-card-title>
                            <b-card-text>
                                <span class="mr-3">{{p.project.year}}</span>
                                <span class="mr-3">|</span>
                                <span>{{p.project.user.name}}</span> <!--팀장명-->
                                <span v-if="p.project.memNum>1">외 {{p.project.memNum-1}}명</span>
                            </b-card-text>
                            <!--주제-->
                            <b-card-text class="mt-4">
                                <p class="etc" style="font-size:11pt; font-weight:bold">{{p.project.theme}}</p>
                            </b-card-text>
                            <!--태그-->
                            <b-card-text class="tags">
                                <span class="mr-2" style="font-size:12pt;" v-for="(tag, index) in p.project.tag.split(',')" :key="index">
                                    <b-badge variant="light" class="">#&nbsp;{{tag}}</b-badge>
                                </span>
                            </b-card-text>
                            <!--카드 하단-->
                            <b-card-text>
                                <div style="font-size: 2rem;">
                                    <!--모달 띄우기-->
                                    <b-button v-b-modal.project-modal squared variant="outline-secondary" size="sm" @click="getProjectInfo(p.project.projectId)">자세히</b-button>
                                    <!--좋아요-->
                                    <b-icon 
                                        :id="`tooltip-like-${p.project.projectId}`" 
                                        :icon="p.star==true?'heart-fill':'heart'" 
                                        scale="1.3" 
                                        class="like rounded-circle p-2" 
                                        variant="danger"
                                        @click="like(index, p.star)"
                                    ></b-icon>
                                    <b-tooltip triggers="hover" :target="`tooltip-like-${p.project.projectId}`">
                                        <span>좋아요<span v-if="p.star">&nbsp;취소</span></span>
                                    </b-tooltip>
                                </div>
                            </b-card-text>
                        </b-card>
                    </div>
                </div>
            </div>

            <!--로딩 스피너-->
            <div>
                <div v-if="!allLoaded" v-show="busy" class="d-flex justify-content-center mb-3">
                    <b-spinner variant="secondary" label="Loading..."></b-spinner>
                </div>
            </div>

            <!--프로젝트 정보 모달-->
            <b-modal 
                size="lg"
                id="project-modal"
                content-class="project-modal"
                header-class="modal-header"
                body-class="p-0 modal-body"
                title="프로젝트 정보"
                @hidden="resetModal"
            >
                <div style="width:calc(100% - 1px);" class="p-3 text-center" v-if="!modalLoaded">
                    <b-spinner label="Loading..."></b-spinner>
                </div>
                <div style="width:calc(100% - 1px);" v-if="modalLoaded"><!--크롬 너비 내림-->
                    <b-row cols="1" style="margin:0;">
                    <div class="board-header">
                        <p class="page-title etc">{{projectInfo.project.projectName}}</p>
                        <p style="margin:20px">{{projectInfo.project.theme}}</p>
                        <b-button squared variant="outline-secondary" class="m-1" size="sm"
                            @click="moveToProject(projectInfo.project.projectId)"
                        >
                        <!--:onclick="`window.open('/project/'+${projectInfo.project.projectId}+'/summary')`"-->
                            프로젝트 페이지
                        </b-button>
                        <!--깃허브 리포지토리로 이동-->
                        <b-button 
                            squared variant="outline-secondary" class="m-1" size="sm"
                            :onclick="`window.open('${projectInfo.project.github}')`"
                        >
                            <i class="fa fa-github" style="font-size:16pt"></i>&nbsp;github
                        </b-button>
                    </div>
                    </b-row>
                    <!--모달 내용-->
                    <b-row cols="1" class="modal-sector">
                        <b-row cols-md="2" cols="1" style="margin:10px 0; text-align:center;">
                            <b-col style="padding:5px;">
                                <span style="font-weight:bold;">참여 연도</span>
                                <h6 class="mt-2">{{projectInfo.project.year}}</h6>
                            </b-col>
                            <b-col style="padding:5px;">
                                <span style="font-weight:bold;">참여 대회</span>
                                <h6 class="mt-2">대회 이름</h6>
                            </b-col>
                        </b-row>
                    </b-row>
                    <b-row cols="1" class="modal-sector">
                        <div>
                            <span style="font-weight:bold">👥&nbsp;팀원 정보</span>
                            <b-icon v-b-toggle.member-collapse icon="chevron-down" variant="secondary" style="float:right; cursor:pointer; margin-top:4px"></b-icon>
                        </div>
                        <b-collapse visible id="member-collapse">
                            <div style="margin-top:20px">
                                <h5>
                                    <span class="badge badge-light mytag" v-for="mem in projectInfo.member" :key="mem.user.userId">
                                        <b-icon icon="person-fill"></b-icon>{{mem.user.name}}&nbsp;({{mem.user.userNum}})
                                    </span>
                                </h5>
                            </div>
                        </b-collapse>
                    </b-row>
                    <b-row cols="1" class="modal-sector">
                        <div>
                            <span style="font-weight:bold">📄&nbsp;프로젝트 내용</span>
                            <b-icon v-b-toggle.content-collapse icon="chevron-down" variant="secondary" style="float:right; cursor:pointer; margin-top:4px"></b-icon>
                        </div>
                        <b-collapse visible id="content-collapse">
                            <div style="margin:20px; padding:20px; background:whitesmoke; height:150px; overflow-y:scroll">
                                <p>{{projectInfo.project.content}}</p>
                            </div>
                        </b-collapse>
                    </b-row>
                    <b-row cols="1" class="modal-sector">
                        <div>
                            <span style="font-weight:bold">🔧&nbsp;기술 태그</span>
                            <b-icon v-b-toggle.tag-collapse icon="chevron-down" variant="secondary" style="float:right; cursor:pointer; margin-top:4px"></b-icon>
                        </div>
                        <b-collapse visible id="tag-collapse" v-if="projectInfo.project.tag">
                            <div style="margin-top:20px">
                                <h5>
                                    <span class="badge badge-light mytag" v-for="(tag, index) in projectInfo.project.tag.split(',')" :key="index">
                                        #&nbsp;{{tag}}
                                    </span>
                                </h5>
                            </div>
                        </b-collapse>
                    </b-row>
                </div>
                <template v-slot:modal-footer><div></div></template>
            </b-modal>
        </div>
        
        <!--메시지 박스-->
        <div v-if="allLoaded" style="text-align:center; padding-bottom:50px;">
            <h6 style="color:silver">더 이상 프로젝트가 없습니다.</h6>
        </div>

        <!--탑 버튼-->
        <div style="font-size: 50pt; position:fixed; bottom:20px;right:20px; z-index: 10; text-align:center">
            <b-icon icon="chevron-double-up" onclick="location.href='#'" class="top-button rounded-circle p-2 shadow" style="display:block; margin:10px auto;"></b-icon>
            <h6>Back to top</h6>
        </div>
        
    </div>
</template>

<script>
import axios from 'axios'
import infiniteScroll from 'vue-infinite-scroll'
export default {
    directives: {infiniteScroll},
    name: 'Contest',
    data() {
        return {
            projectCard:[],
            busy: false,
            next: {},
            allLoaded:false,

            //임시 연도와 대회
            contests:[
                { value: null, text: '선택 안함' },
                { value: '1', text: '대회1' },
                { value: '2', text: '대회2' },
                { value: '3', text: '대회3' },
                { value: '4', text: '대회4' },
            ],
            years:[
                { value: null, text: '선택 안함' },
                { value: '2019', text: '2019년도' },
                { value: '2020', text: '2020년도' },
            ],

            //검색 관련
            keyWord:'',
            filterTitle:'', filterCaptain:'', filterYear:null, filterContest:null,
            filter:false,
            searchShow:false,

            //모달 정보
            modalLoaded:false,
            projectInfo:{
                project:{},
                member:[]
            },


            test:[]
        }
    },

    mounted(){
        console.log('IT 경진대회')

        //검색창 항상 포커싱
        // setInterval(function(){
        //     var focusbox;
        //     focusbox = document.getElementById("input-box");
        //     focusbox.focus();
        // })

        //this.getAllProjects(); //infite scroll로 가져오므로 일단 주석처리
    },

    methods:{
        //infite scroll
        //페이지 최하단에서 발생되는 메소드
        loadMore() {
            if(this.allLoaded==false){
                this.busy = true // 무한 스크롤 기능 비활성화
                this.getWinners();
            }
        },

        //전체 프로젝트에서 contest 칼럼이 1인 프로젝트 가져오기
        getWinners(){
            // let params={
            //     limit:6,
            //     start:this.next
            // }
            axios.get('/api/allProjects')
            .then(response => {
                this.busy = false
                let limit=6;
                let arr=[];
                //let lastOne;
                for(let i=0;i<response.data.length;i++){
                    if(response.data[i].project.contest==1){
                        arr.push(response.data[i])
                        //lastOne=response.data[i]
                    }
                }
                //참가 연도로 정렬
                arr.sort(function(p1,p2){
                    return p2.project.year-p1.project.year
                })
                let arr2 = arr.slice(this.projectCard.length,this.projectCard.length + limit)
                if(arr2.length==0)
                    this.allLoaded=true
                this.projectCard=this.projectCard.concat(arr2);
                //this.next=lastOne;
            });
        },

        //좋아요
        like(index){
            // let y=document.scrollingElement.scrollTop
            // console.log(y)
            let pc=this.filtered[index]
            axios.post('/api/changeStar', {projectId:pc.project.projectId})
            .then(function(){
                axios.get('/api/projectBoard/modal/'+pc.project.projectId)
                .then(response=>{
                    pc.star=response.data.star
                })
            }
            )
        },

        //모달 정보
        getProjectInfo(pid){
            this.getMember(pid)
            this.getProject(pid)
        },

        getMember(pid){
            axios.get('/api/project/'+pid+'/allMember')
            .then(response => {
                console.log(response.data)
                this.projectInfo.member=response.data
                // this.modalLoaded=true
            })
        },

        getProject(pid){
            axios.get('/api/projectBoard/modal/'+pid)
            .then(response => {
                this.projectInfo.project=response.data.project
                this.modalLoaded=true
            })
        },

        resetModal(){
            this.projectInfo={
                member:[],
                project:{}
            }
            this.modalLoaded=false
        },

        moveToProject(pId){
            let Url='/project/'+pId+'/summary'
            this.$router.push({
                path: Url,
            })
        },
        
        //토글
        searchToggle(){
            this.searchShow=this.searchShow?false:true
            this.keyWord=''
            this.filterTitle=''; this.filterCaptain=''; this.filterYear=null; this.filterContest=null;
        },
        filterToggle(){
            this.filter=this.filter?false:true
            this.keyWord=''
            this.filterTitle=''; this.filterCaptain=''; this.filterYear=null; this.filterContest=null;
        },
    },
    computed:{
        checkLength(){
            if(this.filterTitle.length>0||this.filterCaptain.length>0||this.filterYear||this.filterYear)
                return true;
            return false
        },

        // serch(){
        //     //키워드 검색일때
        //     if(this.filter==false){
                
        //     }
        //     //필터 검색일때
        //     else{

        //     }
        // }

        filtered(){
            if(!this.filter){
                //키워드검색란에 무언가 입력되었을 때
                if(this.keyWord!=''){
                    //프로젝트가 모두 로드되지 않았을 때 검색하는 경우 여기서 로드시킴
                    if(this.allLoaded==false){this.getWinners()}
                    //조건에 맞는 프로젝트 배열 반환
                    //키워드 공백제거
                    let keyword=this.keyWord.replace(/ /g,"").toLowerCase();
                    return this.projectCard.filter(p=>{
                        // let regex = new RegExp(keyword, "g");
                        // p.project.projectName.replace(regex, "<span class='highlight'>" + keyword + "</span>");
                        return (
                            p.project.projectName.replace(/ /g,"").toLowerCase().includes(keyword)||  //플젝명
                            p.project.user.name.replace(/ /g,"").toLowerCase().includes(keyword)||  //팀장명
                            p.project.year==keyword||  //연도
                            p.project.content.replace(/ /g,"").toLowerCase().includes(keyword)||  //내용
                            p.project.tag.toLowerCase().includes(keyword)  //태그
                        )
                        
                    })
                }
                else return this.projectCard
            }
            else{
                if(this.checkLength){
                    if(this.allLoaded==false){this.getWinners()}
                    return this.projectCard.filter(p=>{
                        if(this.filterYear==null&&this.filterContest==null){
                            return (
                                p.project.projectName.replace(/ /g,"").toLowerCase().includes(this.filterTitle.replace(/ /g,"").toLowerCase())&&
                                p.project.user.name.replace(/ /g,"").toLowerCase().includes(this.filterCaptain.replace(/ /g,"").toLowerCase())
                            )
                        }
                        else if(this.filterYear==null&&this.filterContest!=null){
                            return (
                                p.project.projectName.replace(/ /g,"").toLowerCase().includes(this.filterTitle.replace(/ /g,"").toLowerCase())&&
                                p.project.user.name.replace(/ /g,"").toLowerCase().includes(this.filterCaptain.replace(/ /g,"").toLowerCase())
                                //대회 데이터 추가 후 아래 코드 주석 해제
                                //p.project.contest==this.filterContest
                            )
                        }
                        else if(this.filterYear!=null&&this.filterContest==null){
                            return (
                                p.project.projectName.replace(/ /g,"").toLowerCase().includes(this.filterTitle.replace(/ /g,"").toLowerCase())&&
                                p.project.user.name.replace(/ /g,"").toLowerCase().includes(this.filterCaptain.replace(/ /g,"").toLowerCase())&&
                                p.project.year==this.filterYear
                            )
                        }
                        else{
                            return (
                                p.project.projectName.replace(/ /g,"").toLowerCase().includes(this.filterTitle.replace(/ /g,"").toLowerCase())&&
                                p.project.user.name.replace(/ /g,"").toLowerCase().includes(this.filterCaptain.replace(/ /g,"").toLowerCase())&&
                                p.project.year==this.filterYear
                                //대회 데이터 추가 후 아래 코드 주석 해제
                                //p.project.contest==this.filterContest
                            )
                        }
                    })
                }
                else return this.projectCard
            }
        }

    },

    watch:{
        
    }

    
}
</script>

<style scoped>
.top-button{
    background: #f1f1f1;
    color:#555;
    cursor: pointer;
    transition:all 0.3s ease;
}

.top-button:hover{
    color: #f1f1f1;
    background:#555;
    transform: translateY(-10px);
}

.top-button~h6{
    opacity: 0;
    transition:all 0.3s ease;
}

.top-button:hover ~h6{
    opacity: 1;
}

/* 너비값 넘어가면 글자 숨기기 */
.etc {
    white-space: nowrap;
    overflow: hidden;
    text-overflow: ellipsis;
}

* {
    box-sizing: border-box;
}

*:focus { outline:none !important; }

.page-title{
    font-weight: bold;
    font-size: 20pt;
    margin:0 20px;
}

.board-header{
    text-align:center; border-radius:0; padding:30px; background:#f1f1f1;
}

/* 검색 관련 */
.form-wrapper{
    background:white; 
    width:90%; 
    margin:auto;
    display:flex; 
    flex-direction:row; 
    padding:10px;
}

.filter-wrapper{
    background:white; 
    width:90%;
    margin:auto; 
    border-top:1px solid #f1f1f1
}

.filters{
    background:white;
    display: inline-block;
    margin:5px 0;
    padding:20px 50px;
    text-align:left;
}

.filter-form{
    width:100%;
    padding:10px 0;
    border:none;
    border-bottom: 1px solid silver;
    border-radius: 0;
}

.filter-form:focus{
    outline: none;
    box-shadow: none;
}

input[type="search"]::-webkit-search-cancel-button{
    -webkit-appearance: none;
    cursor: pointer;
    height: 20px;
    width: 20px;
    background-image: url("data:image/svg+xml;charset=UTF-8,%3csvg fill='%23ccc' xmlns='http://www.w3.org/2000/svg' width='18' height='18' viewBox='0 0 24 24'%3e%3cpath d='M12 2C6.47 2 2 6.47 2 12s4.47 10 10 10 10-4.47 10-10S17.53 2 12 2zm5 13.59L15.59 17 12 13.41 8.41 17 7 15.59 10.59 12 7 8.41 8.41 7 12 10.59 15.59 7 17 8.41 13.41 12 17 15.59z'/%3e%3c/svg%3e");
}

.search-box{
    width:90%;border:none; margin:0 30px; font-size:12pt;
}

.search-box:disabled{
    background: white;
}

.search-box-icon{
    cursor: pointer; margin:10px;width:5%
}

/* 배치 -> css 없어도 cols로 됨..나중에 수정 예정*/
.column {
  float: left;
  width: 33%;
  padding: 0 20px;
}

.row {margin: 50px 0px;}

.row:after {
  content: "";
  display: table;
  clear: both;
}

/*화면따라 카드 사이즈 조정*/
@media (min-width: 850px) and (max-width: 1400px) {
    .column {
        width: 50%;
        display: block;
        margin-bottom: 20px;
    }
}
@media (max-width: 850px) {
    .column {
        width: 100%;
        display: block;
        margin-bottom: 20px;
    }
    .board-header{padding: 50px 0;}
    .filters{padding:20px 10px;}
}

/*카드 스타일*/
.card {
    box-shadow: 0 4px 8px 0 rgba(0, 0, 0, 0.2);
    text-align: left;
    background-color: #f1f1f1;
    border-radius: 0;
    z-index: 3;
}

/* .wrapper:hover{} */

.card:hover{
    /* transform: translateY(-20px); */
    background:whitesmoke;
}

.card .tags{
    height:30px; 
    overflow-y:scroll
}
.tags::-webkit-scrollbar {
    display:none;
}

.mytag{
    box-shadow: 5px 5px 5px rgb(211, 211, 211);
    border-radius: 0;
    margin: 5px 10px;
}

.like{
    cursor:pointer; float:right; margin-top:15px
}

.like:hover{
    background:rgb(211, 211, 211); translate: 0.5s;
}

/* 모달 관련 */
::v-deep .project-modal{
    /* top:50px; */
    border-radius: 0;
    border:none;
}

::v-deep .modal-header{
    border:none;
}

::v-deep .modal-title{
    font-size: 12pt; font-weight: bold;
}

::v-deep .modal-body{
    border:none;
}

::v-deep .modal-footer{
    padding:10px 20px;
    border:none;
}

.modal-sector{
    margin:0;border-bottom:1px solid #f1f1f1; padding:10px 30px;
}

/* 스크롤바 */
/* width */
::-webkit-scrollbar {
  width: 10px;
}

/* Track */
::-webkit-scrollbar-track {
  background: #f1f1f1; 
}
 
/* Handle */
::-webkit-scrollbar-thumb {
  background: #888; 
}

/* Handle on hover */
::-webkit-scrollbar-thumb:hover {
  background: #555; 
}
</style>