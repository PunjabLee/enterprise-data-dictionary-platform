<template>
  <section class="page-stack">
    <header class="page-heading">
      <a-typography-title :level="2">资产目录</a-typography-title>
      <a-typography-paragraph>查询企业数据资产、责任归属和发布状态</a-typography-paragraph>
    </header>

    <a-card>
      <a-form layout="vertical" :model="filters">
        <a-row :gutter="[12, 12]">
          <a-col :xs="24" :md="8">
            <a-form-item label="关键字">
              <a-input v-model:value="filters.keyword" allow-clear placeholder="资产编码、名称或说明" />
            </a-form-item>
          </a-col>
          <a-col :xs="12" :md="4">
            <a-form-item label="资产类型">
              <a-select v-model:value="filters.assetType" allow-clear :options="assetTypeOptions" />
            </a-form-item>
          </a-col>
          <a-col :xs="12" :md="4">
            <a-form-item label="状态">
              <a-select v-model:value="filters.status" allow-clear :options="statusOptions" />
            </a-form-item>
          </a-col>
          <a-col :xs="12" :md="4">
            <a-form-item label="业务域">
              <a-input v-model:value="filters.businessDomain" allow-clear />
            </a-form-item>
          </a-col>
          <a-col :xs="12" :md="4">
            <a-form-item label="所属系统">
              <a-input v-model:value="filters.systemCode" allow-clear />
            </a-form-item>
          </a-col>
        </a-row>
        <a-space>
          <a-button type="primary" :loading="loading" @click="loadAssets">查询</a-button>
          <a-button @click="resetFilters">重置</a-button>
        </a-space>
      </a-form>
    </a-card>

    <a-alert v-if="error" type="error" show-icon :message="error" />

    <a-card title="资产列表">
      <a-table
        row-key="id"
        :columns="columns"
        :data-source="assets"
        :loading="loading"
        :pagination="false"
        :scroll="{ x: 980 }"
      >
        <template #bodyCell="{ column, record }">
          <template v-if="column.key === 'asset'">
            <RouterLink :to="{ name: routeNames.assetDetail, params: { id: record.id } }">
              <a-typography-text strong>{{ record.displayName || record.name }}</a-typography-text>
            </RouterLink>
            <div class="muted-line">{{ record.code }}</div>
          </template>
          <template v-else-if="column.key === 'assetType'">
            <a-tag color="blue">{{ record.assetType }}</a-tag>
          </template>
          <template v-else-if="column.key === 'status'">
            <a-tag :color="statusColor(record.status)">{{ statusText(record.status) }}</a-tag>
          </template>
          <template v-else-if="column.key === 'version'">
            v{{ record.version }}
          </template>
        </template>
        <template #emptyText>
          <a-empty description="暂无资产" />
        </template>
      </a-table>
    </a-card>
  </section>
</template>

<script setup lang="ts">
import type { TableColumnsType } from 'ant-design-vue';
import { onMounted, reactive, ref } from 'vue';
import { RouterLink } from 'vue-router';

import { routeNames } from '../../router/routeNames';
import { fetchAssets, type MetadataAsset } from '../../services/catalog/assets';

const filters = reactive({
  keyword: undefined as string | undefined,
  assetType: undefined as string | undefined,
  status: undefined as string | undefined,
  businessDomain: undefined as string | undefined,
  systemCode: undefined as string | undefined
});

const assets = ref<MetadataAsset[]>([]);
const loading = ref(false);
const error = ref<string | null>(null);

const assetTypeOptions = [
  { label: '表', value: 'table' },
  { label: '系统', value: 'system' },
  { label: '接口', value: 'api' },
  { label: '报表', value: 'report' }
];
const statusOptions = [
  { label: '草稿', value: 'draft' },
  { label: '已发布', value: 'published' },
  { label: '已废弃', value: 'deprecated' }
];
const columns: TableColumnsType<MetadataAsset> = [
  { title: '资产', key: 'asset', fixed: 'left', width: 280 },
  { title: '类型', key: 'assetType', dataIndex: 'assetType', width: 120 },
  { title: '业务域', dataIndex: 'businessDomain', width: 140 },
  { title: '系统', dataIndex: 'systemCode', width: 120 },
  { title: '状态', key: 'status', dataIndex: 'status', width: 120 },
  { title: '版本', key: 'version', dataIndex: 'version', width: 100 },
  { title: '更新时间', dataIndex: 'updatedAt', width: 220 }
];

onMounted(loadAssets);

async function loadAssets() {
  loading.value = true;
  error.value = null;
  try {
    assets.value = await fetchAssets({
      ...filters,
      limit: 100,
      offset: 0
    });
  } catch (caught) {
    assets.value = [];
    error.value = caught instanceof Error ? caught.message : '资产查询失败';
  } finally {
    loading.value = false;
  }
}

function resetFilters() {
  filters.keyword = undefined;
  filters.assetType = undefined;
  filters.status = undefined;
  filters.businessDomain = undefined;
  filters.systemCode = undefined;
  void loadAssets();
}

function statusColor(status: string) {
  if (status === 'published') {
    return 'green';
  }
  if (status === 'deprecated') {
    return 'default';
  }
  return 'gold';
}

function statusText(status: string) {
  const names: Record<string, string> = {
    draft: '草稿',
    published: '已发布',
    deprecated: '已废弃'
  };
  return names[status] ?? status;
}
</script>
