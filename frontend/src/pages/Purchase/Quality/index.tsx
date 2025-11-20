import { useState, useEffect } from 'react'
import { Table, Button, Space, Modal, Form, Input, DatePicker, Select, message, Tag } from 'antd'
import { PlusOutlined, CheckOutlined, DownloadOutlined, SearchOutlined, UploadOutlined, SettingOutlined } from '@ant-design/icons'
import type { ColumnsType } from 'antd/es/table'
import { getQualityCheckPage, createQualityCheck, completeQualityCheck, createQualityCheckFromReceiving, type PurchaseQualityCheck } from '@/api/purchase/quality'
import { exportToExcel } from '@/utils/export'
import dayjs from 'dayjs'
import { Permission } from '@/components/Permission'

export default function PurchaseQualityPage() {
  const [data, setData] = useState<PurchaseQualityCheck[]>([])
  const [loading, setLoading] = useState(false)
  const [total, setTotal] = useState(0)
  const [current, setCurrent] = useState(1)
  const [pageSize, setPageSize] = useState(10)
  const [form] = Form.useForm()

  useEffect(() => {
    loadData()
  }, [current, pageSize])

  const loadData = async () => {
    setLoading(true)
    try {
      const res = await getQualityCheckPage({
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

  const handleComplete = async (id: number) => {
    try {
      await completeQualityCheck(id)
      message.success('质检完成')
      loadData()
    } catch (error: any) {
      message.error(error.response?.data?.msg || '操作失败')
    }
  }

  const columns: ColumnsType<PurchaseQualityCheck> = [
    {
      title: '质检单号',
      dataIndex: 'checkNo',
      key: 'checkNo',
    },
    {
      title: '收货单号',
      dataIndex: 'receivingNo',
      key: 'receivingNo',
    },
    {
      title: '供应商',
      dataIndex: 'supplierName',
      key: 'supplierName',
    },
    {
      title: '质检日期',
      dataIndex: 'checkDate',
      key: 'checkDate',
    },
    {
      title: '质检类型',
      dataIndex: 'checkType',
      key: 'checkType',
      render: (type) => {
        const typeMap: Record<string, string> = {
          INCOMING: '来料检验',
          PROCESS: '过程检验',
          FINAL: '最终检验',
        }
        return typeMap[type] || type
      },
    },
    {
      title: '合格率',
      dataIndex: 'qualifiedRate',
      key: 'qualifiedRate',
      render: (rate) => rate ? `${rate.toFixed(2)}%` : '-',
    },
    {
      title: '检验结果',
      dataIndex: 'checkResult',
      key: 'checkResult',
      render: (result) => {
        const resultMap: Record<string, { text: string; color: string }> = {
          PASSED: { text: '合格', color: 'success' },
          FAILED: { text: '不合格', color: 'error' },
          PARTIAL: { text: '部分合格', color: 'warning' },
        }
        const info = resultMap[result] || { text: result, color: 'default' }
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
          CHECKING: { text: '检验中', color: 'processing' },
          APPROVED: { text: '已审核', color: 'success' },
          COMPLETED: { text: '已完成', color: 'success' },
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
          {record.status === 'CHECKING' && (
            <Permission permission="purchase:quality:complete">
              <Button type="link" onClick={() => handleComplete(record.checkId!)}>完成质检</Button>
            </Permission>
          )}
        </Space>
      ),
    },
  ]

  return (
    <div>
      <div style={{ marginBottom: 16, display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
        <Space>
          <Input placeholder="搜索质检单号" style={{ width: 200 }} allowClear />
          <Button icon={<SearchOutlined />}>搜索</Button>
        </Space>
        <Space>
          <Button 
            icon={<DownloadOutlined />} 
            onClick={() => exportToExcel(data, columns, '采购质检列表')}
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
          <Permission permission="purchase:quality:add">
            <Button type="primary" icon={<PlusOutlined />} onClick={() => message.info('新增功能开发中')}>
              新增质检单
            </Button>
          </Permission>
        </Space>
      </div>

      <Table
        columns={columns}
        dataSource={data}
        loading={loading}
        rowKey="checkId"
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
    </div>
  )
}

