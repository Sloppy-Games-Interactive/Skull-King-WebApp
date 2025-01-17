import { defineStore } from 'pinia'
import { reactive } from 'vue'
import { ChatMessage, type ChatMessageInterface } from '@/core/model/Chat'
import { useSessionStorage } from '@vueuse/core'

export const useChatStore = defineStore('chat', () => {
  const sessionStorage = useSessionStorage<ChatMessageInterface[]>('chat', [], {
    deep: true,
    serializer: {
      read: (v: string) =>
        JSON.parse(v).map(
          (message: ChatMessageInterface) => new ChatMessage(message),
        ),
      write: (v: ChatMessageInterface[]) => JSON.stringify(v),
    },
  })

  const messages = reactive<ChatMessage[]>(sessionStorage.value ?? [])

  const addMessage = (message: ChatMessageInterface) => {
    messages.push(message)
    sessionStorage.value = messages
  }

  const clear = () => {
    sessionStorage.value = []
    messages.splice(0, messages.length)
  }

  return {
    messages,
    addMessage,
    clear,
  }
})
