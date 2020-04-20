<template>
    <div class="findPass">
        <h3>비밀번호 찾기</h3>
        <input type="text" v-model="number" placeholder="학번"><br>
        <input type="password" v-model="password" placeholder="비밀번호"><br>
        <button @click="findPass">찾기</button>
    </div>
</template>
<script>
export default {
    name: 'FindPass',
    methods: {
        findPass() {
            this.$router.push({
                path: '/'
            })
        }
    },
}
</script>