import { BACKEND_URL } from '@/core/utils/Constants.js'
module.exports = {
  // options...
  devServer: {
    proxy: BACKEND_URL,
  }
}
