<script setup>
import { useAuthStore } from '../store/auth';
import { onMounted, ref } from 'vue';

const auth = useAuthStore()
const articles = ref([])
const error = ref('')

onMounted(async () => {
    const response = await fetch('http://localhost:8090/api/v1/user/get-news',{
        method : "GET",
        headers : {
            "Authorization" : `Bearer ${auth.token}`
        }
    })

    const parsedResponse = await response.json();

    if(response.status == 200) {
        articles.value = parsedResponse;
    
    } else {
        error.value = parsedResponse.message;
    }
})

</script>

<template>
    <div>
    </div>
</template>