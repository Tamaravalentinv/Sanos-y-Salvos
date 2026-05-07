# Sanos y Salvos - Frontend

**Plataforma web para recuperación de mascotas perdidas**

Frontend moderno y completo construido con React 18, TypeScript, Vite y Tailwind CSS.

---

## Tabla de Contenidos

- [Requisitos](#requisitos)
- [Instalación](#instalación)
- [Ejecución](#ejecución)
- [Scripts Disponibles](#scripts-disponibles)
- [Estructura del Proyecto](#estructura-del-proyecto)
- [Componentes](#componentes)
- [Servicios & API](#servicios--api)
- [Estado Global](#estado-global-zustand)
- [Testing](#testing)
- [Build Producción](#build-para-producción)
- [Troubleshooting](#troubleshooting)
- [Stack Tecnológico](#stack-tecnológico)

---

## Características

- **Autenticación JWT** - Sistema seguro de login y registro
- **Dashboard Interactivo** - Estadísticas en tiempo real
- **Gestión de Reportes** - Crear, ver y filtrar reportes
- **Motor de Coincidencias** - Visualizar coincidencias encontradas
- **Mapa Interactivo** - Ver hotspots y zonas con Leaflet
- **Sistema de Notificaciones** - Multi-canal con preferencias
- **Sistema de Mensajes** - Comunicación entre usuarios
- **Perfil de Usuario** - Editar información y cambiar contraseña
- **Diseño Responsivo** - Optimizado para mobile
- **TypeScript Strict** - Tipado completo (noUnusedLocals activado)
- **Tailwind CSS** - Estilos modernos y personalizables
- **Hot Module Replacement** - Cambios en tiempo real

---

## Requisitos

### Software Necesario

- **Node.js** ≥ 18.0.0 ([descargar](https://nodejs.org/))
- **npm** ≥ 9.0.0 (incluido con Node.js)
- **Git** ([descargar](https://git-scm.com/))
- **Docker & Docker Compose** (para ejecutar backend)

### Verificar Instalación

```bash
# Verificar Node.js
node --version     # v18.x.x o superior

# Verificar npm
npm --version      # 9.x.x o superior

# Verificar Git
git --version
```

---

## Instalación

### 1. Clonar el Repositorio

```bash
git clone https://github.com/tu-usuario/sanos-y-salvos.git
cd sanos-y-salvos
```

### 2. Navegar a la Carpeta Frontend

```bash
cd frontend
```

### 3. Instalar Dependencias

```bash
npm install
```

Se instalarán **385 paquetes** incluyendo:
- React, React Router, React DOM
- Vite, TypeScript, ESLint, Prettier
- Tailwind CSS, PostCSS, Autoprefixer
- Axios, Zustand, Leaflet
- React Hot Toast, React Icons

### 4. Configurar Variables de Entorno

```bash
# Copiar template de variables de entorno
cp .env.example .env.local
```

**Contenido de `.env.local`:**

```env
VITE_API_URL=http://localhost:8080/api
```

---

## Ejecución

### Inicio Rápido (Desarrollo Completo)

#### Paso 1: Iniciar Backend

En la **carpeta raíz** del proyecto:

```bash
# Navegar a raíz (si estás en frontend/)
cd ..

# Iniciar Docker Compose (MySQL + 8 microservicios)
docker-compose up -d

# Verificar que todo está corriendo
docker-compose ps
```

**Esperado:** 9 servicios en estado "Up" y "healthy"

#### Paso 2: Ejecutar Frontend

En la **carpeta `frontend/`**:

```bash
npm run dev
```

**Salida esperada:**

```
  VITE v5.4.21  ready in 538 ms

  ➜  Local:   http://localhost:3001/
  ➜  Network: use --host to expose
  ➜  press h + enter to show help
```

#### Paso 3: Acceder a la Aplicación

Abrir en navegador:

```
http://localhost:3001
```

---

## Scripts Disponibles

### Desarrollo

```bash
# Iniciar servidor de desarrollo (Vite con HMR)
npm run dev
```

Características:
- Hot Module Replacement (HMR) en tiempo real
- Proxy a API Gateway (localhost:8080)
- Servidor en puerto 3001 (automático si está ocupado)

### Calidad de Código

```bash
# Ejecutar ESLint (verificar código)
npm run lint

# Verificar tipos TypeScript
npm run type-check

# Formatear código con Prettier
npm run format
```

### Build

```bash
# Compilar TypeScript y empaquetar con Vite
npm run build

# Previsualizar build de producción localmente
npm run preview
```

### Resumen de Scripts

| Script | Comando | Descripción |
|--------|---------|-------------|
| `dev` | `vite` | Servidor desarrollo con HMR |
| `build` | `tsc && vite build` | Build optimizado para producción |
| `preview` | `vite preview` | Previsualizar build de producción |
| `lint` | `eslint .` | Validar código con ESLint |
| `type-check` | `tsc --noEmit` | Verificar tipos sin generar archivos |
| `format` | `prettier --write` | Formatear código automáticamente |

---

## 📁 Estructura del Proyecto

```
frontend/
├── public/                 # Assets estáticos
│   └── .gitkeep           # (Imágenes, favicons, etc.)
├── src/
│   ├── __tests__/         # Tests y mocks
│   │   └── setup.ts
│   ├── components/        # Componentes reutilizables
│   │   ├── Badge.tsx
│   │   ├── Button.tsx
│   │   ├── Card.tsx
│   │   ├── Header.tsx
│   │   ├── Input.tsx
│   │   ├── Layout.tsx
│   │   ├── MapPicker.tsx
│   │   ├── ProtectedRoute.tsx
│   │   ├── Select.tsx
│   │   ├── Sidebar.tsx
│   │   └── Textarea.tsx
│   ├── config/            # Configuración centralizada
│   │   └── index.ts
│   ├── constants/         # Constantes globales
│   │   └── index.ts
│   ├── context/           # Zustand stores (estado global)
│   │   ├── authStore.ts
│   │   ├── mensajeStore.ts
│   │   └── notificacionStore.ts
│   ├── hooks/             # Custom React hooks
│   │   └── index.ts
│   ├── pages/             # Páginas/vistas
│   │   ├── LoginPage.tsx
│   │   ├── RegisterPage.tsx
│   │   ├── DashboardPage.tsx
│   │   ├── ReportesPage.tsx
│   │   ├── CrearReportePage.tsx
│   │   ├── ReporteDetailPage.tsx
│   │   ├── CoincidenciasPage.tsx
│   │   ├── MensajesPage.tsx
│   │   ├── NotificacionesPage.tsx
│   │   ├── GeolocalizacionPage.tsx
│   │   └── PerfilPage.tsx
│   ├── services/          # Servicios HTTP
│   │   ├── api.client.ts
│   │   ├── auth.service.ts
│   │   ├── reporte.service.ts
│   │   ├── coincidencia.service.ts
│   │   ├── notificacion.service.ts
│   │   ├── dashboard.service.ts
│   │   └── geolocalizacion.service.ts
│   ├── types/             # Tipos TypeScript globales
│   │   └── index.ts
│   ├── utils/             # Funciones helpers
│   │   └── helpers.ts
│   ├── App.tsx            # Componente raíz
│   ├── main.tsx           # Punto de entrada
│   ├── index.css          # Estilos globales
│   └── vite-env.d.ts      # Tipos de Vite
├── .eslintrc.json         # ESLint config
├── .prettierrc             # Prettier config
├── .env.example           # Template de env
├── .gitignore             # Ignorados por Git
├── ESTRUCTURA.md          # Documentación de estructura
├── index.html             # HTML principal
├── package.json           # Dependencias
├── package-lock.json      # Lock file
├── postcss.config.js      # PostCSS config
├── tailwind.config.js     # Tailwind config
├── tsconfig.json          # TypeScript config
├── tsconfig.node.json     # TS config para Vite
├── vite.config.ts         # Vite config
└── README.md              # Este archivo
```

Para detalles completos: [ESTRUCTURA.md](./ESTRUCTURA.md)

---

## Componentes

### Componentes Disponibles

Ubicados en `src/components/`:

#### UI Base
- **Button** - Botón reutilizable
- **Input** - Campo de entrada
- **Select** - Selector desplegable
- **Textarea** - Área de texto
- **Badge** - Etiqueta
- **Card** - Contenedor de tarjeta

#### Layout
- **Layout** - Layout con sidebar
- **Header** - Encabezado
- **Sidebar** - Barra de navegación

#### Features
- **ProtectedRoute** - Ruta protegida (requiere auth)
- **MapPicker** - Selector de ubicación (Leaflet)

### Ejemplos de Uso

```typescript
// Button
<Button onClick={handleClick}>Enviar</Button>

// Input
<Input type="email" placeholder="correo@email.com" />

// Card
<Card>
  <h2>Título</h2>
  <p>Contenido</p>
</Card>

// Badge
<Badge variant="success">Activo</Badge>

// MapPicker
<MapPicker center={[40.4168, -3.7038]} onChange={setLocation} />
```

---

## Servicios & API

### Servicios Disponibles

```typescript
import { authService } from '@/services/auth.service';
import { reporteService } from '@/services/reporte.service';
import { coincidenciaService } from '@/services/coincidencia.service';
import { notificacionService } from '@/services/notificacion.service';
import { dashboardService } from '@/services/dashboard.service';
import { geolocalizacionService } from '@/services/geolocalizacion.service';
import { apiClient } from '@/services/api.client';
```

### Ejemplo: Usar Servicio

```typescript
import { reporteService } from '@/services/reporte.service';

export function ReportesComponent() {
  const [reportes, setReportes] = useState([]);

  useEffect(() => {
    reporteService.getReportes()
      .then(data => setReportes(data))
      .catch(error => console.error('Error:', error));
  }, []);

  return (
    <div>
      {reportes.map(reporte => (
        <div key={reporte.id}>{reporte.titulo}</div>
      ))}
    </div>
  );
}
```

### Endpoints de API

```
POST   /api/auth/login              - Login
POST   /api/auth/register           - Registro
GET    /api/user/profile            - Perfil
POST   /api/reportes                - Crear reporte
GET    /api/reportes                - Listar reportes
GET    /api/reportes/:id            - Detalle reporte
GET    /api/coincidencias           - Coincidencias
GET    /api/notificaciones          - Notificaciones
GET    /api/mensajes                - Mensajes
GET    /api/ubicacion               - Ubicación
```

---

## Estado Global (Zustand)

### Stores Disponibles

```typescript
import { useAuthStore } from '@/context/authStore';
import { useMensajeStore } from '@/context/mensajeStore';
import { useNotificacionStore } from '@/context/notificacionStore';
```

### Ejemplo: Auth Store

```typescript
import { useAuthStore } from '@/context/authStore';

export function ProfileComponent() {
  const user = useAuthStore((state) => state.user);
  const logout = useAuthStore((state) => state.logout);

  return (
    <div>
      <p>Bienvenido, {user?.nombre}</p>
      <button onClick={logout}>Cerrar sesión</button>
    </div>
  );
}
```

---

## 🧪 Testing

### Ejecutar Validaciones

```bash
# ESLint - Validar código
npm run lint

# TypeScript - Verificar tipos
npm run type-check

# Prettier - Formatear código
npm run format
```

### Configuración

- **ESLint:** `.eslintrc.json` - Reglas TypeScript/React
- **Prettier:** `.prettierrc` - Formateo automático
- **TypeScript:** `tsconfig.json` - Configuración TS

---

## Build para Producción

### Crear Build Optimizado

```bash
npm run build
```

**Proceso:**
1. TypeScript se compila a JavaScript
2. Vite empaqueta y minifica
3. Genera sourcemaps (opcional)
4. Optimiza assets

**Resultado:**
- Carpeta `dist/` lista para desplegar

### Previsualizar Build

```bash
npm run preview
```

Sirve localmente el build de producción para pruebas.

### Tamaño del Build

```bash
# Verificar tamaño
ls -lh dist/

# Típico: 150-250 KB (gzipped)
```

---

## Variables de Entorno

### Archivo `.env.local`

```env
# URL del API Backend
VITE_API_URL=http://localhost:8080/api

# Nota: Variables se acceden con import.meta.env.VITE_*
```

### Uso en Código

```typescript
// Acceso directo
const apiUrl = import.meta.env.VITE_API_URL;

// Desde config
import { API_CONFIG } from '@/config';
const baseUrl = API_CONFIG.baseURL;
```

---

## Troubleshooting

### Error: Puerto 3000 en uso

```bash
# Vite automáticamente usa 3001, 3002, etc.
# O matar proceso manualmente:

# Windows (PowerShell)
Get-Process | Where-Object {$_.Port -eq 3000} | Stop-Process -Force

# Mac/Linux
lsof -ti:3000 | xargs kill -9
```

### Error: No se conecta al backend

```bash
# Verificar que Docker está corriendo
docker ps

# Si no, iniciar:
docker-compose up -d

# Verificar logs
docker-compose logs api-gateway
```

### Error: Módulos no encontrados

```bash
# Reinstalar dependencias
rm -r node_modules package-lock.json
npm install

# Reiniciar servidor
npm run dev
```

### Error: TypeScript errors

```bash
# Verificar tipos
npm run type-check

# Revisar tsconfig.json está correcto
```

### Error: Estilos Tailwind no aplican

```bash
# Verificar que tailwind.config.js es correcto
# Verificar que index.css tiene:
#   @tailwind base;
#   @tailwind components;
#   @tailwind utilities;

# Reiniciar servidor
npm run dev
```

---

## Stack Tecnológico

### Frontend Framework

| Tecnología | Versión | Propósito |
|------------|---------|----------|
| React | 18.2.0 | Framework UI |
| TypeScript | 5.3.2 | Type safety |
| React Router | 6.20.0 | Routing SPA |
| Vite | 5.0.7 | Bundler rápido |

### UI & Styling

| Tecnología | Versión | Propósito |
|------------|---------|----------|
| Tailwind CSS | 3.3.6 | Utility-first styling |
| PostCSS | 8.4.32 | CSS processing |
| Autoprefixer | 10.4.16 | Vendor prefixes |
| React Icons | 4.12.0 | Iconografía |

### Estado & HTTP

| Tecnología | Versión | Propósito |
|------------|---------|----------|
| Zustand | 4.4.0 | State management |
| Axios | 1.6.2 | HTTP client |

### Mapas

| Tecnología | Versión | Propósito |
|------------|---------|----------|
| Leaflet | 1.9.4 | Mapas interactivos |
| React Leaflet | 4.2.1 | Integración React |

### Notificaciones

| Tecnología | Versión | Propósito |
|------------|---------|----------|
| React Hot Toast | 2.4.1 | Toast notifications |

### Herramientas de Desarrollo

| Herramienta | Versión | Propósito |
|-------------|---------|----------|
| ESLint | 8.55.0 | Code linting |
| Prettier | 3.1.1 | Code formatting |
| @typescript-eslint | 6.13.2 | TS/React rules |

---

## Guía de Desarrollo

### Path Aliases (Imports Limpios)

```typescript
// Recomendado
import { Button } from '@/components';
import { authService } from '@/services/auth.service';
import { ROUTES } from '@/constants';

// Evitar
import { Button } from '../../../../components';
import { authService } from '../../services/auth.service';
```

### Estructura de Carpetas

```typescript
// Componentes
src/components/Button.tsx

// Páginas
src/pages/DashboardPage.tsx

// Servicios
src/services/auth.service.ts

// Estado
src/context/authStore.ts

// Tipos
src/types/index.ts
```

---

## Deployment

### Desplegar en Netlify

```bash
# Build
npm run build

# Netlify automáticamente detecta dist/
# Configura redirect en netlify.toml para SPA
```

### Desplegar en Vercel

```bash
# Vercel automáticamente ejecuta build
# Configura rewrite de rutas SPA
```

---

## Recursos

- [Vite Docs](https://vitejs.dev/)
- [React Docs](https://react.dev/)
- [TypeScript Docs](https://www.typescriptlang.org/)
- [Tailwind CSS](https://tailwindcss.com/)
- [React Router](https://reactrouter.com/)
- [Zustand](https://github.com/pmndrs/zustand)
- [Leaflet](https://leafletjs.com/)

---

## Contribuciones

```bash
# 1. Crea rama
git checkout -b feature/tu-feature

# 2. Commit con mensaje convencional
git commit -m "feat: agregar nueva funcionalidad"

# 3. Push
git push origin feature/tu-feature

# 4. Abre Pull Request en GitHub
```

---

## Checklist Rápido

- [ ] Node.js ≥ 18 instalado
- [ ] `npm install` ejecutado
- [ ] `.env.local` configurado
- [ ] Backend corriendo (`docker-compose up -d`)
- [ ] `npm run dev` ejecutado
- [ ] Aplicación accesible en `http://localhost:3001`
- [ ] Código formateado (`npm run format`)
- [ ] Sin errores de linting (`npm run lint`)

---

**Última actualización:** 2026-05-07  
**Versión:** 1.0.0  
**Estado:** En desarrollo
```

El frontend estará disponible en `http://localhost:3000`

## 📁 Estructura del Proyecto

```
frontend/
├── src/
│   ├── components/          # Componentes reutilizables
│   │   ├── Button.tsx
│   │   ├── Card.tsx
│   │   ├── Input.tsx
│   │   ├── Layout.tsx
│   │   ├── Header.tsx
│   │   └── Sidebar.tsx
│   ├── pages/              # Páginas de la aplicación
│   │   ├── LoginPage.tsx
│   │   ├── DashboardPage.tsx
│   │   ├── ReportesPage.tsx
│   │   ├── CrearReportePage.tsx
│   │   ├── CoincidenciasPage.tsx
│   │   ├── GeolocalizacionPage.tsx
│   │   ├── NotificacionesPage.tsx
│   │   └── PerfilPage.tsx
│   ├── services/           # Servicios API
│   │   ├── api.client.ts
│   │   ├── auth.service.ts
│   │   ├── reporte.service.ts
│   │   ├── coincidencia.service.ts
│   │   ├── geolocalizacion.service.ts
│   │   ├── notificacion.service.ts
│   │   └── dashboard.service.ts
│   ├── context/            # Zustand stores
│   │   ├── authStore.ts
│   │   └── notificacionStore.ts
│   ├── types/              # Definiciones TypeScript
│   │   └── index.ts
│   ├── utils/              # Funciones utilitarias
│   │   └── helpers.ts
│   ├── App.tsx             # Componente raíz
│   └── main.tsx            # Punto de entrada
├── public/                 # Archivos estáticos
├── index.html              # HTML base
├── package.json
├── tsconfig.json
├── vite.config.ts
└── tailwind.config.js
```

## 🎨 Componentes Disponibles

### Layout
- `Header`: Barra superior con navegación
- `Sidebar`: Menú lateral de navegación
- `Layout`: Estructura base con header y sidebar
- `ProtectedRoute`: Rutas protegidas por autenticación

### UI Components
- `Button`: Botón versátil con variantes
- `Card`: Contenedor de contenido
- `Input`: Campo de entrada
- `Select`: Selector dropdown
- `Textarea`: Área de texto
- `Badge`: Etiqueta de estado

## 📄 Páginas Implementadas

### 1. **LoginPage** (`/login`)
- Formulario de autenticación
- Validación de credenciales
- Redirección al dashboard

### 2. **RegisterPage** (`/register`)
- Registro de nuevos usuarios
- Soporte para múltiples tipos de usuario
- Validación de formulario

### 3. **DashboardPage** (`/dashboard`)
- Estadísticas globales
- Reportes recientes
- Acciones rápidas
- Métricas de éxito

### 4. **ReportesPage** (`/reportes`)
- Listado de reportes
- Filtros por tipo, estado y ciudad
- Paginación
- Vista de detalles

### 5. **CrearReportePage** (`/reportes/crear`)
- Formulario completo de reporte
- Información de mascota
- Datos de ubicación
- Validación en cliente

### 6. **CoincidenciasPage** (`/coincidencias`)
- Listado de coincidencias
- Scoring de similitud
- Factores de coincidencia
- Confirmación/Rechazo

### 7. **GeolocalizacionPage** (`/mapa`)
- Visualización de hotspots
- Zonas de riesgo
- Filtro por ciudad
- Datos de incidencia

### 8. **NotificacionesPage** (`/notificaciones`)
- Listado de notificaciones
- Filtro de leídas/no leídas
- Marcar como leído
- Eliminar notificaciones

### 9. **PerfilPage** (`/perfil`)
- Editar información personal
- Cambiar contraseña
- Ver datos de cuenta

## 🔌 Integración de APIs

### AuthService
```typescript
authService.login(email, password)
authService.register(userData)
authService.getCurrentUser()
authService.updateProfile(userId, data)
authService.changePassword(oldPassword, newPassword)
```

### ReporteService
```typescript
reporteService.getAllReportes(filtros)
reporteService.getReporteById(id)
reporteService.createReporte(data)
reporteService.updateReporte(id, data)
reporteService.resolverReporte(id, detalles)
reporteService.buscarReportesCercanos(lat, lon, radius)
```

### CoincidenciaService
```typescript
coincidenciaService.getCoincidendasRecientes(limit)
coincidenciaService.confirmarCoincidencia(id)
coincidenciaService.rechazarCoincidencia(id)
coincidenciaService.getEstadisticas()
```

### GeolocalizacionService
```typescript
geolocalizacionService.getHotspotsIncidencia(ciudad)
geolocalizacionService.getZonasIncidencia(ciudad)
geolocalizacionService.getCiudadesConReportes()
```

### NotificacionService
```typescript
notificacionService.getNotificaciones(leidas, page, size)
notificacionService.marcarComoLeida(id)
notificacionService.marcarTodasComoLeidas()
notificacionService.getPreferencias()
notificacionService.updatePreferencias(data)
```

## 🎯 Gestión de Estado (Zustand)

### AuthStore
- `user`: Información del usuario actual
- `token`: Token JWT
- `isAuthenticated`: Estado de autenticación
- `login()`: Autenticación
- `logout()`: Cerrar sesión

### NotificacionStore
- `notificaciones`: Lista de notificaciones
- `noLeidasCount`: Contador de no leídas
- `loadNotificaciones()`: Cargar notificaciones
- `marcarComoLeida()`: Marcar como leída
- `deleteNotificacion()`: Eliminar notificación

## 🔐 Autenticación y Seguridad

- Token JWT almacenado en localStorage
- Interceptor automático de requests
- Redireccionamiento a login en 401
- Rutas protegidas con ProtectedRoute
- Validación de formularios en cliente

## 🎨 Temas y Estilos

### Colores Principales
- Primario: `#0ea5e9` (Azul cielo)
- Éxito: `#10b981` (Verde)
- Peligro: `#ef4444` (Rojo)
- Advertencia: `#f59e0b` (Naranja)

### Responsive Design
- Mobile-first approach
- Breakpoints: sm (640px), md (768px), lg (1024px)
- Sidebar colapsable en móvil
- Grid automático en componentes

## 🧪 Testing (Estructura preparada)

```bash
# Ejecutar tests
npm run test

# Coverage
npm run test:coverage
```

## 📦 Build y Deployment

```bash
# Build para producción
npm run build

# Preview del build
npm run preview
```

El build genera archivos optimizados en la carpeta `dist/`.

## 🔗 Configuración de Variables de Entorno

```env
# .env
REACT_APP_API_URL=http://localhost:8080/api
```

Para desarrollo local, usa la configuración incluida en `vite.config.ts` que redirige `/api` a `http://localhost:8080`.

## 📝 Notas de Desarrollo

### Agregar Nueva Página
1. Crear archivo en `src/pages/NombrePage.tsx`
2. Importar en `App.tsx`
3. Agregar ruta en el router
4. Agregar enlace en Sidebar si es necesario

### Agregar Nuevo Servicio
1. Crear archivo en `src/services/nombre.service.ts`
2. Exportar funciones async
3. Usar `api` del cliente HTTP
4. Importar tipos desde `@/types`

### Agregar Nuevo Componente
1. Crear archivo en `src/components/NombreComponent.tsx`
2. Exportar como default
3. Usar Tailwind CSS para estilos
4. Aceptar props tipadas en TypeScript

## 🚀 Próximos Pasos

- [ ] Integración de Google Maps/Leaflet para mapa interactivo
- [ ] Upload de imágenes para reportes
- [ ] Soporte para múltiples idiomas (i18n)
- [ ] PWA - Instalable como aplicación
- [ ] Notificaciones push en tiempo real
- [ ] Búsqueda avanzada con filtros más complejos
- [ ] Exportar reportes a PDF
- [ ] Chat entre usuarios
- [ ] Califícaciones y reviews de servicios

## 📞 Soporte

Para reportar bugs o sugerencias, contacta al equipo de desarrollo.

## 📄 Licencia

Este proyecto es parte de Sanos y Salvos - Plataforma de Recuperación de Mascotas.
