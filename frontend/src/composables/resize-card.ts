import { computed, type Ref } from 'vue'
import type { ComputedRef } from 'vue'

type ResizeCardStyle = {
  '--textured_background-size': string
  '--card_border-radius': string
  '--card_padding': string
  '--card_max-width': string
  '--card_max-height': string
  '--values_font-size': string
  '--values_x-outset': string
  '--values_y-outset': string
  '--value_border-width': string
  '--value_width': string
  '--value_height': string
  '--value_box-shadow-size': string
  '--value_text-shadow-size': string
  '--value_text-shadow-offset': string
  '--inner_border-radius': string
  '--text_padding-top': string
  '--text_padding-bottom': string
  '--text_font-size': string
  '--image_box-shadow-size': string
}

export default function useResizeCard(
  availableWidth: Ref<number, number>,
  availableHeight: Ref<number, number>,
) {
  const targetWidth = 747
  const targetHeight = 1122

  let scaleRatio = computed(() =>
    Math.min(
      availableWidth.value / targetWidth,
      availableHeight.value / targetHeight,
    ),
  )

  let scaledWidth = computed(() => targetWidth * scaleRatio.value)
  let scaledHeight = computed(() => targetHeight * scaleRatio.value)

  const style = computed<ResizeCardStyle>(() => {
    return {
      '--textured_background-size': `${182 * scaleRatio.value}px`,
      '--card_border-radius': `${30 * scaleRatio.value}px`,
      '--card_padding': `${30 * scaleRatio.value}px`,
      '--card_max-width': `${747 * scaleRatio.value}px`,
      '--card_max-height': `${1122 * scaleRatio.value}px`,
      '--values_font-size': `${144 * scaleRatio.value}px`,
      '--values_x-outset': `${-38 * scaleRatio.value}px`,
      '--values_y-outset': `${-18 * scaleRatio.value}px`,
      '--value_border-width': `${20 * scaleRatio.value}px`,
      '--value_width': `${200 * scaleRatio.value}px`,
      '--value_height': `${200 * scaleRatio.value}px`,
      '--value_box-shadow-size': `${10 * scaleRatio.value}px`,
      '--value_text-shadow-size': `${5 * scaleRatio.value}px`,
      '--value_text-shadow-offset': `${1 * scaleRatio.value}px`,
      '--inner_border-radius': `${20 * scaleRatio.value}px`,
      '--text_padding-top': `${32 * scaleRatio.value}px`,
      '--text_padding-bottom': `${32 * scaleRatio.value}px`,
      '--text_font-size': `${112 * scaleRatio.value}px`,
      '--image_box-shadow-size': `${20 * scaleRatio.value}px`,
    }
  })

  return {
    style,
    scaleRatio,
    scaledHeight,
    scaledWidth,
  }
}
