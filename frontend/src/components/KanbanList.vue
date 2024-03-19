<script setup>

    // Props
    const props = defineProps({
        title: String,
        plans: Object,
        plansUtils: Object
    });

</script>
<template>
    <div class="w-100">
        <div class="text-center text-primary">
            <h3 class="text-h5">{{props.title}}</h3>
        </div>
        <div v-if="plans.length"
             style="height: 63vh; scrollbar-width: thin;scrollbar-color: var(--v-theme-surface) var(--v-theme-background);"
             class="overflow-y-auto pa-1 mt-8"
        >
            <v-card
                v-for="(plan) in plans"
                class="mb-8 text-primary bg-background"
                :key="plan.id"
                @click="props.plansUtils.currentExpandedPlan() === plan.id ? props.plansUtils.hidePlan() : props.plansUtils.expandPlan(plan.id)"
                :ripple="false"
            >
                <v-card-item>
                    <div class="d-flex justify-space-between align-center">
                        <div class="w-60">
                            <h4>{{ plan.title }}</h4>
                            <div v-if="plan.tags.length" class="mt-5 w-100 d-flex flex-wrap ga-2">
                                <v-chip
                                    v-for="(tag,index) in plan.tags"
                                    :color="tag.color"
                                    label
                                    :key="index"
                                >
                                    {{ tag.name }}
                                </v-chip>
                            </div>
                        </div>                        
                        <div class="d-flex">
                            <v-btn
                                v-if="plan.kanbanList !== 'TODO'"
                                color="primary"
                                variant="text"
                                icon="mdi-chevron-left"
                                @click.stop="props.plansUtils.changeList(plan.id, (plan.kanbanList === 'DONE' ? 'TODAY' : plan.kanbanList === 'TODAY' ? 'THIS_WEEK' : 'TODO'))">
                            </v-btn>
                            <v-btn
                                v-if="plan.kanbanList !== 'DONE'"
                                color="primary"
                                variant="text"
                                icon="mdi-chevron-right"
                                @click.stop="props.plansUtils.changeList(plan.id, (plan.kanbanList === 'TODO' ? 'THIS_WEEK' : plan.kanbanList === 'THIS_WEEK' ? 'TODAY' : 'DONE'))">
                            </v-btn>
                        </div>
                    </div>
                </v-card-item>
                <v-expand-transition>
                    <v-card
                        class="bg-background"
                        v-show="props.plansUtils.currentExpandedPlan() === plan.id"
                    >
                        <v-card-item>
                            <div :class="'d-flex align-start '+((plan.content || plan.image) ? 'justify-space-between' : 'justify-end')">
                                <div v-if="plan.content || plan.image" class="w-60 d-flex flex-column ga-6">
                                    <div v-if="plan.content">
                                        {{ plan.content }}
                                    </div>
                                    <v-img
                                        v-if="plan.image"
                                        class="w-50"
                                        max-height="80"
                                        :src="plan.image"
                                        @click.stop="props.plansUtils.viewImage(plan.image)"
                                        cover
                                    ></v-img>
                                </div>
                                <div>
                                    <div class="d-flex">
                                        <v-btn
                                            color="error"
                                            variant="text"
                                            icon="mdi-delete"
                                            @click.stop="props.plansUtils.deletePlan(plan.id)"
                                            >
                                        </v-btn>
                                        <v-btn
                                            color="warning"
                                            variant="text"
                                            icon="mdi-pencil"
                                            @click.stop="props.plansUtils.openChangePlanForm(plan.id)"
                                            >
                                        </v-btn>
                                    </div>
                                </div> 
                            </div>                           
                        </v-card-item>
                    </v-card>
                </v-expand-transition>
            </v-card>
        </div>
        <div v-else class="mt-8 text-center text-disabled">
            {{ `${props.title} list is empty` }}
        </div>
    </div>
</template>
<style scoped>
.tags-container > * + *{
    margin-left:8px;
}
.w-60 {
    width: 60%;
}
</style>