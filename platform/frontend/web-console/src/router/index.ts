import type { RouteRecordRaw } from 'vue-router';
import { createRouter, createWebHistory } from 'vue-router';

import ConsoleLayout from '../layouts/ConsoleLayout.vue';
import PlatformHome from '../pages/dashboard/PlatformHome.vue';
import RuntimeHealth from '../pages/runtime/RuntimeHealth.vue';
import { installRouterGuards } from './guards';
import { routeNames } from './routeNames';
import './types';

export const routes: RouteRecordRaw[] = [
  {
    path: '/',
    component: ConsoleLayout,
    meta: {
      title: 'Console',
      breadcrumb: 'Console',
      public: true
    },
    children: [
      {
        path: '',
        name: routeNames.dashboard,
        component: PlatformHome,
        meta: {
          title: 'Overview',
          breadcrumb: 'Overview',
          public: true,
          navigation: {
            key: routeNames.dashboard,
            label: 'Overview',
            order: 10
          }
        }
      },
      {
        path: 'runtime',
        name: routeNames.runtime,
        component: RuntimeHealth,
        meta: {
          title: 'Runtime',
          breadcrumb: 'Runtime',
          public: true,
          navigation: {
            key: routeNames.runtime,
            label: 'Runtime',
            order: 20
          }
        }
      }
    ]
  },
  {
    path: '/:pathMatch(.*)*',
    redirect: {
      name: routeNames.dashboard
    }
  }
];

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes
});

installRouterGuards(router);

export default router;
