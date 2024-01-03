<script setup>
    import { onMounted, reactive, ref } from 'vue';
    import { useAuthStore } from '../store/auth';
    import { useStatusStore } from '../store/status';
    import KanbanList from '../components/KanbanList.vue';

    const auth = useAuthStore()
    const statusDialog = useStatusStore()
    const loading = ref(true)
    const allPlans = ref([]);
    const idList = ref([]);
    const plansCategorized = reactive({
        "todo": [],
        "this_week": [],
        "today": [],
        "done": []
    })
    const planUtils = {
        changeList : (id, newList) => {
            const index = idList.value.indexOf(id);
            allPlans.value[index].kanbanList = newList;
            allPlans.value[index].changed = true;
            categorizePlans();
        }
    }

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
                allPlans.value = parsedResponse

                extractIdList()
                categorizePlans()
                loading.value = false
            
            } else {
                throw new Error(parsedResponse.message)
            }
        }catch(e) {

            statusDialog.openStatusDialog(e,'error')

        }
    })

    const categorizePlans = () => {
        plansCategorized.todo = allPlans.value.filter((plan) => plan.kanbanList == "TODO");
        plansCategorized.this_week = allPlans.value.filter((plan) => plan.kanbanList == "THIS_WEEK");
        plansCategorized.today = allPlans.value.filter((plan) => plan.kanbanList == "TODAY");
        plansCategorized.done = allPlans.value.filter((plan) => plan.kanbanList == "DONE");
    }

    const extractIdList = () => {
        idList.value = allPlans.value.map((plan) => plan.id);
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
                <v-btn
                    color="cyan-darken-4"
                    variant="tonal"
                    icon="mdi-content-save">
                </v-btn>
            </div>
        </div>
        <div v-if="loading" class="mt-10 d-flex justify-center">
            <div class="d-flex flex-column justify-center align-center ga-3">
                <v-progress-circular color="cyan-darken-4" indeterminate :size="50"></v-progress-circular>
                <p class="text-cyan-darken-4">Loading</p>
            </div>           
        </div>
        <div v-else class="my-10 d-flex justify-space-between ga-8">
            <KanbanList title="Todo" :plans="plansCategorized.todo" :planUtils="planUtils"/>
            <KanbanList title="This Week" :plans="plansCategorized.this_week" :planUtils="planUtils"/>
            <KanbanList title="Today" :plans="plansCategorized.today" :planUtils="planUtils"/>
            <KanbanList title="Done" :plans="plansCategorized.done" :planUtils="planUtils"/>
        </div>
    </div>  
</template>