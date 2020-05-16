<template>
    <b-container id="app" fluid>
        <b-row no-gutters class="h-100">
        <!--사이드바 영역-->
            <b-col lg="3" class="header">
                <b-row align-v="start">
                    <b-col>사이드바 헤더</b-col>
                </b-row>
                <b-row align-v="center">
                    <b-col>               
                        <b-nav vertical>
                            <b-nav-item>Active</b-nav-item>
                            <b-nav-item>Link</b-nav-item>
                            <b-nav-item>Another Link</b-nav-item>
                            <b-nav-item>Disabled</b-nav-item>
                        </b-nav>
                    </b-col>
                </b-row>
                <b-row align-v="end">
                    <b-col>푸터</b-col>
                </b-row>
            </b-col>
        <!--컨텐츠 영역-->
            <b-col lg="9" class="content">
                <div>
                    <b-jumbotron header="BootstrapVue" lead="Bootstrap v4 Components for Vue.js 2">
                        <p>For more information visit website</p>
                        <b-button variant="primary" href="#">More Info</b-button>
                    </b-jumbotron>
                </div>
            </b-col>
        </b-row>
    </b-container>
</template>>

<script>
export default {
    name: "test",
}
</script>

<style scoped>
    #app{
        padding: 0px; 
        background:yellow;
    }
    .header{
        background:silver;
    }
</style>