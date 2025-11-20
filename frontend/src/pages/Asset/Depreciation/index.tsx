import { useState, useEffect } from 'react'
import { Table, Button, Space, Modal, Form, Input, InputNumber, DatePicker, Select, message, Tag } from 'antd'
import { PlusOutlined, CalculatorOutlined, CheckOutlined, SearchOutlined, DownloadOutlined } from '@ant-design/icons'
import type { ColumnsType } from 'antd/es/table'
import { getDepreciationPage, configDepreciation, calculateDepreciation, confirmDepreciation, getDepreciationDetails, type AssetDepreciation, type AssetDepreciationDetail } from '@/api/asset/depreciation'
import { getAssetPage, type Asset } from '@/api/asset'
import { exportToExcel } from '@/utils/export'
import dayjs from 'dayjs'

export default function AssetDepreciationPage() {
  const [data, setData] = useState<AssetDepreciation[]>([])
  const [loading, setLoading] = useState(false)
  const [total, setTotal] = useState(0)
  const [current, setCurrent] = useState(1)
  const [pageSize, setPageSize] = useState(10)
  const [modalVisible, setModalVisible] = useState(false)
  const [detailModalVisible, setDetailModalVisible] = useState(false)
  const [currentDepreciation, setCurrentDepreciation] = useState<AssetDepreciation | null>(null)
  const [details, setDetails] = useState<AssetDepreciationDetail[]>([])
  const [form] = Form.useForm()
  const [assets, setAssets] = useState<Asset[]>([])

  useEffect(() => {
    loadData()
    loadAssets()
  }, [current, pageSize])

  const loadData = async () => {
    setLoading(true)
    try {
      const res = await getDepreciationPage({
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
      depreciationMethod: 'STRAIGHT_LINE',
      residualValue: 0,
      status: 'ACTIVE',
      startDate: dayjs(),
    })
    setModalVisible(true)
  }

  const handleSubmit = async () => {
    try {
      const values = await form.validateFields()
      const submitData = {
        ...values,
        startDate: values.startDate ? values.startDate.format('YYYY-MM-DD') : undefined,
      }
      await configDepreciation(submitData)
      message.success('配置成功')
      setModalVisible(false)
      loadData()
    } catch (error) {
      console.error('提交失败', error)
    }
  }

  const handleCalculate = async () => {
    const month = dayjs().format('YYYY-MM')
    try {
      await calculateDepreciation(month)
      message.success('计算成功')
      loadData()
    } catch (error) {
      message.error('计算失败')
    }
  }

  const handleViewDetails = async (record: AssetDepreciation) => {
    setCurrentDepreciation(record)
    try {
      const res = await getDepreciationDetails(record.depreciationId!)
      setDetails(res.data)
      setDetailModalVisible(true)
    } catch (error) {
      message.error('加载明细失败')
    }
  }

  const handleConfirm = async (detailId: number) => {
    try {
      await confirmDepreciation(detailId)
      message.success('确认成功')
      if (currentDepreciation) {
        handleViewDetails(currentDepreciation)
      }
    } catch (error) {
      message.error('确认失败')
    }
  }

  const columns: ColumnsType<AssetDepreciation> = [
    {
      title: '资产编码',
      dataIndex: 'assetCode',
      key: 'assetCode',
    },
    {
      title: '资产名称',
      dataIndex: 'assetName',
      key: 'assetName',
    },
    {
      title: '折旧方法',
      dataIndex: 'depreciationMethod',
      key: 'depreciationMethod',
      render: (method) => {
        return method === 'STRAIGHT_LINE' ? '直线法' : '加速折旧法'
      },
    },
    {
      title: '原值',
      dataIndex: 'originalValue',
      key: 'originalValue',
      render: (value) => value ? `¥${value.toFixed(2)}` : '-',
    },
    {
      title: '累计折旧',
      dataIndex: 'accumulatedDepreciation',
      key: 'accumulatedDepreciation',
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
          ACTIVE: { text: '折旧中', color: 'blue' },
          STOPPED: { text: '已停用', color: 'default' },
          COMPLETED: { text: '已提完', color: 'success' },
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
          <Button type="link" onClick={() => handleViewDetails(record)}>明细</Button>
        </Space>
      ),
    },
  ]

  return (
    <div>
      <div style={{ marginBottom: 16, display: 'flex', justifyContent: 'space-between' }}>
        <Space>
          <Input placeholder="搜索资产编码" style={{ width: 200 }} allowClear />
          <Button icon={<SearchOutlined />}>搜索</Button>
        </Space>
        <Space>
          <Button icon={<CalculatorOutlined />} onClick={handleCalculate}>
            计算折旧
          </Button>
          <Button icon={<DownloadOutlined />} onClick={() => exportToExcel(data, columns, '资产折旧列表')}>
            导出Excel
          </Button>
          <Button type="primary" icon={<PlusOutlined />} onClick={handleAdd}>
            配置折旧
          </Button>
        </Space>
      </div>

      <Table
        columns={columns}
        dataSource={data}
        loading={loading}
        rowKey="depreciationId"
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
        title="配置折旧"
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
          <Form.Item name="depreciationMethod" label="折旧方法" rules={[{ required: true }]}>
            <Select>
              <Select.Option value="STRAIGHT_LINE">直线法</Select.Option>
              <Select.Option value="ACCELERATED">加速折旧法</Select.Option>
            </Select>
          </Form.Item>
          <Form.Item name="originalValue" label="原值" rules={[{ required: true }]}>
            <InputNumber style={{ width: '100%' }} min={0} precision={2} />
          </Form.Item>
          <Form.Item name="residualValue" label="残值">
            <InputNumber style={{ width: '100%' }} min={0} precision={2} />
          </Form.Item>
          <Form.Item name="usefulLife" label="使用年限（月）" rules={[{ required: true }]}>
            <InputNumber style={{ width: '100%' }} min={1} />
          </Form.Item>
          <Form.Item name="startDate" label="开始折旧日期" rules={[{ required: true }]}>
            <DatePicker style={{ width: '100%' }} />
          </Form.Item>
        </Form>
      </Modal>

      <Modal
        title="折旧明细"
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
            { title: '折旧月份', dataIndex: 'depreciationMonth', key: 'depreciationMonth' },
            { title: '折旧金额', dataIndex: 'depreciationAmount', key: 'depreciationAmount', render: (v) => `¥${v.toFixed(2)}` },
            { title: '累计折旧', dataIndex: 'accumulatedAmount', key: 'accumulatedAmount', render: (v) => `¥${v.toFixed(2)}` },
            { title: '净值', dataIndex: 'netValue', key: 'netValue', render: (v) => `¥${v.toFixed(2)}` },
            { title: '状态', dataIndex: 'status', key: 'status',
              render: (status) => {
                const statusMap: Record<string, { text: string; color: string }> = {
                  PENDING: { text: '待确认', color: 'orange' },
                  CONFIRMED: { text: '已确认', color: 'success' },
                }
                const info = statusMap[status] || { text: status, color: 'default' }
                return <Tag color={info.color}>{info.text}</Tag>
              },
            },
            { title: '操作', key: 'action',
              render: (_, record) => (
                record.status === 'PENDING' && (
                  <Button type="link" onClick={() => handleConfirm(record.detailId!)}>确认</Button>
                )
              ),
            },
          ]}
        />
      </Modal>
    </div>
  )
}

