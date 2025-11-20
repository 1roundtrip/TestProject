import { useState, useEffect } from 'react'
import { Table, Button, Space, Modal, Form, Input, Select, InputNumber, message, Popconfirm, Tag, Upload, Drawer } from 'antd'
import { PlusOutlined, EditOutlined, DeleteOutlined, DownloadOutlined, UploadOutlined, SettingOutlined } from '@ant-design/icons'
import type { ColumnsType } from 'antd/es/table'
import { Permission } from '@/components/Permission'
import { getPerformancePage, createPerformance, updatePerformance, deletePerformance, type MaintenancePerformance } from '@/api/maintenance/performance'
import { exportToExcel } from '@/utils/export'
import { parseExcelFile, downloadImportTemplate } from '@/utils/import'
import type { UploadFile } from 'antd/es/upload/interface'

export default function MaintenancePerformancePage() {
  const [data, setData] = useState<MaintenancePerformance[]>([])
  const [loading, setLoading] = useState(false)
  const [total, setTotal] = useState(0)
  const [current, setCurrent] = useState(1)
  const [pageSize, setPageSize] = useState(10)
  const [modalVisible, setModalVisible] = useState(false)
  const [editingRecord, setEditingRecord] = useState<MaintenancePerformance | null>(null)
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
      const res = await getPerformancePage({ current, size: pageSize })
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
    form.setFieldsValue({ evaluationPeriod: 'MONTHLY' })
    setModalVisible(true)
  }

  const handleEdit = (record: MaintenancePerformance) => {
    setEditingRecord(record)
    form.setFieldsValue(record)
    setModalVisible(true)
  }

  const handleSubmit = async () => {
    try {
      const values = await form.validateFields()
      if (editingRecord?.performanceId) {
        await updatePerformance(values)
        message.success('更新成功')
      } else {
        await createPerformance(values)
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
      await deletePerformance(id)
      message.success('删除成功')
      loadData()
    } catch (error: any) {
      message.error(error.response?.data?.msg || '删除失败')
    }
  }

  const handleExport = () => {
    const exportColumns = columns.filter(col => 'dataIndex' in col && col.dataIndex) as Array<{ title: string; dataIndex: string }>
    exportToExcel(data, exportColumns, '绩效考核')
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
      ['evaluatedUserName', 'evaluationPeriod', 'completionRate', 'qualityScore', 'totalScore'].map(field => ({ title: field, dataIndex: field })),
      '绩效考核导入模板'
    )
  }

  const getLevelTag = (level?: string) => {
    const levelMap: Record<string, { color: string; text: string }> = {
      EXCELLENT: { color: 'success', text: '优秀' },
      GOOD: { color: 'processing', text: '良好' },
      AVERAGE: { color: 'default', text: '一般' },
      POOR: { color: 'error', text: '较差' },
    }
    const levelInfo = levelMap[level || ''] || { color: 'default', text: level || '未知' }
    return <Tag color={levelInfo.color}>{levelInfo.text}</Tag>
  }

  const columns: ColumnsType<MaintenancePerformance> = [
    { title: '被考核人', dataIndex: 'evaluatedUserName', key: 'evaluatedUserName' },
    { title: '考核周期', dataIndex: 'evaluationPeriod', key: 'evaluationPeriod' },
    { title: '完成率', dataIndex: 'completionRate', key: 'completionRate', render: (rate) => rate ? `${rate}%` : '-' },
    { title: '质量评分', dataIndex: 'qualityScore', key: 'qualityScore' },
    { title: '总分', dataIndex: 'totalScore', key: 'totalScore' },
    { title: '绩效等级', dataIndex: 'performanceLevel', key: 'performanceLevel', render: (level) => getLevelTag(level) },
    {
      title: '操作',
      key: 'action',
      render: (_, record) => (
        <Space>
          <Permission permission="maintenance:performance:edit">
            <Button type="link" icon={<EditOutlined />} onClick={() => handleEdit(record)}>编辑</Button>
          </Permission>
          <Permission permission="maintenance:performance:remove">
            <Popconfirm title="确定要删除吗？" onConfirm={() => handleDelete(record.performanceId!)}>
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
          <Permission permission="maintenance:performance:add">
            <Button type="primary" icon={<PlusOutlined />} onClick={handleAdd}>
              新增考核
            </Button>
          </Permission>
        </Space>
      </div>
      <Table
        columns={columns}
        dataSource={data}
        loading={loading}
        rowKey="performanceId"
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
        title={editingRecord ? '编辑考核' : '新增考核'}
        open={modalVisible}
        onOk={handleSubmit}
        onCancel={() => setModalVisible(false)}
      >
        <Form form={form} layout="vertical">
          <Form.Item name="evaluatedUserId" label="被考核人ID" rules={[{ required: true }]}>
            <Input placeholder="请输入被考核人ID" />
          </Form.Item>
          <Form.Item name="evaluationPeriod" label="考核周期">
            <Select>
              <Select.Option value="MONTHLY">月度</Select.Option>
              <Select.Option value="QUARTERLY">季度</Select.Option>
              <Select.Option value="YEARLY">年度</Select.Option>
            </Select>
          </Form.Item>
          <Form.Item name="totalScore" label="总分">
            <InputNumber min={0} max={100} style={{ width: '100%' }} />
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

