<template>
    <b-container id="app" fluid>
        <div>
            <b-sidebar id="sidebar" no-header visible="true" style="width:350px">
            <template v-slot:footer="{hide}">
            <div class="d-flex bg-dark text-light align-items-center px-3 py-2">
                <strong class="mr-auto">Footer</strong>
                <b-button size="sm" @click="hide">Close</b-button>
            </div>
            </template>
            <div class="px-3 py-2">
                <p>
                Cras mattis consectetur purus sit amet fermentum. Cras justo odio, dapibus ac facilisis
                in, egestas eget quam. Morbi leo risus, porta ac consectetur ac, vestibulum at eros.
                </p>
                <b-img src="https://picsum.photos/500/500/?image=54" fluid thumbnail></b-img>
            </div>
            </b-sidebar>
        </div>
        <b-row no-gutters class="h-100">
            <b-col id="content" style="">
                <b-row no-gutters class="h-100">
                    <b-col style="background:red">
                        <b-button v-b-toggle.sidebar>Toggle Sidebar</b-button>
                    </b-col>
                    <b-col style="background:blue">
                        <b-button @click="toggle">Toggle Sidebar</b-button>
                    </b-col>
                </b-row>
            </b-col>       
        </b-row>
    </b-container>
</template>>

<script>
export default {
    name: "test2",
    data(){
        return{
        }
    },
    methods:{
    }
}
</script>

<style scoped>
@media screen and (max-width: 480px){
    #content{
        margin-left: 0;
    }
}
    #app{
        padding: 0px; 
        background:yellow;
    }
    .header{
        background:silver;
    }

    #sidebar{
        background: red;
    }

    #content{
        margin-left:350px;
    }
</style>