<script setup lang="ts">
import {defineProps, onMounted, ref} from "vue";
import axios from "axios";
import {useRouter} from "vue-router";

const router = useRouter();

const post = ref({
    id: 0,
    title: "",
    content: "",
});

const props = defineProps({
    postId: {
        type: [Number, String],
        required: true,
    }
});

onMounted(() => {
    axios.get(`/api/posts/${props.postId}`).then((res) => {
        post.value = res.data;
    });
});

const edit = () => {
    axios.patch(`/api/posts/${props.postId}`, post.value).then(() => {
        router.replace({name: 'home'});
    });
}
</script>

<template>
    <div>
        <el-input v-model="post.title" type="text"/>
    </div>
    <div class="mt-2">
        <el-input v-model="post.content" type="textarea" row="15" />
    </div>

    <div class="mt-2 d-flex justify-content-end">
        <el-button type="primary" @click="edit()">수정완료</el-button>
    </div>
</template>

<style scoped>

</style>