<script setup>
    import { ref, reactive, watch } from 'vue';

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

    // Props
    const props = defineProps({
        planModelProp: Object,
        allTagsProp: Object,
        selectedImageProp : Object,
        utils: Object,
        mode: String,
        isVisibleProp: Boolean,
        planFormFuncs: Object,
    });

    // Models
    const planModel = ref({})
    const tagModel = ref({
        name: '',
        color: '',
        created : true
    })

    // Extra
    const allTags = ref([])
    const selectedImage = ref([])
    const isVisible = ref(false)
    const isAddTagVisible = ref(false)        
    const validations = reactive({
        plan : false,
        addTag : false
    })

    // Watchers
    watch(() => props.planModelProp, (planModelProp) => {
        planModel.value = planModelProp
    })
    watch(() => props.allTagsProp, (allTagsProp) => {
        allTags.value = allTagsProp
    })
    watch(() => props.selectedImageProp, (selectedImageProp) => {
        selectedImage.value = selectedImageProp
    })
    watch(() => props.isVisibleProp, (isVisibleProp) => {
        isVisible.value = isVisibleProp
    })

    // Form Rules
    const rules = {
        length : len => v => (v || '').length <= len || `Invalid character length, required ${len} at max`,
        required: v => !!v || 'This field is required',
        count: len => v => v.length <= len || `You can select ${len} options at most`
    }

    // Functions
    const addPlan = () => {
        props.planFormFuncs.closePlanForm()

        planModel.value.id = props.utils.generatePlanId()
        
        if(selectedImage.value.length) {
           props.planFormFuncs.handleImageAssoc(selectedImage.value, planModel.value.id)
        }

        planModel.value.created = true

        props.utils.addNewPlan(planModel.value)
    }

    const changePlan = () => {
        props.planFormFuncs.closePlanForm()

        if(props.planFormFuncs.imageAssocExists(planModel.value.id) || selectedImage.value.length) {
            props.planFormFuncs.handleImageAssoc(selectedImage.value, planModel.value.id)
        
        }
        planModel.value.changed = true

        props.utils.changeExistingPlan(planModel.value)
    }

    const addTag = () => {
        isAddTagVisible.value = false

        let tagFound = false

        for (const tagObject of allTags.value) {

            if(tagObject.value.name === tagModel.value.name && tagObject.value.color === tagModel.value.color) {

                if(!planModel.value.tags.includes(tagObject.value)) {
                    planModel.value.tags.push(tagObject.value)
                    sortTags('planModelTags')

                }

                tagFound = true
                break;
            }
        }

        if(!tagFound) {

            allTags.value.push({
                title : tagModel.value.name,
                value : tagModel.value
            })
            sortTags('allTags')


            planModel.value.tags.push(tagModel.value)
            sortTags('planModelTags')

            // We don't sort these here because we want to keep them chronogically in local storage
            let lastAddedTags = localStorage.getItem('lastAddedTags')

            if(lastAddedTags) {
                lastAddedTags = JSON.parse(lastAddedTags)

                if(lastAddedTags.length === 5) {
                    lastAddedTags.pop()
                }

                lastAddedTags.unshift(tagModel.value)
                localStorage.setItem('lastAddedTags', JSON.stringify(lastAddedTags))

            } else {
                localStorage.setItem('lastAddedTags',JSON.stringify([tagModel.value]))

            }

        }

        tagModel.value = {
            name: '',
            color: '',
            created : true
        }
    }

    const sortTags = (tagsArrayName) => {

        if(tagsArrayName === 'allTags') {

            allTags.value.sort((a, b) => {
                if (a.title < b.title) {
                    return -1;
                }
                if (a.title > b.title) {
                    return 1;
                }
                return 0;
            });
        
        } else {

            planModel.value.tags.sort((a, b) => {
                if (a.name < b.name) {
                    return -1;
                }
                if (a.name > b.name) {
                    return 1;
                }
                return 0;
            });

        }
        
    }

    const extractUrl = () => {
       if(selectedImage.value.length) {
            planModel.value.image = URL.createObjectURL(selectedImage.value[0]);
       } else {
            planModel.value.image = "";
       }       
    }

    const deleteImage = () => {
        selectedImage.value = [];
        planModel.value.image = "";
    }
</script>
<template>
    <v-dialog
        transition="dialog-bottom-transition"
        v-model="isVisible"
        @click:outside="props.planFormFuncs.closePlanForm()"
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
                    :title="props.mode === 'add' ? 'Add Plan' : 'Change Plan'"
                ></v-toolbar>
                <div class="ma-4">
                    <v-text-field
                        v-model="planModel.title"
                        :rules="[rules.length(50),rules.required]"
                        color="cyan-darken-4"
                        label="Title"
                        counter="50"
                    >
                    </v-text-field>
                    <v-textarea
                        v-model="planModel.content"
                        clearable
                        clear-icon="mdi-close-circle"
                        auto-grow
                        counter="255"
                        :rules="[rules.length(255)]"
                        color="cyan-darken-4"
                        label="Content"
                        rows="3"
                    ></v-textarea>
                    <div class="d-flex ga-2">
                        <v-file-input
                            v-model="selectedImage"
                            @update:modelValue="extractUrl()"
                            accept="image/png, image/jpeg"                            
                            prepend-icon=""
                            label="Image"
                        ></v-file-input>
                        <v-hover v-slot="{ isHovering, props }">
                            <v-img  
                                v-if="planModel.image"
                                @click="deleteImage()"
                                max-height="55"
                                max-width="70"
                                :src="planModel.image"
                                style="cursor: pointer;"
                                v-bind="props"
                                cover
                            >
                                <v-overlay
                                    :model-value="isHovering"
                                    contained
                                    class="align-center justify-center"
                                >
                                    <v-icon icon="mdi-delete" color="white"></v-icon>
                                </v-overlay>
                            </v-img>
                        </v-hover>                        
                    </div>
                    <v-select
                        v-model="planModel.kanbanList"
                        :items="kanbanLists"
                        item-value="value"
                        item-title="title"
                        label="List"
                    ></v-select>
                    <div class="d-flex ga-2">
                        <v-select
                            v-model="planModel.tags"
                            :items="allTags"
                            item-value="value"
                            item-title="title"
                            :rules="[rules.count(3)]"
                            label="Tags"
                            @update:modelValue="sortTags('planModelTags')"
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
                        @click="props.planFormFuncs.closePlanForm()"
                    >Close
                    </v-btn>
                    <v-btn
                        :disabled="!validations.plan"
                        variant="tonal"
                        color="cyan-darken-4"
                        @click="props.mode === 'add' ? addPlan() : changePlan()"
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