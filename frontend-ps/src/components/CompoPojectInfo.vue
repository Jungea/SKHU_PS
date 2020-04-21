<template>
    <div>
        <center>    
            <b-container class="bv-row">    
                <b-row class="m-4">    
                    <b-col>    
                        <center>    
                            <h4>파이썬 프로젝트</h4>    
                        </center>    
                    </b-col>    
                </b-row>    
                <b-row>    
                    <b-button variant="outline-secondary" style="width: 100px ; height: 70px;">프로젝트 생성</b-button>    
                </b-row>    
                <b-row>    
                    <b-card align="left" bg-variant="dark" :header="item.name" text-variant="white" v-b-modal.modal-xl :key="item" v-for="item in data" style="width: 25rem; height: 10rem; margin: 60px;">    
                        <b-card-text>    
                            {{item.word}}    
                        </b-card-text>    
                    </b-card>    
                </b-row>    
            </b-container>    
        </center>    
    
        <!-- 예시용 모달 -->
        <b-modal id="modal-xl" size="lg" title="프로젝트 이름">
              <b-table stacked  :items="items"></b-table>
        </b-modal>
    </div>

</template>

<script>
export default {
    data() {
        return {
            items: [
          {'플젝 이름': '파이썬 프로젝트', '사용 언어': '자바','사용 도구':'스프링','주제':'자바로 계산기 만들기','내용':'자바를 이용해서 배운걸 토대로 만들어볼 예정입니다.','모집여부':'모집중'},
        ],
        };
    }
}
</script>

<style>

</style>