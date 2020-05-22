<template>
    <div class="containerStyle">
        <b-row>
            <b-col cols="8">
                <h4>{{project.projectName}}: 주간 목표</h4>
            </b-col>
            <b-col cols="4">
                <b-button v-b-modal.modal-xl variant="outline-secondary" style="height: 70px; float:right;">목표 생성</b-button>
            </b-col>
        </b-row>
        <div style="margin-top:50px">
            <b-row cols-md="3" cols="1">
                <b-col class="mb-5" :key="goal.weekly_id" v-for="goal in goals">
                    <b-card @click="todo(project.projectId, goal.week)" bg-variant="dark" text-variant="white" v-bind:title="goal.week.toString()+'주차'" style="cursor:pointer; height:150px">
                        <b-card-text>
                            {{goal.start_time}} ~ {{getEndDate(goal.start_time)}}
                        </b-card-text>
                        <b-card-text>
                            {{goal.detail}}
                        </b-card-text>
                    </b-card>
                </b-col>
            </b-row>
            
        </div>

        <b-modal id="modal-xl" size="lg" title="목표 생성" 
            @show="resetModal"
            @hidden="resetModal"
            @ok="handleOk" ref="modal" data-backdrop="static">
            <form ref="form" @submit.stop.prevent="handleSubmit">
                <b-form-group
                    :label=title.toString()
                    label-for="createWG"
                    invalid-feedback="입력하지 않은 필수 입력 사항이 있습니다.">
                    <table class="table table-bordered" id="createWG">
                        <tbody>
                        <tr>
                            <th scope="row" style="width:28%">시작 날짜</th>
                            <td>
                                <b-form-input v-model="context.selectedFormatted" style="width:75%; margin-right:10px; float:left;" id="startDate" placeholder="시작 날짜를 입력하세요" disabled></b-form-input>
                                <b-button style="width:20%; clear:both" v-b-modal.calendar>날짜 선택</b-button>
                                <b-form-text>선택 날짜로부터 7일간의 목표를 생성합니다.</b-form-text>
                            </td>
                        </tr>
                        <tr>
                            <th scope="row">설명</th>
                            <td>
                                <b-form-textarea id="detail" v-model="detail" placeholder="내용을 입력하세요"></b-form-textarea>
                                <b-form-text>간단한 주간 목표 설명을 적으세요.</b-form-text>
                            </td>
                        </tr>
                        </tbody>
                    </table>
                </b-form-group>
            </form>
        </b-modal>

        <b-modal id="calendar" ok-only>
            <b-calendar block @context="onContext" :date-disabled-fn="dateDisabled"></b-calendar>
        </b-modal>
    </div>
</template>

<script>
import axios from 'axios';

export default {
    data(){
        return{
            project:{},
            context:{},
            detail:'',
            title:'',
            goals:[
                {weekly_id:1, week:1, detail:'가나다라마바사', start_time:'2020-05-09'}, 
                {weekly_id:2, week:2, detail:'아자차카타파하', start_time:'2020-05-16'},
                {weekly_id:3, week:3, detail:'abcdefg', start_time:'2020-05-02'}
            ],
            days:[]
        }
        
    },
    mounted() {
        axios.get('/api/project/'+this.$route.params.projectId)
        .then(response => {
            this.project = response.data
        }),

        // 주간 목표 목록 구현하면 주석해제
        axios.get('/api/project/'+this.$route.params.projectId+'/weeklyGoal')
        .then(response => {
            console.log(response.data)
        }),
        
        this.title=this.goals.length+1+"주차 목표 생성"

        this.days=this.getRange()

        this.goals.sort(function(a,b){
            var date1 = new Date(a.start_time);
            var date2 = new Date(b.start_time);
            return date1-date2
        })

        for(let i=0;i<this.goals.length;i++){
            this.goals[i].week=i+1
        }

    },
    conputed(){
        
    },
    methods:{
        todo(projectId, week) {
            this.$router.push({
                path: '/project/'+projectId+'/weekly/'+week
            })
        },

        dateDisabled(ymd, date) {
            date.setDate(date.getDate()+1);
            let myDay=date.toISOString().slice(0, 10)

            date.setDate(date.getDate()+6);//일자 겹치지 않기 위해 넣음
            let myDay2=date.toISOString().slice(0, 10)

            if(this.days.indexOf(myDay)==-1&&this.days.indexOf(myDay2)==-1){
                return false
            }
            return true
        },

        onContext(ctx) {
            this.context = ctx
        },

        getEndDate(dt){
            let arr=dt.split('-')
            let date=new Date(arr[0],arr[1],arr[2])
            let endDate=new Date(arr[0],arr[1],arr[2])
            endDate.setDate(date.getDate()+6);
            let month=(endDate.getMonth()<10)?'0'+endDate.getMonth():endDate.getMonth()
            let day=(endDate.getDate()<10)?'0'+endDate.getDate():endDate.getDate()
            return endDate.getFullYear()+'-'+month+'-'+day;
        },

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

        getRange(){
            for(let i=0;i<this.goals.length;i++){
                this.addRange(this.goals[i].start_time, this.getEndDate(this.goals[i].start_time),this.days)
            }
            console.log(this.days)
            return this.days;
        },

        resetModal() {
            this.context={}
            this.detail=''
        },

        handleOk(bvModalEvt) {
            bvModalEvt.preventDefault()
            this.handleSubmit()
        },

        handleSubmit() {
            
            this.submit()
            this.$nextTick(() => {
                this.$bvModal.hide('modal-xl')
            })
        },

        submit() {
            alert('시작날짜'+this.context.selectedYMD+'상세'+this.detail)
            //구현하면 주석해제
            axios.post('/api/createGoal',{
                projectId:this.project.projectId,
                startTime:this.context.selectedYMD,  //시작 연월일
                detail:this.detail,
                week:this.goals.length+1  //하나 생성하면 주 수 증가
            }).then(response => { 
                this.project = response.data;
                location.reload();
            });
        },
    }
}
</script>

<style scoped>
</style>