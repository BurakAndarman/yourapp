<script setup>
import { reactive, ref } from 'vue';
import { useAuthStore } from '@/store/auth';

const user = reactive({
    username: '',
    password: ''
})
const auth = useAuthStore()
const error = ref('');

const onSubmit = async () => {
    try{
        await auth.login(user.username,user.password);
    } catch (e) {
        error.value = e;
    }
}

</script>

<template>
    <div class="row">
        <div class="col-sm-4">
        <h2 class="align-center">Login</h2>
            <form @submit.prevent="onSubmit">
                <div class="form-group">
                    <label for="username">Username</label>
                    <input type="username" v-model="user.username" class="form-control" placeholder="User">
                </div>
                <div class="form-group">
                    <label for="password">Password</label>
                    <input type="password" v-model="user.password" class="form-control" placeholder="Password">
                </div>
                <br>
                <div class="alert aler-danger" v-if="error">{{ error }}</div>
                <button type="submit" class="btn btn-primary">Login</button>
                <button class="btn btn-info">
                    <RouterLink to="/register">Register</RouterLink>
                </button>                
            </form>
        </div>
    </div>
</template>

