import { defineStore } from 'pinia';

export const usePlanFormStore = defineStore({
    id: 'planform',
    state: () => {
        return {
            planModel: {
                id : 0,
                title : "",
                content: "",
                tags : [],
                kanbanList : 'TODO',
                created : false,
                changed : false,
                deleted : false
            },
            allTags : [],
            planFormUtils : {},
            isVisible : false,
            mode : ''
        }
    },
    actions: {
        setPlanModel(planModel){
            this.planModel = planModel
        },
        setPlanFormUtils(utils){
            this.planFormUtils = utils
        },
        setVisibility(status){
            this.isVisible = status
        },
        setAllTags(allTags){
            this.allTags = allTags
        },
        setMode(mode){
            this.mode = mode
        },
        reset(){
            this.planModel = {
                id : 0,
                title : "",
                content: "",
                tags : [],
                kanbanList : 'TODO',
                created : false,
                changed : false,
                deleted : false
            }
            this.allTags = []
        }
    }
})