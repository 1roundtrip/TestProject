import { ThemeConfig } from 'antd'

/**
 * 矿用蓝主题配置 - 优化版（更柔和的颜色和圆滑的边框）
 */
export const theme: ThemeConfig = {
  token: {
    // 主色 - 使用更柔和的蓝色
    colorPrimary: '#4f8ff7',
    colorSuccess: '#22c55e',
    colorWarning: '#f59e0b',
    colorError: '#f87171',
    colorInfo: '#60a5fa',
    
    // 背景色 - 使用更柔和的渐变背景
    colorBgBase: '#ffffff',
    colorBgContainer: '#f5f7fa',
    colorBgElevated: '#ffffff',
    colorBgLayout: '#f0f2f5',
    
    // 文字色 - 更柔和的文字颜色
    colorText: '#1f2937',
    colorTextSecondary: '#6b7280',
    colorTextTertiary: '#9ca3af',
    
    // 边框 - 更柔和的边框颜色和更大的圆角
    colorBorder: '#e5e7eb',
    colorBorderSecondary: '#f3f4f6',
    borderRadius: 12,
    borderRadiusLG: 16,
    borderRadiusSM: 8,
    
    // 字体
    fontFamily: '-apple-system, BlinkMacSystemFont, "Segoe UI", Roboto, "Helvetica Neue", Arial, sans-serif',
    fontSize: 14,
    
    // 阴影 - 更柔和的阴影
    boxShadow: '0 1px 3px 0 rgba(0, 0, 0, 0.05), 0 1px 2px 0 rgba(0, 0, 0, 0.03)',
    boxShadowSecondary: '0 4px 6px -1px rgba(0, 0, 0, 0.05), 0 2px 4px -1px rgba(0, 0, 0, 0.03)',
  },
  components: {
    Layout: {
      bodyBg: '#f5f7fa',
      headerBg: '#ffffff',
      siderBg: '#ffffff',
      headerPadding: '0 24px',
      headerHeight: 64,
    },
    Menu: {
      itemBg: 'transparent',
      itemSelectedBg: '#eff6ff',
      itemHoverBg: '#f3f4f6',
      itemActiveBg: '#eff6ff',
      itemSelectedColor: '#4f8ff7',
      itemColor: '#4b5563',
      borderRadius: 8,
      subMenuItemBg: 'transparent',
    },
    Button: {
      primaryColor: '#ffffff',
      borderRadius: 8,
      controlHeight: 36,
    },
    Card: {
      borderRadius: 16,
      paddingLG: 24,
      boxShadow: '0 1px 3px 0 rgba(0, 0, 0, 0.05), 0 1px 2px 0 rgba(0, 0, 0, 0.03)',
    },
    Table: {
      borderRadius: 12,
      headerBg: '#f9fafb',
      headerColor: '#374151',
      rowHoverBg: '#f9fafb',
    },
    Input: {
      borderRadius: 8,
      paddingBlock: 8,
      paddingInline: 12,
    },
    Select: {
      borderRadius: 8,
    },
    Modal: {
      borderRadius: 16,
    },
    Drawer: {
      borderRadius: 16,
    },
  },
}











