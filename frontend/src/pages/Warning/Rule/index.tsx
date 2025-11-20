import { useState, useEffect } from 'react'
import { Table, Button, Space, Modal, Form, Input, InputNumber, Select, message, Popconfirm, Tag, Upload, Drawer, Switch } from 'antd'
import { PlusOutlined, EditOutlined, DeleteOutlined, DownloadOutlined, UploadOutlined, SettingOutlined } from '@ant-design/icons'
import type { ColumnsType } from 'antd/es/table'
import { Permission } from '@/components/Permission'
import { getRulePage, createRule, updateRule, deleteRule, enableRule, type WarningRule } from '@/api/warning/rule'
import { exportToExcel } from '@/utils/export'
import { parseExcelFile, downloadImportTemplate } from '@/utils/import'
import type { UploadFile } from 'antd/es/upload/interface'

export default function WarningRulePage() {
  const [data, setData] = useState<WarningRule[]>([])
  const [loading, setLoading] = useState(false)
  const [total, setTotal] = useState(0)
  const [current, setCurrent] = useState(1)
  const [pageSize, setPageSize] = useState(10)
  const [modalVisible, setModalVisible] = useState(false)
  const [editingRecord, setEditingRecord] = useState<WarningRule | null>(null)
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
      const res = await getRulePage({
        current,
        size: pageSize,
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
    setEditingRecord(null)
    form.resetFields()
    form.setFieldsValue({
      ruleType: 'ASSET',
      checkFrequency: 'REALTIME',
      isEnabled: 1,
      priority: 0,
    })
    setModalVisible(true)
  }

  const handleEdit = (record: WarningRule) => {
    setEditingRecord(record)
    form.setFieldsValue(record)
    setModalVisible(true)
  }

  const handleSubmit = async () => {
    try {
      const values = await form.validateFields()
      if (editingRecord?.ruleId) {
        await updateRule({ ...values, ruleId: editingRecord.ruleId })
        message.success('更新成功')
      } else {
        await createRule(values)
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
      await deleteRule(id)
      message.success('删除成功')
      loadData()
    } catch (error: any) {
      message.error(error.response?.data?.msg || '删除失败')
    }
  }

  const handleEnable = async (id: number, enabled: boolean) => {
    try {
      await enableRule(id, enabled ? 1 : 0)
      message.success('操作成功')
      loadData()
    } catch (error: any) {
      message.error(error.response?.data?.msg || '操作失败')
    }
  }

  const handleExport = () => {
    const exportColumns = columns.filter(col => 'dataIndex' in col && col.dataIndex) as Array<{ title: string; dataIndex: string }>
    exportToExcel(data, exportColumns, '预警规则')
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
      ['ruleCode', 'ruleName', 'ruleType', 'warningCategory', 'checkFrequency', 'isEnabled', 'priority'].map(field => ({ title: field, dataIndex: field })),
      '预警规则导入模板'
    )
  }

  const columns: ColumnsType<WarningRule> = [
    { title: '规则编码', dataIndex: 'ruleCode', key: 'ruleCode' },
    { title: '规则名称', dataIndex: 'ruleName', key: 'ruleName' },
    {
      title: '规则类型',
      dataIndex: 'ruleType',
      key: 'ruleType',
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
    { title: '预警分类', dataIndex: 'warningCategory', key: 'warningCategory' },
    {
      title: '检查频率',
      dataIndex: 'checkFrequency',
      key: 'checkFrequency',
      render: (freq) => {
        const freqMap: Record<string, string> = {
          REALTIME: '实时',
          HOURLY: '每小时',
          DAILY: '每天',
          WEEKLY: '每周',
        }
        return freqMap[freq] || freq
      },
    },
    {
      title: '状态',
      dataIndex: 'isEnabled',
      key: 'isEnabled',
      render: (enabled, record) => (
        <Tag color={enabled === 1 ? 'success' : 'default'}>
          {enabled === 1 ? '启用' : '停用'}
        </Tag>
      ),
    },
    { title: '优先级', dataIndex: 'priority', key: 'priority' },
    {
      title: '操作',
      key: 'action',
      render: (_, record) => (
        <Space>
          <Permission permission="warning:rule:edit">
            <Button type="link" icon={<EditOutlined />} onClick={() => handleEdit(record)}>编辑</Button>
          </Permission>
          <Permission permission="warning:rule:enable">
            <Switch
              checked={record.isEnabled === 1}
              onChange={(checked) => handleEnable(record.ruleId!, checked)}
              size="small"
            />
          </Permission>
          <Permission permission="warning:rule:remove">
            <Popconfirm title="确定要删除吗？" onConfirm={() => handleDelete(record.ruleId!)}>
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
          <Permission permission="warning:rule:add">
            <Button type="primary" icon={<PlusOutlined />} onClick={handleAdd}>
              新增规则
            </Button>
          </Permission>
        </Space>
      </div>

      <Table
        columns={columns}
        dataSource={data}
        loading={loading}
        rowKey="ruleId"
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
        title={editingRecord ? '编辑规则' : '新增规则'}
        open={modalVisible}
        onOk={handleSubmit}
        onCancel={() => setModalVisible(false)}
        width={800}
      >
        <Form form={form} layout="vertical">
          <Form.Item name="ruleCode" label="规则编码" rules={[{ required: true }]}>
            <Input />
          </Form.Item>
          <Form.Item name="ruleName" label="规则名称" rules={[{ required: true }]}>
            <Input />
          </Form.Item>
          <Form.Item name="ruleType" label="规则类型" rules={[{ required: true }]}>
            <Select>
              <Select.Option value="ASSET">资产</Select.Option>
              <Select.Option value="INVENTORY">库存</Select.Option>
              <Select.Option value="PURCHASE">采购</Select.Option>
              <Select.Option value="MAINTENANCE">维修</Select.Option>
              <Select.Option value="FINANCE">财务</Select.Option>
            </Select>
          </Form.Item>
          <Form.Item name="warningCategory" label="预警分类" rules={[{ required: true }]}>
            <Input />
          </Form.Item>
          <Form.Item name="checkFrequency" label="检查频率">
            <Select>
              <Select.Option value="REALTIME">实时</Select.Option>
              <Select.Option value="HOURLY">每小时</Select.Option>
              <Select.Option value="DAILY">每天</Select.Option>
              <Select.Option value="WEEKLY">每周</Select.Option>
            </Select>
          </Form.Item>
          <Form.Item name="priority" label="优先级">
            <InputNumber min={0} style={{ width: '100%' }} />
          </Form.Item>
          <Form.Item name="ruleCondition" label="规则条件">
            <Input.TextArea rows={3} placeholder="JSON格式" />
          </Form.Item>
          <Form.Item name="ruleExpression" label="规则表达式">
            <Input.TextArea rows={3} />
          </Form.Item>
          <Form.Item name="remark" label="备注">
            <Input.TextArea rows={2} />
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

