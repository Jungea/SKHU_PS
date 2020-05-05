<template>
  <div class="home">
    <b-row class="h-100">
        <b-col>
        <div>
        <center>    
            <b-container class="bv-row">    
                <b-row class="m-4">    
                    <b-col>  
                            <h4>내 프로젝트</h4>      
                    </b-col>    
                </b-row>    
                <b-row style="padding-left: 94%">    
                    <b-button v-b-modal.modal-xl variant="outline-secondary" style="min-width: 100px ; height: 70px;">프로젝트 생성</b-button>    
                </b-row>
                <p style="color:silver; margin:300px auto;" v-if="data.length==0?true:false">프로젝트가 비었습니다.</p>     
                <b-row cols="2">    
                    <b-col :key="index" v-for="(item, index) in data">
                    <b-card @click="viewSummary(item.projectId)" align="left" bg-variant="dark" text-variant="white" style="width: 30rem; height: 15rem; margin: 60px;"> <!-- 30rem == 480px -->
                        <div>
                            <b-card-header style="padding: 0 0 10px 0">
                                <table>
                                    <tr>
                                        <td style="width: 100%">{{item.projectName}}</td>
                                        <td><b-button @click="changePin(item.projectId)" style="background-color: rgb(52,58,64) ; border-color: rgb(52,58,64) ; margin-top: -10px" ><b-icon scale=1.5 v-bind:icon="item.pin==true?'star-fill':'star'"></b-icon></b-button></td>

                                    </tr>
                                </table>
                            </b-card-header>
                       </div>
                        <b-card-text style="margin-top: 10px">
                            <p style="font-size:17px">인원 :{{item.memNum}}명</p>
                            <b-button variant="success" style="cursor:default;" v-show="item.progState==false">진행중</b-button>
                            <b-button variant="danger"  style="cursor:default" v-show="item.progState==true">진행완료</b-button>
                        </b-card-text>    
                    </b-card>  

                    </b-col>
                </b-row>    
            </b-container>    
        </center>    

        <!--프로젝트 생성 모달 -->
        <b-modal id="modal-xl" size="lg" title="프로젝트 생성" 
            @show="resetModal"
            @hidden="resetModal"
            @ok="handleOk" ref="modal" data-backdrop="static">
            <form ref="form" @submit.stop.prevent="handleSubmit">
            <b-form-group
                :state="projectState"
                label=""
                label-for="createProject"
                invalid-feedback="입력하지 않은 필수 입력 사항이 있습니다."
            >
                <table class="table table-bordered" id="createProject">
                    <tbody>
                        <tr>
                            <th scope="row" style="width:28%">프로젝트명</th>
                            <td>
                                <b-form-input v-model="projectName" id="input-default" required placeholder="프로젝트 이름을 입력하세요"></b-form-input>
                                <b-form-valid-feedback :state="nameValidation">
                                    아주 nice!
                                </b-form-valid-feedback>
                                <b-form-invalid-feedback :state="nameValidation">
                                    프로젝트 이름은 필수 입력사항입니다.
                                </b-form-invalid-feedback>
                            </td>
                        </tr>
                        <tr>
                            <th scope="row">주제</th>
                            <td>
                                <b-form-input id="input-default2" v-model="theme" placeholder="주제를 입력하세요"></b-form-input>
                                <b-form-valid-feedback :state="themeValidation">
                                    감동적인 주제입니다.
                                </b-form-valid-feedback>
                                <b-form-invalid-feedback :state="themeValidation">
                                    주제는 필수 입력사항입니다.
                                </b-form-invalid-feedback>
                            </td>
                        </tr>
                        <tr>
                            <th scope="row">내용</th>
                            <td>
                                <b-form-textarea id="textarea-state" v-model="content" placeholder="내용을 입력하세요"></b-form-textarea>
                                <b-form-text id="input-live-help">프로젝트에 대한 간단한 설명을 적어주세요.</b-form-text>
                                <b-form-valid-feedback :state="contentValidation">
                                    완전 대박 예감!
                                </b-form-valid-feedback>
                                <b-form-invalid-feedback :state="contentValidation">
                                    내용은 필수 입력사항입니다.
                                </b-form-invalid-feedback>
                            </td>
                        </tr>
                        <tr>
                            <th scope="row">사용기술과 언어</th>
                            <td>
                                <b-form-tags
                                    input-id="tags-remove-on-delete"
                                    :input-attrs="{ 'aria-describedby': 'tags-remove-on-delete-help' }"
                                    v-model="tagArray"
                                    separator=" ,;"
                                    placeholder="태그를 입력하세요"
                                    remove-on-delete
                                    add-on-enter
                                    class="mb-2"
                                ></b-form-tags>
                                <b-form-text id="input-live-help2">프로젝트에 쓰일 언어나 기술, 도구를 입력하세요.</b-form-text>
                                <b-form-valid-feedback :state="tagValidation">
                                    이걸 쓴다니...당신은 천재인가요??
                                </b-form-valid-feedback>
                                <b-form-invalid-feedback :state="tagValidation">
                                    태그를 하나 이상 입력해주세요.
                                </b-form-invalid-feedback>
                            </td>
                        </tr>
                        <tr>
                            <th scope="row">깃허브<br>리포지토리 주소</th>
                            <td>
                                <b-form-input required id="github" v-model="repoUrl" placeholder="주소를 입력하세요"></b-form-input>
                                <b-form-text id="input-live-help1">프로젝트를 등록한 깃허브 리포지토리가 있다면 입력하세요.</b-form-text>
                            </td>
                        </tr>
                        <tr>
                            <th scope="row">인원 모집 상태</th>
                            <td>
                                <b-form-select v-model="rcrtState" :options="options"></b-form-select>
                                <b-form-text id="input-live-help3">팀원 모집 여부를 선택합니다. 혼자 하는 경우 '모집 완료'를 선택하세요.</b-form-text>
                            </td>
                        <tr>
                            <th scope="row">과목 여부</th>
                            <td>
                                <b-form-checkbox v-model="subjectChecked" name="check-button" switch>
                                </b-form-checkbox>
                                <b-form-text id="input-live-help4">프로젝트 수업의 프로젝트인 경우 설정을 켜세요.</b-form-text>
                            </td>
                        </tr>
                        <tr v-if="subjectChecked">
                            <th scope="row">과목 인증키</th>
                            <td>
                                <b-form-input style="width:75%; margin-right:10px; float:left;" id="input-default3" v-model="authKey" placeholder="인증키를 입력하세요" :disabled="authCheck==true"></b-form-input>
                                <b-button style="width:15%; clear:both" @click="checkAuth()">검사</b-button>
                                <b-form-text id="input-live-help5">해당 수업의 인증키를 입력하세요.</b-form-text>
                                <b-form-valid-feedback :state="subjectValidation">
                                </b-form-valid-feedback>
                                <b-form-invalid-feedback :state="subjectValidation">
                                    인증키를 입력하세요.
                                </b-form-invalid-feedback>
                                <b-form-valid-feedback :state="authValidation">
                                    올바른 인증키입니다.
                                </b-form-valid-feedback>
                                <b-form-invalid-feedback :state="authValidation">
                                    인증키 검사를 완료해주세요.
                                </b-form-invalid-feedback>
                            </td>
                        </tr>
                    </tbody>
    
                </table>
            </b-form-group>
            </form>
        </b-modal>
    </div>
      </b-col>
    </b-row>
  </div>
</template>

<script>
import axios from 'axios';
// import Sidebar from '@/components/Sidebar.vue'
export default {
  name: 'Home',
  components: {
    // Sidebar,
  },
    data() {
        return {
            data:{},
            // 프로젝트 생성 모달 창
            rcrtState:false,
            projectName:'',
            theme:'',
            content:'',
            options: [
                { value: false, text: '모집중' },
                { value: true, text: '모집 완료' },
            ],
            tagArray:[],
            authKey:'',
            stringTag:'',
            project:null,
            subjectChecked:false,
            repoUrl:'',
            authkeys:[],
            authCheck:false
        };
    },
    mounted() { 
        axios.get('/api/user/projects') // 내 프로젝트 모든 목록
        .then(response => {
            this.data = response.data 
            // alert('json:'+JSON.stringify(this.data[0].pin))
            
        });
    },

     methods: {
                changePin(itemProjectId) {   // pin이 바뀌었을때
                    axios.post('/api/changePin', {
                        projectId:itemProjectId
                    })
                    .then(response => {
                        this.data = response.data
                    })
                    
                    event.stopPropagation()
                    location.reload()
                },
                viewSummary(projectId) {
                    this.$router.push({
                    path: '/project/'+projectId+'/summary'
                })
        },
        
        //모달메소드
        handleOk(bvModalEvt) {
            // Prevent modal from closing
            bvModalEvt.preventDefault()
            // Trigger submit handler
            this.handleSubmit()
        },
        handleSubmit() {
            if (!this.nameValidation || !this.themeValidation || !this.contentValidation
                || !this.tagValidation || !this.subjectValidation || !this.authValidation) {
                return 
            }
            
            this.submit()
            this.$nextTick(() => {
                this.$bvModal.hide('modal-xl')
            })
        },
        resetModal() {
            this.projectName='';
            this.theme='';
            this.content='';
            this.stringTag='';
            this.tagArray=[];
            this.rcrtState=false;
            this.authKey='';
            this.repoUrl='';
            this.subjectChecked=false;
            //this.projectState=false;
        },

        checkAuth(){
            axios.post('/api/subjects')
            .then(response => {
                for (var i in response.data){
                    this.authkeys.push(response.data[i].authKey)
                }
                //console.log(this.authkeys)
                if(this.authkeys.indexOf(this.authKey)==-1){
                    alert('유효하지 않은 인증키입니다.')
                    this.authKey=''
                }
                else{
                    alert('올바른 인증키입니다.')
                    this.authCheck=true
                }
            }).catch((erro)=> {
                console.error(erro);
            });
        },

        submit() {
            console.log('눌렀다')
            console.log('name:'+this.projectName);
            for(var i in this.tagArray) {
               this.stringTag+=this.tagArray[i]+",";
            }
            this.stringTag=this.stringTag.slice(0,this.stringTag.length-1);
            axios.post('/api/makeProject',{
                projectName:this.projectName,
                theme:this.theme,
                content:this.content,
                tag:this.stringTag,
                rcrtState:this.rcrtState,
                authKey:this.authKey,
                github: this.repoUrl
            }).then(response => { 
                this.project = response.data;
                location.reload();
                // if(this.project=='authKey를 잘못 입력했습니다') {
                //     alert('입력한 인증키와 일치하는 과목이 없습니다');
                //     return;
                // } else {
                //     alert('성공!');
                //     location.reload();
                // }
            });
        },
    },
    computed:{
        nameValidation(){        //플젝명 체크
             return this.projectName.replace(/ /g,"").length>0
         },
         themeValidation(){           //플젝 체크
             return this.theme.replace(/ /g,"").length>0
         },
         contentValidation(){           //내용 체크
             return this.content.replace(/ /g,"").length>0
         },
         tagValidation(){            //태그 체크
             return this.tagArray.length>0
         },
         subjectValidation(){            //과목 체크
            if(this.subjectChecked==true){
                return this.authKey.replace(/ /g,"").length>0
            }
            else
                return true
         },
         authValidation(){
            if(this.subjectChecked==true){
                if(this.authCheck==true)
                    return true
                else
                    return false
            }
            else
                return true
             
         },
         projectState(){
             return this.nameValidation&&this.themeValidation&&this.contentValidation
                    &&this.tagValidation&&this.subjectValidation&&this.authValidation
        },
    }
}

</script>

<style>
.card-header { font-size: 20px }
.card-text { font-size: 13px }
</style>