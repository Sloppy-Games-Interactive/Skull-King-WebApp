import { useRoute } from 'vue-router'
import { inject, ref } from 'vue'
import { API_INJECTION_KEY, ApiService } from '@/core/rest/api'
import { User } from '@/core/model/User'
import { useUserStore } from '@/core/stores/userStore'

export function useAuth() {
  const api = inject(API_INJECTION_KEY) as ApiService

  const route = useRoute()
  const { query } = route

  const sessionUuid = ref<string | null>(null)
  const userStore = useUserStore()

  if (query.uuid) {
    sessionUuid.value = query.uuid as string
  }

  const getUser = async () => {
    if (!sessionUuid.value) {
      return
    }

    const user = await api.getUser(sessionUuid.value)
    userStore.setUser(new User(user))
  }
  getUser()
}
