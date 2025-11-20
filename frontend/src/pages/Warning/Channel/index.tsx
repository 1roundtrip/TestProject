import { useState, useEffect } from 'react'
import { Table, Button, Space, Modal, Form, Input, InputNumber, Select, message, Tag, Upload, Drawer, Switch } from 'antd'
import { EditOutlined, DownloadOutlined, UploadOutlined, SettingOutlined } from '@ant-design/icons'
import type { ColumnsType } from 'antd/es/table'
import { Permission } from '@/components/Permission'
import { getChannelPage, updateChannel, enableChannel, type WarningChannel } from '@/api/warning/channel'
import { exportToExcel } from '@/utils/export'
import { parseExcelFile, downloadImportTemplate } from '@/utils/import'
import type { UploadFile } from 'antd/es/upload/interface'

export default function WarningChannelPage() {
  const [data, setData] = useState<WarningChannel[]>([])
  const [loading, setLoading] = useState(false)
  const [total, setTotal] = useState(0)
  const [current, setCurrent] = useState(1)
  const [pageSize, setPageSize] = useState(10)
  const [modalVisible, setModalVisible] = useState(false)
  const [editingRecord, setEditingRecord] = useState<WarningChannel | null>(null)
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
      const res = await getChannelPage({
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

  const handleEdit = (record: WarningChannel) => {
    setEditingRecord(record)
    // 只设置可编辑的字段，排除 channelCode（渠道编码不可修改）
    form.setFieldsValue({
      channelName: record.channelName,
      channelConfig: record.channelConfig,
      priority: record.priority,
      dailyLimit: record.dailyLimit,
      remark: record.remark,
    })
    setModalVisible(true)
  }

  const handleSubmit = async () => {
    try {
      const values = await form.validateFields()
      if (editingRecord?.channelId) {
        // 只传递可更新的字段，不包含 channelCode 和 channelType（这些字段不可修改）
        await updateChannel({
          channelId: editingRecord.channelId,
          channelName: values.channelName,
          channelConfig: values.channelConfig,
          priority: values.priority,
          dailyLimit: values.dailyLimit,
          remark: values.remark,
        } as any) // 使用 as any 因为 TypeScript 接口要求 channelCode 和 channelType
        message.success('更新成功')
        setModalVisible(false)
        loadData()
      }
    } catch (error: any) {
      message.error(error.response?.data?.msg || '操作失败')
    }
  }

  const handleEnable = async (id: number, enabled: boolean) => {
    try {
      await enableChannel(id, enabled ? 1 : 0)
      message.success('操作成功')
      loadData()
    } catch (error: any) {
      message.error(error.response?.data?.msg || '操作失败')
    }
  }

  const handleExport = () => {
    const exportColumns = columns.filter(col => 'dataIndex' in col && col.dataIndex) as Array<{ title: string; dataIndex: string }>
    exportToExcel(data, exportColumns, '预警渠道')
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
      ['channelCode', 'channelName', 'channelType', 'channelConfig', 'isEnabled', 'priority', 'dailyLimit'].map(field => ({ title: field, dataIndex: field })),
      '预警渠道导入模板'
    )
  }

  const columns: ColumnsType<WarningChannel> = [
    { title: '渠道编码', dataIndex: 'channelCode', key: 'channelCode' },
    { title: '渠道名称', dataIndex: 'channelName', key: 'channelName' },
    {
      title: '渠道类型',
      dataIndex: 'channelType',
      key: 'channelType',
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
    { title: '优先级', dataIndex: 'priority', key: 'priority' },
    { title: '每日限制', dataIndex: 'dailyLimit', key: 'dailyLimit' },
    { title: '当前数量', dataIndex: 'currentCount', key: 'currentCount' },
    {
      title: '状态',
      dataIndex: 'isEnabled',
      key: 'isEnabled',
      render: (enabled, record) => (
        <Space>
          <Tag color={enabled === 1 ? 'success' : 'default'}>
            {enabled === 1 ? '启用' : '停用'}
          </Tag>
          <Permission permission="warning:channel:enable">
            <Switch
              checked={enabled === 1}
              onChange={(checked) => handleEnable(record.channelId!, checked)}
              size="small"
            />
          </Permission>
        </Space>
      ),
    },
    {
      title: '操作',
      key: 'action',
      render: (_, record) => (
        <Space>
          <Permission permission="warning:channel:edit">
            <Button type="link" icon={<EditOutlined />} onClick={() => handleEdit(record)}>编辑</Button>
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
        </Space>
      </div>

      <Table
        columns={columns}
        dataSource={data}
        loading={loading}
        rowKey="channelId"
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
        title="编辑渠道"
        open={modalVisible}
        onOk={handleSubmit}
        onCancel={() => setModalVisible(false)}
        width={700}
      >
        <Form form={form} layout="vertical">
          <Form.Item name="channelName" label="渠道名称" rules={[{ required: true }]}>
            <Input />
          </Form.Item>
          <Form.Item name="channelConfig" label="渠道配置">
            <Input.TextArea rows={6} placeholder="JSON格式配置，例如：&#10;邮件：{&quot;smtp_host&quot;:&quot;&quot;,&quot;smtp_port&quot;:25}&#10;短信：{&quot;api_url&quot;:&quot;&quot;,&quot;api_key&quot;:&quot;&quot;}" />
          </Form.Item>
          <Form.Item name="priority" label="优先级">
            <InputNumber min={0} style={{ width: '100%' }} />
          </Form.Item>
          <Form.Item name="dailyLimit" label="每日发送限制">
            <InputNumber min={0} style={{ width: '100%' }} placeholder="0表示无限制" />
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
