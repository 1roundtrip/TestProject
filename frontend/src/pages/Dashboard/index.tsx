import { useEffect, useState } from 'react'
import { Row, Col, Card, Statistic, Spin } from 'antd'
import {
  DatabaseOutlined,
  ToolOutlined,
  WarningOutlined,
  AlertOutlined,
} from '@ant-design/icons'
import ReactECharts from 'echarts-for-react'
import { getDashboardStats, getAssetStatusDistribution, getRepairTrend } from '@/api/dashboard'

export default function Dashboard() {
  const [loading, setLoading] = useState(true)
  const [stats, setStats] = useState({
    totalAssets: 0,
    repairingAssets: 0,
    inventoryAlerts: 0,
    explosionProofWarnings: 0,
  })
  const [statusData, setStatusData] = useState<any[]>([])
  const [trendData, setTrendData] = useState<any[]>([])

  useEffect(() => {
    loadData()
    // 每30秒刷新一次
    const timer = setInterval(loadData, 30000)
    return () => clearInterval(timer)
  }, [])

  const loadData = async () => {
    try {
      setLoading(true)
      
      // 获取统计数据
      const statsRes = await getDashboardStats()
      if (statsRes.code === 200 && statsRes.data) {
        setStats({
          totalAssets: statsRes.data.totalAssets || 0,
          repairingAssets: statsRes.data.repairingAssets || 0,
          inventoryAlerts: statsRes.data.inventoryAlerts || 0,
          explosionProofWarnings: statsRes.data.explosionProofWarnings || 0,
        })
      }
      
      // 获取设备状态分布
      const statusRes = await getAssetStatusDistribution()
      if (statusRes.code === 200 && statusRes.data) {
        setStatusData(statusRes.data)
      }
      
      // 获取维修趋势
      const trendRes = await getRepairTrend()
      if (trendRes.code === 200 && trendRes.data) {
        setTrendData(trendRes.data)
      }
    } catch (error) {
      console.error('加载数据失败', error)
    } finally {
      setLoading(false)
    }
  }

  const statusChartOption = {
    title: {
      text: '设备状态分布',
      left: 'center',
    },
    tooltip: {
      trigger: 'item',
    },
    legend: {
      orient: 'vertical',
      left: 'left',
    },
    series: [
      {
        name: '设备状态',
        type: 'pie',
        radius: '50%',
        data: statusData,
        emphasis: {
          itemStyle: {
            shadowBlur: 10,
            shadowOffsetX: 0,
            shadowColor: 'rgba(0, 0, 0, 0.5)',
          },
        },
      },
    ],
  }

  const trendChartOption = {
    title: {
      text: '维修趋势',
      left: 'center',
    },
    tooltip: {
      trigger: 'axis',
    },
    xAxis: {
      type: 'category',
      data: trendData && trendData.length > 0 ? trendData.map((item) => item.date) : [],
    },
    yAxis: {
      type: 'value',
    },
    series: [
      {
        name: '维修数量',
        type: 'line',
        data: trendData && trendData.length > 0 ? trendData.map((item) => item.count) : [],
        smooth: true,
        itemStyle: {
          color: '#3b82f6',
        },
      },
    ],
  }

  if (loading) {
    return (
      <div style={{ textAlign: 'center', padding: '50px' }}>
        <Spin size="large" />
      </div>
    )
  }

  return (
    <div>
      <Row gutter={[16, 16]}>
        <Col xs={24} sm={12} lg={6}>
          <Card>
            <Statistic
              title="设备总数"
              value={stats.totalAssets}
              prefix={<DatabaseOutlined />}
              valueStyle={{ color: '#3b82f6' }}
            />
          </Card>
        </Col>
        <Col xs={24} sm={12} lg={6}>
          <Card>
            <Statistic
              title="待维修数"
              value={stats.repairingAssets}
              prefix={<ToolOutlined />}
              valueStyle={{ color: '#f59e0b' }}
            />
          </Card>
        </Col>
        <Col xs={24} sm={12} lg={6}>
          <Card>
            <Statistic
              title="库存预警"
              value={stats.inventoryAlerts}
              prefix={<WarningOutlined />}
              valueStyle={{ color: '#ef4444' }}
            />
          </Card>
        </Col>
        <Col xs={24} sm={12} lg={6}>
          <Card>
            <Statistic
              title="防爆预警"
              value={stats.explosionProofWarnings}
              prefix={<AlertOutlined />}
              valueStyle={{ color: '#ef4444' }}
            />
          </Card>
        </Col>
      </Row>

      <Row gutter={[16, 16]} style={{ marginTop: 16 }}>
        <Col xs={24} lg={12}>
          <Card>
            <ReactECharts option={statusChartOption} style={{ height: '400px' }} />
          </Card>
        </Col>
        <Col xs={24} lg={12}>
          <Card>
            <ReactECharts option={trendChartOption} style={{ height: '400px' }} />
          </Card>
        </Col>
      </Row>
    </div>
  )
}

