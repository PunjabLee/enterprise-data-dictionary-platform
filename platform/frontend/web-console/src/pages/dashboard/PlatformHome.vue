<template>
  <section class="page-stack">
    <header class="page-heading">
      <a-typography-title :level="2">Platform Foundation</a-typography-title>
      <a-typography-paragraph>
        Backend, frontend, and local runtime boundaries
      </a-typography-paragraph>
    </header>

    <a-row :gutter="[16, 16]">
      <a-col v-for="boundary in boundaries" :key="boundary.title" :xs="24" :md="8">
        <a-card class="boundary-card" :title="boundary.title">
          <p>{{ boundary.description }}</p>
          <a-tag :color="boundary.color">{{ boundary.tag }}</a-tag>
        </a-card>
      </a-col>
    </a-row>

    <a-card title="Local Startup">
      <a-steps :current="0" responsive>
        <a-step title="Start runtime" description="PostgreSQL 16 and Redis" />
        <a-step title="Run backend" description="Spring Boot on port 8080" />
        <a-step title="Run console" description="Vite on port 5173" />
      </a-steps>
    </a-card>
  </section>
</template>

<script setup lang="ts">
const boundaries = [
  {
    title: 'Backend',
    description: 'Spring Boot Maven application with DDD layer placeholders.',
    tag: 'metadata-platform',
    color: 'blue'
  },
  {
    title: 'Frontend',
    description: 'Vue 3 TypeScript console with Ant Design Vue and routing.',
    tag: 'web-console',
    color: 'green'
  },
  {
    title: 'Deploy',
    description: 'Local Docker Compose services for PostgreSQL and Redis.',
    tag: 'local-runtime',
    color: 'gold'
  }
];
</script>
