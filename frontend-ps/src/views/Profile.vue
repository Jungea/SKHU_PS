<template>
  <div class="home h-100">
    <b-row class="h-100">
      <b-col class="myNav overflow-auto" cols="200px">
        <Sidebar/>
      </b-col>
      <b-col>
        <div class="profileContainer">
            <h3>마이페이지</h3>
            <hr style="margin:30px 0;">
            <div class="infoContainer">
                <h5>내 정보</h5>
                <b-button variant="dark" size="sm" @click="editProfile">정보 수정</b-button>                
                <div class="profileTable">
                    <b-table stacked="stacked" 
                    :fields="fields" 
                    :items="items" 
                    responsive="sm"
                    fixed="true"
                    table-class="table1"
                    >
                        <template v-slot:cell(name)="data">
                            {{ data.value}}
                        </template>
                        <template v-slot:cell(major)="data">
                            {{ data.value}}
                        </template>
                        <template v-slot:cell(grade)="data">
                            {{ data.value}}
                        </template>
                        <template v-slot:cell(github)="data">
                            {{ data.value}}
                        </template>
                        <template v-slot:cell(language)="data">
                            {{ data.value}}
                        </template>
                    </b-table>
                </div>
            </div>
        </div>
      </b-col>
    </b-row>
  </div>
</template>

<script>
import axios from 'axios';
import Sidebar from '@/components/Sidebar.vue'

export default {
  name: 'Profile',
  components: {
    Sidebar
  },
  data() {
      return {
        fields:['name', 'major','grade','github','language'],
        items: [],
      }
   },
   mounted() {
        axios.get('/api/user')
        .then(response => {
            this.items.push(response.data);
        }).catch((erro)=> {
          console.error(erro);
        });

   },
   methods:{
       editProfile() {
            this.$router.push({
                path: '/editProfile'
            })
        }
   }
}
</script>

<style>
    .profileContainer {
        margin:100px;
    }

    .profileContainer h3{
        font-weight: bold;
    }

    .infoContainer h5{
        font-weight: bold;
        float: left;
        margin-right: 30px;
    }
    .profileTable{
        margin: auto;
        margin-top:30px;
        max-width: 1000px;
    }
    .table1{
        text-align: center;
    }
</style>