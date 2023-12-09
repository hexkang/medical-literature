import Vue from 'vue'
import App from './App.vue'
import 'element-ui/lib/theme-chalk/index.css'
import element from './element/index'
import router  from '@/router'
import Login from '@/components/Login'
import Register from '@/components/Register'
import  findPass  from '@/components/findPass'
import thesis from '@/components/thesis'
import {fileDownload} from './utils'
// 引入仓库
import store from './store'
import '@/assets/font/font.css'
// import '@/mock/mockServer'
Vue.prototype.fileDownload = fileDownload


Vue.component(Login.name,Login)
Vue.component(Register.name,Register)
Vue.component(thesis.name,thesis)
Vue.component(findPass.name,findPass)
Vue.use(element)


Vue.config.productionTip = false

new Vue({
  render: h => h(App),
  router,
  beforeCreate(){
    Vue.prototype.$bus = this;
  },
  store
}).$mount('#app')
