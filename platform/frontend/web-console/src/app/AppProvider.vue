<template>
  <a-config-provider :theme="theme">
    <RouterView />
  </a-config-provider>
</template>

<script setup lang="ts">
const theme = {
  token: {
    colorPrimary: '#1769aa',
    borderRadius: 6,
    fontFamily:
      'Inter, ui-sans-serif, system-ui, -apple-system, BlinkMacSystemFont, "Segoe UI", sans-serif'
  }
};
</script>
