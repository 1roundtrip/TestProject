/**
 * Excel导出工具函数
 * 使用简单的CSV格式导出，兼容Excel
 */

/**
 * 导出数据为Excel（CSV格式）
 * @param data 要导出的数据数组
 * @param columns 列配置，包含title和dataIndex
 * @param filename 文件名（不含扩展名）
 */
export function exportToExcel<T extends Record<string, any>>(
  data: T[],
  columns: Array<{ title: string; dataIndex: string; render?: (value: any, record: T) => any }>,
  filename: string = 'export'
) {
  // CSV头部（BOM UTF-8，确保Excel正确识别中文）
  const BOM = '\uFEFF'
  let csv = BOM

  // 添加表头
  const headers = columns.map(col => col.title).join(',')
  csv += headers + '\n'

  // 添加数据行
  data.forEach(record => {
    const row = columns.map(col => {
      let value = record[col.dataIndex]
      
      // 如果列有自定义render函数，尝试提取文本值
      if (col.render) {
        try {
          const rendered = col.render(value, record)
          // 如果是React元素，尝试提取文本内容
          if (rendered && typeof rendered === 'object' && 'props' in rendered) {
            // 如果是Tag组件，提取children
            if (rendered.props && rendered.props.children) {
              value = Array.isArray(rendered.props.children) 
                ? rendered.props.children.join('') 
                : rendered.props.children
            } else {
              value = rendered
            }
          } else {
            value = rendered
          }
        } catch (e) {
          // 如果render失败，使用原始值
          value = record[col.dataIndex]
        }
      }
      
      // 处理null和undefined
      if (value === null || value === undefined) {
        return ''
      }
      
      // 如果是对象或数组，转换为字符串
      if (typeof value === 'object') {
        // 如果是React元素，尝试提取文本
        if (value && typeof value === 'object' && 'props' in value) {
          value = value.props?.children || ''
        } else {
          value = JSON.stringify(value)
        }
      }
      
      // 转换为字符串并处理特殊字符
      const str = String(value).trim()
      
      // 如果包含逗号、引号或换行符，需要用引号包裹并转义引号
      if (str.includes(',') || str.includes('"') || str.includes('\n')) {
        return `"${str.replace(/"/g, '""')}"`
      }
      
      return str
    }).join(',')
    
    csv += row + '\n'
  })

  // 创建Blob并下载
  const blob = new Blob([csv], { type: 'text/csv;charset=utf-8;' })
  const link = document.createElement('a')
  const url = URL.createObjectURL(blob)
  
  link.setAttribute('href', url)
  link.setAttribute('download', `${filename}_${new Date().getTime()}.csv`)
  link.style.visibility = 'hidden'
  
  document.body.appendChild(link)
  link.click()
  document.body.removeChild(link)
  
  URL.revokeObjectURL(url)
}

/**
 * 从后端API导出数据
 * @param url 导出接口URL
 * @param filename 文件名
 */
export async function exportFromAPI(url: string, filename: string = 'export') {
  try {
    const token = localStorage.getItem('token')
    const response = await fetch(url, {
      method: 'GET',
      headers: {
        'Authorization': `Bearer ${token}`,
      },
    })

    if (!response.ok) {
      throw new Error('导出失败')
    }

    const blob = await response.blob()
    const link = document.createElement('a')
    const downloadUrl = URL.createObjectURL(blob)
    
    link.setAttribute('href', downloadUrl)
    link.setAttribute('download', `${filename}_${new Date().getTime()}.xlsx`)
    link.style.visibility = 'hidden'
    
    document.body.appendChild(link)
    link.click()
    document.body.removeChild(link)
    
    URL.revokeObjectURL(downloadUrl)
  } catch (error) {
    console.error('导出失败:', error)
    throw error
  }
}

