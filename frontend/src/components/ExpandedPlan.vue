<script setup>
    import { useExpandPlanStore } from '../store/expandPlan';

    const props = defineProps({
        plan:Object
    });

    const expandPlan = useExpandPlanStore();
</script>
<template>
    <v-expand-transition>
        <v-card
            v-show="expandPlan.id == props.plan.id"
            class="py-2"
        >
            <v-card-subtitle class="tags-container" v-if="props.plan.tags.length">
                <v-chip
                    v-for="(tag,index) in props.plan.tags"
                    color="primary"
                    label
                    :key="index"
                >
                    {{ tag }}
                </v-chip>
            </v-card-subtitle>
            <v-card-text>
                {{ props.plan.content }}
            </v-card-text>
            <v-card-actions>
                <v-btn
                icon="mdi-pencil"
                variant="text"
                ></v-btn>
            </v-card-actions>
        </v-card>
    </v-expand-transition>
</template>
<style scoped>
.tags-container > * + *{
    margin-left:8px;
}
</style>