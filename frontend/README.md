# Sanos y Salvos - Frontend

Frontend moderno y completo para la plataforma de recuperación de mascotas Sanos y Salvos.

## 🚀 Características

- ✅ **Autenticación JWT**: Sistema seguro de login y registro
- ✅ **Dashboard Interactivo**: Estadísticas en tiempo real
- ✅ **Gestión de Reportes**: Crear, ver y filtrar reportes
- ✅ **Motor de Coincidencias**: Visualizar y confirmar coincidencias
- ✅ **Mapa de Incidencias**: Ver hotspots y zonas de riesgo
- ✅ **Sistema de Notificaciones**: Multi-canal con preferencias
- ✅ **Perfil de Usuario**: Editar información y cambiar contraseña
- ✅ **Diseño Responsivo**: Totalmente optimizado para móvil
- ✅ **TypeScript**: Tipado completo para mayor seguridad
- ✅ **Tailwind CSS**: Estilos modernos y personalizables

## 🛠️ Stack Tecnológico

- **React 18.2.0**: Librería de interfaz
- **TypeScript**: Lenguaje tipado
- **React Router 6**: Enrutamiento SPA
- **Axios**: Cliente HTTP
- **Zustand**: Gestión de estado
- **Tailwind CSS**: Framework de estilos
- **React Icons**: Iconografía
- **React Hot Toast**: Notificaciones
- **Vite**: Build tool ultrarrápido

## 📦 Instalación

### Requisitos Previos
- Node.js 16+
- npm o yarn
- Backend ejecutándose en `http://localhost:8080`

### Pasos

```bash
# 1. Navegar al directorio frontend
cd frontend

# 2. Instalar dependencias
npm install

# 3. Crear archivo .env
cp .env.example .env

# 4. Iniciar servidor de desarrollo
npm run dev
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
