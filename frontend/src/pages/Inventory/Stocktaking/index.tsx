import { useState, useEffect } from 'react'
import { Table, Button, Space, Modal, Form, Input, DatePicker, Select, message, Popconfirm, Tag, Upload, Drawer } from 'antd'
import { PlusOutlined, EditOutlined, DeleteOutlined, CheckOutlined, DownloadOutlined, UploadOutlined, PlayCircleOutlined, SettingOutlined } from '@ant-design/icons'
import type { ColumnsType } from 'antd/es/table'
import { Permission } from '@/components/Permission'
import { getStocktakingPage, createStocktaking, startStocktaking, completeStocktaking, confirmStocktaking, type InventoryStocktaking } from '@/api/inventory/stocktaking'
import { getWarehousePage, type InventoryWarehouse } from '@/api/inventory/warehouse'
import { exportToExcel } from '@/utils/export'
import { parseExcelFile, downloadImportTemplate } from '@/utils/import'
import dayjs from 'dayjs'
import type { UploadFile } from 'antd/es/upload/interface'

export default function InventoryStocktakingPage() {
  const [data, setData] = useState<InventoryStocktaking[]>([])
  const [warehouses, setWarehouses] = useState<InventoryWarehouse[]>([])
  const [loading, setLoading] = useState(false)
  const [total, setTotal] = useState(0)
  const [current, setCurrent] = useState(1)
  const [pageSize, setPageSize] = useState(10)
  const [modalVisible, setModalVisible] = useState(false)
  const [editingRecord, setEditingRecord] = useState<InventoryStocktaking | null>(null)
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
      const res = await getStocktakingPage({
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
      stocktakingType: 'FULL',
      stocktakingDate: dayjs(),
      status: 'DRAFT',
    })
    setModalVisible(true)
  }

  const handleEdit = (record: InventoryStocktaking) => {
    setEditingRecord(record)
    form.setFieldsValue({
      ...record,
      stocktakingDate: record.stocktakingDate ? dayjs(record.stocktakingDate) : null,
    })
    setModalVisible(true)
  }

  const handleSubmit = async () => {
    try {
      const values = await form.validateFields()
      const submitData = {
        stocktaking: {
          ...values,
          stocktakingDate: values.stocktakingDate ? values.stocktakingDate.format('YYYY-MM-DD') : undefined,
        },
        details: [],
      }
      
      if (editingRecord?.stocktakingId) {
        message.warning('编辑功能待实现')
      } else {
        await createStocktaking(submitData)
        message.success('创建成功')
        setModalVisible(false)
        loadData()
      }
    } catch (error: any) {
      message.error(error.response?.data?.msg || '操作失败')
    }
  }

  const handleStart = async (id: number) => {
    try {
      await startStocktaking(id)
      message.success('开始盘点成功')
      loadData()
    } catch (error: any) {
      message.error(error.response?.data?.msg || '开始盘点失败')
    }
  }

  const handleComplete = async (id: number) => {
    try {
      await completeStocktaking(id)
      message.success('完成盘点成功')
      loadData()
    } catch (error: any) {
      message.error(error.response?.data?.msg || '完成盘点失败')
    }
  }

  const handleConfirm = async (id: number) => {
    try {
      await confirmStocktaking(id)
      message.success('确认成功')
      loadData()
    } catch (error: any) {
      message.error(error.response?.data?.msg || '确认失败')
    }
  }

  const handleExport = () => {
    exportToExcel(data, columns, '盘点单列表')
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
      message.success('导入成功')
      setImportModalVisible(false)
      setImportFileList([])
      loadData()
    } catch (error: any) {
      message.error(error.message || '导入失败')
    }
  }

  const handleDownloadTemplate = () => {
    downloadImportTemplate('盘点单导入模板', ['stocktakingNo', 'stocktakingType', 'warehouseCode', 'stocktakingDate', 'inventoryUserName', 'status'])
  }

  const columns: ColumnsType<InventoryStocktaking> = [
    {
      title: '盘点单号',
      dataIndex: 'stocktakingNo',
      key: 'stocktakingNo',
    },
    {
      title: '盘点类型',
      dataIndex: 'stocktakingType',
      key: 'stocktakingType',
      render: (type) => {
        const typeMap: Record<string, string> = {
          FULL: '全面盘点',
          PARTIAL: '部分盘点',
          SPOT: '抽查盘点',
        }
        return typeMap[type] || type
      },
    },
    {
      title: '仓库',
      dataIndex: 'warehouseName',
      key: 'warehouseName',
    },
    {
      title: '盘点日期',
      dataIndex: 'stocktakingDate',
      key: 'stocktakingDate',
    },
    {
      title: '应盘项数',
      dataIndex: 'totalItems',
      key: 'totalItems',
    },
    {
      title: '已盘项数',
      dataIndex: 'countedItems',
      key: 'countedItems',
    },
    {
      title: '盘盈项数',
      dataIndex: 'surplusItems',
      key: 'surplusItems',
      render: (items) => items ? <Tag color="green">{items}</Tag> : '-',
    },
    {
      title: '盘亏项数',
      dataIndex: 'shortageItems',
      key: 'shortageItems',
      render: (items) => items ? <Tag color="red">{items}</Tag> : '-',
    },
    {
      title: '状态',
      dataIndex: 'status',
      key: 'status',
      render: (status) => {
        const statusMap: Record<string, { text: string; color: string }> = {
          DRAFT: { text: '草稿', color: 'default' },
          IN_PROGRESS: { text: '盘点中', color: 'processing' },
          COMPLETED: { text: '已完成', color: 'success' },
          CONFIRMED: { text: '已确认', color: 'success' },
          CANCELLED: { text: '已取消', color: 'default' },
        }
        const info = statusMap[status] || { text: status, color: 'default' }
        return <Tag color={info.color}>{info.text}</Tag>
      },
    },
    {
      title: '操作',
      key: 'action',
      width: 300,
      render: (_, record) => (
        <Space>
          {record.status === 'DRAFT' && (
            <Permission permission="inventory:stocktaking:start">
              <Button type="link" size="small" icon={<PlayCircleOutlined />} onClick={() => handleStart(record.stocktakingId!)}>
                开始盘点
              </Button>
            </Permission>
          )}
          {record.status === 'IN_PROGRESS' && (
            <Permission permission="inventory:stocktaking:complete">
              <Button type="link" size="small" icon={<CheckOutlined />} onClick={() => handleComplete(record.stocktakingId!)}>
                完成盘点
              </Button>
            </Permission>
          )}
          {record.status === 'COMPLETED' && (
            <Permission permission="inventory:stocktaking:confirm">
              <Button type="link" size="small" icon={<CheckOutlined />} onClick={() => handleConfirm(record.stocktakingId!)}>
                确认
              </Button>
            </Permission>
          )}
          <Permission permission="inventory:stocktaking:edit">
            <Button type="link" size="small" icon={<EditOutlined />} onClick={() => handleEdit(record)}>
              编辑
            </Button>
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
          <Permission permission="inventory:stocktaking:add">
            <Button type="primary" icon={<PlusOutlined />} onClick={handleAdd}>
              新增盘点单
            </Button>
          </Permission>
        </Space>
      </div>
      <Table
        columns={columns}
        dataSource={data}
        loading={loading}
        rowKey="stocktakingId"
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
        title={editingRecord ? '编辑盘点单' : '新增盘点单'}
        open={modalVisible}
        onOk={handleSubmit}
        onCancel={() => setModalVisible(false)}
        width={700}
      >
        <Form form={form} layout="vertical">
          <Form.Item
            name="stocktakingType"
            label="盘点类型"
            rules={[{ required: true, message: '请选择盘点类型' }]}
          >
            <Select placeholder="请选择盘点类型">
              <Select.Option value="FULL">全面盘点</Select.Option>
              <Select.Option value="PARTIAL">部分盘点</Select.Option>
              <Select.Option value="SPOT">抽查盘点</Select.Option>
            </Select>
          </Form.Item>
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
            name="stocktakingDate"
            label="盘点日期"
            rules={[{ required: true, message: '请选择盘点日期' }]}
          >
            <DatePicker style={{ width: '100%' }} />
          </Form.Item>
          <Form.Item name="inventoryUserName" label="盘点人">
            <Input placeholder="请输入盘点人" />
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

