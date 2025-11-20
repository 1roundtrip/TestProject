import { useState, useEffect } from 'react'
import { Card, Descriptions, Avatar, Button, Form, Input, message, Tabs } from 'antd'
import { UserOutlined, LockOutlined } from '@ant-design/icons'
import { useAuthStore } from '@/store/authStore'
import { changePassword } from '@/api/auth'

export default function ProfilePage() {
  const { userInfo } = useAuthStore()
  const [form] = Form.useForm()
  const [passwordForm] = Form.useForm()

  useEffect(() => {
    if (userInfo) {
      form.setFieldsValue({
        username: userInfo.username,
        nickName: userInfo.nickName || userInfo.username,
        email: userInfo.email || '',
        phone: userInfo.phone || '',
      })
    }
  }, [userInfo, form])

  const handleSubmit = async (values: any) => {
    try {
      // TODO: 调用API更新用户信息
      message.success('个人信息更新成功')
    } catch (error) {
      message.error('更新失败')
    }
  }

  const handlePasswordSubmit = async (values: any) => {
    try {
      if (!userInfo?.userId) {
        message.error('用户信息不完整')
        return
      }
      
      await changePassword({
        userId: userInfo.userId,
        oldPassword: values.oldPassword,
        newPassword: values.newPassword,
      })
      
      message.success('密码修改成功')
      passwordForm.resetFields()
    } catch (error: any) {
      message.error(error.response?.data?.msg || error.message || '密码修改失败')
    }
  }

  return (
    <div>
      <Card title="个人中心" style={{ marginBottom: 16 }}>
        <div style={{ textAlign: 'center', marginBottom: 24 }}>
          <Avatar size={100} icon={<UserOutlined />} />
          <div style={{ marginTop: 16, fontSize: 18, fontWeight: 'bold' }}>
            {userInfo?.nickName || userInfo?.username || '用户'}
          </div>
        </div>

        <Descriptions title="基本信息" bordered column={1}>
          <Descriptions.Item label="用户名">{userInfo?.username || '-'}</Descriptions.Item>
          <Descriptions.Item label="昵称">{userInfo?.nickName || '-'}</Descriptions.Item>
          <Descriptions.Item label="邮箱">{userInfo?.email || '-'}</Descriptions.Item>
          <Descriptions.Item label="手机号">{userInfo?.phone || '-'}</Descriptions.Item>
        </Descriptions>
      </Card>

      <Card>
        <Tabs
          defaultActiveKey="info"
          items={[
            {
              key: 'info',
              label: '修改信息',
              icon: <UserOutlined />,
              children: (
                <Form
                  form={form}
                  layout="vertical"
                  onFinish={handleSubmit}
                  style={{ maxWidth: 600 }}
                >
                  <Form.Item
                    name="nickName"
                    label="昵称"
                    rules={[{ required: true, message: '请输入昵称' }]}
                  >
                    <Input placeholder="请输入昵称" />
                  </Form.Item>

                  <Form.Item
                    name="email"
                    label="邮箱"
                    rules={[{ type: 'email', message: '请输入有效的邮箱地址' }]}
                  >
                    <Input placeholder="请输入邮箱" />
                  </Form.Item>

                  <Form.Item
                    name="phone"
                    label="手机号"
                    rules={[{ pattern: /^1[3-9]\d{9}$/, message: '请输入有效的手机号' }]}
                  >
                    <Input placeholder="请输入手机号" />
                  </Form.Item>

                  <Form.Item>
                    <Button type="primary" htmlType="submit">
                      保存修改
                    </Button>
                  </Form.Item>
                </Form>
              ),
            },
            {
              key: 'password',
              label: '修改密码',
              icon: <LockOutlined />,
              children: (
                <Form
                  form={passwordForm}
                  layout="vertical"
                  onFinish={handlePasswordSubmit}
                  style={{ maxWidth: 600 }}
                >
                  <Form.Item
                    name="oldPassword"
                    label="原密码"
                    rules={[{ required: true, message: '请输入原密码' }]}
                  >
                    <Input.Password placeholder="请输入原密码" />
                  </Form.Item>

                  <Form.Item
                    name="newPassword"
                    label="新密码"
                    rules={[
                      { required: true, message: '请输入新密码' },
                      { min: 6, message: '密码长度不能少于6位' },
                    ]}
                  >
                    <Input.Password placeholder="请输入新密码（至少6位）" />
                  </Form.Item>

                  <Form.Item
                    name="confirmPassword"
                    label="确认新密码"
                    dependencies={['newPassword']}
                    rules={[
                      { required: true, message: '请确认新密码' },
                      ({ getFieldValue }) => ({
                        validator(_, value) {
                          if (!value || getFieldValue('newPassword') === value) {
                            return Promise.resolve()
                          }
                          return Promise.reject(new Error('两次输入的密码不一致'))
                        },
                      }),
                    ]}
                  >
                    <Input.Password placeholder="请再次输入新密码" />
                  </Form.Item>

                  <Form.Item>
                    <Button type="primary" htmlType="submit">
                      修改密码
                    </Button>
                  </Form.Item>
                </Form>
              ),
            },
          ]}
        />
      </Card>
    </div>
  )
}

