import 'vue-router';

export interface NavigationMeta {
  key: string;
  label: string;
  order: number;
}

declare module 'vue-router' {
  interface RouteMeta {
    title?: string;
    breadcrumb?: string;
    public?: boolean;
    requiresAuth?: boolean;
    navigation?: NavigationMeta;
    navigationKey?: string;
  }
}

export {};
