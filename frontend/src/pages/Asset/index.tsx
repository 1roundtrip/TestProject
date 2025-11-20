import { useState } from 'react'
import { Outlet, useNavigate, useLocation } from 'react-router-dom'
import { Layout, Menu } from 'antd'
import {
  DatabaseOutlined,
  InboxOutlined,
  ExportOutlined,
  SwapOutlined,
  CalculatorOutlined,
  FileSearchOutlined,
  DeleteOutlined,
  BarChartOutlined,
  ToolOutlined,
} from '@ant-design/icons'
import type { MenuProps } from 'antd'

const { Sider, Content } = Layout

const assetMenuItems: MenuProps['items'] = [
  {
    key: '/asset/archive',
    icon: <DatabaseOutlined />,
    label: '资产档案',
  },
  {
    key: '/asset/storage',
    icon: <InboxOutlined />,
    label: '资产入库',
  },
  {
    key: '/asset/borrow',
    icon: <ExportOutlined />,
    label: '资产领用退库',
  },
  {
    key: '/asset/transfer',
    icon: <SwapOutlined />,
    label: '资产转移调拨',
  },
  {
    key: '/asset/repair',
    icon: <ToolOutlined />,
    label: '资产维修管理',
  },
  {
    key: '/asset/depreciation',
    icon: <CalculatorOutlined />,
    label: '资产折旧管理',
  },
  {
    key: '/asset/inventory',
    icon: <FileSearchOutlined />,
    label: '资产盘点管理',
  },
  {
    key: '/asset/scrap',
    icon: <DeleteOutlined />,
    label: '资产报废管理',
  },
  {
    key: '/asset/report',
    icon: <BarChartOutlined />,
    label: '资产报表分析',
  },
]

export default function AssetPage() {
  const navigate = useNavigate()
  const location = useLocation()
  const [selectedKey, setSelectedKey] = useState(location.pathname)

  const handleMenuClick = ({ key }: { key: string }) => {
    setSelectedKey(key)
    navigate(key)
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
          items={assetMenuItems}
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
