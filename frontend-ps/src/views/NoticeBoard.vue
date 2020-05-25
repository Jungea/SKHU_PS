<template>
    <div class="containerStyle">
        <h3 style="margin-left: 5%">공지 게시판</h3>
        <hr style="width: 90%">
        <center>
            <b-form-group label-for="SubjectNotice">
                <table class="table table-bordered" id="SubjectNotice" style="width: 90%">
                    <tr>
                        <th class="th1">번호</th>
                        <th class="th1">제목</th>
                        <th class="th1">작성일</th>
                        <th class="th1">제출 마감일</th>
                    </tr>
                    <tr v-for="(item, index) in notice" :key="index" @click="viewContent(item.num)">
                        <td class="td1" style="width: 8%"> {{ item.num }} </td>
                        <td> <b> {{ item.title }} </b> </td>
                        <td class="td1" style="width: 20%"> {{ item.editDate }} </td>
                        <td class="td1"> {{ item.date }} </td>
                    </tr>
                </table>
                <div style="text-align: right ; margin-right: 5%">
                    <b-button variant="dark" v-b-modal.modal-newBoard>게시글 작성</b-button>
                </div>
            </b-form-group>
        </center>


     <b-modal id="modal-newBoard" size="lg"  @show="resetModal" @hidden="resetModal" title="게시글 작성" @ok="newBoard">
        <center>
            <b-form-group label-for="newNotice">
                <table class="table table-bordered" id="newNotice" >
                    <tr>
                       <th style="width:20%">제목</th>
                            <td>
                                <b-form-input v-model.trim="title" placeholder="게시글 제목을 입력하세요."></b-form-input>
                            </td>
                    </tr>
                    <tr>
                       <th>내용</th>
                            <td>
                                <b-form-textarea rows="9" v-model="content" placeholder="게시글 내용을 입력하세요."></b-form-textarea>
                            </td>
                    </tr>
                    <tr>
                        <td>과제 제출</td>
                        <td>
                            <b-input v-model="deadline" disabled="true" style="width: 60% ; float: left ; margin-right: 15px"></b-input>
                            <b-button variant="dark" v-b-modal.calendar1 @click="newNowDate()">제출 기한 선택</b-button>
                        </td>
                    </tr>
                    <tr>
                        <td>연장 기한</td>
                        <td>
                            <b-input v-model="extention" disabled="true" style="width: 60% ; float: left ; margin-right: 15px"></b-input>
                            <b-button variant="dark" v-b-modal.calendar2 @click="newNowDate()">연장 기한 선택</b-button>
                        </td>
                    </tr>
                </table>
            </b-form-group>
        </center>
    </b-modal>

    <b-modal id="calendar1" ok-only no-close-on-backdrop>
        <b-calendar block @context="onContext1" :date-disabled-fn="dateDisabled1"></b-calendar>
    </b-modal>
    <b-modal id="calendar2" ok-only no-close-on-backdrop>
        <b-calendar block @context="onContext2" :date-disabled-fn="dateDisabled2"></b-calendar>
    </b-modal>

    </div>    
</template>
<script>
// import axios from 'axios'
export default {
    name: 'noticeBoard',
    data() {
        return {
           notice: [
               { num: 5, title: '안녕하세요 공지입니다.', editDate: '2020-05-30 13:00', date: '-'},
               { num: 4, title: '05/30일까지 프로젝트 진행현황 제출하세요.', editDate: '2020-05-29 13:00', date: '2020-05-30 23:59'},
               { num: 3, title: '05/29일까지 프로젝트 진행현황 제출하세요.', editDate: '2020-05-28 13:00', date: '2020-05-29 23:59'},
               { num: 2, title: '05/28일까지 프로젝트 진행현황 제출하세요.', editDate: '2020-05-27 13:00', date: '2020-05-28 23:59'},
               { num: 1, title: '05/27일까지 프로젝트 진행현황 제출하세요.', editDate: '2020-05-26 13:00', date: '2020-05-27 23:59'}
           ],
           title: '',
           content: '',
           nowDate: '',
           deadline: '',
           extention: ''
        } 
    },
    methods: {
        resetModal() {
            this.title = '',
            this.content = '',
            this.deadline = '',
            this.extention = '',
            this.nowDate = ''
        },
        newBoard() {
            alert("게시글 제목: " + this.title + " / 게시글 내용: " + this.content + ' / 제출 기한: ' + this.deadline)
        },
        dateDisabled1(ymd, date) {
            let d = date;
            let date2 = this.leadingZeros(d.getFullYear(), 4) + '-' + this.leadingZeros(d.getMonth() + 1, 2) + '-' + this.leadingZeros(d.getDate(), 2);
            return date2 < this.nowDate
        },
        dateDisabled2(ymd, date) {
            let d = date;
            let date2 = this.leadingZeros(d.getFullYear(), 4) + '-' + this.leadingZeros(d.getMonth() + 1, 2) + '-' + this.leadingZeros(d.getDate(), 2);
            return date2 <= this.deadline
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
            if(this.deadline == '')
                this.deadline = this.nowDate
        },
        onContext1(ctx) {
            this.deadline = ctx.activeYMD;
        },
        onContext2(ctx) {
            this.extention = ctx.activeYMD;
        },
        viewContent(num) {
            this.$router.push({
                path: '/noticeContent/' + num
            })
        }
    }
}
</script>