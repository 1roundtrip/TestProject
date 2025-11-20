import { useState, useEffect } from 'react'
import { Table, Button, Space, Select, message, Tag, Drawer, Form, Upload, Modal, Input, InputNumber } from 'antd'
import { PlusOutlined, DownloadOutlined, SettingOutlined, UploadOutlined } from '@ant-design/icons'
import type { ColumnsType } from 'antd/es/table'
import { Permission } from '@/components/Permission'
import { getHandleRecordPage, createHandleRecord, type WarningHandleRecord } from '@/api/warning/tracking'
import { exportToExcel } from '@/utils/export'
import { parseExcelFile, downloadImportTemplate } from '@/utils/import'
import type { UploadFile } from 'antd/es/upload/interface'

export default function WarningTrackingPage() {
  const [data, setData] = useState<WarningHandleRecord[]>([])
  const [loading, setLoading] = useState(false)
  const [total, setTotal] = useState(0)
  const [current, setCurrent] = useState(1)
  const [pageSize, setPageSize] = useState(10)
  const [settingDrawerVisible, setSettingDrawerVisible] = useState(false)
  const [importModalVisible, setImportModalVisible] = useState(false)
  const [importFileList, setImportFileList] = useState<UploadFile[]>([])
  const [addModalVisible, setAddModalVisible] = useState(false)
  const [addForm] = Form.useForm()
  const [filters, setFilters] = useState<{
    recordId?: number
    handlerId?: number
  }>({})

  useEffect(() => {
    loadData()
  }, [current, pageSize, filters])

  const loadData = async () => {
    setLoading(true)
    try {
      const res = await getHandleRecordPage({
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
      handleType: 'PROCESS',
    })
    setAddModalVisible(true)
  }

  const handleAddSubmit = async () => {
    try {
      const values = await addForm.validateFields()
      await createHandleRecord(values)
      message.success('创建成功')
      setAddModalVisible(false)
      loadData()
    } catch (error: any) {
      message.error(error.response?.data?.msg || '创建失败')
    }
  }

  const handleExport = () => {
    const exportColumns = columns.filter(col => 'dataIndex' in col && col.dataIndex) as Array<{ title: string; dataIndex: string }>
    exportToExcel(data, exportColumns, '预警处理跟踪')
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
      ['recordId', 'handleType', 'handlerName', 'handleAction', 'handleContent', 'handleTime'].map(field => ({ title: field, dataIndex: field })),
      '预警处理跟踪导入模板'
    )
  }

  const getHandleTypeTag = (type?: string) => {
    const typeMap: Record<string, { color: string; text: string }> = {
      ASSIGN: { color: 'default', text: '分配' },
      PROCESS: { color: 'processing', text: '处理' },
      RESOLVE: { color: 'success', text: '解决' },
      ESCALATE: { color: 'warning', text: '升级' },
      TRANSFER: { color: 'default', text: '转交' },
    }
    const info = typeMap[type || ''] || { color: 'default', text: type || '未知' }
    return <Tag color={info.color}>{info.text}</Tag>
  }

  const columns: ColumnsType<WarningHandleRecord> = [
    { title: '预警记录ID', dataIndex: 'recordId', key: 'recordId', width: 120 },
    {
      title: '处理类型',
      dataIndex: 'handleType',
      key: 'handleType',
      width: 100,
      render: (type) => getHandleTypeTag(type),
    },
    { title: '处理人', dataIndex: 'handlerName', key: 'handlerName', width: 120 },
    { title: '处理动作', dataIndex: 'handleAction', key: 'handleAction', width: 120 },
    { title: '处理内容', dataIndex: 'handleContent', key: 'handleContent', width: 200 },
    { title: '处理时间', dataIndex: 'handleTime', key: 'handleTime', width: 180 },
    { title: '下一处理人', dataIndex: 'nextHandlerName', key: 'nextHandlerName', width: 120 },
  ]

  return (
    <div>
      <div style={{ marginBottom: 16, display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
        <Space>
          <Select
            placeholder="预警记录ID"
            allowClear
            style={{ width: 150 }}
            showSearch
            onChange={(value) => setFilters({ ...filters, recordId: value })}
          />
          <Select
            placeholder="处理人ID"
            allowClear
            style={{ width: 150 }}
            showSearch
            onChange={(value) => setFilters({ ...filters, handlerId: value })}
          />
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
          <Permission permission="warning:tracking:add">
            <Button type="primary" icon={<PlusOutlined />} onClick={handleAdd}>
              新增处理记录
            </Button>
          </Permission>
        </Space>
      </div>

      <Table
        columns={columns}
        dataSource={data}
        loading={loading}
        rowKey="handleId"
        scroll={{ x: 1000 }}
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
        title="新增处理记录"
        open={addModalVisible}
        onOk={handleAddSubmit}
        onCancel={() => setAddModalVisible(false)}
        width={700}
      >
        <Form form={addForm} layout="vertical">
          <Form.Item name="recordId" label="预警记录ID" rules={[{ required: true }]}>
            <InputNumber style={{ width: '100%' }} placeholder="请输入预警记录ID" />
          </Form.Item>
          <Form.Item name="handleType" label="处理类型" rules={[{ required: true }]}>
            <Select>
              <Select.Option value="ASSIGN">分配</Select.Option>
              <Select.Option value="PROCESS">处理</Select.Option>
              <Select.Option value="RESOLVE">解决</Select.Option>
              <Select.Option value="ESCALATE">升级</Select.Option>
              <Select.Option value="TRANSFER">转交</Select.Option>
            </Select>
          </Form.Item>
          <Form.Item name="handlerId" label="处理人ID" rules={[{ required: true }]}>
            <InputNumber style={{ width: '100%' }} placeholder="请输入处理人ID" />
          </Form.Item>
          <Form.Item name="handlerName" label="处理人姓名" rules={[{ required: true }]}>
            <Input placeholder="请输入处理人姓名" />
          </Form.Item>
          <Form.Item name="handleAction" label="处理动作">
            <Input placeholder="请输入处理动作" />
          </Form.Item>
          <Form.Item name="handleContent" label="处理内容">
            <Input.TextArea rows={4} placeholder="请输入处理内容" />
          </Form.Item>
          <Form.Item name="nextHandlerId" label="下一处理人ID">
            <InputNumber style={{ width: '100%' }} placeholder="请输入下一处理人ID" />
          </Form.Item>
          <Form.Item name="nextHandlerName" label="下一处理人姓名">
            <Input placeholder="请输入下一处理人姓名" />
          </Form.Item>
        </Form>
      </Modal>

      <Modal
        title="导入预警处理跟踪数据"
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
