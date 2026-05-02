📁 PROJECT STRUCTURE

```
sanos-y-salvos/
├── 📄 pom.xml                      # Maven parent POM (multi-module)
├── 📄 docker-compose.yml           # Orquestación de servicios
├── 📄 .env                         # Variables de entorno (NO subir a Git)
├── 📄 .env.example                 # Plantilla de variables de entorno
├── 📄 README.md                    # Documentación principal
├── 📄 CONTRIBUTING.md              # Guía de contribución
│
├── 📂 api-gateway/                 # API Gateway (Spring Boot + Resilience4j)
│   ├── pom.xml
│   ├── Dockerfile
│   └── src/
│       ├── main/java/...
│       └── main/resources/
│           ├── application.properties
│           └── application-docker.properties
│
├── 📂 frontend/                    # React + TypeScript + Vite
│   ├── package.json
│   ├── tsconfig.json
│   ├── vite.config.ts
│   ├── tailwind.config.js
│   ├── Dockerfile
│   └── src/
│       ├── components/             # 11 componentes reutilizables
│       ├── pages/                  # 9 páginas principales
│       ├── services/               # 8 servicios HTTP
│       ├── context/                # 2 stores Zustand
│       ├── types/                  # Interfaces TypeScript
│       └── utils/                  # Funciones auxiliares
│
├── 📂 ms-usuarios/                 # Microservicio: Gestión de Usuarios
│   ├── pom.xml
│   ├── Dockerfile
│   └── src/main/java/.../
│
├── 📂 ms-reportes/                 # Microservicio: Reportes de Mascotas
│   ├── pom.xml
│   ├── Dockerfile
│   └── src/main/java/.../
│
├── 📂 ms-geolocalizacion/          # Microservicio: Geolocalización
│   ├── pom.xml
│   ├── Dockerfile
│   └── src/main/java/.../
│
├── 📂 ms-coincidencias/            # Microservicio: Motor de Matching
│   ├── pom.xml
│   ├── Dockerfile
│   └── src/main/java/.../
│
├── 📂 ms-notificaciones/           # Microservicio: Sistema de Notificaciones
│   ├── pom.xml
│   ├── Dockerfile
│   └── src/main/java/.../
│
├── 📂 ms-geolocalizacion/          # Microservicio: Geolocalizacion
│   ├── pom.xml
│   ├── Dockerfile
│   └── src/main/java/.../
│
├── 📂 database/                    # Schemas y scripts SQL
│   ├── completo_script.sql         # Script completo de BD (3 bases)
│   └── ...
│
├── 📂 scripts/                     # Utilidades y automatización
│   ├── build.sh
│   ├── up.sh
│   ├── down.sh
│   ├── logs.sh
│   └── health-check.sh
│
└── 📂 .git/                        # Control de versión

═══════════════════════════════════════════════════════════

📊 ARQUITECTURA DE MICROSERVICIOS

┌─────────────────────────────────────────────────────────────┐
│                        FRONTEND (React)                      │
│                    :3000 (localhost:3000)                    │
└──────────────────────────┬──────────────────────────────────┘
                           │
                           ↓ HTTPS
┌─────────────────────────────────────────────────────────────┐
│                      API GATEWAY                              │
│              (Spring Boot + Resilience4j)                     │
│                    :8080 (localhost:8080)                     │
│  - Autenticación JWT                                          │
│  - Circuit Breaker (Resilience4j)                             │
│  - Request Logging & Validation                               │
└─────────┬──────────────┬──────────────┬──────────────┬───────┘
          │              │              │              │
     :8084│         :8083│         :8081│         :8082│         :8085
          ↓              ↓              ↓              ↓              ↓
    ┌──────────┐   ┌──────────┐  ┌──────────┐  ┌──────────┐  ┌──────────┐
    │ MS       │   │ MS       │  │ MS       │  │ MS       │  │ MS       │
    │Usuarios  │   │Reportes  │  │Geoloc.   │  │Coincid.  │  │Notif.    │
    └──────────┘   └──────────┘  └──────────┘  └──────────┘  └──────────┘
         │              │              │              │              │
         └──────────────┴──────────────┴──────────────┴──────────────┘
                        │
                        ↓
            ┌─────────────────────────┐
            │   MySQL Database        │
            │   (3 Schemas)           │
            ├─────────────────────────┤
            │ • sanosysalvos_usuarios │
            │ • sanosysalvos          │
            │ • sanosysalvos_notif.   │
            └─────────────────────────┘

═══════════════════════════════════════════════════════════

🔌 PUERTOS Y DIRECCIONES

│ Servicio              │ Puerto │ Dirección                       │
├──────────────────────┼────────┼─────────────────────────────────┤
│ Frontend              │ 3000   │ http://localhost:3000           │
│ API Gateway           │ 8080   │ http://localhost:8080           │
│ MS Usuarios           │ 8084   │ http://localhost:8084           │
│ MS Reportes           │ 8083   │ http://localhost:8083           │
│ MS Geolocalizacion    │ 8081   │ http://localhost:8081           │
│ MS Coincidencias      │ 8082   │ http://localhost:8082           │
│ MS Notificaciones     │ 8085   │ http://localhost:8085           │
│ MySQL                 │ 3306   │ mysql://root@localhost:3306     │
│ phpMyAdmin            │ 80     │ http://localhost/phpmyadmin     │

═══════════════════════════════════════════════════════════

📦 DEPENDENCIAS PRINCIPALES

Backend:
  ✓ Spring Boot 3.5.12
  ✓ Spring Cloud Gateway 2023.0.3
  ✓ Spring Data JPA (ORM)
  ✓ MySQL Driver 8.0
  ✓ Resilience4j 2.1.0 (Circuit Breaker)
  ✓ JJWT 0.11.5 (JWT Authentication)
  ✓ Lombok (Reduce boilerplate)

Frontend:
  ✓ React 18.2.0
  ✓ TypeScript 5.3.2
  ✓ Vite 5.0.7 (Build tool)
  ✓ React Router 6.20.0
  ✓ Zustand (State management)
  ✓ Axios (HTTP client)
  ✓ Tailwind CSS 3.3.0
  ✓ Lucide Icons

Docker:
  ✓ Maven 3.9 (Build stage)
  ✓ Eclipse Temurin 17 (Runtime)
  ✓ MySQL 8.0
  ✓ Nginx (Frontend reverse proxy)

═══════════════════════════════════════════════════════════

🚀 QUICK START

# Iniciar todo
docker-compose up -d

# Ver logs en tiempo real
docker-compose logs -f

# Detener todo
docker-compose down

# Limpiar volúmenes y recrear
docker-compose down -v && docker-compose up -d

═══════════════════════════════════════════════════════════

📝 CONVENCIONES

Nombres de Ramas:
  - feature/description         (Nuevas características)
  - fix/description             (Correcciones)
  - docs/description            (Documentación)
  - refactor/description        (Refactorización)

Commits:
  - feat: description
  - fix: description
  - docs: description
  - refactor: description

Métodos HTTP REST:
  - GET /api/reportes           (Listar)
  - GET /api/reportes/:id       (Obtener uno)
  - POST /api/reportes          (Crear)
  - PUT /api/reportes/:id       (Actualizar)
  - DELETE /api/reportes/:id    (Eliminar)

═══════════════════════════════════════════════════════════
