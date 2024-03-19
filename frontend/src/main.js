import { createApp } from 'vue'
import { createPinia } from 'pinia'

import '@mdi/font/css/materialdesignicons.css'
import 'vuetify/styles'
import App from './App.vue'
import router from './router'
import { createVuetify } from 'vuetify'
import colors from 'vuetify/util/colors'
import * as components from 'vuetify/components'
import * as directives from 'vuetify/directives'
import { aliases, mdi } from 'vuetify/iconsets/mdi'

const app = createApp(App)

const darkTheme = {
  dark: true,
  colors: {
    background: colors.grey.darken3,
    surface: colors.grey.darken4,
    primary: colors.teal.lighten3,
    secondary: colors.green.lighten3,
    error: colors.red.accent2,
    warning: colors.orange.accent2
  },
};

const lightTheme = {
  light : true,
  colors : {
    background : colors.grey.lighten4,
    surface : colors.grey.lighten5,
    primary : colors.teal.darken3,
    secondary : colors.green.darken3,
    error : colors.red.darken3,
    warning : colors.orange.darken3
  }
}

app.use(createPinia())
app.use(createVuetify({
    theme : {
      defaultTheme: "lightTheme",
      themes: {
        darkTheme,
        lightTheme
      }
    },
    components,
    directives,
    icons: {
        defaultSet: 'mdi',
        aliases,
        sets: {
          mdi,
        },
      },
}))
app.use(router)

app.mount('#app')