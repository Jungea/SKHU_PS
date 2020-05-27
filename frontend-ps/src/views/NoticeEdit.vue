<template>
    <div class="containerStyle">
        <h3 style="margin-left: 5%">게시글 수정</h3>
        <hr style="width: 90%">
        <center>
            <b-form-group label-for="SubjectNotice">
                <table class="table table-bordered" id="SubjectNotice" style="width: 90%">
                    <tr>
                        <th class="th1" style="width: 20% ; vertical-align: middle">제목</th>
                        <td><b-input v-model="list.title"></b-input></td>
                    </tr>
                    <tr>
                        <td colspan="2"><b-textarea v-model.trim="list.content" style="height: 400px ; min-height: 400px ; max-height: 800px"></b-textarea></td>
                    </tr>
                    <tr>
                        <th class="th1" style="vertical-align: middle">첨부 파일</th>
                        <td>
                            <b-input v-model="deadline" :disabled="true" style="width: 60% ; float: left ; margin-right: 15px"></b-input>
                            <b-button variant="dark" @click="addFile()" style="margin-right: 15px">첨부 파일 선택</b-button>
                            <b-button variant="dark" @click="resetFile()">삭제</b-button>
                        </td>
                    </tr>
                    <tr>
                        <th class="th1" style="vertical-align: middle">제출 기한</th>
                        <td>
                            <b-input v-model="deadlineTime" :disabled="true" style="width: 60% ; float: left ; margin-right: 15px"></b-input>
                            <b-button variant="dark" v-b-modal.calendar1 @click="newNowDate()" style="margin-right: 15px">제출 기한 선택</b-button>
                            <b-button variant="dark" @click="reset1()">삭제</b-button>
                        </td>
                    </tr>
                    <tr>
                        <th class="th1" style="vertical-align: middle">연장 기한</th>
                        <td>
                            <b-input v-model="extentionTime" :disabled="true" style="width: 60% ; float: left ; margin-right: 15px"></b-input>
                            <b-button variant="dark" v-b-modal.calendar2 @click="newNowDate()" style="margin-right: 15px">연장 기한 선택</b-button>
                            <b-button variant="dark" @click="reset2()">삭제</b-button>
                        </td>
                    </tr>
                </table>
                <div style="text-align: right ; margin-right: 5%">
                    <b-button @click="save()" variant="dark">저장</b-button>
                </div>
            </b-form-group>
        </center>

        <b-modal id="calendar1" ok-only no-close-on-backdrop>
            <b-calendar block @context="onContext1" :date-disabled-fn="dateDisabled1"></b-calendar>
        </b-modal>
        <b-modal id="calendar2" ok-only no-close-on-backdrop>
            <b-calendar block @context="onContext2" :date-disabled-fn="dateDisabled2"></b-calendar>
        </b-modal>
    </div>
</template>

<script>
import axios from 'axios'
export default {
    name: 'noticeEdit',
    data() {
        return {
            postId: '',
            nowDate: '',
            list: {  },
            deadlineTime:'',
            extentionTime:'',
        }
    },
    mounted() {
         axios.get('/api/noticeBoard/post/'+this.$route.params.postId) // 모든 과목 정보
        .then(response => {
            this.list=response.data
            if(this.list.deadlineTime!='1000-01-01T00:00:00' && this.list.extentionTime=='1000-01-01T00:00:00') {
                this.deadlineTime=this.list.deadlineTime.substring(0,10)
            } else if(this.list.deadlineTime!='1000-01-01T00:00:00' && this.list.extentionTime!='1000-01-01T00:00:00') {
                this.deadlineTime=this.list.deadlineTime.substring(0,10)
                this.extentionTime=this.list.extentionTime.substring(0,10)
            } 
        });
        
        this.postId = this.$route.params.postId
    },
    methods: {
        dateDisabled1(ymd, date) {
            let d = date;
            let date2 = this.leadingZeros(d.getFullYear(), 4) + '-' + this.leadingZeros(d.getMonth() + 1, 2) + '-' + this.leadingZeros(d.getDate(), 2);
            return date2 < this.nowDate
        },
        dateDisabled2(ymd, date) {
            let d = date;
            let date2 = this.leadingZeros(d.getFullYear(), 4) + '-' + this.leadingZeros(d.getMonth() + 1, 2) + '-' + this.leadingZeros(d.getDate(), 2);
            return date2 <= this.list.deadline
        },
        leadingZeros(n, digits) {
            var zero = '';
            n = n.toString();

            if (n.length < digits) {
                for (var i = 0; i < digits - n.length; i++)
                    zero += '0';
            }
                return zero + n;
        },
        newNowDate() {
            let d = new Date();
            this.nowDate = this.leadingZeros(d.getFullYear(), 4) + '-' + this.leadingZeros(d.getMonth() + 1, 2) + '-' + this.leadingZeros(d.getDate(), 2);
            if(this.list.deadline == '')
                this.list.deadline = this.nowDate
        },
        onContext1(ctx) {
            this.deadlineTime = ctx.activeYMD;
        },
        onContext2(ctx) {
            this.extentionTime = ctx.activeYMD;
        },
        reset1() {
            this.deadlineTime = ''
        },
        reset2() {
            this.extentionTime = ''
        },
        save() {
            if(((this.deadlineTime >= this.extentionTime) && (this.deadlineTime != '' && this.extentionTime != ''))
                || (this.deadlineTime == '' && this.extentionTime != '')) {
                alert("기한 설정이 잘못되었습니다.")
            }
            else {
                if(this.deadlineTime=='' && this.extentionTime=='') {
                    this.deadlineTime='1000-01-01'
                    this.extentionTime='1000-01-01'
                } else if(this.deadlineTime!='' && this.extentionTime=='') {
                    this.extentionTime='1000-01-01'
                }
                axios.post('/api/noticeBoard/modifyPost', {
                    postId:this.$route.params.postId,
                    title:this.list.title,
                    content:this.list.content,
                    deadlineTime:this.deadlineTime,
                    extentionTime:this.extentionTime,
                })
                .then(response => {
                    this.data = response.data
                    this.$router.push({
                        path: '/subject/'+this.$route.params.subjectId+'/noticeBoard/' + this.$route.params.postId
                    })
                })
            }
        },
        addFile() {
            alert("파일 추가")
        },
        resetFile() {
            alert("파일 전부 지우기")
        }
    }
}
</script>