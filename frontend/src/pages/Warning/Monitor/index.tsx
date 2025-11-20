import { useState, useEffect } from 'react'
import { Table, Button, Space, Modal, Form, Input, Select, message, Popconfirm, Tag, Drawer, Upload } from 'antd'
import { PlusOutlined, DownloadOutlined, SettingOutlined, CheckOutlined, CloseOutlined, StopOutlined, UploadOutlined } from '@ant-design/icons'
import type { ColumnsType } from 'antd/es/table'
import { Permission } from '@/components/Permission'
import { getRecordPage, createRecord, handleRecord, ignoreRecord, closeRecord, type WarningRecord } from '@/api/warning/monitor'
import { exportToExcel } from '@/utils/export'
import { parseExcelFile, downloadImportTemplate } from '@/utils/import'
import type { UploadFile } from 'antd/es/upload/interface'

export default function WarningMonitorPage() {
  const [data, setData] = useState<WarningRecord[]>([])
  const [loading, setLoading] = useState(false)
  const [total, setTotal] = useState(0)
  const [current, setCurrent] = useState(1)
  const [pageSize, setPageSize] = useState(10)
  const [handleModalVisible, setHandleModalVisible] = useState(false)
  const [selectedRecord, setSelectedRecord] = useState<WarningRecord | null>(null)
  const [form] = Form.useForm()
  const [addModalVisible, setAddModalVisible] = useState(false)
  const [addForm] = Form.useForm()
  const [settingDrawerVisible, setSettingDrawerVisible] = useState(false)
  const [importModalVisible, setImportModalVisible] = useState(false)
  const [importFileList, setImportFileList] = useState<UploadFile[]>([])
  const [filters, setFilters] = useState<{
    warningType?: string
    warningLevelCode?: string
    status?: string
    sourceType?: string
  }>({})

  useEffect(() => {
    loadData()
  }, [current, pageSize, filters])

  const loadData = async () => {
    setLoading(true)
    try {
      const res = await getRecordPage({
        current,
        size: pageSize,
        ...filters,
      })
      setData(res.data.records || [])
      setTotal(res.data.total || 0)
    } catch (error: any) {
      message.error(error.response?.data?.msg || '加载数据失败')
    } finally {
      setLoading(false)
    }
  }

  const handleAdd = () => {
    addForm.resetFields()
    addForm.setFieldsValue({
      status: 'PENDING',
      warningType: 'ASSET_EXPIRY',
    })
    setAddModalVisible(true)
  }

  const handleAddSubmit = async () => {
    try {
      const values = await addForm.validateFields()
      await createRecord(values)
      message.success('创建成功')
      setAddModalVisible(false)
      loadData()
    } catch (error: any) {
      message.error(error.response?.data?.msg || '创建失败')
    }
  }

  const handleHandle = (record: WarningRecord) => {
    setSelectedRecord(record)
    form.resetFields()
    setHandleModalVisible(true)
  }

  const handleSubmit = async () => {
    try {
      const values = await form.validateFields()
      if (selectedRecord?.recordId) {
        await handleRecord(selectedRecord.recordId, values)
        message.success('处理成功')
        setHandleModalVisible(false)
        loadData()
      }
    } catch (error: any) {
      message.error(error.response?.data?.msg || '操作失败')
    }
  }

  const handleIgnore = async (id: number) => {
    try {
      await ignoreRecord(id)
      message.success('已忽略')
      loadData()
    } catch (error: any) {
      message.error(error.response?.data?.msg || '操作失败')
    }
  }

  const handleClose = async (id: number) => {
    try {
      await closeRecord(id)
      message.success('已关闭')
      loadData()
    } catch (error: any) {
      message.error(error.response?.data?.msg || '操作失败')
    }
  }

  const handleExport = () => {
    const exportColumns = columns.filter(col => 'dataIndex' in col && col.dataIndex) as Array<{ title: string; dataIndex: string }>
    exportToExcel(data, exportColumns, '预警监控')
  }

  const handleImport = async () => {
    if (importFileList.length === 0) {
      message.warning('请选择要导入的文件')
      return
    }
    try {
      const file = importFileList[0].originFileObj
      if (!file) return
      await parseExcelFile(file)
      message.success('导入成功')
      setImportModalVisible(false)
      setImportFileList([])
      loadData()
    } catch (error: any) {
      message.error(error.message || '导入失败')
    }
  }

  const handleDownloadTemplate = () => {
    downloadImportTemplate(
      ['warningTitle', 'warningType', 'warningLevelCode', 'sourceType', 'sourceName', 'status'].map(field => ({ title: field, dataIndex: field })),
      '预警监控导入模板'
    )
  }

  const getStatusTag = (status?: string) => {
    const statusMap: Record<string, { color: string; text: string }> = {
      PENDING: { color: 'default', text: '待处理' },
      PROCESSING: { color: 'processing', text: '处理中' },
      RESOLVED: { color: 'success', text: '已解决' },
      IGNORED: { color: 'default', text: '已忽略' },
      CLOSED: { color: 'default', text: '已关闭' },
    }
    const info = statusMap[status || ''] || { color: 'default', text: status || '未知' }
    return <Tag color={info.color}>{info.text}</Tag>
  }

  const getLevelTag = (level?: string) => {
    const levelMap: Record<string, { color: string; text: string }> = {
      LOW: { color: 'success', text: '低' },
      MEDIUM: { color: 'warning', text: '中' },
      HIGH: { color: 'error', text: '高' },
      CRITICAL: { color: 'error', text: '紧急' },
    }
    const info = levelMap[level || ''] || { color: 'default', text: level || '未知' }
    return <Tag color={info.color}>{info.text}</Tag>
  }

  const columns: ColumnsType<WarningRecord> = [
    { title: '预警标题', dataIndex: 'warningTitle', key: 'warningTitle', width: 200 },
    {
      title: '预警级别',
      dataIndex: 'warningLevelName',
      key: 'warningLevelName',
      width: 100,
      render: (_, record) => getLevelTag(record.warningLevelCode),
    },
    {
      title: '预警类型',
      dataIndex: 'warningType',
      key: 'warningType',
      width: 120,
    },
    {
      title: '来源类型',
      dataIndex: 'sourceType',
      key: 'sourceType',
      width: 100,
      render: (type) => {
        const typeMap: Record<string, string> = {
          ASSET: '资产',
          INVENTORY: '库存',
          PURCHASE: '采购',
          MAINTENANCE: '维修',
          FINANCE: '财务',
        }
        return typeMap[type] || type
      },
    },
    { title: '来源名称', dataIndex: 'sourceName', key: 'sourceName', width: 150 },
    {
      title: '状态',
      dataIndex: 'status',
      key: 'status',
      width: 100,
      render: (status) => getStatusTag(status),
    },
    { title: '触发时间', dataIndex: 'triggerTime', key: 'triggerTime', width: 180 },
    {
      title: '操作',
      key: 'action',
      fixed: 'right',
      width: 200,
      render: (_, record) => (
        <Space>
          {record.status === 'PENDING' && (
            <Permission permission="warning:monitor:handle">
              <Button type="link" icon={<CheckOutlined />} onClick={() => handleHandle(record)}>处理</Button>
            </Permission>
          )}
          {record.status === 'PENDING' && (
            <Permission permission="warning:monitor:ignore">
              <Popconfirm title="确定要忽略吗？" onConfirm={() => handleIgnore(record.recordId!)}>
                <Button type="link" icon={<StopOutlined />}>忽略</Button>
              </Popconfirm>
            </Permission>
          )}
          <Permission permission="warning:monitor:close">
            <Popconfirm title="确定要关闭吗？" onConfirm={() => handleClose(record.recordId!)}>
              <Button type="link" icon={<CloseOutlined />}>关闭</Button>
            </Popconfirm>
          </Permission>
        </Space>
      ),
    },
  ]

  return (
    <div>
      <div style={{ marginBottom: 16, display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
        <Space>
          <Select
            placeholder="预警类型"
            allowClear
            style={{ width: 150 }}
            onChange={(value) => setFilters({ ...filters, warningType: value })}
          >
            <Select.Option value="ASSET_EXPIRY">资产到期</Select.Option>
            <Select.Option value="INVENTORY_LOW">库存不足</Select.Option>
            <Select.Option value="PURCHASE_OVERDUE">采购超期</Select.Option>
          </Select>
          <Select
            placeholder="预警级别"
            allowClear
            style={{ width: 150 }}
            onChange={(value) => setFilters({ ...filters, warningLevelCode: value })}
          >
            <Select.Option value="LOW">低</Select.Option>
            <Select.Option value="MEDIUM">中</Select.Option>
            <Select.Option value="HIGH">高</Select.Option>
            <Select.Option value="CRITICAL">紧急</Select.Option>
          </Select>
          <Select
            placeholder="状态"
            allowClear
            style={{ width: 150 }}
            onChange={(value) => setFilters({ ...filters, status: value })}
          >
            <Select.Option value="PENDING">待处理</Select.Option>
            <Select.Option value="PROCESSING">处理中</Select.Option>
            <Select.Option value="RESOLVED">已解决</Select.Option>
          </Select>
        </Space>
        <Space>
          <Button icon={<DownloadOutlined />} onClick={handleExport} disabled={data.length === 0}>
            导出Excel
          </Button>
          <Button icon={<UploadOutlined />} onClick={() => setImportModalVisible(true)}>
            导入Excel
          </Button>
          <Button icon={<SettingOutlined />} onClick={() => setSettingDrawerVisible(true)}>
            设置
          </Button>
          <Permission permission="warning:monitor:add">
            <Button type="primary" icon={<PlusOutlined />} onClick={handleAdd}>
              新增预警
            </Button>
          </Permission>
        </Space>
      </div>

      <Table
        columns={columns}
        dataSource={data}
        loading={loading}
        rowKey="recordId"
        scroll={{ x: 1200 }}
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
        title="新增预警记录"
        open={addModalVisible}
        onOk={handleAddSubmit}
        onCancel={() => setAddModalVisible(false)}
        width={700}
      >
        <Form form={addForm} layout="vertical">
          <Form.Item name="warningTitle" label="预警标题" rules={[{ required: true }]}>
            <Input placeholder="请输入预警标题" />
          </Form.Item>
          <Form.Item name="warningType" label="预警类型" rules={[{ required: true }]}>
            <Select>
              <Select.Option value="ASSET_EXPIRY">资产到期</Select.Option>
              <Select.Option value="INVENTORY_LOW">库存不足</Select.Option>
              <Select.Option value="PURCHASE_OVERDUE">采购超期</Select.Option>
              <Select.Option value="MAINTENANCE_TIMEOUT">维修超时</Select.Option>
              <Select.Option value="FINANCE_OVERDUE">财务逾期</Select.Option>
            </Select>
          </Form.Item>
          <Form.Item name="warningLevelCode" label="预警级别">
            <Select>
              <Select.Option value="LOW">低</Select.Option>
              <Select.Option value="MEDIUM">中</Select.Option>
              <Select.Option value="HIGH">高</Select.Option>
              <Select.Option value="CRITICAL">紧急</Select.Option>
            </Select>
          </Form.Item>
          <Form.Item name="warningContent" label="预警内容">
            <Input.TextArea rows={4} placeholder="请输入预警内容" />
          </Form.Item>
          <Form.Item name="sourceType" label="来源类型">
            <Select>
              <Select.Option value="ASSET">资产</Select.Option>
              <Select.Option value="INVENTORY">库存</Select.Option>
              <Select.Option value="PURCHASE">采购</Select.Option>
              <Select.Option value="MAINTENANCE">维修</Select.Option>
              <Select.Option value="FINANCE">财务</Select.Option>
            </Select>
          </Form.Item>
          <Form.Item name="sourceName" label="来源名称">
            <Input placeholder="请输入来源名称" />
          </Form.Item>
          <Form.Item name="status" label="状态">
            <Select>
              <Select.Option value="PENDING">待处理</Select.Option>
              <Select.Option value="PROCESSING">处理中</Select.Option>
              <Select.Option value="RESOLVED">已解决</Select.Option>
            </Select>
          </Form.Item>
          <Form.Item name="remark" label="备注">
            <Input.TextArea rows={2} placeholder="请输入备注" />
          </Form.Item>
        </Form>
      </Modal>

      <Modal
        title="处理预警"
        open={handleModalVisible}
        onOk={handleSubmit}
        onCancel={() => setHandleModalVisible(false)}
      >
        <Form form={form} layout="vertical">
          <Form.Item name="handleResult" label="处理结果" rules={[{ required: true }]}>
            <Input.TextArea rows={4} placeholder="请输入处理结果" />
          </Form.Item>
        </Form>
      </Modal>

      <Modal
        title="导入预警监控数据"
        open={importModalVisible}
        onOk={handleImport}
        onCancel={() => {
          setImportModalVisible(false)
          setImportFileList([])
        }}
        okText="导入"
        cancelText="取消"
      >
        <div style={{ marginBottom: 16 }}>
          <Button onClick={handleDownloadTemplate}>下载导入模板</Button>
        </div>
        <Upload
          fileList={importFileList}
          beforeUpload={() => false}
          onChange={({ fileList }) => setImportFileList(fileList)}
          accept=".xlsx,.xls"
        >
          <Button icon={<UploadOutlined />}>选择文件</Button>
        </Upload>
      </Modal>

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
        </Space>
      </Drawer>
    </div>
  )
}

