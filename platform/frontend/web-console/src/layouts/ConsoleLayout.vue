<template>
  <a-layout class="console-shell">
    <a-layout-sider
      v-model:collapsed="collapsed"
      class="console-sider"
      :width="248"
      :collapsed-width="72"
      :trigger="null"
      collapsible
      breakpoint="lg"
    >
      <RouterLink class="brand" :to="{ name: routeNames.dashboard }" aria-label="Enterprise Data Dictionary home">
        <span class="brand-mark">DD</span>
        <span class="brand-name">Enterprise Data Dictionary</span>
      </RouterLink>

      <a-menu
        class="console-nav"
        theme="dark"
        mode="inline"
        :items="menuItems"
        :selected-keys="selectedKeys"
        @click="handleMenuClick"
      />
    </a-layout-sider>

    <a-layout class="console-main">
      <a-layout-header class="console-topbar">
        <a-space :size="12" class="topbar-left">
          <a-button type="text" class="shell-trigger" :aria-label="triggerLabel" @click="toggleCollapsed">
            <template #icon>
              <MenuUnfoldOutlined v-if="collapsed" />
              <MenuFoldOutlined v-else />
            </template>
          </a-button>
          <a-breadcrumb :items="breadcrumbItems" />
        </a-space>

        <a-space :size="12" class="topbar-actions">
          <a-tag :color="authTagColor">{{ authTagLabel }}</a-tag>
          <a-dropdown :trigger="['click']">
            <a-button class="user-button">
              <template #icon>
                <UserOutlined />
              </template>
              <span class="user-name">{{ userDisplayName }}</span>
            </a-button>
            <template #overlay>
              <a-menu :items="userMenuItems" @click="handleUserMenuClick" />
            </template>
          </a-dropdown>
        </a-space>
      </a-layout-header>

      <a-layout-content class="console-content">
        <RouterView v-slot="{ Component, route: viewRoute }">
          <Transition name="route-fade" mode="out-in">
            <component :is="Component" :key="viewRoute.fullPath" />
          </Transition>
        </RouterView>
      </a-layout-content>
    </a-layout>
  </a-layout>
</template>

<script setup lang="ts">
import {
  LogoutOutlined,
  MenuFoldOutlined,
  MenuUnfoldOutlined,
  UserOutlined
} from '@ant-design/icons-vue';
import type { BreadcrumbProps, MenuProps } from 'ant-design-vue';
import { computed, h, ref } from 'vue';
import { RouterLink, RouterView, useRoute, useRouter } from 'vue-router';

import { buildConsoleMenuItems } from '../router/navigation';
import { routeNames } from '../router/routeNames';
import { useAuthStore } from '../stores/auth';

const route = useRoute();
const router = useRouter();
const auth = useAuthStore();

const collapsed = ref(false);
const menuItems = buildConsoleMenuItems();

const selectedKeys = computed(() => [
  String(route.meta.navigation?.key ?? route.meta.navigationKey ?? route.name ?? routeNames.dashboard)
]);
const breadcrumbItems = computed<BreadcrumbProps['items']>(() =>
  route.matched
    .filter((record) => record.meta.title)
    .map((record) => ({
      title: String(record.meta.breadcrumb ?? record.meta.title)
    }))
);
const triggerLabel = computed(() => (collapsed.value ? 'Expand navigation' : 'Collapse navigation'));
const userDisplayName = computed(() => auth.currentPrincipal.value?.displayName ?? 'Local session');
const authTagLabel = computed(() => (auth.isAuthenticated.value ? 'Authenticated' : 'Local'));
const authTagColor = computed(() => (auth.isAuthenticated.value ? 'green' : 'default'));
const userMenuItems = computed<MenuProps['items']>(() => [
  {
    key: 'session-status',
    label: auth.isAuthenticated.value ? 'Signed in' : 'No active identity',
    disabled: true
  },
  {
    type: 'divider'
  },
  {
    key: 'sign-out',
    icon: () => h(LogoutOutlined),
    label: 'Clear session',
    disabled: !auth.isAuthenticated.value
  }
]);

const handleMenuClick: MenuProps['onClick'] = ({ key }) => {
  router.push({ name: String(key) });
};

const handleUserMenuClick: MenuProps['onClick'] = ({ key }) => {
  if (key === 'sign-out') {
    auth.signOut();
  }
};

function toggleCollapsed() {
  collapsed.value = !collapsed.value;
}
</script>
