import { useState, useEffect } from 'react'
import { Table, Button, Space, Modal, Form, Input, InputNumber, DatePicker, Select, message, Popconfirm, Tag, Upload, Drawer } from 'antd'
import { PlusOutlined, EditOutlined, DeleteOutlined, CheckOutlined, DownloadOutlined, SearchOutlined, UploadOutlined, SettingOutlined } from '@ant-design/icons'
import type { ColumnsType } from 'antd/es/table'
import { getOrderPage, createOrder, submitOrder, approveOrder, confirmOrder, deleteOrder, createOrderFromRequisition, type PurchaseOrder } from '@/api/purchase/order'
import { exportToExcel } from '@/utils/export'
import { parseExcelFile, downloadImportTemplate, validateImportData } from '@/utils/import'
import dayjs from 'dayjs'
import { Permission } from '@/components/Permission'
import type { UploadFile } from 'antd/es/upload/interface'

export default function PurchaseOrderPage() {
  const [data, setData] = useState<PurchaseOrder[]>([])
  const [loading, setLoading] = useState(false)
  const [total, setTotal] = useState(0)
  const [current, setCurrent] = useState(1)
  const [pageSize, setPageSize] = useState(10)
  const [modalVisible, setModalVisible] = useState(false)
  const [editingRecord, setEditingRecord] = useState<PurchaseOrder | null>(null)
  const [form] = Form.useForm()
  const [importModalVisible, setImportModalVisible] = useState(false)
  const [settingDrawerVisible, setSettingDrawerVisible] = useState(false)
  const [importFileList, setImportFileList] = useState<UploadFile[]>([])

  useEffect(() => {
    loadData()
  }, [current, pageSize])

  const loadData = async () => {
    setLoading(true)
    try {
      const res = await getOrderPage({
        current,
        size: pageSize,
      })
      setData(res.data.records || [])
      setTotal(res.data.total || 0)
    } catch (error: any) {
      console.error('加载数据失败:', error)
      message.error(error.response?.data?.msg || '加载数据失败')
      setData([])
      setTotal(0)
    } finally {
      setLoading(false)
    }
  }

  const handleAdd = () => {
    setEditingRecord(null)
    form.resetFields()
    form.setFieldsValue({
      orderDate: dayjs(),
      orderType: 'NORMAL',
      currency: 'CNY',
      status: 'DRAFT',
    })
    setModalVisible(true)
  }

  const handleEdit = (record: PurchaseOrder) => {
    setEditingRecord(record)
    form.setFieldsValue({
      ...record,
      orderDate: record.orderDate ? dayjs(record.orderDate) : null,
      deliveryDate: record.deliveryDate ? dayjs(record.deliveryDate) : null,
    })
    setModalVisible(true)
  }

  const handleSubmit = async () => {
    try {
      const values = await form.validateFields()
      const submitData = {
        order: {
          ...values,
          orderDate: values.orderDate ? values.orderDate.format('YYYY-MM-DD') : undefined,
          deliveryDate: values.deliveryDate ? values.deliveryDate.format('YYYY-MM-DD') : undefined,
        },
        details: [],
      }
      
      if (editingRecord?.orderId) {
        message.warning('编辑功能待实现')
      } else {
        await createOrder(submitData)
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
      await deleteOrder(id)
      message.success('删除成功')
      loadData()
    } catch (error: any) {
      message.error(error.response?.data?.msg || '删除失败')
    }
  }

  const handleSubmitOrder = async (id: number) => {
    try {
      await submitOrder(id)
      message.success('提交成功')
      loadData()
    } catch (error: any) {
      message.error(error.response?.data?.msg || '提交失败')
    }
  }

  const handleApprove = async (id: number) => {
    try {
      await approveOrder(id)
      message.success('审批通过')
      loadData()
    } catch (error: any) {
      message.error(error.response?.data?.msg || '审批失败')
    }
  }

  const handleConfirm = async (id: number) => {
    try {
      await confirmOrder(id)
      message.success('确认成功')
      loadData()
    } catch (error: any) {
      message.error(error.response?.data?.msg || '确认失败')
    }
  }

  // 处理导入
  const handleImport = async (file: File) => {
    try {
      const data = await parseExcelFile(file)
      
      // 验证数据
      const validation = validateImportData(data, ['订单编号', '供应商名称'])
      if (!validation.valid) {
        message.error(`数据验证失败：${validation.errors.join('; ')}`)
        return false
      }

      // 转换数据格式
      const importData = data.map(row => ({
        orderNo: row['订单编号'] || row['orderNo'],
        supplierName: row['供应商名称'] || row['supplierName'],
        orderDate: row['订单日期'] || row['orderDate'],
        deliveryDate: row['交货日期'] || row['deliveryDate'],
        orderType: row['订单类型'] || row['orderType'] || 'NORMAL',
        currency: row['币种'] || row['currency'] || 'CNY',
        status: row['状态'] || row['status'] || 'DRAFT',
        deliveryAddress: row['交货地址'] || row['deliveryAddress'],
        deliveryMethod: row['交货方式'] || row['deliveryMethod'],
        paymentTerms: row['付款条件'] || row['paymentTerms'],
        remark: row['备注'] || row['remark'],
      }))

      // 批量导入
      let successCount = 0
      let failCount = 0
      for (const item of importData) {
        try {
          await createOrder({
            order: {
              ...item,
              orderDate: item.orderDate ? dayjs(item.orderDate).format('YYYY-MM-DD') : undefined,
              deliveryDate: item.deliveryDate ? dayjs(item.deliveryDate).format('YYYY-MM-DD') : undefined,
            },
            details: [],
          })
          successCount++
        } catch (error) {
          failCount++
          console.error('导入失败:', item, error)
        }
      }

      message.success(`导入完成：成功 ${successCount} 条，失败 ${failCount} 条`)
      setImportModalVisible(false)
      setImportFileList([])
      loadData()
      return false
    } catch (error) {
      message.error('导入失败：' + (error instanceof Error ? error.message : '未知错误'))
      return false
    }
  }

  // 下载导入模板
  const handleDownloadTemplate = () => {
    const templateColumns = [
      { title: '订单编号', dataIndex: 'orderNo' },
      { title: '供应商名称', dataIndex: 'supplierName' },
      { title: '订单日期', dataIndex: 'orderDate' },
      { title: '交货日期', dataIndex: 'deliveryDate' },
      { title: '订单类型', dataIndex: 'orderType' },
      { title: '币种', dataIndex: 'currency' },
      { title: '状态', dataIndex: 'status' },
      { title: '交货地址', dataIndex: 'deliveryAddress' },
      { title: '交货方式', dataIndex: 'deliveryMethod' },
      { title: '付款条件', dataIndex: 'paymentTerms' },
      { title: '备注', dataIndex: 'remark' },
    ]
    downloadImportTemplate(templateColumns, '采购订单导入模板')
  }

  const columns: ColumnsType<PurchaseOrder> = [
    {
      title: '订单编号',
      dataIndex: 'orderNo',
      key: 'orderNo',
    },
    {
      title: '供应商',
      dataIndex: 'supplierName',
      key: 'supplierName',
    },
    {
      title: '订单日期',
      dataIndex: 'orderDate',
      key: 'orderDate',
    },
    {
      title: '交货日期',
      dataIndex: 'deliveryDate',
      key: 'deliveryDate',
    },
    {
      title: '订单总额',
      dataIndex: 'totalAmountWithTax',
      key: 'totalAmountWithTax',
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
          EXECUTING: { text: '执行中', color: 'processing' },
          PARTIAL_RECEIVED: { text: '部分收货', color: 'warning' },
          RECEIVED: { text: '已收货', color: 'success' },
          COMPLETED: { text: '已完成', color: 'success' },
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
              <Permission permission="purchase:order:edit">
                <Button type="link" onClick={() => handleEdit(record)}>编辑</Button>
              </Permission>
              <Permission permission="purchase:order:submit">
                <Button type="link" onClick={() => handleSubmitOrder(record.orderId!)}>提交</Button>
              </Permission>
              <Permission permission="purchase:order:remove">
                <Popconfirm title="确定要删除吗？" onConfirm={() => handleDelete(record.orderId!)}>
                  <Button type="link" danger>删除</Button>
                </Popconfirm>
              </Permission>
            </>
          )}
          {record.status === 'SUBMITTED' && (
            <>
              <Permission permission="purchase:order:approve">
                <Button type="link" onClick={() => handleApprove(record.orderId!)}>审批通过</Button>
              </Permission>
            </>
          )}
          {record.status === 'APPROVED' && (
            <>
              <Permission permission="purchase:order:confirm">
                <Button type="link" onClick={() => handleConfirm(record.orderId!)}>确认订单</Button>
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
          <Input placeholder="搜索订单编号" style={{ width: 200 }} allowClear />
          <Button icon={<SearchOutlined />}>搜索</Button>
        </Space>
        <Space>
          <Button 
            icon={<DownloadOutlined />} 
            onClick={() => exportToExcel(data, columns, '采购订单列表')}
            disabled={data.length === 0}
          >
            导出Excel
          </Button>
          <Button 
            icon={<UploadOutlined />}
            onClick={() => setImportModalVisible(true)}
          >
            导入Excel
          </Button>
          <Button 
            icon={<SettingOutlined />}
            onClick={() => setSettingDrawerVisible(true)}
          >
            设置
          </Button>
          <Permission permission="purchase:order:add">
            <Button type="primary" icon={<PlusOutlined />} onClick={handleAdd}>
              新增订单
            </Button>
          </Permission>
        </Space>
      </div>

      <Table
        columns={columns}
        dataSource={data}
        loading={loading}
        rowKey="orderId"
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
        title={editingRecord ? '编辑采购订单' : '新增采购订单'}
        open={modalVisible}
        onOk={handleSubmit}
        onCancel={() => setModalVisible(false)}
        width={800}
      >
        <Form form={form} layout="vertical">
          <Form.Item name="supplierName" label="供应商名称" rules={[{ required: true }]}>
            <Input />
          </Form.Item>
          <Form.Item name="orderType" label="订单类型">
            <Select>
              <Select.Option value="NORMAL">普通订单</Select.Option>
              <Select.Option value="URGENT">紧急订单</Select.Option>
              <Select.Option value="CONTRACT">合同订单</Select.Option>
            </Select>
          </Form.Item>
          <Form.Item name="orderDate" label="订单日期" rules={[{ required: true }]}>
            <DatePicker style={{ width: '100%' }} />
          </Form.Item>
          <Form.Item name="deliveryDate" label="交货日期">
            <DatePicker style={{ width: '100%' }} />
          </Form.Item>
          <Form.Item name="deliveryAddress" label="交货地址">
            <Input.TextArea rows={2} />
          </Form.Item>
          <Form.Item name="deliveryMethod" label="交货方式">
            <Select>
              <Select.Option value="EXPRESS">快递</Select.Option>
              <Select.Option value="LOGISTICS">物流</Select.Option>
              <Select.Option value="SELF">自提</Select.Option>
            </Select>
          </Form.Item>
          <Form.Item name="paymentTerms" label="付款条件">
            <Input />
          </Form.Item>
          <Form.Item name="currency" label="币种">
            <Select>
              <Select.Option value="CNY">人民币</Select.Option>
              <Select.Option value="USD">美元</Select.Option>
            </Select>
          </Form.Item>
          <Form.Item name="remark" label="备注">
            <Input.TextArea rows={3} />
          </Form.Item>
        </Form>
      </Modal>

      {/* 导入Modal */}
      <Modal
        title="导入采购订单数据"
        open={importModalVisible}
        onCancel={() => {
          setImportModalVisible(false)
          setImportFileList([])
        }}
        footer={null}
        width={600}
      >
        <Space direction="vertical" style={{ width: '100%' }} size="large">
          <div>
            <Button onClick={handleDownloadTemplate} type="link">
              下载导入模板
            </Button>
            <p style={{ marginTop: 8, color: '#666' }}>
              请先下载模板，按照模板格式填写数据后上传
            </p>
          </div>
          <Upload
            fileList={importFileList}
            beforeUpload={(file) => {
              handleImport(file)
              return false
            }}
            onChange={({ fileList }) => setImportFileList(fileList)}
            accept=".xlsx,.xls,.csv"
            maxCount={1}
          >
            <Button icon={<UploadOutlined />}>选择Excel文件</Button>
          </Upload>
        </Space>
      </Modal>

      {/* 设置抽屉 */}
      <Drawer
        title="页面设置"
        placement="right"
        onClose={() => setSettingDrawerVisible(false)}
        open={settingDrawerVisible}
        width={400}
      >
        <Space direction="vertical" style={{ width: '100%' }} size="large">
          <div>
            <h4>显示设置</h4>
            <Space direction="vertical" style={{ width: '100%' }}>
              <Form.Item label="每页显示条数">
                <Select
                  value={pageSize}
                  onChange={(value) => {
                    setPageSize(value)
                    setCurrent(1)
                  }}
                  style={{ width: '100%' }}
                >
                  <Select.Option value={10}>10条/页</Select.Option>
                  <Select.Option value={20}>20条/页</Select.Option>
                  <Select.Option value={50}>50条/页</Select.Option>
                  <Select.Option value={100}>100条/页</Select.Option>
                </Select>
              </Form.Item>
            </Space>
          </div>
          <div>
            <h4>数据管理</h4>
            <Space direction="vertical" style={{ width: '100%' }}>
              <Button block onClick={() => {
                handleDownloadTemplate()
                setSettingDrawerVisible(false)
              }}>
                下载导入模板
              </Button>
              <Button block onClick={() => {
                exportToExcel(data, columns, '采购订单列表')
                setSettingDrawerVisible(false)
              }} disabled={data.length === 0}>
                导出当前数据
              </Button>
            </Space>
          </div>
        </Space>
      </Drawer>
    </div>
  )
}

