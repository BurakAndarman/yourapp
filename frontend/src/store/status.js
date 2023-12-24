import { defineStore } from 'pinia';

export const useStatusStore = defineStore({
    id: 'status',
    state: () => {
        return {
            isVisible: false,
            message: '',
            status: ''
        }
    },
    actions: {
        closeStatusDialog() {
            this.isVisible = false;
            this.message = '';
            this.status = '';
        },
        openStatusDialog(message,status){
            this.message = message;
            this.status = status;
            this.isVisible = true;
        }
    }
})