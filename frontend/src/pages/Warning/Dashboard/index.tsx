import { useState, useEffect } from 'react'
import { Card, Row, Col, Statistic, Tag, Table } from 'antd'
import { getDashboardSummary, getRecentWarnings } from '@/api/warning/dashboard'
import type { WarningRecord } from '@/api/warning/monitor'
import type { ColumnsType } from 'antd/es/table'

export default function WarningDashboardPage() {
  const [data, setData] = useState<WarningRecord[]>([])
  const [loading, setLoading] = useState(false)
  const [statistics, setStatistics] = useState<any>({})

  useEffect(() => {
    loadData()
    const interval = setInterval(() => {
      loadData()
    }, 30000) // 每30秒刷新一次
    return () => clearInterval(interval)
  }, [])

  const loadData = async () => {
    setLoading(true)
    try {
      const [summaryRes, recentRes] = await Promise.all([
        getDashboardSummary(),
        getRecentWarnings(10),
      ])
      
      setStatistics(summaryRes.data || {})
      setData(recentRes.data || [])
    } catch (error: any) {
      console.error('加载数据失败:', error)
    } finally {
      setLoading(false)
    }
  }

  const getLevelTag = (level?: string) => {
    const levelMap: Record<string, { color: string; text: string }> = {
      LOW: { color: 'success', text: '低' },
      MEDIUM: { color: 'warning', text: '中' },
      HIGH: { color: 'error', text: '高' },
      CRITICAL: { color: 'error', text: '紧急' },
    }
    const info = levelMap[level || ''] || { color: 'default', text: level || '未知' }
    return <Tag color={info.color}>{info.text}</Tag>
  }

  const columns: ColumnsType<WarningRecord> = [
    { title: '预警标题', dataIndex: 'warningTitle', key: 'warningTitle', width: 200 },
    {
      title: '预警级别',
      dataIndex: 'warningLevelName',
      key: 'warningLevelName',
      width: 100,
      render: (_, record) => getLevelTag(record.warningLevelCode),
    },
    { title: '来源类型', dataIndex: 'sourceType', key: 'sourceType', width: 100 },
    { title: '来源名称', dataIndex: 'sourceName', key: 'sourceName', width: 150 },
    { title: '触发时间', dataIndex: 'triggerTime', key: 'triggerTime', width: 180 },
  ]

  return (
    <div>
      <Row gutter={16} style={{ marginBottom: 16 }}>
        <Col span={6}>
          <Card>
            <Statistic title="预警总数" value={statistics.totalCount || 0} />
          </Card>
        </Col>
        <Col span={6}>
          <Card>
            <Statistic title="待处理" value={statistics.pendingCount || 0} valueStyle={{ color: '#faad14' }} />
          </Card>
        </Col>
        <Col span={6}>
          <Card>
            <Statistic title="处理中" value={statistics.processingCount || 0} valueStyle={{ color: '#1890ff' }} />
          </Card>
        </Col>
        <Col span={6}>
          <Card>
            <Statistic title="紧急预警" value={statistics.criticalCount || 0} valueStyle={{ color: '#f5222d' }} />
          </Card>
        </Col>
      </Row>

      <Card title="最新预警">
        <Table
          columns={columns}
          dataSource={data.slice(0, 10)}
          loading={loading}
          rowKey="recordId"
          pagination={false}
          size="small"
        />
      </Card>
    </div>
  )
}
