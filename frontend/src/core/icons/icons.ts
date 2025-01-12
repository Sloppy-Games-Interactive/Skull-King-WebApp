import { library } from '@fortawesome/fontawesome-svg-core'

import { faUserSecret, faBars } from '@fortawesome/free-solid-svg-icons'
// import { ICON_NAME } from '@fortawesome/free-regular-svg-icons'

export function initFontAwesomeLibrary() {
  library.add(faUserSecret)
  library.add(faBars)
  // library.add(ICON_NAME)
  // ...
}
