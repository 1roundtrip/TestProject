import { useState, useEffect } from 'react'
import { Table, Button, Space, Modal, Form, Input, Select, message, Popconfirm, Tag, Upload, Drawer } from 'antd'
import { PlusOutlined, EditOutlined, DeleteOutlined, DownloadOutlined, UploadOutlined, SettingOutlined } from '@ant-design/icons'
import type { ColumnsType } from 'antd/es/table'
import { Permission } from '@/components/Permission'
import { getFaultPage, createFaultRecord, updateFaultRecord, deleteFaultRecord, type MaintenanceFaultRecord } from '@/api/maintenance/fault'
import { exportToExcel } from '@/utils/export'
import { parseExcelFile, downloadImportTemplate } from '@/utils/import'
import type { UploadFile } from 'antd/es/upload/interface'

export default function MaintenanceFaultPage() {
  const [data, setData] = useState<MaintenanceFaultRecord[]>([])
  const [loading, setLoading] = useState(false)
  const [total, setTotal] = useState(0)
  const [current, setCurrent] = useState(1)
  const [pageSize, setPageSize] = useState(10)
  const [modalVisible, setModalVisible] = useState(false)
  const [editingRecord, setEditingRecord] = useState<MaintenanceFaultRecord | null>(null)
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
      const res = await getFaultPage({ current, size: pageSize })
      setData(res.data.records || [])
      setTotal(res.data.total || 0)
    } catch (error: any) {
      message.error(error.response?.data?.msg || '加载数据失败')
    } finally {
      setLoading(false)
    }
  }

  const handleAdd = () => {
    setEditingRecord(null)
    form.resetFields()
    form.setFieldsValue({ faultSeverity: 'MEDIUM' })
    setModalVisible(true)
  }

  const handleEdit = (record: MaintenanceFaultRecord) => {
    setEditingRecord(record)
    form.setFieldsValue(record)
    setModalVisible(true)
  }

  const handleSubmit = async () => {
    try {
      const values = await form.validateFields()
      if (editingRecord?.faultId) {
        await updateFaultRecord(values)
        message.success('更新成功')
      } else {
        await createFaultRecord(values)
        message.success('创建成功')
      }
      setModalVisible(false)
      loadData()
    } catch (error: any) {
      message.error(error.response?.data?.msg || '操作失败')
    }
  }

  const handleDelete = async (id: number) => {
    try {
      await deleteFaultRecord(id)
      message.success('删除成功')
      loadData()
    } catch (error: any) {
      message.error(error.response?.data?.msg || '删除失败')
    }
  }

  const handleExport = () => {
    const exportColumns = columns.filter(col => 'dataIndex' in col && col.dataIndex) as Array<{ title: string; dataIndex: string }>
    exportToExcel(data, exportColumns, '设备故障')
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
      // TODO: 批量导入逻辑
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
      ['faultNo', 'assetName', 'faultType', 'faultSeverity'].map(field => ({ title: field, dataIndex: field })),
      '设备故障导入模板'
    )
  }

  const getSeverityTag = (severity?: string) => {
    const severityMap: Record<string, { color: string; text: string }> = {
      LOW: { color: 'default', text: '低' },
      MEDIUM: { color: 'processing', text: '中' },
      HIGH: { color: 'warning', text: '高' },
      CRITICAL: { color: 'error', text: '严重' },
    }
    const severityInfo = severityMap[severity || ''] || { color: 'default', text: severity || '未知' }
    return <Tag color={severityInfo.color}>{severityInfo.text}</Tag>
  }

  const columns: ColumnsType<MaintenanceFaultRecord> = [
    { title: '故障编号', dataIndex: 'faultNo', key: 'faultNo' },
    { title: '设备名称', dataIndex: 'assetName', key: 'assetName' },
    { title: '故障类型', dataIndex: 'faultType', key: 'faultType' },
    { title: '严重程度', dataIndex: 'faultSeverity', key: 'faultSeverity', render: (severity) => getSeverityTag(severity) },
    {
      title: '操作',
      key: 'action',
      render: (_, record) => (
        <Space>
          <Permission permission="maintenance:fault:edit">
            <Button type="link" icon={<EditOutlined />} onClick={() => handleEdit(record)}>编辑</Button>
          </Permission>
          <Permission permission="maintenance:fault:remove">
            <Popconfirm title="确定要删除吗？" onConfirm={() => handleDelete(record.faultId!)}>
              <Button type="link" danger icon={<DeleteOutlined />}>删除</Button>
            </Popconfirm>
          </Permission>
        </Space>
      ),
    },
  ]

  return (
    <div>
      <div style={{ marginBottom: 16, display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
        <div></div>
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
          <Permission permission="maintenance:fault:add">
            <Button type="primary" icon={<PlusOutlined />} onClick={handleAdd}>
              新增故障
            </Button>
          </Permission>
        </Space>
      </div>
      <Table
        columns={columns}
        dataSource={data}
        loading={loading}
        rowKey="faultId"
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
        title={editingRecord ? '编辑故障' : '新增故障'}
        open={modalVisible}
        onOk={handleSubmit}
        onCancel={() => setModalVisible(false)}
      >
        <Form form={form} layout="vertical">
          <Form.Item name="assetId" label="设备ID" rules={[{ required: true }]}>
            <Input placeholder="请输入设备ID" />
          </Form.Item>
          <Form.Item name="assetName" label="设备名称">
            <Input />
          </Form.Item>
          <Form.Item name="faultType" label="故障类型">
            <Input />
          </Form.Item>
          <Form.Item name="faultSeverity" label="严重程度">
            <Select>
              <Select.Option value="LOW">低</Select.Option>
              <Select.Option value="MEDIUM">中</Select.Option>
              <Select.Option value="HIGH">高</Select.Option>
              <Select.Option value="CRITICAL">严重</Select.Option>
            </Select>
          </Form.Item>
          <Form.Item name="faultDescription" label="故障描述">
            <Input.TextArea rows={4} />
          </Form.Item>
        </Form>
      </Modal>

      <Modal
        title="导入Excel"
        open={importModalVisible}
        onOk={handleImport}
        onCancel={() => {
          setImportModalVisible(false)
          setImportFileList([])
        }}
      >
        <Space direction="vertical" style={{ width: '100%' }}>
          <Button onClick={handleDownloadTemplate}>下载导入模板</Button>
          <Upload
            fileList={importFileList}
            beforeUpload={(file) => {
              setImportFileList([file])
              return false
            }}
            onRemove={() => setImportFileList([])}
            accept=".xlsx,.xls"
          >
            <Button icon={<UploadOutlined />}>选择文件</Button>
          </Upload>
        </Space>
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

