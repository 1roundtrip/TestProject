import { useState, useEffect } from 'react'
import { Table, Button, Space, message, Modal, Form, Input, Select, Checkbox } from 'antd'
import { PlusOutlined, EditOutlined, DeleteOutlined, SettingOutlined } from '@ant-design/icons'
import type { ColumnsType } from 'antd/es/table'
import { Permission } from '@/components/Permission'
import { 
  getUserPage, 
  addUser, 
  updateUser, 
  deleteUser, 
  getUserRoles, 
  assignUserRoles,
  getRolePage,
  type SysUser,
  type SysRole 
} from '@/api/system'

export default function SystemUserPage() {
  const [data, setData] = useState<SysUser[]>([])
  const [loading, setLoading] = useState(false)
  const [modalVisible, setModalVisible] = useState(false)
  const [roleModalVisible, setRoleModalVisible] = useState(false)
  const [editingUser, setEditingUser] = useState<SysUser | null>(null)
  const [currentUserId, setCurrentUserId] = useState<number | null>(null)
  const [roles, setRoles] = useState<SysRole[]>([])
  const [form] = Form.useForm()
  const [roleForm] = Form.useForm()

  useEffect(() => {
    loadData()
    loadRoles()
  }, [])

  const loadData = async () => {
    setLoading(true)
    try {
      const res = await getUserPage({ current: 1, size: 100 })
      if (res.code === 200 && res.data) {
        setData(res.data.records || [])
      }
    } catch (error) {
      console.error('加载用户数据失败', error)
      message.error('加载数据失败')
    } finally {
      setLoading(false)
    }
  }

  const loadRoles = async () => {
    try {
      const res = await getRolePage({ current: 1, size: 100 })
      if (res.code === 200 && res.data) {
        setRoles(res.data.records || [])
      }
    } catch (error) {
      console.error('加载角色数据失败', error)
    }
  }

  const handleAdd = () => {
    setEditingUser(null)
    form.resetFields()
    setModalVisible(true)
  }

  const handleEdit = (record: SysUser) => {
    setEditingUser(record)
    form.setFieldsValue(record)
    setModalVisible(true)
  }

  const handleDelete = async (userId: number) => {
    Modal.confirm({
      title: '确认删除',
      content: '确定要删除这个用户吗？',
      onOk: async () => {
        try {
          const res = await deleteUser(userId)
          if (res.code === 200) {
            message.success('删除成功')
            loadData()
          } else {
            message.error(res.msg || '删除失败')
          }
        } catch (error) {
          console.error('删除用户失败', error)
          message.error('删除失败')
        }
      },
    })
  }

  const handleSetRoles = async (userId: number) => {
    setCurrentUserId(userId)
    try {
      const res = await getUserRoles(userId)
      if (res.code === 200 && res.data) {
        roleForm.setFieldsValue({ roleIds: res.data })
      }
      setRoleModalVisible(true)
    } catch (error) {
      console.error('加载用户角色失败', error)
      message.error('加载用户角色失败')
    }
  }

  const handleSubmit = async () => {
    try {
      const values = await form.validateFields()
      // 新增用户时，如果没有设置密码，使用默认密码
      if (!editingUser && !values.password) {
        values.password = '123456' // 默认密码
      }
      // 编辑用户时，如果密码为空，则不更新密码
      if (editingUser && !values.password) {
        delete values.password
      }
      
      let res
      if (editingUser) {
        // 更新用户：只发送需要更新的字段
        const updateData: SysUser = {
          userId: editingUser.userId,
          username: values.username || editingUser.username,
          nickName: values.nickName,
          email: values.email,
          phone: values.phone,
          status: values.status,
        }
        // 如果提供了新密码，则更新密码
        if (values.password) {
          updateData.password = values.password
        }
        res = await updateUser(updateData)
      } else {
        // 新增用户
        res = await addUser(values)
      }
      
      if (res.code === 200) {
        message.success(editingUser ? '更新成功' : '新增成功')
        setModalVisible(false)
        form.resetFields()
        loadData()
      } else {
        message.error(res.msg || (editingUser ? '更新失败' : '新增失败'))
      }
    } catch (error: any) {
      console.error('提交失败', error)
      message.error(error.response?.data?.msg || error.message || '提交失败')
    }
  }

  const handleRoleSubmit = async () => {
    try {
      const values = await roleForm.validateFields()
      if (!currentUserId) return
      
      const res = await assignUserRoles(currentUserId, values.roleIds || [])
      if (res.code === 200) {
        message.success('权限设置成功')
        setRoleModalVisible(false)
        roleForm.resetFields()
        loadData()
      } else {
        message.error(res.msg || '权限设置失败')
      }
    } catch (error) {
      console.error('提交失败', error)
    }
  }

  const columns: ColumnsType<SysUser> = [
    {
      title: '用户名',
      dataIndex: 'username',
      key: 'username',
    },
    {
      title: '昵称',
      dataIndex: 'nickName',
      key: 'nickName',
    },
    {
      title: '邮箱',
      dataIndex: 'email',
      key: 'email',
    },
    {
      title: '手机号',
      dataIndex: 'phone',
      key: 'phone',
    },
    {
      title: '状态',
      dataIndex: 'status',
      key: 'status',
      render: (status) => (status === '0' ? '正常' : '停用'),
    },
    {
      title: '操作',
      key: 'action',
      render: (_, record) => (
        <Space size="middle">
          <Permission permission="system:user:edit">
            <Button
              type="link"
              icon={<EditOutlined />}
              onClick={() => handleEdit(record)}
            >
              编辑
            </Button>
          </Permission>
          <Permission permission="system:user:edit">
            <Button
              type="link"
              icon={<SettingOutlined />}
              onClick={() => handleSetRoles(record.userId!)}
            >
              权限设置
            </Button>
          </Permission>
          <Permission permission="system:user:remove">
            <Button
              type="link"
              danger
              icon={<DeleteOutlined />}
              onClick={() => handleDelete(record.userId!)}
            >
              删除
            </Button>
          </Permission>
        </Space>
      ),
    },
  ]

  return (
    <div>
      <div style={{ marginBottom: 16 }}>
        <Permission permission="system:user:add">
          <Button type="primary" icon={<PlusOutlined />} onClick={handleAdd}>
            新增用户
          </Button>
        </Permission>
      </div>
      <Table
        columns={columns}
        dataSource={data}
        loading={loading}
        rowKey="userId"
      />
      <Modal
        title={editingUser ? '编辑用户' : '新增用户'}
        open={modalVisible}
        onOk={handleSubmit}
        onCancel={() => {
          setModalVisible(false)
          form.resetFields()
        }}
        width={600}
      >
        <Form
          form={form}
          layout="vertical"
          initialValues={{ status: '0' }}
        >
          <Form.Item
            name="username"
            label="用户名"
            rules={[{ required: true, message: '请输入用户名' }]}
          >
            <Input placeholder="请输入用户名" disabled={!!editingUser} />
          </Form.Item>
          {!editingUser && (
            <Form.Item
              name="password"
              label="密码"
              rules={[{ required: false, message: '请输入密码，不填则默认为123456' }]}
            >
              <Input.Password placeholder="请输入密码，不填则默认为123456" />
            </Form.Item>
          )}
          {editingUser && (
            <Form.Item
              name="password"
              label="密码"
              rules={[{ required: false }]}
            >
              <Input.Password placeholder="留空则不修改密码" />
            </Form.Item>
          )}
          <Form.Item
            name="nickName"
            label="昵称"
          >
            <Input placeholder="请输入昵称" />
          </Form.Item>
          <Form.Item
            name="email"
            label="邮箱"
          >
            <Input placeholder="请输入邮箱" />
          </Form.Item>
          <Form.Item
            name="phone"
            label="手机号"
          >
            <Input placeholder="请输入手机号" />
          </Form.Item>
          <Form.Item
            name="status"
            label="状态"
            rules={[{ required: true, message: '请选择状态' }]}
          >
            <Select>
              <Select.Option value="0">正常</Select.Option>
              <Select.Option value="1">停用</Select.Option>
            </Select>
          </Form.Item>
        </Form>
      </Modal>
      <Modal
        title="权限设置"
        open={roleModalVisible}
        onOk={handleRoleSubmit}
        onCancel={() => {
          setRoleModalVisible(false)
          roleForm.resetFields()
        }}
        width={600}
      >
        <Form
          form={roleForm}
          layout="vertical"
        >
          <Form.Item
            name="roleIds"
            label="角色"
          >
            <Checkbox.Group>
              {roles.map(role => {
                // 将常见的英文角色名称转换为中文
                const getRoleName = (name: string) => {
                  const nameMap: Record<string, string> = {
                    'Super Admin': '超级管理员',
                    'Admin': '管理员',
                    'User': '普通用户',
                    'Guest': '访客',
                  }
                  return nameMap[name] || name
                }
                return (
                  <Checkbox key={role.roleId} value={role.roleId}>
                    {getRoleName(role.roleName)}
                  </Checkbox>
                )
              })}
            </Checkbox.Group>
          </Form.Item>
        </Form>
      </Modal>
    </div>
  )
}





