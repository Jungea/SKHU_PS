<template>
    <div>
        <h4 style="margin-top: 80px">프로젝트 개요 수정 창</h4>
        <form ref="form" @submit.stop.prevent="handleSubmit" class="mt-5">
            <b-form-group
                label-for="ProjectSummary"
                invalid-feedback="입력하지 않은 필수 입력 사항이 있습니다."
            >
                <table class="table table-bordered" id="ProjectSummary">
                    <tbody>
                        <tr>
                            <th scope="row" style="width:28%">프로젝트명</th>
                            <td>
                                <b-form-input v-model="data.projectName"></b-form-input>
                            </td>
                        </tr>
                        <tr>
                            <th scope="row">주제</th>
                            <td>
                                <b-form-input v-model="data.theme"></b-form-input>
                            </td>
                        </tr>
                        <tr>
                            <th scope="row">내용</th>
                            <td>
                                <b-form-input v-model="data.content"></b-form-input>
                            </td>
                        </tr>
                        <tr>
                            <th scope="row">사용기술과 언어</th>
                            <td>
                                <b-form-tags input-id="tag" :input-attrs="{ 'aria-describedby': 'tags-remove-on-delete-help' }" v-model="tagArray"
                               separator=" ,;" placeholder="태그를 입력하세요" remove-on-delete add-on-enter class="mb-2">
                               </b-form-tags>
                            </td>
                        </tr>
                        <tr>
                            <th scope="row">깃허브 리포지토리 주소</th>
                            <td>
                               <b-form-input v-model="data.github"></b-form-input>
                            </td>
                        </tr>
                        <tr>
                            <th scope="row">인원 모집 상태</th>
                            <td>
                               <b-form-select v-model="state" :options="stateArray" value-field="item" text-field="name"></b-form-select>
                            </td>
                        </tr>
                        <tr>
                            <th scope="row">멤버 수</th>
                            <td>
                                <b-form-input v-model="data.memNum"></b-form-input>
                            </td>
                        </tr>
                    </tbody>    
                </table>
                <div style="text-align: right">
                    <b-button variant="dark" @click="save">수정</b-button> <b-button variant="danger" @click="done">취소</b-button>
                </div>
            </b-form-group>
            </form>
    </div>
</template>

<script>
import axios from 'axios';
export default {
    name: 'editProject',
    mounted() {
        axios.get('api/project/5')
        .then(response => {
            this.data = response.data
            this.tagArray = this.data.tag.split(',')

            if (this.data.rcrtState == false)
                this.state = 0
            else
                this.state = 1
        })
     },
     methods: {
        save() {
            if(this.state == 0) {
                this.stateName = "false"
            }
            else {
                this.stateName = 'true'
            }
            this.data.tag = this.tagArray.join(','),
            
            axios.post('/api/editProject/5',{
                projectId: this.data.projectId,
                projectName: this.data.projectName,
                memNum: this.data.memNum,
                theme: this.data.theme,
                content: this.data.content,
                tag: this.data.tag,
                github: this.data.github,
                rcrtState: this.stateName
                })
            .then().catch((erro)=> { console.error(erro);
            });
            this.$router.push({
                path: '/summary'
            })
         },
        done() {
            this.$router.push({
                path: '/summary'
            })
        },
        stateCheck() {
            if(this.data.rcrtState == false) {
                this.state = 0;
            }
            else
                this.state = 1;
        }
    },
    data() {
        return { 
            data: {},
            stateArray: [ { item: 0, name: '모집중'}, { item: 1, name: '모집완료'} ],
            tagArray: [],
            state: 0,
            stateName: ''
        } 
    },
}
</script>

<style>
</style>