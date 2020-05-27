<template>
    <div class="containerStyle">
        <b-row>
            <b-col cols="8">
                <h4>{{project.projectName}}: 주간 목표</h4>
            </b-col>
            <b-col cols="4">
                <b-button @click="openModal(0)" variant="outline-secondary" style="height: 70px; float:right;">목표 생성</b-button>
            </b-col>
        </b-row>
        <div style="margin-top:50px">
            <b-row cols-md="3" cols="1">
                <b-col class="mb-5" :key="goal.weeklyId" v-for="(goal,index) in goals">
                    <b-card @click="todo(project.projectId, index+1, goal.weeklyId)" bg-variant="dark" text-variant="white" v-bind:title="(index + 1)+' 주차'" style="cursor:pointer;padding:10px">
                        <b-card-text>
                            {{goal.startTime}} ~ {{getEndDate(goal.startTime)}}
                        </b-card-text>
                        <b-card-text class="detail">
                            {{goal.detail}}
                        </b-card-text>
                        <b-icon-three-dots :id="`popover-${goal.weeklyId}`" style="float:right"></b-icon-three-dots>
                        <b-popover
                            :target="`popover-${goal.weeklyId}`"
                            placement="rightbottom"
                            triggers="hover"
                            boundary="viewport"
                        >
                            <template v-slot:title>
                                menu
                            </template>
                            <b-button size="sm" style="width:100%" @click="openModal(1, goal);">수정</b-button>
                            <b-button @click="deleteWeekly(goal)" size="sm" class="mt-1" style="width:100%">삭제</b-button>
                        </b-popover>
                    </b-card>
                </b-col>
            </b-row>
            
        </div>

        <b-modal id="modal-xl" size="lg" :title="`주간 목표 ${modalState}`" 
            @show="resetModal"
            @hidden="resetModal"
            @ok="handleOk" ref="modal" data-backdrop="static">
            <form ref="form" @submit.stop.prevent="handleSubmit()">
                <b-form-group
                    :state="weeklyValidation"
                    :label=title.toString()
                    label-for="createWG"
                >
                    <table class="table table-bordered" id="createWG">
                        <tbody>
                        <tr>
                            <th scope="row" style="width:28%">시작 날짜</th>
                            <td>
                                <b-form-input 
                                    v-model="day" 
                                    style="width:75%; margin-right:10px; float:left;" 
                                    id="startDate" 
                                    placeholder="시작 날짜를 입력하세요" 
                                    disabled
                                ></b-form-input>
                                <b-button style="width:20%; clear:both" v-b-modal.calendar>날짜 {{modalState=='생성'?'선택':'변경'}}</b-button>
                                <b-form-text>선택 날짜로부터 7일간의 목표를 생성합니다.</b-form-text>
                            </td>
                        </tr>
                        <tr>
                            <th scope="row">설명</th>
                            <td>
                                <b-form-textarea 
                                    id="detail" 
                                    v-model="detail" 
                                    placeholder="내용을 입력하세요"
                                ></b-form-textarea>
                                <b-form-text>간단한 주간 목표 설명을 적으세요.</b-form-text>
                            </td>
                        </tr>
                        </tbody>
                    </table>
                </b-form-group>
            </form>
            <template v-slot:modal-footer>
                <b-button @click="handleOk" :disabled="!weeklyValidation">{{modalState}}</b-button>
            </template>
        </b-modal>

        <b-modal id="calendar" @cancel="day=''">
            <template v-slot:modal-header>
                <h5>날짜 선택</h5>
            </template>
            <b-calendar block @context="onContext" :date-disabled-fn="dateDisabled_CR" v-if="modalState=='생성'" :hide-header="true" no-highlight-today></b-calendar>
            <b-calendar block @context="onContext" :date-disabled-fn="dateDisabled_ED" v-if="modalState=='수정'" :hide-header="true" no-highlight-today></b-calendar>
        </b-modal>
    </div>
</template>

<script>
import axios from 'axios';

export default {
    data(){
        return{
            modalState:'',
            project:{},
            context:{},
            detail:'',
            title:'',
            goals:[
                // {weekly_id:1, week:1, detail:'가나다라마바사', start_time:'2020-05-09'}, 
                // {weekly_id:2, week:2, detail:'아자차카타파하', start_time:'2020-05-16'},
                // {weekly_id:3, week:3, detail:'abcdefg', start_time:'2020-05-02'}
            ],
            weekly:{},
            days:[],
            day:'',
        }
        
    },
    mounted() {
        axios.get('/api/project/'+this.$route.params.projectId)
        .then(response => {
            this.project = response.data
        }),

        // 주간 목표 목록 구현하면 주석해제
        this.goalsReload();

    },
    computed:{
        temp1(){
            let idx=this.goals.indexOf(this.weekly)
            // let startIdx; let endIdx; 
            let arr;
            let st; let et; let startDate; let endDate
            //수정할거에 이전 목표와 다음 목표 있을 때
            if(this.goals[idx+1]&&this.goals[idx-1]){
                //시작가능 날짜: 이전 목표 종료일+1~다음 목표 시작 날짜-7일
                st=this.getEndDate(this.goals[idx-1].startTime).split('-')
                et=this.goals[idx+1].startTime.split('-')
                startDate=new Date(st[0],parseInt(st[1])-1,st[2])
                endDate=new Date(et[0],parseInt(et[1])-1,et[2])
                startDate.setDate(startDate.getDate()+1)
                endDate.setDate(endDate.getDate()-7)
            }
            //다음 목표만 있을 때 == 맨 앞
            else if(this.goals[idx+1]&&!this.goals[idx-1]){
                //시작가능 날짜: 현재 목표 시작 -6일부터 다음 목표 시작 날짜-7까지
                st=this.weekly.startTime.split('-')
                et=this.goals[idx+1].startTime.split('-')
                startDate=new Date(st[0],parseInt(st[1])-1,st[2])
                endDate=new Date(et[0],parseInt(et[1])-1,et[2])
                startDate.setDate(startDate.getDate()-6)
                endDate.setDate(endDate.getDate()-7)
                //arr=this.format(startDate)+'~'+this.format(endDate)
            }
            //이전 목표만 있을 때 == 맨 뒤
            else if(!this.goals[idx+1]&&this.goals[idx-1]){
                //시작가능 날짜: 이전목표 끝 날짜 +1일부터 현재 목표 끝날짜까지
                st=this.getEndDate(this.goals[idx-1].startTime).split('-')
                et=this.getEndDate(this.weekly.startTime).split('-')
                startDate=new Date(st[0],parseInt(st[1])-1,st[2])
                endDate=new Date(et[0],parseInt(et[1])-1,et[2])
                startDate.setDate(startDate.getDate()+1)
                
                //이전 목표 끝날짜
            }
            //원래거 하나만 있을 때
            else{
                //시작가능 날짜: 현재 목표 시작 -6일부터 현재 목표 끝날짜까지
                st=this.weekly.startTime.split('-')
                et=this.getEndDate(this.weekly.startTime).split('-')
                startDate=new Date(st[0],parseInt(st[1])-1,st[2])
                endDate=new Date(et[0],parseInt(et[1])-1,et[2])
                startDate.setDate(startDate.getDate()-6)
            }
            arr=this.getRange2(this.format(startDate),this.format(endDate))
            return arr

        },

        //내용이 있어야 true return
        dayValidation(){
            return !this.day==''
        },
        detValidation(){
            return !this.detail==''
        },

        //둘 다 true여야 true 리턴
        weeklyValidation(){
            return this.dayValidation&&this.detValidation
        }
    },
    methods:{
        openModal(temp, weekly){
            if(temp==0){
                this.modalState='생성'
                this.title=this.goals.length+1+"주차 목표 생성"
            }
            else{
                this.modalState='수정'
                this.weekly=weekly
                this.title=this.goals.indexOf(weekly)+1+"주차 목표 수정"
            }
            this.$bvModal.show('modal-xl')
        },
        goalsReload() {
            axios.get('/api/project/'+this.$route.params.projectId+'/weeklyGoal')
                    .then(response => {
                        this.goals = response.data;
                        this.days=this.getRange()

                        this.goals.sort(function(a,b){
                            var date1 = new Date(a.startTime);
                            var date2 = new Date(b.startTime);
                            return date1-date2
                        })
                    })
        },
        todo(projectId, week, weeklyId) {
            this.$router.push({
                path: '/project/'+projectId+'/weekly/'+week,
                query: {id: weeklyId}
            })
        },

        //true 반환한 값은 달력에서 선택 못함
        //생성일 때
        dateDisabled_CR(ymd, date) { 
            let day=new Date(date)
            let day2=new Date(date)
            day2.setDate(day.getDate()+6)

            let myDay=this.format(day)
            let myDay2=this.format(day2)

            if(this.days.indexOf(myDay)==-1&&this.days.indexOf(myDay2)==-1){
                return false
            }
            
            return true
        },
        //수정일 때
        dateDisabled_ED(ymd, date) { 
            let day=new Date(date)
            let day2=new Date(date)
            day2.setDate(day.getDate()+6)

            let myDay=this.format(day)
            let myDay2=this.format(day2)
            
            if(this.temp1.indexOf(myDay)!=-1||(this.days.indexOf(myDay)==-1&&this.days.indexOf(myDay2)==-1)){
                return false
            }

            return true
        },

        dateClass(ymd, date){
            let day=new Date(date)
            let myDay=this.format(day)

            return(myDay==11)?'table-info' : ''
        },

        onContext(ctx) {
            this.context = ctx
            this.day=ctx.selectedYMD
        },

        //yyyy-mm-dd 포맷 변환
        format(date){
            let realMonth=date.getMonth()+1
            let month=(realMonth<10)?'0'+realMonth:realMonth
            let day=(date.getDate()<10)?'0'+date.getDate():date.getDate()
            return date.getFullYear()+'-'+month+'-'+day;
        },

        //0527 수정함
        getEndDate(dt){
            let arr=dt.split('-')
            let date=new Date(arr[0],parseInt(arr[1])-1,arr[2])
            let endDate=new Date(arr[0],parseInt(arr[1])-1,arr[2])
            endDate.setDate(date.getDate()+6);
            let s=this.format(endDate)
            return s
            /*
            let date=new Date(arr[0],arr[1],arr[2])
            let endDate=new Date(arr[0],arr[1],arr[2])
            endDate.setDate(date.getDate()+6);
            let month=(endDate.getMonth()<10)?'0'+endDate.getMonth():endDate.getMonth()
            let day=(endDate.getDate()<10)?'0'+endDate.getDate():endDate.getDate()
            return endDate.getFullYear()+'-'+month+'-'+day;
            */
        },

        //addRange, getRange2 기능이 완전 같은 함수라서(파라미터만 다름) 나중에 수정할 예정
        addRange(startDate, endDate, days){
            var dateMove = new Date(startDate);
            if (startDate == endDate){
                startDate = dateMove.toISOString().slice(0,10);
                days.push(startDate);
            }
            else{
                while (startDate < endDate){
                    startDate = dateMove.toISOString().slice(0, 10);
                    //console.log(dateMove)
                    days.push(startDate);
                    dateMove.setDate(dateMove.getDate() + 1);
                }
            }
        },
        getRange2(startDate, endDate){
            let arr=[];
            var dateMove = new Date(startDate);
            if (startDate == endDate){
                startDate = dateMove.toISOString().slice(0,10);
                arr.push(startDate);
            }
            else{
                while (startDate < endDate){
                    startDate = dateMove.toISOString().slice(0, 10);
                    //console.log(dateMove)
                    arr.push(startDate);
                    dateMove.setDate(dateMove.getDate() + 1);
                }
            }
            return arr
        },

        getRange(){
            for(let i=0;i<this.goals.length;i++){
                this.addRange(this.goals[i].startTime, this.getEndDate(this.goals[i].startTime),this.days)
            }
            return this.days;
        },

        resetModal() {
            if(this.modalState=='수정'){this.day=this.weekly.startTime;this.detail=this.weekly.detail}
            else{this.day='';this.context={};this.detail=''}
            //this.title=''
        },

        handleOk(bvModalEvt) {
            bvModalEvt.preventDefault()
            this.handleSubmit()
        },

        handleSubmit() {
            if(this.modalState=='생성'){
                this.submit()
                this.modalState=''
            }
            else{
                this.editWeekly(this.weekly,this.day,this.detail)
                this.modalState=''
                this.weekly={}
            }
            this.$nextTick(() => {
                this.$bvModal.hide('modal-xl')
            })
        },

        //생성
        submit() {
            //alert('시작날짜'+this.context.selectedYMD+'상세'+this.detail)
            //구현하면 주석해제
            axios.post('/api/createGoal',{
                projectId:this.project.projectId,
                startTime:this.context.selectedYMD,  //시작 연월일
                detail:this.detail,
            }).then(() => { 
                this.goalsReload();
            });
        },

        //삭제
        deleteWeekly(weekly) {
            if(confirm('삭제합니까?')){
                axios.get('/api/deleteWeekly/'+weekly.weeklyId)
                .then(() => this.goalsReload())
            }
        },

        //수정
        editWeekly(weekly,date,detail){
            axios.post('/api/editWeekly',{
                weeklyId: weekly.weeklyId,
                startTime: date,  //시작 연월일
                detail: detail,
            }).then(() => { 
                this.goalsReload();
            });
        }
    }
}
</script>

<style scoped>
.detail{
    height:20px; overflow-y:scroll;
}
.detail::-webkit-scrollbar {
    display:none;
}
</style>