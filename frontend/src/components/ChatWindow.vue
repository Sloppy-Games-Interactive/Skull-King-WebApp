<script setup lang="ts">
import { useChatStore } from '@/core/stores/chatStore'
import { computed, nextTick, ref, watch } from 'vue'
import { ChatBus, ChatEvent, ChatEventName } from '@/core/event-bus'
import { useLobbyStore } from '@/core/stores/lobbyStore'
import { useEventBus } from '@vueuse/core'
import { useGameStateStore } from '@/core/stores/gameState'

const gameState = useGameStateStore()
const lobby = useLobbyStore()
const chat = useChatStore()
const chatBus = useEventBus<ChatEvent>(ChatBus)

const message = ref('')
const chatMessagesContainer = ref<HTMLElement | null>(null)

async function scrollDown() {
  await nextTick()
  if (chatMessagesContainer.value) {
    chatMessagesContainer.value.scrollTop = chatMessagesContainer.value.scrollHeight
  }
}

scrollDown()

watch(
  () => chat.messages,
  scrollDown,
  { deep: true }
)

const sendMessage = () => {
  if (!lobby.me || message.value.trim().length === 0) {
    return
  }
  chatBus.emit(
    new ChatEvent(ChatEventName.Message, lobby.me, message.value.trim()),
  )

  message.value = ''
}

const authorColors = computed(() => {
  const colorMap = new Map<string, string>()

  gameState.players.forEach((player, index) => {
    colorMap.set(player.id, 'chat-author-' + (index + 1))
  })

  return colorMap
})

withDefaults(
  defineProps<{
    width?: number
    height?: number
    fill?: boolean
  }>(),
  {
    width: 400,
    height: 400,
    fill: false,
  },
)
</script>

<template>
  <div
    class="chat bg-black/10 p-3 h-full w-full text-start leading-snug"
    :class="fill ? '' : `max-w-[${width}px] max-h-[${height}px]`"
  >
    <div class="chat-messages-container overflow-y-auto" ref="chatMessagesContainer">
      <div class="chat-line" v-for="(entry, idx) of chat.messages" :key="idx">
        <span class="chat-author" :class="authorColors.get(entry.author.id)"
          >{{ entry.author.name }}:</span
        >
        <span class="chat-message text-white">{{ entry.message }}</span>
      </div>
    </div>
    <v-text-field
      class="chat-input text-white pt-1"
      label="Send Message"
      variant="underlined"
      density="compact"
      hide-details
      @keydown.enter.prevent="sendMessage"
      v-model="message"
    ></v-text-field>
  </div>
</template>

<style scoped lang="scss">
@use 'sass:list';

.chat {
  display: grid;

  font-size: 1.1rem;
  text-shadow: rgba(51, 51, 51, 0.5) 1px 1px 2px;

  grid-template-areas: 'messages' 'input';
  grid-template-rows: 1fr auto;
}

.chat-messages-container {
  grid-area: messages;
}

.chat-author {
  grid-area: author;
}

.chat-message {
  grid-area: message;
}

.chat-input {
  grid-area: input;
}

.chat-line {
  display: grid;
  grid-template-columns: auto 1fr;
  gap: 0.25rem;
  grid-template-areas: 'author message';
  justify-content: start;
}

$chat-color-array: #7249c0, #77cd5b, #cb54ad, #cebe50, #4a304f, #ce6836,
  #9598c4, #5a633c, #b4525a, #93c8ae;

@for $i from 1 through 10 {
  .chat-author-#{$i} {
    color: list.nth($chat-color-array, $i);
  }
}
</style>
