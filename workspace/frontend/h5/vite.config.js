import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'
import path from 'path'

export default defineConfig({
  plugins: [vue()],
  resolve: {
    alias: {
      '@': path.resolve(__dirname, 'src')
    }
  },
  server: {
    port: 3001,
    proxy: {
      '/api/v1/user': {
        target: 'http://localhost:8081',
        changeOrigin: true
      },
      '/api/v1/resumes': {
        target: 'http://localhost:8082',
        changeOrigin: true
      },
      '/api/v1/interview': {
        target: 'http://localhost:8083',
        changeOrigin: true
      },
      '/api/v1/questions': {
        target: 'http://localhost:8084',
        changeOrigin: true
      },
      '/api/v1/ai': {
        target: 'http://localhost:8087',
        changeOrigin: true
      },
      '/api/v1/member': {
        target: 'http://localhost:8085',
        changeOrigin: true
      }
    }
  }
})
