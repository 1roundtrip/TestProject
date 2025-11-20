import { useState, useEffect } from 'react'
import { Table, Button, Space, Modal, Form, Input, InputNumber, DatePicker, Select, message, Popconfirm, Tag } from 'antd'
import { PlusOutlined, CheckOutlined, CloseOutlined, SearchOutlined, DownloadOutlined } from '@ant-design/icons'
import type { ColumnsType } from 'antd/es/table'
import { getTransferPage, createTransfer, approveTransfer, rejectTransfer, executeTransfer, deleteTransfer, type AssetTransfer } from '@/api/asset/transfer'
import { getAssetPage, type Asset } from '@/api/asset'
import { exportToExcel } from '@/utils/export'
import dayjs from 'dayjs'

export default function AssetTransferPage() {
  const [data, setData] = useState<AssetTransfer[]>([])
  const [loading, setLoading] = useState(false)
  const [total, setTotal] = useState(0)
  const [current, setCurrent] = useState(1)
  const [pageSize, setPageSize] = useState(10)
  const [modalVisible, setModalVisible] = useState(false)
  const [approveModalVisible, setApproveModalVisible] = useState(false)
  const [currentRecord, setCurrentRecord] = useState<AssetTransfer | null>(null)
  const [form] = Form.useForm()
  const [approveForm] = Form.useForm()
  const [assets, setAssets] = useState<Asset[]>([])

  useEffect(() => {
    loadData()
    loadAssets()
  }, [current, pageSize])

  const loadData = async () => {
    setLoading(true)
    try {
      const res = await getTransferPage({
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
    form.resetFields()
    form.setFieldsValue({
      transferDate: dayjs(),
      status: 'PENDING',
    })
    setModalVisible(true)
  }

  const handleSubmit = async () => {
    try {
      const values = await form.validateFields()
      const submitData = {
        ...values,
        transferDate: values.transferDate ? values.transferDate.format('YYYY-MM-DD') : undefined,
      }
      await createTransfer(submitData)
      message.success('创建成功')
      setModalVisible(false)
      loadData()
    } catch (error) {
      console.error('提交失败', error)
    }
  }

  const handleApprove = (record: AssetTransfer) => {
    setCurrentRecord(record)
    approveForm.resetFields()
    setApproveModalVisible(true)
  }

  const handleApproveSubmit = async () => {
    try {
      const values = await approveForm.validateFields()
      await approveTransfer(currentRecord!.transferId!, values.approveRemark)
      message.success('审批成功')
      setApproveModalVisible(false)
      loadData()
    } catch (error) {
      message.error('审批失败')
    }
  }

  const handleReject = async (id: number) => {
    try {
      await rejectTransfer(id, '')
      message.success('驳回成功')
      loadData()
    } catch (error) {
      message.error('驳回失败')
    }
  }

  const handleExecute = async (id: number) => {
    try {
      await executeTransfer(id)
      message.success('执行成功')
      loadData()
    } catch (error) {
      message.error('执行失败')
    }
  }

  const handleAssetChange = (assetId: number) => {
    const asset = assets.find(a => a.assetId === assetId)
    if (asset) {
      form.setFieldsValue({
        assetCode: asset.assetCode,
        assetName: asset.assetName,
        fromDeptId: asset.deptId,
        fromLocation: asset.location,
      })
    }
  }

  const columns: ColumnsType<AssetTransfer> = [
    {
      title: '转移单号',
      dataIndex: 'transferNo',
      key: 'transferNo',
    },
    {
      title: '资产名称',
      dataIndex: 'assetName',
      key: 'assetName',
    },
    {
      title: '原部门',
      dataIndex: 'fromDeptName',
      key: 'fromDeptName',
    },
    {
      title: '目标部门',
      dataIndex: 'toDeptName',
      key: 'toDeptName',
    },
    {
      title: '转移日期',
      dataIndex: 'transferDate',
      key: 'transferDate',
    },
    {
      title: '状态',
      dataIndex: 'status',
      key: 'status',
      render: (status) => {
        const statusMap: Record<string, { text: string; color: string }> = {
          PENDING: { text: '待转移', color: 'orange' },
          TRANSFERRED: { text: '已转移', color: 'success' },
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
          {record.status === 'PENDING' && (
            <>
              <Button type="link" onClick={() => handleApprove(record)}>审批</Button>
              <Button type="link" danger onClick={() => handleReject(record.transferId!)}>驳回</Button>
            </>
          )}
          {record.status === 'PENDING' && (
            <Button type="link" onClick={() => handleExecute(record.transferId!)}>执行</Button>
          )}
        </Space>
      ),
    },
  ]

  return (
    <div>
      <div style={{ marginBottom: 16, display: 'flex', justifyContent: 'space-between' }}>
        <Space>
          <Input placeholder="搜索转移单号" style={{ width: 200 }} allowClear />
          <Button icon={<SearchOutlined />}>搜索</Button>
        </Space>
        <Space>
          <Button icon={<DownloadOutlined />} onClick={() => exportToExcel(data, columns, '资产转移列表')}>
            导出Excel
          </Button>
          <Button type="primary" icon={<PlusOutlined />} onClick={handleAdd}>
            新增转移单
          </Button>
        </Space>
      </div>

      <Table
        columns={columns}
        dataSource={data}
        loading={loading}
        rowKey="transferId"
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
        title="新增转移单"
        open={modalVisible}
        onOk={handleSubmit}
        onCancel={() => setModalVisible(false)}
        width={600}
      >
        <Form form={form} layout="vertical">
          <Form.Item name="assetId" label="资产" rules={[{ required: true }]}>
            <Select
              showSearch
              placeholder="请选择资产"
              onChange={handleAssetChange}
              options={assets.map(a => ({ label: `${a.assetCode} - ${a.assetName}`, value: a.assetId }))}
            />
          </Form.Item>
          <Form.Item name="toDeptId" label="目标部门ID" rules={[{ required: true }]}>
            <InputNumber style={{ width: '100%' }} />
          </Form.Item>
          <Form.Item name="toDeptName" label="目标部门名称" rules={[{ required: true }]}>
            <Input />
          </Form.Item>
          <Form.Item name="toLocation" label="目标位置">
            <Input />
          </Form.Item>
          <Form.Item name="transferDate" label="转移日期" rules={[{ required: true }]}>
            <DatePicker style={{ width: '100%' }} />
          </Form.Item>
          <Form.Item name="transferReason" label="转移原因">
            <Input.TextArea rows={3} />
          </Form.Item>
        </Form>
      </Modal>

      <Modal
        title="审批转移单"
        open={approveModalVisible}
        onOk={handleApproveSubmit}
        onCancel={() => setApproveModalVisible(false)}
      >
        <Form form={approveForm} layout="vertical">
          <Form.Item name="approveRemark" label="审批意见">
            <Input.TextArea rows={4} />
          </Form.Item>
        </Form>
      </Modal>
    </div>
  )
}

