<script setup>
    import { onMounted, reactive, ref } from 'vue';
    import { useAuthStore } from '../store/auth';
    import { useStatusStore } from '../store/status';
    import KanbanList from '../components/KanbanList.vue';
    import PlanForm from '../components/PlanForm.vue'

    // Store
    const auth = useAuthStore()
    const statusDialog = useStatusStore()

    // States
    const loading = ref(true);
    const plans = reactive({
        allPlans : [],
        idsPlans : [],
        categorizedPlans : {
            todo : [],
            this_week : [],
            today : [],
            done : []
        },
        expandedPlan : 0,
    })
    const planForm = reactive({
        planModel : {
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
        utils : {},
        isVisible : false,
        mode : '',
    })

    // Functions
    const afterFetchPlans = () => {
        plans.idsPlans = plans.allPlans.map((plan) => {

            plan.tags.sort((a, b) => {
                if (a.name < b.name) {
                    return -1;
                }
                if (a.name > b.name) {
                    return 1;
                }
                return 0;
            });

            return plan.id;
        });

        categorizePlans()
    }

    const categorizePlans = () => {
        plans.categorizedPlans.todo = plans.allPlans.filter((plan) => (plan.kanbanList === "TODO" && !plan.deleted));
        plans.categorizedPlans.this_week = plans.allPlans.filter((plan) => (plan.kanbanList === "THIS_WEEK" && !plan.deleted));
        plans.categorizedPlans.today = plans.allPlans.filter((plan) => (plan.kanbanList === "TODAY" && !plan.deleted));
        plans.categorizedPlans.done = plans.allPlans.filter((plan) => (plan.kanbanList === "DONE" && !plan.deleted));
    }

    const openAddPlanForm = () => {
        planForm.planModel = {
            id : 0,
            title : "",
            content: "",
            tags : [],
            kanbanList : 'TODO',
            created : false,
            changed : false,
            deleted : false
        }
        planForm.allTags = []
        planForm.utils = addPlanFormUtils
        planForm.mode = 'add'
        planForm.isVisible = true
    }

    const savePlans = async () => {

        try{
            const response = await fetch('http://localhost:8090/api/v1/user/plans',{
                method : "POST",
                headers : {
                    "Content-Type" : "application/json",
                    "Authorization" : `Bearer ${auth.token}`
                },
                body: JSON.stringify(plans.allPlans)
            });

            if(response.status == 401) {
                auth.logout();
            }            

            if(response.status == 200) {
                const successResponse = await response.text()

                statusDialog.openStatusDialog(successResponse,'success')

                await fetchPlans()
            } else {
                const errorResponse = await response.json();

                throw new Error(errorResponse.message)
            }
        }catch(e) {

            statusDialog.openStatusDialog(e,'error')

        }

    }

    onMounted(async () => {

        await fetchPlans()

    })

    const fetchPlans = async () => {

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
                plans.allPlans = parsedResponse

                afterFetchPlans()

                if(loading.value) {
                    loading.value = false
                }                
            
            } else {
                throw new Error(parsedResponse.message)
            }
        }catch(e) {

            statusDialog.openStatusDialog(e,'error')

        }

    }

    // Functions to be used in KanbanList subcomponent
    const plansUtils = {
        changeList : (id, newList) => {
            const index = plans.idsPlans.indexOf(id);
            plans.allPlans[index].kanbanList = newList;
            plans.allPlans[index].changed = true;
            categorizePlans();
        },
        deletePlan : (id) => {
            const index = plans.idsPlans.indexOf(id);
            plans.allPlans[index].deleted = true;
            categorizePlans();
        },
        openChangePlanForm : (id) => {
            plans.expandedPlan = 0
            const index = plans.idsPlans.indexOf(id)
            const plan = JSON.parse(JSON.stringify(plans.allPlans[index])) // For deep copying the object
            const allTags = plan.tags.map((tag) => {
                return {
                    title : tag.name,
                    value : tag
                }
            })

            planForm.planModel = plan;
            planForm.allTags = allTags;            
            planForm.utils = changePlanFormUtils;
            planForm.mode = 'change';
            planForm.isVisible = true;
        },
        expandPlan : (id) => {
            plans.expandedPlan = id;
        },
        hidePlan : () => {
            plans.expandedPlan = 0;
        },
        currentExpandedPlan : () => plans.expandedPlan,
    }

    // Functions to be used in PlanForm subcomponent for changing plan
    const changePlanFormUtils = {
        changeExistingPlan : (plan) => {
            const index = plans.idsPlans.indexOf(plan.id)
            plans.allPlans[index] = plan            
            categorizePlans()
        }
    }

    // Functions to be used in PlanForm subcomponent for adding plan
    const addPlanFormUtils = {
        generatePlanId : () => plans.idsPlans.length ? parseInt(plans.idsPlans.slice(-1)) + 1 : 1,
        addNewPlan : (plan) => {
            plans.allPlans.push(plan)
            plans.idsPlans.push(plan.id)
            categorizePlans()
        }
    }

    // Other functions to be used in PlanForm subcomponent
    const closePlanForm = () => {
        planForm.isVisible = false;
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
                    icon="mdi-plus"
                    class="me-3"
                    @click="openAddPlanForm()">
                </v-btn>
                <v-btn
                    color="cyan-darken-4"
                    variant="tonal"
                    icon="mdi-content-save"
                    @click="savePlans()">
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
            <KanbanList title="Todo" :plans="plans.categorizedPlans.todo" :plansUtils="plansUtils"/>
            <KanbanList title="This Week" :plans="plans.categorizedPlans.this_week" :plansUtils="plansUtils"/>
            <KanbanList title="Today" :plans="plans.categorizedPlans.today" :plansUtils="plansUtils"/>
            <KanbanList title="Done" :plans="plans.categorizedPlans.done" :plansUtils="plansUtils"/>
        </div>
    </div>
    <PlanForm :planModelProp="planForm.planModel"
              :allTagsProp="planForm.allTags"
              :utils="planForm.utils"
              :mode="planForm.mode"
              :isVisibleProp="planForm.isVisible"
              :closePlanForm="closePlanForm"
    />
</template>