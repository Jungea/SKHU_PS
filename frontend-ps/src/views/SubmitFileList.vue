<template>
    <div class="scoreStyle">
        <b-row>
            <b-col style="background ;margin-right: -100px">
            <center>
                <h4 style="text-align: left ; margin-left: 20%">{{ title }}의 과제 제출 현황</h4>
                <hr style="width: 60%">
                <form ref="form">
                    <b-form-group label-for="SubjectStudent">
                        <table class="table table-bordered" id="SubjectStudent" v-bind="data" style="width: 60%">
                            <tbody>            
                                <tr>
                                    <th class="th1" style="width: 30%">프로젝트 이름</th>
                                    <th class="th1" style="width: 13%">이름</th>
                                    <th class="th1">제출 파일</th>
                                    <th class="th1" style="width: 12%">점수</th>
                                    <th class="th1" style="width: 5%">조별</th>
                                </tr>
                                <tr :key="index" v-for="(item, index) in memberList" v-bind:class="{check1: (indexList.indexOf(index)!=-1&&index!=0)}">
                                    <td class="td1" style="vertical-align: middle">{{ item.project.projectName }}</td>
                                    <td class="td1" style="vertical-align: middle">{{ item.user.name }}</td>
                                    <td class="td1" style="vertical-align: middle">
                                        <div v-for="(file, index) in item.files" :key="index" class="fileItem" style="cursor:pointer ; color:blue" @click="download(file)">
                                            <div>{{ file.name }}</div>
                                        </div>
                                    </td>
                                    <td class="td1">
                                        <center><b-input @change="allCheck(indexList.indexOf(index))" v-model="scoreList[index]" style="text-align: center ; width:50px"></b-input></center>
                                    </td>
                                    <td class="td1" style="vertical-align: middle;" v-bind:class="{check2: (indexList.indexOf(index)==-1)}">
                                        <div style="padding-left: 10px ; padding-top: 5px" v-if="indexList.indexOf(index)!=-1">
                                            <b-checkbox @change="info(indexList.indexOf(index))" v-model="checkBox[indexList.indexOf(index)]" size="lg"></b-checkbox>
                                        </div>
                                    </td>
                                </tr>
                            </tbody>
                        </table>
                        <div v-if="data" style="text-align: right ; margin-right: 20%"><b-button variant="dark" @click="save()">저장</b-button></div>
                    </b-form-group>                
                </form>
            </center>
            </b-col>
        </b-row>
    </div>
</template>

<script>
import axios from 'axios';
export default {
    name: 'score',
    data() {
        return {
            data: [],
            files: [],
            title: '',
            viewToggle: false,
            memberList: [],
            scoreList: [],
            indexList: [],
            checkBox: [],
            a:[],
        }
    },
    updated() {
        
    },
     mounted() {
        axios.get('/api/project/getScore?postId='+this.$route.params.postId).then(response => { // 프로젝트 이름 가져오기
            if(response.data==null) {
                this.scoreList=[]
            } else {
                this.scoreList=response.data
            }
            }).catch((erro) => {
                console.error(erro);
        });
        axios.get('/api/noticeBoard/submitFiles/' + this.$route.params.subjectId + '/'+this.$route.params.postId)
        .then(response => {
            this.data=response.data

            axios.get('/api/subject/' + this.$route.params.subjectId + '/member?sort=project')
            .then(respon => {
                this.memberList = respon.data
                var i = 0 ;
                var j = 0 ;
                var array1 = [];
                var array2 = [];

                for(i = 0 ; i < this.memberList.length ; i++)
                    array1.push(this.memberList[i].project.projectName)

                for(i = 0 ; i < this.data.length ; i++)
                    array2.push(this.data[i].project.projectName)

                for(; j < this.data.length ; j++) {
                    this.indexList.push(array1.indexOf(array2[j]))
                }

                for(j = 0 ; j < this.indexList.length ; j++)
                    this.checkBox.push(false)
            })
        }),
        // axios.get('/api/subject/' + this.$route.params.subjectId + '/projects')
        // .then(response => {
        //     this.data = response.data
        //     for(let i = 0 ;i < response.data.length ; i++)
        //         this.files.push('2017320 ' + this.data[i].user.name + '.txt')
        // }),
        axios.get('/api/noticeBoard/post/'+this.$route.params.postId) // 게시글 정보
        .then(response => {
            this.title = response.data.title
        });
    },
    methods: {
        download(file) {
            // axios.get('/api/file1/download/'+file.fileId).then(response => {
            //     var fileURL = window.URL.createObjectURL(new Blob([response.data]));
            //     var fileLink = document.createElement('a');
            //     fileLink.href = fileURL;
            //     fileLink.setAttribute('download', file.name);
            //     document.body.appendChild(fileLink);
            //     fileLink.click();
            // })
            axios({
                method: 'GET',
                url: '/api/file1/download/'+file.fileId,                 
                responseType: 'blob' // 가장 중요함
            }).then(response => {
                var fileURL = window.URL.createObjectURL(new Blob([response.data]));
                var fileLink = document.createElement('a');
                fileLink.href = fileURL;
                fileLink.setAttribute('download', file.name);
                document.body.appendChild(fileLink);
                fileLink.click();
            })    
        },
        save() {
            alert("점수가 저장되었습니다.")
            
            axios.post('/api/project/score', {
                memberList:this.memberList,
                scoreList:this.scoreList,
                postId:this.$route.params.postId,
            })

        },
        info(value) {
            if(!this.checkBox[value])
                alert("팀장의 점수에 조별 점수를 입력해주세요.")  
        },
        allCheck(value) {
            if(this.checkBox[value]) {
                let index = 0;
                if(value == this.indexList.length-1)
                    index = this.memberList.length - this.indexList[value];
                else
                    index = this.indexList[value+1] - this.indexList[value];

                for(let i = 0 ; i < index ; i++)
                    this.scoreList[this.indexList[value]+i] = this.scoreList[this.indexList[value]]
                
                this.$forceUpdate()
            }
        }
    }
}
</script>

<style>
    .th1 { background-color: rgb(243,243,243) ; text-align: center }
    .td1 { text-align: center }
    .fileItem:hover { text-decoration: underline }
    .scoreStyle {
        margin: 100px 100px 100px 0;
        min-width: 350px;
    }
    /* .check1 { border-top: 5px solid RGB(222,226,230) !important} */
    .check1 { border-top-style: double }
    .check2 { border-top: hidden !important}

</style>