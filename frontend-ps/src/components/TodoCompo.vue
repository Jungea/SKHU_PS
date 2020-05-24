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
                                    :id="`new-todo-${todo.id}`"
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
                            <b-card :key="todo.id" v-for="todo in todo" v-show="todo.status==0" draggable="true" class="mt-2">
                                <!--삭제 체크박스-->
                                <b-form-checkbox
                                    :value="todo"
                                    name="delete"
                                    style="float:left" 
                                    v-if="deleteSelect"
                                ></b-form-checkbox>
                                <span :style="`float:left; color:${member[userIndex(todo.user_id)]['color']}`" v-if="(todo.id!=activingEditId)?true:false">
                                    {{todo.detail}}
                                </span>
                                <!--수정#1-->
                                <b-form @submit.prevent="editDetail(todo)" v-if="(todo.id==activingEditId)?true:false">
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
                                <b-icon-three-dots v-show="(todo.id!=activingEditId)?true:false" style="float:right; cursor:pointer" :id="`popover1-${todo.id}`"></b-icon-three-dots>                         
                                <!--메뉴 팝오버#1-->
                                <b-popover
                                    :target="`popover1-${todo.id}`"
                                    placement="rightbottom"
                                    triggers="focus"
                                    boundary="viewport"
                                >
                                    <template v-slot:title>
                                        menu
                                    </template>
                                    <!--수정, 이동 트리거-->
                                    <b-button size="sm" style="width:100px" @click="editShow(todo)">수정</b-button><br>
                                    <b-button @click="changeStatus(todo,1)" size="sm" class="mt-1" style="width:100px">
                                        <b-icon-caret-right-fill></b-icon-caret-right-fill>
                                    </b-button>
                                </b-popover>
                                
                            </b-card>
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
                                    :id="`new-todo-${todo.id}`"
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
                            <b-card :key="todo.id" v-for="todo in todo" v-show="todo.status==1" draggable="true" class="mt-2">
                                <!--삭제 체크박스-->
                                <b-form-checkbox
                                    :value="todo"
                                    name="delete"
                                    style="float:left" 
                                    v-if="deleteSelect2"
                                ></b-form-checkbox>
                                <span :style="`float:left; color:${member[userIndex(todo.user_id)]['color']}`" v-if="(todo.id!=activingEditId)?true:false">
                                    {{todo.detail}}
                                </span>
                                <!--수정#2-->
                                <b-form @submit.prevent="editDetail(todo)" v-if="(todo.id==activingEditId)?true:false">
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
                                <b-icon-three-dots v-show="(todo.id!=activingEditId)?true:false" style="float:right; cursor:pointer" :id="`popover2-${todo.id}`"></b-icon-three-dots>                         
                                <!--메뉴 팝오버#2-->
                                <b-popover
                                    :target="`popover2-${todo.id}`"
                                    placement="rightbottom"
                                    triggers="focus"
                                    boundary="viewport"
                                >
                                    <template v-slot:title>
                                        menu
                                    </template>
                                    <b-button size="sm" style="width:100px" @click="editShow(todo)">수정</b-button>
                                    <div>
                                        <b-button @click="changeStatus(todo,0)" size="sm" class="mt-1" style="width:48px">
                                            <b-icon-caret-left-fill></b-icon-caret-left-fill>
                                        </b-button>
                                        <b-button @click="changeStatus(todo,2)" size="sm" class="mt-1" style="width:48px;float:right">
                                            <b-icon-caret-right-fill></b-icon-caret-right-fill>
                                        </b-button>
                                    </div>
                                </b-popover>
                                
                            </b-card>
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
                                    :id="`new-todo-${todo.id}`"
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
                            <b-card :key="todo.id" v-for="todo in todo" v-show="todo.status==2" draggable="true" class="mt-2">
                                <!--삭제 체크박스-->
                                <b-form-checkbox
                                    :value="todo"
                                    name="delete"
                                    style="float:left" 
                                    v-if="deleteSelect3"
                                ></b-form-checkbox>
                                <span :style="`float:left; color:${member[userIndex(todo.user_id)]['color']}`" v-if="(todo.id!=activingEditId)?true:false">
                                    {{todo.detail}}
                                </span>
                                <!--수정#3-->
                                <b-form @submit.prevent="editDetail(todo)" v-if="(todo.id==activingEditId)?true:false">
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
                                <b-icon-three-dots v-show="(todo.id!=activingEditId)?true:false" style="float:right; cursor:pointer" :id="`popover3-${todo.id}`"></b-icon-three-dots>                         
                                <!--메뉴 팝오버#3-->
                                <b-popover
                                    :target="`popover3-${todo.id}`"
                                    placement="rightbottom"
                                    triggers="focus"
                                    boundary="viewport"
                                >
                                    <template v-slot:title>
                                        menu
                                    </template>
                                    <b-button size="sm" style="width:100px" @click="editShow(todo)">수정</b-button><br>
                                    <b-button @click="changeStatus(todo,1)" size="sm" class="mt-1" style="width:100px">
                                        <b-icon-caret-left-fill></b-icon-caret-left-fill>
                                    </b-button>
                                </b-popover>
                                
                            </b-card>
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
        <ul v-for="t in todo" :key="t.todoId">
            <li>[{{t.progState}}] {{t.detail}} -{{t.user.name}}</li>
        </ul>
    </div>
</template>>

<script>
import axios from 'axios';

export default {
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
                status:null,
                user_id:null
            },
            modalId:'',
            idcount:7,    //id 증가를 위한 임시 변수
            todo: [
                // {id:1, user_id:1, detail:'똥싸기', status:0,},
                // {id:2, user_id:2, detail:'밥먹기', status:0,},
                // {id:3, user_id:4, detail:'치킨시키기', status:1,},
                // {id:4, user_id:1, detail:'잠자기', status:1,},
                // {id:5, user_id:2, detail:'나루토보기', status:1,},
                // {id:6, user_id:1, detail:'메이플하기', status:2,},
                // {id:7, user_id:4, detail:'샤워하기', status:2,}
            ],

            member:[
                {id:1, color:'orange', name:'짱구'}, {id:2, color:'blue', name:'유리'}, {id:4, color:'green', name:'철수'}
            ]
        }
        
    },
    methods:{
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
                    projectId:this.$route.params.projectId,
                    detail:this.new_todo.detail,
                    week:this.$route.params.week,
                    progState:st
                }).then(() => this.todoReload());
            }
            this.show=false; this.show2=false; this.show3=false
            this.new_todo.detail=''
            
        },
        //수정 트리거
        editShow(todo){
            this.editText=todo.detail
            this.show=false; this.show2=false; this.show3=false
            this.deleteSelect=false; this.deleteSelect2=false; this.deleteSelect3=false
            this.activingEditId=todo.id
        },
        //todo detail 수정(update)
        editDetail(todo){
            if(this.editText!=todo.detail){
                todo.detail=this.editText
                alert(JSON.stringify(todo))
                //axios
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
                    this.delete(this.selected)
                }
                this.deleteSelect=(this.deleteSelect==false)?true:false
                this.deleteSelect2=false; this.deleteSelect3=false
            }
            else if(status==1){
                if(this.deleteSelect2==true){
                    this.delete(this.selected)
                }
                this.deleteSelect2=(this.deleteSelect2==false)?true:false
                this.deleteSelect=false; this.deleteSelect3=false
            }
            else{
                if(this.deleteSelect3==true){
                    this.delete(this.selected)
                }
                this.deleteSelect3=(this.deleteSelect3==false)?true:false
                this.deleteSelect=false; this.deleteSelect2=false
            }
            this.selected=[]
        },
        //todo 삭제(delete)
        delete(arr){
            if(arr.length>0){
                alert('삭제:\n'+(JSON.stringify(arr)));
                for(let i=0;i<arr.length;i++){
                    this.todo.splice(this.todo.indexOf(arr[i]),1)
                    //axios
                }
            }
        },
        //status 변경(update)
        changeStatus(todo,to){
            todo.status=to
            //axios
        },

        userIndex(userId){
            var index = this.member.findIndex(obj => obj.id==userId);
            return index;
        },

        todoReload() {
            axios.get('/api/project/'+this.$route.params.projectId+'/weekly/'+this.$route.params.week)
                .then(response => {
                    if(response.data != null)
                        this.todo = response.data;
                    console.log(this.todo);
                })
        }

    },

    mounted() {
        axios.get('/api/project/'+this.$route.params.projectId)
        .then(response => {
            this.project = response.data
        })

        //todo 가져와 배열에 채우기
        this.todoReload();

        //이 프로젝트에 참여중인 멤버 정보 가져오기??

    },
}
</script>

<style scoped>
</style>