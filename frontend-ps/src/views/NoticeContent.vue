<template>
    <div class="containerStyle">
        <h3 style="margin-left: 5%">공지 게시판</h3>
        <hr style="width: 90%">
        <center>
            <b-form-group label-for="SubjectNotice">
                <table class="table table-bordered" id="SubjectNotice" style="width: 90%">
                    <tr>
                        <th class="th1" style="width: 20%">제목</th>
                        <td>안녕하세요 공지입니다.</td>
                    </tr>
                    <tr>
                        <td colspan="2" style="height: 400px ; min-height: 400px">{{ list.content }}</td>
                    </tr>
                    <tr>
                        <th class="th1">제출 기한</th>
                        <td>{{ list.date }}</td>
                    </tr>
                    <tr>
                        <th class="th1">연장 기한</th>
                        <td>{{ list.date2 }}</td>
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
// import axios from 'axios'
export default {
    name: 'noticeContent',
    data() {
        return {
            list: { title: '안녕하세요 공지입니다.', content: '안녕하세요. xxx교수입니다.'
            + ' 코로나 사태로 인하여 대면 강의 수업이 전부 온라인 강의로 변경되었으므로 일정을 조금 변경하였습니다.'
            + ' 다음주까지 현재 진행중인 프로젝트 작업물을 제출하고, 자세한 일정은 강의 동영상에서 설명할테니 참고 바랍니다.', date: '2020-05-31', date2: '2020-06-01' }
        }
    }
}
</script>