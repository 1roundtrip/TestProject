import { useState, useEffect } from 'react'
import { Table, Button, Space, Modal, Form, Input, DatePicker, Select, message, Popconfirm, Tag } from 'antd'
import { PlusOutlined, EditOutlined, DeleteOutlined, CheckOutlined, DownloadOutlined, SearchOutlined, UploadOutlined, SettingOutlined } from '@ant-design/icons'
import type { ColumnsType } from 'antd/es/table'
import { getReturnPage, createReturn, submitReturn, approveReturn, confirmReturn, deleteReturn, type PurchaseReturn } from '@/api/purchase/return'
import { exportToExcel } from '@/utils/export'
import dayjs from 'dayjs'
import { Permission } from '@/components/Permission'

export default function PurchaseReturnPage() {
  const [data, setData] = useState<PurchaseReturn[]>([])
  const [loading, setLoading] = useState(false)
  const [total, setTotal] = useState(0)
  const [current, setCurrent] = useState(1)
  const [pageSize, setPageSize] = useState(10)
  const [modalVisible, setModalVisible] = useState(false)
  const [editingRecord, setEditingRecord] = useState<PurchaseReturn | null>(null)
  const [form] = Form.useForm()

  useEffect(() => {
    loadData()
  }, [current, pageSize])

  const loadData = async () => {
    setLoading(true)
    try {
      const res = await getReturnPage({
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
      returnDate: dayjs(),
      returnType: 'QUALITY',
      status: 'DRAFT',
    })
    setModalVisible(true)
  }

  const handleEdit = (record: PurchaseReturn) => {
    setEditingRecord(record)
    form.setFieldsValue({
      ...record,
      returnDate: record.returnDate ? dayjs(record.returnDate) : null,
    })
    setModalVisible(true)
  }

  const handleSubmit = async () => {
    try {
      const values = await form.validateFields()
      const submitData = {
        return: {
          ...values,
          returnDate: values.returnDate ? values.returnDate.format('YYYY-MM-DD') : undefined,
        },
        details: [],
      }
      
      if (editingRecord?.returnId) {
        message.warning('编辑功能待实现')
      } else {
        await createReturn(submitData)
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
      await deleteReturn(id)
      message.success('删除成功')
      loadData()
    } catch (error: any) {
      message.error(error.response?.data?.msg || '删除失败')
    }
  }

  const handleSubmitReturn = async (id: number) => {
    try {
      await submitReturn(id)
      message.success('提交成功')
      loadData()
    } catch (error: any) {
      message.error(error.response?.data?.msg || '提交失败')
    }
  }

  const handleApprove = async (id: number) => {
    try {
      await approveReturn(id)
      message.success('审批通过')
      loadData()
    } catch (error: any) {
      message.error(error.response?.data?.msg || '审批失败')
    }
  }

  const handleConfirm = async (id: number) => {
    try {
      await confirmReturn(id)
      message.success('确认成功')
      loadData()
    } catch (error: any) {
      message.error(error.response?.data?.msg || '确认失败')
    }
  }

  const columns: ColumnsType<PurchaseReturn> = [
    {
      title: '退货单号',
      dataIndex: 'returnNo',
      key: 'returnNo',
    },
    {
      title: '收货单号',
      dataIndex: 'receivingNo',
      key: 'receivingNo',
    },
    {
      title: '供应商',
      dataIndex: 'supplierName',
      key: 'supplierName',
    },
    {
      title: '退货日期',
      dataIndex: 'returnDate',
      key: 'returnDate',
    },
    {
      title: '退货类型',
      dataIndex: 'returnType',
      key: 'returnType',
      render: (type) => {
        const typeMap: Record<string, string> = {
          QUALITY: '质量问题',
          QUANTITY: '数量错误',
          SPECIFICATION: '规格不符',
          OTHER: '其他',
        }
        return typeMap[type] || type
      },
    },
    {
      title: '退货金额',
      dataIndex: 'totalAmount',
      key: 'totalAmount',
      render: (amount) => amount ? `¥${amount.toFixed(2)}` : '-',
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
          CONFIRMED: { text: '已确认', color: 'success' },
          RETURNING: { text: '退货中', color: 'processing' },
          RETURNED: { text: '已退货', color: 'success' },
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
      width: 300,
      render: (_, record) => (
        <Space>
          {record.status === 'DRAFT' && (
            <>
              <Permission permission="purchase:return:edit">
                <Button type="link" onClick={() => handleEdit(record)}>编辑</Button>
              </Permission>
              <Permission permission="purchase:return:submit">
                <Button type="link" onClick={() => handleSubmitReturn(record.returnId!)}>提交</Button>
              </Permission>
              <Permission permission="purchase:return:remove">
                <Popconfirm title="确定要删除吗？" onConfirm={() => handleDelete(record.returnId!)}>
                  <Button type="link" danger>删除</Button>
                </Popconfirm>
              </Permission>
            </>
          )}
          {record.status === 'SUBMITTED' && (
            <>
              <Permission permission="purchase:return:approve">
                <Button type="link" onClick={() => handleApprove(record.returnId!)}>审批通过</Button>
              </Permission>
            </>
          )}
          {record.status === 'APPROVED' && (
            <>
              <Permission permission="purchase:return:confirm">
                <Button type="link" onClick={() => handleConfirm(record.returnId!)}>确认退货</Button>
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
          <Input placeholder="搜索退货单号" style={{ width: 200 }} allowClear />
          <Button icon={<SearchOutlined />}>搜索</Button>
        </Space>
        <Space>
          <Button 
            icon={<DownloadOutlined />} 
            onClick={() => exportToExcel(data, columns, '采购退货列表')}
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
          <Permission permission="purchase:return:add">
            <Button type="primary" icon={<PlusOutlined />} onClick={handleAdd}>
              新增退货单
            </Button>
          </Permission>
        </Space>
      </div>

      <Table
        columns={columns}
        dataSource={data}
        loading={loading}
        rowKey="returnId"
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
        title={editingRecord ? '编辑退货单' : '新增退货单'}
        open={modalVisible}
        onOk={handleSubmit}
        onCancel={() => setModalVisible(false)}
        width={800}
      >
        <Form form={form} layout="vertical">
          <Form.Item name="receivingNo" label="收货单号">
            <Input />
          </Form.Item>
          <Form.Item name="supplierName" label="供应商名称" rules={[{ required: true }]}>
            <Input />
          </Form.Item>
          <Form.Item name="returnDate" label="退货日期" rules={[{ required: true }]}>
            <DatePicker style={{ width: '100%' }} />
          </Form.Item>
          <Form.Item name="returnType" label="退货类型">
            <Select>
              <Select.Option value="QUALITY">质量问题</Select.Option>
              <Select.Option value="QUANTITY">数量错误</Select.Option>
              <Select.Option value="SPECIFICATION">规格不符</Select.Option>
              <Select.Option value="OTHER">其他</Select.Option>
            </Select>
          </Form.Item>
          <Form.Item name="returnReason" label="退货原因" rules={[{ required: true }]}>
            <Input.TextArea rows={4} />
          </Form.Item>
          <Form.Item name="logisticsCompany" label="物流公司">
            <Input />
          </Form.Item>
          <Form.Item name="logisticsNo" label="物流单号">
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

