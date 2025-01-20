import { library } from '@fortawesome/fontawesome-svg-core'
import {
  faBars,
  faChevronLeft,
  faLink,
  faMessage,
  faQrcode,
  faUsers,
  faUser,
} from '@fortawesome/free-solid-svg-icons'
import { faGithub } from '@fortawesome/free-brands-svg-icons'

export function initFontAwesomeLibrary() {
  library.add(faUser)
  library.add(faBars)
  library.add(faUsers)
  library.add(faMessage)
  library.add(faLink)
  library.add(faQrcode)
  library.add(faChevronLeft)
  library.add(faGithub)
}
