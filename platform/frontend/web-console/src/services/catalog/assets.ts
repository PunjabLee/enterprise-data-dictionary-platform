import { apiClient } from '../apiClient';

export interface MetadataAsset {
  id: number;
  tenantId: string;
  assetType: string;
  code: string;
  name: string;
  displayName?: string | null;
  description?: string | null;
  businessDomain?: string | null;
  systemCode?: string | null;
  ownerUserId?: number | null;
  stewardUserId?: number | null;
  status: string;
  version: number;
  sourceType: string;
  attributesJson?: string | null;
  createdAt?: string | null;
  updatedAt?: string | null;
}

export interface MetadataField {
  id: number;
  tenantId: string;
  assetId: number;
  ordinal?: number | null;
  fieldName: string;
  displayName?: string | null;
  dataType?: string | null;
  lengthValue?: number | null;
  precisionValue?: number | null;
  nullable?: boolean | null;
  primaryKey?: boolean | null;
  keyField?: boolean | null;
  sensitive?: boolean | null;
  classificationLevel?: string | null;
  businessDefinition?: string | null;
  businessRule?: string | null;
  standardCode?: string | null;
  termCode?: string | null;
  ownerUserId?: number | null;
  stewardUserId?: number | null;
  status: string;
  createdAt?: string | null;
  updatedAt?: string | null;
}

export interface MetadataAssetVersion {
  id: number;
  tenantId: string;
  assetId: number;
  versionNumber: number;
  status: string;
  snapshotJson: string;
  publishedBy?: string | null;
  publishedAt?: string | null;
  comment?: string | null;
}

export interface AssetSearchParams {
  keyword?: string;
  assetType?: string;
  status?: string;
  businessDomain?: string;
  systemCode?: string;
  limit?: number;
  offset?: number;
}

export interface FieldDictionarySearchParams {
  keyword?: string;
  assetId?: number;
  sensitive?: boolean;
  status?: string;
  limit?: number;
  offset?: number;
}

export function fetchAssets(params: AssetSearchParams = {}) {
  return apiClient.get<MetadataAsset[]>('/assets', {
    query: params
  });
}

export function fetchAsset(id: number) {
  return apiClient.get<MetadataAsset>(`/assets/${id}`);
}

export function fetchAssetFields(id: number) {
  return apiClient.get<MetadataField[]>(`/assets/${id}/fields`);
}

export function fetchAssetVersions(id: number) {
  return apiClient.get<MetadataAssetVersion[]>(`/assets/${id}/versions`);
}

export function fetchFieldDictionary(params: FieldDictionarySearchParams = {}) {
  return apiClient.get<MetadataField[]>('/dictionary/fields', {
    query: params
  });
}
