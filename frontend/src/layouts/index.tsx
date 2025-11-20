import { useState } from 'react'
import { Outlet, useNavigate, useLocation } from 'react-router-dom'
import { Layout, Menu, Avatar, Dropdown, Space } from 'antd'
import {
  DashboardOutlined,
  DatabaseOutlined,
  ShoppingCartOutlined,
  ToolOutlined,
  InboxOutlined,
  WarningOutlined,
  UserOutlined,
  LogoutOutlined,
  SettingOutlined,
  TeamOutlined,
  MenuFoldOutlined,
  MenuUnfoldOutlined,
} from '@ant-design/icons'
import { useAuthStore } from '@/store/authStore'
import type { MenuProps } from 'antd'

const { Header, Sider, Content } = Layout

const menuItems: MenuProps['items'] = [
  {
    key: '/dashboard',
    icon: <DashboardOutlined />,
    label: '仪表盘',
  },
  {
    key: '/asset',
    icon: <DatabaseOutlined />,
    label: '资产中心',
  },
  {
    key: '/purchase',
    icon: <ShoppingCartOutlined />,
    label: '采购中心',
  },
  {
    key: '/maintenance',
    icon: <ToolOutlined />,
    label: '维修管理',
  },
  {
    key: '/inventory',
    icon: <InboxOutlined />,
    label: '库存中心',
  },
  {
    key: '/warning',
    icon: <WarningOutlined />,
    label: '预警中心',
  },
  {
    key: 'system',
    icon: <SettingOutlined />,
    label: '系统管理',
    children: [
      {
        key: '/system/user',
        label: '用户管理',
      },
      {
        key: '/system/role',
        label: '角色管理',
      },
      {
        key: '/system/menu',
        label: '菜单管理',
      },
    ],
  },
]

export default function MainLayout() {
  const [collapsed, setCollapsed] = useState(false)
  const navigate = useNavigate()
  const location = useLocation()
  const { userInfo, logout } = useAuthStore()

  const handleMenuClick = ({ key }: { key: string }) => {
    navigate(key)
  }

  const handleLogout = () => {
    logout()
    navigate('/login')
  }

  const handleUserMenuClick = ({ key }: { key: string }) => {
    if (key === 'profile') {
      navigate('/profile')
    } else if (key === 'logout') {
      handleLogout()
    }
  }

  const userMenuItems: MenuProps['items'] = [
    {
      key: 'profile',
      icon: <UserOutlined />,
      label: '个人中心',
    },
    {
      type: 'divider',
    },
    {
      key: 'logout',
      icon: <LogoutOutlined />,
      label: '退出登录',
    },
  ]

  return (
    <Layout style={{ minHeight: '100vh', background: '#f5f7fa' }}>
      <Sider 
        trigger={null} 
        collapsible 
        collapsed={collapsed} 
        width={200}
        style={{
          background: '#ffffff',
          boxShadow: '2px 0 8px 0 rgba(0, 0, 0, 0.05)',
        }}
      >
        <div
          style={{
            height: 64,
            display: 'flex',
            alignItems: 'center',
            justifyContent: 'center',
            color: '#1f2937',
            fontSize: 18,
            fontWeight: 'bold',
            borderBottom: '1px solid #f3f4f6',
            background: '#ffffff',
          }}
        >
          {collapsed ? '煤矿ERP' : '智慧煤矿ERP'}
        </div>
        <Menu
          theme="light"
          mode="inline"
          selectedKeys={[location.pathname]}
          items={menuItems}
          onClick={handleMenuClick}
          style={{
            background: 'transparent',
            borderRight: 'none',
            padding: '8px',
          }}
        />
      </Sider>
      <Layout>
        <Header
          style={{
            padding: '0 24px',
            background: '#ffffff',
            display: 'flex',
            alignItems: 'center',
            justifyContent: 'space-between',
            boxShadow: '0 1px 2px 0 rgba(0, 0, 0, 0.03)',
            borderBottom: '1px solid #f3f4f6',
          }}
        >
          <div
            style={{ fontSize: 20, cursor: 'pointer' }}
            onClick={() => setCollapsed(!collapsed)}
          >
            {collapsed ? <MenuUnfoldOutlined /> : <MenuFoldOutlined />}
          </div>
          <Space>
            <Dropdown menu={{ items: userMenuItems, onClick: handleUserMenuClick }} placement="bottomRight">
              <Space style={{ cursor: 'pointer' }}>
                <Avatar icon={<UserOutlined />} />
                <span>{userInfo?.nickName || userInfo?.username || '用户'}</span>
              </Space>
            </Dropdown>
          </Space>
        </Header>
        <Content
          style={{
            margin: '24px',
            padding: 24,
            background: '#ffffff',
            borderRadius: 16,
            minHeight: 'calc(100vh - 112px)',
            boxShadow: '0 1px 3px 0 rgba(0, 0, 0, 0.05), 0 1px 2px 0 rgba(0, 0, 0, 0.03)',
            transition: 'all 0.3s ease',
          }}
        >
          <Outlet />
        </Content>
      </Layout>
    </Layout>
  )
}

