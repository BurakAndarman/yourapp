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
        async login(userName, password){

            const base64encodedData = Buffer.from(`${userName}:${password}`).toString('base64')

            const response = await fetch('http://localhost:8090/api/v1/user/token',{
                method: 'GET',
                headers: {
                    'Authorization': `Basic ${base64encodedData}`
                }
            })

            if(response.status == 200) {
                const token = await response.text()

                localStorage.setItem('user', JSON.stringify(userName))
                localStorage.setItem('token', JSON.stringify(token))

                this.user = userName
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

            router.push('/enter')
        }
    }
})