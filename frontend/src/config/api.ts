import axios, { AxiosInstance, AxiosRequestConfig, AxiosResponse } from 'axios'
import { message } from 'antd'
import { getToken, removeToken } from '@/utils/auth'

const BASE_URL = import.meta.env.VITE_API_BASE_URL || '/api'

// 创建axios实例
const service: AxiosInstance = axios.create({
  baseURL: BASE_URL,
  timeout: 30000,
  headers: {
    'Content-Type': 'application/json',
  },
})

// 请求拦截器
service.interceptors.request.use(
  (config: AxiosRequestConfig) => {
    const token = getToken()
    if (token && config.headers) {
      config.headers.Authorization = `Bearer ${token}`
    }
    return config
  },
  (error) => {
    return Promise.reject(error)
  }
)

// 响应拦截器
service.interceptors.response.use(
  (response: AxiosResponse) => {
    const res = response.data
    
    // 检查响应数据是否存在
    if (!res) {
      console.error('响应数据为空:', response)
      message.error('响应数据为空')
      return Promise.reject(new Error('响应数据为空'))
    }
    
    // 添加调试日志
    console.log('响应拦截器 - 响应数据:', {
      code: res.code,
      msg: res.msg,
      hasData: !!res.data
    })
    
    // 如果返回的状态码不是200，则视为错误
    if (res.code !== 200) {
      const errorMsg = res.msg || res.message || '请求失败'
      console.warn('业务错误:', { code: res.code, msg: errorMsg })
      
      // 401: 未登录或token过期（登录失败也返回401）
      if (res.code === 401) {
        // 登录页面不自动清除token和重定向
        if (window.location.pathname === '/login') {
          // 登录失败，只显示错误消息，不重定向
          message.error(errorMsg)
        } else {
          // 其他页面的401错误，清除token并重定向
          removeToken()
          message.error(errorMsg)
          window.location.href = '/login'
        }
      } else {
        // 其他业务错误
        message.error(errorMsg)
      }
      
      // 创建错误对象，包含完整的响应信息
      const error = new Error(errorMsg) as any
      error.code = res.code
      error.response = response
      return Promise.reject(error)
    }
    
    // 成功响应
    console.log('响应拦截器 - 成功响应:', res)
    return res
  },
  (error) => {
    // 处理HTTP错误状态码
    if (error.response) {
      const status = error.response.status
      const res = error.response.data
      
      // 403: 禁止访问（权限不足）
      if (status === 403) {
        const errorMsg = res?.msg || res?.message || '权限不足，访问被拒绝'
        message.error(errorMsg)
        // 如果是权限问题，可以选择跳转到登录页或显示权限不足提示
        // removeToken()
        // window.location.href = '/login'
      } 
      // 401: 未授权（未登录或token过期）
      else if (status === 401) {
        const errorMsg = res?.msg || res?.message || '未登录或登录已过期'
        message.error(errorMsg)
        removeToken()
        window.location.href = '/login'
      }
      // 其他HTTP错误
      else {
        const errorMsg = res?.msg || res?.message || error.message || `请求失败 (${status})`
        message.error(errorMsg)
      }
    } else {
      // 网络错误或其他错误
      message.error(error.message || '网络错误，请检查网络连接')
    }
    
    return Promise.reject(error)
  }
)

export default service











