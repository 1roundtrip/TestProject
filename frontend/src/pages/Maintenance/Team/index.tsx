import { useState, useEffect } from 'react'
import { Table, Button, Space, Modal, Form, Input, Select, message, Popconfirm, Tag, Upload, Drawer } from 'antd'
import { PlusOutlined, EditOutlined, DeleteOutlined, DownloadOutlined, UploadOutlined, SettingOutlined } from '@ant-design/icons'
import type { ColumnsType } from 'antd/es/table'
import { Permission } from '@/components/Permission'
import { getTeamPage, createTeam, updateTeam, deleteTeam, type MaintenanceTeam } from '@/api/maintenance/team'
import { exportToExcel } from '@/utils/export'
import { parseExcelFile, downloadImportTemplate } from '@/utils/import'
import type { UploadFile } from 'antd/es/upload/interface'

export default function MaintenanceTeamPage() {
  const [data, setData] = useState<MaintenanceTeam[]>([])
  const [loading, setLoading] = useState(false)
  const [total, setTotal] = useState(0)
  const [current, setCurrent] = useState(1)
  const [pageSize, setPageSize] = useState(10)
  const [modalVisible, setModalVisible] = useState(false)
  const [editingRecord, setEditingRecord] = useState<MaintenanceTeam | null>(null)
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
      const res = await getTeamPage({ current, size: pageSize })
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
    form.setFieldsValue({ status: 'ACTIVE', teamType: 'GENERAL' })
    setModalVisible(true)
  }

  const handleEdit = (record: MaintenanceTeam) => {
    setEditingRecord(record)
    form.setFieldsValue(record)
    setModalVisible(true)
  }

  const handleSubmit = async () => {
    try {
      const values = await form.validateFields()
      if (editingRecord?.teamId) {
        await updateTeam(values)
        message.success('更新成功')
      } else {
        await createTeam({ team: values, memberIds: [] })
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
      await deleteTeam(id)
      message.success('删除成功')
      loadData()
    } catch (error: any) {
      message.error(error.response?.data?.msg || '删除失败')
    }
  }

  const handleExport = () => {
    const exportColumns = columns.filter(col => 'dataIndex' in col && col.dataIndex) as Array<{ title: string; dataIndex: string }>
    exportToExcel(data, exportColumns, '维修团队')
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
      ['teamCode', 'teamName', 'teamType', 'leaderName', 'status'].map(field => ({ title: field, dataIndex: field })),
      '维修团队导入模板'
    )
  }

  const columns: ColumnsType<MaintenanceTeam> = [
    { title: '团队编码', dataIndex: 'teamCode', key: 'teamCode' },
    { title: '团队名称', dataIndex: 'teamName', key: 'teamName' },
    { title: '团队类型', dataIndex: 'teamType', key: 'teamType' },
    { title: '负责人', dataIndex: 'leaderName', key: 'leaderName' },
    { title: '成员数量', dataIndex: 'memberCount', key: 'memberCount' },
    { title: '状态', dataIndex: 'status', key: 'status', render: (status) => <Tag color={status === 'ACTIVE' ? 'success' : 'default'}>{status === 'ACTIVE' ? '激活' : '停用'}</Tag> },
    {
      title: '操作',
      key: 'action',
      render: (_, record) => (
        <Space>
          <Permission permission="maintenance:team:edit">
            <Button type="link" icon={<EditOutlined />} onClick={() => handleEdit(record)}>编辑</Button>
          </Permission>
          <Permission permission="maintenance:team:remove">
            <Popconfirm title="确定要删除吗？" onConfirm={() => handleDelete(record.teamId!)}>
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
          <Permission permission="maintenance:team:add">
            <Button type="primary" icon={<PlusOutlined />} onClick={handleAdd}>
              新增团队
            </Button>
          </Permission>
        </Space>
      </div>
      <Table
        columns={columns}
        dataSource={data}
        loading={loading}
        rowKey="teamId"
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
        title={editingRecord ? '编辑团队' : '新增团队'}
        open={modalVisible}
        onOk={handleSubmit}
        onCancel={() => setModalVisible(false)}
      >
        <Form form={form} layout="vertical">
          <Form.Item name="teamName" label="团队名称" rules={[{ required: true }]}>
            <Input />
          </Form.Item>
          <Form.Item name="teamType" label="团队类型">
            <Select>
              <Select.Option value="GENERAL">通用</Select.Option>
              <Select.Option value="ELECTRICAL">电气</Select.Option>
              <Select.Option value="MECHANICAL">机械</Select.Option>
              <Select.Option value="INSTRUMENT">仪表</Select.Option>
            </Select>
          </Form.Item>
          <Form.Item name="status" label="状态">
            <Select>
              <Select.Option value="ACTIVE">激活</Select.Option>
              <Select.Option value="INACTIVE">停用</Select.Option>
            </Select>
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

