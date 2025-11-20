import { useState } from 'react'
import { Outlet, useNavigate, useLocation } from 'react-router-dom'
import { Layout, Menu } from 'antd'
import {
  FileTextOutlined,
  FileAddOutlined,
  TeamOutlined,
  ShoppingCartOutlined,
  FileDoneOutlined,
  InboxOutlined,
  SafetyCertificateOutlined,
  RollbackOutlined,
  DollarOutlined,
  BarChartOutlined,
} from '@ant-design/icons'
import type { MenuProps } from 'antd'

const { Sider, Content } = Layout

const purchaseMenuItems: MenuProps['items'] = [
  {
    key: '/purchase/plan',
    icon: <FileTextOutlined />,
    label: '采购计划',
  },
  {
    key: '/purchase/requisition',
    icon: <FileAddOutlined />,
    label: '采购申请',
  },
  {
    key: '/purchase/supplier',
    icon: <TeamOutlined />,
    label: '供应商管理',
  },
  {
    key: '/purchase/order',
    icon: <ShoppingCartOutlined />,
    label: '采购订单',
  },
  {
    key: '/purchase/contract',
    icon: <FileDoneOutlined />,
    label: '采购合同',
  },
  {
    key: '/purchase/receiving',
    icon: <InboxOutlined />,
    label: '采购收货',
  },
  {
    key: '/purchase/quality',
    icon: <SafetyCertificateOutlined />,
    label: '采购质检',
  },
  {
    key: '/purchase/return',
    icon: <RollbackOutlined />,
    label: '采购退货',
  },
  {
    key: '/purchase/payment',
    icon: <DollarOutlined />,
    label: '采购付款',
  },
  {
    key: '/purchase/report',
    icon: <BarChartOutlined />,
    label: '采购报表',
  },
]

export default function PurchasePage() {
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
          items={purchaseMenuItems}
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
