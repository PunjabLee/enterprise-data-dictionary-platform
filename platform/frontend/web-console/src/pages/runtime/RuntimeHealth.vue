<template>
  <section class="page-stack">
    <header class="page-heading">
      <a-typography-title :level="2">Runtime</a-typography-title>
      <a-typography-paragraph>Local backend health probe</a-typography-paragraph>
    </header>

    <a-card title="Backend Health">
      <a-space direction="vertical" :size="16" class="full-width">
        <a-button type="primary" :loading="loading" @click="refresh">
          Check API
        </a-button>

        <a-alert v-if="error" type="error" show-icon :message="error" />

        <pre v-if="health" class="health-output">{{ formattedHealth }}</pre>
      </a-space>
    </a-card>
  </section>
</template>

<script setup lang="ts">
import { computed, ref } from 'vue';

import { fetchPlatformHealth } from '../../services/platformHealth';
import type { PlatformHealth } from '../../types/platform';

const health = ref<PlatformHealth | null>(null);
const error = ref<string | null>(null);
const loading = ref(false);

const formattedHealth = computed(() => JSON.stringify(health.value, null, 2));

async function refresh() {
  loading.value = true;
  error.value = null;

  try {
    health.value = await fetchPlatformHealth();
  } catch (caught) {
    health.value = null;
    error.value = caught instanceof Error ? caught.message : 'Health check failed.';
  } finally {
    loading.value = false;
  }
}
</script>
