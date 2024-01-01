<script setup>
    import { onMounted, reactive } from 'vue';
    import { useAuthStore } from '../store/auth';
    import { useStatusStore } from '../store/status';

    const auth = useAuthStore()
    const statusDialog = useStatusStore()
    const plansCategorized = reactive({
        "todo": [],
        "this_week": [],
        "today": [],
        "done": []
    })

    onMounted(async () => {

        try{
            const response = await fetch('http://localhost:8090/api/v1/user/plans',{
                method : "GET",
                headers : {
                    "Authorization" : `Bearer ${auth.token}`
                }
            });

            if(response.status == 401) {
                auth.logout();
            }

            const parsedResponse = await response.json();

            if(response.status == 200) {
                categorizePlans(parsedResponse)
            
            } else {
                throw new Error(parsedResponse.message)
            }
        }catch(e) {

            statusDialog.openStatusDialog(e,'error')

        }
    })

    const categorizePlans = (plans) => {
        plansCategorized.todo = plans.filter((plan) => plan.kanbanList == "TODO");
        plansCategorized.this_week = plans.filter((plan) => plan.kanbanList == "THIS_WEEK");
        plansCategorized.today = plans.filter((plan) => plan.kanbanList == "TODAY");
        plansCategorized.done = plans.filter((plan) => plan.kanbanList == "DONE");
    }

</script>
<template>
    <div class="mx-auto" style="width:75%;">
        <div class="my-14 d-flex justify-space-between align-center">
            <div class="text-cyan-darken-4">
                <h1 class="text-h2">Plans</h1>
                <h2 class="text-h4 mt-3">Organize Your Plans</h2>
            </div>
            <div>
            </div>
        </div>
        <div class="mt-10 d-flex justify-center">
            <div class="d-flex flex-column justify-center align-center ga-3">
                <v-progress-circular color="cyan-darken-4" indeterminate :size="50"></v-progress-circular>
                <p class="text-cyan-darken-4">Loading</p>
            </div>           
        </div>
    </div>  
</template>