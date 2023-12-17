import { createRouter, createWebHistory } from 'vue-router'
import EnterView from '../views/EnterView.vue'
import HomeView from '../views/HomeView.vue'
import PlansView from '../views/PlansView.vue'
import WeatherView from '../views/WeatherView.vue'
import { useAuthStore } from '../store/auth'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      name: 'home',
      component: HomeView
    },
    {
      path: '/plans',
      name: 'plans',
      component: PlansView
    },
    {
      path: '/weather',
      name: 'weather',
      component: WeatherView
    },
    {
      path: '/enter',
      name: 'enter',
      component: EnterView
    }
  ]
})

router.beforeEach(async (to) => {
  const publicpages = ['/enter']
  const authRequired = !publicpages.includes(to.path)
  const auth = useAuthStore()

  if(authRequired && !auth.user){
    return '/enter'
  }
})

export default router
