<template>
    <div class="containerStyle">
        <h3 style="margin-left: 5%">공지 게시판</h3>
        <hr style="width: 90%">
        <center>
            <b-form-group label-for="SubjectNotice">
                <table class="table table-bordered" id="SubjectNotice" style="width: 90%">
                    <tr>
                        <th class="th1" style="width: 20%">제목</th>
                        <td>{{list.title}}</td>
                    </tr>
                    <tr>
                        <td colspan="2" style="height: 400px ; min-height: 400px">{{ list.content }}</td>
                    </tr>
                    <tr>
                        <th class="th1">제출 기한</th>
                        <td>{{ list.deadlineTime=='1000-01-01T00:00:00'?'-':list.deadlineTime.substring(0,10)+" "+list.deadlineTime.substring(11,16)}}</td>
                    </tr>
                    <tr>
                        <th class="th1">연장 기한</th>
                        <td>{{ list.deadlineTime=='1000-01-01T00:00:00'?'-':list.extentionTime.substring(0,10)+" "+list.extentionTime.substring(11,16) }}</td>
                    </tr>
                    <tr>
                        <th class="th1">제출 여부</th>
                        <td>{{list.deadlineTime=='1000-01-01T00:00:00'?'-':'X'}}</td>
                    </tr>
                                    </table>
                <div style="text-align: right ; margin-right: 5%">
                    <b-button variant="dark">수정</b-button>
                </div>
            </b-form-group>
        </center>
    </div>
</template>

<script>
import axios from 'axios'
export default {
    name: 'noticeContent',
    data() {
        return {
            list: {},
        }
    },
    mounted() { 
        axios.get('/api/noticeBoard/post/'+this.$route.params.postId) // 모든 과목 정보
        .then(response => {
            this.list=response.data
            
        });
    }
}
</script>