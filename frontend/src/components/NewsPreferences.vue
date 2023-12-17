<script setup>
    import { reactive } from 'vue';

    const dialog = reactive({
        isVisible: false,
        selectedCategories: [],
        allCategories: ['business','entertainment','health','science','sports','technology']
    });

    const openDialog = () => {
        dialog.isVisible = true;

        // TODO: We will fetch selectedCategories from database
    }

    const closeWithOk = () => {
        dialog.isVisible = false;

        // TODO: We will post selectedCategories to backend
    }
</script>
<template>
    <v-btn
        color="cyan-darken-4"
        variant="tonal"
        icon="mdi-cog"
        @click="openDialog">
    </v-btn>
    <v-dialog
        transition="dialog-bottom-transition"
        v-model="dialog.isVisible"
        width="auto"
    >
        <v-card
            width="400"
        >
            <v-toolbar
                color="cyan-darken-4"
                title="Choose Categories"
            ></v-toolbar>
            <div class="ma-4">
                <v-checkbox v-for="(category,index) in dialog.allCategories"
                v-model="dialog.selectedCategories"
                :label="category"
                :value="category"
                color="cyan-darken-4"
                :key="index"
                hide-details
                >
                </v-checkbox>
            </div>            
            <v-card-actions class="justify-end">
                <v-btn
                    variant="text"
                    color="cyan-darken-4"
                    @click="dialog.isVisible = false"
                >Close
                </v-btn>
                <v-btn
                    variant="tonal"
                    color="cyan-darken-4"
                    @click="closeWithOk"
                >Ok
                </v-btn>
            </v-card-actions>
        </v-card>
    </v-dialog>
</template>