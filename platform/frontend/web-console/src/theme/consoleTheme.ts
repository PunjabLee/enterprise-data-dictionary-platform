import type { ThemeConfig } from 'ant-design-vue/es/config-provider/context';

export const consoleTheme: ThemeConfig = {
  token: {
    colorPrimary: '#0f766e',
    colorInfo: '#2563eb',
    colorSuccess: '#15803d',
    colorWarning: '#d97706',
    colorError: '#dc2626',
    colorText: '#182230',
    colorTextSecondary: '#52606d',
    colorBorder: '#d9e2ec',
    colorBgLayout: '#f4f7f6',
    borderRadius: 6,
    fontFamily:
      'Inter, ui-sans-serif, system-ui, -apple-system, BlinkMacSystemFont, "Segoe UI", sans-serif'
  },
  components: {
    Layout: {
      headerBg: '#ffffff',
      siderBg: '#163235',
      triggerBg: '#10272a'
    },
    Menu: {
      darkItemBg: '#163235',
      darkItemSelectedBg: '#0f766e',
      itemBorderRadius: 6
    },
    Card: {
      borderRadiusLG: 6
    },
    Button: {
      borderRadius: 6
    }
  }
};
