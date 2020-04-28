<template>
    <div class="p-4">
        <h2 class="mb-4">프로젝트 관리</h2>
        <hr>
        <div>
            <h5>팀원 초대</h5>
            <b-form inline @submit="invite">
                <label class="sr-only" for="inline-form-input-userNum">Number</label>
                <b-input class="mr-2" type="search" v-model="userNum" placeholder="Enter member number"></b-input>
                <b-button variant="primary" type="submit" :disabled="userNum.length == 0">Invite</b-button>
            </b-form>
            <br/>

            <h5>팀원 목록</h5>
            <ul>
                <li v-for="member in memberList" :key="member.JoinId">
                    {{member.user.userNum}} {{member.user.name}}
                </li>
            </ul>
            <br/>

            <h5>초대 이력</h5>
            <ul>
                <li v-for="invit in inviteList" :key="invit.JoinId">
                    {{invit.user.userNum}} {{invit.user.name}} - {{state[invit.state]}}
                </li>
            </ul>
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
            projectId: 5, //수정필요
            memberList: [],
            inviteList: [],
            state: ["대기", "수락", "거절"]

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
<style lang="stylus" scoped>

</style>