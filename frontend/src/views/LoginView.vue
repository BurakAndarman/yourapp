<script setup>
import { reactive, ref } from 'vue';
import { useAuthStore } from '@/store/auth';

const user = reactive({
    username: '',
    password: ''
})
const auth = useAuthStore()
const error = ref('')

const onSubmit = async () => {
    try{
        if(user.username !== '' && user.password !== ''){
            await auth.login(user.username,user.password)
        }        
    } catch (e) {
        error.value = e;
    }
}

</script>

<template>
    <v-container class="d-flex align-center justify-center" style="height: 100vh;background-color: #006064;" fluid>
        <v-container class="d-flex justify-center">
            <v-card class="px-8 pb-8 pt-4" width="400">
                <v-card-item class="pl-0 pb-8 text-center">
                    <v-card-title class="text-h4">YourApp</v-card-title>
                    <v-card-subtitle class="pt-4 text-subtitle-1">Welcome To YourApp</v-card-subtitle>
                </v-card-item>
                <v-form @submit.prevent="onSubmit">
                    <v-text-field
                        v-model="user.username"
                        label="Username"
                    ></v-text-field>
                    <v-text-field
                        v-model="user.password"
                        type="password"
                        label="Password"
                    ></v-text-field>
                    <v-alert
                        v-if="error"
                        variant="tonal"
                        type="error"
                        class="mb-5 mt-5"
                    >{{ error }}</v-alert>
                    <v-btn type="submit" color="success" block class="mt-2">Login</v-btn>
                    <v-btn to="/register" color="light-blue-darken-3" block class="mt-3">Register</v-btn>
                </v-form>
            </v-card>
        </v-container>
    </v-container>
</template>

