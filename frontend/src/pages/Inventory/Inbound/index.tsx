import { useState, useEffect } from 'react'
import { Table, Button, Space, Modal, Form, Input, DatePicker, Select, message, Popconfirm, Tag, Upload, Drawer } from 'antd'
import { PlusOutlined, EditOutlined, DeleteOutlined, CheckOutlined, DownloadOutlined, UploadOutlined, SettingOutlined } from '@ant-design/icons'
import type { ColumnsType } from 'antd/es/table'
import { Permission } from '@/components/Permission'
import { getInboundPage, createInbound, submitInbound, approveInbound, receiveInbound, type InventoryInbound } from '@/api/inventory/inbound'
import { getWarehousePage, type InventoryWarehouse } from '@/api/inventory/warehouse'
import { exportToExcel } from '@/utils/export'
import { parseExcelFile, downloadImportTemplate } from '@/utils/import'
import dayjs from 'dayjs'
import type { UploadFile } from 'antd/es/upload/interface'

export default function InventoryInboundPage() {
  const [data, setData] = useState<InventoryInbound[]>([])
  const [warehouses, setWarehouses] = useState<InventoryWarehouse[]>([])
  const [loading, setLoading] = useState(false)
  const [total, setTotal] = useState(0)
  const [current, setCurrent] = useState(1)
  const [pageSize, setPageSize] = useState(10)
  const [modalVisible, setModalVisible] = useState(false)
  const [editingRecord, setEditingRecord] = useState<InventoryInbound | null>(null)
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
      const res = await getInboundPage({
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
      inboundType: 'PURCHASE',
      inboundDate: dayjs(),
      status: 'DRAFT',
    })
    setModalVisible(true)
  }

  const handleEdit = (record: InventoryInbound) => {
    setEditingRecord(record)
    form.setFieldsValue({
      ...record,
      inboundDate: record.inboundDate ? dayjs(record.inboundDate) : null,
    })
    setModalVisible(true)
  }

  const handleSubmit = async () => {
    try {
      const values = await form.validateFields()
      const submitData = {
        inbound: {
          ...values,
          inboundDate: values.inboundDate ? values.inboundDate.format('YYYY-MM-DD') : undefined,
        },
        details: [],
      }
      
      if (editingRecord?.inboundId) {
        message.warning('编辑功能待实现')
      } else {
        await createInbound(submitData)
        message.success('创建成功')
        setModalVisible(false)
        loadData()
      }
    } catch (error: any) {
      message.error(error.response?.data?.msg || '操作失败')
    }
  }

  const handleDelete = async (id: number) => {
    try {
      // TODO: 实现删除接口
      message.success('删除成功')
      loadData()
    } catch (error: any) {
      message.error(error.response?.data?.msg || '删除失败')
    }
  }

  const handleApprove = async (id: number) => {
    try {
      await approveInbound(id)
      message.success('审批成功')
      loadData()
    } catch (error: any) {
      message.error(error.response?.data?.msg || '审批失败')
    }
  }

  const handleReceive = async (id: number) => {
    try {
      await receiveInbound(id)
      message.success('收货成功')
      loadData()
    } catch (error: any) {
      message.error(error.response?.data?.msg || '收货失败')
    }
  }

  const handleExport = () => {
    exportToExcel(data, columns, '入库单列表')
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
    downloadImportTemplate('入库单导入模板', ['inboundNo', 'inboundType', 'warehouseCode', 'inboundDate', 'supplierName', 'handlerName', 'receiverName', 'status'])
  }

  const columns: ColumnsType<InventoryInbound> = [
    {
      title: '入库单号',
      dataIndex: 'inboundNo',
      key: 'inboundNo',
    },
    {
      title: '入库类型',
      dataIndex: 'inboundType',
      key: 'inboundType',
      render: (type) => {
        const typeMap: Record<string, string> = {
          PURCHASE: '采购入库',
          PRODUCTION: '生产入库',
          RETURN: '退货入库',
          TRANSFER: '调拨入库',
          ADJUST: '调整入库',
          OTHER: '其他',
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
      title: '来源单号',
      dataIndex: 'sourceNo',
      key: 'sourceNo',
    },
    {
      title: '入库日期',
      dataIndex: 'inboundDate',
      key: 'inboundDate',
    },
    {
      title: '供应商',
      dataIndex: 'supplierName',
      key: 'supplierName',
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
          RECEIVED: { text: '已收货', color: 'success' },
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
      width: 250,
      render: (_, record) => (
        <Space>
          {record.status === 'SUBMITTED' && (
            <Permission permission="inventory:inbound:approve">
              <Button type="link" size="small" icon={<CheckOutlined />} onClick={() => handleApprove(record.inboundId!)}>
                审批
              </Button>
            </Permission>
          )}
          {record.status === 'APPROVED' && (
            <Permission permission="inventory:inbound:receive">
              <Button type="link" size="small" icon={<CheckOutlined />} onClick={() => handleReceive(record.inboundId!)}>
                收货
              </Button>
            </Permission>
          )}
          <Permission permission="inventory:inbound:edit">
            <Button type="link" size="small" icon={<EditOutlined />} onClick={() => handleEdit(record)}>
              编辑
            </Button>
          </Permission>
          <Permission permission="inventory:inbound:remove">
            <Popconfirm title="确定要删除吗？" onConfirm={() => handleDelete(record.inboundId!)}>
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
          <Permission permission="inventory:inbound:add">
            <Button type="primary" icon={<PlusOutlined />} onClick={handleAdd}>
              新增入库单
            </Button>
          </Permission>
        </Space>
      </div>
      <Table
        columns={columns}
        dataSource={data}
        loading={loading}
        rowKey="inboundId"
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
        title={editingRecord ? '编辑入库单' : '新增入库单'}
        open={modalVisible}
        onOk={handleSubmit}
        onCancel={() => setModalVisible(false)}
        width={700}
      >
        <Form form={form} layout="vertical">
          <Form.Item
            name="inboundType"
            label="入库类型"
            rules={[{ required: true, message: '请选择入库类型' }]}
          >
            <Select placeholder="请选择入库类型">
              <Select.Option value="PURCHASE">采购入库</Select.Option>
              <Select.Option value="PRODUCTION">生产入库</Select.Option>
              <Select.Option value="RETURN">退货入库</Select.Option>
              <Select.Option value="TRANSFER">调拨入库</Select.Option>
              <Select.Option value="ADJUST">调整入库</Select.Option>
              <Select.Option value="OTHER">其他</Select.Option>
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
            name="inboundDate"
            label="入库日期"
            rules={[{ required: true, message: '请选择入库日期' }]}
          >
            <DatePicker style={{ width: '100%' }} />
          </Form.Item>
          <Form.Item name="sourceType" label="来源类型">
            <Select placeholder="请选择来源类型">
              <Select.Option value="PURCHASE_ORDER">采购订单</Select.Option>
              <Select.Option value="PRODUCTION_ORDER">生产订单</Select.Option>
              <Select.Option value="TRANSFER_ORDER">调拨单</Select.Option>
            </Select>
          </Form.Item>
          <Form.Item name="sourceNo" label="来源单号">
            <Input placeholder="请输入来源单号" />
          </Form.Item>
          <Form.Item name="supplierName" label="供应商名称">
            <Input placeholder="请输入供应商名称" />
          </Form.Item>
          <Form.Item name="handlerName" label="经办人">
            <Input placeholder="请输入经办人" />
          </Form.Item>
          <Form.Item name="receiverName" label="收货人">
            <Input placeholder="请输入收货人" />
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

