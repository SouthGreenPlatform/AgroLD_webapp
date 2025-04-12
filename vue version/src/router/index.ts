import { createRouter, createWebHistory } from 'vue-router'
import Home from '../views/Home.vue'
import About from '@/views/About.vue'
import AdvancedSearch from '@/views/AdvancedSearch.vue'
import SparqlEditor from '@/views/SparqlEditor.vue'
import Documentation from '@/views/Documentation.vue'
import QuickSearch from '@/views/QuickSearch.vue'
import ApiDoc from '@/views/ApiDoc.vue'
import Survey from '@/views/Survey.vue'
import RelFinder from '@/views/RelFinder.vue'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      name: 'home',
      component: Home,
    },
    {
      path: '/about',
      name: 'about',
      component: About,
      // route level code-splitting
      // this generates a separate chunk (About.[hash].js) for this route
      // which is lazy-loaded when the route is visited.
      // component: () => import('../views/AboutView.vue'),
    },
    {
      path: '/advanced-search',
      name: 'advanced-search',
      component: AdvancedSearch,
    },
    {
      path: '/sparql-editor',
      name: 'sparql-editor',
      component: SparqlEditor,
    },
    {
      path: '/documentation',
      name: 'documentation',
      component: Documentation
    },
    {
      path: '/quick-search',
      name: 'quick-search',
      component: QuickSearch,
    },
    {
      path: '/api-doc',
      name: 'api-doc',
      component: ApiDoc,
    },
    {
      path: '/survey',
      name: 'survey',
      component: Survey,
    },
    {
      path: '/rel-finder',
      name: 'rel-finder',
      component: RelFinder
    },
    {
      path: '/:pathMatch(.*)*',
      name: 'not-found',
      redirect: '/',
    }
  ],
})

export default router
