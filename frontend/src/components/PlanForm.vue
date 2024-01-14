<script setup>
    import { ref, reactive } from 'vue';
    import { usePlanFormStore } from '../store/planform';

    // Store
    const planForm = usePlanFormStore();

    // Constants
    const kanbanLists = [
        {
            title : 'Todo',
            value : 'TODO'
        },{
            title : 'This Week',
            value : 'THIS_WEEK'
        },{
            title : 'Today',
            value : 'TODAY'
        },{
            title : 'Done',
            value : 'DONE'
        }
    ];

    // Models
    let tagModel = reactive({
        name: '',
        color: '',
        created : true
    })

    // Extra
    const isAddTagVisible = ref(false)

    const validations = reactive({
        plan : false,
        addTag : false
    })

    // Form Rules
    const rules = {
        length : len => v => (v || '').length <= len || `Invalid character length, required ${len} at max`,
        required: v => !!v || 'This field is required',
        count: len => v => v.length <= len || `You can select ${len} options at most`
    }

    // Functions
    const addPlan = () => {
        planForm.setVisibility(false)

        planForm.planModel.id = planForm.planFormUtils.generatePlanId()
        planForm.planModel.created = true

        planForm.planFormUtils.addNewPlan(planForm.planModel)
    }

    const changePlan = () => {
        planForm.setVisibility(false)
        
        planForm.planModel.changed = true

        planForm.planFormUtils.changeExistingPlan(planForm.planModel)
    }

    const addTag = () => {
        isAddTagVisible.value = false

        planForm.allTags.push({
            title : tagModel.name,
            value : tagModel
        })
        planForm.planModel.tags.push(tagModel)
        tagModel = {
            name: '',
            color: '',
            created : true
        }
    }

</script>
<template>
    <v-dialog
        transition="dialog-bottom-transition"
        v-model="planForm.isVisible"
        width="auto"
    >
        <v-card
            width="400"
        >
            <v-form
                v-model="validations.plan"
            >
                <v-toolbar
                    color="cyan-darken-4"
                    :title="planForm.mode === 'add' ? 'Add Plan' : 'Change Plan'"
                ></v-toolbar>
                <div class="ma-4">
                    <v-text-field
                        v-model="planForm.planModel.title"
                        :rules="[rules.length(50),rules.required]"
                        color="cyan-darken-4"
                        label="Title"
                        counter="50"
                    >
                    </v-text-field>
                    <v-textarea
                        v-model="planForm.planModel.content"
                        clearable
                        clear-icon="mdi-close-circle"
                        auto-grow
                        counter="255"
                        :rules="[rules.length(255)]"
                        color="cyan-darken-4"
                        label="Content"
                        rows="3"
                    ></v-textarea>
                    <v-select
                        v-model="planForm.planModel.kanbanList"
                        :items="kanbanLists"
                        item-value="value"
                        item-title="title"
                        label="List"
                    ></v-select>
                    <div class="d-flex ga-2">
                        <v-select
                            v-model="planForm.planModel.tags"
                            :items="planForm.allTags"
                            item-value="value"
                            item-title="title"
                            :rules="[rules.count(3)]"
                            label="Tags"
                            multiple
                        >
                            <template v-slot:selection="{item}">
                                <v-chip :color="item.value.color">
                                    <span>{{ item.value.name }}</span>
                                </v-chip>
                            </template>                                
                        </v-select>
                        <div class="mt-3">
                            <v-btn
                                color="cyan-darken-4"
                                variant="tonal"                                    
                                @click="isAddTagVisible = true">
                                Add
                            </v-btn>
                        </div>
                    </div>
                </div>            
                <v-card-actions class="justify-end">
                    <v-btn
                        variant="text"
                        color="cyan-darken-4"
                        @click="planForm.setVisibility(false)"
                    >Close
                    </v-btn>
                    <v-btn
                        :disabled="!validations.plan"
                        variant="tonal"
                        color="cyan-darken-4"
                        @click="planForm.mode === 'add' ? addPlan() : changePlan()"
                    >Ok
                    </v-btn>
                </v-card-actions>
            </v-form>
        </v-card>
    </v-dialog>
    <v-overlay
        v-model="isAddTagVisible"
        class="justify-center align-center"
    >
        <v-card class="pa-4">
            <v-form v-model="validations.addTag">
                <div class="d-flex flex-column ga-5">     
                    <v-color-picker
                        v-model="tagModel.color"
                        :modes="['rgba']"
                        hide-canvas 
                        hide-inputs
                        show-swatches>
                    </v-color-picker>
                    <v-text-field
                        v-model="tagModel.name"
                        :rules="[rules.length(15),rules.required]"
                        color="cyan-darken-4"
                        label="Tag"
                    >
                    </v-text-field>
                    <v-btn
                        :disabled="!validations.addTag"
                        variant="tonal"
                        color="cyan-darken-4"
                        @click="addTag()"
                    >OK
                    </v-btn>
                </div>
            </v-form>
        </v-card>
    </v-overlay>
</template>