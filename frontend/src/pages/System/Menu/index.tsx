import { useState, useEffect } from 'react'
import { Table, Button, Space, message, Modal, Form, Input, Select, InputNumber } from 'antd'
import { PlusOutlined, EditOutlined, DeleteOutlined } from '@ant-design/icons'
import type { ColumnsType } from 'antd/es/table'
import { Permission } from '@/components/Permission'
import { getMenuList, addMenu, updateMenu, deleteMenu, type SysMenu } from '@/api/system'

export default function SystemMenuPage() {
  const [data, setData] = useState<SysMenu[]>([])
  const [loading, setLoading] = useState(false)
  const [modalVisible, setModalVisible] = useState(false)
  const [editingMenu, setEditingMenu] = useState<SysMenu | null>(null)
  const [form] = Form.useForm()

  useEffect(() => {
    loadData()
  }, [])

  const loadData = async () => {
    setLoading(true)
    try {
      const res = await getMenuList()
      if (res.code === 200 && res.data) {
        setData(res.data || [])
      }
    } catch (error) {
      console.error('加载菜单数据失败', error)
      message.error('加载数据失败')
    } finally {
      setLoading(false)
    }
  }

  const handleAdd = () => {
    setEditingMenu(null)
    form.resetFields()
    setModalVisible(true)
  }

  const handleEdit = (record: SysMenu) => {
    setEditingMenu(record)
    form.setFieldsValue(record)
    setModalVisible(true)
  }

  const handleDelete = async (menuId: number) => {
    Modal.confirm({
      title: '确认删除',
      content: '确定要删除这个菜单吗？',
      onOk: async () => {
        try {
          const res = await deleteMenu(menuId)
          if (res.code === 200) {
            message.success('删除成功')
            loadData()
          } else {
            message.error(res.msg || '删除失败')
          }
        } catch (error) {
          console.error('删除菜单失败', error)
          message.error('删除失败')
        }
      },
    })
  }

  const handleSubmit = async () => {
    try {
      const values = await form.validateFields()
      const res = editingMenu
        ? await updateMenu({ ...editingMenu, ...values })
        : await addMenu(values)
      
      if (res.code === 200) {
        message.success(editingMenu ? '更新成功' : '新增成功')
        setModalVisible(false)
        form.resetFields()
        loadData()
      } else {
        message.error(res.msg || (editingMenu ? '更新失败' : '新增失败'))
      }
    } catch (error) {
      console.error('提交失败', error)
    }
  }

  const columns: ColumnsType<SysMenu> = [
    {
      title: '菜单名称',
      dataIndex: 'menuName',
      key: 'menuName',
    },
    {
      title: '权限标识',
      dataIndex: 'perms',
      key: 'perms',
    },
    {
      title: '菜单类型',
      dataIndex: 'menuType',
      key: 'menuType',
      render: (type) => {
        const typeMap: Record<string, string> = {
          M: '目录',
          C: '菜单',
          F: '按钮',
        }
        return typeMap[type] || type
      },
    },
    {
      title: '排序',
      dataIndex: 'orderNum',
      key: 'orderNum',
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
          <Permission permission="system:menu:edit">
            <Button
              type="link"
              icon={<EditOutlined />}
              onClick={() => handleEdit(record)}
            >
              编辑
            </Button>
          </Permission>
          <Permission permission="system:menu:remove">
            <Button
              type="link"
              danger
              icon={<DeleteOutlined />}
              onClick={() => handleDelete(record.menuId!)}
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
        <Permission permission="system:menu:add">
          <Button type="primary" icon={<PlusOutlined />} onClick={handleAdd}>
            新增菜单
          </Button>
        </Permission>
      </div>
      <Table
        columns={columns}
        dataSource={data}
        loading={loading}
        rowKey="menuId"
      />
      <Modal
        title={editingMenu ? '编辑菜单' : '新增菜单'}
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
          initialValues={{ status: '0', menuType: 'C', orderNum: 0, parentId: 0 }}
        >
          <Form.Item
            name="menuName"
            label="菜单名称"
            rules={[{ required: true, message: '请输入菜单名称' }]}
          >
            <Input placeholder="请输入菜单名称" />
          </Form.Item>
          <Form.Item
            name="menuType"
            label="菜单类型"
            rules={[{ required: true, message: '请选择菜单类型' }]}
          >
            <Select>
              <Select.Option value="M">目录</Select.Option>
              <Select.Option value="C">菜单</Select.Option>
              <Select.Option value="F">按钮</Select.Option>
            </Select>
          </Form.Item>
          <Form.Item
            name="perms"
            label="权限标识"
          >
            <Input placeholder="请输入权限标识" />
          </Form.Item>
          <Form.Item
            name="orderNum"
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





