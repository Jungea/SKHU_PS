<template>
    <div>
        <h4 style="margin-top: 80px">프로젝트 개요</h4>
        <div style="text-align: right">
            <b-button v-b-modal.modal-xl variant="outline-secondary" class="mb-5">수정</b-button>
        </div>
             <b-table class="b1t" :items="data" :fields="fields" stacked>
            </b-table>

         <b-modal id="modal-xl" size="lg" title="프로젝트 수정" @OK="save">
            <b-table stacked :items="data" :fields="fields">
                <template v-slot:cell(projectName)="row">
                    <b-form-input v-model="row.item.projectName"/>
                </template>
                <template v-slot:cell(tag)="row">
                    <b-form-input v-model="row.item.tag"/>
                </template>
                <template v-slot:cell(theme)="row">
                    <b-form-input v-model="row.item.theme"/>
                </template>
                <template v-slot:cell(content)="row">
                    <b-form-input v-model="row.item.content"/>
                </template>
                <template v-slot:cell(memNum)="row">
                    <b-form-textarea v-model="row.item.memNum"/>
                </template>
            </b-table>
        </b-modal>
    </div>
</template>

<script>
import axios from 'axios';
export default {
     mounted() {
        axios.get('api/user/project1')
        .then(response => {
            this.data.push(response.data)
        })
     },
     methods: {
        /*
        save() {
            axios.post('url',{
                projectName: this.data.projectName,
                tag: this.data.tag,
                theme: this.data.theme,
                content: this.data.content,
                memNum: this.data.memNum
            })
            .then(response => {
           
            })
        }*/
    },
    data() {
        return { 
            data: [],
            fields: [{
                    key: "projectName",
                    label: "프로젝트 이름"
                },
                {
                    key: "tag",
                    label: "태그"
                },
                {
                    key: "theme",
                    label: "주제"
                },
                {
                    key: "content",
                    label: "내용"
                },
                {
                    key: "memNum",
                    label: "조원"}
                ]           
        } 
    },
}
</script>

<style>
.asd {background-color: }
</style>