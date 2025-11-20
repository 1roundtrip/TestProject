import { useState, useEffect } from 'react'
import { Table, Button, Space, Modal, Form, Input, DatePicker, Select, message, Popconfirm, Tag } from 'antd'
import { PlusOutlined, EditOutlined, DeleteOutlined, CheckOutlined, SearchOutlined, DownloadOutlined } from '@ant-design/icons'
import type { ColumnsType } from 'antd/es/table'
import { getBorrowPage, createBorrow, returnAsset, deleteBorrow, getOverdueBorrows, type AssetBorrow } from '@/api/asset/borrow'
import { getAssetPage, type Asset } from '@/api/asset'
import { exportToExcel } from '@/utils/export'
import dayjs from 'dayjs'

export default function AssetBorrowPage() {
  const [data, setData] = useState<AssetBorrow[]>([])
  const [loading, setLoading] = useState(false)
  const [total, setTotal] = useState(0)
  const [current, setCurrent] = useState(1)
  const [pageSize, setPageSize] = useState(10)
  const [modalVisible, setModalVisible] = useState(false)
  const [editingRecord, setEditingRecord] = useState<AssetBorrow | null>(null)
  const [form] = Form.useForm()
  const [assets, setAssets] = useState<Asset[]>([])

  useEffect(() => {
    loadData()
    loadAssets()
  }, [current, pageSize])

  const loadData = async () => {
    setLoading(true)
    try {
      const res = await getBorrowPage({
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

  const loadAssets = async () => {
    try {
      const res = await getAssetPage({ current: 1, size: 1000 })
      setAssets(res.data.records)
    } catch (error) {
      console.error('加载资产失败', error)
    }
  }

  const handleAdd = () => {
    setEditingRecord(null)
    form.resetFields()
    form.setFieldsValue({
      borrowType: 'BORROW',
      borrowDate: dayjs(),
      status: 'BORROWED',
    })
    setModalVisible(true)
  }

  const handleEdit = (record: AssetBorrow) => {
    setEditingRecord(record)
    form.setFieldsValue({
      ...record,
      borrowDate: record.borrowDate ? dayjs(record.borrowDate) : null,
      expectedReturnDate: record.expectedReturnDate ? dayjs(record.expectedReturnDate) : null,
    })
    setModalVisible(true)
  }

  const handleSubmit = async () => {
    try {
      const values = await form.validateFields()
      const submitData = {
        ...values,
        borrowDate: values.borrowDate ? values.borrowDate.format('YYYY-MM-DD') : undefined,
        expectedReturnDate: values.expectedReturnDate ? values.expectedReturnDate.format('YYYY-MM-DD') : undefined,
      }
      
      if (editingRecord?.borrowId) {
        message.warning('编辑功能待实现')
      } else {
        await createBorrow(submitData)
        message.success('创建成功')
        setModalVisible(false)
        loadData()
      }
    } catch (error) {
      console.error('提交失败', error)
    }
  }

  const handleReturn = async (id: number) => {
    try {
      await returnAsset(id)
      message.success('退库成功')
      loadData()
    } catch (error) {
      message.error('退库失败')
    }
  }

  const handleDelete = async (id: number) => {
    try {
      await deleteBorrow(id)
      message.success('删除成功')
      loadData()
    } catch (error) {
      message.error('删除失败')
    }
  }

  const handleAssetChange = (assetId: number) => {
    const asset = assets.find(a => a.assetId === assetId)
    if (asset) {
      form.setFieldsValue({
        assetCode: asset.assetCode,
        assetName: asset.assetName,
      })
    }
  }

  const columns: ColumnsType<AssetBorrow> = [
    {
      title: '领用单号',
      dataIndex: 'borrowNo',
      key: 'borrowNo',
    },
    {
      title: '类型',
      dataIndex: 'borrowType',
      key: 'borrowType',
      render: (type) => (
        <Tag color={type === 'BORROW' ? 'blue' : 'green'}>
          {type === 'BORROW' ? '领用' : '退库'}
        </Tag>
      ),
    },
    {
      title: '资产名称',
      dataIndex: 'assetName',
      key: 'assetName',
    },
    {
      title: '领用人',
      dataIndex: 'borrowerName',
      key: 'borrowerName',
    },
    {
      title: '领用部门',
      dataIndex: 'borrowerDeptName',
      key: 'borrowerDeptName',
    },
    {
      title: '领用日期',
      dataIndex: 'borrowDate',
      key: 'borrowDate',
    },
    {
      title: '预计归还',
      dataIndex: 'expectedReturnDate',
      key: 'expectedReturnDate',
    },
    {
      title: '状态',
      dataIndex: 'status',
      key: 'status',
      render: (status) => {
        const statusMap: Record<string, { text: string; color: string }> = {
          BORROWED: { text: '已领用', color: 'blue' },
          RETURNED: { text: '已归还', color: 'success' },
          OVERDUE: { text: '逾期', color: 'error' },
        }
        const info = statusMap[status] || { text: status, color: 'default' }
        return <Tag color={info.color}>{info.text}</Tag>
      },
    },
    {
      title: '操作',
      key: 'action',
      width: 200,
      render: (_, record) => (
        <Space>
          {record.status === 'BORROWED' && (
            <Button type="link" onClick={() => handleReturn(record.borrowId!)}>退库</Button>
          )}
          <Popconfirm title="确定要删除吗？" onConfirm={() => handleDelete(record.borrowId!)}>
            <Button type="link" danger>删除</Button>
          </Popconfirm>
        </Space>
      ),
    },
  ]

  return (
    <div>
      <div style={{ marginBottom: 16, display: 'flex', justifyContent: 'space-between' }}>
        <Space>
          <Input placeholder="搜索领用单号" style={{ width: 200 }} allowClear />
          <Button icon={<SearchOutlined />}>搜索</Button>
        </Space>
        <Space>
          <Button icon={<DownloadOutlined />} onClick={() => exportToExcel(data, columns, '资产领用列表')}>
            导出Excel
          </Button>
          <Button type="primary" icon={<PlusOutlined />} onClick={handleAdd}>
            新增领用单
          </Button>
        </Space>
      </div>

      <Table
        columns={columns}
        dataSource={data}
        loading={loading}
        rowKey="borrowId"
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
        title={editingRecord ? '编辑领用单' : '新增领用单'}
        open={modalVisible}
        onOk={handleSubmit}
        onCancel={() => setModalVisible(false)}
        width={600}
      >
        <Form form={form} layout="vertical">
          <Form.Item name="borrowType" label="类型" rules={[{ required: true }]}>
            <Select>
              <Select.Option value="BORROW">领用</Select.Option>
              <Select.Option value="RETURN">退库</Select.Option>
            </Select>
          </Form.Item>
          <Form.Item name="assetId" label="资产" rules={[{ required: true }]}>
            <Select
              showSearch
              placeholder="请选择资产"
              onChange={handleAssetChange}
              filterOption={(input, option) =>
                (option?.label ?? '').toLowerCase().includes(input.toLowerCase())
              }
              options={assets.map(a => ({ label: `${a.assetCode} - ${a.assetName}`, value: a.assetId }))}
            />
          </Form.Item>
          <Form.Item name="assetCode" label="资产编码" hidden>
            <Input />
          </Form.Item>
          <Form.Item name="assetName" label="资产名称" hidden>
            <Input />
          </Form.Item>
          <Form.Item name="borrowerName" label="领用人" rules={[{ required: true }]}>
            <Input />
          </Form.Item>
          <Form.Item name="borrowerDeptName" label="领用部门">
            <Input />
          </Form.Item>
          <Form.Item name="borrowDate" label="领用日期" rules={[{ required: true }]}>
            <DatePicker style={{ width: '100%' }} />
          </Form.Item>
          <Form.Item name="expectedReturnDate" label="预计归还日期">
            <DatePicker style={{ width: '100%' }} />
          </Form.Item>
          <Form.Item name="borrowReason" label="领用原因">
            <Input.TextArea rows={3} />
          </Form.Item>
          <Form.Item name="remark" label="备注">
            <Input.TextArea rows={3} />
          </Form.Item>
        </Form>
      </Modal>
    </div>
  )
}

