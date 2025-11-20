import { useState, useEffect } from 'react'
import { Card, Row, Col, Statistic, DatePicker, Button, Table, message } from 'antd'
import { DollarOutlined, ShoppingCartOutlined, CheckCircleOutlined, WarningOutlined, DownloadOutlined } from '@ant-design/icons'
import type { ColumnsType } from 'antd/es/table'
import { getStatistics, getSupplierEvaluation, type PurchaseStatistics, type SupplierEvaluation } from '@/api/purchase/report'
import { exportToExcel } from '@/utils/export'
import dayjs from 'dayjs'
import { Permission } from '@/components/Permission'

const { RangePicker } = DatePicker

export default function PurchaseReportPage() {
  const [statistics, setStatistics] = useState<PurchaseStatistics>({})
  const [supplierData, setSupplierData] = useState<SupplierEvaluation[]>([])
  const [loading, setLoading] = useState(false)
  const [dateRange, setDateRange] = useState<[dayjs.Dayjs, dayjs.Dayjs]>([
    dayjs().subtract(30, 'day'),
    dayjs(),
  ])

  useEffect(() => {
    loadStatistics()
    loadSupplierEvaluation()
  }, [dateRange])

  const loadStatistics = async () => {
    setLoading(true)
    try {
      const res = await getStatistics({
        startDate: dateRange[0].format('YYYY-MM-DD'),
        endDate: dateRange[1].format('YYYY-MM-DD'),
      })
      setStatistics(res.data)
    } catch (error) {
      message.error('加载统计数据失败')
    } finally {
      setLoading(false)
    }
  }

  const loadSupplierEvaluation = async () => {
    try {
      const res = await getSupplierEvaluation()
      // setSupplierData(res.data)
    } catch (error) {
      message.error('加载供应商评价失败')
    }
  }

  const supplierColumns: ColumnsType<SupplierEvaluation> = [
    {
      title: '供应商名称',
      dataIndex: 'supplierName',
      key: 'supplierName',
    },
    {
      title: '综合评分',
      dataIndex: 'totalScore',
      key: 'totalScore',
      render: (score) => score ? score.toFixed(1) : '-',
    },
    {
      title: '质量评分',
      dataIndex: 'qualityScore',
      key: 'qualityScore',
      render: (score) => score ? score.toFixed(1) : '-',
    },
    {
      title: '服务评分',
      dataIndex: 'serviceScore',
      key: 'serviceScore',
      render: (score) => score ? score.toFixed(1) : '-',
    },
    {
      title: '价格评分',
      dataIndex: 'priceScore',
      key: 'priceScore',
      render: (score) => score ? score.toFixed(1) : '-',
    },
    {
      title: '订单数量',
      dataIndex: 'orderCount',
      key: 'orderCount',
    },
  ]

  return (
    <div>
      <Permission permission="purchase:report:view">
        <div style={{ marginBottom: 16, display: 'flex', justifyContent: 'space-between' }}>
          <RangePicker
            value={dateRange}
            onChange={(dates) => {
              if (dates) {
                setDateRange([dates[0]!, dates[1]!])
              }
            }}
          />
          <Button icon={<DownloadOutlined />} onClick={() => exportToExcel(supplierData, supplierColumns, '采购报表')}>
            导出Excel
          </Button>
        </div>

        <Row gutter={16} style={{ marginBottom: 16 }}>
          <Col span={6}>
            <Card>
              <Statistic
                title="订单总数"
                value={statistics.totalOrders || 0}
                prefix={<ShoppingCartOutlined />}
                valueStyle={{ color: '#3f8600' }}
              />
            </Card>
          </Col>
          <Col span={6}>
            <Card>
              <Statistic
                title="订单总额"
                value={statistics.totalAmount || 0}
                prefix={<DollarOutlined />}
                precision={2}
                suffix="元"
                valueStyle={{ color: '#1890ff' }}
              />
            </Card>
          </Col>
          <Col span={6}>
            <Card>
              <Statistic
                title="已付款金额"
                value={statistics.totalPaid || 0}
                prefix={<DollarOutlined />}
                precision={2}
                suffix="元"
                valueStyle={{ color: '#3f8600' }}
              />
            </Card>
          </Col>
          <Col span={6}>
            <Card>
              <Statistic
                title="质检合格率"
                value={statistics.qualityPassRate || 0}
                prefix={<CheckCircleOutlined />}
                precision={2}
                suffix="%"
                valueStyle={{ color: '#3f8600' }}
              />
            </Card>
          </Col>
        </Row>

        <Card title="供应商评价报表" style={{ marginTop: 16 }}>
          <Table
            columns={supplierColumns}
            dataSource={supplierData}
            loading={loading}
            rowKey="supplierId"
            pagination={false}
          />
        </Card>
      </Permission>
    </div>
  )
}

