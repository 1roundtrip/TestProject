import { useState, useEffect } from 'react'
import { Table, Button, Space, Select, message, Tag, Drawer, Form, Upload, Modal, Input } from 'antd'
import { PlusOutlined, DownloadOutlined, SettingOutlined, ReloadOutlined, UploadOutlined } from '@ant-design/icons'
import type { ColumnsType } from 'antd/es/table'
import { Permission } from '@/components/Permission'
import { getNotificationPage, createNotification, resendNotification, type WarningNotification } from '@/api/warning/notification'
import { exportToExcel } from '@/utils/export'
import { parseExcelFile, downloadImportTemplate } from '@/utils/import'
import type { UploadFile } from 'antd/es/upload/interface'

export default function WarningNotificationPage() {
  const [data, setData] = useState<WarningNotification[]>([])
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
    channelType?: string
    sendStatus?: string
  }>({})

  useEffect(() => {
    loadData()
  }, [current, pageSize, filters])

  const loadData = async () => {
    setLoading(true)
    try {
      const res = await getNotificationPage({
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
      sendStatus: 'PENDING',
      channelType: 'IN_APP',
      retryCount: 0,
    })
    setAddModalVisible(true)
  }

  const handleAddSubmit = async () => {
    try {
      const values = await addForm.validateFields()
      await createNotification(values)
      message.success('创建成功')
      setAddModalVisible(false)
      loadData()
    } catch (error: any) {
      message.error(error.response?.data?.msg || '创建失败')
    }
  }

  const handleResend = async (id: number) => {
    try {
      await resendNotification(id)
      message.success('重发成功')
      loadData()
    } catch (error: any) {
      message.error(error.response?.data?.msg || '重发失败')
    }
  }

  const handleExport = () => {
    const exportColumns = columns.filter(col => 'dataIndex' in col && col.dataIndex) as Array<{ title: string; dataIndex: string }>
    exportToExcel(data, exportColumns, '预警通知')
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
      ['notificationTitle', 'channelType', 'recipientName', 'recipientEmail', 'recipientPhone', 'sendStatus'].map(field => ({ title: field, dataIndex: field })),
      '预警通知导入模板'
    )
  }

  const getStatusTag = (status?: string) => {
    const statusMap: Record<string, { color: string; text: string }> = {
      PENDING: { color: 'default', text: '待发送' },
      SENDING: { color: 'processing', text: '发送中' },
      SUCCESS: { color: 'success', text: '成功' },
      FAILED: { color: 'error', text: '失败' },
    }
    const info = statusMap[status || ''] || { color: 'default', text: status || '未知' }
    return <Tag color={info.color}>{info.text}</Tag>
  }

  const columns: ColumnsType<WarningNotification> = [
    { title: '通知标题', dataIndex: 'notificationTitle', key: 'notificationTitle', width: 200 },
    {
      title: '渠道类型',
      dataIndex: 'channelType',
      key: 'channelType',
      width: 100,
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
    { title: '接收人', dataIndex: 'recipientName', key: 'recipientName', width: 120 },
    { title: '接收邮箱', dataIndex: 'recipientEmail', key: 'recipientEmail', width: 180 },
    { title: '接收手机', dataIndex: 'recipientPhone', key: 'recipientPhone', width: 120 },
    {
      title: '发送状态',
      dataIndex: 'sendStatus',
      key: 'sendStatus',
      width: 100,
      render: (status) => getStatusTag(status),
    },
    { title: '发送时间', dataIndex: 'sendTime', key: 'sendTime', width: 180 },
    { title: '重试次数', dataIndex: 'retryCount', key: 'retryCount', width: 100 },
    {
      title: '操作',
      key: 'action',
      fixed: 'right',
      width: 100,
      render: (_, record) => (
        <Space>
          {record.sendStatus === 'FAILED' && (
            <Permission permission="warning:notification:resend">
              <Button type="link" icon={<ReloadOutlined />} onClick={() => handleResend(record.notificationId!)}>
                重发
              </Button>
            </Permission>
          )}
        </Space>
      ),
    },
  ]

  return (
    <div>
      <div style={{ marginBottom: 16, display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
        <Space>
          <Select
            placeholder="渠道类型"
            allowClear
            style={{ width: 150 }}
            onChange={(value) => setFilters({ ...filters, channelType: value })}
          >
            <Select.Option value="IN_APP">站内信</Select.Option>
            <Select.Option value="EMAIL">邮件</Select.Option>
            <Select.Option value="SMS">短信</Select.Option>
            <Select.Option value="WECHAT">微信</Select.Option>
            <Select.Option value="DINGTALK">钉钉</Select.Option>
          </Select>
          <Select
            placeholder="发送状态"
            allowClear
            style={{ width: 150 }}
            onChange={(value) => setFilters({ ...filters, sendStatus: value })}
          >
            <Select.Option value="PENDING">待发送</Select.Option>
            <Select.Option value="SENDING">发送中</Select.Option>
            <Select.Option value="SUCCESS">成功</Select.Option>
            <Select.Option value="FAILED">失败</Select.Option>
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
          <Permission permission="warning:notification:add">
            <Button type="primary" icon={<PlusOutlined />} onClick={handleAdd}>
              新增通知
            </Button>
          </Permission>
        </Space>
      </div>

      <Table
        columns={columns}
        dataSource={data}
        loading={loading}
        rowKey="notificationId"
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
        title="新增预警通知"
        open={addModalVisible}
        onOk={handleAddSubmit}
        onCancel={() => setAddModalVisible(false)}
        width={700}
      >
        <Form form={addForm} layout="vertical">
          <Form.Item name="recordId" label="预警记录ID" rules={[{ required: true }]}>
            <Input type="number" placeholder="请输入预警记录ID" />
          </Form.Item>
          <Form.Item name="notificationTitle" label="通知标题" rules={[{ required: true }]}>
            <Input placeholder="请输入通知标题" />
          </Form.Item>
          <Form.Item name="channelType" label="渠道类型" rules={[{ required: true }]}>
            <Select>
              <Select.Option value="IN_APP">站内信</Select.Option>
              <Select.Option value="EMAIL">邮件</Select.Option>
              <Select.Option value="SMS">短信</Select.Option>
              <Select.Option value="WECHAT">微信</Select.Option>
              <Select.Option value="DINGTALK">钉钉</Select.Option>
            </Select>
          </Form.Item>
          <Form.Item name="recipientName" label="接收人姓名">
            <Input placeholder="请输入接收人姓名" />
          </Form.Item>
          <Form.Item name="recipientEmail" label="接收邮箱">
            <Input placeholder="请输入接收邮箱" />
          </Form.Item>
          <Form.Item name="recipientPhone" label="接收手机">
            <Input placeholder="请输入接收手机" />
          </Form.Item>
          <Form.Item name="notificationContent" label="通知内容">
            <Input.TextArea rows={4} placeholder="请输入通知内容" />
          </Form.Item>
          <Form.Item name="sendStatus" label="发送状态">
            <Select>
              <Select.Option value="PENDING">待发送</Select.Option>
              <Select.Option value="SENDING">发送中</Select.Option>
              <Select.Option value="SUCCESS">成功</Select.Option>
              <Select.Option value="FAILED">失败</Select.Option>
            </Select>
          </Form.Item>
        </Form>
      </Modal>

      <Modal
        title="导入预警通知数据"
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
