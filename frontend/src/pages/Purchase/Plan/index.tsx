import { useState, useEffect } from 'react'
import { Table, Button, Space, Modal, Form, Input, InputNumber, DatePicker, Select, message, Popconfirm, Tag, Upload, Drawer } from 'antd'
import { PlusOutlined, EditOutlined, DeleteOutlined, CheckOutlined, CloseOutlined, DownloadOutlined, SearchOutlined, UploadOutlined, SettingOutlined } from '@ant-design/icons'
import type { ColumnsType } from 'antd/es/table'
import { getPlanPage, createPlan, submitPlan, approvePlan, rejectPlan, deletePlan, getPlanDetails, type PurchasePlan, type PurchasePlanDetail } from '@/api/purchase/plan'
import { exportToExcel } from '@/utils/export'
import { parseExcelFile, downloadImportTemplate, validateImportData } from '@/utils/import'
import dayjs from 'dayjs'
import { Permission } from '@/components/Permission'
import type { UploadFile } from 'antd/es/upload/interface'

export default function PurchasePlanPage() {
  const [data, setData] = useState<PurchasePlan[]>([])
  const [loading, setLoading] = useState(false)
  const [total, setTotal] = useState(0)
  const [current, setCurrent] = useState(1)
  const [pageSize, setPageSize] = useState(10)
  const [modalVisible, setModalVisible] = useState(false)
  const [editingRecord, setEditingRecord] = useState<PurchasePlan | null>(null)
  const [details, setDetails] = useState<PurchasePlanDetail[]>([])
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
      const res = await getPlanPage({
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
    setDetails([])
    form.setFieldsValue({
      planYear: new Date().getFullYear(),
      status: 'DRAFT',
    })
    setModalVisible(true)
  }

  const handleEdit = async (record: PurchasePlan) => {
    setEditingRecord(record)
    form.setFieldsValue({
      ...record,
    })
    try {
      const res = await getPlanDetails(record.planId!)
      setDetails(res.data)
    } catch (error) {
      message.error('加载明细失败')
    }
    setModalVisible(true)
  }

  const handleSubmit = async () => {
    try {
      const values = await form.validateFields()
      const submitData = {
        plan: {
          ...values,
        },
        details: details,
      }
      
      if (editingRecord?.planId) {
        message.warning('编辑功能待实现')
      } else {
        await createPlan(submitData)
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
      await deletePlan(id)
      message.success('删除成功')
      loadData()
    } catch (error: any) {
      message.error(error.response?.data?.msg || '删除失败')
    }
  }

  const handleSubmitPlan = async (id: number) => {
    try {
      await submitPlan(id)
      message.success('提交成功')
      loadData()
    } catch (error: any) {
      message.error(error.response?.data?.msg || '提交失败')
    }
  }

  const handleApprove = async (id: number) => {
    try {
      await approvePlan(id)
      message.success('审批通过')
      loadData()
    } catch (error: any) {
      message.error(error.response?.data?.msg || '审批失败')
    }
  }

  const handleReject = async (id: number) => {
    try {
      await rejectPlan(id)
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
      
      // 验证数据
      const validation = validateImportData(data, ['计划名称', '计划年度'])
      if (!validation.valid) {
        message.error(`数据验证失败：${validation.errors.join('; ')}`)
        return false
      }

      // 转换数据格式
      const importData = data.map(row => ({
        planName: row['计划名称'] || row['planName'],
        planYear: row['计划年度'] || row['planYear'],
        planQuarter: row['计划季度'] || row['planQuarter'],
        planMonth: row['计划月份'] || row['planMonth'],
        deptName: row['申请部门'] || row['deptName'],
        budgetAmount: row['预算金额'] || row['budgetAmount'],
        remark: row['备注'] || row['remark'],
      }))

      // 批量导入
      let successCount = 0
      let failCount = 0
      for (const item of importData) {
        try {
          await createPlan({
            plan: item,
            details: [],
          })
          successCount++
        } catch (error) {
          failCount++
          console.error('导入失败:', item, error)
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

  // 下载导入模板
  const handleDownloadTemplate = () => {
    const templateColumns = [
      { title: '计划名称', dataIndex: 'planName' },
      { title: '计划年度', dataIndex: 'planYear' },
      { title: '计划季度', dataIndex: 'planQuarter' },
      { title: '计划月份', dataIndex: 'planMonth' },
      { title: '申请部门', dataIndex: 'deptName' },
      { title: '预算金额', dataIndex: 'budgetAmount' },
      { title: '备注', dataIndex: 'remark' },
    ]
    downloadImportTemplate(templateColumns, '采购计划导入模板')
  }

  const columns: ColumnsType<PurchasePlan> = [
    {
      title: '计划编号',
      dataIndex: 'planNo',
      key: 'planNo',
    },
    {
      title: '计划名称',
      dataIndex: 'planName',
      key: 'planName',
    },
    {
      title: '计划年度',
      dataIndex: 'planYear',
      key: 'planYear',
    },
    {
      title: '部门',
      dataIndex: 'deptName',
      key: 'deptName',
    },
    {
      title: '预算金额',
      dataIndex: 'budgetAmount',
      key: 'budgetAmount',
      render: (amount) => amount ? `¥${amount.toFixed(2)}` : '-',
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
          REJECTED: { text: '已驳回', color: 'error' },
          EXECUTING: { text: '执行中', color: 'processing' },
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
          {record.status === 'DRAFT' && (
            <>
              <Permission permission="purchase:plan:edit">
                <Button type="link" onClick={() => handleEdit(record)}>编辑</Button>
              </Permission>
              <Permission permission="purchase:plan:submit">
                <Button type="link" onClick={() => handleSubmitPlan(record.planId!)}>提交</Button>
              </Permission>
              <Permission permission="purchase:plan:remove">
                <Popconfirm title="确定要删除吗？" onConfirm={() => handleDelete(record.planId!)}>
                  <Button type="link" danger>删除</Button>
                </Popconfirm>
              </Permission>
            </>
          )}
          {record.status === 'SUBMITTED' && (
            <>
              <Permission permission="purchase:plan:approve">
                <Button type="link" onClick={() => handleApprove(record.planId!)}>审批通过</Button>
              </Permission>
              <Permission permission="purchase:plan:approve">
                <Button type="link" danger onClick={() => handleReject(record.planId!)}>驳回</Button>
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
          <Input placeholder="搜索计划编号" style={{ width: 200 }} allowClear />
          <Button icon={<SearchOutlined />}>搜索</Button>
        </Space>
        <Space>
          <Button 
            icon={<DownloadOutlined />} 
            onClick={() => exportToExcel(data, columns, '采购计划列表')}
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
          <Permission permission="purchase:plan:add">
            <Button type="primary" icon={<PlusOutlined />} onClick={handleAdd}>
              新增计划
            </Button>
          </Permission>
        </Space>
      </div>

      <Table
        columns={columns}
        dataSource={data}
        loading={loading}
        rowKey="planId"
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
        title={editingRecord ? '编辑采购计划' : '新增采购计划'}
        open={modalVisible}
        onOk={handleSubmit}
        onCancel={() => setModalVisible(false)}
        width={800}
      >
        <Form form={form} layout="vertical">
          <Form.Item name="planName" label="计划名称" rules={[{ required: true }]}>
            <Input />
          </Form.Item>
          <Form.Item name="planYear" label="计划年度" rules={[{ required: true }]}>
            <InputNumber style={{ width: '100%' }} min={2020} max={2100} />
          </Form.Item>
          <Form.Item name="planQuarter" label="计划季度">
            <Select>
              <Select.Option value={1}>第一季度</Select.Option>
              <Select.Option value={2}>第二季度</Select.Option>
              <Select.Option value={3}>第三季度</Select.Option>
              <Select.Option value={4}>第四季度</Select.Option>
            </Select>
          </Form.Item>
          <Form.Item name="planMonth" label="计划月份">
            <Select>
              {Array.from({ length: 12 }, (_, i) => i + 1).map(month => (
                <Select.Option key={month} value={month}>{month}月</Select.Option>
              ))}
            </Select>
          </Form.Item>
          <Form.Item name="deptName" label="申请部门">
            <Input />
          </Form.Item>
          <Form.Item name="budgetAmount" label="预算金额">
            <InputNumber style={{ width: '100%' }} min={0} precision={2} />
          </Form.Item>
          <Form.Item name="remark" label="备注">
            <Input.TextArea rows={3} />
          </Form.Item>
        </Form>
        <div style={{ marginTop: 16 }}>
          <p>计划明细（待实现）</p>
        </div>
      </Modal>

      {/* 导入Modal */}
      <Modal
        title="导入采购计划数据"
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
                exportToExcel(data, columns, '采购计划列表')
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

