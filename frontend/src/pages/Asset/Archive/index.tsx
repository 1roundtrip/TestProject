import { useState, useEffect } from 'react'
import { Table, Button, Space, Modal, Form, Input, InputNumber, DatePicker, Select, message, Popconfirm, Upload, Drawer } from 'antd'
import { PlusOutlined, EditOutlined, DeleteOutlined, SearchOutlined, DownloadOutlined, UploadOutlined, SettingOutlined } from '@ant-design/icons'
import type { ColumnsType } from 'antd/es/table'
import { Permission } from '@/components/Permission'
import { getAssetPage, addAsset, updateAsset, deleteAsset, getAssetTypes, getManufacturers, type Asset } from '@/api/asset'
import { exportToExcel } from '@/utils/export'
import { parseExcelFile, downloadImportTemplate, validateImportData } from '@/utils/import'
import dayjs from 'dayjs'
import type { UploadFile } from 'antd/es/upload/interface'

const { RangePicker } = DatePicker

export default function AssetArchivePage() {
  const [data, setData] = useState<Asset[]>([])
  const [loading, setLoading] = useState(false)
  const [total, setTotal] = useState(0)
  const [current, setCurrent] = useState(1)
  const [pageSize, setPageSize] = useState(10)
  const [modalVisible, setModalVisible] = useState(false)
  const [editingRecord, setEditingRecord] = useState<Asset | null>(null)
  const [form] = Form.useForm()
  const [assetTypes, setAssetTypes] = useState<string[]>([])
  const [manufacturers, setManufacturers] = useState<string[]>([])
  const [assetTypeOptions, setAssetTypeOptions] = useState<Array<{ label: string; value: string }>>([])
  const [manufacturerOptions, setManufacturerOptions] = useState<Array<{ label: string; value: string }>>([])
  const [importModalVisible, setImportModalVisible] = useState(false)
  const [settingDrawerVisible, setSettingDrawerVisible] = useState(false)
  const [importFileList, setImportFileList] = useState<UploadFile[]>([])

  useEffect(() => {
    loadData()
    loadAssetTypes()
    loadManufacturers()
  }, [current, pageSize])

  const loadAssetTypes = async () => {
    try {
      const res = await getAssetTypes()
      if (res.code === 200 && res.data) {
        setAssetTypes(res.data)
        const defaultTypes = ['电气设备', '机械设备', '通信设备', '监控设备', '检测设备']
        const allTypes = [...new Set([...res.data, ...defaultTypes])]
        setAssetTypeOptions(allTypes.map(type => ({ label: type, value: type })))
      }
    } catch (error) {
      console.error('加载资产类型失败', error)
    }
  }

  const loadManufacturers = async () => {
    try {
      const res = await getManufacturers()
      if (res.code === 200 && res.data) {
        setManufacturers(res.data)
        setManufacturerOptions(res.data.map(manufacturer => ({ label: manufacturer, value: manufacturer })))
      }
    } catch (error) {
      console.error('加载制造商失败', error)
    }
  }

  const handleAssetTypeSearch = (value: string) => {
    if (value && !assetTypeOptions.some(opt => opt.value === value)) {
      setAssetTypeOptions([...assetTypeOptions, { label: value, value }])
    }
  }

  const handleManufacturerSearch = (value: string) => {
    if (value && !manufacturerOptions.some(opt => opt.value === value)) {
      setManufacturerOptions([...manufacturerOptions, { label: value, value }])
    }
  }

  const loadData = async () => {
    setLoading(true)
    try {
      const res = await getAssetPage({
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

  const handleAdd = () => {
    setEditingRecord(null)
    form.resetFields()
    setModalVisible(true)
  }

  const handleEdit = (record: Asset) => {
    setEditingRecord(record)
    form.setFieldsValue({
      ...record,
      purchaseDate: record.purchaseDate ? dayjs(record.purchaseDate) : null,
      explosionProofExpireDate: record.explosionProofExpireDate ? dayjs(record.explosionProofExpireDate) : null,
    })
    setModalVisible(true)
  }

  const handleDelete = async (id: number) => {
    try {
      await deleteAsset(id)
      message.success('删除成功')
      loadData()
    } catch (error) {
      message.error('删除失败')
    }
  }

  const handleSubmit = async () => {
    try {
      const values = await form.validateFields()
      const submitData = {
        ...values,
        purchaseDate: values.purchaseDate ? values.purchaseDate.format('YYYY-MM-DD') : undefined,
        explosionProofExpireDate: values.explosionProofExpireDate ? values.explosionProofExpireDate.format('YYYY-MM-DD') : undefined,
      }
      
      if (editingRecord?.assetId) {
        await updateAsset({ ...submitData, assetId: editingRecord.assetId })
        message.success('更新成功')
      } else {
        await addAsset(submitData)
        message.success('新增成功')
      }
      
      setModalVisible(false)
      loadData()
    } catch (error) {
      console.error('提交失败', error)
    }
  }

  // 处理导入
  const handleImport = async (file: File) => {
    try {
      const data = await parseExcelFile(file)
      
      // 验证数据
      const validation = validateImportData(data, ['资产编码', '资产名称'])
      if (!validation.valid) {
        message.error(`数据验证失败：${validation.errors.join('; ')}`)
        return false
      }

      // 转换数据格式
      const importData = data.map(row => ({
        assetCode: row['资产编码'] || row['assetCode'],
        assetName: row['资产名称'] || row['assetName'],
        assetType: row['资产类型'] || row['assetType'],
        category: row['类别'] || row['category'],
        manufacturer: row['制造商'] || row['manufacturer'],
        model: row['型号'] || row['model'],
        serialNumber: row['序列号'] || row['serialNumber'],
        purchaseDate: row['采购日期'] || row['purchaseDate'],
        purchasePrice: row['采购价格'] || row['purchasePrice'],
        status: row['状态'] || row['status'] || '0',
        location: row['位置'] || row['location'],
        isExplosionProof: row['是否防爆'] || row['isExplosionProof'] || '0',
        explosionProofExpireDate: row['防爆证书到期日期'] || row['explosionProofExpireDate'],
        remark: row['备注'] || row['remark'],
      }))

      // 批量导入
      let successCount = 0
      let failCount = 0
      for (const item of importData) {
        try {
          await addAsset(item)
          successCount++
        } catch (error) {
          failCount++
          console.error('导入失败:', item, error)
        }
      }

      message.success(`导入完成：成功 ${successCount} 条，失败 ${failCount} 条`)
      setImportModalVisible(false)
      setImportFileList([])
      loadData()
      return false
    } catch (error) {
      message.error('导入失败：' + (error instanceof Error ? error.message : '未知错误'))
      return false
    }
  }

  // 下载导入模板
  const handleDownloadTemplate = () => {
    const templateColumns = [
      { title: '资产编码', dataIndex: 'assetCode' },
      { title: '资产名称', dataIndex: 'assetName' },
      { title: '资产类型', dataIndex: 'assetType' },
      { title: '类别', dataIndex: 'category' },
      { title: '制造商', dataIndex: 'manufacturer' },
      { title: '型号', dataIndex: 'model' },
      { title: '序列号', dataIndex: 'serialNumber' },
      { title: '采购日期', dataIndex: 'purchaseDate' },
      { title: '采购价格', dataIndex: 'purchasePrice' },
      { title: '状态', dataIndex: 'status' },
      { title: '位置', dataIndex: 'location' },
      { title: '是否防爆', dataIndex: 'isExplosionProof' },
      { title: '防爆证书到期日期', dataIndex: 'explosionProofExpireDate' },
      { title: '备注', dataIndex: 'remark' },
    ]
    downloadImportTemplate(templateColumns, '资产导入模板')
  }

  const columns: ColumnsType<Asset> = [
    {
      title: '资产编码',
      dataIndex: 'assetCode',
      key: 'assetCode',
    },
    {
      title: '资产名称',
      dataIndex: 'assetName',
      key: 'assetName',
    },
    {
      title: '资产类型',
      dataIndex: 'assetType',
      key: 'assetType',
    },
    {
      title: '制造商',
      dataIndex: 'manufacturer',
      key: 'manufacturer',
    },
    {
      title: '状态',
      dataIndex: 'status',
      key: 'status',
      render: (status) => {
        const statusMap: Record<string, { text: string; color: string }> = {
          '0': { text: '正常', color: 'green' },
          '1': { text: '维修中', color: 'orange' },
          '2': { text: '报废', color: 'red' },
        }
        const statusInfo = statusMap[status] || { text: status, color: 'default' }
        return <span style={{ color: statusInfo.color }}>{statusInfo.text}</span>
      },
    },
    {
      title: '操作',
      key: 'action',
      width: 150,
      render: (_, record) => (
        <Space>
          <Permission permission="asset:archive:edit">
            <Button
              type="link"
              icon={<EditOutlined />}
              onClick={() => handleEdit(record)}
            >
              编辑
            </Button>
          </Permission>
          <Permission permission="asset:archive:remove">
            <Popconfirm
              title="确定要删除吗？"
              onConfirm={() => record.assetId && handleDelete(record.assetId)}
            >
              <Button type="link" danger icon={<DeleteOutlined />}>
                删除
              </Button>
            </Popconfirm>
          </Permission>
        </Space>
      ),
    },
  ]

  return (
    <div>
      <div style={{ marginBottom: 16, display: 'flex', justifyContent: 'space-between' }}>
        <Space>
          <Input
            placeholder="搜索资产名称"
            style={{ width: 200 }}
            allowClear
          />
          <Button icon={<SearchOutlined />}>搜索</Button>
        </Space>
        <Space>
          <Button 
            icon={<DownloadOutlined />} 
            onClick={() => exportToExcel(data, columns, '资产列表')}
            disabled={data.length === 0}
          >
            导出Excel
          </Button>
          <Button 
            icon={<UploadOutlined />}
            onClick={() => setImportModalVisible(true)}
          >
            导入Excel
          </Button>
          <Button 
            icon={<SettingOutlined />}
            onClick={() => setSettingDrawerVisible(true)}
          >
            设置
          </Button>
          <Permission permission="asset:archive:add">
            <Button type="primary" icon={<PlusOutlined />} onClick={handleAdd}>
              新增资产
            </Button>
          </Permission>
        </Space>
      </div>

      <Table
        columns={columns}
        dataSource={data}
        loading={loading}
        rowKey="assetId"
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

      <Modal
        title={editingRecord ? '编辑资产' : '新增资产'}
        open={modalVisible}
        onOk={handleSubmit}
        onCancel={() => setModalVisible(false)}
        width={800}
      >
        <Form form={form} layout="vertical">
          <Form.Item
            name="assetCode"
            label="资产编码"
            rules={[{ required: true, message: '请输入资产编码' }]}
          >
            <Input />
          </Form.Item>
          <Form.Item
            name="assetName"
            label="资产名称"
            rules={[{ required: true, message: '请输入资产名称' }]}
          >
            <Input />
          </Form.Item>
          <Form.Item name="assetType" label="资产类型">
            <Select
              showSearch
              allowClear
              placeholder="请选择或输入资产类型"
              options={assetTypeOptions}
              onSearch={handleAssetTypeSearch}
              filterOption={(input, option) =>
                (option?.label ?? '').toLowerCase().includes(input.toLowerCase())
              }
            />
          </Form.Item>
          <Form.Item name="manufacturer" label="制造商">
            <Select
              showSearch
              allowClear
              placeholder="请选择或输入制造商"
              options={manufacturerOptions}
              onSearch={handleManufacturerSearch}
              filterOption={(input, option) =>
                (option?.label ?? '').toLowerCase().includes(input.toLowerCase())
              }
            />
          </Form.Item>
          <Form.Item name="model" label="型号">
            <Input />
          </Form.Item>
          <Form.Item name="purchaseDate" label="采购日期">
            <DatePicker style={{ width: '100%' }} />
          </Form.Item>
          <Form.Item name="purchasePrice" label="采购价格">
            <InputNumber style={{ width: '100%' }} min={0} precision={2} />
          </Form.Item>
          <Form.Item name="status" label="状态">
            <Select>
              <Select.Option value="0">正常</Select.Option>
              <Select.Option value="1">维修中</Select.Option>
              <Select.Option value="2">报废</Select.Option>
            </Select>
          </Form.Item>
          <Form.Item name="isExplosionProof" label="是否防爆设备">
            <Select>
              <Select.Option value="0">否</Select.Option>
              <Select.Option value="1">是</Select.Option>
            </Select>
          </Form.Item>
          <Form.Item name="explosionProofExpireDate" label="防爆证书到期日期">
            <DatePicker style={{ width: '100%' }} />
          </Form.Item>
          <Form.Item name="remark" label="备注">
            <Input.TextArea rows={3} />
          </Form.Item>
        </Form>
      </Modal>

      {/* 导入Modal */}
      <Modal
        title="导入资产数据"
        open={importModalVisible}
        onCancel={() => {
          setImportModalVisible(false)
          setImportFileList([])
        }}
        footer={null}
        width={600}
      >
        <Space direction="vertical" style={{ width: '100%' }} size="large">
          <div>
            <Button onClick={handleDownloadTemplate} type="link">
              下载导入模板
            </Button>
            <p style={{ marginTop: 8, color: '#666' }}>
              请先下载模板，按照模板格式填写数据后上传
            </p>
          </div>
          <Upload
            fileList={importFileList}
            beforeUpload={(file) => {
              handleImport(file)
              return false
            }}
            onChange={({ fileList }) => setImportFileList(fileList)}
            accept=".xlsx,.xls,.csv"
            maxCount={1}
          >
            <Button icon={<UploadOutlined />}>选择Excel文件</Button>
          </Upload>
        </Space>
      </Modal>

      {/* 设置抽屉 */}
      <Drawer
        title="页面设置"
        placement="right"
        onClose={() => setSettingDrawerVisible(false)}
        open={settingDrawerVisible}
        width={400}
      >
        <Space direction="vertical" style={{ width: '100%' }} size="large">
          <div>
            <h4>显示设置</h4>
            <Space direction="vertical" style={{ width: '100%' }}>
              <Form.Item label="每页显示条数">
                <Select
                  value={pageSize}
                  onChange={(value) => {
                    setPageSize(value)
                    setCurrent(1)
                  }}
                  style={{ width: '100%' }}
                >
                  <Select.Option value={10}>10条/页</Select.Option>
                  <Select.Option value={20}>20条/页</Select.Option>
                  <Select.Option value={50}>50条/页</Select.Option>
                  <Select.Option value={100}>100条/页</Select.Option>
                </Select>
              </Form.Item>
            </Space>
          </div>
          <div>
            <h4>数据管理</h4>
            <Space direction="vertical" style={{ width: '100%' }}>
              <Button block onClick={() => {
                handleDownloadTemplate()
                setSettingDrawerVisible(false)
              }}>
                下载导入模板
              </Button>
              <Button block onClick={() => {
                exportToExcel(data, columns, '资产列表')
                setSettingDrawerVisible(false)
              }} disabled={data.length === 0}>
                导出当前数据
              </Button>
            </Space>
          </div>
        </Space>
      </Drawer>
    </div>
  )
}

