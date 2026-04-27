export interface PlatformHealth {
  status: string;
  components?: Record<string, PlatformHealthComponent>;
  [key: string]: unknown;
}

export interface PlatformHealthComponent {
  status?: string;
  details?: Record<string, unknown>;
  [key: string]: unknown;
}
