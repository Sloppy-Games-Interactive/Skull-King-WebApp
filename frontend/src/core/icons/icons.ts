import { library } from '@fortawesome/fontawesome-svg-core'


import { faUserSecret } from '@fortawesome/free-solid-svg-icons/faUserSecret'
import { faBars } from '@fortawesome/free-solid-svg-icons/faBars'
import { faUsers } from '@fortawesome/free-solid-svg-icons/faUsers'
import { faMessage } from '@fortawesome/free-solid-svg-icons/faMessage'
import { faLink } from '@fortawesome/free-solid-svg-icons/faLink'
// import { ICON_NAME } from '@fortawesome/free-regular-svg-icons'

export function initFontAwesomeLibrary() {
  library.add(faUserSecret)
  library.add(faBars)
  library.add(faUsers)
  library.add(faMessage)
  library.add(faLink)
  // library.add(ICON_NAME)
  // ...
}
