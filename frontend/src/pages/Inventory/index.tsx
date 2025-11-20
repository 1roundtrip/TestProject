import { useState } from 'react'
import { Outlet, useNavigate, useLocation } from 'react-router-dom'
import { Layout, Menu } from 'antd'
import {
  HomeOutlined,
  AppstoreOutlined,
  DatabaseOutlined,
  ArrowDownOutlined,
  ArrowUpOutlined,
  SwapOutlined,
  EditOutlined,
  FileSearchOutlined,
  WarningOutlined,
  BarChartOutlined,
} from '@ant-design/icons'
import type { MenuProps } from 'antd'

const { Sider, Content } = Layout

const inventoryMenuItems: MenuProps['items'] = [
  {
    key: '/inventory/warehouse',
    icon: <HomeOutlined />,
    label: '仓库管理',
  },
  {
    key: '/inventory/location',
    icon: <AppstoreOutlined />,
    label: '库位管理',
  },
  {
    key: '/inventory/material',
    icon: <DatabaseOutlined />,
    label: '库存物品',
  },
  {
    key: '/inventory/inbound',
    icon: <ArrowDownOutlined />,
    label: '入库管理',
  },
  {
    key: '/inventory/outbound',
    icon: <ArrowUpOutlined />,
    label: '出库管理',
  },
  {
    key: '/inventory/transfer',
    icon: <SwapOutlined />,
    label: '库存调拨',
  },
  {
    key: '/inventory/adjustment',
    icon: <EditOutlined />,
    label: '库存调整',
  },
  {
    key: '/inventory/stocktaking',
    icon: <FileSearchOutlined />,
    label: '库存盘点',
  },
  {
    key: '/inventory/warning',
    icon: <WarningOutlined />,
    label: '库存预警',
  },
  {
    key: '/inventory/report',
    icon: <BarChartOutlined />,
    label: '库存报表',
  },
]

export default function InventoryPage() {
  const navigate = useNavigate()
  const location = useLocation()
  const [selectedKey, setSelectedKey] = useState(() => {
    const path = location.pathname
    const menuItem = inventoryMenuItems.find(item => item?.key === path)
    return menuItem?.key as string || '/inventory/warehouse'
  })

  const handleMenuClick: MenuProps['onClick'] = (e) => {
    setSelectedKey(e.key)
    navigate(e.key)
  }

  return (
    <Layout style={{ minHeight: '100vh', background: 'transparent' }}>
      <Sider 
        width={200} 
        style={{ 
          background: '#ffffff', 
          borderRight: '1px solid #f3f4f6',
          borderRadius: '16px 0 0 16px',
          boxShadow: '0 1px 3px 0 rgba(0, 0, 0, 0.05)',
          marginRight: '16px',
        }}
      >
        <Menu
          mode="inline"
          selectedKeys={[selectedKey]}
          items={inventoryMenuItems}
          onClick={handleMenuClick}
          style={{ 
            height: '100%', 
            borderRight: 0,
            padding: '8px',
            background: 'transparent',
          }}
        />
      </Sider>
      <Content 
        style={{ 
          padding: '24px', 
          background: '#ffffff',
          borderRadius: '0 16px 16px 0',
          boxShadow: '0 1px 3px 0 rgba(0, 0, 0, 0.05)',
          flex: 1,
        }}
      >
        <Outlet />
      </Content>
    </Layout>
  )
}
