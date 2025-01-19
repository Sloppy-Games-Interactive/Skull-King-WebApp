import { defineStore } from 'pinia'
import { useSessionStorage } from '@vueuse/core'
import { ref } from 'vue'
import type { User } from '@/core/model/User'

export const useUserStore = defineStore('user', () => {

  const sessionStorage = useSessionStorage<User | null>('user', null, {
    deep: true,
    serializer: {
      read: (v: string) => JSON.parse(v),
      write: (v: User | null) => JSON.stringify(v),
    },
  })

  const user = ref<User | null>(sessionStorage.value ?? null)

  const setUser = (u: User) => {
    user.value = u
    sessionStorage.value = u
  }

  return {
    user,
    setUser
  }
})
