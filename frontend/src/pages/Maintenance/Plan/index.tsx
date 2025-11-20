import { useState, useEffect } from 'react'
import { Table, Button, Space, Modal, Form, Input, Select, DatePicker, message, Popconfirm, Tag, Upload, Drawer } from 'antd'
import { PlusOutlined, EditOutlined, DeleteOutlined, DownloadOutlined, UploadOutlined, SettingOutlined, PlayCircleOutlined } from '@ant-design/icons'
import type { ColumnsType } from 'antd/es/table'
import { Permission } from '@/components/Permission'
import { getPlanPage, createPlan, updatePlan, deletePlan, executePlan, type MaintenancePlan } from '@/api/maintenance/plan'
import { exportToExcel } from '@/utils/export'
import { parseExcelFile, downloadImportTemplate, validateImportData } from '@/utils/import'
import dayjs from 'dayjs'
import type { UploadFile } from 'antd/es/upload/interface'

export default function MaintenancePlanPage() {
  const [data, setData] = useState<MaintenancePlan[]>([])
  const [loading, setLoading] = useState(false)
  const [total, setTotal] = useState(0)
  const [current, setCurrent] = useState(1)
  const [pageSize, setPageSize] = useState(10)
  const [modalVisible, setModalVisible] = useState(false)
  const [editingRecord, setEditingRecord] = useState<MaintenancePlan | null>(null)
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
      const res = await getPlanPage({ current, size: pageSize })
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
    form.setFieldsValue({ status: 'ACTIVE', planType: 'PREVENTIVE' })
    setModalVisible(true)
  }

  const handleEdit = (record: MaintenancePlan) => {
    setEditingRecord(record)
    form.setFieldsValue({
      ...record,
      nextMaintenanceDate: record.nextMaintenanceDate ? dayjs(record.nextMaintenanceDate) : null,
      lastMaintenanceDate: record.lastMaintenanceDate ? dayjs(record.lastMaintenanceDate) : null,
    })
    setModalVisible(true)
  }

  const handleSubmit = async () => {
    try {
      const values = await form.validateFields()
      const submitData = {
        ...values,
        nextMaintenanceDate: values.nextMaintenanceDate ? values.nextMaintenanceDate.format('YYYY-MM-DD') : undefined,
        lastMaintenanceDate: values.lastMaintenanceDate ? values.lastMaintenanceDate.format('YYYY-MM-DD') : undefined,
      }
      
      if (editingRecord?.planId) {
        await updatePlan(submitData)
        message.success('更新成功')
      } else {
        await createPlan(submitData)
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
      await deletePlan(id)
      message.success('删除成功')
      loadData()
    } catch (error: any) {
      message.error(error.response?.data?.msg || '删除失败')
    }
  }

  const handleExecute = async (id: number) => {
    try {
      await executePlan(id)
      message.success('执行成功')
      loadData()
    } catch (error: any) {
      message.error(error.response?.data?.msg || '执行失败')
    }
  }

  const handleImport = async (file: File) => {
    try {
      const data = await parseExcelFile(file)
      const validation = validateImportData(data, ['计划编号', '计划名称'])
      if (!validation.valid) {
        message.error(`数据验证失败：${validation.errors.join('; ')}`)
        return false
      }
      message.success('导入成功')
      loadData()
      return false
    } catch (error: any) {
      message.error('导入失败：' + (error.message || '未知错误'))
      return false
    }
  }

  const handleExport = () => {
    const columns = [
      { header: '计划编号', key: 'planNo' },
      { header: '计划名称', key: 'planName' },
      { header: '设备名称', key: 'assetName' },
      { header: '维护类型', key: 'maintenanceType' },
      { header: '状态', key: 'status' },
    ]
    exportToExcel(data, columns, '维护计划')
  }

  const getStatusTag = (status?: string) => {
    const statusMap: Record<string, { color: string; text: string }> = {
      ACTIVE: { color: 'success', text: '激活' },
      PAUSED: { color: 'warning', text: '暂停' },
      COMPLETED: { color: 'default', text: '已完成' },
      CANCELLED: { color: 'error', text: '已取消' },
    }
    const statusInfo = statusMap[status || ''] || { color: 'default', text: status || '未知' }
    return <Tag color={statusInfo.color}>{statusInfo.text}</Tag>
  }

  const columns: ColumnsType<MaintenancePlan> = [
    { title: '计划编号', dataIndex: 'planNo', key: 'planNo', width: 150 },
    { title: '计划名称', dataIndex: 'planName', key: 'planName', width: 200 },
    { title: '设备名称', dataIndex: 'assetName', key: 'assetName', width: 150 },
    { title: '维护类型', dataIndex: 'maintenanceType', key: 'maintenanceType', width: 120 },
    { title: '状态', dataIndex: 'status', key: 'status', width: 100, render: (status) => getStatusTag(status) },
    { title: '下次维护日期', dataIndex: 'nextMaintenanceDate', key: 'nextMaintenanceDate', width: 150 },
    {
      title: '操作',
      key: 'action',
      fixed: 'right',
      width: 250,
      render: (_, record) => (
        <Space size="middle">
          <Permission permission="maintenance:plan:edit">
            <Button type="link" icon={<EditOutlined />} onClick={() => handleEdit(record)}>编辑</Button>
          </Permission>
          {record.status === 'ACTIVE' && (
            <Permission permission="maintenance:plan:execute">
              <Button type="link" icon={<PlayCircleOutlined />} onClick={() => handleExecute(record.planId!)}>执行</Button>
            </Permission>
          )}
          <Permission permission="maintenance:plan:remove">
            <Popconfirm title="确定要删除吗？" onConfirm={() => handleDelete(record.planId!)}>
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
          <Permission permission="maintenance:plan:add">
            <Button type="primary" icon={<PlusOutlined />} onClick={handleAdd}>
              新增计划
            </Button>
          </Permission>
        </Space>
      </div>
      
      <Table
        columns={columns}
        dataSource={data}
        loading={loading}
        rowKey="planId"
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
        scroll={{ x: 1200 }}
      />

      <Modal
        title={editingRecord ? '编辑计划' : '新增计划'}
        open={modalVisible}
        onOk={handleSubmit}
        onCancel={() => setModalVisible(false)}
        width={800}
      >
        <Form form={form} layout="vertical">
          <Form.Item name="planName" label="计划名称" rules={[{ required: true }]}>
            <Input placeholder="请输入计划名称" />
          </Form.Item>
          <Form.Item name="planType" label="计划类型">
            <Select>
              <Select.Option value="PREVENTIVE">预防性</Select.Option>
              <Select.Option value="PREDICTIVE">预测性</Select.Option>
              <Select.Option value="CORRECTIVE">纠正性</Select.Option>
            </Select>
          </Form.Item>
          <Form.Item name="assetId" label="设备ID" rules={[{ required: true }]}>
            <Input placeholder="请输入设备ID" />
          </Form.Item>
          <Form.Item name="assetName" label="设备名称">
            <Input placeholder="请输入设备名称" />
          </Form.Item>
          <Form.Item name="maintenanceType" label="维护类型">
            <Select>
              <Select.Option value="DAILY">日常</Select.Option>
              <Select.Option value="WEEKLY">周度</Select.Option>
              <Select.Option value="MONTHLY">月度</Select.Option>
              <Select.Option value="QUARTERLY">季度</Select.Option>
              <Select.Option value="YEARLY">年度</Select.Option>
            </Select>
          </Form.Item>
          <Form.Item name="nextMaintenanceDate" label="下次维护日期">
            <DatePicker style={{ width: '100%' }} />
          </Form.Item>
        </Form>
      </Modal>

      <Modal
        title="导入计划"
        open={importModalVisible}
        onCancel={() => {
          setImportModalVisible(false)
          setImportFileList([])
        }}
        footer={null}
      >
        <Upload fileList={importFileList} beforeUpload={handleImport} onRemove={() => setImportFileList([])} maxCount={1} accept=".xlsx,.xls">
          <Button icon={<UploadOutlined />}>选择文件</Button>
        </Upload>
        <div style={{ marginTop: 16 }}>
          <Button type="link" onClick={() => downloadImportTemplate([{ header: '计划编号', key: 'planNo' }, { header: '计划名称', key: 'planName' }], '维护计划导入模板')}>
            下载导入模板
          </Button>
        </div>
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

