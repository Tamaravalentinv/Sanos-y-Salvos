# Estructura del Proyecto Frontend - Sanos y Salvos

## Estructura General

```
frontend/
├── public/                    # Assets estáticos (imágenes, favicons, etc.)
├── src/                      # Código fuente principal
│   ├── __tests__/            # Tests y configuración de testing
│   ├── components/           # Componentes reutilizables
│   ├── config/               # Configuración de la aplicación
│   ├── constants/            # Constantes globales
│   ├── context/              # Zustand stores (estado global)
│   ├── hooks/                # Custom React hooks
│   ├── pages/                # Componentes de página (vistas)
│   ├── services/             # Servicios HTTP y lógica de negocio
│   ├── types/                # Tipos TypeScript globales
│   ├── utils/                # Funciones utilitarias
│   ├── App.tsx               # Componente raíz de la aplicación
│   ├── main.tsx              # Punto de entrada
│   ├── index.css             # Estilos globales
│   └── vite-env.d.ts         # Declaraciones de tipos Vite
├── .eslintrc.json            # Configuración de ESLint
├── .prettierrc                # Configuración de Prettier
├── .gitignore                # Archivos ignorados por Git
├── .env.example              # Template de variables de entorno
├── Dockerfile                # Imagen Docker del frontend
├── index.html                # HTML principal
├── package.json              # Dependencias y scripts
├── package-lock.json         # Lock file de dependencias
├── postcss.config.js         # Configuración de PostCSS
├── tailwind.config.js        # Configuración de Tailwind CSS
├── tsconfig.json             # Configuración de TypeScript
├── tsconfig.node.json        # Configuración TypeScript para Vite
└── vite.config.ts            # Configuración de Vite
```

---

## Descripción de Carpetas

### **public/**
- **Propósito:** Assets estáticos que se sirven directamente
- **Contenido:** Imágenes, favicons, PDFs, fuentes, etc.
- **Acceso:** `/filename` en la aplicación
- **Ejemplo:** 
  ```
  public/
  ├── vite.svg
  ├── favicon.ico
  ├── images/
  │   ├── logo.png
  │   └── hero.jpg
  └── fonts/
  ```

### **src/**
Carpeta raíz del código fuente.

#### **src/__tests__/**
- **Propósito:** Pruebas unitarias, integración y E2E
- **Contenido:** Test cases, mocks, setup de testing
- **Convención:** Archivos con extensión `.test.ts`, `.test.tsx`, `.spec.ts`
- **Estructura:** Espejo la estructura de `src/` para fácil localización
- **Ejemplo:**
  ```
  __tests__/
  ├── setup.ts
  ├── components/
  │   └── Button.test.tsx
  └── services/
      └── auth.service.test.ts
  ```

#### **src/components/**
- **Propósito:** Componentes reutilizables de UI
- **Contenido:** Componentes sin lógica de página específica
- **Características:** Altamente reutilizables, bien documentados, aislados
- **Ejemplos:** Badge, Button, Card, Header, Input, Layout, etc.
- **Estructura:**
  ```
  components/
  ├── Badge.tsx
  ├── Button.tsx
  ├── Button.module.css (opcional)
  └── index.ts (barrel export)
  ```

#### **src/config/**
- **Propósito:** Configuración centralizada de la aplicación
- **Contenido:** 
  - `APP_CONFIG`: Información de la app
  - `API_CONFIG`: URLs y timeouts
  - `AUTH_CONFIG`: Claves de almacenamiento
  - `MAP_CONFIG`: Configuración de mapas
- **Ventaja:** Fácil cambiar config por entorno
- **Ejemplo:**
  ```ts
  export const API_CONFIG = {
    baseURL: 'http://localhost:8080/api',
    timeout: 30000,
  };
  ```

#### **src/constants/**
- **Propósito:** Constantes de la aplicación
- **Contenido:** 
  - Rutas de la aplicación
  - Estados y tipos enumerables
  - Mensajes comunes
  - Valores timeout
- **Ventaja:** Cambios centralizados
- **Ejemplo:**
  ```ts
  export const ROUTES = {
    LOGIN: '/login',
    DASHBOARD: '/dashboard',
  };
  ```

#### **src/context/**
- **Propósito:** Gestión del estado global con Zustand
- **Contenido:** Stores para autenticación, mensajes, notificaciones
- **Actual:** 
  - `authStore.ts`: Estado de autenticación
  - `mensajeStore.ts`: Estado de mensajes
  - `notificacionStore.ts`: Estado de notificaciones
- **Ventaja:** Estado compartido sin prop drilling
- **Ejemplo:**
  ```ts
  export const useAuthStore = create((set) => ({
    user: null,
    setUser: (user) => set({ user }),
  }));
  ```

#### **src/hooks/**
- **Propósito:** Custom React hooks reutilizables
- **Contenido:** Hooks personalizados para lógica común
- **Futuros ejemplos:**
  - `useFetch()`: Request HTTP con loading/error
  - `useLocalStorage()`: Persistencia en localStorage
  - `useAuth()`: Lógica de autenticación
  - `useForm()`: Manejo de formularios
- **Estructura:**
  ```
  hooks/
  ├── index.ts
  ├── useFetch.ts
  ├── useAuth.ts
  └── useForm.ts
  ```

#### **src/pages/**
- **Propósito:** Componentes de página (vistas/rutas)
- **Contenido:** Componentes que ocupan la pantalla completa
- **Actuales:**
  - LoginPage, RegisterPage (autenticación)
  - DashboardPage (inicio)
  - ReportesPage, CrearReportePage, ReporteDetailPage (reportes)
  - CoincidenciasPage (coincidencias)
  - MensajesPage, NotificacionesPage (comunicación)
  - GeolocalizacionPage (ubicación)
  - PerfilPage (perfil de usuario)
- **Convención:** Nombres con sufijo "Page"
- **Estructura:**
  ```
  pages/
  ├── LoginPage.tsx
  ├── DashboardPage.tsx
  └── ReportesPage.tsx
  ```

#### **src/services/**
- **Propósito:** Servicios HTTP y lógica de negocio
- **Contenido:** Clientes API para cada entidad
- **Actuales:**
  - `api.client.ts`: Cliente HTTP base (Axios)
  - `auth.service.ts`: Autenticación
  - `reporte.service.ts`: Gestión de reportes
  - `coincidencia.service.ts`: Coincidencias
  - `notificacion.service.ts`: Notificaciones
  - `dashboard.service.ts`: Dashboard
  - `geolocalizacion.service.ts`: Ubicación
- **Estructura:**
  ```
  services/
  ├── api.client.ts
  ├── auth.service.ts
  └── reporte.service.ts
  ```
- **Patrón:**
  ```ts
  export class AuthService {
    static login(email, password) { }
    static logout() { }
    static getProfile() { }
  }
  ```

#### **src/types/**
- **Propósito:** Definiciones de tipos TypeScript globales
- **Contenido:** Interfaces y tipos compartidos
- **Actual:** `index.ts` centraliza los tipos
- **Estructura:**
  ```ts
  export interface User {
    id: string;
    email: string;
    nombre: string;
  }
  
  export interface Reporte {
    id: string;
    titulo: string;
    descripcion: string;
  }
  ```

#### **src/utils/**
- **Propósito:** Funciones utilitarias y helpers
- **Contenido:** Funciones puras reutilizables
- **Actual:** `helpers.ts` con funciones comunes
- **Ejemplos:**
  - Formateo de fechas
  - Parseo de datos
  - Validaciones
  - Transformaciones
- **Estructura:**
  ```
  utils/
  ├── helpers.ts
  ├── formatters.ts
  ├── validators.ts
  └── transforms.ts
  ```

### **Archivos Raíz de src/**

#### **src/App.tsx**
- Componente raíz de la aplicación
- Configuración de rutas principales
- Proveedores de contexto

#### **src/main.tsx**
- Punto de entrada de la aplicación
- Renderiza el componente `App` en el DOM
- Setup global

#### **src/index.css**
- Estilos globales de la aplicación
- Resets y estilos base
- Importa Tailwind CSS

#### **src/vite-env.d.ts**
- Declaraciones de tipos para Vite
- Importaciones especiales

---

## 🔗 Path Aliases

Configurados en `tsconfig.json` para evitar imports relativos profundos:

```ts
import { Button } from '@/components';           // ✅ Bueno
import { Button } from '../../../../components'; // ❌ Evitar

import { ROUTES } from '@/constants';
import { useAuthStore } from '@/context/authStore';
import { authService } from '@/services/auth.service';
import type { User } from '@/types';
import { formatDate } from '@/utils/helpers';
```

---

## Guía de Dónde Poner Código

| Tipo de Código | Ubicación | Ejemplo |
|---|---|---|
| Componente reutilizable | `src/components/` | `Button.tsx` |
| Página/Pantalla completa | `src/pages/` | `DashboardPage.tsx` |
| Llamadas a API | `src/services/` | `auth.service.ts` |
| Estado global | `src/context/` | `authStore.ts` |
| Custom hook | `src/hooks/` | `useFetch.ts` |
| Tipos TypeScript | `src/types/` | `index.ts` |
| Constantes | `src/constants/` | `index.ts` |
| Funciones utilitarias | `src/utils/` | `helpers.ts` |
| Configuración app | `src/config/` | `index.ts` |
| Tests | `src/__tests__/` | `Button.test.tsx` |
| Assets estáticos | `public/` | `logo.png` |

---

## Checklist de Estructura

- [x] `src/` - Código fuente organizado
- [x] `public/` - Assets estáticos
- [x] `src/components/` - Componentes reutilizables (11)
- [x] `src/pages/` - Páginas/vistas (11)
- [x] `src/services/` - Servicios HTTP (7)
- [x] `src/context/` - State management (3 stores)
- [x] `src/types/` - Tipos TypeScript
- [x] `src/utils/` - Funciones helpers
- [x] `src/hooks/` - Custom React hooks
- [x] `src/constants/` - Constantes globales
- [x] `src/config/` - Configuración app
- [x] `src/__tests__/` - Tests y setup
- [x] Path aliases en `tsconfig.json`
- [x] `vite.config.ts` - Configurado con publicDir

---

## Comandos Útiles

```bash
# Desarrollo
npm run dev

# Build para producción
npm run build

# Verificar tipos
npm run type-check

# Linting
npm run lint

# Formatear código
npm run format

# Preview del build
npm run preview
```

---

## Notas

- La carpeta `node_modules/` está ignorada en Git (`.gitignore`)
- La carpeta `dist/` es generada por `npm run build`
- Variables de entorno: copiar `.env.example` a `.env.local`
- Tailwind CSS está configurado con importación en `index.css`

---

*Estructura creada: 2026-05-07*
*Versión: 1.0*
