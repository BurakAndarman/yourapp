<script setup>
    import { onMounted, reactive, ref } from 'vue';
    import { useAuthStore } from '../store/auth';
    import { useStatusStore } from '../store/status';
    import KanbanList from '../components/KanbanList.vue';
    import PlanForm from '../components/PlanForm.vue'
    import { useSavingStore } from '../store/saving';

    // Store
    const auth = useAuthStore()
    const statusDialog = useStatusStore()
    const saving = useSavingStore()

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
        expandedPlan : 0
    })
    const imagesAssoc = ref({});
    const planImageOverlay = reactive({
        isVisible : false,
        url : ''
    })
    const planForm = reactive({
        planModel : {
            id : 0,
            title : "",
            content: "",
            image : "",
            tags : [],
            kanbanList : 'TODO',
            imageIndex : null,
            created : false,
            changed : false,
            deleted : false
        },
        selectedImage : [],
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


    const sortAllTags = () => {
        planForm.allTags.sort((a, b) => {
            if (a.title < b.title) {
                return -1;
            }
            if (a.title > b.title) {
                return 1;
            }
            return 0;
        })
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
            image: "",
            tags : [],
            kanbanList : 'TODO',
            imageIndex : null,
            created : false,
            changed : false,
            deleted : false
        }

        let lastAddedTags = localStorage.getItem('lastAddedTags')

        if(lastAddedTags) {
            lastAddedTags = JSON.parse(lastAddedTags)

            planForm.allTags = lastAddedTags.map((tag) => {
                return {
                    title : tag.name,
                    value : tag
                }
            })

            sortAllTags()

        } else {
            planForm.allTags = []

        }
        
        planForm.selectedImage = []
        planForm.utils = addPlanFormUtils
        planForm.mode = 'add'
        planForm.isVisible = true
    }

    const savePlans = async () => {

        try{

            const formData = new FormData();           

            let counter = 0

            for (const [id, image] of Object.entries(imagesAssoc.value)) {

                if(image.length) {
                    const index = plans.idsPlans.indexOf(Number(id));
                    plans.allPlans[index].imageIndex = counter
                    formData.append("images",image[0])
                    counter++;
                }
                
            }

            formData.append('plansDtoList',JSON.stringify(plans.allPlans))

            saving.showSavingIndicator()

            const response = await fetch('http://localhost:8090/api/v1/user/plans',{
                method : "POST",
                headers : {
                    "Authorization" : `Bearer ${auth.token}`
                },
                body: formData
            });

            if(response.status == 401) {
                saving.closeSavingIndicator()

                auth.logout();
            }            

            if(response.status == 200) {
                const successResponse = await response.text()

                saving.closeSavingIndicator()

                statusDialog.openStatusDialog(successResponse,'success')

                imagesAssoc.value = {}

                await fetchPlans()
                
            } else {
                const errorResponse = await response.json();

                throw new Error(errorResponse.message)
            }
        }catch(e) {

            saving.closeSavingIndicator()

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
            const originalPlan = plans.allPlans[index]

            const plan = JSON.parse(JSON.stringify(originalPlan)) // For deep copying the object

            const allTags = []            
            let lastAddedTags = localStorage.getItem('lastAddedTags')

            if(lastAddedTags) {
                lastAddedTags = JSON.parse(lastAddedTags)
                lastAddedTags = lastAddedTags.filter((tag) => {

                    let notFoundInPlanTags = true;

                    for (const planTag of plan.tags) {
                        if(planTag.name === tag.name && planTag.color === tag.color) {
                            notFoundInPlanTags = false;
                            break;
                        }
                    }

                    return notFoundInPlanTags;
                })

                lastAddedTags = lastAddedTags.map((tag) => {
                    return {
                        title : tag.name,
                        value : tag
                    }
                })

                allTags.push(...lastAddedTags)
            }

            const planTags = plan.tags.map((tag) => {
                return {
                    title : tag.name,
                    value : tag
                }
            })

            allTags.push(...planTags)

            planForm.planModel = plan;

            planForm.allTags = allTags;
            if(lastAddedTags) {
                sortAllTags()
            }

            if((plan.id in imagesAssoc.value) && imagesAssoc.value[plan.id].length) {
                const imageFile = imagesAssoc.value[plan.id][0]
                planForm.selectedImage = [new File([imageFile], imageFile.name, { type: imageFile.type })] // For cloning the file
            } else {
                planForm.selectedImage = []
            }

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
        viewImage : (url) => {
            planImageOverlay.url = url;
            planImageOverlay.isVisible = true;
        }
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
    const planFormFuncs = {
        handleImageAssoc : (image, id) => {
            imagesAssoc.value[id] = image;
        },
        imageAssocExists : (id) => (id in imagesAssoc.value),
        closePlanForm : () => {
            planForm.isVisible = false;
        }
    }
</script>
<template>
    <div class="bg-surface py-14">
        <div class="mx-auto" style="width:75%;">
            <div class="d-flex justify-space-between align-center">
                <div class="text-primary">
                    <h1 class="text-h2">Plans</h1>
                    <h2 class="text-h4 mt-3">Organize Your Plans</h2>
                </div>
                <div>
                    <v-btn
                        color="primary"    
                        variant="tonal"
                        icon="mdi-plus"
                        class="me-3"
                        @click="openAddPlanForm()">
                    </v-btn>
                    <v-btn
                        color="primary"
                        variant="tonal"
                        icon="mdi-content-save"
                        @click="savePlans()">
                    </v-btn>
                </div>
            </div>
            <div v-if="loading" class="mt-10 h-screen">
                <div class="d-flex flex-column justify-center align-center ga-3">
                    <v-progress-circular color="primary" indeterminate :size="50"></v-progress-circular>
                    <p class="text-primary">Loading</p>
                </div>
            </div>
            <div v-else class="my-10 d-flex justify-space-between ga-8">
                <KanbanList title="Todo" :plans="plans.categorizedPlans.todo" :plansUtils="plansUtils"/>
                <KanbanList title="This Week" :plans="plans.categorizedPlans.this_week" :plansUtils="plansUtils"/>
                <KanbanList title="Today" :plans="plans.categorizedPlans.today" :plansUtils="plansUtils"/>
                <KanbanList title="Done" :plans="plans.categorizedPlans.done" :plansUtils="plansUtils"/>
            </div>
        </div>
    </div>    
    <v-overlay
        v-model="planImageOverlay.isVisible"
        class="justify-center align-center"
    >
        <v-img
            width="720"
            height="720"
            :src="planImageOverlay.url"
        ></v-img>
    </v-overlay>
    <PlanForm :planModelProp="planForm.planModel"
              :allTagsProp="planForm.allTags"
              :selectedImageProp="planForm.selectedImage"
              :utils="planForm.utils"
              :mode="planForm.mode"
              :isVisibleProp="planForm.isVisible"
              :planFormFuncs="planFormFuncs"
    />
</template>