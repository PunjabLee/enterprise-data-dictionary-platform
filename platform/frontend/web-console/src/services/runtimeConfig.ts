export interface RuntimeConfig {
  apiBaseUrl: string;
  authRequired: boolean;
  loginUrl?: string;
}

function normalizeBaseUrl(value: string | undefined) {
  const trimmed = value?.trim() ?? '';

  if (trimmed.length === 0 || trimmed === '/') {
    return '';
  }

  return trimmed.replace(/\/+$/, '');
}

function readBoolean(value: string | undefined) {
  return value?.toLowerCase() === 'true';
}

function readOptionalString(value: string | undefined) {
  const trimmed = value?.trim();
  return trimmed && trimmed.length > 0 ? trimmed : undefined;
}

export const runtimeConfig: RuntimeConfig = {
  apiBaseUrl: normalizeBaseUrl(import.meta.env.VITE_API_BASE_URL ?? '/api'),
  authRequired: readBoolean(import.meta.env.VITE_AUTH_REQUIRED),
  loginUrl: readOptionalString(import.meta.env.VITE_LOGIN_URL)
};
