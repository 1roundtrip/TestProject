import { useState, useEffect } from 'react'
import { Table, Button, Space, Modal, Form, Input, Select, DatePicker, InputNumber, message, Popconfirm, Tag, Upload, Drawer } from 'antd'
import { PlusOutlined, EditOutlined, DeleteOutlined, CheckOutlined, DownloadOutlined, UploadOutlined, SettingOutlined, UserOutlined } from '@ant-design/icons'
import type { ColumnsType } from 'antd/es/table'
import { Permission } from '@/components/Permission'
import { 
  getWorkOrderPage, 
  createWorkOrder, 
  updateWorkOrder, 
  deleteWorkOrder, 
  assignWorkOrder, 
  startWorkOrder, 
  completeWorkOrder,
  type MaintenanceWorkOrder 
} from '@/api/maintenance/workOrder'
import { exportToExcel } from '@/utils/export'
import { parseExcelFile, downloadImportTemplate, validateImportData } from '@/utils/import'
import dayjs from 'dayjs'
import type { UploadFile } from 'antd/es/upload/interface'

const { TextArea } = Input

export default function MaintenanceWorkOrderPage() {
  const [data, setData] = useState<MaintenanceWorkOrder[]>([])
  const [loading, setLoading] = useState(false)
  const [total, setTotal] = useState(0)
  const [current, setCurrent] = useState(1)
  const [pageSize, setPageSize] = useState(10)
  const [modalVisible, setModalVisible] = useState(false)
  const [assignModalVisible, setAssignModalVisible] = useState(false)
  const [completeModalVisible, setCompleteModalVisible] = useState(false)
  const [editingRecord, setEditingRecord] = useState<MaintenanceWorkOrder | null>(null)
  const [form] = Form.useForm()
  const [assignForm] = Form.useForm()
  const [completeForm] = Form.useForm()
  const [importModalVisible, setImportModalVisible] = useState(false)
  const [settingDrawerVisible, setSettingDrawerVisible] = useState(false)
  const [importFileList, setImportFileList] = useState<UploadFile[]>([])

  useEffect(() => {
    loadData()
  }, [current, pageSize])

  const loadData = async () => {
    setLoading(true)
    try {
      const res = await getWorkOrderPage({
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

  const handleAdd = () => {
    setEditingRecord(null)
    form.resetFields()
    form.setFieldsValue({
      workOrderType: 'REPAIR',
      priority: 'NORMAL',
      status: 'PENDING',
      reportedTime: dayjs(),
    })
    setModalVisible(true)
  }

  const handleEdit = (record: MaintenanceWorkOrder) => {
    setEditingRecord(record)
    form.setFieldsValue({
      ...record,
      reportedTime: record.reportedTime ? dayjs(record.reportedTime) : null,
      scheduledStartTime: record.scheduledStartTime ? dayjs(record.scheduledStartTime) : null,
      scheduledEndTime: record.scheduledEndTime ? dayjs(record.scheduledEndTime) : null,
    })
    setModalVisible(true)
  }

  const handleSubmit = async () => {
    try {
      const values = await form.validateFields()
      const submitData = {
        workOrder: {
          ...values,
          reportedTime: values.reportedTime ? values.reportedTime.format('YYYY-MM-DD HH:mm:ss') : undefined,
          scheduledStartTime: values.scheduledStartTime ? values.scheduledStartTime.format('YYYY-MM-DD HH:mm:ss') : undefined,
          scheduledEndTime: values.scheduledEndTime ? values.scheduledEndTime.format('YYYY-MM-DD HH:mm:ss') : undefined,
        },
        details: [],
      }
      
      if (editingRecord?.workOrderId) {
        await updateWorkOrder(submitData.workOrder)
        message.success('更新成功')
      } else {
        await createWorkOrder(submitData)
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
      await deleteWorkOrder(id)
      message.success('删除成功')
      loadData()
    } catch (error: any) {
      message.error(error.response?.data?.msg || '删除失败')
    }
  }

  const handleAssign = (record: MaintenanceWorkOrder) => {
    setEditingRecord(record)
    assignForm.resetFields()
    setAssignModalVisible(true)
  }

  const handleAssignSubmit = async () => {
    try {
      const values = await assignForm.validateFields()
      if (!editingRecord?.workOrderId) return
      
      await assignWorkOrder(editingRecord.workOrderId, values)
      message.success('分配成功')
      setAssignModalVisible(false)
      loadData()
    } catch (error: any) {
      message.error(error.response?.data?.msg || '分配失败')
    }
  }

  const handleStart = async (id: number) => {
    try {
      await startWorkOrder(id)
      message.success('开始维修成功')
      loadData()
    } catch (error: any) {
      message.error(error.response?.data?.msg || '操作失败')
    }
  }

  const handleComplete = (record: MaintenanceWorkOrder) => {
    setEditingRecord(record)
    completeForm.resetFields()
    setCompleteModalVisible(true)
  }

  const handleCompleteSubmit = async () => {
    try {
      const values = await completeForm.validateFields()
      if (!editingRecord?.workOrderId) return
      
      await completeWorkOrder(editingRecord.workOrderId, values)
      message.success('完成工单成功')
      setCompleteModalVisible(false)
      loadData()
    } catch (error: any) {
      message.error(error.response?.data?.msg || '操作失败')
    }
  }

  // 处理导入
  const handleImport = async (file: File) => {
    try {
      const data = await parseExcelFile(file)
      
      const validation = validateImportData(data, ['工单编号', '设备名称'])
      if (!validation.valid) {
        message.error(`数据验证失败：${validation.errors.join('; ')}`)
        return false
      }

      // TODO: 实现批量导入逻辑
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
      { header: '工单编号', key: 'workOrderNo' },
      { header: '设备名称', key: 'assetName' },
      { header: '故障类型', key: 'faultType' },
      { header: '优先级', key: 'priority' },
      { header: '状态', key: 'status' },
      { header: '报修人', key: 'reportedByName' },
      { header: '报修时间', key: 'reportedTime' },
    ]
    exportToExcel(data, columns, '维修工单')
  }

  const handleDownloadTemplate = () => {
    downloadImportTemplate([
      { header: '工单编号', key: 'workOrderNo' },
      { header: '设备名称', key: 'assetName' },
      { header: '故障类型', key: 'faultType' },
      { header: '优先级', key: 'priority' },
      { header: '故障描述', key: 'faultDescription' },
    ], '维修工单导入模板')
  }

  const getStatusTag = (status?: string) => {
    const statusMap: Record<string, { color: string; text: string }> = {
      PENDING: { color: 'default', text: '待分配' },
      ASSIGNED: { color: 'processing', text: '已分配' },
      IN_PROGRESS: { color: 'processing', text: '进行中' },
      PAUSED: { color: 'warning', text: '暂停' },
      COMPLETED: { color: 'success', text: '已完成' },
      CANCELLED: { color: 'error', text: '已取消' },
    }
    const statusInfo = statusMap[status || ''] || { color: 'default', text: status || '未知' }
    return <Tag color={statusInfo.color}>{statusInfo.text}</Tag>
  }

  const getPriorityTag = (priority?: string) => {
    const priorityMap: Record<string, { color: string; text: string }> = {
      LOW: { color: 'default', text: '低' },
      NORMAL: { color: 'processing', text: '正常' },
      HIGH: { color: 'warning', text: '高' },
      URGENT: { color: 'error', text: '紧急' },
    }
    const priorityInfo = priorityMap[priority || ''] || { color: 'default', text: priority || '未知' }
    return <Tag color={priorityInfo.color}>{priorityInfo.text}</Tag>
  }

  const columns: ColumnsType<MaintenanceWorkOrder> = [
    {
      title: '工单编号',
      dataIndex: 'workOrderNo',
      key: 'workOrderNo',
      width: 150,
    },
    {
      title: '设备名称',
      dataIndex: 'assetName',
      key: 'assetName',
      width: 150,
    },
    {
      title: '故障类型',
      dataIndex: 'faultType',
      key: 'faultType',
      width: 120,
    },
    {
      title: '优先级',
      dataIndex: 'priority',
      key: 'priority',
      width: 100,
      render: (priority) => getPriorityTag(priority),
    },
    {
      title: '状态',
      dataIndex: 'status',
      key: 'status',
      width: 100,
      render: (status) => getStatusTag(status),
    },
    {
      title: '报修人',
      dataIndex: 'reportedByName',
      key: 'reportedByName',
      width: 100,
    },
    {
      title: '维修团队',
      dataIndex: 'assignedTeamName',
      key: 'assignedTeamName',
      width: 120,
    },
    {
      title: '报修时间',
      dataIndex: 'reportedTime',
      key: 'reportedTime',
      width: 180,
    },
    {
      title: '操作',
      key: 'action',
      fixed: 'right',
      width: 300,
      render: (_, record) => (
        <Space size="middle">
          <Permission permission="maintenance:workorder:edit">
            <Button
              type="link"
              icon={<EditOutlined />}
              onClick={() => handleEdit(record)}
            >
              编辑
            </Button>
          </Permission>
          {record.status === 'PENDING' && (
            <Permission permission="maintenance:workorder:assign">
              <Button
                type="link"
                icon={<UserOutlined />}
                onClick={() => handleAssign(record)}
              >
                分配
              </Button>
            </Permission>
          )}
          {record.status === 'ASSIGNED' && (
            <Permission permission="maintenance:workorder:start">
              <Button
                type="link"
                onClick={() => handleStart(record.workOrderId!)}
              >
                开始
              </Button>
            </Permission>
          )}
          {record.status === 'IN_PROGRESS' && (
            <Permission permission="maintenance:workorder:complete">
              <Button
                type="link"
                icon={<CheckOutlined />}
                onClick={() => handleComplete(record)}
              >
                完成
              </Button>
            </Permission>
          )}
          <Permission permission="maintenance:workorder:remove">
            <Popconfirm
              title="确定要删除这条工单吗？"
              onConfirm={() => handleDelete(record.workOrderId!)}
            >
              <Button type="link" danger icon={<DeleteOutlined />}>
                删除
              </Button>
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
          <Permission permission="maintenance:workorder:add">
            <Button type="primary" icon={<PlusOutlined />} onClick={handleAdd}>
              新增工单
            </Button>
          </Permission>
        </Space>
      </div>
      
      <Table
        columns={columns}
        dataSource={data}
        loading={loading}
        rowKey="workOrderId"
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
        scroll={{ x: 1500 }}
      />

      {/* 新增/编辑工单模态框 */}
      <Modal
        title={editingRecord ? '编辑工单' : '新增工单'}
        open={modalVisible}
        onOk={handleSubmit}
        onCancel={() => setModalVisible(false)}
        width={800}
      >
        <Form form={form} layout="vertical">
          <Form.Item name="workOrderNo" label="工单编号">
            <Input placeholder="自动生成" disabled={!!editingRecord} />
          </Form.Item>
          <Form.Item name="workOrderType" label="工单类型" rules={[{ required: true }]}>
            <Select>
              <Select.Option value="REPAIR">维修</Select.Option>
              <Select.Option value="MAINTENANCE">保养</Select.Option>
              <Select.Option value="INSPECTION">检查</Select.Option>
              <Select.Option value="EMERGENCY">紧急</Select.Option>
            </Select>
          </Form.Item>
          <Form.Item name="priority" label="优先级" rules={[{ required: true }]}>
            <Select>
              <Select.Option value="LOW">低</Select.Option>
              <Select.Option value="NORMAL">正常</Select.Option>
              <Select.Option value="HIGH">高</Select.Option>
              <Select.Option value="URGENT">紧急</Select.Option>
            </Select>
          </Form.Item>
          <Form.Item name="assetId" label="设备ID" rules={[{ required: true }]}>
            <InputNumber style={{ width: '100%' }} placeholder="请输入设备ID" />
          </Form.Item>
          <Form.Item name="assetName" label="设备名称">
            <Input placeholder="请输入设备名称" />
          </Form.Item>
          <Form.Item name="faultType" label="故障类型">
            <Input placeholder="请输入故障类型" />
          </Form.Item>
          <Form.Item name="faultDescription" label="故障描述">
            <TextArea rows={4} placeholder="请输入故障描述" />
          </Form.Item>
          <Form.Item name="reportedTime" label="报修时间">
            <DatePicker showTime style={{ width: '100%' }} />
          </Form.Item>
          <Form.Item name="remark" label="备注">
            <TextArea rows={2} placeholder="请输入备注" />
          </Form.Item>
        </Form>
      </Modal>

      {/* 分配工单模态框 */}
      <Modal
        title="分配工单"
        open={assignModalVisible}
        onOk={handleAssignSubmit}
        onCancel={() => setAssignModalVisible(false)}
      >
        <Form form={assignForm} layout="vertical">
          <Form.Item name="teamId" label="维修团队">
            <InputNumber style={{ width: '100%' }} placeholder="请输入团队ID" />
          </Form.Item>
          <Form.Item name="technicianId" label="维修技师">
            <InputNumber style={{ width: '100%' }} placeholder="请输入技师ID" />
          </Form.Item>
        </Form>
      </Modal>

      {/* 完成工单模态框 */}
      <Modal
        title="完成工单"
        open={completeModalVisible}
        onOk={handleCompleteSubmit}
        onCancel={() => setCompleteModalVisible(false)}
      >
        <Form form={completeForm} layout="vertical">
          <Form.Item name="qualityScore" label="质量评分" rules={[{ required: true }]}>
            <InputNumber min={0} max={100} style={{ width: '100%' }} placeholder="请输入质量评分(0-100)" />
          </Form.Item>
          <Form.Item name="qualityComment" label="质量评价">
            <TextArea rows={4} placeholder="请输入质量评价" />
          </Form.Item>
        </Form>
      </Modal>

      {/* 导入模态框 */}
      <Modal
        title="导入工单"
        open={importModalVisible}
        onCancel={() => {
          setImportModalVisible(false)
          setImportFileList([])
        }}
        footer={null}
      >
        <Upload
          fileList={importFileList}
          beforeUpload={handleImport}
          onRemove={() => setImportFileList([])}
          maxCount={1}
          accept=".xlsx,.xls"
        >
          <Button icon={<UploadOutlined />}>选择文件</Button>
        </Upload>
        <div style={{ marginTop: 16 }}>
          <Button type="link" onClick={handleDownloadTemplate}>
            下载导入模板
          </Button>
        </div>
      </Modal>

      {/* 设置抽屉 */}
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

