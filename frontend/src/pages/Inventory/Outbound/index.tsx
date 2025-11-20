import { useState, useEffect } from 'react'
import { Table, Button, Space, Modal, Form, Input, DatePicker, Select, message, Popconfirm, Tag, Upload, Drawer } from 'antd'
import { PlusOutlined, EditOutlined, DeleteOutlined, CheckOutlined, DownloadOutlined, UploadOutlined, SettingOutlined } from '@ant-design/icons'
import type { ColumnsType } from 'antd/es/table'
import { Permission } from '@/components/Permission'
import { getOutboundPage, createOutbound, submitOutbound, approveOutbound, issueOutbound, type InventoryOutbound } from '@/api/inventory/outbound'
import { getWarehousePage, type InventoryWarehouse } from '@/api/inventory/warehouse'
import { exportToExcel } from '@/utils/export'
import { parseExcelFile, downloadImportTemplate } from '@/utils/import'
import dayjs from 'dayjs'
import type { UploadFile } from 'antd/es/upload/interface'

export default function InventoryOutboundPage() {
  const [data, setData] = useState<InventoryOutbound[]>([])
  const [warehouses, setWarehouses] = useState<InventoryWarehouse[]>([])
  const [loading, setLoading] = useState(false)
  const [total, setTotal] = useState(0)
  const [current, setCurrent] = useState(1)
  const [pageSize, setPageSize] = useState(10)
  const [modalVisible, setModalVisible] = useState(false)
  const [editingRecord, setEditingRecord] = useState<InventoryOutbound | null>(null)
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
      const res = await getOutboundPage({
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
      outboundType: 'SALE',
      outboundDate: dayjs(),
      status: 'DRAFT',
    })
    setModalVisible(true)
  }

  const handleEdit = (record: InventoryOutbound) => {
    setEditingRecord(record)
    form.setFieldsValue({
      ...record,
      outboundDate: record.outboundDate ? dayjs(record.outboundDate) : null,
    })
    setModalVisible(true)
  }

  const handleSubmit = async () => {
    try {
      const values = await form.validateFields()
      const submitData = {
        outbound: {
          ...values,
          outboundDate: values.outboundDate ? values.outboundDate.format('YYYY-MM-DD') : undefined,
        },
        details: [],
      }
      
      if (editingRecord?.outboundId) {
        message.warning('编辑功能待实现')
      } else {
        await createOutbound(submitData)
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
      await approveOutbound(id)
      message.success('审批成功')
      loadData()
    } catch (error: any) {
      message.error(error.response?.data?.msg || '审批失败')
    }
  }

  const handleIssue = async (id: number) => {
    try {
      await issueOutbound(id)
      message.success('发放成功')
      loadData()
    } catch (error: any) {
      message.error(error.response?.data?.msg || '发放失败')
    }
  }

  const handleExport = () => {
    exportToExcel(data, columns, '出库单列表')
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
    downloadImportTemplate('出库单导入模板', ['outboundNo', 'outboundType', 'warehouseCode', 'outboundDate', 'deptName', 'recipientName', 'handlerName', 'status'])
  }

  const columns: ColumnsType<InventoryOutbound> = [
    {
      title: '出库单号',
      dataIndex: 'outboundNo',
      key: 'outboundNo',
    },
    {
      title: '出库类型',
      dataIndex: 'outboundType',
      key: 'outboundType',
      render: (type) => {
        const typeMap: Record<string, string> = {
          SALE: '销售出库',
          PRODUCTION: '生产领用',
          MAINTENANCE: '维修领用',
          TRANSFER: '调拨出库',
          SCRAP: '报废出库',
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
      title: '去向单号',
      dataIndex: 'destinationNo',
      key: 'destinationNo',
    },
    {
      title: '出库日期',
      dataIndex: 'outboundDate',
      key: 'outboundDate',
    },
    {
      title: '领用部门',
      dataIndex: 'deptName',
      key: 'deptName',
    },
    {
      title: '领用人',
      dataIndex: 'recipientName',
      key: 'recipientName',
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
          ISSUED: { text: '已发放', color: 'success' },
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
            <Permission permission="inventory:outbound:approve">
              <Button type="link" size="small" icon={<CheckOutlined />} onClick={() => handleApprove(record.outboundId!)}>
                审批
              </Button>
            </Permission>
          )}
          {record.status === 'APPROVED' && (
            <Permission permission="inventory:outbound:issue">
              <Button type="link" size="small" icon={<CheckOutlined />} onClick={() => handleIssue(record.outboundId!)}>
                发放
              </Button>
            </Permission>
          )}
          <Permission permission="inventory:outbound:edit">
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
          <Permission permission="inventory:outbound:add">
            <Button type="primary" icon={<PlusOutlined />} onClick={handleAdd}>
              新增出库单
            </Button>
          </Permission>
        </Space>
      </div>
      <Table
        columns={columns}
        dataSource={data}
        loading={loading}
        rowKey="outboundId"
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
        title={editingRecord ? '编辑出库单' : '新增出库单'}
        open={modalVisible}
        onOk={handleSubmit}
        onCancel={() => setModalVisible(false)}
        width={700}
      >
        <Form form={form} layout="vertical">
          <Form.Item
            name="outboundType"
            label="出库类型"
            rules={[{ required: true, message: '请选择出库类型' }]}
          >
            <Select placeholder="请选择出库类型">
              <Select.Option value="SALE">销售出库</Select.Option>
              <Select.Option value="PRODUCTION">生产领用</Select.Option>
              <Select.Option value="MAINTENANCE">维修领用</Select.Option>
              <Select.Option value="TRANSFER">调拨出库</Select.Option>
              <Select.Option value="SCRAP">报废出库</Select.Option>
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
            name="outboundDate"
            label="出库日期"
            rules={[{ required: true, message: '请选择出库日期' }]}
          >
            <DatePicker style={{ width: '100%' }} />
          </Form.Item>
          <Form.Item name="destinationType" label="去向类型">
            <Select placeholder="请选择去向类型">
              <Select.Option value="SALE_ORDER">销售订单</Select.Option>
              <Select.Option value="PRODUCTION_ORDER">生产订单</Select.Option>
              <Select.Option value="MAINTENANCE_ORDER">维修工单</Select.Option>
            </Select>
          </Form.Item>
          <Form.Item name="destinationNo" label="去向单号">
            <Input placeholder="请输入去向单号" />
          </Form.Item>
          <Form.Item name="deptName" label="领用部门">
            <Input placeholder="请输入领用部门" />
          </Form.Item>
          <Form.Item name="recipientName" label="领用人">
            <Input placeholder="请输入领用人" />
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

