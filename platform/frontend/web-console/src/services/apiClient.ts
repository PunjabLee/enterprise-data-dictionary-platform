import { runtimeConfig } from './runtimeConfig';
import { useAuthStore } from '../stores/auth';

export type QueryValue = string | number | boolean | null | undefined;

export interface ApiRequestOptions extends Omit<RequestInit, 'body'> {
  body?: BodyInit | Record<string, unknown> | unknown[] | null;
  query?: Record<string, QueryValue | QueryValue[]>;
}

export class ApiError extends Error {
  readonly status: number;
  readonly statusText: string;
  readonly url: string;
  readonly body: unknown;

  constructor(message: string, response: Response, body: unknown) {
    super(message);
    this.name = 'ApiError';
    this.status = response.status;
    this.statusText = response.statusText;
    this.url = response.url;
    this.body = body;
  }
}

type ApiMutationOptions = Omit<ApiRequestOptions, 'body' | 'method'>;

function createUrl(path: string, query?: ApiRequestOptions['query']) {
  const normalizedPath = path.startsWith('/') ? path : `/${path}`;
  const url = `${runtimeConfig.apiBaseUrl}${normalizedPath}`;

  return appendQuery(url, query);
}

function appendQuery(url: string, query?: ApiRequestOptions['query']) {
  if (!query) {
    return url;
  }

  const params = new URLSearchParams();

  for (const [key, value] of Object.entries(query)) {
    const values = Array.isArray(value) ? value : [value];

    for (const entry of values) {
      if (entry !== null && entry !== undefined) {
        params.append(key, String(entry));
      }
    }
  }

  const queryString = params.toString();

  if (queryString.length === 0) {
    return url;
  }

  return `${url}${url.includes('?') ? '&' : '?'}${queryString}`;
}

function isBodyInit(value: unknown): value is BodyInit {
  return (
    typeof value === 'string' ||
    value instanceof Blob ||
    value instanceof FormData ||
    value instanceof URLSearchParams ||
    value instanceof ArrayBuffer
  );
}

function serializeBody(body: ApiRequestOptions['body'], headers: Headers) {
  if (body === undefined || body === null) {
    return body;
  }

  if (isBodyInit(body)) {
    return body;
  }

  if (!headers.has('Content-Type')) {
    headers.set('Content-Type', 'application/json');
  }

  return JSON.stringify(body);
}

async function parseResponseBody(response: Response) {
  if (response.status === 204) {
    return undefined;
  }

  const contentType = response.headers.get('content-type') ?? '';

  if (contentType.includes('application/json')) {
    return response.json() as Promise<unknown>;
  }

  return response.text();
}

async function createApiError(response: Response) {
  const body = await parseResponseBody(response);
  const message = `API request failed with HTTP ${response.status}.`;
  return new ApiError(message, response, body);
}

export async function apiRequest<TResponse>(
  path: string,
  options: ApiRequestOptions = {}
): Promise<TResponse> {
  const { body, headers: providedHeaders, query, ...fetchOptions } = options;
  const auth = useAuthStore();
  const headers = new Headers(providedHeaders);
  const token = auth.getAccessToken();

  if (!headers.has('Accept')) {
    headers.set('Accept', 'application/json');
  }

  if (token && !headers.has('Authorization')) {
    headers.set('Authorization', `Bearer ${token}`);
  }

  const response = await fetch(createUrl(path, query), {
    credentials: 'include',
    ...fetchOptions,
    headers,
    body: serializeBody(body, headers)
  });

  if (response.status === 401) {
    auth.markUnauthorized();
  }

  if (!response.ok) {
    throw await createApiError(response);
  }

  return parseResponseBody(response) as Promise<TResponse>;
}

export const apiClient = {
  request: apiRequest,
  get<TResponse>(path: string, options?: ApiRequestOptions) {
    return apiRequest<TResponse>(path, { ...options, method: 'GET' });
  },
  post<TResponse>(path: string, body?: ApiRequestOptions['body'], options?: ApiMutationOptions) {
    return apiRequest<TResponse>(path, { ...options, method: 'POST', body });
  },
  put<TResponse>(path: string, body?: ApiRequestOptions['body'], options?: ApiMutationOptions) {
    return apiRequest<TResponse>(path, { ...options, method: 'PUT', body });
  },
  patch<TResponse>(path: string, body?: ApiRequestOptions['body'], options?: ApiMutationOptions) {
    return apiRequest<TResponse>(path, { ...options, method: 'PATCH', body });
  },
  delete<TResponse>(path: string, options?: ApiRequestOptions) {
    return apiRequest<TResponse>(path, { ...options, method: 'DELETE' });
  }
};
