<script setup>
  import { ref, watch } from "vue";
  import { useAuthStore } from '@/store/auth';
  import StatusDialog from '@/components/StatusDialog.vue';
  import SavingIndicator from './components/SavingIndicator.vue';
  import { useTheme } from "vuetify";

  const auth = useAuthStore()

  const theme = useTheme();
  const darkMode = ref(false);

  watch(() => auth.user , (newUserValue) => {
    if(newUserValue) {
      let modePreferences = localStorage.getItem('modePreferences')

      if(modePreferences) {

        modePreferences = JSON.parse(modePreferences)
        const userModePreference = modePreferences.find(modePreference => modePreference.userName === newUserValue)

        if(userModePreference && theme.global.name.value !== userModePreference.mode) {
            theme.global.name.value = userModePreference.mode
            darkMode.value = userModePreference.mode === 'darkTheme'
        }

        if(!userModePreference) {
          if(modePreferences.length === 5) {
            modePreferences.pop()
          }

          modePreferences.unshift({
              userName : newUserValue,
              mode : 'lightTheme'
          })

          localStorage.setItem('modePreferences', JSON.stringify(modePreferences))

          theme.global.name.value = 'lightTheme'
          darkMode.value = false

        }

      } else {

        localStorage.setItem('modePreferences', JSON.stringify([{
            userName : newUserValue,
            mode : 'lightTheme'
        }]))

      }
    }
  })

  const toggleTheme = () => {
    const newTheme = darkMode.value ? "darkTheme" : "lightTheme"

    const modePreferences = JSON.parse(localStorage.getItem('modePreferences'))
    const index = modePreferences.findIndex(modePreference => modePreference.userName === auth.user)
    modePreferences[index].mode = newTheme
    localStorage.setItem('modePreferences', JSON.stringify(modePreferences))

    theme.global.name.value = newTheme
  }

  const logout = () => {
    theme.global.name.value = "lightTheme"
    darkMode.value = false
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

