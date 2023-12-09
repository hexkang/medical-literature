import App from './App'

// main.js，注意要在use方法之后执行
import uView from './uni_modules/uview-ui/index.js'
Vue.use(uView)
// 如此配置即可
uni.$u.config.unit = 'rpx'

// #ifndef VUE3
import Vue from 'vue'
Vue.config.productionTip = false
App.mpType = 'app'
const app = new Vue({
    ...App
})
app.$mount()
// #endif

// #ifdef VUE3
import { createSSRApp } from 'vue'
export function createApp() {
  const app = createSSRApp(App)
  return {
    app
  }
}
// #endif