import { useState, useEffect } from 'react'
import { Table, Button, Space, Modal, Form, Input, Select, message, Tag, Card, Statistic, Row, Col } from 'antd'
import { WarningOutlined, CheckOutlined, CloseOutlined, DownloadOutlined, ReloadOutlined } from '@ant-design/icons'
import type { ColumnsType } from 'antd/es/table'
import { Permission } from '@/components/Permission'
import { getWarningPage, handleWarning, ignoreWarning, generateWarnings, getWarningStatistics, type InventoryWarning } from '@/api/inventory/warning'
import { exportToExcel } from '@/utils/export'

export default function InventoryWarningPage() {
  const [data, setData] = useState<InventoryWarning[]>([])
  const [statistics, setStatistics] = useState<any>({})
  const [loading, setLoading] = useState(false)
  const [total, setTotal] = useState(0)
  const [current, setCurrent] = useState(1)
  const [pageSize, setPageSize] = useState(10)
  const [handleModalVisible, setHandleModalVisible] = useState(false)
  const [selectedWarning, setSelectedWarning] = useState<InventoryWarning | null>(null)
  const [form] = Form.useForm()

  useEffect(() => {
    loadData()
    loadStatistics()
  }, [current, pageSize])

  const loadStatistics = async () => {
    try {
      const res = await getWarningStatistics()
      setStatistics(res.data || {})
    } catch (error) {
      console.error('加载统计数据失败:', error)
    }
  }

  const loadData = async () => {
    setLoading(true)
    try {
      const res = await getWarningPage({
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

  const handleWarningClick = (record: InventoryWarning) => {
    setSelectedWarning(record)
    form.resetFields()
    setHandleModalVisible(true)
  }

  const handleWarningSubmit = async () => {
    try {
      const values = await form.validateFields()
      await handleWarning(selectedWarning!.warningId!, values.handleResult)
      message.success('处理成功')
      setHandleModalVisible(false)
      loadData()
      loadStatistics()
    } catch (error: any) {
      message.error(error.response?.data?.msg || '处理失败')
    }
  }

  const handleIgnore = async (id: number) => {
    try {
      await ignoreWarning(id)
      message.success('已忽略')
      loadData()
      loadStatistics()
    } catch (error: any) {
      message.error(error.response?.data?.msg || '操作失败')
    }
  }

  const handleGenerate = async () => {
    try {
      await generateWarnings()
      message.success('预警生成成功')
      loadData()
      loadStatistics()
    } catch (error: any) {
      message.error(error.response?.data?.msg || '生成失败')
    }
  }

  const handleExport = () => {
    exportToExcel(data, columns, '库存预警列表')
  }

  const columns: ColumnsType<InventoryWarning> = [
    {
      title: '预警编号',
      dataIndex: 'warningNo',
      key: 'warningNo',
    },
    {
      title: '预警类型',
      dataIndex: 'warningType',
      key: 'warningType',
      render: (type) => {
        const typeMap: Record<string, { text: string; color: string }> = {
          LOW_STOCK: { text: '低库存', color: 'orange' },
          HIGH_STOCK: { text: '高库存', color: 'blue' },
          EXPIRY: { text: '到期预警', color: 'red' },
          ABNORMAL: { text: '异常预警', color: 'purple' },
        }
        const info = typeMap[type] || { text: type, color: 'default' }
        return <Tag color={info.color}>{info.text}</Tag>
      },
    },
    {
      title: '预警级别',
      dataIndex: 'warningLevel',
      key: 'warningLevel',
      render: (level) => {
        const levelMap: Record<string, { text: string; color: string }> = {
          LOW: { text: '低', color: 'default' },
          NORMAL: { text: '正常', color: 'blue' },
          HIGH: { text: '高', color: 'orange' },
          URGENT: { text: '紧急', color: 'red' },
        }
        const info = levelMap[level] || { text: level, color: 'default' }
        return <Tag color={info.color}>{info.text}</Tag>
      },
    },
    {
      title: '仓库',
      dataIndex: 'warehouseName',
      key: 'warehouseName',
    },
    {
      title: '物料编码',
      dataIndex: 'materialCode',
      key: 'materialCode',
    },
    {
      title: '物料名称',
      dataIndex: 'materialName',
      key: 'materialName',
    },
    {
      title: '当前库存',
      dataIndex: 'currentQuantity',
      key: 'currentQuantity',
    },
    {
      title: '最低库存',
      dataIndex: 'minStock',
      key: 'minStock',
    },
    {
      title: '预警信息',
      dataIndex: 'warningMessage',
      key: 'warningMessage',
      ellipsis: true,
    },
    {
      title: '预警时间',
      dataIndex: 'warningTime',
      key: 'warningTime',
    },
    {
      title: '状态',
      dataIndex: 'status',
      key: 'status',
      render: (status) => {
        const statusMap: Record<string, { text: string; color: string }> = {
          PENDING: { text: '待处理', color: 'orange' },
          PROCESSING: { text: '处理中', color: 'processing' },
          RESOLVED: { text: '已解决', color: 'success' },
          IGNORED: { text: '已忽略', color: 'default' },
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
          {record.status === 'PENDING' && (
            <>
              <Permission permission="inventory:warning:handle">
                <Button type="link" size="small" icon={<CheckOutlined />} onClick={() => handleWarningClick(record)}>
                  处理
                </Button>
              </Permission>
              <Permission permission="inventory:warning:ignore">
                <Button type="link" size="small" icon={<CloseOutlined />} onClick={() => handleIgnore(record.warningId!)}>
                  忽略
                </Button>
              </Permission>
            </>
          )}
        </Space>
      ),
    },
  ]

  return (
    <div>
      <Card style={{ marginBottom: 16 }}>
        <Row gutter={16}>
          <Col span={6}>
            <Statistic title="待处理预警" value={statistics.pendingCount || 0} prefix={<WarningOutlined />} valueStyle={{ color: '#faad14' }} />
          </Col>
          <Col span={6}>
            <Statistic title="处理中预警" value={statistics.processingCount || 0} prefix={<WarningOutlined />} valueStyle={{ color: '#1890ff' }} />
          </Col>
          <Col span={6}>
            <Statistic title="已解决预警" value={statistics.resolvedCount || 0} prefix={<CheckOutlined />} valueStyle={{ color: '#52c41a' }} />
          </Col>
          <Col span={6}>
            <Statistic title="总预警数" value={statistics.totalCount || 0} />
          </Col>
        </Row>
      </Card>

      <div style={{ marginBottom: 16, display: 'flex', justifyContent: 'space-between' }}>
        <div></div>
        <Space>
          <Button icon={<DownloadOutlined />} onClick={handleExport} disabled={data.length === 0}>
            导出Excel
          </Button>
          <Button icon={<ReloadOutlined />} onClick={handleGenerate}>
            生成预警
          </Button>
        </Space>
      </div>
      <Table
        columns={columns}
        dataSource={data}
        loading={loading}
        rowKey="warningId"
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
        title="处理预警"
        open={handleModalVisible}
        onOk={handleWarningSubmit}
        onCancel={() => setHandleModalVisible(false)}
        width={600}
      >
        <Form form={form} layout="vertical">
          <Form.Item label="预警类型">
            <Input value={selectedWarning?.warningType} disabled />
          </Form.Item>
          <Form.Item label="物料名称">
            <Input value={selectedWarning?.materialName} disabled />
          </Form.Item>
          <Form.Item label="当前库存">
            <Input value={selectedWarning?.currentQuantity} disabled />
          </Form.Item>
          <Form.Item label="预警信息">
            <Input.TextArea value={selectedWarning?.warningMessage} disabled rows={3} />
          </Form.Item>
          <Form.Item
            name="handleResult"
            label="处理结果"
            rules={[{ required: true, message: '请输入处理结果' }]}
          >
            <Input.TextArea rows={4} placeholder="请输入处理结果" />
          </Form.Item>
        </Form>
      </Modal>
    </div>
  )
}

