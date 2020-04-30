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

                    <b-button style="clear:both" type="submit" variant="secondary"
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
                        {{member.user.userNum}} {{member.user.name}}
                    </li>
                </ul>
            </div>
            <hr>
            <div style="padding:30px;">
            <h5>초대 이력</h5>
                <ul style="list-style:none; padding:0;">
                    <li class="mt-3" v-for="invit in inviteList" :key="invit.JoinId">
                        {{invit.user.userNum}} {{invit.user.name}} - {{state[invit.state]}}
                    </li>
                </ul>
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
            userNum: '',
            projectId: this.$route.params.projectId,
            memberList: [],
            inviteList: [],
            state: ["대기", "수락", "거절"],
            projectTitle:''

        }
    },
    mounted() {
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
        
    },
    methods: {
        invite(evt) {
            evt.preventDefault();

             axios.get('/api/invite/'+this.projectId+"/"+this.userNum)
            .then(response => {alert(response.data);
                location.reload();
            })
            .catch((erro)=> {
                console.error(erro);
            });            
        }
    },
}
</script>
<style>
h5{font-weight: bold;}
</style>