<script setup>
import { useAuthStore } from '../store/auth';
import { onMounted, ref } from 'vue';

const auth = useAuthStore()
const data = ref('')

const logout = () => {
    auth.logout()
}

onMounted(async () => {
    const response = await fetch('http://localhost:8090/api/v1/user/',{
        method : "GET",
        headers : {
            "Authorization" : `Bearer ${auth.token}`
        }
    })

    if(response.status == 200) {
        data.value = await response.text();
    
    } else {
        data.value = "An error happened while fetching data"
    } 
})

</script>

<template>
    <h1>
        {{ data }}
        <a href="#" @click="logout">Logout</a>
    </h1>
</template>