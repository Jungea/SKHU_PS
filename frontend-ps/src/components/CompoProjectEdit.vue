<template>
    <div class="containerStyle">
        <h3>프로젝트 개요 수정</h3>
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
                                <b-form-invalid-feedback :state="nameValidation">
                                    프로젝트 이름을 입력해주세요.
                                </b-form-invalid-feedback>
                            </td>
                        </tr>
                        <tr>
                            <th scope="row">주제</th>
                            <td>
                                <b-form-input v-model="data.theme"></b-form-input>
                                <b-form-invalid-feedback :state="themeValidation">
                                    주제를 입력해주세요.
                                </b-form-invalid-feedback>
                            </td>
                        </tr>
                        <tr>
                            <th scope="row">내용</th>
                            <td>
                                <b-form-textarea v-model="data.content"></b-form-textarea>
                                <b-form-invalid-feedback :state="contentValidation">
                                    프로젝트 내용을 입력해주세요.
                                </b-form-invalid-feedback>
                            </td>
                        </tr>
                        <tr>
                            <th scope="row">사용기술과 언어</th>
                            <td>
                                <b-form-tags input-id="tag" :input-attrs="{ 'aria-describedby': 'tags-remove-on-delete-help' }" v-model="tagArray"
                               separator=" ,;" placeholder="태그를 입력하세요" remove-on-delete add-on-enter class="mb-2">
                               </b-form-tags>
                               <b-form-invalid-feedback :state="tagValidation">
                                    태그를 입력해주세요.
                                </b-form-invalid-feedback>
                            </td>
                        </tr>
                        <tr>
                            <th scope="row">깃허브 리포지토리 주소</th>
                            <td>
                               <b-form-input v-model="data.github"></b-form-input>
                            </td>
                        </tr>
                    </tbody>    
                </table>
                <div style="text-align: right">
                    <b-button variant="dark" @click="save">수정</b-button> <b-button variant="danger" @click="cancle">취소</b-button>
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
        axios.get('/api/project/'+this.$route.params.projectId)
        .then(response => {
            this.data = response.data
            this.tagArray = this.data.tag.split(',')
        })
     },
     methods: {
        save() {
            this.data.tag = this.tagArray.join(',')
            
            if(!this.nameValidation)
                alert("프로젝트 이름을 입력하세요.")
            else if(!this.themeValidation)
                alert("프로젝트 주제를 입력하세요.")
            else if(!this.contentValidation)
                alert("프로젝트 내용을 입력하세요.")
            else if(!this.tagValidation)
                alert("태그를 입력하세요.")
            else {
                axios.post('/api/project/'+this.$route.params.projectId+'/edit',{
                    projectId: this.data.projectId,
                    projectName: this.data.projectName,
                    theme: this.data.theme,
                    content: this.data.content,
                    tag: this.data.tag,
                    github: this.data.github,
                    progState: this.data.progState,
                    rcrtState: this.data.rcrtState
                    })
                .then().catch((erro)=> { console.error(erro);
                });
                
                location.href='/project/'+this.$route.params.projectId+'/summary'    
            }        
         },
        cancle() {
                    this.$router.push({
                    path: '/project/'+this.$route.params.projectId+'/summary'
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
        } 
    },
    computed: {
        nameValidation() {
            return !this.data.projectName == ''
        },
        themeValidation() {
            return !this.data.theme == ''
        },
        contentValidation() {
            return !this.data.content == ''
        },
        tagValidation() {
            return this.tagArray.length > 0
        }
    }
}
</script>

<style>
.containerStyle h3{
    font-weight: bold;
}

.containerStyle {
    margin: 100px;
    min-width: 500px;
}
</style>