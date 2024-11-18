import { computed, ref } from 'vue'
import type { Ref } from 'vue'

export default function useResizeCard(availableWidth: Ref<number, number>, availableHeight: Ref<number, number>) {
  const targetWidth = 747;
  const targetHeight = 1122;

  let scaleRatio = ref(1)

  const style = computed(() => {
    scaleRatio.value = Math.min((availableWidth.value / targetWidth), availableHeight.value / targetHeight);

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
    scaleRatio
  }
}
