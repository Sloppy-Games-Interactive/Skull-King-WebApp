import { createRouter, createWebHistory } from 'vue-router'
import MainMenuView from '@/views/MainMenuView.vue'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      name: 'home',
      meta: {
        bg: 'bg-town',
      },
      component: MainMenuView,
    },
    {//TODO maybe use sub-routes instead
      path: '/new-game',
      name: 'new-game',
      meta: {
        bg: 'bg-pre-game stage-1 stage-1_2',
      },
      // route level code-splitting
      // this generates a separate chunk (About.[hash].js) for this route
      // which is lazy-loaded when the route is visited.
      component: () => import('@/views/pre-game/NewGameView.vue'),
    },
    {
      path: '/join-game',
      name: 'join-game',
      meta: {
        bg: 'bg-pre-game stage-2 stage-2_2',
      },
      component: () => import('@/views/pre-game/JoinGameView.vue'),
    },
    {
      path: '/game-lobby',
      name: 'game-lobby',
      meta: {
        bg: 'bg-pre-game stage-3 stage-3_3',
      },
      component: () => import('@/views/pre-game/GameLobbyView.vue'),
    },
    {
      path: '/play',
      name: 'play',
      meta: {
        bg: 'bg-tavern',
      },
      component: () => import('@/views/GameView.vue'),
    },
    {
      path: '/settings',
      name: 'settings',
      meta: {
        bg: 'bg-settings',
      },
      component: () => import('../../views/SettingsView.vue'),
    },
    {
      path: '/test',
      name: 'test',
      meta: {
        bg: 'bg-test',
      },
      component: () => import('@/views/TestView.vue'),
    }
  ],
})

export default router
