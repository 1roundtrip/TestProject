import { useState, useEffect } from 'react'
import { Table, Button, Space, Modal, Form, Input, InputNumber, DatePicker, Select, message, Popconfirm, Tag } from 'antd'
import { PlusOutlined, CheckOutlined, CloseOutlined, SearchOutlined, DownloadOutlined } from '@ant-design/icons'
import type { ColumnsType } from 'antd/es/table'
import { getScrapPage, createScrap, approveScrap, rejectScrap, completeScrap, deleteScrap, type AssetScrap } from '@/api/asset/scrap'
import { getAssetPage, type Asset } from '@/api/asset'
import { exportToExcel } from '@/utils/export'
import dayjs from 'dayjs'

export default function AssetScrapPage() {
  const [data, setData] = useState<AssetScrap[]>([])
  const [loading, setLoading] = useState(false)
  const [total, setTotal] = useState(0)
  const [current, setCurrent] = useState(1)
  const [pageSize, setPageSize] = useState(10)
  const [modalVisible, setModalVisible] = useState(false)
  const [approveModalVisible, setApproveModalVisible] = useState(false)
  const [currentRecord, setCurrentRecord] = useState<AssetScrap | null>(null)
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
      const res = await getScrapPage({
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
      scrapDate: dayjs(),
      scrapType: 'NATURAL',
      status: 'PENDING',
    })
    setModalVisible(true)
  }

  const handleSubmit = async () => {
    try {
      const values = await form.validateFields()
      const submitData = {
        ...values,
        scrapDate: values.scrapDate ? values.scrapDate.format('YYYY-MM-DD') : undefined,
      }
      await createScrap(submitData)
      message.success('创建成功')
      setModalVisible(false)
      loadData()
    } catch (error) {
      console.error('提交失败', error)
    }
  }

  const handleApprove = (record: AssetScrap) => {
    setCurrentRecord(record)
    approveForm.resetFields()
    setApproveModalVisible(true)
  }

  const handleApproveSubmit = async () => {
    try {
      const values = await approveForm.validateFields()
      await approveScrap(currentRecord!.scrapId!, values.approveRemark)
      message.success('审批成功')
      setApproveModalVisible(false)
      loadData()
    } catch (error) {
      message.error('审批失败')
    }
  }

  const handleReject = async (id: number) => {
    try {
      await rejectScrap(id, '')
      message.success('驳回成功')
      loadData()
    } catch (error) {
      message.error('驳回失败')
    }
  }

  const handleComplete = async (id: number) => {
    try {
      await completeScrap(id)
      message.success('报废完成')
      loadData()
    } catch (error) {
      message.error('操作失败')
    }
  }

  const handleDelete = async (id: number) => {
    try {
      await deleteScrap(id)
      message.success('删除成功')
      loadData()
    } catch (error) {
      message.error('删除失败')
    }
  }

  const columns: ColumnsType<AssetScrap> = [
    {
      title: '报废单号',
      dataIndex: 'scrapNo',
      key: 'scrapNo',
    },
    {
      title: '资产名称',
      dataIndex: 'assetName',
      key: 'assetName',
    },
    {
      title: '报废类型',
      dataIndex: 'scrapType',
      key: 'scrapType',
      render: (type) => {
        const typeMap: Record<string, string> = {
          NATURAL: '自然报废',
          DAMAGE: '损坏报废',
          REPLACE: '更新换代',
          OTHER: '其他',
        }
        return typeMap[type] || type
      },
    },
    {
      title: '原值',
      dataIndex: 'originalValue',
      key: 'originalValue',
      render: (value) => value ? `¥${value.toFixed(2)}` : '-',
    },
    {
      title: '净值',
      dataIndex: 'netValue',
      key: 'netValue',
      render: (value) => value ? `¥${value.toFixed(2)}` : '-',
    },
    {
      title: '状态',
      dataIndex: 'status',
      key: 'status',
      render: (status) => {
        const statusMap: Record<string, { text: string; color: string }> = {
          PENDING: { text: '待审批', color: 'orange' },
          APPROVED: { text: '已审批', color: 'blue' },
          REJECTED: { text: '已驳回', color: 'error' },
          COMPLETED: { text: '已完成', color: 'success' },
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
          {record.status === 'PENDING' && (
            <>
              <Button type="link" onClick={() => handleApprove(record)}>审批</Button>
              <Button type="link" danger onClick={() => handleReject(record.scrapId!)}>驳回</Button>
            </>
          )}
          {record.status === 'APPROVED' && (
            <Button type="link" onClick={() => handleComplete(record.scrapId!)}>完成</Button>
          )}
          {record.status === 'PENDING' && (
            <Popconfirm title="确定要删除吗？" onConfirm={() => handleDelete(record.scrapId!)}>
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
          <Input placeholder="搜索报废单号" style={{ width: 200 }} allowClear />
          <Button icon={<SearchOutlined />}>搜索</Button>
        </Space>
        <Space>
          <Button icon={<DownloadOutlined />} onClick={() => exportToExcel(data, columns, '资产报废列表')}>
            导出Excel
          </Button>
          <Button type="primary" icon={<PlusOutlined />} onClick={handleAdd}>
            新增报废申请
          </Button>
        </Space>
      </div>

      <Table
        columns={columns}
        dataSource={data}
        loading={loading}
        rowKey="scrapId"
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
        title="新增报废申请"
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
              options={assets.map(a => ({ label: `${a.assetCode} - ${a.assetName}`, value: a.assetId }))}
            />
          </Form.Item>
          <Form.Item name="scrapType" label="报废类型" rules={[{ required: true }]}>
            <Select>
              <Select.Option value="NATURAL">自然报废</Select.Option>
              <Select.Option value="DAMAGE">损坏报废</Select.Option>
              <Select.Option value="REPLACE">更新换代</Select.Option>
              <Select.Option value="OTHER">其他</Select.Option>
            </Select>
          </Form.Item>
          <Form.Item name="scrapDate" label="报废日期" rules={[{ required: true }]}>
            <DatePicker style={{ width: '100%' }} />
          </Form.Item>
          <Form.Item name="scrapReason" label="报废原因" rules={[{ required: true }]}>
            <Input.TextArea rows={4} />
          </Form.Item>
          <Form.Item name="scrapValue" label="残值">
            <InputNumber style={{ width: '100%' }} min={0} precision={2} />
          </Form.Item>
          <Form.Item name="remark" label="备注">
            <Input.TextArea rows={3} />
          </Form.Item>
        </Form>
      </Modal>

      <Modal
        title="审批报废申请"
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

