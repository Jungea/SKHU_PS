<template>
    <div class="containerStyle">
        <b-row>
            <b-col>
                <h4>{{project.projectName}}의 {{$route.params.week}}주차 To-do List</h4>
            </b-col>
        </b-row>
        <!--todo 영역...차후 코드 간소화 하겠음-->
        <div style="margin-top:50px;">
            <b-row cols-md="3" cols="1">
                <!--시작안함-->
                <b-col>
                    <b-card style="height:700px;">
                        <template v-slot:header>
                            <span style="font-size:14pt;"><b>시작 안함</b></span>
                            <span style="float:right;">
                                <b-icon-plus style="cursor:pointer; margin-right:20px" @click="createShow(0)"></b-icon-plus>
                                <b-icon-trash style="cursor:pointer;" @click="deleteCheckShow(0)"></b-icon-trash>
                            </span>
                        </template>
                        <b-card-body style="overflow-y:scroll; ;padding:0;height:600px">
                            <!--생성#1-->
                            <b-form @submit.prevent="createNewTodo(0)" v-if="show">
                                <b-form-textarea
                                    :id="`new-todo-${todo.todoId}`"
                                    v-model="new_todo.detail"
                                    placeholder="할 일 작성"
                                    rows="2"
                                    max-rows="4"
                                ></b-form-textarea>
                                <div style="margin-top:10px;">
                                    <b-button size="sm" type="submit" variant="primary">Submit</b-button>
                                    <b-button @click="show=false" size="sm" variant="danger">cancle</b-button>
                                </div>
                            </b-form>
                            <!--할 일 카드 목록-->
                            <b-form-checkbox-group v-model="selected">
                            <draggable :list="state1"  group="people" @start="drag=true;deleteSelect=false" @end="drag=false" @change="log(0, $event)">
                            <b-card :key="todo.todoId" v-for="todo in state1" draggable="true" class="mt-2">
                                <!--삭제 체크박스-->
                                <b-form-checkbox
                                    :value="todo"
                                    name="delete"
                                    style="float:left" 
                                    v-if="deleteSelect"
                                ></b-form-checkbox>
                                <span :style="`float:left; color:${member!=null?'black':member[getIndexById(member,'userId',todo.userId)]['color']}`" v-if="(todo.todoId!=activingEditId)?true:false">
                                    <sup>{{todo.userName}}</sup><br> {{todo.detail}}
                                </span>
                                <!--수정#1-->
                                <b-form @submit.prevent="editDetail(todo)" v-if="(todo.todoId==activingEditId)?true:false">
                                    <b-form-textarea
                                        :value="`${todo.detail}`"
                                        v-model="editText"
                                        rows="2"
                                        max-rows="4"
                                        style="float:left; margin-bottom:10px"
                                    ></b-form-textarea>
                                    <b-button size="sm" type="submit" variant="primary">Submit</b-button>
                                    <b-button @click="cancel" size="sm" variant="danger">cancel</b-button>
                                </b-form>
                                <b-icon-three-dots v-show="(todo.todoId!=activingEditId)?true:false" style="float:right; cursor:pointer" :id="`popover1-${todo.todoId}`"></b-icon-three-dots>                         
                                <!--메뉴 팝오버#1-->
                                <b-popover
                                    :target="`popover1-${todo.todoId}`"
                                    placement="rightbottom"
                                    triggers="focus"
                                    boundary="viewport"
                                >
                                    <template v-slot:title>
                                        menu
                                    </template>
                                    <!--수정, 이동 트리거-->
                                    <b-button size="sm" style="width:100px" @click="editShow(todo)">수정</b-button><br>
                                    <b-button @click="changeStatus(todo, 0, 1)" size="sm" class="mt-1" style="width:100px">
                                        <b-icon-caret-right-fill></b-icon-caret-right-fill>
                                    </b-button>
                                </b-popover>
                            </b-card>
                            </draggable>
                            </b-form-checkbox-group>
                        </b-card-body>
                    </b-card>
                </b-col>
                <!--진행중-->
                <b-col>
                    <b-card style="height:700px;">
                        <template v-slot:header>
                            <span style="font-size:14pt;"><b>진행 중</b></span>
                            <span style="float:right;">
                                <b-icon-plus style="cursor:pointer; margin-right:20px" @click="createShow(1)"></b-icon-plus>
                                <b-icon-trash style="cursor:pointer;" @click="deleteCheckShow(1)"></b-icon-trash>
                            </span>
                        </template>
                        <b-card-body style="overflow-y:scroll; padding:0;height:600px">
                            <!--생성#2-->
                            <b-form @submit.prevent="createNewTodo(1)" v-if="show2">
                                <b-form-textarea
                                    :id="`new-todo-${todo.todoId}`"
                                    v-model="new_todo.detail"
                                    placeholder="할 일 작성"
                                    rows="2"
                                    max-rows="4"
                                ></b-form-textarea>
                                <div style="margin-top:10px;">
                                    <b-button size="sm" type="submit" variant="primary">Submit</b-button>
                                    <b-button @click="show2=false" size="sm" variant="danger">cancle</b-button>
                                </div>
                            </b-form>
                            <!--할 일 카드 목록-->
                            <b-form-checkbox-group v-model="selected">
                            <draggable :list="state2"  group="people" @start="drag=true;deleteSelect=false" @end="drag=false" @change="log(1, $event)">
                            <b-card :key="todo.todoId" v-for="todo in state2" draggable="true" class="mt-2">
                                <!--삭제 체크박스-->
                                <b-form-checkbox
                                    :value="todo"
                                    name="delete"
                                    style="float:left" 
                                    v-if="deleteSelect2"
                                ></b-form-checkbox>
                                <span :style="`float:left; color:${member!=null?'black':member[getIndexById(member,'userId',todo.userId)]['color']}`" v-if="(todo.todoId!=activingEditId)?true:false">
                                    <sup>{{todo.userName}}</sup><br> {{todo.detail}}
                                </span>
                                <!--수정#2-->
                                <b-form @submit.prevent="editDetail(todo)" v-if="(todo.todoId==activingEditId)?true:false">
                                    <b-form-textarea
                                        :value="`${todo.detail}`"
                                        v-model="editText"
                                        rows="2"
                                        max-rows="4"
                                        style="float:left; margin-bottom:10px"
                                    ></b-form-textarea>
                                    <b-button size="sm" type="submit" variant="primary">Submit</b-button>
                                    <b-button @click="cancel" size="sm" variant="danger">cancel</b-button>
                                </b-form>
                                <b-icon-three-dots v-show="(todo.todoId!=activingEditId)?true:false" style="float:right; cursor:pointer" :id="`popover2-${todo.todoId}`"></b-icon-three-dots>                         
                                <!--메뉴 팝오버#2-->
                                <b-popover
                                    :target="`popover2-${todo.todoId}`"
                                    placement="rightbottom"
                                    triggers="focus"
                                    boundary="viewport"
                                >
                                    <template v-slot:title>
                                        menu
                                    </template>
                                    <b-button size="sm" style="width:100px" @click="editShow(todo)">수정</b-button>
                                    <div>
                                        <b-button @click="changeStatus(todo, 1, 0)" size="sm" class="mt-1" style="width:48px">
                                            <b-icon-caret-left-fill></b-icon-caret-left-fill>
                                        </b-button>
                                        <b-button @click="changeStatus(todo, 1, 2)" size="sm" class="mt-1" style="width:48px;float:right">
                                            <b-icon-caret-right-fill></b-icon-caret-right-fill>
                                        </b-button>
                                    </div>
                                </b-popover>
                            </b-card>
                            </draggable>
                            </b-form-checkbox-group>
                        </b-card-body>
                    </b-card>
                </b-col>
                <!--완료-->
                <b-col>
                    <b-card style="height:700px;">
                        <template v-slot:header>
                            <span style="font-size:14pt;"><b>완료</b></span>
                            <span style="float:right;">
                                <b-icon-plus style="cursor:pointer; margin-right:20px" @click="createShow(2)"></b-icon-plus>
                                <b-icon-trash style="cursor:pointer;" @click="deleteCheckShow(2)"></b-icon-trash>
                            </span>
                        </template>
                        <b-card-body style="overflow-y:scroll; padding:0;height:600px">
                            <!--생성#3-->
                            <b-form @submit.prevent="createNewTodo(2)" v-if="show3">
                                <b-form-textarea
                                    :id="`new-todo-${todo.todoId}`"
                                    v-model="new_todo.detail"
                                    placeholder="할 일 작성"
                                    rows="2"
                                    max-rows="4"
                                ></b-form-textarea>
                                <div style="margin-top:10px;">
                                    <b-button size="sm" type="submit" variant="primary">Submit</b-button>
                                    <b-button @click="show3=false" size="sm" variant="danger">cancle</b-button>
                                </div>
                            </b-form>
                            <!--할 일 카드 목록-->
                            <b-form-checkbox-group v-model="selected">
                            <draggable :list="state3"  group="people" @start="drag=true;deleteSelect=false" @end="drag=false" @change="log(2, $event)">
                            <b-card :key="todo.todoId" v-for="todo in state3" draggable="true" class="mt-2">
                                <!--삭제 체크박스-->
                                <b-form-checkbox
                                    :value="todo"
                                    name="delete"
                                    style="float:left" 
                                    v-if="deleteSelect3"
                                ></b-form-checkbox>
                                <span :style="`float:left; color:${member!=null?'black':member[getIndexById(member,'userId',todo.userId)]['color']}`" v-if="(todo.todoId!=activingEditId)?true:false">
                                    <sup>{{todo.userName}}</sup><br> {{todo.detail}}
                                </span>
                                <!--수정#3-->
                                <b-form @submit.prevent="editDetail(todo)" v-if="(todo.todoId==activingEditId)?true:false">
                                    <b-form-textarea
                                        :value="`${todo.detail}`"
                                        v-model="editText"
                                        rows="2"
                                        max-rows="4"
                                        style="float:left; margin-bottom:10px"
                                    ></b-form-textarea>
                                    <b-button size="sm" type="submit" variant="primary">Submit</b-button>
                                    <b-button @click="cancel" size="sm" variant="danger">cancel</b-button>
                                </b-form>
                                <b-icon-three-dots v-show="(todo.todoId!=activingEditId)?true:false" style="float:right; cursor:pointer" :id="`popover3-${todo.todoId}`"></b-icon-three-dots>                         
                                <!--메뉴 팝오버#3-->
                                <b-popover
                                    :target="`popover3-${todo.todoId}`"
                                    placement="rightbottom"
                                    triggers="focus"
                                    boundary="viewport"
                                >
                                    <template v-slot:title>
                                        menu
                                    </template>
                                    <b-button size="sm" style="width:100px" @click="editShow(todo)">수정</b-button><br>
                                    <b-button @click="changeStatus(todo, 2, 1)" size="sm" class="mt-1" style="width:100px">
                                        <b-icon-caret-left-fill></b-icon-caret-left-fill>
                                    </b-button>
                                </b-popover>
                                
                            </b-card>
                            </draggable>
                            </b-form-checkbox-group>
                        </b-card-body>
                    </b-card>
                </b-col>
            </b-row>
        </div>
        <b-row style="margin:auto; margin-top:20px">
            <b-card no-body class="text-center" style="width:100%; padding:20px">
                <span>
                <span v-for="mem in member" :key="mem.id" style="margin:0 20px;">
                    <span :style="`color:${mem.color};`">■</span>{{mem.name}}
                </span></span>
            </b-card>
        </b-row>
        <!-- <ul v-for="t in todo" :key="t.todoId">
            <li>[{{t.progState}}] {{t.detail}} -{{t.user.name}}</li>
        </ul> -->
        
    </div>
</template>>

<script>
import axios from 'axios';
import draggable from 'vuedraggable'
export default {
    components: {
        draggable,
    },
    data(){
        return{
            project:{},
            show:false, show2:false, show3:false,  //todo 생성 폼 보여짐 상태, 각각 [시작 안함, 진행중, 완료]에서 쓰임
            deleteSelect:false, deleteSelect2:false, deleteSelect3:false,  //delete 체크 여부, 각각 [시작 안함, 진행중, 완료]에서 쓰임

            editText:'',
            activingEditId:null,
            selected:[],
            //새로 만들어질 todo
            new_todo:{
                id:null,
                detail:'',
                prog:null,
                userId:null
            },
            modalId:'',
            idcount:7,    //id 증가를 위한 임시 변수
            todo: [
                // {todoId:1, userId:1, detail:'똥싸기', prog:0,},
                // {todoId:2, userId:2, detail:'밥먹기', prog:0,},
                // {todoId:3, userId:4, detail:'치킨시키기', prog:1,},
                // {todoId:4, userId:1, detail:'잠자기', prog:1,},
                // {todoId:5, userId:2, detail:'나루토보기', prog:1,},
                // {todoId:6, userId:1, detail:'메이플하기', prog:2,},
                // {todoId:7, userId:4, detail:'샤워하기', prog:2,}
            ],

            state1:[],
            state2:[],
            state3:[],

            member:[
                //{id:1, color:'orange', name:'짱구'}, {id:2, color:'blue', name:'유리'}, {id:4, color:'green', name:'철수'}
            ]
        }
        
    },
    methods:{
        //일정 드래그 처리
        log(prog, evt) {
            //prog- evt가 발생한 리스트 위치(0,1,2)
            if(evt.moved) {  //순서 변경
                let oldIndex = evt.moved.oldIndex;
                let newIndex = evt.moved.newIndex;
                let start = oldIndex < newIndex? oldIndex : newIndex;
                let end = start == oldIndex? newIndex : oldIndex;

                for(let i = start; i <= end; ++i) {
                    this.move(eval("this.state"+(prog+1))[i].todoId, prog, i, end);
                }
            } else if(evt.added) { //추가
                let start = evt.added.newIndex;
                let end = eval("this.state"+(prog+1)).length;

                for(let i = start; i < end; ++i) {
                    this.move(eval("this.state"+(prog+1))[i].todoId, prog, i, end-1);
                }

            } else if(evt.removed) { //제거
                let start = evt.removed.oldIndex;
                let end = eval("this.state"+(prog+1)).length;

                for(let i = start; i < end; ++i) {
                    this.move(eval("this.state"+(prog+1))[i].todoId, prog, i, end-1);
                }
                
            }
        },
        //일정 이동 디비 등록
        move(todoId, prog, order, end) {
            if(end == -1)
                end = 0;
            axios.get('/api/moveTodo/'+todoId+'/'+prog+'/'+order)
                .then(() => { if(end == order) this.todoReload() });
        },
        //todo 추가 트리거
        createShow(status){
            this.new_todo={}
            this.deleteSelect=false; this.deleteSelect2=false; this.deleteSelect3=false
            if(status==0){
                this.show=(this.show==false)?true:false
                this.show2=false; this.show3=false;
            }
            else if(status==1){
                this.show2=(this.show2==false)?true:false
                this.show=false; this.show3=false
            }
            else{
                this.show3=(this.show3==false)?true:false
                this.show=false; this.show2=false
            }
            this.activingEditId=null
        },
        //todo 생성(create)
        createNewTodo(st){
            if(this.new_todo.detail!=''){
                // this.todo.push({id:this.idcount, detail:this.new_todo.detail, status:st})
                // this.idcount+=1
                //axios
                axios.post('/api/createTodo',{
                    weeklyId:this.$route.query.id,
                    detail:this.new_todo.detail,
                    progState:st,
                    order: eval("this.state"+(st+1)).length
                }).then(() => this.todoReload());//불러온 배열이 기존 배열과 중첩됨
            }
            this.show=false; this.show2=false; this.show3=false
            this.new_todo.detail=''
            
        },
        //수정 트리거
        editShow(todo){
            this.editText=todo.detail
            this.show=false; this.show2=false; this.show3=false
            this.deleteSelect=false; this.deleteSelect2=false; this.deleteSelect3=false
            this.activingEditId=todo.todoId
        },
        //todo detail 수정(update)
        editDetail(todo){
            if(this.editText.trim().length != 0) {
                if(this.editText != todo.detail){
                    todo.detail=this.editText;
                    axios.post('/api/editTodo',{
                        todoId:todo.todoId,
                        detail:todo.detail
                    }).then(() => this.todoReload());
                }
            }
            this.editText='';
            this.activingEditId=null
        },
        //수정 취소
        cancel(){
            this.editText='';
            this.activingEditId=null
        },
        //todo 삭제 트리거
        deleteCheckShow(status){
            //수정중이었으면 수정 취소시킴
            this.activingEditId=null
            this.show=false; this.show2=false; this.show3=false
            if(status==0){
                if(this.deleteSelect==true){
                    this.delete(this.selected, status)
                }
                this.deleteSelect=(this.deleteSelect==false)?true:false
                this.deleteSelect2=false; this.deleteSelect3=false
            }
            else if(status==1){
                if(this.deleteSelect2==true){
                    this.delete(this.selected, status)
                }
                this.deleteSelect2=(this.deleteSelect2==false)?true:false
                this.deleteSelect=false; this.deleteSelect3=false
            }
            else{
                if(this.deleteSelect3==true){
                    this.delete(this.selected, status)
                }
                this.deleteSelect3=(this.deleteSelect3==false)?true:false
                this.deleteSelect=false; this.deleteSelect2=false
            }
            this.selected=[]
        },
        //todo 삭제(delete)
        delete(arr, prog){
            if(arr.length > 0){
                let start = 0;
                for(let i = 0; i < arr.length; i++){
                    start = start > arr[i].order? arr[i].order : start;

                    eval("this.state"+(prog+1)).splice(arr[i].order-i, 1);
                    axios.get('/api/deleteTodo/'+arr[i].todoId).then();
                }
                let end = eval("this.state"+(prog+1)).length;
                    for(let i = start; i < end; ++i) 
                        this.move(eval("this.state"+(prog+1))[i].todoId, prog, i, end-1);
            } else {
                alert("선택한 일정이 없습니다.")
            }
        },
        //status 변경(update)
        changeStatus(todo, from, to){
            eval("this.state"+(to+1)).push(todo);
            eval("this.state"+(from+1)).splice(todo.order, 1);

            //added
            this.move(todo.todoId, to, eval("this.state"+(to+1)).length-1, -1);

            //removed
            let start = todo.order;
            let end = eval("this.state"+(from+1)).length;
            for(let i = start; i < end; ++i) {
                this.move(eval("this.state"+(from+1))[i].todoId, from, i, end-1);
            }

        },

        //모든 배열에서 사용할 수 있도록 수정
        //모든 칼럼으로 조회할 수 있도록 수정
        getIndexById(arr,key,value){
            var index = arr.findIndex(obj => obj[key]==value);
            return index;
        },

        todoReload() {
            axios.get('/api/project/weekly/'+this.$route.query.id+'/todos')
                .then(response => {
                    if(response.data != null){
                        response.data.sort((x, y) => x.order-y.order);
                        let todoTemp=[]; let temp1=[]; let temp2=[]; let temp3=[];
                        for(let i=0;i<response.data.length;i++){
                            let data={
                                'todoId':response.data[i].todoId,
                                'userId':response.data[i].user.userId,
                                'userName':response.data[i].user.name,
                                'detail':response.data[i].detail,
                                'created':response.data[i].createTime,
                                'prog':response.data[i].progState,
                                'weekly':response.data[i].weekly.weeklyId,
                                'order':response.data[i].order
                            }
                            todoTemp.push(data)
                            if(data.prog==0)
                                temp1.push(data)
                            else if(data.prog==1)
                                temp2.push(data)
                            else
                                temp3.push(data)
                        }
                        this.todo=todoTemp;
                        this.state1=temp1; this.state2=temp2; this.state3=temp3;
                    }
                    // console.log('todo 로드합니다')
                })
        }

    },

    mounted() {
        if(this.$route.query.id == undefined) {
            axios.get('/api/project/'+this.$route.params.projectId+'/weeklyGoal')
                .then(response => {
                    let goals = response.data;

                    goals.sort(function(a,b){
                        var date1 = new Date(a.startTime);
                        var date2 = new Date(b.startTime);
                        return date1-date2
                    })
                    
                    let week = goals.findIndex(p => p.weeklyId == this.$route.params.week) + 1;
                    
                    this.$router.push({
                        path: '/project/'+this.$route.params.projectId+'/weekly/'+week,
                        query: {id: this.$route.params.week}
                    })
                })
        }

        axios.get('/api/project/'+this.$route.params.projectId)
        .then(response => {
            this.project = response.data
        })

        //todo 가져와 배열에 채우기
        if(this.$route.query.id != undefined) 
            this.todoReload();
        
        //이 프로젝트에 참여중인 멤버 정보 가져오기??

    },
    watch: {
      '$route' () {
          this.todoReload();
      }
    }
}
</script>

<style scoped>
</style>