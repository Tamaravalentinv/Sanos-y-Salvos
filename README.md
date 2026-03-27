# Sanos y Salvos 

Plataforma integral de recuperación de mascotas perdidas basada en microservicios. Conecta a ciudadanos, clínicas veterinarias, refugios y municipalidades para mejorar las tasas de recuperación mediante geolocalización, coincidencias inteligentes y notificaciones en tiempo real.

## 📋 Tabla de Contenidos

- [Características](#características)
- [Arquitectura](#arquitectura)
- [Requisitos Previos](#requisitos-previos)
- [Inicio Rápido](#inicio-rápido)
- [Estructura del Proyecto](#estructura-del-proyecto)
- [Desarrollo](#desarrollo)
- [Pruebas Unitarias](#pruebas-unitarias-y-cobertura-de-código)
- [Rutas API](#rutas-api)

## ✨ Características

- **Gestión de Reportes**: Crear reportes de mascotas perdidas o encontradas
- **Gestión de Proyectos**: Administración de proyectos y tareas de trabajo
- **Coincidencias Inteligentes**: Motor de matching basado en características (raza, color, tamaño, proximidad)
- **Geolocalización**: Tracking de ubicaciones con análisis de zonas de incidencia
- **Notificaciones Multi-canal**: Email, SMS, Push, notificaciones internas
- **Autenticación JWT**: Seguridad en todos los microservicios
- **Pruebas Unitarias**: Suite de 32 tests con ~92% de cobertura de código
- **Docker**: Containerización completa para fácil despliegue

## 🏗️ Arquitectura

Sistema de **7 microservicios** + API Gateway (8 componentes total):

### Microservicios

| Servicio | Puerto | Descripción |
|----------|--------|-------------|
| **MS Usuarios** | 8084 | Gestión de usuarios, organizaciones y autenticación |
| **MS Reportes** | 8083 | Reportes de mascotas perdidas/encontradas |
| **MS Geolocalizacion** | 8081 | Tracking de ubicaciones y hotspots de incidencia |
| **MS Coincidencias** | 8082 | Motor de matching inteligente (6-factor scoring) |
| **MS Notificaciones** | 8085 | Sistema de notificaciones multi-canal |
| **MS Proyectos** | 8086 | Gestión de proyectos y tareas de trabajo |
| **API Gateway** | 8080 | BFF con seguridad, routing y circuit breaker |

### Patrones de Diseño Implementados

- ✅ **Repository Pattern**: Acceso a datos vía Spring Data JPA
- ✅ **Factory Method**: Creación flexible de objetos (usuarios, reportes, coincidencias)
- ✅ **Circuit Breaker**: Protección de fallos en API Gateway (Resilience4j)
- ✅ **BFF Pattern**: API Gateway como Backend for Frontend

### Tecnologías

- **Framework**: Spring Boot 3.3.13
- **Java**: 17
- **Base de Datos**: MySQL 8.0
- **ORM**: Hibernate + Spring Data JPA
- **Seguridad**: Spring Security + JWT
- **Testing**: JUnit 5 + Mockito
- **Code Coverage**: JaCoCo 0.8.10
- **CI/CD**: SonarQube 3.10.0.2594
- **Contenedorización**: Docker + Docker Compose

## 📋 Requisitos Previos

- **Java 17** o superior
- **Maven 3.9+**
- **Docker 20.10+**
- **Docker Compose 1.29+**
- **MySQL 8.0+** (opcional si usas Docker)
- **Git**

## 🚀 Inicio Rápido

### Desarrollo Local (Recomendado)

#### 1. Clonar el repositorio

```bash
git clone <repository-url>
cd Sanos-y-Salvos-main
```

#### 2. Configurar la base de datos MySQL

1. Inicia XAMPP y verifica que MySQL está ejecutándose
2. Abre phpMyAdmin: http://localhost/phpmyadmin
3. Importa: `database/completo_script.sql`
4. Se crean automáticamente las 3 bases de datos necesarias

#### 3. Compilar el proyecto

```bash
mvn clean install -DskipTests
```

#### 4. Ejecutar los microservicios

En terminales separadas:

```bash
# MS Usuarios (Puerto 8084)
cd ms-usuarios && mvn spring-boot:run

# MS Reportes (Puerto 8083)
cd ms-reportes && mvn spring-boot:run

# MS Geolocalizacion (Puerto 8081)
cd ms-geolocalizacion && mvn spring-boot:run

# MS Coincidencias (Puerto 8082)
cd ms-coincidencias && mvn spring-boot:run

# MS Notificaciones (Puerto 8085)
cd ms-notificaciones && mvn spring-boot:run

# MS Proyectos (Puerto 8086)
cd ms-proyectos && mvn spring-boot:run

# API Gateway (Puerto 8080)
cd api-gateway && mvn spring-boot:run
```

**Endpoints disponibles:**
- API Gateway (BFF): http://localhost:8080/api
- MS Usuarios: http://localhost:8084
- MS Reportes: http://localhost:8083
- MS Geolocalizacion: http://localhost:8081
- MS Coincidencias: http://localhost:8082
- MS Notificaciones: http://localhost:8085
- MS Proyectos: http://localhost:8086

#### 5. Verificar salud de servicios

```bash
curl http://localhost:8080/api/health
```

## 📁 Estructura del Proyecto

```
Sanos-y-Salvos-main/
├── pom.xml                          # Parent POM multi-módulo
├── README.md
├── docker-compose.yml
│
├── ms-usuarios/                     # Gestión de usuarios
│   ├── pom.xml
│   ├── Dockerfile
│   └── src/
│
├── ms-reportes/                     # Reportes de mascotas
│   ├── pom.xml
│   ├── Dockerfile
│   └── src/
│
├── ms-geolocalizacion/              # Geolocalización
│   ├── pom.xml
│   ├── Dockerfile
│   └── src/
│
├── ms-coincidencias/                # Motor de matching
│   ├── pom.xml
│   ├── Dockerfile
│   └── src/
│
├── ms-notificaciones/               # Notificaciones
│   ├── pom.xml
│   ├── Dockerfile
│   └── src/
│
├── ms-proyectos/                    # Gestión de proyectos
│   ├── pom.xml
│   ├── Dockerfile
│   └── src/
│
├── api-gateway/                     # API Gateway BFF
│   ├── pom.xml
│   ├── Dockerfile
│   └── src/
│
├── database/                        # Scripts SQL
│   └── completo_script.sql
│
└── scripts/                         # Scripts deployment
    ├── build.sh
    ├── up.sh
    ├── down.sh
    ├── logs.sh
    └── health-check.sh
```

## 🛠️ Desarrollo

### Compilar y Empaquetar

```bash
# Compilar todo el proyecto
mvn clean install -DskipTests

# Construir solo un módulo
mvn clean package -DskipTests -pl ms-proyectos
```

### Ejecutar Tests

```bash
# Todos los tests
mvn test

# Tests de un módulo específico
mvn test -pl ms-proyectos
```

## ✅ Pruebas Unitarias y Cobertura de Código

**Estado: COMPLETADAS**

### MS Proyectos - Suite de Tests (32 pruebas)

| Componente | Tests | Cobertura |
|-----------|-------|-----------|
| ProyectoService | 8 | 97.6% |
| TareaService | 8 | 97.8% |
| ProyectoController | 8 | 90.3% |
| TareaController | 8 | 81.6% |
| **TOTAL** | **32** | **~92%** |

### Características de la Suite de Tests

- ✅ JUnit 5 + Mockito para pruebas unitarias
- ✅ 32 tests implementados (0 fallos, 0 errores)
- ✅ Cobertura de código: ~92% 
- ✅ JaCoCo v0.8.10 para generación de reportes
- ✅ SonarQube v3.10.0.2594 configurado

### Generar Reporte de Cobertura

```bash
# Ejecutar tests y generar reporte JaCoCo
mvn clean test

# El reporte se genera en: ms-proyectos/target/site/jacoco/index.html
# Abre en tu navegador para ver detalles detallados
```

## 🔐 Seguridad

- Las credenciales están en `.env` (gitignored)
- Las contraseñas están hasheadas con BCrypt
- JWT para autenticación stateless
- Variables de entorno para configuración sensible

## 📊 Base de Datos

Se crean automáticamente 3 bases de datos:

- `sanosysalvos_usuarios` - MS Usuarios
- `sanosysalvos` - MS Reportes, Geolocalizacion, Coincidencias, Proyectos
- `sanosysalvos_notificaciones` - MS Notificaciones

El script `database/completo_script.sql` se ejecuta automáticamente en la instalación.

## 🐳 Despliegue con Docker

```bash
# Iniciar stack
docker compose up -d

# Ver logs
docker compose logs -f

# Detener stack
docker compose down
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

POST   /api/proyectos                   - Crear proyecto
GET    /api/proyectos/{id}              - Obtener proyecto
PUT    /api/proyectos/{id}              - Actualizar proyecto
DELETE /api/proyectos/{id}              - Eliminar proyecto

POST   /api/tareas                      - Crear tarea
GET    /api/tareas/{id}                 - Obtener tarea
PUT    /api/tareas/{id}                 - Actualizar tarea
DELETE /api/tareas/{id}                 - Eliminar tarea
```

