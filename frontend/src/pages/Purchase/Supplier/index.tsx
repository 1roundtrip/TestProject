import { useState, useEffect } from 'react'
import { Table, Button, Space, Modal, Form, Input, InputNumber, Select, message, Popconfirm, Tag } from 'antd'
import { PlusOutlined, EditOutlined, DeleteOutlined, DownloadOutlined, SearchOutlined, StarOutlined, UploadOutlined, SettingOutlined } from '@ant-design/icons'
import type { ColumnsType } from 'antd/es/table'
import { getSupplierPage, addSupplier, updateSupplier, deleteSupplier, evaluateSupplier, getSupplierEvaluations, type PurchaseSupplier, type PurchaseSupplierEvaluation } from '@/api/purchase/supplier'
import { exportToExcel } from '@/utils/export'
import { Permission } from '@/components/Permission'

export default function PurchaseSupplierPage() {
  const [data, setData] = useState<PurchaseSupplier[]>([])
  const [loading, setLoading] = useState(false)
  const [total, setTotal] = useState(0)
  const [current, setCurrent] = useState(1)
  const [pageSize, setPageSize] = useState(10)
  const [modalVisible, setModalVisible] = useState(false)
  const [evaluationModalVisible, setEvaluationModalVisible] = useState(false)
  const [editingRecord, setEditingRecord] = useState<PurchaseSupplier | null>(null)
  const [selectedSupplierId, setSelectedSupplierId] = useState<number | null>(null)
  const [form] = Form.useForm()
  const [evaluationForm] = Form.useForm()

  useEffect(() => {
    loadData()
  }, [current, pageSize])

  const loadData = async () => {
    setLoading(true)
    try {
      const res = await getSupplierPage({
        current,
        size: pageSize,
      })
      setData(res.data.records)
      setTotal(res.data.total)
    } catch (error) {
      message.error('加载数据失败')
    } finally {
      setLoading(false)
    }
  }

  const handleAdd = () => {
    setEditingRecord(null)
    form.resetFields()
    form.setFieldsValue({
      status: 'ACTIVE',
      currency: 'CNY',
    })
    setModalVisible(true)
  }

  const handleEdit = (record: PurchaseSupplier) => {
    setEditingRecord(record)
    form.setFieldsValue(record)
    setModalVisible(true)
  }

  const handleSubmit = async () => {
    try {
      const values = await form.validateFields()
      if (editingRecord?.supplierId) {
        await updateSupplier({ ...values, supplierId: editingRecord.supplierId })
        message.success('更新成功')
      } else {
        await addSupplier(values)
        message.success('创建成功')
      }
      setModalVisible(false)
      loadData()
    } catch (error: any) {
      message.error(error.response?.data?.msg || '操作失败')
    }
  }

  const handleDelete = async (id: number) => {
    try {
      await deleteSupplier(id)
      message.success('删除成功')
      loadData()
    } catch (error: any) {
      message.error(error.response?.data?.msg || '删除失败')
    }
  }

  const handleEvaluate = (record: PurchaseSupplier) => {
    setSelectedSupplierId(record.supplierId!)
    evaluationForm.resetFields()
    setEvaluationModalVisible(true)
  }

  const handleEvaluationSubmit = async () => {
    try {
      const values = await evaluationForm.validateFields()
      await evaluateSupplier(selectedSupplierId!, values)
      message.success('评价成功')
      setEvaluationModalVisible(false)
      loadData()
    } catch (error: any) {
      message.error(error.response?.data?.msg || '评价失败')
    }
  }

  const columns: ColumnsType<PurchaseSupplier> = [
    {
      title: '供应商编码',
      dataIndex: 'supplierCode',
      key: 'supplierCode',
    },
    {
      title: '供应商名称',
      dataIndex: 'supplierName',
      key: 'supplierName',
    },
    {
      title: '供应商类型',
      dataIndex: 'supplierType',
      key: 'supplierType',
      render: (type) => {
        const typeMap: Record<string, string> = {
          MAIN: '主要供应商',
          AUXILIARY: '辅助供应商',
          STRATEGIC: '战略供应商',
        }
        return typeMap[type] || type
      },
    },
    {
      title: '信用等级',
      dataIndex: 'creditLevel',
      key: 'creditLevel',
      render: (level) => {
        const levelMap: Record<string, { text: string; color: string }> = {
          AAA: { text: 'AAA', color: 'green' },
          AA: { text: 'AA', color: 'blue' },
          A: { text: 'A', color: 'cyan' },
          B: { text: 'B', color: 'orange' },
        }
        const info = levelMap[level] || { text: level, color: 'default' }
        return <Tag color={info.color}>{info.text}</Tag>
      },
    },
    {
      title: '综合评分',
      dataIndex: 'totalRating',
      key: 'totalRating',
      render: (rating) => rating ? (
        <span>
          <StarOutlined style={{ color: '#faad14' }} /> {rating.toFixed(1)}
        </span>
      ) : '-',
    },
    {
      title: '联系人',
      dataIndex: 'contactPerson',
      key: 'contactPerson',
    },
    {
      title: '联系电话',
      dataIndex: 'contactPhone',
      key: 'contactPhone',
    },
    {
      title: '状态',
      dataIndex: 'status',
      key: 'status',
      render: (status) => {
        const statusMap: Record<string, { text: string; color: string }> = {
          ACTIVE: { text: '启用', color: 'success' },
          INACTIVE: { text: '停用', color: 'default' },
          BLACKLIST: { text: '黑名单', color: 'error' },
        }
        const info = statusMap[status] || { text: status, color: 'default' }
        return <Tag color={info.color}>{info.text}</Tag>
      },
    },
    {
      title: '操作',
      key: 'action',
      width: 250,
      render: (_, record) => (
        <Space>
          <Permission permission="purchase:supplier:edit">
            <Button type="link" onClick={() => handleEdit(record)}>编辑</Button>
          </Permission>
          <Permission permission="purchase:supplier:evaluate">
            <Button type="link" onClick={() => handleEvaluate(record)}>评价</Button>
          </Permission>
          <Permission permission="purchase:supplier:remove">
            <Popconfirm title="确定要删除吗？" onConfirm={() => handleDelete(record.supplierId!)}>
              <Button type="link" danger>删除</Button>
            </Popconfirm>
          </Permission>
        </Space>
      ),
    },
  ]

  return (
    <div>
      <div style={{ marginBottom: 16, display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
        <Space>
          <Input placeholder="搜索供应商名称" style={{ width: 200 }} allowClear />
          <Button icon={<SearchOutlined />}>搜索</Button>
        </Space>
        <Space>
          <Button 
            icon={<DownloadOutlined />} 
            onClick={() => exportToExcel(data, columns, '供应商列表')}
            disabled={data.length === 0}
          >
            导出Excel
          </Button>
          <Button 
            icon={<UploadOutlined />}
            onClick={() => message.info('导入功能开发中')}
          >
            导入Excel
          </Button>
          <Button 
            icon={<SettingOutlined />}
            onClick={() => message.info('设置功能开发中')}
          >
            设置
          </Button>
          <Permission permission="purchase:supplier:add">
            <Button type="primary" icon={<PlusOutlined />} onClick={handleAdd}>
              新增供应商
            </Button>
          </Permission>
        </Space>
      </div>

      <Table
        columns={columns}
        dataSource={data}
        loading={loading}
        rowKey="supplierId"
        pagination={{
          current,
          pageSize,
          total,
          showSizeChanger: true,
          showTotal: (total) => `共 ${total} 条`,
          onChange: (page, size) => {
            setCurrent(page)
            setPageSize(size)
          },
        }}
      />

      <Modal
        title={editingRecord ? '编辑供应商' : '新增供应商'}
        open={modalVisible}
        onOk={handleSubmit}
        onCancel={() => setModalVisible(false)}
        width={800}
      >
        <Form form={form} layout="vertical">
          <Form.Item name="supplierCode" label="供应商编码">
            <Input />
          </Form.Item>
          <Form.Item name="supplierName" label="供应商名称" rules={[{ required: true }]}>
            <Input />
          </Form.Item>
          <Form.Item name="supplierType" label="供应商类型">
            <Select>
              <Select.Option value="MAIN">主要供应商</Select.Option>
              <Select.Option value="AUXILIARY">辅助供应商</Select.Option>
              <Select.Option value="STRATEGIC">战略供应商</Select.Option>
            </Select>
          </Form.Item>
          <Form.Item name="creditLevel" label="信用等级">
            <Select>
              <Select.Option value="AAA">AAA</Select.Option>
              <Select.Option value="AA">AA</Select.Option>
              <Select.Option value="A">A</Select.Option>
              <Select.Option value="B">B</Select.Option>
            </Select>
          </Form.Item>
          <Form.Item name="contactPerson" label="联系人">
            <Input />
          </Form.Item>
          <Form.Item name="contactPhone" label="联系电话">
            <Input />
          </Form.Item>
          <Form.Item name="contactEmail" label="联系邮箱">
            <Input />
          </Form.Item>
          <Form.Item name="address" label="地址">
            <Input.TextArea rows={2} />
          </Form.Item>
          <Form.Item name="status" label="状态">
            <Select>
              <Select.Option value="ACTIVE">启用</Select.Option>
              <Select.Option value="INACTIVE">停用</Select.Option>
              <Select.Option value="BLACKLIST">黑名单</Select.Option>
            </Select>
          </Form.Item>
          <Form.Item name="remark" label="备注">
            <Input.TextArea rows={3} />
          </Form.Item>
        </Form>
      </Modal>

      <Modal
        title="评价供应商"
        open={evaluationModalVisible}
        onOk={handleEvaluationSubmit}
        onCancel={() => setEvaluationModalVisible(false)}
        width={600}
      >
        <Form form={evaluationForm} layout="vertical">
          <Form.Item name="qualityScore" label="质量评分(0-10)" rules={[{ required: true }]}>
            <InputNumber style={{ width: '100%' }} min={0} max={10} precision={1} />
          </Form.Item>
          <Form.Item name="deliveryScore" label="交货评分(0-10)" rules={[{ required: true }]}>
            <InputNumber style={{ width: '100%' }} min={0} max={10} precision={1} />
          </Form.Item>
          <Form.Item name="serviceScore" label="服务评分(0-10)" rules={[{ required: true }]}>
            <InputNumber style={{ width: '100%' }} min={0} max={10} precision={1} />
          </Form.Item>
          <Form.Item name="priceScore" label="价格评分(0-10)" rules={[{ required: true }]}>
            <InputNumber style={{ width: '100%' }} min={0} max={10} precision={1} />
          </Form.Item>
          <Form.Item name="evaluationContent" label="评价内容">
            <Input.TextArea rows={4} />
          </Form.Item>
        </Form>
      </Modal>
    </div>
  )
}

