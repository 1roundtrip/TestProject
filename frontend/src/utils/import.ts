/**
 * Excel导入工具函数
 * 支持CSV和Excel文件解析
 */

import { message } from 'antd'

/**
 * 解析CSV文件
 */
function parseCSV(content: string): Array<Record<string, any>> {
  const lines = content.split('\n').filter(line => line.trim())
  if (lines.length < 2) {
    throw new Error('CSV文件至少需要包含表头和数据行')
  }

  // 解析CSV行（处理引号内的逗号）
  const parseCSVLine = (line: string): string[] => {
    const result: string[] = []
    let current = ''
    let inQuotes = false
    
    for (let i = 0; i < line.length; i++) {
      const char = line[i]
      if (char === '"') {
        if (inQuotes && line[i + 1] === '"') {
          current += '"'
          i++
        } else {
          inQuotes = !inQuotes
        }
      } else if (char === ',' && !inQuotes) {
        result.push(current.trim())
        current = ''
      } else {
        current += char
      }
    }
    result.push(current.trim())
    return result
  }

  const headers = parseCSVLine(lines[0])
  const rows = lines.slice(1).map(parseCSVLine)
  
  return rows
    .filter(row => row.some(cell => cell !== ''))
    .map(row => {
      const obj: Record<string, any> = {}
      headers.forEach((header, index) => {
        if (header) {
          obj[header] = row[index] ?? null
        }
      })
      return obj
    })
}

/**
 * 解析Excel文件（简化版，优先使用CSV）
 * @param file 文件对象
 * @returns Promise<Array<Record<string, any>>>
 */
export function parseExcelFile(file: File): Promise<Array<Record<string, any>>> {
  return new Promise((resolve, reject) => {
    // 如果是CSV文件，直接解析
    if (file.name.endsWith('.csv')) {
      const reader = new FileReader()
      reader.onload = (e) => {
        try {
          const content = e.target?.result as string
          const result = parseCSV(content)
          resolve(result)
        } catch (error) {
          reject(error)
        }
      }
      reader.onerror = () => reject(new Error('文件读取失败'))
      reader.readAsText(file, 'UTF-8')
      return
    }

    // Excel文件需要xlsx库，如果没有则提示
    try {
      // 动态导入xlsx
      import('xlsx').then((XLSX) => {
        const reader = new FileReader()
        reader.onload = (e) => {
          try {
            const data = e.target?.result as ArrayBuffer
            // 使用array类型读取，更兼容
            const workbook = XLSX.read(data, { type: 'array' })
            
            const firstSheetName = workbook.SheetNames[0]
            const worksheet = workbook.Sheets[firstSheetName]
            
            const jsonData = XLSX.utils.sheet_to_json(worksheet, { 
              header: 1,
              defval: null 
            }) as any[][]
            
            if (jsonData.length < 2) {
              reject(new Error('Excel文件至少需要包含表头和数据行'))
              return
            }
            
            const headers = jsonData[0] as string[]
            const rows = jsonData.slice(1)
            
            const result = rows
              .filter(row => row.some(cell => cell !== null && cell !== ''))
              .map(row => {
                const obj: Record<string, any> = {}
                headers.forEach((header, index) => {
                  if (header) {
                    obj[header] = row[index] ?? null
                  }
                })
                return obj
              })
            
            resolve(result)
          } catch (error) {
            reject(error)
          }
        }
        reader.onerror = () => reject(new Error('文件读取失败'))
        reader.readAsBinaryString(file)
      }).catch(() => {
        // 如果没有xlsx库，提示用户安装或使用CSV
        reject(new Error('请安装xlsx库以支持Excel文件导入，或使用CSV格式。运行: npm install xlsx'))
      })
    } catch (error) {
      reject(error)
    }
  })
}

/**
 * 下载导入模板（CSV格式，兼容Excel）
 * @param columns 列配置
 * @param filename 文件名
 */
export function downloadImportTemplate(
  columns: Array<{ title: string; dataIndex: string }>,
  filename: string = 'import_template'
) {
  try {
    // 创建CSV内容（BOM UTF-8，确保Excel正确识别中文）
    const BOM = '\uFEFF'
    const headers = columns.map(col => col.title).join(',')
    const csv = BOM + headers + '\n'
    
    // 创建Blob并下载
    const blob = new Blob([csv], { type: 'text/csv;charset=utf-8;' })
    const link = document.createElement('a')
    const url = URL.createObjectURL(blob)
    
    link.setAttribute('href', url)
    link.setAttribute('download', `${filename}.csv`)
    link.style.visibility = 'hidden'
    
    document.body.appendChild(link)
    link.click()
    document.body.removeChild(link)
    
    URL.revokeObjectURL(url)
    message.success('模板下载成功')
  } catch (error) {
    console.error('下载模板失败:', error)
    message.error('下载模板失败')
  }
}

/**
 * 验证导入数据
 * @param data 导入的数据
 * @param requiredFields 必填字段列表
 * @returns { valid: boolean, errors: string[] }
 */
export function validateImportData(
  data: Array<Record<string, any>>,
  requiredFields: string[]
): { valid: boolean; errors: string[] } {
  const errors: string[] = []
  
  if (data.length === 0) {
    errors.push('导入数据为空')
    return { valid: false, errors }
  }
  
  data.forEach((row, index) => {
    const rowNum = index + 2 // Excel行号（从第2行开始，第1行是表头）
    
    requiredFields.forEach(field => {
      if (!row[field] || (typeof row[field] === 'string' && row[field].trim() === '')) {
        errors.push(`第${rowNum}行：${field} 不能为空`)
      }
    })
  })
  
  return {
    valid: errors.length === 0,
    errors
  }
}

