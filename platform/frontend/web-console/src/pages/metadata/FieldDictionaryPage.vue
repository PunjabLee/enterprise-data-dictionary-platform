<template>
  <section class="page-stack">
    <header class="page-heading">
      <a-typography-title :level="2">字段字典</a-typography-title>
      <a-typography-paragraph>维护字段业务定义、分类分级和标准引用</a-typography-paragraph>
    </header>

    <a-card>
      <a-space direction="vertical" :size="12" class="full-width">
        <a-row :gutter="[12, 12]">
          <a-col :xs="24" :md="10">
            <a-input v-model:value="filters.keyword" allow-clear placeholder="字段名、中文名或业务定义" />
          </a-col>
          <a-col :xs="12" :md="4">
            <a-select
              v-model:value="filters.sensitive"
              allow-clear
              class="full-width"
              placeholder="敏感标识"
              :options="sensitiveOptions"
            />
          </a-col>
          <a-col :xs="12" :md="4">
            <a-select
              v-model:value="filters.status"
              allow-clear
              class="full-width"
              placeholder="状态"
              :options="statusOptions"
            />
          </a-col>
          <a-col :xs="24" :md="6">
            <a-space>
              <a-button type="primary" :loading="loading" @click="loadFields">查询</a-button>
              <a-button @click="resetFilters">重置</a-button>
            </a-space>
          </a-col>
        </a-row>
      </a-space>
    </a-card>

    <a-alert v-if="error" type="error" show-icon :message="error" />

    <a-card title="字段列表">
      <a-table
        row-key="id"
        :columns="columns"
        :data-source="fields"
        :loading="loading"
        :pagination="false"
        :scroll="{ x: 1180 }"
      >
        <template #bodyCell="{ column, record }">
          <template v-if="column.key === 'field'">
            <a-typography-text strong>{{ record.displayName || record.fieldName }}</a-typography-text>
            <div class="muted-line">{{ record.fieldName }}</div>
          </template>
          <template v-else-if="column.key === 'sensitive'">
            <a-tag :color="record.sensitive ? 'red' : 'default'">
              {{ record.sensitive ? '敏感' : '普通' }}
            </a-tag>
          </template>
          <template v-else-if="column.key === 'definition'">
            <span>{{ record.businessDefinition || '未维护' }}</span>
          </template>
        </template>
        <template #emptyText>
          <a-empty description="暂无字段字典" />
        </template>
      </a-table>
    </a-card>
  </section>
</template>

<script setup lang="ts">
import type { TableColumnsType } from 'ant-design-vue';
import { onMounted, reactive, ref } from 'vue';

import { fetchFieldDictionary, type MetadataField } from '../../services/catalog/assets';

const filters = reactive({
  keyword: undefined as string | undefined,
  sensitive: undefined as boolean | undefined,
  status: undefined as string | undefined
});
const fields = ref<MetadataField[]>([]);
const loading = ref(false);
const error = ref<string | null>(null);

const sensitiveOptions = [
  { label: '敏感字段', value: true },
  { label: '普通字段', value: false }
];
const statusOptions = [
  { label: '有效', value: 'active' },
  { label: '停用', value: 'disabled' }
];
const columns: TableColumnsType<MetadataField> = [
  { title: '字段', key: 'field', fixed: 'left', width: 260 },
  { title: '资产 ID', dataIndex: 'assetId', width: 100 },
  { title: '数据类型', dataIndex: 'dataType', width: 120 },
  { title: '分类分级', dataIndex: 'classificationLevel', width: 140 },
  { title: '敏感', key: 'sensitive', width: 100 },
  { title: '业务定义', key: 'definition', dataIndex: 'businessDefinition', width: 320 },
  { title: '标准', dataIndex: 'standardCode', width: 140 },
  { title: '术语', dataIndex: 'termCode', width: 140 }
];

onMounted(loadFields);

async function loadFields() {
  loading.value = true;
  error.value = null;
  try {
    fields.value = await fetchFieldDictionary({
      ...filters,
      limit: 100,
      offset: 0
    });
  } catch (caught) {
    fields.value = [];
    error.value = caught instanceof Error ? caught.message : '字段字典查询失败';
  } finally {
    loading.value = false;
  }
}

function resetFilters() {
  filters.keyword = undefined;
  filters.sensitive = undefined;
  filters.status = undefined;
  void loadFields();
}
</script>
