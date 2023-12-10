<template>
    <div class="card align-left">
        <div class="card-header">Register Form</div>
        <div class="card-body">
            <form @submit.prevent="submitRegister">
                <div class="form-group">
                    <label for="firstName">First Name</label>
                    <input type="text" v-model="registerData.firstName" name="firstName" id="firstName" class="form-control">
                </div>
                <div class="form-group">
                    <label for="email">Email</label>
                    <input type="email" v-model="registerData.email" name="email" id="email" class="form-control">
                </div>
                <div class="form-group">
                    <label for="password">Password</label>
                    <input type="password" v-model="registerData.password" name="password" id="password" class="form-control">
                </div>
                <br>
                <div class="alert aler-danger" v-if="errorResponse.error">{{ errorResponse.error }}</div>
                <button type="submit" class="btn btn-success">Save</button>
            </form>
        </div>
    </div>
</template>

<script>
    //import axios from 'axios'
    import { reactive } from 'vue'
    //import router from '@/router'

    export default {
        setup() {
            let registerData = reactive({
                firstName: "",
                email: "",
                password : "",
            })
            let errorResponse = reactive({
                error: ""
            })

            const submitRegister = () => {
                /*
                axios.post("http://localhost:8090/api/v1/user/save", registerData)
                    .then(() => {
                        router.push({name: 'login'})
                    })
                    .catch(error => {
                        errorResponse.error = error
                    })
                    */
            }

            return {registerData, submitRegister, errorResponse}
        }
    }
</script>