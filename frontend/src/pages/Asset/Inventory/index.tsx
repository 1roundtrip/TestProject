import { useState, useEffect } from 'react'
import { Table, Button, Space, Modal, Form, Input, DatePicker, Select, message, Tag, InputNumber } from 'antd'
import { PlusOutlined, PlayCircleOutlined, CheckCircleOutlined, CheckOutlined, SearchOutlined, DownloadOutlined } from '@ant-design/icons'
import type { ColumnsType } from 'antd/es/table'
import { getInventoryPage, createInventory, addInventoryDetails, startInventory, completeInventory, confirmInventory, getInventoryDetails, handleDifference, type AssetInventory, type AssetInventoryDetail } from '@/api/asset/inventory'
import { getAssetPage, type Asset } from '@/api/asset'
import { exportToExcel } from '@/utils/export'
import dayjs from 'dayjs'

export default function AssetInventoryPage() {
  const [data, setData] = useState<AssetInventory[]>([])
  const [loading, setLoading] = useState(false)
  const [total, setTotal] = useState(0)
  const [current, setCurrent] = useState(1)
  const [pageSize, setPageSize] = useState(10)
  const [modalVisible, setModalVisible] = useState(false)
  const [detailModalVisible, setDetailModalVisible] = useState(false)
  const [currentInventory, setCurrentInventory] = useState<AssetInventory | null>(null)
  const [details, setDetails] = useState<AssetInventoryDetail[]>([])
  const [form] = Form.useForm()
  const [assets, setAssets] = useState<Asset[]>([])

  useEffect(() => {
    loadData()
    loadAssets()
  }, [current, pageSize])

  const loadData = async () => {
    setLoading(true)
    try {
      const res = await getInventoryPage({
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
      inventoryType: 'FULL',
      inventoryDate: dayjs(),
      status: 'DRAFT',
    })
    setModalVisible(true)
  }

  const handleSubmit = async () => {
    try {
      const values = await form.validateFields()
      const submitData = {
        ...values,
        inventoryDate: values.inventoryDate ? values.inventoryDate.format('YYYY-MM-DD') : undefined,
      }
      const res = await createInventory(submitData)
      message.success('创建成功')
      setModalVisible(false)
      
      // 添加盘点明细
      if (res.data && res.data.inventoryId) {
        setCurrentInventory(res.data)
        setDetailModalVisible(true)
      }
      loadData()
    } catch (error) {
      console.error('提交失败', error)
    }
  }

  const handleStart = async (id: number) => {
    try {
      await startInventory(id)
      message.success('开始盘点')
      loadData()
    } catch (error) {
      message.error('操作失败')
    }
  }

  const handleComplete = async (id: number) => {
    try {
      await completeInventory(id)
      message.success('盘点完成')
      loadData()
    } catch (error) {
      message.error('操作失败')
    }
  }

  const handleConfirm = async (id: number) => {
    try {
      await confirmInventory(id)
      message.success('确认成功')
      loadData()
    } catch (error) {
      message.error('确认失败')
    }
  }

  const handleViewDetails = async (record: AssetInventory) => {
    setCurrentInventory(record)
    try {
      const res = await getInventoryDetails(record.inventoryId!)
      setDetails(res.data)
      setDetailModalVisible(true)
    } catch (error) {
      message.error('加载明细失败')
    }
  }

  const updateDetailQuantity = async (detailId: number, actualQuantity: number) => {
    try {
      // 这里应该调用更新接口，暂时简化处理
      const newDetails = details.map(d => 
        d.detailId === detailId ? { ...d, actualQuantity } : d
      )
      setDetails(newDetails)
    } catch (error) {
      message.error('更新失败')
    }
  }

  const handleDetailDifference = async (detailId: number) => {
    try {
      await handleDifference(detailId, '已处理')
      message.success('处理成功')
      if (currentInventory) {
        handleViewDetails(currentInventory)
      }
    } catch (error) {
      message.error('处理失败')
    }
  }

  const columns: ColumnsType<AssetInventory> = [
    {
      title: '盘点单号',
      dataIndex: 'inventoryNo',
      key: 'inventoryNo',
    },
    {
      title: '盘点类型',
      dataIndex: 'inventoryType',
      key: 'inventoryType',
      render: (type) => {
        const typeMap: Record<string, string> = {
          FULL: '全面盘点',
          PARTIAL: '部分盘点',
          SPOT: '抽查盘点',
        }
        return typeMap[type] || type
      },
    },
    {
      title: '盘点日期',
      dataIndex: 'inventoryDate',
      key: 'inventoryDate',
    },
    {
      title: '应盘数量',
      dataIndex: 'totalCount',
      key: 'totalCount',
    },
    {
      title: '实盘数量',
      dataIndex: 'actualCount',
      key: 'actualCount',
    },
    {
      title: '盘盈',
      dataIndex: 'surplusCount',
      key: 'surplusCount',
      render: (count) => count > 0 ? <Tag color="success">+{count}</Tag> : '-',
    },
    {
      title: '盘亏',
      dataIndex: 'shortageCount',
      key: 'shortageCount',
      render: (count) => count > 0 ? <Tag color="error">-{count}</Tag> : '-',
    },
    {
      title: '状态',
      dataIndex: 'status',
      key: 'status',
      render: (status) => {
        const statusMap: Record<string, { text: string; color: string }> = {
          DRAFT: { text: '草稿', color: 'default' },
          IN_PROGRESS: { text: '盘点中', color: 'processing' },
          COMPLETED: { text: '已完成', color: 'success' },
          CONFIRMED: { text: '已确认', color: 'success' },
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
          <Button type="link" onClick={() => handleViewDetails(record)}>明细</Button>
          {record.status === 'DRAFT' && (
            <Button type="link" onClick={() => handleStart(record.inventoryId!)}>开始</Button>
          )}
          {record.status === 'IN_PROGRESS' && (
            <Button type="link" onClick={() => handleComplete(record.inventoryId!)}>完成</Button>
          )}
          {record.status === 'COMPLETED' && (
            <Button type="link" onClick={() => handleConfirm(record.inventoryId!)}>确认</Button>
          )}
        </Space>
      ),
    },
  ]

  return (
    <div>
      <div style={{ marginBottom: 16, display: 'flex', justifyContent: 'space-between' }}>
        <Space>
          <Input placeholder="搜索盘点单号" style={{ width: 200 }} allowClear />
          <Button icon={<SearchOutlined />}>搜索</Button>
        </Space>
        <Space>
          <Button icon={<DownloadOutlined />} onClick={() => exportToExcel(data, columns, '资产盘点列表')}>
            导出Excel
          </Button>
          <Button type="primary" icon={<PlusOutlined />} onClick={handleAdd}>
            新增盘点单
          </Button>
        </Space>
      </div>

      <Table
        columns={columns}
        dataSource={data}
        loading={loading}
        rowKey="inventoryId"
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
        title="新增盘点单"
        open={modalVisible}
        onOk={handleSubmit}
        onCancel={() => setModalVisible(false)}
        width={600}
      >
        <Form form={form} layout="vertical">
          <Form.Item name="inventoryType" label="盘点类型" rules={[{ required: true }]}>
            <Select>
              <Select.Option value="FULL">全面盘点</Select.Option>
              <Select.Option value="PARTIAL">部分盘点</Select.Option>
              <Select.Option value="SPOT">抽查盘点</Select.Option>
            </Select>
          </Form.Item>
          <Form.Item name="inventoryDate" label="盘点日期" rules={[{ required: true }]}>
            <DatePicker style={{ width: '100%' }} />
          </Form.Item>
          <Form.Item name="warehouse" label="仓库">
            <Input />
          </Form.Item>
          <Form.Item name="deptName" label="部门">
            <Input />
          </Form.Item>
          <Form.Item name="remark" label="备注">
            <Input.TextArea rows={3} />
          </Form.Item>
        </Form>
      </Modal>

      <Modal
        title="盘点明细"
        open={detailModalVisible}
        onCancel={() => setDetailModalVisible(false)}
        footer={null}
        width={1000}
      >
        <Table
          dataSource={details}
          rowKey="detailId"
          pagination={false}
          columns={[
            { title: '资产编码', dataIndex: 'assetCode', key: 'assetCode' },
            { title: '资产名称', dataIndex: 'assetName', key: 'assetName' },
            { title: '账面数量', dataIndex: 'bookQuantity', key: 'bookQuantity' },
            { title: '实盘数量', dataIndex: 'actualQuantity', key: 'actualQuantity',
              render: (value, record) => (
                currentInventory?.status === 'IN_PROGRESS' ? (
                  <InputNumber
                    value={value}
                    min={0}
                    onChange={(v) => updateDetailQuantity(record.detailId!, v || 0)}
                    style={{ width: 100 }}
                  />
                ) : value
              ),
            },
            { title: '差异数量', dataIndex: 'differenceQuantity', key: 'differenceQuantity',
              render: (diff) => {
                if (diff > 0) return <Tag color="success">+{diff}</Tag>
                if (diff < 0) return <Tag color="error">{diff}</Tag>
                return '-'
              },
            },
            { title: '差异类型', dataIndex: 'differenceType', key: 'differenceType',
              render: (type) => {
                const typeMap: Record<string, { text: string; color: string }> = {
                  SURPLUS: { text: '盘盈', color: 'success' },
                  SHORTAGE: { text: '盘亏', color: 'error' },
                  NORMAL: { text: '正常', color: 'default' },
                }
                const info = typeMap[type] || { text: type, color: 'default' }
                return <Tag color={info.color}>{info.text}</Tag>
              },
            },
            { title: '处理状态', dataIndex: 'handleStatus', key: 'handleStatus',
              render: (status) => {
                return status === 'PROCESSED' ? <Tag color="success">已处理</Tag> : <Tag>待处理</Tag>
              },
            },
            { title: '操作', key: 'action',
              render: (_, record) => (
                record.differenceType !== 'NORMAL' && record.handleStatus !== 'PROCESSED' && (
                  <Button type="link" onClick={() => handleDetailDifference(record.detailId!)}>处理</Button>
                )
              ),
            },
          ]}
        />
      </Modal>
    </div>
  )
}

