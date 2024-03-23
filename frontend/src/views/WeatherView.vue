<script setup>
    import {ref} from 'vue'
    import WeatherPreferences from '../components/WeatherPreferences.vue';

    const weatherForecasts = ref([])
</script>
<template>
    <div class="bg-surface py-14">
        <div class="mx-auto" style="width:75%;">
            <div class="d-flex justify-space-between align-center">
                <div class="text-primary">
                    <h1 class="text-h2">Weather</h1>
                    <h2 class="text-h4 mt-3">Take a Look at Forecasts</h2>
                </div>
                <div>
                    <WeatherPreferences/>
                </div>
            </div>
            <div v-if="weatherForecasts.length">
            </div>
            <div v-else class="mt-10 h-screen">
                <div class="d-flex flex-column justify-center align-center ga-3">
                    <v-progress-circular color="primary" indeterminate :size="50"></v-progress-circular>
                    <p class="text-primary">Loading</p>
                </div>
            </div>
        </div>
    </div>
</template>