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
                            <b-form-file  multiple v-model="files2" class="mt-3" plain ></b-form-file>          
                            <div v-if="this.files.length!=0">
                                <hr>
                                <div v-for="(item, index) in files2" :key="index" > <!--추가하는 파일 이름-->
                                    {{ item.name }}
                                </div>
                                <div v-for="(item, index) in files" :key="index"> <!--기존 파일-->
                                    <div v-if="!removeFileId.includes(item.fileId)">
                                        <div style="cursor:pointer;color:blue ; display: inline-block" class="mb-2"  @click="download(item)">{{ item.name }} </div>
                                            <div class="ml-2" style="display: inline-block">
                                                <b-button pill size="sm" variant="outline-dark" @click="removeFile(item.fileId,index)">X</b-button>
                                            </div>                               
                                    </div>
                                </div>
                            </div>
                        </td>
                    </tr>
                    <tr>
                        <th class="th1" style="vertical-align: middle">제출 기한</th>
                        <td>
                            <b-input v-model="deadlineTime" :disabled="true" style="width: 60% ; float: left ; margin-right: 15px"></b-input>
                            <b-button variant="dark" v-b-modal.calendar1 @click="newNowDate()" style="margin-right: 15px">제출 기한 선택</b-button>
                            <b-button variant="danger" @click="reset(1)">삭제</b-button>
                        </td>
                    </tr>
                    <tr>
                        <th class="th1" style="vertical-align: middle">연장 기한</th>
                        <td>
                            <b-input v-model="extentionTime" :disabled="true" style="width: 60% ; float: left ; margin-right: 15px"></b-input>
                            <b-button variant="dark" :disabled="(deadlineTime.length==0)" v-b-modal.calendar2 @click="newNowDate()" style="margin-right: 15px">연장 기한 선택</b-button>
                            <b-button variant="danger" :disabled="(deadlineTime.length==0)" @click="reset(2)">삭제</b-button>
                        </td>
                    </tr>
                </table>
                <div style="text-align: right ; margin-right: 5%">
                    <b-button :disabled="checkTime()" @click="save()" variant="dark">저장</b-button>
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
            files:null, // 기존에 저장된 파일
            files2:null, // 새로 업로드 한 파일
            removeFileId:[],
        }
    },
    mounted() {
        axios.get('/api/file1/list/'+this.$route.params.postId) 
        .then(response => {
            this.files=response.data
        });
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
        checkTime() {
            let date1 = new Date(this.deadlineTime)
            let date2 = new Date(this.extentionTime)
            if(date1 >= date2)
                return true
            else
                return false
        },
        removeFile(fileId) {
            this.removeFileId.push(fileId)
        },
        download(file) {
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
        dateDisabled1(ymd, date) {
            let d = date;
            let date2 = this.leadingZeros(d.getFullYear(), 4) + '-' + this.leadingZeros(d.getMonth() + 1, 2) + '-' + this.leadingZeros(d.getDate(), 2);
            return date2 < this.nowDate
        },
        dateDisabled2(ymd, date) {
            let d = date;
            let date2 = this.leadingZeros(d.getFullYear(), 4) + '-' + this.leadingZeros(d.getMonth() + 1, 2) + '-' + this.leadingZeros(d.getDate(), 2);
            return date2 <= this.deadlineTime
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
            if(this.list.deadlineTime == '')
                this.list.deadlineTime = this.nowDate
        },
        onContext1(ctx) {
            this.deadlineTime = ctx.activeYMD;
        },
        onContext2(ctx) {
            this.extentionTime = ctx.activeYMD;
        },
        reset(id) {
            if(id == 1) {
                this.deadlineTime = ''
                this.extentionTime = ''
            }
            else
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
                    console.log(this.data = response.data)
                    // 파일 삭제
                    axios.post('/api/file1/delete/'+this.removeFileId.toString(), {
                        // fileIds: this.removeFileId.toString()
                    }).then(response => {
                        response.data
                    })

                    // 파일 추가
                    let formData = new FormData();
                    for(let i=0;i<this.files2.length;i++) {
                        formData.append("file", this.files2[i]);
                    }
                    axios.post('/api/file1/upload/'+this.$route.params.postId, 
                        formData,
                        {
                            headers: {
                                'Content-Type': 'multipart/form-data'
                            }
                        }
                    )
                    .then(response => {
                        console.log(response.data)
                    })
                    this.$router.push({
                        path: '/subject/'+this.$route.params.subjectId+'/noticeBoard/' + this.$route.params.postId
                    })
                })
            }
        },

    }
}
</script>