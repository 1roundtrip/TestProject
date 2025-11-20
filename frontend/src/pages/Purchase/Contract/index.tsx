import { useState, useEffect } from 'react'
import { Table, Button, Space, Modal, Form, Input, InputNumber, DatePicker, Select, message, Popconfirm, Tag } from 'antd'
import { PlusOutlined, EditOutlined, DeleteOutlined, CheckOutlined, DownloadOutlined, SearchOutlined, UploadOutlined, SettingOutlined } from '@ant-design/icons'
import type { ColumnsType } from 'antd/es/table'
import { getContractPage, createContract, submitContract, approveContract, signContract, deleteContract, createContractFromOrder, type PurchaseContract } from '@/api/purchase/contract'
import { exportToExcel } from '@/utils/export'
import dayjs from 'dayjs'
import { Permission } from '@/components/Permission'

export default function PurchaseContractPage() {
  const [data, setData] = useState<PurchaseContract[]>([])
  const [loading, setLoading] = useState(false)
  const [total, setTotal] = useState(0)
  const [current, setCurrent] = useState(1)
  const [pageSize, setPageSize] = useState(10)
  const [modalVisible, setModalVisible] = useState(false)
  const [editingRecord, setEditingRecord] = useState<PurchaseContract | null>(null)
  const [form] = Form.useForm()

  useEffect(() => {
    loadData()
  }, [current, pageSize])

  const loadData = async () => {
    setLoading(true)
    try {
      const res = await getContractPage({
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
      contractDate: dayjs(),
      startDate: dayjs(),
      contractType: 'SPECIFIC',
      currency: 'CNY',
      status: 'DRAFT',
    })
    setModalVisible(true)
  }

  const handleEdit = (record: PurchaseContract) => {
    setEditingRecord(record)
    form.setFieldsValue({
      ...record,
      contractDate: record.contractDate ? dayjs(record.contractDate) : null,
      startDate: record.startDate ? dayjs(record.startDate) : null,
      endDate: record.endDate ? dayjs(record.endDate) : null,
    })
    setModalVisible(true)
  }

  const handleSubmit = async () => {
    try {
      const values = await form.validateFields()
      const submitData = {
        contract: {
          ...values,
          contractDate: values.contractDate ? values.contractDate.format('YYYY-MM-DD') : undefined,
          startDate: values.startDate ? values.startDate.format('YYYY-MM-DD') : undefined,
          endDate: values.endDate ? values.endDate.format('YYYY-MM-DD') : undefined,
        },
        details: [],
      }
      
      if (editingRecord?.contractId) {
        message.warning('编辑功能待实现')
      } else {
        await createContract(submitData)
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
      await deleteContract(id)
      message.success('删除成功')
      loadData()
    } catch (error: any) {
      message.error(error.response?.data?.msg || '删除失败')
    }
  }

  const handleSubmitContract = async (id: number) => {
    try {
      await submitContract(id)
      message.success('提交成功')
      loadData()
    } catch (error: any) {
      message.error(error.response?.data?.msg || '提交失败')
    }
  }

  const handleApprove = async (id: number) => {
    try {
      await approveContract(id)
      message.success('审批通过')
      loadData()
    } catch (error: any) {
      message.error(error.response?.data?.msg || '审批失败')
    }
  }

  const handleSign = async (id: number) => {
    try {
      await signContract(id)
      message.success('签订成功')
      loadData()
    } catch (error: any) {
      message.error(error.response?.data?.msg || '签订失败')
    }
  }

  const columns: ColumnsType<PurchaseContract> = [
    {
      title: '合同编号',
      dataIndex: 'contractNo',
      key: 'contractNo',
    },
    {
      title: '合同名称',
      dataIndex: 'contractName',
      key: 'contractName',
    },
    {
      title: '供应商',
      dataIndex: 'supplierName',
      key: 'supplierName',
    },
    {
      title: '合同类型',
      dataIndex: 'contractType',
      key: 'contractType',
      render: (type) => {
        const typeMap: Record<string, string> = {
          FRAMEWORK: '框架合同',
          SPECIFIC: '具体合同',
        }
        return typeMap[type] || type
      },
    },
    {
      title: '合同金额',
      dataIndex: 'totalAmount',
      key: 'totalAmount',
      render: (amount) => amount ? `¥${amount.toFixed(2)}` : '-',
    },
    {
      title: '签订日期',
      dataIndex: 'contractDate',
      key: 'contractDate',
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
          SIGNED: { text: '已签订', color: 'success' },
          EXECUTING: { text: '执行中', color: 'processing' },
          COMPLETED: { text: '已完成', color: 'success' },
          TERMINATED: { text: '已终止', color: 'error' },
        }
        const info = statusMap[status] || { text: status, color: 'default' }
        return <Tag color={info.color}>{info.text}</Tag>
      },
    },
    {
      title: '操作',
      key: 'action',
      width: 350,
      render: (_, record) => (
        <Space>
          {record.status === 'DRAFT' && (
            <>
              <Permission permission="purchase:contract:edit">
                <Button type="link" onClick={() => handleEdit(record)}>编辑</Button>
              </Permission>
              <Permission permission="purchase:contract:submit">
                <Button type="link" onClick={() => handleSubmitContract(record.contractId!)}>提交</Button>
              </Permission>
              <Permission permission="purchase:contract:remove">
                <Popconfirm title="确定要删除吗？" onConfirm={() => handleDelete(record.contractId!)}>
                  <Button type="link" danger>删除</Button>
                </Popconfirm>
              </Permission>
            </>
          )}
          {record.status === 'SUBMITTED' && (
            <>
              <Permission permission="purchase:contract:approve">
                <Button type="link" onClick={() => handleApprove(record.contractId!)}>审批通过</Button>
              </Permission>
            </>
          )}
          {record.status === 'APPROVED' && (
            <>
              <Permission permission="purchase:contract:sign">
                <Button type="link" onClick={() => handleSign(record.contractId!)}>签订合同</Button>
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
          <Input placeholder="搜索合同编号" style={{ width: 200 }} allowClear />
          <Button icon={<SearchOutlined />}>搜索</Button>
        </Space>
        <Space>
          <Button 
            icon={<DownloadOutlined />} 
            onClick={() => exportToExcel(data, columns, '采购合同列表')}
            disabled={data.length === 0}
          >
            导出Excel
          </Button>
          <Button 
            icon={<UploadOutlined />}
            onClick={() => message.info('导入功能开发中')}
          >
            导入Excel
          </Button>
          <Button 
            icon={<SettingOutlined />}
            onClick={() => message.info('设置功能开发中')}
          >
            设置
          </Button>
          <Permission permission="purchase:contract:add">
            <Button type="primary" icon={<PlusOutlined />} onClick={handleAdd}>
              新增合同
            </Button>
          </Permission>
        </Space>
      </div>

      <Table
        columns={columns}
        dataSource={data}
        loading={loading}
        rowKey="contractId"
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
        title={editingRecord ? '编辑采购合同' : '新增采购合同'}
        open={modalVisible}
        onOk={handleSubmit}
        onCancel={() => setModalVisible(false)}
        width={800}
      >
        <Form form={form} layout="vertical">
          <Form.Item name="contractName" label="合同名称" rules={[{ required: true }]}>
            <Input />
          </Form.Item>
          <Form.Item name="supplierName" label="供应商名称" rules={[{ required: true }]}>
            <Input />
          </Form.Item>
          <Form.Item name="contractType" label="合同类型">
            <Select>
              <Select.Option value="FRAMEWORK">框架合同</Select.Option>
              <Select.Option value="SPECIFIC">具体合同</Select.Option>
            </Select>
          </Form.Item>
          <Form.Item name="contractDate" label="签订日期" rules={[{ required: true }]}>
            <DatePicker style={{ width: '100%' }} />
          </Form.Item>
          <Form.Item name="startDate" label="开始日期" rules={[{ required: true }]}>
            <DatePicker style={{ width: '100%' }} />
          </Form.Item>
          <Form.Item name="endDate" label="结束日期" rules={[{ required: true }]}>
            <DatePicker style={{ width: '100%' }} />
          </Form.Item>
          <Form.Item name="currency" label="币种">
            <Select>
              <Select.Option value="CNY">人民币</Select.Option>
              <Select.Option value="USD">美元</Select.Option>
            </Select>
          </Form.Item>
          <Form.Item name="paymentMethod" label="付款方式">
            <Input />
          </Form.Item>
          <Form.Item name="remark" label="备注">
            <Input.TextArea rows={3} />
          </Form.Item>
        </Form>
      </Modal>
    </div>
  )
}

