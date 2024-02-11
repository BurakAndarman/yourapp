<script setup>
    import { useSavingStore } from '../store/saving';

    const saving = useSavingStore();
</script>
<template>
    <v-overlay
        v-model="saving.saving"
        class="align-center justify-center"
    >
        <div class="d-flex flex-column justify-center align-center ga-3">
            <v-progress-circular
                color="white"
                indeterminate
                :size="64"
            ></v-progress-circular>
            <p class="text-white text-h6">Saving Changes</p>
        </div>
    </v-overlay>
</template>
