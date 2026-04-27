import type { PlatformHealth } from '../types/platform';

const API_BASE_URL = import.meta.env.VITE_API_BASE_URL ?? '/api';

export async function fetchPlatformHealth(): Promise<PlatformHealth> {
  const response = await fetch(`${API_BASE_URL}/actuator/health`);

  if (!response.ok) {
    throw new Error(`Backend health check failed with HTTP ${response.status}.`);
  }

  return response.json() as Promise<PlatformHealth>;
}
