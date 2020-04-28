<template>
    <div>
        <b-button @click="click1">Button</b-button>
    </div>

</template>

<script>
export default {
    data() {
        return {
           
        };
    },
    methods: {
        click1() {
            alert('json:'+JSON.stringify(this.$store.state.myProjectPin))
        }
    }
}
</script>

<style>

</style>