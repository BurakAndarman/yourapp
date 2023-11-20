<template>
    <div class="row">
        <div class="col-sm-4">
        <h2 class="align-center">Login</h2>
            <form @submit.prevent="submitLogin">
                <div class="form-group">
                    <label for="email">Email</label>
                    <input type="email" v-model="loginData.email" class="form-control" placeholder="email">
                </div>
                <div class="form-group">
                    <label for="password">Password</label>
                    <input type="password" v-model="loginData.password" class="form-control" placeholder="Password">
                </div>
                <br>
                <div class="alert aler-danger" v-if="errorResponse.error">{{ errorResponse.error }}</div>
                <button type="submit" class="btn btn-primary">Login</button>
                <button class="btn btn-info">
                    <RouterLink to="/register">Register</RouterLink>
                </button>                
            </form>
        </div>
    </div>
</template>

<script>
    import axios from 'axios'
    import { reactive } from 'vue'
    import router from '@/router'

    export default {
        setup() {
            let loginData = reactive({
                email: "",
                password : ""
            })
            let errorResponse = reactive({
                error: ""
            })

            const submitLogin = () => {
                axios.post("http://localhost:8090/api/v1/user/login", loginData)
                    .then((response) => {
                        if(response.data.status) {
                            router.push({name: 'home', params: { name: loginData.email}})
                        }else {
                            errorResponse.error = response.data.message
                        }
                        
                    })
                    .catch(error => {
                        errorResponse.error = error
                    })
            }

            return {loginData, submitLogin, errorResponse}
        }
    }
</script>

