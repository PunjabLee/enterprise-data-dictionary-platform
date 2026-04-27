import type { PlatformHealth } from '../types/platform';
import { apiClient } from './apiClient';

export async function fetchPlatformHealth(): Promise<PlatformHealth> {
  return apiClient.get<PlatformHealth>('/actuator/health');
}
