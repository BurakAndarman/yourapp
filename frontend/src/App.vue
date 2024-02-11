<script setup>
  import { useAuthStore } from '@/store/auth';
  import StatusDialog from '@/components/StatusDialog.vue';
  import SavingIndicator from './components/SavingIndicator.vue';

  const auth = useAuthStore()

  const logout = () => {
    auth.logout()
}
</script>
<template>
    <v-toolbar
      v-if="auth.user"
      color="cyan-darken-4">
      <v-toolbar-title class="pl-5">YourApp</v-toolbar-title>
      <v-spacer></v-spacer>
      <v-btn to="/" icon>
        <v-icon>mdi-newspaper-variant-multiple</v-icon>
      </v-btn>
      <v-btn to="/plans" icon>
        <v-icon>mdi-file-outline</v-icon>
      </v-btn>
      <v-btn to="/weather" icon>
        <v-icon>mdi-weather-cloudy</v-icon>
      </v-btn>
      <v-btn 
        class="font-weight-bold ml-8"
        prepend-icon="mdi-logout" 
        variant="tonal"
        @click="logout">
        Logout
      </v-btn>
    </v-toolbar>
    <StatusDialog/>
    <SavingIndicator/>
    <RouterView/>
</template>

