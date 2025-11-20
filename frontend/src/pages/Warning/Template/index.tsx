import { useState, useEffect } from 'react'
import { Table, Button, Space, Modal, Form, Input, Select, message, Popconfirm, Tag, Upload, Drawer, Switch } from 'antd'
import { PlusOutlined, EditOutlined, DeleteOutlined, DownloadOutlined, UploadOutlined, SettingOutlined } from '@ant-design/icons'
import type { ColumnsType } from 'antd/es/table'
import { Permission } from '@/components/Permission'
import { getTemplatePage, createTemplate, updateTemplate, deleteTemplate, type WarningTemplate } from '@/api/warning/template'
import { exportToExcel } from '@/utils/export'
import { parseExcelFile, downloadImportTemplate } from '@/utils/import'
import type { UploadFile } from 'antd/es/upload/interface'

export default function WarningTemplatePage() {
  const [data, setData] = useState<WarningTemplate[]>([])
  const [loading, setLoading] = useState(false)
  const [total, setTotal] = useState(0)
  const [current, setCurrent] = useState(1)
  const [pageSize, setPageSize] = useState(10)
  const [modalVisible, setModalVisible] = useState(false)
  const [editingRecord, setEditingRecord] = useState<WarningTemplate | null>(null)
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
      const res = await getTemplatePage({
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
      templateType: 'IN_APP',
      isEnabled: 1,
      isDefault: 0,
    })
    setModalVisible(true)
  }

  const handleEdit = (record: WarningTemplate) => {
    setEditingRecord(record)
    form.setFieldsValue(record)
    setModalVisible(true)
  }

  const handleSubmit = async () => {
    try {
      const values = await form.validateFields()
      if (editingRecord?.templateId) {
        await updateTemplate({ ...values, templateId: editingRecord.templateId })
        message.success('更新成功')
      } else {
        await createTemplate(values)
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
      await deleteTemplate(id)
      message.success('删除成功')
      loadData()
    } catch (error: any) {
      message.error(error.response?.data?.msg || '删除失败')
    }
  }

  const handleExport = () => {
    const exportColumns = columns.filter(col => 'dataIndex' in col && col.dataIndex) as Array<{ title: string; dataIndex: string }>
    exportToExcel(data, exportColumns, '预警模板')
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
      ['templateCode', 'templateName', 'templateType', 'templateSubject', 'templateContent', 'isDefault', 'isEnabled'].map(field => ({ title: field, dataIndex: field })),
      '预警模板导入模板'
    )
  }

  const columns: ColumnsType<WarningTemplate> = [
    { title: '模板编码', dataIndex: 'templateCode', key: 'templateCode' },
    { title: '模板名称', dataIndex: 'templateName', key: 'templateName' },
    {
      title: '模板类型',
      dataIndex: 'templateType',
      key: 'templateType',
      render: (type) => {
        const typeMap: Record<string, string> = {
          IN_APP: '站内信',
          EMAIL: '邮件',
          SMS: '短信',
          WECHAT: '微信',
          DINGTALK: '钉钉',
        }
        return typeMap[type] || type
      },
    },
    { title: '预警类型', dataIndex: 'warningType', key: 'warningType' },
    {
      title: '是否默认',
      dataIndex: 'isDefault',
      key: 'isDefault',
      render: (isDefault) => (
        <Tag color={isDefault === 1 ? 'success' : 'default'}>
          {isDefault === 1 ? '是' : '否'}
        </Tag>
      ),
    },
    {
      title: '状态',
      dataIndex: 'isEnabled',
      key: 'isEnabled',
      render: (enabled) => (
        <Tag color={enabled === 1 ? 'success' : 'default'}>
          {enabled === 1 ? '启用' : '停用'}
        </Tag>
      ),
    },
    {
      title: '操作',
      key: 'action',
      render: (_, record) => (
        <Space>
          <Permission permission="warning:template:edit">
            <Button type="link" icon={<EditOutlined />} onClick={() => handleEdit(record)}>编辑</Button>
          </Permission>
          <Permission permission="warning:template:remove">
            <Popconfirm title="确定要删除吗？" onConfirm={() => handleDelete(record.templateId!)}>
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
          <Permission permission="warning:template:add">
            <Button type="primary" icon={<PlusOutlined />} onClick={handleAdd}>
              新增模板
            </Button>
          </Permission>
        </Space>
      </div>

      <Table
        columns={columns}
        dataSource={data}
        loading={loading}
        rowKey="templateId"
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
        title={editingRecord ? '编辑模板' : '新增模板'}
        open={modalVisible}
        onOk={handleSubmit}
        onCancel={() => setModalVisible(false)}
        width={800}
      >
        <Form form={form} layout="vertical">
          <Form.Item name="templateCode" label="模板编码" rules={[{ required: true }]}>
            <Input />
          </Form.Item>
          <Form.Item name="templateName" label="模板名称" rules={[{ required: true }]}>
            <Input />
          </Form.Item>
          <Form.Item name="templateType" label="模板类型" rules={[{ required: true }]}>
            <Select>
              <Select.Option value="IN_APP">站内信</Select.Option>
              <Select.Option value="EMAIL">邮件</Select.Option>
              <Select.Option value="SMS">短信</Select.Option>
              <Select.Option value="WECHAT">微信</Select.Option>
              <Select.Option value="DINGTALK">钉钉</Select.Option>
            </Select>
          </Form.Item>
          <Form.Item name="warningType" label="预警类型">
            <Input />
          </Form.Item>
          <Form.Item name="templateSubject" label="模板主题">
            <Input />
          </Form.Item>
          <Form.Item name="templateContent" label="模板内容" rules={[{ required: true }]}>
            <Input.TextArea rows={6} placeholder="支持变量：{warning_title}, {warning_content}, {trigger_time}等" />
          </Form.Item>
          <Form.Item name="templateVariables" label="模板变量">
            <Input.TextArea rows={3} placeholder="JSON格式" />
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
