<template>
    <div class="p-5">
        <h2 class="mb-4">프로젝트 관리</h2>

        <div style="padding:30px;">
            <h4>멤버 관리</h4>
            <hr>

            <div style="padding:30px;">
                <h5>팀원 초대</h5>
                <b-form @submit="invite" style="margin-top:30px">
                    <b-form-group
                        id="input-group-1"
                        label="학번"
                        label-for="invite">

                        <b-form-input
                            id="invite"
                            v-model="userNum"
                            type="search"
                            required
                            placeholder="Enter member number"
                            class="mr-2"
                            style="width:40%;float:left"></b-form-input>

                        <b-button style="clear:both" type="submit" :disabled="userNum.length == 0">초대</b-button>
                        <b-form-text id="help1">초대하고 싶은 사람의 학번을 입력하세요.</b-form-text>
                    </b-form-group>
                </b-form>
            </div>
            <hr>

            <div style="padding:30px;">
                <b-card no-body header="현재 팀 구성원">
                    <b-list-group flush>
                        <b-list-group-item class="inviteList" v-for="member in memberList" :key="member.JoinId">
                            <p style="display:inline; font-size: 12pt">
                                {{member.user.name}}({{member.user.userNum}})
                            </p>
                            <b-button @click="exile(member)" size="sm" variant="danger" style="float:right">내보내기</b-button>
                        </b-list-group-item>
                    </b-list-group>
                    <b-card-body v-if="memberList.length==0">
                        나 이외에 다른 구성원이 없습니다.
                    </b-card-body>
                </b-card>
            </div>
            <hr>

            <div style="padding:30px;">
                <b-card no-body header="초대 이력">
                    <b-list-group flush>
                        <b-list-group-item class="inviteList" v-for="invit in inviteList" :key="invit.JoinId">
                            <p style="display:inline; font-size: 12pt">
                                {{invit.user.name}}({{invit.user.userNum}})<b-badge variant="warning" class="ml-2">{{state[invit.state]}}</b-badge>
                            </p>
                            <b-button @click="deleteWaiting(invit)" size="sm" variant="danger" style="float:right">초대 취소</b-button>
                        </b-list-group-item>
                    </b-list-group>
                    <b-card-body v-if="inviteList.length==0">
                        프로젝트에 초대한 인원이 없습니다.
                    </b-card-body>
                </b-card>
            </div>
            <hr style="margin:30px 0;">

            <div style="padding:30px;">
                <b-card no-body header="신청자 목록">
                    <b-list-group flush>
                        <b-list-group-item class="inviteList" tag="li" v-for="application in applicationList" :key="application.joinId">
                            <p style="display:inline; font-size: 12pt">{{application.user.name}}({{application.user.userNum}})</p>
                            <span style="float:right">
                                <b-button size="sm" @click="turnState(application, 1)" class="mr-2" variant="success">수락</b-button>
                                <b-button size="sm" @click="turnState(application, 2)" variant="danger">거절</b-button>
                            </span>
                        </b-list-group-item>
                    </b-list-group>
                    <b-card-body v-if="applicationList.length==0">
                        현재 프로젝트에 대한 신청자가 없습니다.
                    </b-card-body>
                </b-card>
            </div>
            <br/>
        </div>

        <!-- 설정 -->
        <div style="padding: 30px;">
            <h4>설정</h4>
            <hr>
            <div style="padding:30px;">
            <table class="table table-bordered" v-if="!edit">
                <tbody>
                    <tr>
                        <th style="width:28%">인원 모집 상태</th>
                        <td>
                            <span v-if="projectInfo.rcrtState==false">모집중</span>
                            <span v-if="projectInfo.rcrtState==true">모집 안함</span>
                        </td>
                    </tr>
                    <tr>
                        <th style="width:28%">프로젝트 진행 상태</th>
                        <td>
                            <span v-if="projectInfo.progState==false">진행중</span>
                            <span v-if="projectInfo.progState==true">진행 완료</span>
                        </td>
                    </tr>
                    <tr>
                        <th style="width:28%">과목</th>
                        <td>
                            <span v-if="subjectName.length!=0">{{ subjectName }}</span>
                            <span v-if="subjectName.length==0">과목 없음</span>
                        </td>
                    </tr>
                </tbody>
            </table>

            <!-- 설정 수정 상태 -->
            <table class="table table-bordered" v-if="edit">
                <tbody>
                    <tr>
                        <th style="width:28%">인원 모집 상태</th>
                        <td>
                            <b-select v-model="projectInfo.rcrtState" :options="rcrtStateArray" value-field="item" text-field="text"></b-select>
                        </td>
                    </tr>
                    <tr>
                        <th style="width:28%">프로젝트 진행 상태</th>
                        <td>
                            <b-select v-model="projectInfo.progState" :options="progStateArray" value-field="item" text-field="text"></b-select>
                        </td>
                    </tr>
                    <tr>
                        <th style="width:28%">과목 인증 키</th>
                        <td>
                            <b-input v-model="subjectPass"></b-input>
                        </td>
                    </tr>
                </tbody>
            </table>

            <div class="mb-4" style="text-align: right" v-if="!edit">
                <b-button variant="dark" @click="stateEdit()">수정</b-button>
            </div>

            <div class="mb-4" style="text-align: right" v-if="edit">
                <b-button class="mr-3" variant="dark" @click="edit = false">취소</b-button>
                <b-button variant="dark" @click="stateEdit()">저장</b-button>
            </div>
            <div class="mb-4" style="text-align: right">
                <b-button variant="danger" @click="deleteProject()">프로젝트 삭제</b-button>
            </div>
            </div>

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
            
            subjectPass: '',
            subjectName: '',

            progStateArray: [ {item: false, text:'진행중'}, {item: true, text: '진행 완료'}],
            rcrtStateArray: [ {item: false, text:'모집중'}, {item: true, text: '모집 안함'}],
            progStateName: '',
            rcrtStateName: '',
            edit: false,

            newProgState: false,
            newRcrtState: false,

        }
    },
    mounted() {
        //설정 데이터
        this.projectReload();   
        //로그인한 유저가 누구인지 확인
        axios.get('/api/user')
            .then(response => {
                this.user = response.data
            }).catch((erro)=> {
                console.error(erro);
        });
        //팀원(팀장 제외)
        this.memberReload();
        //초대한 이력
        this.invitationReload();
        //신청받은 이력
        this.applicationReload();
    },
    methods: {

        //설정 데이터
        projectReload() {
            axios.get('/api/project/'+this.projectId)
                .then(response => {
                    this.projectInfo = response.data;
                    this.subjectName = this.projectInfo.subject != null? this.projectInfo.subject.title : '';
                    console.log(this.projectInfo)
                    // this.progStateCheck()
                    // this.rcrtStateCheck()
                }).catch((erro) => {
                    console.error(erro);
                }) 
        },
        //팀원(팀장 제외)
        memberReload() {
            axios.get('/api/project/'+this.projectId+"/member")
                .then(response => this.memberList = response.data || [])
                .catch((erro)=> {
                    console.error(erro);
                });
        },
        //초대한 이력
        invitationReload() {
            axios.get('/api/project/'+this.projectId+"/inviteList")
                .then(response => this.inviteList = response.data || [])
                .catch((erro)=> {
                    console.error(erro);
                });
        },
        //신청받은 이력
        applicationReload() {
            axios.get('/api/project/'+this.projectId+"/applicationList")
                .then(response => this.applicationList = response.data || [])
                .catch((erro)=> {
                    console.error(erro);
                });
        },
        stateEdit() {
            if(this.edit) { // 저장버튼을 누름
                axios.put('/api/project/'+this.$route.params.projectId+'/projectManager/setting', {
                        progState: this.projectInfo.progState,
                        rcrtState: this.projectInfo.rcrtState,
                        subjectAuthKey: this.subjectPass
                    }).then(() => {this.projectReload(); this.edit = false;}).catch(erro => console.error(erro));
                
            } else { // 수정 버튼을 누름
                this.subjectPass = this.projectInfo.subject != null? this.projectInfo.subject.authKey : '';
                this.newProgState = this.projectInfo.progState;
                this.newRcrtState = this.projectInfo.rcrtState;
                this.edit = true;
            }
            
        },
        invite(evt) {
            evt.preventDefault();
            if(this.projectInfo.user.userId == this.user.userId){
                axios.get('/api/invite/'+this.projectId+"/"+this.userNum)
                .then(response => {
                    alert(response.data);
                    this.invitationReload();
                })
                .catch((erro)=> {
                    console.error(erro);
                });
            }
            else
                alert('권한이 없습니다')
            this.userNum = '';          
        },

        //초대 취소
        deleteWaiting(invite){
                if(this.projectInfo.user.userId == this.user.userId){  //팀장인 경우 => 나중에 이게 팀장만 보는 부분으로 수정되면 없애도 됨
                    if(confirm("초대를 취소합니까?")){
                        axios.post('/api/deletejoin/'+invite.joinId)
                            .then(() => { this.invitationReload(); alert('취소되었습니다.') })
                            .catch(erro => console.error(erro))
                        
                    }         
                }
                else
                    alert('권한이 없습니다')
        },

        //멤버 추방
        exile(member){
            if(this.projectInfo.user.userId==this.user.userId) {
                if(confirm(member.user.name+"님을 "+this.projectInfo.projectName+"에서 내보냅니까?")) {
                    axios.post('/api/exile/'+member.user.userId+'/'+this.projectId)
                        .then(() => { this.memberReload(); alert('내보냈습니다.') })
                        .catch( erro => console.error(erro) )
                }
            }
            else
                alert('권한이 없습니다')
        },

        // 신청 수락,거절
        turnState(application, state){
            axios.post('/api/turnjoinstate/'+application.joinId+'/'+state)
            .then(() => {
                this.applicationReload();
                this.memberReload();
            })
            .catch((erro)=> {
                console.error(erro);
            });
        },
        // 프로젝트 삭제
        deleteProject() {
            let result=confirm('이 프로젝트를 삭제하시겠습니까?')
            if(result) {
                axios.post('/api/project/delete',{
                    projectId:this.$route.params.projectId
                })
                .then(() => {
                    this.$router.push({
                        path: '/home',
                    })
                })
            }
         }
    },
}
</script>
<style>
h5{font-weight: bold;}
</style>