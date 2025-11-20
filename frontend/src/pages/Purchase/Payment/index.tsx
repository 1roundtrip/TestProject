import { useState, useEffect } from 'react'
import { Table, Button, Space, Modal, Form, Input, InputNumber, DatePicker, Select, message, Popconfirm, Tag } from 'antd'
import { PlusOutlined, EditOutlined, DeleteOutlined, CheckOutlined, DownloadOutlined, SearchOutlined, UploadOutlined, SettingOutlined } from '@ant-design/icons'
import type { ColumnsType } from 'antd/es/table'
import { getPaymentPage, createPayment, submitPayment, approvePayment, confirmPayment, deletePayment, createPaymentFromOrder, type PurchasePayment } from '@/api/purchase/payment'
import { exportToExcel } from '@/utils/export'
import dayjs from 'dayjs'
import { Permission } from '@/components/Permission'

export default function PurchasePaymentPage() {
  const [data, setData] = useState<PurchasePayment[]>([])
  const [loading, setLoading] = useState(false)
  const [total, setTotal] = useState(0)
  const [current, setCurrent] = useState(1)
  const [pageSize, setPageSize] = useState(10)
  const [modalVisible, setModalVisible] = useState(false)
  const [editingRecord, setEditingRecord] = useState<PurchasePayment | null>(null)
  const [form] = Form.useForm()

  useEffect(() => {
    loadData()
  }, [current, pageSize])

  const loadData = async () => {
    setLoading(true)
    try {
      const res = await getPaymentPage({
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
      paymentDate: dayjs(),
      paymentType: 'PROGRESS',
      paymentMethod: 'TRANSFER',
      currency: 'CNY',
      status: 'DRAFT',
    })
    setModalVisible(true)
  }

  const handleEdit = (record: PurchasePayment) => {
    setEditingRecord(record)
    form.setFieldsValue({
      ...record,
      paymentDate: record.paymentDate ? dayjs(record.paymentDate) : null,
    })
    setModalVisible(true)
  }

  const handleSubmit = async () => {
    try {
      const values = await form.validateFields()
      const submitData = {
        payment: {
          ...values,
          paymentDate: values.paymentDate ? values.paymentDate.format('YYYY-MM-DD') : undefined,
        },
        details: [],
      }
      
      if (editingRecord?.paymentId) {
        message.warning('编辑功能待实现')
      } else {
        await createPayment(submitData)
        message.success('创建成功')
        setModalVisible(false)
        loadData()
      }
    } catch (error: any) {
      message.error(error.response?.data?.msg || '操作失败')
    }
  }

  const handleDelete = async (id: number) => {
    try {
      await deletePayment(id)
      message.success('删除成功')
      loadData()
    } catch (error: any) {
      message.error(error.response?.data?.msg || '删除失败')
    }
  }

  const handleSubmitPayment = async (id: number) => {
    try {
      await submitPayment(id)
      message.success('提交成功')
      loadData()
    } catch (error: any) {
      message.error(error.response?.data?.msg || '提交失败')
    }
  }

  const handleApprove = async (id: number) => {
    try {
      await approvePayment(id)
      message.success('审批通过')
      loadData()
    } catch (error: any) {
      message.error(error.response?.data?.msg || '审批失败')
    }
  }

  const handleConfirm = async (id: number) => {
    try {
      await confirmPayment(id)
      message.success('付款成功')
      loadData()
    } catch (error: any) {
      message.error(error.response?.data?.msg || '付款失败')
    }
  }

  const columns: ColumnsType<PurchasePayment> = [
    {
      title: '付款单号',
      dataIndex: 'paymentNo',
      key: 'paymentNo',
    },
    {
      title: '订单号',
      dataIndex: 'orderNo',
      key: 'orderNo',
    },
    {
      title: '供应商',
      dataIndex: 'supplierName',
      key: 'supplierName',
    },
    {
      title: '付款类型',
      dataIndex: 'paymentType',
      key: 'paymentType',
      render: (type) => {
        const typeMap: Record<string, string> = {
          ADVANCE: '预付款',
          PROGRESS: '进度款',
          FINAL: '尾款',
          OTHER: '其他',
        }
        return typeMap[type] || type
      },
    },
    {
      title: '付款金额',
      dataIndex: 'paymentAmount',
      key: 'paymentAmount',
      render: (amount) => amount ? `¥${amount.toFixed(2)}` : '-',
    },
    {
      title: '付款日期',
      dataIndex: 'paymentDate',
      key: 'paymentDate',
    },
    {
      title: '状态',
      dataIndex: 'status',
      key: 'status',
      render: (status) => {
        const statusMap: Record<string, { text: string; color: string }> = {
          DRAFT: { text: '草稿', color: 'default' },
          SUBMITTED: { text: '已提交', color: 'processing' },
          APPROVED: { text: '已审批', color: 'success' },
          PAID: { text: '已付款', color: 'success' },
          REJECTED: { text: '已驳回', color: 'error' },
          CANCELLED: { text: '已取消', color: 'default' },
        }
        const info = statusMap[status] || { text: status, color: 'default' }
        return <Tag color={info.color}>{info.text}</Tag>
      },
    },
    {
      title: '操作',
      key: 'action',
      width: 350,
      render: (_, record) => (
        <Space>
          {record.status === 'DRAFT' && (
            <>
              <Permission permission="purchase:payment:edit">
                <Button type="link" onClick={() => handleEdit(record)}>编辑</Button>
              </Permission>
              <Permission permission="purchase:payment:submit">
                <Button type="link" onClick={() => handleSubmitPayment(record.paymentId!)}>提交</Button>
              </Permission>
              <Permission permission="purchase:payment:remove">
                <Popconfirm title="确定要删除吗？" onConfirm={() => handleDelete(record.paymentId!)}>
                  <Button type="link" danger>删除</Button>
                </Popconfirm>
              </Permission>
            </>
          )}
          {record.status === 'SUBMITTED' && (
            <>
              <Permission permission="purchase:payment:approve">
                <Button type="link" onClick={() => handleApprove(record.paymentId!)}>审批通过</Button>
              </Permission>
            </>
          )}
          {record.status === 'APPROVED' && (
            <>
              <Permission permission="purchase:payment:confirm">
                <Button type="link" onClick={() => handleConfirm(record.paymentId!)}>确认付款</Button>
              </Permission>
            </>
          )}
        </Space>
      ),
    },
  ]

  return (
    <div>
      <div style={{ marginBottom: 16, display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
        <Space>
          <Input placeholder="搜索付款单号" style={{ width: 200 }} allowClear />
          <Button icon={<SearchOutlined />}>搜索</Button>
        </Space>
        <Space>
          <Button 
            icon={<DownloadOutlined />} 
            onClick={() => exportToExcel(data, columns, '采购付款列表')}
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
          <Permission permission="purchase:payment:add">
            <Button type="primary" icon={<PlusOutlined />} onClick={handleAdd}>
              新增付款单
            </Button>
          </Permission>
        </Space>
      </div>

      <Table
        columns={columns}
        dataSource={data}
        loading={loading}
        rowKey="paymentId"
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
        title={editingRecord ? '编辑付款单' : '新增付款单'}
        open={modalVisible}
        onOk={handleSubmit}
        onCancel={() => setModalVisible(false)}
        width={800}
      >
        <Form form={form} layout="vertical">
          <Form.Item name="orderNo" label="订单号">
            <Input />
          </Form.Item>
          <Form.Item name="supplierName" label="供应商名称" rules={[{ required: true }]}>
            <Input />
          </Form.Item>
          <Form.Item name="paymentType" label="付款类型">
            <Select>
              <Select.Option value="ADVANCE">预付款</Select.Option>
              <Select.Option value="PROGRESS">进度款</Select.Option>
              <Select.Option value="FINAL">尾款</Select.Option>
              <Select.Option value="OTHER">其他</Select.Option>
            </Select>
          </Form.Item>
          <Form.Item name="paymentDate" label="付款日期" rules={[{ required: true }]}>
            <DatePicker style={{ width: '100%' }} />
          </Form.Item>
          <Form.Item name="paymentMethod" label="付款方式">
            <Select>
              <Select.Option value="TRANSFER">转账</Select.Option>
              <Select.Option value="CHECK">支票</Select.Option>
              <Select.Option value="CASH">现金</Select.Option>
              <Select.Option value="OTHER">其他</Select.Option>
            </Select>
          </Form.Item>
          <Form.Item name="paymentAmount" label="付款金额" rules={[{ required: true }]}>
            <InputNumber style={{ width: '100%' }} min={0} precision={2} />
          </Form.Item>
          <Form.Item name="bankName" label="付款银行">
            <Input />
          </Form.Item>
          <Form.Item name="bankAccount" label="付款账号">
            <Input />
          </Form.Item>
          <Form.Item name="accountName" label="账户名称">
            <Input />
          </Form.Item>
          <Form.Item name="remark" label="备注">
            <Input.TextArea rows={3} />
          </Form.Item>
        </Form>
      </Modal>
    </div>
  )
}

