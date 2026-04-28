import {
  BookOutlined,
  CloudServerOutlined,
  DashboardOutlined,
  ProfileOutlined,
  TableOutlined
} from '@ant-design/icons-vue';
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
  },
  {
    key: routeNames.assetCatalog,
    routeName: routeNames.assetCatalog,
    label: '资产目录',
    icon: ProfileOutlined
  },
  {
    key: routeNames.fieldDictionary,
    routeName: routeNames.fieldDictionary,
    label: '字段字典',
    icon: TableOutlined
  },
  {
    key: routeNames.glossary,
    routeName: routeNames.glossary,
    label: '业务术语',
    icon: BookOutlined
  }
];

export function buildConsoleMenuItems(): MenuProps['items'] {
  return consoleNavigationItems.map((item) => ({
    key: item.key,
    icon: () => h(item.icon),
    label: item.label
  }));
}
