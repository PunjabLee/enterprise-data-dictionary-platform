import type { RouteRecordRaw } from 'vue-router';
import { createRouter, createWebHistory } from 'vue-router';

import ConsoleLayout from '../layouts/ConsoleLayout.vue';
import AssetCatalogPage from '../pages/catalog/AssetCatalogPage.vue';
import AssetDetailPage from '../pages/catalog/AssetDetailPage.vue';
import PlatformHome from '../pages/dashboard/PlatformHome.vue';
import GlossaryPage from '../pages/glossary/GlossaryPage.vue';
import FieldDictionaryPage from '../pages/metadata/FieldDictionaryPage.vue';
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
      },
      {
        path: 'assets',
        name: routeNames.assetCatalog,
        component: AssetCatalogPage,
        meta: {
          title: '资产目录',
          breadcrumb: '资产目录',
          navigation: {
            key: routeNames.assetCatalog,
            label: '资产目录',
            order: 30
          }
        }
      },
      {
        path: 'assets/:id',
        name: routeNames.assetDetail,
        component: AssetDetailPage,
        meta: {
          title: '资产详情',
          breadcrumb: '资产详情',
          navigation: {
            key: routeNames.assetCatalog,
            label: '资产目录',
            order: 31
          }
        }
      },
      {
        path: 'dictionary/fields',
        name: routeNames.fieldDictionary,
        component: FieldDictionaryPage,
        meta: {
          title: '字段字典',
          breadcrumb: '字段字典',
          navigation: {
            key: routeNames.fieldDictionary,
            label: '字段字典',
            order: 40
          }
        }
      },
      {
        path: 'glossary',
        name: routeNames.glossary,
        component: GlossaryPage,
        meta: {
          title: '业务术语',
          breadcrumb: '业务术语',
          navigation: {
            key: routeNames.glossary,
            label: '业务术语',
            order: 50
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
