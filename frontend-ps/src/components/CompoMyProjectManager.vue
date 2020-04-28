<template>
    <div class="p-4">
        <h2 class="mb-4">프로젝트 관리</h2>
        <hr>
        <div>
            <h5>팀원</h5>
            <b-form inline @submit="invite">
                <label class="sr-only" for="inline-form-input-userNum">Number</label>
                <b-input class="mr-2" type="search" v-model="userNum" placeholder="Enter member number"></b-input>
                <b-button variant="primary" type="submit" :disabled="userNum.length == 0">Invite</b-button>
            </b-form>
        </div>
    </div>
</template>
<script>
import axios from 'axios';
export default {
    data() {
        return {
            userNum: '',
            projectId: 5,
        }
    },
    methods: {
        invite(evt) {
            evt.preventDefault();

             axios.get('/api/invite/'+this.projectId+"/"+this.userNum)
            .then(response => alert(response.data)).catch((erro)=> {
                console.error(erro);
            });
            // response => {this.name = response.data.name}
            
        }
    },
}
</script>
<style lang="stylus" scoped>

</style>