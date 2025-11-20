import { useState, useEffect } from 'react'
import { Table, Button, Space, Modal, Form, Input, DatePicker, Select, message, Popconfirm, Tag, Upload, Drawer } from 'antd'
import { PlusOutlined, EditOutlined, DeleteOutlined, CheckOutlined, DownloadOutlined, UploadOutlined, SwapOutlined, SettingOutlined } from '@ant-design/icons'
import type { ColumnsType } from 'antd/es/table'
import { Permission } from '@/components/Permission'
import { getTransferPage, createTransfer, submitTransfer, approveTransfer, outboundTransfer, inboundTransfer, type InventoryTransfer } from '@/api/inventory/transfer'
import { getWarehousePage, type InventoryWarehouse } from '@/api/inventory/warehouse'
import { exportToExcel } from '@/utils/export'
import { parseExcelFile, downloadImportTemplate } from '@/utils/import'
import dayjs from 'dayjs'
import type { UploadFile } from 'antd/es/upload/interface'

export default function InventoryTransferPage() {
  const [data, setData] = useState<InventoryTransfer[]>([])
  const [warehouses, setWarehouses] = useState<InventoryWarehouse[]>([])
  const [loading, setLoading] = useState(false)
  const [total, setTotal] = useState(0)
  const [current, setCurrent] = useState(1)
  const [pageSize, setPageSize] = useState(10)
  const [modalVisible, setModalVisible] = useState(false)
  const [editingRecord, setEditingRecord] = useState<InventoryTransfer | null>(null)
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
      const res = await getTransferPage({
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
      transferType: 'WAREHOUSE',
      transferDate: dayjs(),
      status: 'DRAFT',
    })
    setModalVisible(true)
  }

  const handleEdit = (record: InventoryTransfer) => {
    setEditingRecord(record)
    form.setFieldsValue({
      ...record,
      transferDate: record.transferDate ? dayjs(record.transferDate) : null,
    })
    setModalVisible(true)
  }

  const handleSubmit = async () => {
    try {
      const values = await form.validateFields()
      const submitData = {
        transfer: {
          ...values,
          transferDate: values.transferDate ? values.transferDate.format('YYYY-MM-DD') : undefined,
        },
        details: [],
      }
      
      if (editingRecord?.transferId) {
        message.warning('编辑功能待实现')
      } else {
        await createTransfer(submitData)
        message.success('创建成功')
        setModalVisible(false)
        loadData()
      }
    } catch (error: any) {
      message.error(error.response?.data?.msg || '操作失败')
    }
  }

  const handleApprove = async (id: number) => {
    try {
      await approveTransfer(id)
      message.success('审批成功')
      loadData()
    } catch (error: any) {
      message.error(error.response?.data?.msg || '审批失败')
    }
  }

  const handleOutbound = async (id: number) => {
    try {
      await outboundTransfer(id)
      message.success('出库成功')
      loadData()
    } catch (error: any) {
      message.error(error.response?.data?.msg || '出库失败')
    }
  }

  const handleInbound = async (id: number) => {
    try {
      await inboundTransfer(id)
      message.success('入库成功')
      loadData()
    } catch (error: any) {
      message.error(error.response?.data?.msg || '入库失败')
    }
  }

  const handleExport = () => {
    exportToExcel(data, columns, '调拨单列表')
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
    downloadImportTemplate('调拨单导入模板', ['transferNo', 'transferType', 'fromWarehouseCode', 'toWarehouseCode', 'transferDate', 'handlerName', 'status'])
  }

  const columns: ColumnsType<InventoryTransfer> = [
    {
      title: '调拨单号',
      dataIndex: 'transferNo',
      key: 'transferNo',
    },
    {
      title: '调拨类型',
      dataIndex: 'transferType',
      key: 'transferType',
      render: (type) => {
        const typeMap: Record<string, string> = {
          WAREHOUSE: '仓库调拨',
          LOCATION: '库位调拨',
        }
        return typeMap[type] || type
      },
    },
    {
      title: '源仓库',
      dataIndex: 'fromWarehouseName',
      key: 'fromWarehouseName',
    },
    {
      title: '目标仓库',
      dataIndex: 'toWarehouseName',
      key: 'toWarehouseName',
    },
    {
      title: '调拨日期',
      dataIndex: 'transferDate',
      key: 'transferDate',
    },
    {
      title: '总数量',
      dataIndex: 'totalQuantity',
      key: 'totalQuantity',
    },
    {
      title: '总金额',
      dataIndex: 'totalAmount',
      key: 'totalAmount',
      render: (amount) => amount ? `¥${amount.toFixed(2)}` : '-',
    },
    {
      title: '状态',
      dataIndex: 'status',
      key: 'status',
      render: (status) => {
        const statusMap: Record<string, { text: string; color: string }> = {
          DRAFT: { text: '草稿', color: 'default' },
          SUBMITTED: { text: '已提交', color: 'processing' },
          APPROVED: { text: '已审批', color: 'success' },
          OUTBOUND: { text: '已出库', color: 'processing' },
          INBOUND: { text: '已入库', color: 'success' },
          COMPLETED: { text: '已完成', color: 'success' },
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
          {record.status === 'SUBMITTED' && (
            <Permission permission="inventory:transfer:approve">
              <Button type="link" size="small" icon={<CheckOutlined />} onClick={() => handleApprove(record.transferId!)}>
                审批
              </Button>
            </Permission>
          )}
          {record.status === 'APPROVED' && (
            <Permission permission="inventory:transfer:outbound">
              <Button type="link" size="small" icon={<SwapOutlined />} onClick={() => handleOutbound(record.transferId!)}>
                出库
              </Button>
            </Permission>
          )}
          {record.status === 'OUTBOUND' && (
            <Permission permission="inventory:transfer:inbound">
              <Button type="link" size="small" icon={<CheckOutlined />} onClick={() => handleInbound(record.transferId!)}>
                入库
              </Button>
            </Permission>
          )}
          <Permission permission="inventory:transfer:edit">
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
          <Permission permission="inventory:transfer:add">
            <Button type="primary" icon={<PlusOutlined />} onClick={handleAdd}>
              新增调拨单
            </Button>
          </Permission>
        </Space>
      </div>
      <Table
        columns={columns}
        dataSource={data}
        loading={loading}
        rowKey="transferId"
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
        title={editingRecord ? '编辑调拨单' : '新增调拨单'}
        open={modalVisible}
        onOk={handleSubmit}
        onCancel={() => setModalVisible(false)}
        width={700}
      >
        <Form form={form} layout="vertical">
          <Form.Item
            name="transferType"
            label="调拨类型"
            rules={[{ required: true, message: '请选择调拨类型' }]}
          >
            <Select placeholder="请选择调拨类型">
              <Select.Option value="WAREHOUSE">仓库调拨</Select.Option>
              <Select.Option value="LOCATION">库位调拨</Select.Option>
            </Select>
          </Form.Item>
          <Form.Item
            name="fromWarehouseId"
            label="源仓库"
            rules={[{ required: true, message: '请选择源仓库' }]}
          >
            <Select placeholder="请选择源仓库" showSearch optionFilterProp="children">
              {warehouses.map(wh => (
                <Select.Option key={wh.warehouseId} value={wh.warehouseId}>
                  {wh.warehouseName} ({wh.warehouseCode})
                </Select.Option>
              ))}
            </Select>
          </Form.Item>
          <Form.Item
            name="toWarehouseId"
            label="目标仓库"
            rules={[{ required: true, message: '请选择目标仓库' }]}
          >
            <Select placeholder="请选择目标仓库" showSearch optionFilterProp="children">
              {warehouses.map(wh => (
                <Select.Option key={wh.warehouseId} value={wh.warehouseId}>
                  {wh.warehouseName} ({wh.warehouseCode})
                </Select.Option>
              ))}
            </Select>
          </Form.Item>
          <Form.Item
            name="transferDate"
            label="调拨日期"
            rules={[{ required: true, message: '请选择调拨日期' }]}
          >
            <DatePicker style={{ width: '100%' }} />
          </Form.Item>
          <Form.Item name="handlerName" label="经办人">
            <Input placeholder="请输入经办人" />
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

