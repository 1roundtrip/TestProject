import { useState, useEffect } from 'react'
import { Table, Button, Space, Modal, Form, Input, InputNumber, Select, message, Popconfirm, Tag, Upload, Drawer } from 'antd'
import { PlusOutlined, EditOutlined, DeleteOutlined, DownloadOutlined, UploadOutlined, SettingOutlined } from '@ant-design/icons'
import type { ColumnsType } from 'antd/es/table'
import { Permission } from '@/components/Permission'
import { getLocationPage, createLocation, updateLocation, deleteLocation, type InventoryLocation } from '@/api/inventory/location'
import { getWarehousePage, type InventoryWarehouse } from '@/api/inventory/warehouse'
import { exportToExcel } from '@/utils/export'
import { parseExcelFile, downloadImportTemplate } from '@/utils/import'
import type { UploadFile } from 'antd/es/upload/interface'

export default function InventoryLocationPage() {
  const [data, setData] = useState<InventoryLocation[]>([])
  const [warehouses, setWarehouses] = useState<InventoryWarehouse[]>([])
  const [loading, setLoading] = useState(false)
  const [total, setTotal] = useState(0)
  const [current, setCurrent] = useState(1)
  const [pageSize, setPageSize] = useState(10)
  const [modalVisible, setModalVisible] = useState(false)
  const [editingRecord, setEditingRecord] = useState<InventoryLocation | null>(null)
  const [form] = Form.useForm()
  const [importModalVisible, setImportModalVisible] = useState(false)
  const [settingDrawerVisible, setSettingDrawerVisible] = useState(false)
  const [importFileList, setImportFileList] = useState<UploadFile[]>([])

  useEffect(() => {
    loadData()
    loadWarehouses()
  }, [current, pageSize])

  const loadWarehouses = async () => {
    try {
      const res = await getWarehousePage({ current: 1, size: 1000 })
      setWarehouses(res.data.records || [])
    } catch (error) {
      console.error('加载仓库列表失败:', error)
    }
  }

  const loadData = async () => {
    setLoading(true)
    try {
      const res = await getLocationPage({
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
      locationType: 'NORMAL',
      status: 'ACTIVE',
    })
    setModalVisible(true)
  }

  const handleEdit = (record: InventoryLocation) => {
    setEditingRecord(record)
    form.setFieldsValue(record)
    setModalVisible(true)
  }

  const handleSubmit = async () => {
    try {
      const values = await form.validateFields()
      if (editingRecord?.locationId) {
        await updateLocation({ ...values, locationId: editingRecord.locationId })
        message.success('更新成功')
      } else {
        await createLocation(values)
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
      await deleteLocation(id)
      message.success('删除成功')
      loadData()
    } catch (error: any) {
      message.error(error.response?.data?.msg || '删除失败')
    }
  }

  const handleExport = () => {
    exportToExcel(data, columns, '库位列表')
  }

  const handleImport = async () => {
    if (importFileList.length === 0) {
      message.warning('请选择要导入的文件')
      return
    }
    try {
      const file = importFileList[0].originFileObj
      if (!file) return
      const excelData = await parseExcelFile(file)
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
    downloadImportTemplate('库位导入模板', ['warehouseCode', 'locationCode', 'locationName', 'locationType', 'zone', 'aisle', 'shelf', 'level', 'position', 'capacity', 'capacityUnit', 'status'])
  }

  const columns: ColumnsType<InventoryLocation> = [
    {
      title: '仓库',
      dataIndex: 'warehouseName',
      key: 'warehouseName',
    },
    {
      title: '库位编码',
      dataIndex: 'locationCode',
      key: 'locationCode',
    },
    {
      title: '库位名称',
      dataIndex: 'locationName',
      key: 'locationName',
    },
    {
      title: '库位类型',
      dataIndex: 'locationType',
      key: 'locationType',
      render: (type) => {
        const typeMap: Record<string, string> = {
          NORMAL: '普通',
          COLD: '冷藏',
          FROZEN: '冷冻',
          DANGEROUS: '危险品',
        }
        return typeMap[type] || type
      },
    },
    {
      title: '区域',
      dataIndex: 'zone',
      key: 'zone',
    },
    {
      title: '通道',
      dataIndex: 'aisle',
      key: 'aisle',
    },
    {
      title: '货架',
      dataIndex: 'shelf',
      key: 'shelf',
    },
    {
      title: '层',
      dataIndex: 'level',
      key: 'level',
    },
    {
      title: '位置',
      dataIndex: 'position',
      key: 'position',
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
      render: (status) => {
        const statusMap: Record<string, { text: string; color: string }> = {
          ACTIVE: { text: '激活', color: 'green' },
          INACTIVE: { text: '停用', color: 'red' },
          OCCUPIED: { text: '占用', color: 'orange' },
        }
        const info = statusMap[status] || { text: status, color: 'default' }
        return <Tag color={info.color}>{info.text}</Tag>
      },
    },
    {
      title: '操作',
      key: 'action',
      width: 200,
      render: (_, record) => (
        <Space>
          <Permission permission="inventory:location:edit">
            <Button type="link" size="small" icon={<EditOutlined />} onClick={() => handleEdit(record)}>
              编辑
            </Button>
          </Permission>
          <Permission permission="inventory:location:remove">
            <Popconfirm title="确定要删除吗？" onConfirm={() => handleDelete(record.locationId!)}>
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
          <Permission permission="inventory:location:add">
            <Button type="primary" icon={<PlusOutlined />} onClick={handleAdd}>
              新增库位
            </Button>
          </Permission>
        </Space>
      </div>
      <Table
        columns={columns}
        dataSource={data}
        loading={loading}
        rowKey="locationId"
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
        title={editingRecord ? '编辑库位' : '新增库位'}
        open={modalVisible}
        onOk={handleSubmit}
        onCancel={() => setModalVisible(false)}
        width={600}
      >
        <Form form={form} layout="vertical">
          <Form.Item
            name="warehouseId"
            label="仓库"
            rules={[{ required: true, message: '请选择仓库' }]}
          >
            <Select placeholder="请选择仓库" showSearch optionFilterProp="children">
              {warehouses.map(wh => (
                <Select.Option key={wh.warehouseId} value={wh.warehouseId}>
                  {wh.warehouseName} ({wh.warehouseCode})
                </Select.Option>
              ))}
            </Select>
          </Form.Item>
          <Form.Item
            name="locationCode"
            label="库位编码"
            rules={[{ required: true, message: '请输入库位编码' }]}
          >
            <Input placeholder="请输入库位编码" />
          </Form.Item>
          <Form.Item name="locationName" label="库位名称">
            <Input placeholder="请输入库位名称" />
          </Form.Item>
          <Form.Item
            name="locationType"
            label="库位类型"
            rules={[{ required: true, message: '请选择库位类型' }]}
          >
            <Select placeholder="请选择库位类型">
              <Select.Option value="NORMAL">普通</Select.Option>
              <Select.Option value="COLD">冷藏</Select.Option>
              <Select.Option value="FROZEN">冷冻</Select.Option>
              <Select.Option value="DANGEROUS">危险品</Select.Option>
            </Select>
          </Form.Item>
          <Form.Item name="zone" label="区域">
            <Input placeholder="请输入区域" />
          </Form.Item>
          <Form.Item name="aisle" label="通道">
            <Input placeholder="请输入通道" />
          </Form.Item>
          <Form.Item name="shelf" label="货架">
            <Input placeholder="请输入货架" />
          </Form.Item>
          <Form.Item name="level" label="层">
            <Input placeholder="请输入层" />
          </Form.Item>
          <Form.Item name="position" label="位置">
            <Input placeholder="请输入位置" />
          </Form.Item>
          <Form.Item name="capacity" label="容量">
            <InputNumber style={{ width: '100%' }} min={0} precision={2} placeholder="请输入容量" />
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
              <Select.Option value="OCCUPIED">占用</Select.Option>
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
