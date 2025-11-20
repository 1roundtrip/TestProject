import { useState, useEffect } from 'react'
import { Card, Row, Col, Statistic, DatePicker, Button, message, Table } from 'antd'
import { DownloadOutlined } from '@ant-design/icons'
import dayjs from 'dayjs'
import { getStatisticsSummary, getStatisticsPage, type WarningStatistics } from '@/api/warning/statistics'
import { getRecordPage, type WarningRecord } from '@/api/warning/monitor'
import { exportToExcel } from '@/utils/export'
import type { ColumnsType } from 'antd/es/table'

const { RangePicker } = DatePicker

export default function WarningStatisticsPage() {
  const [dateRange, setDateRange] = useState<[dayjs.Dayjs, dayjs.Dayjs] | null>(null)
  const [statistics, setStatistics] = useState<any>({})
  const [data, setData] = useState<WarningRecord[]>([])
  const [loading, setLoading] = useState(false)

  const loadStatistics = async () => {
    if (!dateRange) {
      message.warning('请选择日期范围')
      return
    }
    setLoading(true)
    try {
      const startDate = dateRange[0].format('YYYY-MM-DD')
      const endDate = dateRange[1].format('YYYY-MM-DD')
      
      const [summaryRes, recordsRes] = await Promise.all([
        getStatisticsSummary({ startDate, endDate }),
        getRecordPage({ current: 1, size: 1000 }),
      ])
      
      setStatistics(summaryRes.data || {})
      setData(recordsRes.data.records || [])
    } catch (error: any) {
      message.error(error.response?.data?.msg || '加载数据失败')
    } finally {
      setLoading(false)
    }
  }

  const handleExport = () => {
    const columns: ColumnsType<WarningRecord> = [
      { title: '预警标题', dataIndex: 'warningTitle', key: 'warningTitle' },
      { title: '预警类型', dataIndex: 'warningType', key: 'warningType' },
      { title: '状态', dataIndex: 'status', key: 'status' },
      { title: '触发时间', dataIndex: 'triggerTime', key: 'triggerTime' },
    ]
    exportToExcel(data, columns.filter(col => 'dataIndex' in col && col.dataIndex) as Array<{ title: string; dataIndex: string }>, '预警统计')
    message.success('导出成功')
  }

  const columns: ColumnsType<WarningRecord> = [
    { title: '预警标题', dataIndex: 'warningTitle', key: 'warningTitle' },
    { title: '预警类型', dataIndex: 'warningType', key: 'warningType' },
    { title: '预警级别', dataIndex: 'warningLevelName', key: 'warningLevelName' },
    { title: '状态', dataIndex: 'status', key: 'status' },
    { title: '触发时间', dataIndex: 'triggerTime', key: 'triggerTime' },
  ]

  return (
    <div>
      <Card style={{ marginBottom: 16 }}>
        <Row gutter={16} align="middle">
          <Col>
            <RangePicker
              value={dateRange}
              onChange={(dates) => setDateRange(dates as [dayjs.Dayjs, dayjs.Dayjs] | null)}
            />
          </Col>
          <Col>
            <Button type="primary" onClick={loadStatistics} loading={loading}>
              查询
            </Button>
          </Col>
          <Col>
            <Button icon={<DownloadOutlined />} onClick={handleExport}>
              导出报表
            </Button>
          </Col>
        </Row>
      </Card>

      <Row gutter={16} style={{ marginBottom: 16 }}>
        <Col span={6}>
          <Card>
            <Statistic title="预警总数" value={statistics.totalCount || 0} />
          </Card>
        </Col>
        <Col span={6}>
          <Card>
            <Statistic title="待处理" value={statistics.pendingCount || 0} />
          </Card>
        </Col>
        <Col span={6}>
          <Card>
            <Statistic title="处理中" value={statistics.processingCount || 0} />
          </Card>
        </Col>
        <Col span={6}>
          <Card>
            <Statistic title="已解决" value={statistics.resolvedCount || 0} />
          </Card>
        </Col>
      </Row>

      <Card title="预警明细">
        <Table
          columns={columns}
          dataSource={data}
          loading={loading}
          rowKey="recordId"
          pagination={false}
          size="small"
        />
      </Card>
    </div>
  )
}
