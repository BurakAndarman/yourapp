<script setup>
    import {ref, onMounted} from 'vue'
    import { useStatusStore } from '../store/status';
    import { useAuthStore } from '../store/auth';
    import WeatherPreferences from '../components/WeatherPreferences.vue';
    import WeatherCard from '../components/WeatherCard.vue';

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

                        afterFetchWeatherInfo()
                    
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

    const afterFetchWeatherInfo = () => {
        weatherInfo.value.forecastsList.forEach((cityForecast) => {
            cityForecast.forecast.forecastday[0].astro.sunrise = reformatTwelveHourToMilitary(cityForecast.forecast.forecastday[0].astro.sunrise)
            cityForecast.forecast.forecastday[0].astro.sunset = reformatTwelveHourToMilitary(cityForecast.forecast.forecastday[0].astro.sunset)

            cityForecast.isDayTime = isDayTime(cityForecast.location.localtime, cityForecast.forecast.forecastday[0].astro.sunrise, cityForecast.forecast.forecastday[0].astro.sunset)
        })
    }

    const reformatTwelveHourToMilitary = (twelveHour) => {
        const hourArray = twelveHour.split(':')
        const pm = hourArray[1].includes('PM')
        let hour = parseInt(hourArray[0])

        if (pm && hour < 12) {
            hour += 12
        
        } else if (!pm && hour == 12) {
            hour = 0
        }

        return `${hour.toString().padStart(2, '0')}:${hourArray[1].replace(/[AP]M/g, '')}`
    }

    const isDayTime = (localTime, sunrise, sunset) => {
        const time = new Date(localTime).toLocaleTimeString('en-US',{ hour12 : false })

        return time > sunrise && time < sunset
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
                    <WeatherPreferences :fetchNewWeatherInfo="fetchWeatherInfo"/>
                </div>
            </div>
            <div v-if="Object.keys(weatherInfo).length !== 0"
                 :class="`${weatherInfo.forecastsList.length === 1 || weatherInfo.look === 'slider' ? 'h-screen' : ''}`"
                 :style="{ marginTop : (weatherInfo.look === 'slider' && weatherInfo.format === 'simple') || (weatherInfo.look === 'cards' && weatherInfo.forecastsList.length === 1 && weatherInfo.format === 'simple') ? '8em' : '40px' }"
            >
                <div v-if="weatherInfo.look === 'cards'"
                     class="d-flex flex-column ga-10"
                >
                    <WeatherCard v-for="(cityForecast, index) in weatherInfo.forecastsList"
                                 :cityForecast="cityForecast"
                                 :format="weatherInfo.format"
                                 :cityIndex="index"
                                 :key="index"
                    />
                </div>
                <template v-else>
                    <v-carousel v-if="weatherInfo.forecastsList.length > 1"
                                height="auto"
                                hide-delimiters
                    >
                        <v-carousel-item v-for="(cityForecast, index) in weatherInfo.forecastsList"
                                         :key="index"
                        >
                            <WeatherCard :cityForecast="cityForecast"
                                         :format="weatherInfo.format"
                                         :cityIndex="index"
                            />
                        </v-carousel-item>
                    </v-carousel>
                    <WeatherCard v-else
                                 :cityForecast="weatherInfo.forecastsList[0]"
                                 :format="weatherInfo.format"
                                 :cityIndex="0"
                    />
                </template>
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
<style scoped>
    :deep(.v-carousel .v-btn) {
        background-color: white;
        color:black;
    }
</style>