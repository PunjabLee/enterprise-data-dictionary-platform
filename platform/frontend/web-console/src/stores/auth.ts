import { computed, reactive, readonly } from 'vue';

export type AuthStatus = 'anonymous' | 'authenticated';

export interface UserPrincipal {
  id: string;
  displayName: string;
  email?: string;
  roles: string[];
}

interface AuthState {
  initialized: boolean;
  status: AuthStatus;
  principal: UserPrincipal | null;
  postLoginRedirect: string | null;
  lastUnauthorizedAt: string | null;
}

const state = reactive<AuthState>({
  initialized: false,
  status: 'anonymous',
  principal: null,
  postLoginRedirect: null,
  lastUnauthorizedAt: null
});

let accessToken: string | null = null;

const isAuthenticated = computed(() => state.status === 'authenticated');
const currentPrincipal = computed(() => state.principal);

function bootstrap() {
  state.initialized = true;
}

function signIn(principal: UserPrincipal, token?: string | null) {
  state.status = 'authenticated';
  state.principal = principal;
  state.lastUnauthorizedAt = null;
  accessToken = token ?? null;
}

function signOut() {
  state.status = 'anonymous';
  state.principal = null;
  accessToken = null;
}

function rememberRedirect(path: string) {
  state.postLoginRedirect = path;
}

function consumeRedirect() {
  const redirect = state.postLoginRedirect;
  state.postLoginRedirect = null;
  return redirect;
}

function markUnauthorized() {
  state.status = 'anonymous';
  state.principal = null;
  state.lastUnauthorizedAt = new Date().toISOString();
  accessToken = null;
}

function getAccessToken() {
  return accessToken;
}

export const authStore = {
  state: readonly(state),
  isAuthenticated,
  currentPrincipal,
  bootstrap,
  signIn,
  signOut,
  rememberRedirect,
  consumeRedirect,
  markUnauthorized,
  getAccessToken
};

export function useAuthStore() {
  return authStore;
}
