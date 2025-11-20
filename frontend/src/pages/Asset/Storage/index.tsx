import { useState, useEffect } from 'react'
import { Table, Button, Space, Modal, Form, Input, InputNumber, DatePicker, Select, message, Popconfirm, Tag, Drawer } from 'antd'
import { PlusOutlined, EditOutlined, DeleteOutlined, CheckOutlined, CloseOutlined, SettingOutlined, DownloadOutlined, SearchOutlined } from '@ant-design/icons'
import type { ColumnsType } from 'antd/es/table'
import { getStoragePage, createStorage, confirmStorage, cancelStorage, deleteStorage, getStorageDetails, type AssetStorage, type AssetStorageDetail } from '@/api/asset/storage'
import { exportToExcel } from '@/utils/export'
import dayjs from 'dayjs'

export default function AssetStoragePage() {
  const [data, setData] = useState<AssetStorage[]>([])
  const [loading, setLoading] = useState(false)
  const [total, setTotal] = useState(0)
  const [current, setCurrent] = useState(1)
  const [pageSize, setPageSize] = useState(10)
  const [modalVisible, setModalVisible] = useState(false)
  const [detailModalVisible, setDetailModalVisible] = useState(false)
  const [editingRecord, setEditingRecord] = useState<AssetStorage | null>(null)
  const [details, setDetails] = useState<AssetStorageDetail[]>([])
  const [form] = Form.useForm()
  const [detailForm] = Form.useForm()

  useEffect(() => {
    loadData()
  }, [current, pageSize])

  const loadData = async () => {
    setLoading(true)
    try {
      const res = await getStoragePage({
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
    detailForm.resetFields()
    setDetails([])
    form.setFieldsValue({
      storageType: 'PURCHASE',
      storageDate: dayjs(),
      status: 'DRAFT',
    })
    setModalVisible(true)
  }

  const handleEdit = (record: AssetStorage) => {
    setEditingRecord(record)
    form.setFieldsValue({
      ...record,
      storageDate: record.storageDate ? dayjs(record.storageDate) : null,
    })
    loadDetails(record.storageId!)
    setModalVisible(true)
  }

  const loadDetails = async (storageId: number) => {
    try {
      const res = await getStorageDetails(storageId)
      setDetails(res.data)
    } catch (error) {
      console.error('加载明细失败', error)
    }
  }

  const handleViewDetails = async (record: AssetStorage) => {
    await loadDetails(record.storageId!)
    setDetailModalVisible(true)
  }

  const handleSubmit = async () => {
    try {
      const values = await form.validateFields()
      const submitData = {
        storage: {
          ...values,
          storageDate: values.storageDate ? values.storageDate.format('YYYY-MM-DD') : undefined,
        },
        details: details.map(d => ({
          ...d,
          purchaseDate: d.purchaseDate ? d.purchaseDate : undefined,
        })),
      }
      
      if (editingRecord?.storageId) {
        message.warning('编辑功能待实现')
      } else {
        await createStorage(submitData)
        message.success('创建成功')
        setModalVisible(false)
        loadData()
      }
    } catch (error) {
      console.error('提交失败', error)
    }
  }

  const handleConfirm = async (id: number) => {
    try {
      await confirmStorage(id)
      message.success('确认成功')
      loadData()
    } catch (error) {
      message.error('确认失败')
    }
  }

  const handleCancel = async (id: number) => {
    try {
      await cancelStorage(id)
      message.success('取消成功')
      loadData()
    } catch (error) {
      message.error('取消失败')
    }
  }

  const handleDelete = async (id: number) => {
    try {
      await deleteStorage(id)
      message.success('删除成功')
      loadData()
    } catch (error) {
      message.error('删除失败')
    }
  }

  const addDetail = () => {
    const newDetail: AssetStorageDetail = {
      assetName: '',
      quantity: 1,
      unitPrice: 0,
      totalPrice: 0,
    }
    setDetails([...details, newDetail])
  }

  const removeDetail = (index: number) => {
    setDetails(details.filter((_, i) => i !== index))
  }

  const updateDetail = (index: number, field: keyof AssetStorageDetail, value: any) => {
    const newDetails = [...details]
    newDetails[index] = { ...newDetails[index], [field]: value }
    if (field === 'unitPrice' || field === 'quantity') {
      const unitPrice = newDetails[index].unitPrice || 0
      const quantity = newDetails[index].quantity || 0
      newDetails[index].totalPrice = unitPrice * quantity
    }
    setDetails(newDetails)
  }

  const columns: ColumnsType<AssetStorage> = [
    {
      title: '入库单号',
      dataIndex: 'storageNo',
      key: 'storageNo',
    },
    {
      title: '入库类型',
      dataIndex: 'storageType',
      key: 'storageType',
      render: (type) => {
        const typeMap: Record<string, { text: string; color: string }> = {
          PURCHASE: { text: '采购入库', color: 'blue' },
          TRANSFER: { text: '调拨入库', color: 'green' },
          REPAIR: { text: '维修入库', color: 'orange' },
          OTHER: { text: '其他', color: 'default' },
        }
        const info = typeMap[type] || { text: type, color: 'default' }
        return <Tag color={info.color}>{info.text}</Tag>
      },
    },
    {
      title: '入库日期',
      dataIndex: 'storageDate',
      key: 'storageDate',
    },
    {
      title: '供应商',
      dataIndex: 'supplierName',
      key: 'supplierName',
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
          CANCELLED: { text: '已取消', color: 'error' },
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
          <Button type="link" onClick={() => handleViewDetails(record)}>明细</Button>
          {record.status === 'DRAFT' && (
            <>
              <Button type="link" onClick={() => handleEdit(record)}>编辑</Button>
              <Button type="link" onClick={() => handleConfirm(record.storageId!)}>确认</Button>
              <Button type="link" danger onClick={() => handleCancel(record.storageId!)}>取消</Button>
            </>
          )}
          {record.status === 'DRAFT' && (
            <Popconfirm title="确定要删除吗？" onConfirm={() => handleDelete(record.storageId!)}>
              <Button type="link" danger>删除</Button>
            </Popconfirm>
          )}
        </Space>
      ),
    },
  ]

  return (
    <div>
      <div style={{ marginBottom: 16, display: 'flex', justifyContent: 'space-between' }}>
        <Space>
          <Input placeholder="搜索入库单号" style={{ width: 200 }} allowClear />
          <Button icon={<SearchOutlined />}>搜索</Button>
        </Space>
        <Space>
          <Button icon={<DownloadOutlined />} onClick={() => exportToExcel(data, columns, '资产入库列表')}>
            导出Excel
          </Button>
          <Button type="primary" icon={<PlusOutlined />} onClick={handleAdd}>
            新增入库单
          </Button>
        </Space>
      </div>

      <Table
        columns={columns}
        dataSource={data}
        loading={loading}
        rowKey="storageId"
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

      {/* 入库单Modal */}
      <Modal
        title={editingRecord ? '编辑入库单' : '新增入库单'}
        open={modalVisible}
        onOk={handleSubmit}
        onCancel={() => setModalVisible(false)}
        width={1000}
      >
        <Form form={form} layout="vertical">
          <Form.Item name="storageType" label="入库类型" rules={[{ required: true }]}>
            <Select>
              <Select.Option value="PURCHASE">采购入库</Select.Option>
              <Select.Option value="TRANSFER">调拨入库</Select.Option>
              <Select.Option value="REPAIR">维修入库</Select.Option>
              <Select.Option value="OTHER">其他</Select.Option>
            </Select>
          </Form.Item>
          <Form.Item name="storageDate" label="入库日期" rules={[{ required: true }]}>
            <DatePicker style={{ width: '100%' }} />
          </Form.Item>
          <Form.Item name="supplierName" label="供应商">
            <Input />
          </Form.Item>
          <Form.Item name="warehouse" label="仓库">
            <Input />
          </Form.Item>
          <Form.Item name="location" label="存放位置">
            <Input />
          </Form.Item>
          <Form.Item name="remark" label="备注">
            <Input.TextArea rows={3} />
          </Form.Item>
        </Form>

        <div style={{ marginTop: 16 }}>
          <div style={{ display: 'flex', justifyContent: 'space-between', marginBottom: 8 }}>
            <h4>入库明细</h4>
            <Button type="dashed" onClick={addDetail} icon={<PlusOutlined />}>添加明细</Button>
          </div>
          <Table
            dataSource={details}
            rowKey={(_, index) => index.toString()}
            pagination={false}
            size="small"
            columns={[
              { title: '资产名称', dataIndex: 'assetName', key: 'assetName',
                render: (_, record, index) => (
                  <Input value={record.assetName} onChange={(e) => updateDetail(index, 'assetName', e.target.value)} />
                ),
              },
              { title: '数量', dataIndex: 'quantity', key: 'quantity', width: 100,
                render: (_, record, index) => (
                  <InputNumber value={record.quantity} min={1} onChange={(v) => updateDetail(index, 'quantity', v)} style={{ width: '100%' }} />
                ),
              },
              { title: '单价', dataIndex: 'unitPrice', key: 'unitPrice', width: 120,
                render: (_, record, index) => (
                  <InputNumber value={record.unitPrice} min={0} precision={2} onChange={(v) => updateDetail(index, 'unitPrice', v)} style={{ width: '100%' }} />
                ),
              },
              { title: '总价', dataIndex: 'totalPrice', key: 'totalPrice', width: 120,
                render: (totalPrice) => totalPrice ? `¥${totalPrice.toFixed(2)}` : '-',
              },
              { title: '操作', key: 'action', width: 80,
                render: (_, __, index) => (
                  <Button type="link" danger onClick={() => removeDetail(index)}>删除</Button>
                ),
              },
            ]}
          />
        </div>
      </Modal>

      {/* 明细Modal */}
      <Modal
        title="入库明细"
        open={detailModalVisible}
        onCancel={() => setDetailModalVisible(false)}
        footer={null}
        width={800}
      >
        <Table
          dataSource={details}
          rowKey="detailId"
          pagination={false}
          columns={[
            { title: '资产编码', dataIndex: 'assetCode', key: 'assetCode' },
            { title: '资产名称', dataIndex: 'assetName', key: 'assetName' },
            { title: '数量', dataIndex: 'quantity', key: 'quantity' },
            { title: '单价', dataIndex: 'unitPrice', key: 'unitPrice', render: (v) => v ? `¥${v.toFixed(2)}` : '-' },
            { title: '总价', dataIndex: 'totalPrice', key: 'totalPrice', render: (v) => v ? `¥${v.toFixed(2)}` : '-' },
          ]}
        />
      </Modal>
    </div>
  )
}

