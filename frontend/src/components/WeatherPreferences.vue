<script setup>
    import { reactive, ref } from 'vue';
    import { useAuthStore } from '../store/auth';
    import { useStatusStore } from '../store/status';
    import { useSavingStore } from '../store/saving';

    const auth = useAuthStore()
    const statusDialog = useStatusStore()
    const saving = useSavingStore()

    const weatherDialog = reactive({
        isVisible : false
    })
    const citySuggestions = ref([])

    const filterCities = () => {

    }

    const openWeatherDialog = () => {
        weatherDialog.isVisible = true
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
                    :customFilter="filterCities()"
                    :items="citySuggestions"
                    item-title="name"
                    item-value="id"
                    label="Cities"
                ></v-autocomplete>
            </div>
        </v-card>
    </v-dialog>
</template>