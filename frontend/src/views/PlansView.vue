<script setup>
    import { onMounted, reactive, ref } from 'vue';
    import { useAuthStore } from '../store/auth';
    import { useStatusStore } from '../store/status';
    import KanbanList from '../components/KanbanList.vue';

    // Constants
    const allLists = [
        {
            text : 'Todo',
            value : 'TODO'
        },{
            text : 'This Week',
            value : 'THIS_WEEK'
        },{
            text : 'Today',
            value : 'TODAY'
        },{
            text : 'Done',
            value : 'DONE'
        }
    ];

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
        planModel : {
            title : "",
            content: "",
            tags : [],
            kanbanList : 'TODO',
            created : false,
            changed : false,
            deleted : false
        }
    })
    const tags = reactive({
        allTags : [],
        tagModel : {
            tagName: '',
            color: ''
        }
    });
    const addPlanVisible = ref(false);
    const addTagVisible = ref(false);
    const addPlanValid = ref(false);    
    const addTagValid = ref(false);

    // Form Rules
    const rules = {
        length : len => v => (v || '').length <= len || `Invalid character length, required ${len} at max`,
        required: v => !!v || 'This field is required',
        count: len => v => v.length <= len || `You can select ${len} options at most`
    };

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
        changePlan : (id) => {

        },
        expandPlan : (id) => {
            plans.expandedPlan = id;
        },
        hidePlan : () => {
            plans.expandedPlan = 0;
        },
        currentExpandedPlan : () => {
            return plans.expandedPlan;
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
                plans.allPlans = parsedResponse

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
        plans.categorizedPlans.todo = plans.allPlans.filter((plan) => (plan.kanbanList === "TODO" && !plan.deleted));
        plans.categorizedPlans.this_week = plans.allPlans.filter((plan) => (plan.kanbanList === "THIS_WEEK" && !plan.deleted));
        plans.categorizedPlans.today = plans.allPlans.filter((plan) => (plan.kanbanList === "TODAY" && !plan.deleted));
        plans.categorizedPlans.done = plans.allPlans.filter((plan) => (plan.kanbanList === "DONE" && !plan.deleted));
    }

    const extractIdList = () => {
        plans.idsPlans = plans.allPlans.map((plan) => plan.id);
    }

    const addPlan = () => {

    }

    const addTag = () => {
        tags.allTags.push(tags.tagModel)
        plans.planModel.tags.push(tags.tagModel.tagName)
        tags.tagModel = {
            tagName: '',
            color: ''
        }
        addTagVisible.value = false
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
                    @click="addPlanVisible = true">
                </v-btn>
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
            <KanbanList title="Todo" :plans="plans.categorizedPlans.todo" :plansUtils="plansUtils"/>
            <KanbanList title="This Week" :plans="plans.categorizedPlans.this_week" :plansUtils="plansUtils"/>
            <KanbanList title="Today" :plans="plans.categorizedPlans.today" :plansUtils="plansUtils"/>
            <KanbanList title="Done" :plans="plans.categorizedPlans.done" :plansUtils="plansUtils"/>
        </div>
        <v-dialog
            transition="dialog-bottom-transition"
            v-model="addPlanVisible"
            width="auto"
        >
            <v-card
                width="400"
            >
                <v-form
                    v-model="addPlanValid"
                >
                    <v-toolbar
                        color="cyan-darken-4"
                        title="Add Plan"
                    ></v-toolbar>
                    <div class="ma-4">
                        <v-text-field
                            v-model="plans.planModel.title"
                            :rules="[rules.length(50),rules.required]"
                            color="cyan-darken-4"
                            label="Title"
                            counter="50"
                        >
                        </v-text-field>
                        <v-textarea
                            v-model="plans.planModel.content"
                            clearable
                            clear-icon="mdi-close-circle"
                            auto-grow
                            counter="255"
                            :rules="[rules.length(255),rules.required]"
                            color="cyan-darken-4"
                            label="Content"
                            rows="3"
                        ></v-textarea>
                        <v-select
                            v-model="plans.planModel.kanbanList"
                            :items="allLists"
                            item-value="value"
                            item-title="text"
                            label="List"
                        ></v-select>
                        <div class="d-flex ga-2">
                            <v-select
                                v-model="plans.planModel.tags"
                                :items="tags.allTags"
                                item-value="tagName"
                                item-title="tagName"
                                :rules="[rules.count(3)]"
                                label="Tags"
                                multiple
                            >
                                <template v-slot:selection="{index}">
                                    <v-chip :color="tags.allTags[index].color">
                                        <span>{{ tags.allTags[index].tagName }}</span>
                                    </v-chip>
                                </template>
                            </v-select>
                            <div class="mt-3">
                                <v-btn
                                    color="cyan-darken-4"
                                    variant="tonal"                                    
                                    @click="addTagVisible = !addTagVisible">
                                    Add
                                </v-btn>
                                <v-overlay
                                    v-model="addTagVisible"
                                    class="justify-center align-center"
                                >
                                    <v-card class="pa-4">
                                        <v-form v-model="addTagValid">
                                            <div class="d-flex flex-column ga-5">     
                                                <v-color-picker
                                                    v-model="tags.tagModel.color"
                                                    :modes="['rgba']"
                                                    hide-canvas 
                                                    hide-inputs
                                                    show-swatches>
                                                </v-color-picker>
                                                <v-text-field
                                                    v-model="tags.tagModel.tagName"
                                                    :rules="[rules.length(15),rules.required]"
                                                    color="cyan-darken-4"
                                                    label="Tag"
                                                >
                                                </v-text-field>
                                                <v-btn
                                                    :disabled="!addTagValid"
                                                    variant="tonal"
                                                    color="cyan-darken-4"
                                                    @click="addTag()"
                                                >OK
                                                </v-btn>
                                            </div>
                                        </v-form>
                                    </v-card>
                                </v-overlay>
                            </div>
                        </div>
                    </div>            
                    <v-card-actions class="justify-end">
                        <v-btn
                            variant="text"
                            color="cyan-darken-4"
                            @click="addPlanVisible = false"
                        >Close
                        </v-btn>
                        <v-btn
                            :disabled="!addPlanValid"
                            variant="tonal"
                            color="cyan-darken-4"
                            @click="addPlan()"
                        >Ok
                        </v-btn>
                    </v-card-actions>
                </v-form>
            </v-card>
        </v-dialog>
    </div>  
</template>