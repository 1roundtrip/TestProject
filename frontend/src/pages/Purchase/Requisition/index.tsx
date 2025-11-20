import { useState, useEffect } from 'react'
import { Table, Button, Space, Modal, Form, Input, InputNumber, DatePicker, Select, message, Popconfirm, Tag, Upload, Drawer } from 'antd'
import { PlusOutlined, EditOutlined, DeleteOutlined, CheckOutlined, CloseOutlined, DownloadOutlined, SearchOutlined, UploadOutlined, SettingOutlined } from '@ant-design/icons'
import type { ColumnsType } from 'antd/es/table'
import { getRequisitionPage, createRequisition, submitRequisition, approveRequisition, rejectRequisition, deleteRequisition, type PurchaseRequisition } from '@/api/purchase/requisition'
import { exportToExcel } from '@/utils/export'
import { parseExcelFile, downloadImportTemplate, validateImportData } from '@/utils/import'
import dayjs from 'dayjs'
import { Permission } from '@/components/Permission'
import type { UploadFile } from 'antd/es/upload/interface'

export default function PurchaseRequisitionPage() {
  const [data, setData] = useState<PurchaseRequisition[]>([])
  const [loading, setLoading] = useState(false)
  const [total, setTotal] = useState(0)
  const [current, setCurrent] = useState(1)
  const [pageSize, setPageSize] = useState(10)
  const [modalVisible, setModalVisible] = useState(false)
  const [editingRecord, setEditingRecord] = useState<PurchaseRequisition | null>(null)
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
      const res = await getRequisitionPage({
        current,
        size: pageSize,
      })
      setData(res.data.records)
      setTotal(res.data.total)
    } catch (error) {
      message.error('加载数据失败')
    } finally {
      setLoading(false)
    }
  }

  const handleAdd = () => {
    setEditingRecord(null)
    form.resetFields()
    form.setFieldsValue({
      urgentLevel: 'NORMAL',
      status: 'DRAFT',
    })
    setModalVisible(true)
  }

  const handleEdit = (record: PurchaseRequisition) => {
    setEditingRecord(record)
    form.setFieldsValue(record)
    setModalVisible(true)
  }

  const handleSubmit = async () => {
    try {
      const values = await form.validateFields()
      const submitData = {
        requisition: values,
        details: [],
      }
      
      if (editingRecord?.requisitionId) {
        message.warning('编辑功能待实现')
      } else {
        await createRequisition(submitData)
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
      await deleteRequisition(id)
      message.success('删除成功')
      loadData()
    } catch (error: any) {
      message.error(error.response?.data?.msg || '删除失败')
    }
  }

  const handleSubmitRequisition = async (id: number) => {
    try {
      await submitRequisition(id)
      message.success('提交成功')
      loadData()
    } catch (error: any) {
      message.error(error.response?.data?.msg || '提交失败')
    }
  }

  const handleApprove = async (id: number) => {
    try {
      await approveRequisition(id)
      message.success('审批通过')
      loadData()
    } catch (error: any) {
      message.error(error.response?.data?.msg || '审批失败')
    }
  }

  const handleReject = async (id: number) => {
    try {
      await rejectRequisition(id)
      message.success('已驳回')
      loadData()
    } catch (error: any) {
      message.error(error.response?.data?.msg || '操作失败')
    }
  }

  // 处理导入
  const handleImport = async (file: File) => {
    try {
      const data = await parseExcelFile(file)
      const validation = validateImportData(data, ['申请单号', '申请部门'])
      if (!validation.valid) {
        message.error(`数据验证失败：${validation.errors.join('; ')}`)
        return false
      }
      const importData = data.map(row => ({
        requisitionNo: row['申请单号'] || row['requisitionNo'],
        deptName: row['申请部门'] || row['deptName'],
        requisitionDate: row['申请日期'] || row['requisitionDate'],
        expectedDate: row['期望日期'] || row['expectedDate'],
        remark: row['备注'] || row['remark'],
      }))
      let successCount = 0
      let failCount = 0
      for (const item of importData) {
        try {
          await createRequisition({
            requisition: {
              ...item,
              requisitionDate: item.requisitionDate ? dayjs(item.requisitionDate).format('YYYY-MM-DD') : undefined,
              expectedDate: item.expectedDate ? dayjs(item.expectedDate).format('YYYY-MM-DD') : undefined,
            },
            details: [],
          })
          successCount++
        } catch (error) {
          failCount++
        }
      }
      message.success(`导入完成：成功 ${successCount} 条，失败 ${failCount} 条`)
      setImportModalVisible(false)
      setImportFileList([])
      loadData()
      return false
    } catch (error) {
      message.error('导入失败：' + (error instanceof Error ? error.message : '未知错误'))
      return false
    }
  }

  const handleDownloadTemplate = () => {
    const templateColumns = [
      { title: '申请单号', dataIndex: 'requisitionNo' },
      { title: '申请部门', dataIndex: 'deptName' },
      { title: '申请日期', dataIndex: 'requisitionDate' },
      { title: '期望日期', dataIndex: 'expectedDate' },
      { title: '备注', dataIndex: 'remark' },
    ]
    downloadImportTemplate(templateColumns, '采购申请导入模板')
  }

  const columns: ColumnsType<PurchaseRequisition> = [
    {
      title: '申请单号',
      dataIndex: 'requisitionNo',
      key: 'requisitionNo',
    },
    {
      title: '申请名称',
      dataIndex: 'requisitionName',
      key: 'requisitionName',
    },
    {
      title: '申请部门',
      dataIndex: 'deptName',
      key: 'deptName',
    },
    {
      title: '申请人',
      dataIndex: 'applicantName',
      key: 'applicantName',
    },
    {
      title: '总金额',
      dataIndex: 'totalAmount',
      key: 'totalAmount',
      render: (amount) => amount ? `¥${amount.toFixed(2)}` : '-',
    },
    {
      title: '紧急程度',
      dataIndex: 'urgentLevel',
      key: 'urgentLevel',
      render: (level) => {
        const levelMap: Record<string, { text: string; color: string }> = {
          URGENT: { text: '紧急', color: 'red' },
          NORMAL: { text: '正常', color: 'blue' },
          LOW: { text: '不急', color: 'default' },
        }
        const info = levelMap[level] || { text: level, color: 'default' }
        return <Tag color={info.color}>{info.text}</Tag>
      },
    },
    {
      title: '状态',
      dataIndex: 'status',
      key: 'status',
      render: (status) => {
        const statusMap: Record<string, { text: string; color: string }> = {
          DRAFT: { text: '草稿', color: 'default' },
          SUBMITTED: { text: '已提交', color: 'processing' },
          APPROVING: { text: '审批中', color: 'processing' },
          APPROVED: { text: '已审批', color: 'success' },
          REJECTED: { text: '已驳回', color: 'error' },
          ORDERED: { text: '已下单', color: 'success' },
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
            <>
              <Permission permission="purchase:requisition:edit">
                <Button type="link" onClick={() => handleEdit(record)}>编辑</Button>
              </Permission>
              <Permission permission="purchase:requisition:submit">
                <Button type="link" onClick={() => handleSubmitRequisition(record.requisitionId!)}>提交</Button>
              </Permission>
              <Permission permission="purchase:requisition:remove">
                <Popconfirm title="确定要删除吗？" onConfirm={() => handleDelete(record.requisitionId!)}>
                  <Button type="link" danger>删除</Button>
                </Popconfirm>
              </Permission>
            </>
          )}
          {(record.status === 'SUBMITTED' || record.status === 'APPROVING') && (
            <>
              <Permission permission="purchase:requisition:approve">
                <Button type="link" onClick={() => handleApprove(record.requisitionId!)}>审批通过</Button>
              </Permission>
              <Permission permission="purchase:requisition:approve">
                <Button type="link" danger onClick={() => handleReject(record.requisitionId!)}>驳回</Button>
              </Permission>
            </>
          )}
        </Space>
      ),
    },
  ]

  return (
    <div>
      <div style={{ marginBottom: 16, display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
        <Space>
          <Input placeholder="搜索申请单号" style={{ width: 200 }} allowClear />
          <Button icon={<SearchOutlined />}>搜索</Button>
        </Space>
        <Space>
          <Button 
            icon={<DownloadOutlined />} 
            onClick={() => exportToExcel(data, columns, '采购申请列表')}
            disabled={data.length === 0}
          >
            导出Excel
          </Button>
          <Button 
            icon={<UploadOutlined />}
            onClick={() => setImportModalVisible(true)}
          >
            导入Excel
          </Button>
          <Button 
            icon={<SettingOutlined />}
            onClick={() => setSettingDrawerVisible(true)}
          >
            设置
          </Button>
          <Permission permission="purchase:requisition:add">
            <Button type="primary" icon={<PlusOutlined />} onClick={handleAdd}>
              新增申请
            </Button>
          </Permission>
        </Space>
      </div>

      <Table
        columns={columns}
        dataSource={data}
        loading={loading}
        rowKey="requisitionId"
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
        title={editingRecord ? '编辑采购申请' : '新增采购申请'}
        open={modalVisible}
        onOk={handleSubmit}
        onCancel={() => setModalVisible(false)}
        width={800}
      >
        <Form form={form} layout="vertical">
          <Form.Item name="requisitionName" label="申请名称" rules={[{ required: true }]}>
            <Input />
          </Form.Item>
          <Form.Item name="deptName" label="申请部门">
            <Input />
          </Form.Item>
          <Form.Item name="urgentLevel" label="紧急程度">
            <Select>
              <Select.Option value="URGENT">紧急</Select.Option>
              <Select.Option value="NORMAL">正常</Select.Option>
              <Select.Option value="LOW">不急</Select.Option>
            </Select>
          </Form.Item>
          <Form.Item name="purpose" label="申请用途">
            <Input.TextArea rows={3} />
          </Form.Item>
          <Form.Item name="remark" label="备注">
            <Input.TextArea rows={3} />
          </Form.Item>
        </Form>
      </Modal>

      {/* 导入Modal */}
      <Modal
        title="导入采购申请数据"
        open={importModalVisible}
        onCancel={() => {
          setImportModalVisible(false)
          setImportFileList([])
        }}
        footer={null}
        width={600}
      >
        <Space direction="vertical" style={{ width: '100%' }} size="large">
          <div>
            <Button onClick={handleDownloadTemplate} type="link">
              下载导入模板
            </Button>
            <p style={{ marginTop: 8, color: '#666' }}>
              请先下载模板，按照模板格式填写数据后上传
            </p>
          </div>
          <Upload
            fileList={importFileList}
            beforeUpload={(file) => {
              handleImport(file)
              return false
            }}
            onChange={({ fileList }) => setImportFileList(fileList)}
            accept=".xlsx,.xls,.csv"
            maxCount={1}
          >
            <Button icon={<UploadOutlined />}>选择Excel文件</Button>
          </Upload>
        </Space>
      </Modal>

      {/* 设置抽屉 */}
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
          <div>
            <h4>数据管理</h4>
            <Space direction="vertical" style={{ width: '100%' }}>
              <Button block onClick={() => {
                handleDownloadTemplate()
                setSettingDrawerVisible(false)
              }}>
                下载导入模板
              </Button>
              <Button block onClick={() => {
                exportToExcel(data, columns, '采购申请列表')
                setSettingDrawerVisible(false)
              }} disabled={data.length === 0}>
                导出当前数据
              </Button>
            </Space>
          </div>
        </Space>
      </Drawer>
    </div>
  )
}

