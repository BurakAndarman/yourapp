<script setup>
    defineProps({
        cityForecast : Object,
        format : String,
        cityIndex : Number
    })
</script>
<template>
    <v-card class="text-white"
            :style="{ 
                backgroundImage : `url(https://res.cloudinary.com/df6xomy8c/image/upload/v1/YourApp/Weather/${cityForecast.isDayTime ? 'Day' : 'Night'}Blurred.png)`,
                backgroundSize : 'cover', 
                backgroundPosition: cityForecast.isDayTime ? 'center' : 'start' 
            }"
        >  
        <div class="pa-10">
            <div class="mb-2 d-flex align-center justify-center ga-2">
                <div class="text-h5 font-weight-bold">{{ `${cityForecast.location.name}, ${cityForecast.location.country}` }}</div>
                <v-icon v-if="cityIndex == 0" icon="mdi-map-marker"/>
            </div>
            <div class="d-flex justify-space-around align-center">
                <div class="d-flex align-center ga-10">
                    <v-img
                        width="192"
                        height="192"
                        :src="`https://res.cloudinary.com/df6xomy8c/image/upload/v1/YourApp/Weather/${cityForecast.isDayTime ? 'Day' : 'Night'}Icons/${cityForecast.current.condition.code}.png`"
                    />
                    <div>
                        <div class="text-h4">
                            {{ cityForecast.current.condition.text }}
                        </div>
                        <div class="mt-5 d-flex align-start ga-1">
                            <div class="text-h2">{{ cityForecast.current.temp_c }}</div>
                            <div class="text-h5">°C</div>
                        </div>  
                    </div>
                </div>
                <div class="d-flex ga-10">
                    <div v-for="(forecastDay, index) in cityForecast.forecast.forecastday"
                            class="d-flex flex-column align-center justify-center ga-3"
                            :key="index"
                    >
                        <div>{{ forecastDay.date.split('-').slice(1).reverse().join('/') }}</div>
                        <v-img
                            width="64"
                            height="64"
                            :src="`https://res.cloudinary.com/df6xomy8c/image/upload/v1/YourApp/Weather/DayIcons/${forecastDay.day.condition.code}.png`"
                        />
                        <div class="d-flex align-start ga-1 font-weight-bold">
                            <div>{{ forecastDay.day.avgtemp_c }}</div>
                            <div class="text-caption">°C</div>
                        </div>                        
                    </div>
                </div>
            </div>
            <div v-if="format === 'detailed'"
                    class="d-flex justify-space-evenly align-center my-10"
            >
                <div class="text-h5 d-flex flex-column align-center">
                    <div class="d-flex align-center ga-2">
                        <v-icon icon="mdi-cloud-percent-outline"/>
                        <div class="text-h6">Humidity</div>
                    </div>
                    <div>{{ cityForecast.current.humidity }}<span class="text-h6">%</span></div>
                </div>
                <div class="text-h5 d-flex flex-column align-center">
                    <div class="d-flex align-center ga-2">
                        <v-icon icon="mdi-sun-wireless-outline"/>
                        <div class="text-h6">UV</div>
                    </div>
                    <div>{{ cityForecast.current.uv }}</div>
                </div>
                <div class="text-h5 d-flex flex-column align-center">
                    <div class="d-flex align-center ga-2">
                        <v-icon icon="mdi-weather-windy"/>
                        <div class="text-h6">Wind</div>
                    </div>
                    <div>{{ cityForecast.current.wind_kph }} <span class="text-h6">Kph</span></div>
                </div>
                <div class="text-h5 d-flex flex-column align-center">
                    <div class="d-flex align-center ga-2">
                        <v-icon icon="mdi-compass-outline"/>
                        <div class="text-h6">Wind Dir.</div>
                    </div>
                    <div>{{ cityForecast.current.wind_dir }}</div>
                </div>
                <div class="text-h5 d-flex flex-column align-center">
                    <div class="d-flex align-center ga-2">
                        <v-icon icon="mdi-weather-sunset-up"/>
                        <div class="text-h6">Sunrise</div>
                    </div>
                    <div>{{ cityForecast.forecast.forecastday[0].astro.sunrise }}</div>
                </div>
                <div class="text-h5 d-flex flex-column align-center">
                    <div class="d-flex align-center ga-2">
                        <v-icon icon="mdi-weather-sunset-down"/>
                        <div class="text-h6">Sunset</div>
                    </div>
                    <div>{{ cityForecast.forecast.forecastday[0].astro.sunset }}</div>
                </div>
            </div>
            <div class="text-right font-italic mt-2">
                {{ `Last Updated : ${cityForecast.current.last_updated.split(' ')[1]} (In Local)` }}
            </div>
        </div>
    </v-card>
</template>