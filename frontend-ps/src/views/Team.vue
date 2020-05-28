<template>
    <div class="home">
        <center>    
            <b-container class="bv-row">
                <b-row class="m-4">
                    <b-col><h4>과목 프로젝트 팀 목록</h4></b-col>
                </b-row>
                <b-row class="text-right mb-4">
                </b-row>
                <p style="color:silver; margin:300px auto;" v-if="data.length==0?true:false">해당 과목에 참가한 팀(프로젝트)이 없습니다.</p>
                <b-row cols-md="2" cols="1">
                    <b-col class="mb-5" :key="index" v-for="(item, index) in data">
                        <b-card @click="viewSummary(item.projectId)" align="left" bg-variant="dark" text-variant="white" style="width: 80% ; height: 15rem;"> <!-- 30rem == 480px -->
                            <div>
                                <b-card-header style="padding: 0 0 10px 0">
                                    <table>
                                        <tr>
                                            <td style="width: 100%">
                                                {{item.user.name}} 조
                                            </td>
                                        </tr>
                                    </table>
                                </b-card-header>
                            </div>
                            <b-card-text style="margin-top: 10px ; font-size: 15px">
                                <p>프로젝트 이름: {{ item.projectName }} <br/> 프로젝트 주제: {{ item.theme }}</p>
                                <b-button variant="success" style="cursor:default;" v-show="item.progState==false">진행중</b-button>
                                <b-button variant="danger"  style="cursor:default" v-show="item.progState==true">진행완료</b-button>
                            </b-card-text>
                        </b-card>  
                    </b-col>
                </b-row>    
            </b-container>    
        </center>    
    </div>
</template>

<script>
import axios from 'axios';
export default {
    name: 'subjectTeam',
    data() {
        return {
            data:{}
        };
    },
    mounted() { 
        axios.get('/api/subject/' + this.$route.params.subjectId + '/projects') // 프로젝트 팀(인원)
        .then(response => {
            this.data = response.data
        });
    },

     methods: {
        viewSummary(projectId) {
            this.$router.push({
                path: '/project/' + projectId + '/summary'
            })
        }
    }
}

</script>

<style>
.card-header { font-size: 20px }
.card-text { font-size: 13px }
</style>