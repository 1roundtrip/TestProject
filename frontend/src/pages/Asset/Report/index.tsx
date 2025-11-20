import { useState, useEffect } from 'react'
import { Card, Row, Col, Statistic, Table, DatePicker, Button, Space, message } from 'antd'
import { DollarOutlined, FileTextOutlined, BarChartOutlined, PieChartOutlined } from '@ant-design/icons'
import type { ColumnsType } from 'antd/es/table'
import { getStatistics, getValueAnalysis, getUsageAnalysis, getDepreciationReport, getScrapStatistics } from '@/api/asset/report'
import { exportToExcel } from '@/utils/export'
import dayjs from 'dayjs'

const { RangePicker } = DatePicker

export default function AssetReportPage() {
  const [statistics, setStatistics] = useState<any>({})
  const [valueData, setValueData] = useState<any[]>([])
  const [usageData, setUsageData] = useState<any[]>([])
  const [depreciationData, setDepreciationData] = useState<any[]>([])
  const [scrapData, setScrapData] = useState<any[]>([])
  const [dateRange, setDateRange] = useState<[dayjs.Dayjs, dayjs.Dayjs] | null>(null)

  useEffect(() => {
    loadStatistics()
    loadValueAnalysis()
    loadUsageAnalysis()
    loadDepreciationReport()
    loadScrapStatistics()
  }, [])

  const loadStatistics = async () => {
    try {
      const res = await getStatistics()
      setStatistics(res.data || {})
    } catch (error) {
      console.error('加载统计失败', error)
    }
  }

  const loadValueAnalysis = async () => {
    try {
      const res = await getValueAnalysis()
      setValueData(res.data?.list || [])
    } catch (error) {
      console.error('加载价值分析失败', error)
    }
  }

  const loadUsageAnalysis = async () => {
    try {
      const res = await getUsageAnalysis()
      setUsageData(res.data?.list || [])
    } catch (error) {
      console.error('加载使用率分析失败', error)
    }
  }

  const loadDepreciationReport = async () => {
    try {
      const month = dayjs().format('YYYY-MM')
      const res = await getDepreciationReport(month)
      setDepreciationData(res.data?.list || [])
    } catch (error) {
      console.error('加载折旧报表失败', error)
    }
  }

  const loadScrapStatistics = async () => {
    try {
      const res = await getScrapStatistics()
      setScrapData(res.data?.list || [])
    } catch (error) {
      console.error('加载报废统计失败', error)
    }
  }

  const handleDateRangeChange = (dates: any) => {
    setDateRange(dates)
    if (dates) {
      loadValueAnalysis()
      loadScrapStatistics()
    }
  }

  const valueColumns: ColumnsType<any> = [
    { title: '资产编码', dataIndex: 'assetCode', key: 'assetCode' },
    { title: '资产名称', dataIndex: 'assetName', key: 'assetName' },
    { title: '原值', dataIndex: 'originalValue', key: 'originalValue', render: (v) => v ? `¥${v.toFixed(2)}` : '-' },
    { title: '累计折旧', dataIndex: 'accumulatedDepreciation', key: 'accumulatedDepreciation', render: (v) => v ? `¥${v.toFixed(2)}` : '-' },
    { title: '净值', dataIndex: 'netValue', key: 'netValue', render: (v) => v ? `¥${v.toFixed(2)}` : '-' },
  ]

  const usageColumns: ColumnsType<any> = [
    { title: '资产编码', dataIndex: 'assetCode', key: 'assetCode' },
    { title: '资产名称', dataIndex: 'assetName', key: 'assetName' },
    { title: '使用状态', dataIndex: 'status', key: 'status' },
    { title: '使用率', dataIndex: 'usageRate', key: 'usageRate', render: (v) => v ? `${v}%` : '-' },
  ]

  return (
    <div>
      <div style={{ marginBottom: 16 }}>
        <Space>
          <RangePicker onChange={handleDateRangeChange} />
          <Button onClick={loadStatistics}>刷新</Button>
        </Space>
      </div>

      {/* 统计卡片 */}
      <Row gutter={16} style={{ marginBottom: 16 }}>
        <Col span={6}>
          <Card>
            <Statistic
              title="资产总数"
              value={statistics.totalCount || 0}
              prefix={<FileTextOutlined />}
            />
          </Card>
        </Col>
        <Col span={6}>
          <Card>
            <Statistic
              title="资产原值总额"
              value={statistics.totalOriginalValue || 0}
              prefix={<DollarOutlined />}
              precision={2}
              suffix="元"
            />
          </Card>
        </Col>
        <Col span={6}>
          <Card>
            <Statistic
              title="累计折旧总额"
              value={statistics.totalDepreciation || 0}
              prefix={<DollarOutlined />}
              precision={2}
              suffix="元"
            />
          </Card>
        </Col>
        <Col span={6}>
          <Card>
            <Statistic
              title="资产净值总额"
              value={statistics.totalNetValue || 0}
              prefix={<DollarOutlined />}
              precision={2}
              suffix="元"
            />
          </Card>
        </Col>
      </Row>

      {/* 资产价值分析 */}
      <Card title="资产价值分析" style={{ marginBottom: 16 }}>
        <Table
          columns={valueColumns}
          dataSource={valueData}
          rowKey="assetId"
          pagination={false}
        />
      </Card>

      {/* 资产使用率分析 */}
      <Card title="资产使用率分析" style={{ marginBottom: 16 }}>
        <Table
          columns={usageColumns}
          dataSource={usageData}
          rowKey="assetId"
          pagination={false}
        />
      </Card>

      {/* 资产折旧报表 */}
      <Card title="资产折旧报表" style={{ marginBottom: 16 }}>
        <Table
          dataSource={depreciationData}
          rowKey="detailId"
          pagination={false}
          columns={[
            { title: '资产编码', dataIndex: 'assetCode', key: 'assetCode' },
            { title: '资产名称', dataIndex: 'assetName', key: 'assetName' },
            { title: '折旧月份', dataIndex: 'depreciationMonth', key: 'depreciationMonth' },
            { title: '折旧金额', dataIndex: 'depreciationAmount', key: 'depreciationAmount', render: (v) => v ? `¥${v.toFixed(2)}` : '-' },
            { title: '累计折旧', dataIndex: 'accumulatedAmount', key: 'accumulatedAmount', render: (v) => v ? `¥${v.toFixed(2)}` : '-' },
            { title: '净值', dataIndex: 'netValue', key: 'netValue', render: (v) => v ? `¥${v.toFixed(2)}` : '-' },
          ]}
        />
      </Card>

      {/* 资产报废统计 */}
      <Card title="资产报废统计">
        <Table
          dataSource={scrapData}
          rowKey="scrapId"
          pagination={false}
          columns={[
            { title: '报废单号', dataIndex: 'scrapNo', key: 'scrapNo' },
            { title: '资产名称', dataIndex: 'assetName', key: 'assetName' },
            { title: '报废类型', dataIndex: 'scrapType', key: 'scrapType' },
            { title: '原值', dataIndex: 'originalValue', key: 'originalValue', render: (v) => v ? `¥${v.toFixed(2)}` : '-' },
            { title: '净值', dataIndex: 'netValue', key: 'netValue', render: (v) => v ? `¥${v.toFixed(2)}` : '-' },
            { title: '残值', dataIndex: 'scrapValue', key: 'scrapValue', render: (v) => v ? `¥${v.toFixed(2)}` : '-' },
          ]}
        />
      </Card>
    </div>
  )
}

