import type { RouteLocationNormalized, Router } from 'vue-router';

import { runtimeConfig } from '../services/runtimeConfig';
import { useAuthStore } from '../stores/auth';
import { routeNames } from './routeNames';

const APP_TITLE = 'Enterprise Data Dictionary';

export function installRouterGuards(router: Router) {
  router.beforeEach((to) => {
    const auth = useAuthStore();
    auth.bootstrap();

    if (!routeRequiresAuth(to) || auth.isAuthenticated.value) {
      return true;
    }

    auth.rememberRedirect(to.fullPath);

    if (runtimeConfig.loginUrl) {
      window.location.assign(createLoginRedirect(runtimeConfig.loginUrl, to.fullPath));
      return false;
    }

    return {
      name: routeNames.dashboard,
      query: {
        redirect: to.fullPath
      }
    };
  });

  router.afterEach((to) => {
    document.title = createDocumentTitle(to);
  });
}

function routeRequiresAuth(route: RouteLocationNormalized) {
  const explicitlyProtected = route.matched.some((record) => record.meta.requiresAuth);
  const explicitlyPublic = route.matched.some((record) => record.meta.public);

  if (explicitlyProtected) {
    return true;
  }

  if (explicitlyPublic) {
    return false;
  }

  return runtimeConfig.authRequired;
}

function createLoginRedirect(loginUrl: string, redirectPath: string) {
  const url = new URL(loginUrl, window.location.origin);
  url.searchParams.set('redirect', redirectPath);
  return url.toString();
}

function createDocumentTitle(route: RouteLocationNormalized) {
  const title = route.meta.title;
  return title ? `${title} | ${APP_TITLE}` : APP_TITLE;
}
