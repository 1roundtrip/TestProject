import { useState, useEffect } from 'react'
import { Table, Button, Space, Modal, Form, Input, InputNumber, Select, message, Popconfirm, Tag, Upload, Drawer } from 'antd'
import { PlusOutlined, EditOutlined, DeleteOutlined, DownloadOutlined, UploadOutlined, SettingOutlined } from '@ant-design/icons'
import type { ColumnsType } from 'antd/es/table'
import { Permission } from '@/components/Permission'
import { getWarehousePage, createWarehouse, updateWarehouse, deleteWarehouse, type InventoryWarehouse } from '@/api/inventory/warehouse'
import { exportToExcel } from '@/utils/export'
import { parseExcelFile, downloadImportTemplate } from '@/utils/import'
import type { UploadFile } from 'antd/es/upload/interface'

export default function InventoryWarehousePage() {
  const [data, setData] = useState<InventoryWarehouse[]>([])
  const [loading, setLoading] = useState(false)
  const [total, setTotal] = useState(0)
  const [current, setCurrent] = useState(1)
  const [pageSize, setPageSize] = useState(10)
  const [modalVisible, setModalVisible] = useState(false)
  const [editingRecord, setEditingRecord] = useState<InventoryWarehouse | null>(null)
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
      const res = await getWarehousePage({
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
      warehouseType: 'GENERAL',
      status: 'ACTIVE',
    })
    setModalVisible(true)
  }

  const handleEdit = (record: InventoryWarehouse) => {
    setEditingRecord(record)
    form.setFieldsValue(record)
    setModalVisible(true)
  }

  const handleSubmit = async () => {
    try {
      const values = await form.validateFields()
      if (editingRecord?.warehouseId) {
        await updateWarehouse({ ...values, warehouseId: editingRecord.warehouseId })
        message.success('更新成功')
      } else {
        await createWarehouse(values)
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
      await deleteWarehouse(id)
      message.success('删除成功')
      loadData()
    } catch (error: any) {
      message.error(error.response?.data?.msg || '删除失败')
    }
  }

  const handleExport = () => {
    const exportColumns = columns.filter(col => 'dataIndex' in col && col.dataIndex) as Array<{ title: string; dataIndex: string; render?: (value: any, record: InventoryWarehouse) => any }>
    exportToExcel(data, exportColumns, '仓库列表')
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
      ['warehouseCode', 'warehouseName', 'warehouseType', 'location', 'managerName', 'contactPhone', 'area', 'capacity', 'capacityUnit', 'status'].map(field => ({ title: field, dataIndex: field })),
      '仓库导入模板'
    )
  }

  const columns: ColumnsType<InventoryWarehouse> = [
    {
      title: '仓库编码',
      dataIndex: 'warehouseCode',
      key: 'warehouseCode',
    },
    {
      title: '仓库名称',
      dataIndex: 'warehouseName',
      key: 'warehouseName',
    },
    {
      title: '仓库类型',
      dataIndex: 'warehouseType',
      key: 'warehouseType',
      render: (type) => {
        const typeMap: Record<string, string> = {
          GENERAL: '通用',
          RAW_MATERIAL: '原材料',
          FINISHED: '成品',
          SPARE_PART: '备件',
          DANGEROUS: '危险品',
        }
        return typeMap[type] || type
      },
    },
    {
      title: '位置',
      dataIndex: 'location',
      key: 'location',
    },
    {
      title: '管理员',
      dataIndex: 'managerName',
      key: 'managerName',
    },
    {
      title: '联系电话',
      dataIndex: 'contactPhone',
      key: 'contactPhone',
    },
    {
      title: '面积(㎡)',
      dataIndex: 'area',
      key: 'area',
    },
    {
      title: '容量',
      dataIndex: 'capacity',
      key: 'capacity',
      render: (capacity, record) => capacity ? `${capacity} ${record.capacityUnit || ''}` : '-',
    },
    {
      title: '状态',
      dataIndex: 'status',
      key: 'status',
      render: (status) => (
        <Tag color={status === 'ACTIVE' ? 'green' : 'red'}>
          {status === 'ACTIVE' ? '激活' : '停用'}
        </Tag>
      ),
    },
    {
      title: '操作',
      key: 'action',
      width: 200,
      render: (_, record) => (
        <Space>
          <Permission permission="inventory:warehouse:edit">
            <Button type="link" size="small" icon={<EditOutlined />} onClick={() => handleEdit(record)}>
              编辑
            </Button>
          </Permission>
          <Permission permission="inventory:warehouse:remove">
            <Popconfirm title="确定要删除吗？" onConfirm={() => handleDelete(record.warehouseId!)}>
              <Button type="link" size="small" danger icon={<DeleteOutlined />}>
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
          <Permission permission="inventory:warehouse:add">
            <Button type="primary" icon={<PlusOutlined />} onClick={handleAdd}>
              新增仓库
            </Button>
          </Permission>
        </Space>
      </div>
      <Table
        columns={columns}
        dataSource={data}
        loading={loading}
        rowKey="warehouseId"
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
        title={editingRecord ? '编辑仓库' : '新增仓库'}
        open={modalVisible}
        onOk={handleSubmit}
        onCancel={() => setModalVisible(false)}
        width={600}
      >
        <Form form={form} layout="vertical">
          <Form.Item
            name="warehouseCode"
            label="仓库编码"
            rules={[{ required: true, message: '请输入仓库编码' }]}
          >
            <Input placeholder="请输入仓库编码" />
          </Form.Item>
          <Form.Item
            name="warehouseName"
            label="仓库名称"
            rules={[{ required: true, message: '请输入仓库名称' }]}
          >
            <Input placeholder="请输入仓库名称" />
          </Form.Item>
          <Form.Item
            name="warehouseType"
            label="仓库类型"
            rules={[{ required: true, message: '请选择仓库类型' }]}
          >
            <Select placeholder="请选择仓库类型">
              <Select.Option value="GENERAL">通用</Select.Option>
              <Select.Option value="RAW_MATERIAL">原材料</Select.Option>
              <Select.Option value="FINISHED">成品</Select.Option>
              <Select.Option value="SPARE_PART">备件</Select.Option>
              <Select.Option value="DANGEROUS">危险品</Select.Option>
            </Select>
          </Form.Item>
          <Form.Item name="location" label="仓库位置">
            <Input placeholder="请输入仓库位置" />
          </Form.Item>
          <Form.Item name="managerName" label="管理员姓名">
            <Input placeholder="请输入管理员姓名" />
          </Form.Item>
          <Form.Item name="contactPhone" label="联系电话">
            <Input placeholder="请输入联系电话" />
          </Form.Item>
          <Form.Item name="area" label="仓库面积(㎡)">
            <InputNumber style={{ width: '100%' }} min={0} precision={2} placeholder="请输入仓库面积" />
          </Form.Item>
          <Form.Item name="capacity" label="仓库容量">
            <InputNumber style={{ width: '100%' }} min={0} precision={2} placeholder="请输入仓库容量" />
          </Form.Item>
          <Form.Item name="capacityUnit" label="容量单位">
            <Select placeholder="请选择容量单位">
              <Select.Option value="立方米">立方米</Select.Option>
              <Select.Option value="吨">吨</Select.Option>
              <Select.Option value="件">件</Select.Option>
            </Select>
          </Form.Item>
          <Form.Item name="status" label="状态">
            <Select placeholder="请选择状态">
              <Select.Option value="ACTIVE">激活</Select.Option>
              <Select.Option value="INACTIVE">停用</Select.Option>
            </Select>
          </Form.Item>
          <Form.Item name="remark" label="备注">
            <Input.TextArea rows={3} placeholder="请输入备注" />
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
