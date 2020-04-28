// store.js
import Vue from "vue";
import Vuex from "vuex";

Vue.use(Vuex);

export const store = new Vuex.Store({
    state: {
        myProjectPin:[], // 사이드 바의 셀렉션에 나타날 핀 프로젝트
        selectedPinProjectId:null // 사이드바에서 셀렉트 된 프로젝트
    },
    mutations: {
        makeMyProjectPin(state,data){  
            let count=0;
            state.myProjectPin=[]
            for (let i=0;i<data.length;i++) {
                if(data[i].pin==true) {
                    state.myProjectPin[count]={}
                    state.myProjectPin[count].projectName=data[i].projectName
                    state.myProjectPin[count++].projectId=data[i].projectId
                }
            }
        },
        changeSelectedPinProjectId(state,pinProjectId) {
            state.selectedPinProjectId=pinProjectId
        }
    }

});