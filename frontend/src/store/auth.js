import { defineStore } from 'pinia';
import { Buffer } from 'buffer';
import router from '@/router'

export const useAuthStore = defineStore({
    id: 'auth',
    state: () => {
        return {
            user: localStorage.getItem('user') ? JSON.parse(localStorage.getItem('user')) : '',
            token: localStorage.getItem('token') ? JSON.parse(localStorage.getItem('token')) : '',
            returnUrl: '/'
        }
    },
    actions: {
        async login(username, password){

            const base64encodedData = Buffer.from(`${username}:${password}`).toString('base64')

            const response = await fetch('http://localhost:8090/api/v1/auth/token',{
                method: 'GET',
                headers: {
                    'Authorization': `Basic ${base64encodedData}`
                }
            })

            if(response.status == 200) {
                const token = await response.text()

                localStorage.setItem('user', JSON.stringify(username))
                localStorage.setItem('token', JSON.stringify(token))

                this.user = username
                this.token = token

                router.push(this.returnUrl || '/')
            
            } else {
                throw new Error('Invalid Credentials')
            }
        },
        logout() {
            localStorage.removeItem('user')
            localStorage.removeItem('token')

            this.user = ''
            this.token = ''

            router.push('/login')
        }
    }
})