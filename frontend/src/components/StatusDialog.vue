<script setup>
    import { useStatusStore } from '../store/status';

    const statusDialog = useStatusStore();
</script>
<template>
    <v-dialog 
        width="500"
        v-model="statusDialog.isVisible">
        <v-card class="bg-surface"
                :title="statusDialog.status === 'error' ? 'Error' : 'Success'"
        >
            <v-card-text> 
                {{ statusDialog.message }}
            </v-card-text>
            <v-card-actions>
                <v-spacer></v-spacer>
                <v-btn
                    text="Close"
                    @click="statusDialog.closeStatusDialog"
                ></v-btn>
            </v-card-actions>
        </v-card>
    </v-dialog>
</template>