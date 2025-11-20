import { useState, useEffect } from 'react'
import { Table, Button, Card, message } from 'antd'
import { Permission } from '@/components/Permission'
import { getCheckins, type MaintenanceMobileCheckin } from '@/api/maintenance/mobile'
import type { ColumnsType } from 'antd/es/table'

export default function MaintenanceMobilePage() {
  const [data, setData] = useState<MaintenanceMobileCheckin[]>([])
  const [loading, setLoading] = useState(false)

  const loadData = async () => {
    setLoading(true)
    try {
      // TODO: 获取所有签到记录
      setData([])
    } catch (error: any) {
      message.error(error.response?.data?.msg || '加载数据失败')
    } finally {
      setLoading(false)
    }
  }

  useEffect(() => {
    loadData()
  }, [])

  const columns: ColumnsType<MaintenanceMobileCheckin> = [
    { title: '工单ID', dataIndex: 'workOrderId', key: 'workOrderId' },
    { title: '技师', dataIndex: 'technicianName', key: 'technicianName' },
    { title: '签到类型', dataIndex: 'checkinType', key: 'checkinType' },
    { title: '位置', dataIndex: 'location', key: 'location' },
    { title: '签到时间', dataIndex: 'checkinTime', key: 'checkinTime' },
  ]

  return (
    <div>
      <Card title="移动维修签到记录">
        <Table
          columns={columns}
          dataSource={data}
          loading={loading}
          rowKey="checkinId"
        />
      </Card>
    </div>
  )
}

