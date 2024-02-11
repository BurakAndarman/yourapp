import { defineStore } from 'pinia';

export const useSavingStore = defineStore({
    id: 'saving',
    state: () => {
        return {
            saving: false
        }
    },
    actions: {
        closeSavingIndicator() {
            this.saving = false;
        },
        showSavingIndicator(){
            this.saving = true;
        }
    }
})