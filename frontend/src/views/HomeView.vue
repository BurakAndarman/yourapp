<script setup>
import { useAuthStore } from '../store/auth';
import { onMounted, ref } from 'vue';

const auth = useAuthStore()
const articles = ref([])
const error = ref('')

onMounted(async () => {/*
    const response = await fetch('http://localhost:8090/api/v1/user/get-news',{
        method : "GET",
        headers : {
            "Authorization" : `Bearer ${auth.token}`
        }
    })

    if(response.status == 200) {
        data.articles = response.json();
    
    } else {
        error.value = response.json().message;
    }
    */
})

</script>

<template>
    <div>
    </div>
</template>