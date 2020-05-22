<template>
    <div class="timeLine">
        <center>
            <h3 class="mt-5">TimeLine</h3>
            <hr style="border: 1px solid black ; width: 60% ; margin-bottom: 30px">
            <b-container>
                <p style="color:silver; margin: 300px auto;" v-if="data.length == 0 ? true : false">현재 알림이 없습니다.</p>
                <b-row :key="index" v-for="(item, index) in data" style="width: 60% ; min-height: 70px ; margin: 10px">
                    <b-col class="bc" cols="3">{{item.name}}</b-col>
                    <b-col class="bc2" style=" text-align: left">{{item.content}} 내용 ToDoList 서브 페이지 생성
                        <div style="text-align: right ; font-size: 13px">{{item.timelineTime}} 시간</div>
                    </b-col>
                    <hr style="width: 100% ; border-color: silver">
                </b-row>
                
            </b-container>
        </center>
    </div>
</template>

<script>
import axios from 'axios';

export default {
    name: 'timeLine',
    data() {
        return {
            data: {}
        };
    },
    mounted() {
        axios.get('api/url')
        .then(response => {
            this.data = response.data;
        });
    }
}
</script>

<style>
.bc, .bc2 { border: 1px solid darkgray ; padding-top: 20px ; margin-bottom: 10px}
.bc2 { border-left-style: none }
</style>