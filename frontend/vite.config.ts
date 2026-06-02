import { defineConfig } from 'vite'
import react from '@vitejs/plugin-react'
import path from 'path'

export default defineConfig({
  plugins: [react()],
  publicDir: 'public',
  resolve: {
    alias: {
      '@': path.resolve(__dirname, './src'),
    },
  },
  server: {
    port: 3000,
    proxy: {
      '/api': {
        target: 'http://localhost:8080',
        changeOrigin: true,
      },
    },
  },
  build: {
    outDir: 'dist',
    sourcemap: false,
    minify: 'terser',
  },
  test: {
    environment: 'jsdom',
    globals: true,
    setupFiles: './src/__tests__/setup.ts',
    coverage: {
      provider: 'v8',
      reporter: ['text', 'json-summary'],
      thresholds: {
        statements: 60,
        branches: 50,
        functions: 60,
        lines: 60,
      },
      include: [
        'src/services/auth.service.ts',
        'src/services/coincidencia.service.ts',
        'src/services/dashboard.service.ts',
        'src/services/notificacion.service.ts',
        'src/context/authStore.ts',
        'src/context/mensajeStore.ts',
        'src/context/notificacionStore.ts',
        'src/components/Button.tsx',
        'src/components/Input.tsx',
        'src/components/ProtectedRoute.tsx',
      ],
      exclude: [
        'src/main.tsx',
        'src/vite-env.d.ts',
        'src/**/*.test.{ts,tsx}',
        'src/**/__tests__/**',
      ],
    },
  },
})
