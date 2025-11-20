import { useState, useEffect } from 'react'
import { Table, Button, Space, Modal, Form, Input, DatePicker, Select, message, Popconfirm, Tag } from 'antd'
import { PlusOutlined, EditOutlined, DeleteOutlined, CheckOutlined, DownloadOutlined, SearchOutlined, UploadOutlined, SettingOutlined } from '@ant-design/icons'
import type { ColumnsType } from 'antd/es/table'
import { getReceivingPage, createReceiving, confirmReceiving, deleteReceiving, createReceivingFromOrder, type PurchaseReceiving } from '@/api/purchase/receiving'
import { exportToExcel } from '@/utils/export'
import dayjs from 'dayjs'
import { Permission } from '@/components/Permission'

export default function PurchaseReceivingPage() {
  const [data, setData] = useState<PurchaseReceiving[]>([])
  const [loading, setLoading] = useState(false)
  const [total, setTotal] = useState(0)
  const [current, setCurrent] = useState(1)
  const [pageSize, setPageSize] = useState(10)
  const [modalVisible, setModalVisible] = useState(false)
  const [editingRecord, setEditingRecord] = useState<PurchaseReceiving | null>(null)
  const [form] = Form.useForm()

  useEffect(() => {
    loadData()
  }, [current, pageSize])

  const loadData = async () => {
    setLoading(true)
    try {
      const res = await getReceivingPage({
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
      receivingDate: dayjs(),
      status: 'DRAFT',
    })
    setModalVisible(true)
  }

  const handleEdit = (record: PurchaseReceiving) => {
    setEditingRecord(record)
    form.setFieldsValue({
      ...record,
      receivingDate: record.receivingDate ? dayjs(record.receivingDate) : null,
    })
    setModalVisible(true)
  }

  const handleSubmit = async () => {
    try {
      const values = await form.validateFields()
      const submitData = {
        receiving: {
          ...values,
          receivingDate: values.receivingDate ? values.receivingDate.format('YYYY-MM-DD') : undefined,
        },
        details: [],
      }
      
      if (editingRecord?.receivingId) {
        message.warning('编辑功能待实现')
      } else {
        await createReceiving(submitData)
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
      await deleteReceiving(id)
      message.success('删除成功')
      loadData()
    } catch (error: any) {
      message.error(error.response?.data?.msg || '删除失败')
    }
  }

  const handleConfirm = async (id: number) => {
    try {
      await confirmReceiving(id)
      message.success('确认成功')
      loadData()
    } catch (error: any) {
      message.error(error.response?.data?.msg || '确认失败')
    }
  }

  const columns: ColumnsType<PurchaseReceiving> = [
    {
      title: '收货单号',
      dataIndex: 'receivingNo',
      key: 'receivingNo',
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
      title: '收货日期',
      dataIndex: 'receivingDate',
      key: 'receivingDate',
    },
    {
      title: '仓库',
      dataIndex: 'warehouse',
      key: 'warehouse',
    },
    {
      title: '总金额',
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
          CONFIRMED: { text: '已确认', color: 'success' },
          QUALITY_CHECKING: { text: '质检中', color: 'processing' },
          QUALITY_PASSED: { text: '质检通过', color: 'success' },
          QUALITY_FAILED: { text: '质检不合格', color: 'error' },
          STORED: { text: '已入库', color: 'success' },
          CANCELLED: { text: '已取消', color: 'default' },
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
          {record.status === 'DRAFT' && (
            <>
              <Permission permission="purchase:receiving:edit">
                <Button type="link" onClick={() => handleEdit(record)}>编辑</Button>
              </Permission>
              <Permission permission="purchase:receiving:confirm">
                <Button type="link" onClick={() => handleConfirm(record.receivingId!)}>确认收货</Button>
              </Permission>
              <Permission permission="purchase:receiving:remove">
                <Popconfirm title="确定要删除吗？" onConfirm={() => handleDelete(record.receivingId!)}>
                  <Button type="link" danger>删除</Button>
                </Popconfirm>
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
          <Input placeholder="搜索收货单号" style={{ width: 200 }} allowClear />
          <Button icon={<SearchOutlined />}>搜索</Button>
        </Space>
        <Space>
          <Button 
            icon={<DownloadOutlined />} 
            onClick={() => exportToExcel(data, columns, '采购收货列表')}
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
          <Permission permission="purchase:receiving:add">
            <Button type="primary" icon={<PlusOutlined />} onClick={handleAdd}>
              新增收货单
            </Button>
          </Permission>
        </Space>
      </div>

      <Table
        columns={columns}
        dataSource={data}
        loading={loading}
        rowKey="receivingId"
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
        title={editingRecord ? '编辑收货单' : '新增收货单'}
        open={modalVisible}
        onOk={handleSubmit}
        onCancel={() => setModalVisible(false)}
        width={800}
      >
        <Form form={form} layout="vertical">
          <Form.Item name="orderNo" label="订单号">
            <Input />
          </Form.Item>
          <Form.Item name="supplierName" label="供应商名称">
            <Input />
          </Form.Item>
          <Form.Item name="receivingDate" label="收货日期" rules={[{ required: true }]}>
            <DatePicker style={{ width: '100%' }} />
          </Form.Item>
          <Form.Item name="warehouse" label="仓库">
            <Input />
          </Form.Item>
          <Form.Item name="location" label="存放位置">
            <Input />
          </Form.Item>
          <Form.Item name="deliveryNo" label="送货单号">
            <Input />
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

