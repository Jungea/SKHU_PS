<!--props로 구현한 원본 -->
<template>
  <div class="home h-100">
    <b-row class="h-100">
      <b-col class="myNav overflow-auto" cols="200px">
          <!--home.vue에서 사이드바에서 셀렉트한 값의 pin이 바뀔 때 사이드바도 자동으로 바뀌게 하기 위해서.
          home.vue이외에는 myProjectPin대신에 [0] 넣기-->
        <Sidebar v-bind:my-project-list="myProjectPin"/>
      </b-col>
      <b-col>
        <center>    
            <b-container class="bv-row">    
                <b-row class="m-4">    
                    <b-col>  
                            <h4>내 프로젝트</h4>      
                    </b-col>    
                </b-row>    
                <b-row style="padding-left: 94%">    
                    <b-button v-b-modal.modal-xl variant="outline-secondary" style="width: 80px ; height: 70px;">프로젝트 생성</b-button>    
                </b-row>    
                <b-row cols="2">    
                    <b-col :key="index" v-for="(item, index) in this.data">
                    <b-card @click="viewSummary" align="left" bg-variant="dark"  text-variant="white" style="width: 30rem; height: 12rem; margin: 60px;"> <!-- 30rem == 480px -->
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
            @ok="submit">
            <form ref="form" @submit.stop.prevent="handleSubmit">
            <b-form-group>
            <table class="table table-bordered">
                <tbody>
                    <tr>
                        <th scope="row" style="width:28%">프로젝트명</th>
                        <td><b-form-input  v-model="projectName" id="input-default" placeholder="프로젝트 이름을 입력하세요"></b-form-input></td></tr>
                    <tr>
                        <th scope="row">주제</th>
                        <td><b-form-input required id="input-default2" :state="nameState" v-model="theme" placeholder="주제를 입력하세요"></b-form-input></td>
                    </tr>
                    <tr>
                        <th scope="row">내용</th>
                        <td><b-form-textarea id="textarea-state" v-model="content" placeholder="내용을 입력하세요"></b-form-textarea></td>
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
                        </td>
                    </tr>
                    <tr>
                        <th scope="row">인원 모집 상태</th>
                        <td><b-form-select v-model="rcrtState" :options="options"></b-form-select></td>
                    <tr>
                        <th scope="row">과목 인증키 </th>
                        <td><b-form-input id="input-default3" v-model="authKey" placeholder="수강 과목의 인증키를 입력하세요"></b-form-input></td>
                    </tr>
                </tbody>
 
            </table>
            </b-form-group>
            </form>
        </b-modal>
      </b-col>
    </b-row>
  </div>
</template>

<script>
import Sidebar from '@/components/Sidebar.vue'
import axios from 'axios';
export default {
  name: 'Home',
  components: {
    Sidebar,
  },
    data() {
        return {
            data:{},
            // 프로젝트 생성 모달 창
            rcrtState:false,
            projectName:'',
            theme:null,
            content:'',
            options: [
                { value: false, text: '모집중' },
                { value: true, text: '모집 완료' },
            ],
            tagArray:[],
            authKey:'',
            stringTag:'',
            project:null,
            nameState:null,
            myProjectPin:[], // 내가 pin한 프로젝트 목록
        };
    },
    mounted() { 
        axios.get('/api/user/projects') // 내 프로젝트 모든 목록
        .then(response => {
            this.data = response.data 
            // alert('json:'+JSON.stringify(this.data[0].pin))
            let count=0;
            this.myProjectPin=[]
            for (let i=0;i<this.data.length;i++) {
                if(this.data[i].pin==true) {
                    this.myProjectPin[count]={}
                    this.myProjectPin[count].projectName=this.data[i].projectName
                    this.myProjectPin[count++].projectId=this.data[i].projectId
                }
            }
        });
    },

     methods: {
                viewSummary() {                    
                    this.$router.push({
                    path: '/summary'
                    })
                },
                changePin(itemProjectId) {   // pin이 바뀌었을때
                    axios.post('/api/changePin', {
                        projectId:itemProjectId
                    })
                    .then(response => {this.data = response.data
                        let count=0;
                        this.myProjectPin=[]
                        for (let i=0;i<this.data.length;i++) {
                            if(this.data[i].pin==true) {
                                this.myProjectPin[count]={}
                                this.myProjectPin[count].projectName=this.data[i].projectName
                                this.myProjectPin[count++].projectId=this.data[i].projectId
                            }
                        }
                    })
                    
                    event.stopPropagation()
                },
        handleSubmit() {
            // Exit when the form isn't valid
            if (!this.checkFormValidity()) {
                return true
            }
        },
        checkFormValidity() {
            const valid = this.$refs.form.checkValidity()
            this.nameState = valid
            return valid
        },
        resetModal() {
            this.projectName='';
            this.theme='';
            this.content='';
            this.stringTag='';
            this.tagArray=[];
            this.rcrtState=false;
            this.authKey='';
            this.project=null;
            this.nameState=null
        },
        submit(bvModalEvt) {
            bvModalEvt.preventDefault()
            if(this.handleSubmit()) {
                alert('alert:'+false)
                return
            } else {
            console.log('name:'+this.projectName);
            for(var i in this.tagArray) {
               this.stringTag+=this.tagArray[i]+",";
            }
            this.stringTag=this.stringTag.slice(0,this.stringTag.length-1);
            alert(this.stringTag);
            axios.post('/api/makeProject',{
                projectName:this.projectName,
                theme:this.theme,
                content:this.content,
                tag:this.stringTag,
                rcrtState:this.rcrtState,
                authKey:this.authKey,
            }).then(response => { 
                this.project = response.data;
                if(this.project=='authKey를 잘못 입력했습니다') {
                    alert('authKey가 일치한 과목이 없습니다');
                    return;
                } else {
                    this.$nextTick(() => {
                    this.$bvModal.hide('modal-xl')
                    })
                    alert('성공!');
                }
            });
            }
        },
    }
}

</script>

<style>

.card-header { font-size: 20px }
.card-text { font-size: 13px }

</style>