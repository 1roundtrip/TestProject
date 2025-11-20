import { defineConfig } from 'vite'
import react from '@vitejs/plugin-react'
import path from 'path'

// https://vitejs.dev/config/
export default defineConfig(({ mode }) => {
  // 根据环境变量获取API地址
  const apiBaseUrl = process.env.VITE_API_BASE_URL || '/api'
  const apiTarget = process.env.VITE_API_TARGET || 'http://localhost:8080'
  
  return {
    plugins: [react()],
    resolve: {
      alias: {
        '@': path.resolve(__dirname, './src'),
      },
    },
    server: {
      host: '0.0.0.0', // 允许局域网访问
      port: 3000,
      proxy: {
        '/api': {
          target: apiTarget,
          changeOrigin: true,
          // 不重写路径，保持 /api 前缀，因为后端路径包含 /api
          // rewrite: (path) => path.replace(/^\/api/, ''),
        },
      },
    },
    build: {
      outDir: 'dist',
      assetsDir: 'assets',
      sourcemap: false,
      // 资源优化 - 使用 esbuild (Vite 5 默认)
      minify: 'esbuild',
      // 代码分割
      rollupOptions: {
        output: {
          manualChunks: {
            'react-vendor': ['react', 'react-dom', 'react-router-dom'],
            'antd-vendor': ['antd', '@ant-design/icons'],
            'echarts-vendor': ['echarts', 'echarts-for-react'],
          },
        },
      },
      // 块大小警告限制
      chunkSizeWarningLimit: 1000,
    },
    // 预览配置
    preview: {
      host: '0.0.0.0',
      port: 3000,
    },
  }
})
