import { useState, useEffect } from 'react'
import { Table, Button, Space, message, Modal, Form, Input, InputNumber, Select, DatePicker } from 'antd'
import { PlusOutlined, DownloadOutlined } from '@ant-design/icons'
import type { ColumnsType } from 'antd/es/table'
import { exportToExcel } from '@/utils/export'
import dayjs from 'dayjs'

interface RepairOrder {
  repairId?: number
  repairNo: string
  assetId?: number
  assetCode?: string
  assetName?: string
  faultDescription?: string
  repairType?: string
  repairCost?: number
  status?: string
  repairDate?: string
  remark?: string
}

export default function RepairPage() {
  const [data, setData] = useState<RepairOrder[]>([])
  const [loading, setLoading] = useState(false)
  const [modalVisible, setModalVisible] = useState(false)
  const [form] = Form.useForm()

  useEffect(() => {
    loadData()
  }, [])

  const loadData = async () => {
    setLoading(true)
    // TODO: 调用API
    setTimeout(() => {
      setData([])
      setLoading(false)
    }, 500)
  }

  const handleAdd = () => {
    form.resetFields()
    // 自动生成工单编号
    form.setFieldsValue({
      repairNo: `REP${new Date().getTime()}`,
      status: '0',
    })
    setModalVisible(true)
  }

  const handleSubmit = async () => {
    try {
      const values = await form.validateFields()
      const submitData = {
        ...values,
        repairDate: values.repairDate ? values.repairDate.format('YYYY-MM-DD') : undefined,
      }
      
      // TODO: 调用API保存
      message.success('新增成功')
      setModalVisible(false)
      loadData()
    } catch (error) {
      console.error('提交失败', error)
    }
  }

  const columns: ColumnsType<RepairOrder> = [
    {
      title: '工单编号',
      dataIndex: 'repairNo',
      key: 'repairNo',
    },
    {
      title: '资产编码',
      dataIndex: 'assetCode',
      key: 'assetCode',
    },
    {
      title: '资产名称',
      dataIndex: 'assetName',
      key: 'assetName',
    },
    {
      title: '故障描述',
      dataIndex: 'faultDescription',
      key: 'faultDescription',
    },
    {
      title: '维修类型',
      dataIndex: 'repairType',
      key: 'repairType',
    },
    {
      title: '维修费用',
      dataIndex: 'repairCost',
      key: 'repairCost',
      render: (cost) => cost ? `¥${cost.toFixed(2)}` : '-',
    },
    {
      title: '状态',
      dataIndex: 'status',
      key: 'status',
      render: (status) => {
        const statusMap: Record<string, { text: string; color: string }> = {
          '0': { text: '待维修', color: 'orange' },
          '1': { text: '维修中', color: 'blue' },
          '2': { text: '已完成', color: 'green' },
          '3': { text: '已取消', color: 'red' },
        }
        const statusInfo = statusMap[status || '0'] || { text: status, color: 'default' }
        return <span style={{ color: statusInfo.color }}>{statusInfo.text}</span>
      },
    },
  ]

  return (
    <div>
      <div style={{ marginBottom: 16, display: 'flex', justifyContent: 'space-between' }}>
        <div></div>
        <Space>
          <Button 
            icon={<DownloadOutlined />} 
            onClick={() => exportToExcel(data, columns, '维修工单列表')}
            disabled={data.length === 0}
          >
            导出Excel
          </Button>
          <Button type="primary" icon={<PlusOutlined />} onClick={handleAdd}>
            新增维修工单
          </Button>
        </Space>
      </div>
      <Table
        columns={columns}
        dataSource={data}
        loading={loading}
        rowKey="repairId"
      />

      <Modal
        title="新增维修工单"
        open={modalVisible}
        onOk={handleSubmit}
        onCancel={() => setModalVisible(false)}
        width={600}
      >
        <Form form={form} layout="vertical">
          <Form.Item
            name="repairNo"
            label="工单编号"
            rules={[{ required: true, message: '请输入工单编号' }]}
          >
            <Input disabled />
          </Form.Item>
          <Form.Item
            name="assetCode"
            label="资产编码"
            rules={[{ required: true, message: '请输入资产编码' }]}
          >
            <Input placeholder="请输入资产编码" />
          </Form.Item>
          <Form.Item name="assetName" label="资产名称">
            <Input placeholder="请输入资产名称" />
          </Form.Item>
          <Form.Item
            name="faultDescription"
            label="故障描述"
            rules={[{ required: true, message: '请输入故障描述' }]}
          >
            <Input.TextArea rows={3} placeholder="请输入故障描述" />
          </Form.Item>
          <Form.Item name="repairType" label="维修类型">
            <Select placeholder="请选择维修类型">
              <Select.Option value="日常维护">日常维护</Select.Option>
              <Select.Option value="故障维修">故障维修</Select.Option>
              <Select.Option value="定期保养">定期保养</Select.Option>
              <Select.Option value="紧急维修">紧急维修</Select.Option>
            </Select>
          </Form.Item>
          <Form.Item name="repairCost" label="维修费用">
            <InputNumber style={{ width: '100%' }} min={0} precision={2} placeholder="请输入维修费用" />
          </Form.Item>
          <Form.Item name="repairDate" label="维修日期">
            <DatePicker style={{ width: '100%' }} />
          </Form.Item>
          <Form.Item name="status" label="状态">
            <Select>
              <Select.Option value="0">待维修</Select.Option>
              <Select.Option value="1">维修中</Select.Option>
              <Select.Option value="2">已完成</Select.Option>
              <Select.Option value="3">已取消</Select.Option>
            </Select>
          </Form.Item>
          <Form.Item name="remark" label="备注">
            <Input.TextArea rows={3} placeholder="请输入备注" />
          </Form.Item>
        </Form>
      </Modal>
    </div>
  )
}
