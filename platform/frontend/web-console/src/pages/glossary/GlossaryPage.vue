<template>
  <section class="page-stack">
    <header class="page-heading">
      <a-typography-title :level="2">业务术语</a-typography-title>
      <a-typography-paragraph>术语发布后将在这里和字段字典建立引用关系</a-typography-paragraph>
    </header>

    <a-card>
      <a-empty description="业务术语 API 尚未接入">
        <template #image>
          <BookOutlined class="placeholder-icon" />
        </template>
      </a-empty>
    </a-card>
  </section>
</template>

<script setup lang="ts">
import { BookOutlined } from '@ant-design/icons-vue';
</script>
