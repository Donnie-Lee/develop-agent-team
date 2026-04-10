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
    port: 3000,
    proxy: {
      '/api/v1/user': {
        target: 'http://localhost:8081',
        changeOrigin: true
      },
      '/api/v1/admin': {
        target: 'http://localhost:8081',
        changeOrigin: true
      },
      '/api/v1/resumes': {
        target: 'http://localhost:8082',
        changeOrigin: true
      },
      '/api/v1/questions': {
        target: 'http://localhost:8084',
        changeOrigin: true
      }
    }
  }
})
