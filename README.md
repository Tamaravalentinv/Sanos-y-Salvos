# Sanos y Salvos - Pet Recovery Platform

Plataforma integral de recuperación de mascotas perdidas basada en microservicios. Conecta a ciudadanos, clínicas veterinarias, refugios y municipalidades para mejorar las tasas de recuperación mediante geolocalización, coincidencias inteligentes y notificaciones en tiempo real.

## 📋 Tabla de Contenidos

- [Características](#características)
- [Arquitectura](#arquitectura)
- [Requisitos Previos](#requisitos-previos)
- [Inicio Rápido](#inicio-rápido)
- [Estructura del Proyecto](#estructura-del-proyecto)
- [Documentación](#documentación)
- [Desarrollo](#desarrollo)

## ✨ Características

- **Gestión de Reportes**: Crear reportes de mascotas perdidas o encontradas
- **Coincidencias Inteligentes**: Motor de matching basado en características (raza, color, tamaño, proximidad)
- **Geolocalización**: Tracking de ubicaciones con análisis de zonas de incidencia
- **Notificaciones Multi-canal**: Email, SMS, Push, notificaciones internas
- **Autenticación JWT**: Seguridad en todos los microservicios
- **API Gateway**: BFF centralizado con Circuit Breaker y logging
- **Docker**: Containerización completa para fácil despliegue

## 🏗️ Arquitectura

Sistema de **6 microservicios** independientes + API Gateway:

### Microservicios

| Servicio | Puerto | Descripción |
|----------|--------|-------------|
| **MS Usuarios** | 8084 | Gestión de usuarios, organizaciones y autenticación |
| **MS Reportes** | 8083 | Reportes de mascotas perdidas/encontradas |
| **MS Geolocalizacion** | 8081 | Tracking de ubicaciones y hotspots de incidencia |
| **MS Coincidencias** | 8082 | Motor de matching inteligente (6-factor scoring) |
| **MS Notificaciones** | 8085 | Sistema de notificaciones multi-canal |
| **API Gateway** | 8080 | BFF con seguridad, routing y circuit breaker |

### Patrones de Diseño Implementados

- ✅ **Repository Pattern**: Acceso a datos vía Spring Data JPA
- ✅ **Factory Method**: Creación flexible de objetos (usuarios, reportes, coincidencias)
- ✅ **Circuit Breaker**: Protección de fallos en API Gateway (Resilience4j)
- ✅ **BFF Pattern**: API Gateway como Backend for Frontend

### Tecnologías

- **Framework**: Spring Boot 3.5.12
- **Java**: 17
- **Base de Datos**: MySQL 8.0+ (4 bases de datos independientes)
- **ORM**: Hibernate + Spring Data JPA
- **Seguridad**: Spring Security + JWT (jjwt 0.11.5)
- **Cloud**: Spring Cloud Gateway 2023.0.3
- **Resilencia**: Resilience4j 2.1.0
- **Utilidades**: Lombok
- **Testing**: JUnit 5 + Mockito
- **Contenedorización**: Docker + Docker Compose

## 📋 Requisitos Previos

- **Java 17** o superior
- **Maven 3.9+**
- **Docker 20.10+**
- **Docker Compose 1.29+**
- **MySQL 8.0+** (opcional si usas Docker)
- **Git**

## 🚀 Inicio Rápido

### 1. Clonar el Repositorio

```bash
git clone <repository-url>
cd Sanos-y-Salvos
```

### 2. Configurar Variables de Entorno

```bash
cp .env.example .env
# Edita .env con tus credenciales
nano .env
```

### 3. Construir Imagenes Docker

```bash
./scripts/build.sh
```

### 4. Iniciar el Stack

```bash
./scripts/up.sh
```

### 5. Verificar Servicios

```bash
./scripts/health-check.sh
```

Los servicios estarán disponibles en:
- API Gateway: http://localhost:8080
- MS Usuarios: http://localhost:8084
- MS Reportes: http://localhost:8083
- MS Geolocalizacion: http://localhost:8081
- MS Coincidencias: http://localhost:8082
- MS Notificaciones: http://localhost:8085

### Detener el Stack

```bash
./scripts/down.sh
```

## 📁 Estructura del Proyecto

```
Sanos-y-Salvos/
├── pom.xml                          # Parent POM multi-módulo
├── README.md                        # Este archivo
├── docker-compose.yml               # Orquestación de servicios
├── .env.example                     # Template de configuración
├── .gitignore                       # Archivos ignorados por Git
├── init-databases.sql               # Script inicialización BD
│
├── ms-usuarios/                     # Módulo 1: User Management
│   ├── pom.xml
│   ├── src/
│   └── Dockerfile
│
├── ms-reportes/                     # Módulo 2: Pet Reports
│   ├── pom.xml
│   ├── src/
│   └── Dockerfile
│
├── ms-geolocalizacion/              # Módulo 3: Geolocation
│   ├── pom.xml
│   ├── src/
│   └── Dockerfile
│
├── ms-coincidencias/                # Módulo 4: Matching Engine
│   ├── pom.xml
│   ├── src/
│   └── Dockerfile
│
├── ms-notificaciones/               # Módulo 5: Notifications
│   ├── pom.xml
│   ├── src/
│   └── Dockerfile
│
├── api-gateway/                     # Módulo 6: API Gateway (BFF)
│   ├── pom.xml
│   ├── src/
│   └── Dockerfile
│
├── scripts/                         # Scripts de deployment
│   ├── build.sh                     # Build Docker images
│   ├── up.sh                        # Iniciar stack
│   ├── down.sh                      # Detener stack
│   ├── logs.sh                      # Ver logs
│   └── health-check.sh              # Verificar salud
│
└── docs/                            # Documentación
    ├── DOCKER_README.md             # Guía de Docker
    ├── DOCKER_CONFIG.md             # Configuración de servicios
    ├── SECURITY_CONFIG.md           # Seguridad y credenciales
    └── BACKEND_IMPLEMENTATION.md    # Detalles de implementación
```

## 📚 Documentación

- **[Docker Setup](docs/DOCKER_README.md)** - Guía completa de Docker y Docker Compose
- **[Configuration](docs/DOCKER_CONFIG.md)** - Configuración de servicios y propiedades
- **[Security](docs/SECURITY_CONFIG.md)** - Seguridad, credenciales y variables de entorno
- **[Backend Implementation](docs/BACKEND_IMPLEMENTATION.md)** - Detalles técnicos de la implementación

## 🛠️ Desarrollo

### Compilar el Proyecto

```bash
mvn clean install
```

### Ejecutar un Microservicio Localmente

```bash
# MS Usuarios
cd ms-usuarios
mvn spring-boot:run

# O desde la raíz
mvn spring-boot:run -pl ms-usuarios
```

### Ejecutar Tests

```bash
# Todos los tests
mvn test

# Tests de un módulo específico
mvn test -pl ms-usuarios
```

### Acceder a la Base de Datos

```bash
docker exec -it sanos-mysql mysql -u ${MYSQL_USER} -p${MYSQL_PASSWORD} sanosysalvos
```

### Ver Logs de un Servicio

```bash
docker-compose logs -f ms-usuarios
```

## 🔐 Seguridad

- Las credenciales están en `.env` (gitignored)
- Usa `.env.example` como template
- Nunca commits `.env` o archivos de secretos
- Las contraseñas están hasheadas con BCrypt
- JWT para autenticación stateless
- Variables de entorno para configuración sensible

Ver [docs/SECURITY_CONFIG.md](docs/SECURITY_CONFIG.md) para más detalles.

## 📊 Base de Datos

Se crean 3 bases de datos automáticamente:

- `sanosysalvos_usuarios` - MS Usuarios
- `sanosysalvos` - MS Reportes, Geolocalizacion, Coincidencias
- `sanosysalvos_notificaciones` - MS Notificaciones

El script `init-databases.sql` se ejecuta automáticamente en el contenedor MySQL.

## 🚀 Despliegue

### Docker

Ver [docs/DOCKER_README.md](docs/DOCKER_README.md) para guía completa.

```bash
# Build
./scripts/build.sh

# Deploy
./scripts/up.sh

# Monitorear
./scripts/health-check.sh
./scripts/logs.sh
```

### Parar

```bash
./scripts/down.sh
```

## 📝 Rutas API

Todas las rutas van a través del API Gateway en `http://localhost:8080`:

```
POST   /api/users/register              - Registrar usuario
GET    /api/users/{id}                  - Obtener usuario
PUT    /api/users/{id}                  - Actualizar usuario

POST   /api/reports                     - Crear reporte
GET    /api/reports/{id}                - Obtener reporte
GET    /api/reports/tipo/perdidos       - Reportes de pérdidas

POST   /api/map/ubicacion                - Registrar ubicación
GET    /api/map/hotzones                - Zonas de incidencia

POST   /api/matches/analyze             - Analizar coincidencias
PATCH  /api/matches/{id}/confirmar      - Confirmar match

POST   /api/notifications               - Crear notificación
GET    /api/notifications/user/{id}     - Notificaciones de usuario
```

## 👥 Contribuidores

- [Tu nombre/equipo]

## 📄 Licencia

Este proyecto es parte del Caso Semestral de [Universidad/Institución].

## 🤝 Soporte

Para preguntas o problemas, contactar a [email/contacto].

---

**Última actualización:** Marzo 2026  
**Versión:** 1.0.0
