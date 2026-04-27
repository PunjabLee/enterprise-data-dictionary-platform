<template>
  <a-config-provider :theme="consoleTheme">
    <RouterView />
  </a-config-provider>
</template>

<script setup lang="ts">
import { consoleTheme } from '../theme/consoleTheme';
</script>
