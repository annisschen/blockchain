import Vue from 'vue'
import Router from 'vue-router'
import Home from './views/Home.vue'
import Register from './views/Register.vue'
import BaseBank from './views/BaseBank.vue'
import BaseEnterprise from './views/BaseEnterprise.vue'
import BaseInsuranceCompany from './views/BaseInsuranceCompany.vue'
import BaseTransportationCompany from './views/BaseTransportationCompany.vue'

Vue.use(Router)

const router = new Router({
  base: process.env.BASE_URL,
  routes: [
    {
      path: '/',
      name: 'home',
      component: Home,
      meta: { title: '主页' }
    },
    {
      path: '/register',
      name: 'register',
      component: Register,
      meta: { title: '注册' }
    },
    {
      path: '/bank',
      name: 'bank',
      component: BaseBank,
      meta: { title: '银行' }
    },
    {
      path: '/enterprise',
      name: 'enterprise',
      component: BaseEnterprise,
      meta: { title: '企业' }
    },
    {
      path: '/insurance',
      name: 'insurance',
      component: BaseInsuranceCompany,
      meta: { title: '保险公司' }
    },
    {
      path: '/transportation',
      name: 'transportation',
      component: BaseTransportationCompany,
      meta: { title: '运输公司' }
    },
    /*
    {
      path: '/about',
      name: 'about',
      // route level code-splitting
      // this generates a separate chunk (about.[hash].js) for this route
      // which is lazy-loaded when the route is visited.
      component: () => import('./views/About.vue') // webpackChunkName: "about" /
    }
    */
  ]
})
router.beforeEach((to, from, next) => {
  if (to.meta.title) {
    document.title = to.meta.title
  }
  next()
})
router.afterEach((to, from, next) => {
  window.scrollTo(0, 0)
})
export default router
