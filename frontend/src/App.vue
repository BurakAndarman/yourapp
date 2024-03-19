<script setup>
  import { ref } from "vue";
  import { useAuthStore } from '@/store/auth';
  import StatusDialog from '@/components/StatusDialog.vue';
  import SavingIndicator from './components/SavingIndicator.vue';
  import { useTheme } from "vuetify";

  const auth = useAuthStore()

  const theme = useTheme();
  const darkMode = ref(false);

  const toggleTheme = () => {
    theme.global.name.value = darkMode.value ? "darkTheme" : "lightTheme";
  }

  const logout = () => {
    auth.logout()
  }
</script>
<template>
    <v-toolbar
      v-if="auth.user"
      color="primary">
      <v-toolbar-title class="pl-5">YourApp</v-toolbar-title>
      <v-spacer></v-spacer>
      <div class="mt-5 mr-5">
        <v-switch
            v-model="darkMode"
            color="background"
            @change="toggleTheme()"
            :label="`${darkMode ? 'Dark' : 'Light'}`"
        />
      </div>      
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

