<script setup>
    import { reactive,onMounted } from 'vue';
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
    onMounted(() => {
        // TODO: Write here a fetch for bringing user weather preferences
    })

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
        if(typeof autocomplete.city === 'object' && autocomplete.city !== null) {
            // TODO : Check if the city is in the cities list before pushing
            weatherDialog.form.cities.push(autocomplete.city)
        }
    }

    const deleteCity = (index) => {
        weatherDialog.form.cities.splice(index,1)
    }

    const changeOrder = (index, otherIndex) => {
        [weatherDialog.form.cities[otherIndex], weatherDialog.form.cities[index]] = [weatherDialog.form.cities[index], weatherDialog.form.cities[otherIndex]]
    }

    const openWeatherDialog = () => {
        weatherDialog.isVisible = true
    }

    const closeWithOk = () => {

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
                <div class="mt-1 mb-5">
                    <div class="text-center text-primary font-weight-bold mb-2">Selected Cities</div>
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
                                    <div class="d-flex align-center ga-2">
                                        <v-btn
                                            color="error"
                                            variant="text"
                                            icon="mdi-delete"
                                            @click="deleteCity(index)"
                                        />
                                        <div class="d-flex flex-column">
                                            <v-icon v-if="index !== 0"
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