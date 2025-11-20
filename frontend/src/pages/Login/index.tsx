import { useState } from 'react'
import { useNavigate } from 'react-router-dom'
import { Form, Input, Button, Card, message } from 'antd'
import { UserOutlined, LockOutlined } from '@ant-design/icons'
import { login } from '@/api/auth'
import { useAuthStore } from '@/store/authStore'
import './index.css'

export default function Login() {
  const navigate = useNavigate()
  const { setToken, setUserInfo, setPermissions } = useAuthStore()
  const [loading, setLoading] = useState(false)

  const onFinish = async (values: { username: string; password: string }) => {
    setLoading(true)
    try {
      console.log('开始登录请求:', { username: values.username })
      const res = await login(values)
      console.log('登录成功响应:', res)
      
      // 检查响应结构
      if (!res || !res.data || !res.data.token) {
        console.error('登录响应格式错误:', res)
        message.error('登录失败：响应格式错误')
        return
      }
      
      setToken(res.data.token)
      
      // 保存用户信息
      if (res.data.userInfo) {
        setUserInfo({
          userId: res.data.userInfo.userId,
          username: res.data.userInfo.username,
          nickName: res.data.userInfo.nickName || res.data.userInfo.username,
          avatar: res.data.userInfo.avatar,
        })
      } else {
        // 兼容旧版本，使用默认值
        setUserInfo({
          userId: 1,
          username: values.username,
          nickName: values.username,
        })
      }
      
      // 保存权限信息
      console.log('登录响应中的权限数据:', res.data.permissions)
      console.log('完整登录响应:', JSON.stringify(res.data, null, 2))
      
      if (res.data.permissions && Array.isArray(res.data.permissions)) {
        console.log('设置权限列表:', res.data.permissions)
        setPermissions(res.data.permissions)
      } else if (res.data.userInfo?.permissions && Array.isArray(res.data.userInfo.permissions)) {
        // 尝试从 userInfo 中获取权限
        console.log('从 userInfo 中获取权限:', res.data.userInfo.permissions)
        setPermissions(res.data.userInfo.permissions)
      } else {
        console.warn('登录响应中没有找到权限信息，设置为空数组')
        setPermissions([])
      }
      
      message.success('登录成功')
      navigate('/dashboard')
    } catch (error: any) {
      console.error('登录捕获错误:', {
        message: error?.message,
        code: error?.code,
        response: error?.response?.data
      })
      
      // 响应拦截器已经显示了错误消息，这里不再重复显示
      // 但如果响应拦截器没有处理，这里作为备用
      if (!error?.code) {
        const errorMsg = error?.response?.data?.msg || error?.message || '登录失败，请检查用户名和密码'
        message.error(errorMsg)
      }
    } finally {
      setLoading(false)
    }
  }

  return (
    <div className="login-container">
      <Card className="login-card" title="智慧煤矿ERP管理系统">
        <Form
          name="login"
          onFinish={onFinish}
          autoComplete="off"
          size="large"
        >
          <Form.Item
            name="username"
            rules={[{ required: true, message: '请输入用户名' }]}
          >
            <Input
              prefix={<UserOutlined />}
              placeholder="用户名"
            />
          </Form.Item>

          <Form.Item
            name="password"
            rules={[{ required: true, message: '请输入密码' }]}
          >
            <Input.Password
              prefix={<LockOutlined />}
              placeholder="密码"
            />
          </Form.Item>

          <Form.Item>
            <Button
              type="primary"
              htmlType="submit"
              block
              loading={loading}
              style={{ background: '#3b82f6', borderColor: '#3b82f6' }}
            >
              登录
            </Button>
          </Form.Item>
        </Form>
      </Card>
    </div>
  )
}











