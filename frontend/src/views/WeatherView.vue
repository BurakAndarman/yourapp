<script setup>
    import {ref, onMounted} from 'vue'
    import { useStatusStore } from '../store/status';
    import { useAuthStore } from '../store/auth';
    import WeatherPreferences from '../components/WeatherPreferences.vue';

    const auth = useAuthStore()
    const statusDialog = useStatusStore()
    const weatherInfo = ref({})

    onMounted(() => {

        fetchWeatherInfo()

    })

    const fetchWeatherInfo = () => {

        if(navigator.geolocation) {
            navigator.geolocation.getCurrentPosition(
                async (pos) => {
                    const latitude = pos.coords.latitude
                    const longitude = pos.coords.longitude

                    const response = await fetch(`http://localhost:8090/api/v1/user/weather?latitude=${latitude}&longitude=${longitude}`,{
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
                        weatherInfo.value = parsedResponse;
                    
                    } else {
                        statusDialog.openStatusDialog(parsedResponse.message,'error')
                    }
                },
                (err) => {
                    let error = 'There has been an unknown error.'

                    if(err.code === err.PERMISSION_DENIED) {
                        error = 'Please give permission for your location.'
                    } else if(err.code === err.POSITION_UNAVAILABLE) {
                        error = 'Location information is unavailable.'
                    } else if(err.code === err.TIMEOUT) {
                        error = 'The request to get location timed out.'
                    }

                    statusDialog.openStatusDialog(error,'error')
                },
                {
                    enableHighAccuracy: true,
                    maximumAge: 0,
                }
            )
        } else {
            statusDialog.openStatusDialog('Location information cannot be reached in this browser.','error')
        }

    }


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
            <div v-if="Object.keys(weatherInfo).length !== 0">
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