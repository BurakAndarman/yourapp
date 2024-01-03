<script setup>
    import ExpandedPlan from '../components/ExpandedPlan.vue';
    import { useExpandPlanStore } from '../store/expandPlan';

    const props = defineProps({
        title: String,
        plans: Object,
        planUtils: Object
    });
  
    const expandPlan = useExpandPlanStore();

</script>
<template>
    <div class="w-100">
        <div class="text-center text-cyan-darken-4">
            <h3 class="text-h5">{{props.title}}</h3>
        </div>
        <div v-if="plans.length">
            <v-card
                v-for="(plan) in plans"
                class="my-8 text-cyan-darken-4"
                :title="plan.title"
                :key="plan.id"
                @click="expandPlan.id === plan.id ? expandPlan.hide() : expandPlan.reveal(plan.id)"
                :ripple="false"
            >
                <template v-if="plan.kanbanList !== 'TODO'" v-slot:prepend>
                    <v-btn
                        color="cyan-darken-4"
                        variant="text"
                        icon="mdi-chevron-left"
                        @click.stop="props.planUtils.changeList(plan.id, (plan.kanbanList === 'DONE' ? 'TODAY' : plan.kanbanList === 'TODAY' ? 'THIS_WEEK' : 'TODO'))"
                        >
                    </v-btn>
                </template>
                <template v-if="plan.kanbanList !== 'DONE'" v-slot:append>
                    <v-btn
                        color="cyan-darken-4"
                        variant="text"
                        icon="mdi-chevron-right"
                        @click.stop="props.planUtils.changeList(plan.id, (plan.kanbanList === 'TODO' ? 'THIS_WEEK' : plan.kanbanList === 'THIS_WEEK' ? 'TODAY' : 'DONE'))">
                    </v-btn>
                </template>
                <ExpandedPlan :plan="plan"/>
            </v-card>
        </div>
        <div v-else class="pt-8 text-center text-disabled">
            {{ `${props.title} list is empty` }}
        </div>
    </div>
</template>