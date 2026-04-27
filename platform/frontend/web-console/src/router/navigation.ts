import { CloudServerOutlined, DashboardOutlined } from '@ant-design/icons-vue';
import type { MenuProps } from 'ant-design-vue';
import { h, type Component } from 'vue';

import { routeNames } from './routeNames';

export interface ConsoleNavigationItem {
  key: string;
  routeName: string;
  label: string;
  icon: Component;
}

export const consoleNavigationItems: ConsoleNavigationItem[] = [
  {
    key: routeNames.dashboard,
    routeName: routeNames.dashboard,
    label: 'Overview',
    icon: DashboardOutlined
  },
  {
    key: routeNames.runtime,
    routeName: routeNames.runtime,
    label: 'Runtime',
    icon: CloudServerOutlined
  }
];

export function buildConsoleMenuItems(): MenuProps['items'] {
  return consoleNavigationItems.map((item) => ({
    key: item.key,
    icon: () => h(item.icon),
    label: item.label
  }));
}
