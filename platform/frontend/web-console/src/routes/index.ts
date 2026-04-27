import type { RouteRecordRaw } from 'vue-router';
import { createRouter, createWebHistory } from 'vue-router';

import ConsoleLayout from '../layouts/ConsoleLayout.vue';
import PlatformHome from '../pages/dashboard/PlatformHome.vue';
import RuntimeHealth from '../pages/runtime/RuntimeHealth.vue';

const routes: RouteRecordRaw[] = [
  {
    path: '/',
    component: ConsoleLayout,
    children: [
      {
        path: '',
        name: 'dashboard',
        component: PlatformHome
      },
      {
        path: 'runtime',
        name: 'runtime',
        component: RuntimeHealth
      }
    ]
  }
];

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes
});

export default router;
