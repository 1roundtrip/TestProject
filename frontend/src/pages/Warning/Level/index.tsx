import { useState, useEffect } from 'react'
import { Table, Button, Space, Modal, Form, Input, InputNumber, Select, message, Popconfirm, Tag, Upload, Drawer } from 'antd'
import { PlusOutlined, EditOutlined, DeleteOutlined, DownloadOutlined, UploadOutlined, SettingOutlined } from '@ant-design/icons'
import type { ColumnsType } from 'antd/es/table'
import { Permission } from '@/components/Permission'
import { getLevelPage, createLevel, updateLevel, deleteLevel, type WarningLevel } from '@/api/warning/level'
import { exportToExcel } from '@/utils/export'
import { parseExcelFile, downloadImportTemplate } from '@/utils/import'
import type { UploadFile } from 'antd/es/upload/interface'

export default function WarningLevelPage() {
  const [data, setData] = useState<WarningLevel[]>([])
  const [loading, setLoading] = useState(false)
  const [total, setTotal] = useState(0)
  const [current, setCurrent] = useState(1)
  const [pageSize, setPageSize] = useState(10)
  const [modalVisible, setModalVisible] = useState(false)
  const [editingRecord, setEditingRecord] = useState<WarningLevel | null>(null)
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
      const res = await getLevelPage({
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
      isEnabled: 1,
      levelOrder: 0,
      levelColor: '#52c41a',
    })
    setModalVisible(true)
  }

  const handleEdit = (record: WarningLevel) => {
    setEditingRecord(record)
    form.setFieldsValue(record)
    setModalVisible(true)
  }

  const handleSubmit = async () => {
    try {
      const values = await form.validateFields()
      if (editingRecord?.levelId) {
        await updateLevel({ ...values, levelId: editingRecord.levelId })
        message.success('更新成功')
      } else {
        await createLevel(values)
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
      await deleteLevel(id)
      message.success('删除成功')
      loadData()
    } catch (error: any) {
      message.error(error.response?.data?.msg || '删除失败')
    }
  }

  const handleExport = () => {
    const exportColumns = columns.filter(col => 'dataIndex' in col && col.dataIndex) as Array<{ title: string; dataIndex: string }>
    exportToExcel(data, exportColumns, '预警级别')
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
      ['levelCode', 'levelName', 'levelColor', 'levelOrder', 'notificationChannels', 'isEnabled'].map(field => ({ title: field, dataIndex: field })),
      '预警级别导入模板'
    )
  }

  const columns: ColumnsType<WarningLevel> = [
    { title: '级别编码', dataIndex: 'levelCode', key: 'levelCode' },
    { title: '级别名称', dataIndex: 'levelName', key: 'levelName' },
    {
      title: '级别颜色',
      dataIndex: 'levelColor',
      key: 'levelColor',
      render: (color) => (
        <Space>
          <div style={{ width: 20, height: 20, backgroundColor: color, border: '1px solid #d9d9d9', borderRadius: 4 }} />
          <span>{color}</span>
        </Space>
      ),
    },
    { title: '排序', dataIndex: 'levelOrder', key: 'levelOrder' },
    {
      title: '通知渠道',
      dataIndex: 'notificationChannels',
      key: 'notificationChannels',
      render: (channels) => {
        if (!channels) return '-'
        const channelMap: Record<string, string> = {
          IN_APP: '站内信',
          EMAIL: '邮件',
          SMS: '短信',
          WECHAT: '微信',
          DINGTALK: '钉钉',
        }
        return channels.split(',').map((c: string) => channelMap[c.trim()] || c).join(', ')
      },
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
          <Permission permission="warning:level:edit">
            <Button type="link" icon={<EditOutlined />} onClick={() => handleEdit(record)}>编辑</Button>
          </Permission>
          <Permission permission="warning:level:remove">
            <Popconfirm title="确定要删除吗？" onConfirm={() => handleDelete(record.levelId!)}>
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
          <Permission permission="warning:level:add">
            <Button type="primary" icon={<PlusOutlined />} onClick={handleAdd}>
              新增级别
            </Button>
          </Permission>
        </Space>
      </div>

      <Table
        columns={columns}
        dataSource={data}
        loading={loading}
        rowKey="levelId"
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
        title={editingRecord ? '编辑级别' : '新增级别'}
        open={modalVisible}
        onOk={handleSubmit}
        onCancel={() => setModalVisible(false)}
        width={600}
      >
        <Form form={form} layout="vertical">
          <Form.Item name="levelCode" label="级别编码" rules={[{ required: true }]}>
            <Input />
          </Form.Item>
          <Form.Item name="levelName" label="级别名称" rules={[{ required: true }]}>
            <Input />
          </Form.Item>
          <Form.Item name="levelColor" label="级别颜色">
            <Input type="color" style={{ width: '100%', height: 40 }} />
          </Form.Item>
          <Form.Item name="levelOrder" label="排序">
            <InputNumber min={0} style={{ width: '100%' }} />
          </Form.Item>
          <Form.Item name="notificationChannels" label="通知渠道">
            <Select mode="multiple" placeholder="请选择通知渠道">
              <Select.Option value="IN_APP">站内信</Select.Option>
              <Select.Option value="EMAIL">邮件</Select.Option>
              <Select.Option value="SMS">短信</Select.Option>
              <Select.Option value="WECHAT">微信</Select.Option>
              <Select.Option value="DINGTALK">钉钉</Select.Option>
            </Select>
          </Form.Item>
          <Form.Item name="escalationRule" label="升级规则">
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
