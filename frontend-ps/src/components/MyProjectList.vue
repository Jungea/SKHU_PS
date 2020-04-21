<template>
    <div>
        <center>    
            <b-container class="bv-row">    
                <b-row class="m-4">    
                    <b-col>    
                        <center>    
                            <h4>내 프로젝트</h4>    
                        </center>    
                    </b-col>    
                </b-row>    
                <b-row>    
                    <b-button variant="outline-secondary" style="width: 100px ; height: 70px;">프로젝트 생성</b-button>    
                </b-row>    
                <b-row>    
                    <b-card align="left" bg-variant="dark" :header="item.name" text-variant="white" v-b-modal.modal-1 :key="item" v-for="item in data" style="width: 25rem; height: 10rem; margin: 60px;">    
                        <b-card-text>    
                            {{item.word}}    
                        </b-card-text>    
                    </b-card>    
                </b-row>    
            </b-container>    
        </center>    
    
        <!-- 예시용 모달 -->
        <b-modal id="modal-1" title="BootstrapVue">
        <table border="1">
        <tr>
            <td width="150px">프로젝트명</td>
            <td width="300px">자바플젝</td>
        </tr>
        <tr>
            <td>사용언어</td>
            <td>자바</td>
        </tr>
        <tr>
            <td>사용도구</td>
            <td>스프링</td>
        </tr>
        <tr>
            <td>주제</td>
            <td>ooo로 자바 만들기</td>
        </tr>
        <tr>
            <td>내용</td>
            <td>자바로 뭐뭐만들기</td>
        </tr>
        <tr>
            <td>모집여부</td>
            <td>모집중</td>
        </tr>
        </table>
        </b-modal>
<!----------------------------------------------------------->
    </div>

</template>

<script>
export default {
    data() {
        return {
            data: [{
                    name: '디비 플젝',
                    word: '내용1'
                },
                {
                    name: '파이썬 플젝',
                    word: '내용2'
                },
                {
                    name: '소프 플젝',
                    word: '내용3'
                },
                {
                    name: '자바 플젝',
                    word: '내용4'
                },
                {
                    name: '제목',
                    word: '내용5'
                }
            ]
        };
    }
}
</script>

<style>

</style>