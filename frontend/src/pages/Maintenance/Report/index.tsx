import { useState } from 'react'
import { Card, DatePicker, Button, Statistic, Row, Col, Table, message, Tabs, Tag } from 'antd'
import { DownloadOutlined } from '@ant-design/icons'
import { Permission } from '@/components/Permission'
import { getStatistics, getWorkOrderStatistics, getCostStatistics, getFaultStatistics } from '@/api/maintenance/report'
import { exportToExcel } from '@/utils/export'
import dayjs from 'dayjs'
import type { ColumnsType } from 'antd/es/table'

const { RangePicker } = DatePicker

export default function MaintenanceReportPage() {
  const [dateRange, setDateRange] = useState<[dayjs.Dayjs, dayjs.Dayjs] | null>(null)
  const [statistics, setStatistics] = useState<any>({})
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
      const [statRes, workOrderRes, costRes, faultRes] = await Promise.all([
        getStatistics(startDate, endDate),
        getWorkOrderStatistics(startDate, endDate),
        getCostStatistics(startDate, endDate),
        getFaultStatistics(startDate, endDate),
      ])
      setStatistics({
        ...statRes.data,
        workOrder: workOrderRes.data,
        cost: costRes.data,
        fault: faultRes.data,
      })
    } catch (error: any) {
      message.error(error.response?.data?.msg || '加载数据失败')
    } finally {
      setLoading(false)
    }
  }

  const handleExport = () => {
    if (!dateRange) {
      message.warning('请先选择日期范围并加载数据')
      return
    }
    // 导出详细统计数据
    const exportData = [
      ...(statistics.workOrder?.list || []).map((item: any) => ({
        工单编号: item.workOrderNo,
        设备名称: item.assetName,
        工单类型: item.workOrderType,
        状态: item.status,
        优先级: item.priority,
        报修时间: item.reportedTime,
        完成时间: item.completedTime,
      })),
      ...(statistics.cost?.list || []).map((item: any) => ({
        成本类型: item.costType,
        金额: item.amount,
        工单编号: item.workOrderNo,
        发生日期: item.costDate,
      })),
      ...(statistics.fault?.list || []).map((item: any) => ({
        故障类型: item.faultType,
        设备名称: item.assetName,
        故障描述: item.faultDescription,
        发生时间: item.faultTime,
      })),
    ]
    
    const columns = [
      { title: '工单编号', dataIndex: '工单编号' },
      { title: '设备名称', dataIndex: '设备名称' },
      { title: '工单类型', dataIndex: '工单类型' },
      { title: '状态', dataIndex: '状态' },
      { title: '优先级', dataIndex: '优先级' },
      { title: '报修时间', dataIndex: '报修时间' },
      { title: '完成时间', dataIndex: '完成时间' },
      { title: '成本类型', dataIndex: '成本类型' },
      { title: '金额', dataIndex: '金额' },
      { title: '发生日期', dataIndex: '发生日期' },
      { title: '故障类型', dataIndex: '故障类型' },
      { title: '故障描述', dataIndex: '故障描述' },
      { title: '发生时间', dataIndex: '发生时间' },
    ]
    
    exportToExcel(exportData, columns, `维修报表_${dateRange[0].format('YYYY-MM-DD')}_${dateRange[1].format('YYYY-MM-DD')}`)
    message.success('导出成功')
  }

  const workOrderColumns: ColumnsType<any> = [
    {
      title: '工单编号',
      dataIndex: 'workOrderNo',
      key: 'workOrderNo',
      width: 150,
    },
    {
      title: '设备名称',
      dataIndex: 'assetName',
      key: 'assetName',
      width: 150,
    },
    {
      title: '工单类型',
      dataIndex: 'workOrderType',
      key: 'workOrderType',
      width: 120,
      render: (type) => {
        const typeMap: Record<string, string> = {
          REPAIR: '维修',
          MAINTENANCE: '保养',
          INSPECTION: '检查',
          EMERGENCY: '紧急',
        }
        return typeMap[type] || type
      },
    },
    {
      title: '状态',
      dataIndex: 'status',
      key: 'status',
      width: 100,
      render: (status) => {
        const statusMap: Record<string, { text: string; color: string }> = {
          PENDING: { text: '待分配', color: 'default' },
          ASSIGNED: { text: '已分配', color: 'processing' },
          IN_PROGRESS: { text: '进行中', color: 'processing' },
          PAUSED: { text: '暂停', color: 'warning' },
          COMPLETED: { text: '已完成', color: 'success' },
          CANCELLED: { text: '已取消', color: 'error' },
        }
        const info = statusMap[status] || { text: status, color: 'default' }
        return <Tag color={info.color}>{info.text}</Tag>
      },
    },
    {
      title: '优先级',
      dataIndex: 'priority',
      key: 'priority',
      width: 100,
      render: (priority) => {
        const priorityMap: Record<string, { text: string; color: string }> = {
          LOW: { text: '低', color: 'default' },
          NORMAL: { text: '正常', color: 'processing' },
          HIGH: { text: '高', color: 'warning' },
          URGENT: { text: '紧急', color: 'error' },
        }
        const info = priorityMap[priority] || { text: priority, color: 'default' }
        return <Tag color={info.color}>{info.text}</Tag>
      },
    },
    {
      title: '报修时间',
      dataIndex: 'reportedTime',
      key: 'reportedTime',
      width: 180,
    },
    {
      title: '完成时间',
      dataIndex: 'completedTime',
      key: 'completedTime',
      width: 180,
    },
  ]

  const costColumns: ColumnsType<any> = [
    {
      title: '成本类型',
      dataIndex: 'costType',
      key: 'costType',
      width: 120,
      render: (type) => {
        const typeMap: Record<string, string> = {
          LABOR: '人工',
          MATERIAL: '材料',
          OUTSOURCING: '外包',
          TRANSPORT: '运输',
          OTHER: '其他',
        }
        return typeMap[type] || type
      },
    },
    {
      title: '金额',
      dataIndex: 'amount',
      key: 'amount',
      width: 120,
      render: (amount) => (amount ? `¥${Number(amount).toFixed(2)}` : '-'),
    },
    {
      title: '工单编号',
      dataIndex: 'workOrderNo',
      key: 'workOrderNo',
      width: 150,
    },
    {
      title: '设备名称',
      dataIndex: 'assetName',
      key: 'assetName',
      width: 150,
    },
    {
      title: '发生日期',
      dataIndex: 'costDate',
      key: 'costDate',
      width: 120,
    },
    {
      title: '备注',
      dataIndex: 'remark',
      key: 'remark',
    },
  ]

  const faultColumns: ColumnsType<any> = [
    {
      title: '故障类型',
      dataIndex: 'faultType',
      key: 'faultType',
      width: 120,
    },
    {
      title: '设备名称',
      dataIndex: 'assetName',
      key: 'assetName',
      width: 150,
    },
    {
      title: '故障描述',
      dataIndex: 'faultDescription',
      key: 'faultDescription',
      width: 200,
    },
    {
      title: '严重程度',
      dataIndex: 'severity',
      key: 'severity',
      width: 100,
      render: (severity) => {
        const severityMap: Record<string, { text: string; color: string }> = {
          LOW: { text: '低', color: 'default' },
          MEDIUM: { text: '中', color: 'warning' },
          HIGH: { text: '高', color: 'error' },
        }
        const info = severityMap[severity] || { text: severity, color: 'default' }
        return <Tag color={info.color}>{info.text}</Tag>
      },
    },
    {
      title: '发生时间',
      dataIndex: 'faultTime',
      key: 'faultTime',
      width: 180,
    },
    {
      title: '处理状态',
      dataIndex: 'status',
      key: 'status',
      width: 100,
      render: (status) => {
        const statusMap: Record<string, { text: string; color: string }> = {
          PENDING: { text: '待处理', color: 'default' },
          PROCESSING: { text: '处理中', color: 'processing' },
          RESOLVED: { text: '已解决', color: 'success' },
        }
        const info = statusMap[status] || { text: status, color: 'default' }
        return <Tag color={info.color}>{info.text}</Tag>
      },
    },
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
            <Permission permission="maintenance:report:export">
              <Button icon={<DownloadOutlined />} onClick={handleExport}>
                导出报表
              </Button>
            </Permission>
          </Col>
        </Row>
      </Card>

      <Row gutter={16} style={{ marginBottom: 16 }}>
        <Col span={6}>
          <Card>
            <Statistic title="工单总数" value={statistics.totalWorkOrders || 0} />
          </Card>
        </Col>
        <Col span={6}>
          <Card>
            <Statistic title="完成工单" value={statistics.completedWorkOrders || 0} />
          </Card>
        </Col>
        <Col span={6}>
          <Card>
            <Statistic title="总成本" value={statistics.totalCost || 0} prefix="¥" precision={2} />
          </Card>
        </Col>
        <Col span={6}>
          <Card>
            <Statistic title="故障次数" value={statistics.faultCount || 0} />
          </Card>
        </Col>
      </Row>

      <Card title="详细统计">
        <Tabs
          defaultActiveKey="workOrder"
          items={[
            {
              key: 'workOrder',
              label: '工单统计',
              children: (
                <Table
                  columns={workOrderColumns}
                  dataSource={statistics.workOrder?.list || []}
                  rowKey="workOrderId"
                  pagination={false}
                  size="small"
                />
              ),
            },
            {
              key: 'cost',
              label: '成本统计',
              children: (
                <Table
                  columns={costColumns}
                  dataSource={statistics.cost?.list || []}
                  rowKey="costId"
                  pagination={false}
                  size="small"
                />
              ),
            },
            {
              key: 'fault',
              label: '故障统计',
              children: (
                <Table
                  columns={faultColumns}
                  dataSource={statistics.fault?.list || []}
                  rowKey="faultId"
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

