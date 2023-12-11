<script setup>
    import { reactive, ref } from 'vue';
    import { useAuthStore } from '@/store/auth';

    const user = reactive({
        userName: '',
        password: ''
    })
    const auth = useAuthStore()
    const isRegister = ref(false)
    const error = ref('')
    const success = ref('')

    const onSubmit = async () => {
        try{
            clearMessages()

            if(user.userName !== '' && user.password !== ''){
                if(isRegister.value) {
                    await register()
                } else {
                    await auth.login(user.userName,user.password)
                }            
            }        
        } catch (e) {
            error.value = e
        }
    }

    const register = async () => {
        const response = await fetch('http://localhost:8090/api/v1/user/save',{
            method : "POST",
            headers : {
                "Content-Type" : "application/json"
            },
            body : JSON.stringify(user)
        })

        if(response.status == 200) {
            success.value = "User added successfully"
        
        } else {
            const errorResponse = await response.json()
            throw new Error(errorResponse.message)
        }
    }

    const switchPage = () => {
        clearMessages()
        isRegister.value = !isRegister.value        
    }

    const clearMessages = () => {
        error.value = ''
        success.value = ''
    }
</script>

<template>
    <div class="d-flex align-center justify-center" style="height: 100vh;background-color: #006064;">
        <div class="d-flex justify-center">
            <v-card class="px-8 pb-8 pt-4" width="400">
                <v-card-item class="pl-0 pb-8 text-center">
                    <v-card-title class="text-h4">YourApp</v-card-title>
                    <v-card-subtitle class="pt-4 text-subtitle-1">Welcome To YourApp</v-card-subtitle>
                </v-card-item>
                <v-form @submit.prevent="onSubmit">
                    <v-text-field
                        v-model="user.userName"
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
                    <v-alert
                        v-if="success"
                        variant="tonal"
                        type="success"
                        class="mb-5 mt-5"
                    >{{ success }}</v-alert>
                    <v-btn type="submit" color="success" block class="mt-2">{{ isRegister ? 'Register' : 'Login' }}</v-btn>
                    <v-btn 
                        @click="switchPage" 
                        :color="isRegister ? 'orange-darken-3' : 'light-blue-darken-3'" block class="mt-3"
                    >
                        {{ isRegister ? 'Back To Login' : 'Register' }}
                    </v-btn>
                </v-form>
            </v-card>
        </div>
    </div>
</template>

