import Vue from 'vue'
import Index from './Index.vue'
import Router from 'vue-router'
import router from './config/router';


Vue.config.productionTip = false
Vue.use(Router)

new Vue({
  router,
  render: h => h(Index),
}).$mount('#app')
