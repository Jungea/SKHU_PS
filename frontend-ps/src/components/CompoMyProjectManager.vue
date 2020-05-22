<template>
    <div class="p-5">
        <h2 class="mb-4">프로젝트 관리</h2>

        <div style="padding: 30px ; padding-bottom: 0">
            <h4>설정</h4>
            <hr>
            <table class="table table-bordered" v-if="!edit">
            <tbody>
                <tr>
                    <th style="width:28%">과목</th>
                    <td>
                        {{ subjectName }}
                    </td>
                </tr>
                <tr>
                    <th style="width:28%">진행 상태</th>
                    <td>
                        {{ progStateName }}
                    </td>
                </tr>
                <tr>
                    <th style="width:28%">모집 상태</th>
                    <td>
                        {{ rcrtStateName }}
                    </td>
                </tr>
            </tbody>
            </table>
            <table class="table table-bordered" v-if="edit">
            <tbody>
                <tr>
                    <th style="width:28%">과목</th>
                    <td>
                        <b-input v-model="subjectPass"></b-input>
                    </td>
                </tr>
                <tr>
                    <th style="width:28%">진행 상태</th>
                    <td>
                        <b-select v-model="projectInfo.progState" :options="progStateArray" value-field="item" text-field="text"></b-select>
                    </td>
                </tr>
                <tr>
                    <th style="width:28%">모집 상태</th>
                    <td>
                        <b-select v-model="projectInfo.rcrtState" :options="rcrtStateArray" value-field="item" text-field="text"></b-select>
                    </td>
                </tr>
            </tbody>
            </table>
            <div class="mb-4" style="text-align: right" v-if="!edit">
                <b-button variant="dark" @click="stateEdit()">수정</b-button>
            </div>
            <div class="mb-4" style="text-align: right" v-if="edit">
                <b-button variant="dark" @click="stateEdit()">저장</b-button>
            </div>
        </div>
        <div style="padding:30px;">
            <h4>멤버 관리</h4>
            <hr>
            <div style="padding:30px;">
                <h5>팀원 초대</h5>
                <b-form @submit="invite" style="margin-top:30px">
                <b-form-group
                    id="input-group-1"
                    label="학번"
                    label-for="invite"
                >
                    <b-form-input
                    id="invite"
                    v-model="userNum"
                    type="search"
                    required
                    placeholder="Enter member number"
                    class="mr-2"
                    style="width:40%;float:left"
                    ></b-form-input>

                    <b-button style="clear:both" type="submit"
                    :disabled="userNum.length == 0">초대</b-button>
                    <b-form-text id="help1">초대하고 싶은 사람의 학번을 입력하세요.</b-form-text>
                </b-form-group>
                </b-form>
            </div>
            <hr>
            <div style="padding:30px;">
                <h5>현재 팀 구성원</h5>
                <ul style="list-style:none; padding:0; margin-top:30px">
                    <li class="mt-3" v-for="member in memberList" :key="member.JoinId">
                        <span style="float:left">
                            {{member.user.userNum}} {{member.user.name}}
                        </span>
                        <b-button @click="exile(member)" size="sm" style="clear:both; margin-left:20px">내보내기</b-button>
                    </li>
                </ul>
            </div>

            <hr>
            <div style="padding:30px;">
                <h5>초대한 이력</h5>
                <ul style="list-style:none; padding:0;">
                    <li class="mt-3" v-for="invit in inviteList" :key="invit.JoinId">
                        <span style="float:left">
                            {{invit.user.userNum}} {{invit.user.name}} - {{state[invit.state]}}
                        </span>
                        <b-button @click="deleteWaiting(invit)" size="sm" style="clear:both; margin-left:20px">초대 취소</b-button>
                    </li>
                </ul>
            </div>

            <hr style="margin:30px 0;">
            <div class="infoContainer">
                <h5>신청받은 이력</h5>
                <div style="margin-top:30px; padding: 30px;">
                    <b-row>
                        <b-col style="min-width:350px;" class="mt-0">
                            <b-card>
                                <ul style="list-style:none; padding:0 20px">
                                    <b-media class="inviteList" tag="li" v-for="application in applicationList" :key="application.joinId">
                                        <h5 style="display:inline;">{{application.user.name}}({{application.user.userNum}})</h5>
                                        <span style="float:right">
                                            <b-button size="sm" @click="turnState(application, 1)" class="mr-2" variant="success">수락</b-button>
                                            <b-button size="sm" @click="turnState(application, 2)" variant="danger">거절</b-button>
                                        </span>
                                    </b-media>
                                </ul>
                            </b-card>
                        </b-col>
                    </b-row>
                </div>
            </div>

            <br/>
        </div>
    </div>
</template>
<script>
import axios from 'axios';
export default {
    data() {
        return {
            user:{},
            projectInfo:{},
            userNum: '',
            projectId: this.$route.params.projectId,
            memberList: [],
            inviteList: [],
            state: ["대기", "수락", "거절"],
            projectTitle:'',
            applicationList: [],
            
            subjectArray: [ {item: '123', text: '과목1'}, {item: '1234', text: '과목2'}, {item: '12345', text: '과목3'}],
            subjectPass: '',
            subjectName: '123입력',

            progStateArray: [ {item: false, text:'진행중'}, {item: true, text: '진행완료'}],
            rcrtStateArray: [ {item: false, text:'모집중'}, {item: true, text: '모집완료'}],
            progStateName: '',
            rcrtStateName: '',
            edit: false,
        }
    },
    mounted() {
        axios.get('/api/project/'+this.projectId)
        .then(response => {
            this.projectInfo = response.data
            this.progStateCheck()
            this.rcrtStateCheck()
        }).catch((erro) => {
            console.error(erro);
        })        

        //로그인한 유저가 누구인지 확인
        axios.get('/api/user')
            .then(response => {
                this.user = response.data
            }).catch((erro)=> {
                console.error(erro);
        });

        axios.get('/api/project/'+this.projectId+"/member")
            .then(response => this.memberList=response.data)
            .catch((erro)=> {
                console.error(erro);
            });

        axios.get('/api/project/'+this.projectId+"/inviteList")
            .then(response => this.inviteList=response.data)
            .catch((erro)=> {
                console.error(erro);
            });

        axios.get('/api/project/'+this.projectId+"/applicationList")
            .then(response => this.applicationList=response.data)
            .catch((erro)=> {
                console.error(erro);
            });
    },
    methods: {
        progStateCheck() {
           if(this.projectInfo.progState == false) {
                this.progStateName = '진행중'
           }
            else {
                this.progStateName = '진행완료'
            }
        },
        rcrtStateCheck() {
           if(this.projectInfo.rcrtState == false) {
                this.rcrtStateName = '모집중'
           }
            else {
                this.rcrtStateName = '모집완료'
            }
        },
        stateEdit() {
            if(this.edit == false)
                this.edit = true;
            else
            {
                if(this.subjectPass != '') {
                    for(var i = 0 ; i < this.subjectArray.length ; i++) {
                        if(this.subjectPass == this.subjectArray[i].item)
                            this.subjectName = this.subjectArray[i].text
                    }
                }
                else
                    this.subjectName = '';

                axios.post('/api/project/'+this.$route.params.projectId+'/edit',{
                    projectId: this.projectInfo.projectId,
                    projectName: this.projectInfo.projectName,
                    theme: this.projectInfo.theme,
                    content: this.projectInfo.content,
                    tag: this.projectInfo.tag,
                    github: this.projectInfo.github,
                    progState: this.projectInfo.progState,
                    rcrtState: this.projectInfo.rcrtState
                    })
                .then().catch((erro)=> { console.error(erro);
                });

                this.progStateCheck()
                this.rcrtStateCheck()
                this.edit = false;
            }
        },
        invite(evt) {
            evt.preventDefault();

             axios.get('/api/invite/'+this.projectId+"/"+this.userNum)
            .then(response => {alert(response.data);
                location.reload();
            })
            .catch((erro)=> {
                console.error(erro);
            });            
        },

        //초대 취소
        deleteWaiting(invite){
                if(invite.project.user.userId==this.user.userId){  //팀장인 경우 => 나중에 이게 팀장만 보는 부분으로 수정되면 없애도 됨
                    if(confirm("초대를 취소합니까?")){
                        axios.post('/api/deletejoin/'+invite.joinId)
                        .then()
                        .catch((erro)=>{
                            console.error(erro);
                        })
                        location.reload();
                        alert('취소되었습니다.')
                    }         
                }
                else
                    alert('권한이 없습니다')
        },

        //멤버 추방
        exile(member){
            axios.get('/api/project/'+this.projectId)
            .then(response=>{
                if(response.data.user.userId==this.user.userId){
                    if(confirm(member.user.name+"님을 "+response.data.projectName+"에서 내보냅니까?")){
                        axios.post('/api/exile/'+member.user.userId+'/'+this.projectId)
                        .then()
                        .catch((erro)=>{
                            console.error(erro);
                        })
                        location.reload();
                        alert('내보냈습니다.')
                    }
                }
                else
                    alert('권한이 없습니다')
            })
            .catch((erro)=>{
                console.error(erro);
            })
            
        },

        // 신청 수락,거절
        turnState(application, state){
            if(state==1){
                console.log('수락함')
                axios.post('/api/increaseMember/'+application.project.projectId)
                .then()
                .catch((erro)=> {
                    console.error(erro);
                });
            }
            else{
                console.log('거절함')
            }

            axios.post('/api/turnjoinstate/'+application.joinId+'/'+state)
            .then(() => location.reload() )
            .catch((erro)=> {
                console.error(erro);
            });
        },
    },
}
</script>
<style>
h5{font-weight: bold;}
</style>