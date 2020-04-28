import Vue from 'vue'
import App from './App.vue'
import router from './router'
import { store } from "./store.js";
import {BootstrapVue,BootstrapVueIcons  } from 'bootstrap-vue'
import 'bootstrap/dist/css/bootstrap.min.css'
import 'bootstrap-vue/dist/bootstrap-vue.css'

Vue.use(BootstrapVue)
Vue.use(BootstrapVueIcons );
Vue.config.productionTip = false
Vue.prototype.$EventBus = new Vue();


new Vue({
  router,
  store: store,
  render: h => h(App)
}).$mount('#app')
