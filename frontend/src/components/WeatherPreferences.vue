<script setup>
    import { reactive } from 'vue';
    import { useAuthStore } from '../store/auth';
    import { useStatusStore } from '../store/status';
    import { useSavingStore } from '../store/saving';

    // Constants
    const formatList = [
        {
            title : 'Simple',
            value : 'simple'
        },{
            title : 'Detailed',
            value : 'detailed'
        }
    ];
    const lookList = [
        {
            title : 'Cards',
            value : 'cards'
        },{
            title : 'Slider',
            value : 'slider'
        }
    ]

    // Stores
    const auth = useAuthStore()
    const statusDialog = useStatusStore()
    const saving = useSavingStore()

    // States
    const weatherDialog = reactive({
        isVisible : false,
        form : {
            cities : [],
            format : 'simple',
            look : 'cards'
        }
    })
    const autocomplete = reactive({
        city : null,
        search : '',
        suggestions : [],
        loading : false
    })

    // Functions
    const filterCities = async () => {
        
        try{

            if(!autocomplete.search) {
                autocomplete.suggestions = []
                return
            }

            autocomplete.loading = true

            const response = await fetch(`http://localhost:8090/api/v1/user/weather/cities?query=${autocomplete.search}`,{
                method : "GET",
                headers : {
                    "Authorization" : `Bearer ${auth.token}`
                }
            });

            if(response.status == 401) {
                autocomplete.loading = false
                auth.logout();
            }
            
            const parsedResponse = await response.json();            

            if(response.status == 200) {
                autocomplete.loading = false
                autocomplete.suggestions = parsedResponse

            } else {
                throw new Error(parsedResponse.message)
        
            }

        } catch(e) {
            autocomplete.loading = false
            statusDialog.openStatusDialog(e,'error')
        }

    }

    const handleSelectedCityChange = () => {
        if(typeof autocomplete.city === 'object' && autocomplete.city !== null && !weatherDialog.form.cities.find(city => city.cityId === autocomplete.city.id)) {
            weatherDialog.form.cities.push({
                cityId : autocomplete.city.id,
                name : autocomplete.city.name,
                orderNo : weatherDialog.form.cities.length
            })
        }
    }

    const deleteCity = (index) => {
        weatherDialog.form.cities.splice(index,1)
    }

    const changeOrder = (index, otherIndex) => {
        [weatherDialog.form.cities[otherIndex], weatherDialog.form.cities[index]] = [weatherDialog.form.cities[index], weatherDialog.form.cities[otherIndex]]
    }

    const openWeatherDialog = async () => {

        try{

            const response = await fetch('http://localhost:8090/api/v1/user/weather/weather-preferences',{
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
                weatherDialog.form.format = parsedResponse.format
                weatherDialog.form.look = parsedResponse.look
                weatherDialog.form.cities = parsedResponse.cities.sort((a,b) => a.orderNo - b.orderNo)

                weatherDialog.isVisible = true

            } else {

                throw new Error(parsedResponse.message)

            }

        } catch(e) {
            statusDialog.openStatusDialog(e,'error')
        }
    }

    const closeWithOk = async () => {

        try {

            weatherDialog.form.cities.forEach((city, index) => {
                city.orderNo = index
            })

            weatherDialog.isVisible = false;

            saving.showSavingIndicator()

            const response = await fetch('http://localhost:8090/api/v1/user/weather/weather-preferences',{
                method : "PUT",
                headers : {
                    "Content-Type" : "application/json",
                    "Authorization" : `Bearer ${auth.token}`
                },
                body : JSON.stringify({
                    "format" : weatherDialog.form.format,
                    "look" : weatherDialog.form.look,
                    "cities" : weatherDialog.form.cities
                })
            })

            if(response.status == 401) {
                saving.closeSavingIndicator()

                auth.logout();
            }           

            if(response.status == 200) {
                const successResponse = await response.text()

                saving.closeSavingIndicator()

                statusDialog.openStatusDialog(successResponse,'success')

            } else {
                const errorResponse = await response.json()

                throw new Error(errorResponse.message)
            }

        } catch(e) {
            saving.closeSavingIndicator()

            statusDialog.openStatusDialog(e,'error')

        }
    }
</script>
<template>
    <v-btn color="primary"
           variant="tonal"
           icon="mdi-cog"
           @click="openWeatherDialog()"
    >
    </v-btn>
    <v-dialog transition="dialog-bottom-transition"
              v-model="weatherDialog.isVisible"
              width="auto"
    >
        <v-card class="bg-surface"
                width="400"
        >
            <v-toolbar color="primary"
                       title="Weather Preferences"
            />
            <div class="ma-4">
                <v-autocomplete
                    v-model="autocomplete.city"
                    v-model:search="autocomplete.search"
                    @update:search="filterCities()"
                    @update:modelValue="handleSelectedCityChange()"
                    :loading="autocomplete.loading"
                    :items="autocomplete.suggestions"
                    :disabled="weatherDialog.form.cities.length === 4"
                    hide-no-data
                    no-filter
                    clearable
                    return-object
                    item-title="name"
                    label="Cities"
                >
                    <template v-slot:item="{ props, item }">
                        <v-list-item
                            v-bind="props"
                            :title="item.raw.name"
                            :subtitle="`${item.raw.region}, ${item.raw.country}`"
                        />
                    </template>
                </v-autocomplete>
                <div class="mt-1 mb-8">
                    <div class="text-center text-primary font-weight-bold mb-2">Cities</div>
                    <div class="d-flex flex-column ga-2">
                        <v-card v-for="(city,index) in weatherDialog.form.cities"
                            class="bg-background"
                            :key="index"
                        >
                            <v-card-item>
                                <div class="d-flex align-center justify-space-between">
                                    <div class="w-50">
                                        {{ `${index + 1}. ${city.name}` }}
                                    </div>
                                    <div v-if="index != 0" class="d-flex align-center ga-2">
                                        <v-btn
                                            color="error"
                                            variant="text"
                                            icon="mdi-delete"
                                            @click="deleteCity(index)"
                                        />
                                        <div class="d-flex flex-column">
                                            <v-icon v-if="index !== 1"
                                                    icon="mdi-chevron-up"
                                                    color="primary"
                                                    @click="changeOrder(index, index - 1)"
                                            />
                                            <v-icon v-if="index !== weatherDialog.form.cities.length - 1"
                                                    icon="mdi-chevron-down"
                                                    color="primary"
                                                    @click="changeOrder(index, index + 1)"
                                            />
                                        </div>
                                    </div>
                                </div>
                            </v-card-item>
                        </v-card>
                    </div>                    
                </div>
                <v-select
                    v-model="weatherDialog.form.format"
                    :items="formatList"
                    item-value="value"
                    item-title="title"
                    label="Format"
                />
                <v-select
                    v-model="weatherDialog.form.look"
                    :items="lookList"
                    item-value="value"
                    item-title="title"
                    label="Look"
                />
            </div>
            <v-card-actions class="justify-end">
                <v-btn
                    variant="text"
                    color="primary"
                    @click="weatherDialog.isVisible = false"
                >Close
                </v-btn>
                <v-btn
                    variant="tonal"
                    color="primary"
                    @click="closeWithOk()"
                >Ok
                </v-btn>
            </v-card-actions>
        </v-card>
    </v-dialog>
</template>