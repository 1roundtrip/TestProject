import { useState } from 'react'
import { Outlet, useNavigate, useLocation } from 'react-router-dom'
import { Layout, Menu } from 'antd'
import {
  FileTextOutlined,
  CalendarOutlined,
  TeamOutlined,
  InboxOutlined,
  SafetyCertificateOutlined,
  DollarOutlined,
  WarningOutlined,
  TrophyOutlined,
  BarChartOutlined,
  MobileOutlined,
} from '@ant-design/icons'
import type { MenuProps } from 'antd'

const { Sider, Content } = Layout

const maintenanceMenuItems: MenuProps['items'] = [
  {
    key: '/maintenance/work-order',
    icon: <FileTextOutlined />,
    label: '维修工单',
  },
  {
    key: '/maintenance/plan',
    icon: <CalendarOutlined />,
    label: '维护计划',
  },
  {
    key: '/maintenance/team',
    icon: <TeamOutlined />,
    label: '维修团队',
  },
  {
    key: '/maintenance/part',
    icon: <InboxOutlined />,
    label: '维修备件',
  },
  {
    key: '/maintenance/quality',
    icon: <SafetyCertificateOutlined />,
    label: '质量管理',
  },
  {
    key: '/maintenance/cost',
    icon: <DollarOutlined />,
    label: '成本管理',
  },
  {
    key: '/maintenance/fault',
    icon: <WarningOutlined />,
    label: '故障分析',
  },
  {
    key: '/maintenance/performance',
    icon: <TrophyOutlined />,
    label: '绩效考核',
  },
  {
    key: '/maintenance/report',
    icon: <BarChartOutlined />,
    label: '报表分析',
  },
  {
    key: '/maintenance/mobile',
    icon: <MobileOutlined />,
    label: '移动维修',
  },
]

export default function MaintenancePage() {
  const navigate = useNavigate()
  const location = useLocation()
  
  const [selectedKey, setSelectedKey] = useState(() => {
    const path = location.pathname
    const menuItem = maintenanceMenuItems.find(item => item?.key === path)
    return menuItem?.key as string || '/maintenance/work-order'
  })

  const handleMenuClick: MenuProps['onClick'] = (e) => {
    setSelectedKey(e.key)
    navigate(e.key)
  }

  return (
    <Layout style={{ minHeight: '100vh' }}>
      <Sider width={200} style={{ background: '#fff' }}>
        <Menu
          mode="inline"
          selectedKeys={[selectedKey]}
          items={maintenanceMenuItems}
          onClick={handleMenuClick}
          style={{ height: '100%', borderRight: 0 }}
        />
      </Sider>
      <Layout style={{ padding: '0 24px 24px' }}>
        <Content
          style={{
            background: '#fff',
            padding: 24,
            margin: 0,
            minHeight: 280,
          }}
        >
          <Outlet />
        </Content>
      </Layout>
    </Layout>
  )
}

