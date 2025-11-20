import { useState, useEffect } from 'react'
import { Card, Row, Col, Statistic, DatePicker, Button, message, Table, Tabs } from 'antd'
import { DownloadOutlined } from '@ant-design/icons'
import dayjs from 'dayjs'
import { getReportStatistics, getReportPage, type WarningRecord } from '@/api/warning/report'
import { exportToExcel } from '@/utils/export'
import type { ColumnsType } from 'antd/es/table'

const { RangePicker } = DatePicker

export default function WarningReportPage() {
  const [dateRange, setDateRange] = useState<[dayjs.Dayjs, dayjs.Dayjs] | null>(null)
  const [data, setData] = useState<WarningRecord[]>([])
  const [loading, setLoading] = useState(false)
  const [statistics, setStatistics] = useState<any>({})

  const loadReport = async () => {
    if (!dateRange) {
      message.warning('请选择日期范围')
      return
    }
    setLoading(true)
    try {
      const startDate = dateRange[0].format('YYYY-MM-DD')
      const endDate = dateRange[1].format('YYYY-MM-DD')
      
      const [statsRes, pageRes] = await Promise.all([
        getReportStatistics({ startDate, endDate }),
        getReportPage({ current: 1, size: 1000 }),
      ])
      
      setStatistics(statsRes.data || {})
      setData(pageRes.data.records || [])
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
      { title: '预警级别', dataIndex: 'warningLevelName', key: 'warningLevelName' },
      { title: '状态', dataIndex: 'status', key: 'status' },
      { title: '触发时间', dataIndex: 'triggerTime', key: 'triggerTime' },
    ]
    exportToExcel(data, columns.filter(col => 'dataIndex' in col && col.dataIndex) as Array<{ title: string; dataIndex: string }>, '预警报表')
    message.success('导出成功')
  }

  const typeColumns: ColumnsType<any> = [
    { title: '预警类型', dataIndex: 'type', key: 'type' },
    { title: '数量', dataIndex: 'count', key: 'count' },
  ]

  const levelColumns: ColumnsType<any> = [
    { title: '预警级别', dataIndex: 'level', key: 'level' },
    { title: '数量', dataIndex: 'count', key: 'count' },
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
            <Button type="primary" onClick={loadReport} loading={loading}>
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
        <Col span={8}>
          <Card>
            <Statistic title="预警总数" value={statistics.totalCount || 0} />
          </Card>
        </Col>
        <Col span={8}>
          <Card>
            <Statistic title="高级预警" value={statistics.levelStats?.HIGH || 0} valueStyle={{ color: '#fa8c16' }} />
          </Card>
        </Col>
        <Col span={8}>
          <Card>
            <Statistic title="紧急预警" value={statistics.levelStats?.CRITICAL || 0} valueStyle={{ color: '#f5222d' }} />
          </Card>
        </Col>
      </Row>

      <Card title="预警报表分析">
        <Tabs
          defaultActiveKey="type"
          items={[
            {
              key: 'type',
              label: '按类型统计',
              children: (
                <Table
                  columns={typeColumns}
                  dataSource={Object.entries(statistics.typeStats || {}).map(([type, count]) => ({ type, count, key: type }))}
                  pagination={false}
                  size="small"
                />
              ),
            },
            {
              key: 'level',
              label: '按级别统计',
              children: (
                <Table
                  columns={levelColumns}
                  dataSource={Object.entries(statistics.levelStats || {}).map(([level, count]) => ({ level, count, key: level }))}
                  pagination={false}
                  size="small"
                />
              ),
            },
            {
              key: 'detail',
              label: '详细列表',
              children: (
                <Table
                  columns={[
                    { title: '预警标题', dataIndex: 'warningTitle', key: 'warningTitle' },
                    { title: '预警类型', dataIndex: 'warningType', key: 'warningType' },
                    { title: '预警级别', dataIndex: 'warningLevelName', key: 'warningLevelName' },
                    { title: '状态', dataIndex: 'status', key: 'status' },
                    { title: '触发时间', dataIndex: 'triggerTime', key: 'triggerTime' },
                  ]}
                  dataSource={data}
                  rowKey="recordId"
                  pagination={false}
                  size="small"
                />
              ),
            },
          ]}
        />
      </Card>
    </div>
  )
}
