<template>
  <section class="page-stack">
    <a-button type="link" class="back-link" @click="router.back()">返回资产目录</a-button>

    <a-alert v-if="error" type="error" show-icon :message="error" />

    <a-card v-if="asset" :loading="loading">
      <template #title>
        <a-space direction="vertical" :size="2">
          <span>{{ asset.displayName || asset.name }}</span>
          <a-typography-text type="secondary">{{ asset.code }}</a-typography-text>
        </a-space>
      </template>
      <template #extra>
        <a-space>
          <a-tag :color="asset.status === 'published' ? 'green' : 'gold'">{{ asset.status }}</a-tag>
          <a-tag>v{{ asset.version }}</a-tag>
        </a-space>
      </template>
      <a-descriptions bordered :column="{ xs: 1, md: 2 }" size="small">
        <a-descriptions-item label="资产类型">{{ asset.assetType }}</a-descriptions-item>
        <a-descriptions-item label="来源">{{ asset.sourceType }}</a-descriptions-item>
        <a-descriptions-item label="业务域">{{ asset.businessDomain || '-' }}</a-descriptions-item>
        <a-descriptions-item label="所属系统">{{ asset.systemCode || '-' }}</a-descriptions-item>
        <a-descriptions-item label="Owner">{{ asset.ownerUserId || '-' }}</a-descriptions-item>
        <a-descriptions-item label="Steward">{{ asset.stewardUserId || '-' }}</a-descriptions-item>
        <a-descriptions-item label="说明" :span="2">{{ asset.description || '-' }}</a-descriptions-item>
      </a-descriptions>
    </a-card>

    <a-tabs>
      <a-tab-pane key="fields" tab="字段清单">
        <a-card>
          <a-table
            row-key="id"
            :columns="fieldColumns"
            :data-source="fields"
            :loading="loading"
            :pagination="false"
            :scroll="{ x: 1180 }"
          >
            <template #bodyCell="{ column, record }">
              <template v-if="column.key === 'fieldName'">
                <a-typography-text strong>{{ record.displayName || record.fieldName }}</a-typography-text>
                <div class="muted-line">{{ record.fieldName }}</div>
              </template>
              <template v-else-if="column.key === 'flags'">
                <a-space wrap>
                  <a-tag v-if="record.primaryKey" color="purple">主键</a-tag>
                  <a-tag v-if="record.keyField" color="blue">关键</a-tag>
                  <a-tag v-if="record.sensitive" color="red">敏感</a-tag>
                </a-space>
              </template>
            </template>
            <template #emptyText>
              <a-empty description="暂无字段" />
            </template>
          </a-table>
        </a-card>
      </a-tab-pane>
      <a-tab-pane key="versions" tab="版本记录">
        <a-card>
          <a-timeline>
            <a-timeline-item v-for="version in versions" :key="version.id">
              <a-typography-text strong>v{{ version.versionNumber }}</a-typography-text>
              <span class="muted-inline">{{ version.publishedBy || '-' }} {{ version.publishedAt || '' }}</span>
              <div>{{ version.comment || '无发布备注' }}</div>
            </a-timeline-item>
          </a-timeline>
          <a-empty v-if="versions.length === 0 && !loading" description="暂无版本" />
        </a-card>
      </a-tab-pane>
    </a-tabs>
  </section>
</template>

<script setup lang="ts">
import type { TableColumnsType } from 'ant-design-vue';
import { computed, onMounted, ref } from 'vue';
import { useRoute, useRouter } from 'vue-router';

import {
  fetchAsset,
  fetchAssetFields,
  fetchAssetVersions,
  type MetadataAsset,
  type MetadataAssetVersion,
  type MetadataField
} from '../../services/catalog/assets';

const route = useRoute();
const router = useRouter();
const assetId = computed(() => Number(route.params.id));
const asset = ref<MetadataAsset | null>(null);
const fields = ref<MetadataField[]>([]);
const versions = ref<MetadataAssetVersion[]>([]);
const loading = ref(false);
const error = ref<string | null>(null);

const fieldColumns: TableColumnsType<MetadataField> = [
  { title: '字段', key: 'fieldName', fixed: 'left', width: 260 },
  { title: '类型', dataIndex: 'dataType', width: 120 },
  { title: '分类分级', dataIndex: 'classificationLevel', width: 140 },
  { title: '业务定义', dataIndex: 'businessDefinition', width: 320 },
  { title: '标准', dataIndex: 'standardCode', width: 140 },
  { title: '术语', dataIndex: 'termCode', width: 140 },
  { title: '标识', key: 'flags', width: 180 }
];

onMounted(loadDetail);

async function loadDetail() {
  loading.value = true;
  error.value = null;
  try {
    const [assetResult, fieldResult, versionResult] = await Promise.all([
      fetchAsset(assetId.value),
      fetchAssetFields(assetId.value),
      fetchAssetVersions(assetId.value)
    ]);
    asset.value = assetResult;
    fields.value = fieldResult;
    versions.value = versionResult;
  } catch (caught) {
    error.value = caught instanceof Error ? caught.message : '资产详情加载失败';
  } finally {
    loading.value = false;
  }
}
</script>
