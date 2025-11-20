import { useState, useEffect } from 'react'
import { Table, Button, Space, message, Modal, Form, Input, Select, InputNumber } from 'antd'
import { PlusOutlined, EditOutlined, DeleteOutlined } from '@ant-design/icons'
import type { ColumnsType } from 'antd/es/table'
import { Permission } from '@/components/Permission'
import { getRolePage, addRole, updateRole, deleteRole, type SysRole } from '@/api/system'

export default function SystemRolePage() {
  const [data, setData] = useState<SysRole[]>([])
  const [loading, setLoading] = useState(false)
  const [modalVisible, setModalVisible] = useState(false)
  const [editingRole, setEditingRole] = useState<SysRole | null>(null)
  const [form] = Form.useForm()

  useEffect(() => {
    loadData()
  }, [])

  const loadData = async () => {
    setLoading(true)
    try {
      const res = await getRolePage({ current: 1, size: 100 })
      if (res.code === 200 && res.data) {
        setData(res.data.records || [])
      }
    } catch (error) {
      console.error('加载角色数据失败', error)
      message.error('加载数据失败')
    } finally {
      setLoading(false)
    }
  }

  const handleAdd = () => {
    setEditingRole(null)
    form.resetFields()
    setModalVisible(true)
  }

  const handleEdit = (record: SysRole) => {
    setEditingRole(record)
    form.setFieldsValue(record)
    setModalVisible(true)
  }

  const handleDelete = async (roleId: number) => {
    Modal.confirm({
      title: '确认删除',
      content: '确定要删除这个角色吗？',
      onOk: async () => {
        try {
          const res = await deleteRole(roleId)
          if (res.code === 200) {
            message.success('删除成功')
            loadData()
          } else {
            message.error(res.msg || '删除失败')
          }
        } catch (error) {
          console.error('删除角色失败', error)
          message.error('删除失败')
        }
      },
    })
  }

  const handleSubmit = async () => {
    try {
      const values = await form.validateFields()
      const res = editingRole
        ? await updateRole({ ...editingRole, ...values })
        : await addRole(values)
      
      if (res.code === 200) {
        message.success(editingRole ? '更新成功' : '新增成功')
        setModalVisible(false)
        form.resetFields()
        loadData()
      } else {
        message.error(res.msg || (editingRole ? '更新失败' : '新增失败'))
      }
    } catch (error) {
      console.error('提交失败', error)
    }
  }

  const columns: ColumnsType<SysRole> = [
    {
      title: '角色名称',
      dataIndex: 'roleName',
      key: 'roleName',
      render: (text) => {
        // 将常见的英文角色名称转换为中文
        const nameMap: Record<string, string> = {
          'Super Admin': '超级管理员',
          'Admin': '管理员',
          'User': '普通用户',
          'Guest': '访客',
        }
        return nameMap[text] || text
      },
    },
    {
      title: '角色标识',
      dataIndex: 'roleKey',
      key: 'roleKey',
      render: (text) => {
        // 将常见的英文角色标识转换为中文
        const keyMap: Record<string, string> = {
          'admin': '管理员',
          'user': '普通用户',
          'guest': '访客',
          'super_admin': '超级管理员',
        }
        return keyMap[text] || text
      },
    },
    {
      title: '排序',
      dataIndex: 'roleSort',
      key: 'roleSort',
    },
    {
      title: '状态',
      dataIndex: 'status',
      key: 'status',
      render: (status) => {
        if (status === '0' || status === '正常' || status === 'ACTIVE') {
          return <span style={{ color: '#52c41a' }}>正常</span>
        }
        if (status === '1' || status === '停用' || status === 'DISABLED' || status === 'INACTIVE') {
          return <span style={{ color: '#999' }}>停用</span>
        }
        return status
      },
    },
    {
      title: '备注',
      dataIndex: 'remark',
      key: 'remark',
    },
    {
      title: '操作',
      key: 'action',
      render: (_, record) => (
        <Space size="middle">
          <Permission permission="system:role:edit">
            <Button
              type="link"
              icon={<EditOutlined />}
              onClick={() => handleEdit(record)}
            >
              编辑
            </Button>
          </Permission>
          <Permission permission="system:role:remove">
            <Button
              type="link"
              danger
              icon={<DeleteOutlined />}
              onClick={() => handleDelete(record.roleId!)}
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
        <Permission permission="system:role:add">
          <Button type="primary" icon={<PlusOutlined />} onClick={handleAdd}>
            新增角色
          </Button>
        </Permission>
      </div>
      <Table
        columns={columns}
        dataSource={data}
        loading={loading}
        rowKey="roleId"
        locale={{
          emptyText: '暂无数据',
        }}
      />
      <Modal
        title={editingRole ? '编辑角色' : '新增角色'}
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
          initialValues={{ status: '0', roleSort: 0 }}
        >
          <Form.Item
            name="roleName"
            label="角色名称"
            rules={[{ required: true, message: '请输入角色名称' }]}
          >
            <Input placeholder="请输入角色名称" />
          </Form.Item>
          <Form.Item
            name="roleKey"
            label="角色标识"
            rules={[{ required: true, message: '请输入角色标识' }]}
          >
            <Input placeholder="请输入角色标识" />
          </Form.Item>
          <Form.Item
            name="roleSort"
            label="排序"
          >
            <InputNumber min={0} style={{ width: '100%' }} />
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
          <Form.Item
            name="remark"
            label="备注"
          >
            <Input.TextArea rows={3} placeholder="请输入备注" />
          </Form.Item>
        </Form>
      </Modal>
    </div>
  )
}





