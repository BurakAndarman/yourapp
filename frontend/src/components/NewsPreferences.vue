<script setup>
    import { reactive } from 'vue';
    import { useAuthStore } from '../store/auth';
    import { useStatusStore } from '../store/status';
    import { useSavingStore } from '../store/saving';

    const auth = useAuthStore()

    const statusDialog = useStatusStore()

    const saving = useSavingStore()

    const props = defineProps({
        fetchNewArticles: Function
    });

    const dialog = reactive({
        isVisible: false,
        selectedCategories: [],
        selectedLanguage: ''
    });

    const allCategories = ['business','entertainment','health','science','sports','technology'];
    const allLanguages = ['en','de','fr','it','ru','tr'];

    const openDialog = async () => {

        try{

            const response = await fetch('http://localhost:8090/api/v1/user/news/news-preferences',{
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
                dialog.selectedCategories = parsedResponse.interestedTopics;
                dialog.selectedLanguage = parsedResponse.language;

                dialog.isVisible = true;

            } else {

                throw new Error(parsedResponse.message)
        
            }

        } catch(e) {
            statusDialog.openStatusDialog(e,'error')
        }

    }

    const closeWithOk = async () => {
        
        try {

            dialog.isVisible = false;

            saving.showSavingIndicator()

            const response = await fetch('http://localhost:8090/api/v1/user/news/news-preferences',{
                method : "PUT",
                headers : {
                    "Content-Type" : "application/json",
                    "Authorization" : `Bearer ${auth.token}`
                },
                body : JSON.stringify({
                    "language" : dialog.selectedLanguage,
                    "interestedTopics" : dialog.selectedCategories
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

                await props.fetchNewArticles();
            
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
    <v-btn
        color="primary"
        variant="tonal"
        icon="mdi-cog"
        @click="openDialog()">
    </v-btn>
    <v-dialog
        transition="dialog-bottom-transition"
        v-model="dialog.isVisible"
        width="auto"
    >
        <v-card
            class="bg-surface"
            width="400"
        >
            <v-toolbar
                color="primary"
                title="News Preferences"
            ></v-toolbar>
            <div class="ma-4">
                <div>
                    <div class="text-center text-primary font-weight-bold my-1">Categories</div>
                    <v-checkbox v-for="(category,index) in allCategories"
                    v-model="dialog.selectedCategories"
                    :label="category"
                    :value="category"
                    color="primary"
                    :key="index"
                    hide-details
                    >
                    </v-checkbox>
                </div>
                <div>
                    <div class="text-center text-primary font-weight-bold mt-1 mb-5">Languages</div>
                    <v-select
                    v-model="dialog.selectedLanguage"
                    :items="allLanguages"
                    label="Language"
                    ></v-select>
                </div>
            </div>            
            <v-card-actions class="justify-end">
                <v-btn
                    variant="text"
                    color="primary"
                    @click="dialog.isVisible = false"
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