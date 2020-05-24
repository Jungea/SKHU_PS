<template>
    <div class="containerStyle">
        <center>
            <h3>프로젝트 점수</h3>
            <hr style="width: 50%">        
            <form ref="form">
                <b-form-group label-for="SubjectStudent">
                    <table class="table table-bordered" id="SubjectStudent" v-bind="this.data" style="width: 50%">
                        <tbody>
                            <tr>
                                <th>학번</th>
                                <th>학년</th>
                                <th>이름</th>
                                <th>학과</th>
                            </tr>
                            <tr @click="studentScore(item)" :key="index" v-for="(item, index) in data">
                                <td> {{ item.userNum }} </td>
                                <td> {{ item.grade }} </td>
                                <td > {{ item.name }} </td>
                                <td> {{ item.detDepartment.detName }} </td>
                            </tr>
                        </tbody>
                    </table>
                </b-form-group>
            </form>
        </center>

    <b-modal id="modal-score" size="lg" ok-only @show="resetModal" @hidden="resetModal">
        <center>
            <h3 class="mt-3"> {{ userData.userNum }} | {{ userData.name }} | 점수 </h3>
            <hr style="border: 1px solid black ; width: 60% ; margin-bottom: 30px">
            <b-container style="width: 70%">
                <b-row>
                    <table class="table table-bordered">
                        <tr>
                            <th>주차</th>
                            <th>배점</th>
                            <th>점수</th>
                        </tr>
                        <tr :key="index" v-for="(data, index) in s">
                            <td style="width: 60%"> {{ data.name }} </td>
                            <td> {{ data.max }} </td>
                            <td v-if="!editVisible"> {{ data.score }} </td>
                            <td v-if="editVisible" style="padding: 5px 0 0 0 ; width: 20%">
                                <center>
                                    <b-input v-model="data.score" style="width: 50% ; text-align: center"></b-input>
                                </center>
                            </td>
                        </tr>
                    </table>

                    <div style="text-align: right ; width: 100%">
                        <b-button v-if="!editVisible" variant="dark" @click="editScore()">입력</b-button>
                        <b-button v-if="editVisible" variant="dark" @click="editScore()">저장</b-button>
                    </div>
                </b-row>
            </b-container> 
        </center>
      </b-modal>
    </div>
</template>

<script>
import axios from 'axios';
export default {
    name: 'score',
    data() {
        return {
            data: [],
            userData: [],
            s: [ { name: '1주차', max: 20, score: 20 }, { name: '2주차', max: 10, score: 9 }, { name: '3주차', max: 10, score: 9 }, { name: '4주차', max: 10, score: 8 },
            { name: '5주차', max: 10, score: 10 }, { name: '6주차', max: 10, score: 9 }, { name: '7주차', max: 10, score: 8 }, { name: '8주차', max: 10, score: 9 },
            { name: '9주차', max: 10, score: 10 }, { name: '10주차', max: 15, score: 14 }],
            editVisible: false
        }
    },

    mounted() {
      axios.get('/api/alluser')
      .then(response => {
          this.data = response.data
          
          for(var i = 0 ; i < response.data.length ; i++) {
              if(response.data[i].detDepartment == null)
                this.data[i].detDepartment = "null값"
          }
      })  
    },
    methods: {
        studentScore(item) {
            this.userData = item;
            alert(this.userData.name)
            this.$root.$emit('bv::show::modal', 'modal-score') 
        },
        editScore() {
            if(this.editVisible) {
                this.editVisible = false;
                alert("저장되었습니다.")
            }
            else
                this.editVisible = true;
        },
        resetModal() {
            this.editVisible = false;
        }
    }
}
</script>

<style>
tr, td { text-align: center; }
th { background-color: rgb(243,243,243) }
</style>