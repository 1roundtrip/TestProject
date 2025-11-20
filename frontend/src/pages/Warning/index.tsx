import { Outlet, useNavigate, useLocation } from 'react-router-dom'
import { Layout, Menu } from 'antd'
import {
  FileTextOutlined,
  RadarChartOutlined,
  BellOutlined,
  AuditOutlined,
  BarChartOutlined,
  FlagOutlined,
  FileOutlined,
  ApiOutlined,
  DashboardOutlined,
  LineChartOutlined,
} from '@ant-design/icons'

const { Sider, Content } = Layout

const warningMenuItems = [
  {
    key: '/warning/rule',
    icon: <FileTextOutlined />,
    label: '预警规则',
  },
  {
    key: '/warning/monitor',
    icon: <RadarChartOutlined />,
    label: '预警监控',
  },
  {
    key: '/warning/notification',
    icon: <BellOutlined />,
    label: '预警通知',
  },
  {
    key: '/warning/tracking',
    icon: <AuditOutlined />,
    label: '处理跟踪',
  },
  {
    key: '/warning/statistics',
    icon: <BarChartOutlined />,
    label: '统计分析',
  },
  {
    key: '/warning/level',
    icon: <FlagOutlined />,
    label: '预警级别',
  },
  {
    key: '/warning/template',
    icon: <FileOutlined />,
    label: '预警模板',
  },
  {
    key: '/warning/channel',
    icon: <ApiOutlined />,
    label: '预警渠道',
  },
  {
    key: '/warning/dashboard',
    icon: <DashboardOutlined />,
    label: '预警看板',
  },
  {
    key: '/warning/report',
    icon: <LineChartOutlined />,
    label: '预警报表',
  },
]

export default function WarningPage() {
  const navigate = useNavigate()
  const location = useLocation()

  const handleMenuClick = ({ key }: { key: string }) => {
    navigate(key)
  }

  return (
    <Layout style={{ minHeight: '100vh', background: '#fff' }}>
      <Sider width={200} style={{ background: '#fff', borderRight: '1px solid #f0f0f0' }}>
        <Menu
          mode="inline"
          selectedKeys={[location.pathname]}
          items={warningMenuItems}
          onClick={handleMenuClick}
          style={{ height: '100%', borderRight: 0 }}
        />
      </Sider>
      <Content style={{ padding: '24px', background: '#fff' }}>
        <Outlet />
      </Content>
    </Layout>
  )
}
