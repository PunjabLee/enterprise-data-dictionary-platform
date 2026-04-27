<template>
  <a-layout class="console-shell">
    <a-layout-header class="console-header">
      <div class="brand">
        <span class="brand-mark">DD</span>
        <span class="brand-name">Enterprise Data Dictionary</span>
      </div>
      <a-menu
        class="header-menu"
        theme="dark"
        mode="horizontal"
        :items="menuItems"
        :selected-keys="selectedKeys"
        @click="handleMenuClick"
      />
    </a-layout-header>

    <a-layout-content class="console-content">
      <RouterView />
    </a-layout-content>
  </a-layout>
</template>

<script setup lang="ts">
import { CloudServerOutlined, DashboardOutlined } from '@ant-design/icons-vue';
import type { MenuProps } from 'ant-design-vue';
import { computed, h } from 'vue';
import { useRoute, useRouter } from 'vue-router';

const route = useRoute();
const router = useRouter();

const menuItems: MenuProps['items'] = [
  {
    key: 'dashboard',
    icon: () => h(DashboardOutlined),
    label: 'Overview'
  },
  {
    key: 'runtime',
    icon: () => h(CloudServerOutlined),
    label: 'Runtime'
  }
];

const selectedKeys = computed(() => [String(route.name ?? 'dashboard')]);

const handleMenuClick: MenuProps['onClick'] = ({ key }) => {
  router.push({ name: String(key) });
};
</script>
