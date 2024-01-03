import { defineStore } from 'pinia';

export const useExpandPlanStore = defineStore({
    id: 'expandPlan',
    state: () => {
        return {
            id:0
        }
    },
    actions: {
        reveal(id) {
            this.id = id
        },
        hide() {
            this.id = 0
        }
    }
})