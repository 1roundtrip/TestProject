import { useState, useEffect } from 'react'
import { Card, Row, Col, Statistic, Table, DatePicker, Select, Button, Space, message } from 'antd'
import { BarChartOutlined, ArrowUpOutlined, ArrowDownOutlined, SwapOutlined, DownloadOutlined } from '@ant-design/icons'
import type { ColumnsType } from 'antd/es/table'
import { getReportPage, getReportStatistics, type InventoryStatistics } from '@/api/inventory/report'
import { getWarehousePage, type InventoryWarehouse } from '@/api/inventory/warehouse'
import { exportToExcel } from '@/utils/export'
import dayjs from 'dayjs'

export default function InventoryReportPage() {
  const [data, setData] = useState<InventoryStatistics[]>([])
  const [warehouses, setWarehouses] = useState<InventoryWarehouse[]>([])
  const [statistics, setStatistics] = useState<any>({})
  const [loading, setLoading] = useState(false)
  const [total, setTotal] = useState(0)
  const [current, setCurrent] = useState(1)
  const [pageSize, setPageSize] = useState(10)
  const [dateRange, setDateRange] = useState<[dayjs.Dayjs, dayjs.Dayjs]>([
    dayjs().subtract(30, 'day'),
    dayjs(),
  ])
  const [warehouseId, setWarehouseId] = useState<number | undefined>()

  useEffect(() => {
    loadData()
    loadStatistics()
    loadWarehouses()
  }, [current, pageSize, dateRange, warehouseId])

  const loadWarehouses = async () => {
    try {
      const res = await getWarehousePage({ current: 1, size: 1000 })
      setWarehouses(res.data.records || [])
    } catch (error) {
      console.error('加载仓库列表失败:', error)
    }
  }

  const loadStatistics = async () => {
    try {
      const res = await getReportStatistics({
        startDate: dateRange[0].format('YYYY-MM-DD'),
        endDate: dateRange[1].format('YYYY-MM-DD'),
        warehouseId,
      })
      setStatistics(res.data || {})
    } catch (error) {
      console.error('加载统计数据失败:', error)
    }
  }

  const loadData = async () => {
    setLoading(true)
    try {
      const res = await getReportPage({
        current,
        size: pageSize,
        startDate: dateRange[0].format('YYYY-MM-DD'),
        endDate: dateRange[1].format('YYYY-MM-DD'),
        warehouseId,
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

  const handleExport = () => {
    exportToExcel(data, columns, '库存报表')
  }

  const columns: ColumnsType<InventoryStatistics> = [
    {
      title: '统计日期',
      dataIndex: 'statDate',
      key: 'statDate',
    },
    {
      title: '统计类型',
      dataIndex: 'statType',
      key: 'statType',
      render: (type) => {
        const typeMap: Record<string, string> = {
          DAILY: '日',
          WEEKLY: '周',
          MONTHLY: '月',
          QUARTERLY: '季',
          YEARLY: '年',
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
      title: '物料种类数',
      dataIndex: 'totalMaterials',
      key: 'totalMaterials',
    },
    {
      title: '总库存数量',
      dataIndex: 'totalQuantity',
      key: 'totalQuantity',
    },
    {
      title: '总库存价值',
      dataIndex: 'totalValue',
      key: 'totalValue',
      render: (value) => value ? `¥${value.toFixed(2)}` : '-',
    },
    {
      title: '入库单数',
      dataIndex: 'inboundCount',
      key: 'inboundCount',
    },
    {
      title: '入库数量',
      dataIndex: 'inboundQuantity',
      key: 'inboundQuantity',
    },
    {
      title: '入库金额',
      dataIndex: 'inboundAmount',
      key: 'inboundAmount',
      render: (amount) => amount ? `¥${amount.toFixed(2)}` : '-',
    },
    {
      title: '出库单数',
      dataIndex: 'outboundCount',
      key: 'outboundCount',
    },
    {
      title: '出库数量',
      dataIndex: 'outboundQuantity',
      key: 'outboundQuantity',
    },
    {
      title: '出库金额',
      dataIndex: 'outboundAmount',
      key: 'outboundAmount',
      render: (amount) => amount ? `¥${amount.toFixed(2)}` : '-',
    },
    {
      title: '周转率',
      dataIndex: 'turnoverRate',
      key: 'turnoverRate',
      render: (rate) => rate ? `${rate.toFixed(2)}%` : '-',
    },
  ]

  return (
    <div>
      <Card style={{ marginBottom: 16 }}>
        <Row gutter={16}>
          <Col span={6}>
            <Statistic title="总库存价值" value={statistics.totalValue || 0} prefix="¥" precision={2} />
          </Col>
          <Col span={6}>
            <Statistic title="总入库金额" value={statistics.totalInboundAmount || 0} prefix="¥" precision={2} valueStyle={{ color: '#3f8600' }} prefix={<ArrowDownOutlined />} />
          </Col>
          <Col span={6}>
            <Statistic title="总出库金额" value={statistics.totalOutboundAmount || 0} prefix="¥" precision={2} valueStyle={{ color: '#cf1322' }} prefix={<ArrowUpOutlined />} />
          </Col>
          <Col span={6}>
            <Statistic title="平均周转率" value={statistics.avgTurnoverRate || 0} suffix="%" precision={2} />
          </Col>
        </Row>
      </Card>

      <Card style={{ marginBottom: 16 }}>
        <Space>
          <DatePicker.RangePicker
            value={dateRange}
            onChange={(dates) => {
              if (dates) {
                setDateRange([dates[0]!, dates[1]!])
              }
            }}
          />
          <Select
            placeholder="选择仓库"
            allowClear
            style={{ width: 200 }}
            value={warehouseId}
            onChange={setWarehouseId}
          >
            {warehouses.map(wh => (
              <Select.Option key={wh.warehouseId} value={wh.warehouseId}>
                {wh.warehouseName}
              </Select.Option>
            ))}
          </Select>
          <Button icon={<DownloadOutlined />} onClick={handleExport} disabled={data.length === 0}>
            导出Excel
          </Button>
        </Space>
      </Card>

      <Table
        columns={columns}
        dataSource={data}
        loading={loading}
        rowKey="statId"
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

