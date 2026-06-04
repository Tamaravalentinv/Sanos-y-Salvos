# Sanos y Salvos 

Plataforma integral de recuperación de mascotas perdidas basada en microservicios. Conecta a ciudadanos, clínicas veterinarias, refugios y municipalidades para mejorar las tasas de recuperación mediante geolocalización, coincidencias inteligentes y notificaciones en tiempo real.

## Tabla de Contenidos

- [Estado del Proyecto](#estado-del-proyecto)
- [Características](#características)
- [Arquitectura](#arquitectura)
- [Componentes del Proyecto](#componentes-del-proyecto)
- [Enlaces a Repositorios](#enlaces-a-repositorios)
- [Documentación](#documentación)
- [Arquetipos Maven](#arquetipos-maven)
- [Requisitos Previos](#requisitos-previos)
- [Inicio Rápido](#inicio-rápido)
- [Estructura del Proyecto](#estructura-del-proyecto)
- [Desarrollo](#desarrollo)
- [Pruebas Unitarias](#pruebas-unitarias-y-cobertura-de-código)
- [Rutas API](#rutas-api)

## Estado del Proyecto

**Estado General: COMPLETO Y VALIDADO**

### Validaciones Completadas

- [x] Infraestructura Docker operativa (docker-compose up -d: Exit Code 0)
- [x] BFF (Backend For Frontend) - Estructura validada
  - Código fuente organizado en `/api-gateway/src/`
  - Archivos de configuración presentes (pom.xml, Dockerfile, application.properties)

- [x] Microservicios - Estructura validada (7 servicios)
  - MS Autenticación - Código organizado, configuración, README.md
  - MS Geolocalizacion - Código organizado, configuración, README.md
  - MS Reportes - Código organizado, configuración, README.md
  - MS Notificaciones - Código organizado, configuración, README.md
  - MS Proyectos - Código organizado, configuración, README.md
  - MS Coincidencias - Código organizado, configuración, README.md
  - MS Recursos Humanos - Código organizado, configuración, README.md
  - Todos con instrucciones de ejecución y testing


- [x] Arquetipos Maven - Estructura validada
  - Plantillas base para nuevos microservicios
  - pom.xml configurado
  - README.md con guía de uso


- [x] Documentación generada
  - repositorios.txt - Enlaces y descripciones de repositorios
  - README.md y README por componente
  - sanos-microservice-archetype/ARCHETYPE-USAGE.md - Uso del arquetipo

### Resumen de Componentes

| Componente | Estado | Validación |
|-----------|--------|------------|
| Frontend | Operativo | Código React/TypeScript, npm install exitoso |
| BFF (api-gateway) | Operativo | Puerto 8080, Docker activo |
| MS Autenticación | Operativo | Docker activo, README.md |
| MS Geolocalizacion | Operativo | Docker activo, README.md |
| MS Reportes | Operativo | Docker activo, README.md |
| MS Notificaciones | Operativo | Docker activo, README.md |
| MS Proyectos | Operativo | Docker activo, README.md |
| MS Coincidencias | Operativo | Docker activo, no enrutado por API Gateway |
| MS Recursos Humanos | Operativo | Docker activo, README.md |
| Arquetipos Maven | Disponibles | Plantillas listos para usar |
| Base de Datos | Operativa | MySQL 8.0 con scripts iniciales |

## Componentes del Proyecto

### Frontend
- **Ubicación**: `/frontend`
- **Tecnología**: React 18+ | TypeScript | Vite | Tailwind CSS
- **Descripción**: Aplicación web para gestión de reportes y geolocalización
- **Componentes**: Button, Card, Input, Select, Sidebar, Layout, MapPicker, Badge, Textarea
- **Estado**: npm install completado exitosamente

### Backend For Frontend (BFF)
- **Ubicación**: `/api-gateway`
- **Tecnología**: Java 17 | Spring Boot 3.3.13 | Maven
- **Descripción**: API Gateway que orquesta y compone servicios desde microservicios
- **Puerto**: 8080
- **Patrón**: Circuit Breaker (Resilience4j), BFF Pattern

### Microservicios
- **MS Autenticación**: Gestión de usuarios y JWT
- **MS Geolocalizacion**: Tracking de ubicaciones y análisis geográfico
- **MS Reportes**: Gestión de reportes de mascotas perdidas/encontradas
- **MS Notificaciones**: Sistema multi-canal (email, SMS, push)
- **MS Proyectos**: Administración de proyectos y tareas
- **MS Coincidencias**: Motor de matching inteligente, no enrutado por API Gateway
- **MS Recursos Humanos**: Gestión de empleados, departamentos y permisos

### Arquetipos Maven
- **Ubicación**: `/sanos-microservice-archetype`
- **Descripción**: Plantillas para generar nuevos microservicios
- **Plantillas disponibles**: sanos-microservice-archetype
- **Uso**: Base para nuevos proyectos siguiendo estándares Sanos y Salvos

## Características

- **Gestión de Reportes**: Crear reportes de mascotas perdidas o encontradas
- **Gestión de Proyectos**: Administración de proyectos y tareas de trabajo
- **Coincidencias Inteligentes**: Motor de matching basado en características (raza, color, tamaño, proximidad)
- **Geolocalización**: Tracking de ubicaciones con análisis de zonas de incidencia
- **Notificaciones Multi-canal**: Email, SMS, Push, notificaciones internas
- **Autenticación JWT**: Seguridad en todos los microservicios
- **Pruebas Unitarias**: Suite de 332 tests backend y 13 tests frontend, con cobertura de línea superior al 60% en todos los componentes.
- **Docker**: Containerización completa para fácil despliegue
- **Documentación Completa**: Patrones de arquitectura, estrategia de branching, enlaces a repositorios
- **Arquetipos Maven**: Plantillas para generar nuevos componentes siguiendo estándares

## Arquitectura

Sistema de **7 microservicios** + API Gateway (8 componentes total):

### Microservicios

| Servicio | Puerto | Descripción | Acceso |
|----------|--------|-------------|--------|
| **MS Usuarios** | 8084 | Gestión de usuarios, organizaciones y autenticación | Público (API Gateway) |
| **MS Reportes** | 8083 | Reportes de mascotas perdidas/encontradas | Público (API Gateway) |
| **MS Geolocalizacion** | 8081 | Tracking de ubicaciones y hotspots de incidencia | Público (API Gateway) |
| **MS Coincidencias** | 8082* | Motor de matching inteligente (6-factor scoring) | Interno (vía MS Reportes) |
| **MS Notificaciones** | 8085 | Sistema de notificaciones multi-canal | Público (API Gateway) |
| **MS Proyectos** | 8086 | Gestión de proyectos y tareas de trabajo | Público (API Gateway) |
| **MS Recursos Humanos** | 8087 | Gestión de empleados, departamentos y permisos | Público (API Gateway) |
| **API Gateway** | 8080 | BFF con seguridad, routing y circuit breaker | Público |

*MS Coincidencias no se enruta públicamente desde el API Gateway. En desarrollo local y Docker Compose, su puerto `8082` está publicado para diagnóstico.
## Enlaces a Repositorios

Ver archivo `repositorios.txt` para:
- Enlaces al repositorio principal del proyecto
- Enlaces individuales a repositorios de cada componente (frontend, BFF, microservicios)
- Descripciones detalladas de cada repositorio
- Tecnologías utilizadas en cada componente
- Puertos y configuraciones de cada servicio

**Archivo**: [repositorios.txt](repositorios.txt)

## Documentación

El proyecto incluye documentación completa:

### Documentos Disponibles

1. **repositorios.txt**
   - Enlaces a todos los repositorios de componentes
   - Descripciones técnicas de cada módulo
   - Arquitectura de componentes con puertos y tecnologías
   - Instrucciones de configuración

2. **sanos-microservice-archetype/README.md**
   - Descripción y estructura del arquetipo de microservicios

3. **sanos-microservice-archetype/ARCHETYPE-USAGE.md**
   - Instrucciones para instalar y utilizar el arquetipo

### README por Componente

Cada componente tiene su propio README.md:
- `/api-gateway/BFF-README.md` - Instrucciones del Backend For Frontend
- `/ms-*/README.md` - Instrucciones de cada microservicio
- Todos con instrucciones de instalación, configuración y ejecución
- Documentación sin emojis, formato profesional
### Enrutamiento de MS Coincidencias

El microservicio de coincidencias no está expuesto mediante una ruta directa del API Gateway. El flujo principal pasa por MS Reportes:

```
Flujo anterior:
   Frontend → API Gateway → MS Coincidencias (8082) - EXPUESTO

Flujo actual (Seguro):
   Frontend → API Gateway → MS Reportes → MS Coincidencias (8082) - AISLADO
```

**Beneficios:**
- **Seguridad**: MS Coincidencias no dispone de una ruta pública en el API Gateway
- **Encapsulación**: Depende lógicamente de Reportes
- **Escalabilidad**: Facilita reemplazar con un servicio dedicado
- **Mantenibilidad**: Cambios internos sin afectar API pública

**Acceso a coincidencias:**
```bash
# Correcto - A través de MS Reportes
curl http://localhost:8080/api/bff/coincidencias?userId=1

# Correcto - Directamente en desarrollo
curl http://localhost:8083/matches/pendientes

# Disponible directamente solo en desarrollo local/Docker
curl http://localhost:8082/matches/pendientes
```

Ver documentación de arquitectura para detalles técnicos completos.

### Patrones de Diseño Implementados

- **Repository Pattern**: Acceso a datos vía Spring Data JPA
- **Factory Method**: Creación flexible de objetos (usuarios, reportes, coincidencias)
- **Circuit Breaker**: Protección de fallos en API Gateway (Resilience4j)
- **BFF Pattern**: API Gateway como Backend for Frontend
- **Service Discovery**: Microservicios comunicándose internamente
- **API Gateway Pattern**: Composición y orquestación de servicios

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
- **Documentación**: Markdown con análisis de patrones y estrategia de branching

## Arquetipos Maven

### Disponibles

**sanos-microservice-archetype**
- Ubicación: `/sanos-microservice-archetype`
- Descripción: Plantilla base para crear nuevos microservicios
- Incluye:
  - Estructura estándar de carpetas
  - Dependencias Maven base (Spring Boot, JUnit, Mockito)
  - Configuración de application.properties
  - Dockerfile predefinido
  - Clases base y controladores de ejemplo
  - POM.xml con versiones alineadas al proyecto

### Uso de Arquetipos

Para generar un nuevo microservicio:

```bash
cd sanos-microservice-archetype
mvn install

# Luego usar para generar nuevo proyecto
mvn archetype:generate \
  -DarchetypeGroupId=com.sanosysalvos \
  -DarchetypeArtifactId=sanos-microservice-archetype \
  -DarchetypeVersion=1.0.0 \
  -DgroupId=com.sanosysalvos \
  -DartifactId=ms-nuevo-servicio
```

Ver `sanos-microservice-archetype/ARCHETYPE-USAGE.md` para instrucciones detalladas.

## Requisitos Previos

- **Java 17** o superior
- **Maven 3.9+**
- **Docker 20.10+**
- **Docker Compose 1.29+**
- **Node.js 18+ y npm**
- **MySQL 8.0+** (opcional si usas Docker)
- **Git**

## Inicio Rápido

### Docker Compose (Recomendado)

#### 1. Clonar el repositorio

```bash
git clone <repository-url>
cd Sanos-y-Salvos
```

#### 2. Configurar variables de entorno

```powershell
Copy-Item .env.example .env
```

#### 3. Levantar base de datos, microservicios y API Gateway

```powershell
docker compose up --build -d
docker compose ps
```

El backend queda disponible mediante el API Gateway en `http://localhost:8080`.

#### 4. Levantar el frontend

```powershell
cd frontend
npm install
npm run dev
```

El frontend queda disponible en `http://localhost:3000`.

#### 5. Verificar el proyecto

```powershell
Invoke-WebRequest -UseBasicParsing http://localhost:8080/actuator/health
Invoke-WebRequest -UseBasicParsing http://localhost:3000
```

### Desarrollo Local sin Docker

Para usar MySQL local, importa `database/xampp_completo.sql`. Después ejecuta los módulos desde terminales separadas:

```powershell
cd ms-usuarios; .\mvnw.cmd spring-boot:run
cd ms-reportes; .\mvnw.cmd spring-boot:run
cd ms-geolocalizacion; .\mvnw.cmd spring-boot:run
cd ms-coincidencias; .\mvnw.cmd spring-boot:run
cd ms-notificaciones; .\mvnw.cmd spring-boot:run
cd ms-proyectos; .\mvnw.cmd spring-boot:run
cd ms-recursos-humanos; .\mvnw.cmd spring-boot:run
cd api-gateway; .\mvnw.cmd spring-boot:run
```

**Endpoints disponibles:**
- API Gateway (BFF): http://localhost:8080/api
- MS Usuarios: http://localhost:8084
- MS Reportes: http://localhost:8083
- MS Geolocalizacion: http://localhost:8081
- **MS Coincidencias** (Aislado, acceso solo vía MS Reportes): http://localhost:8082
  - Endpoints: http://localhost:8083/matches/** (delegados por MS Reportes)
- MS Notificaciones: http://localhost:8085
- MS Proyectos: http://localhost:8086
- MS Recursos Humanos: http://localhost:8087

## Estructura del Proyecto

```
Sanos-y-Salvos-main/
├── pom.xml                          # Parent POM multi-módulo
├── README.md
├── docker-compose.yml
├── frontend/                        # React + TypeScript + Vite
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
├── ms-recursos-humanos/             # Empleados, departamentos y permisos
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
│   └── xampp_completo.sql
│
├── sanos-microservice-archetype/    # Arquetipo Maven
│
└── scripts/                         # Scripts de despliegue y cobertura
    ├── build.sh
    ├── up.sh
    ├── down.sh
    ├── logs.sh
    ├── health-check.sh
    └── show-coverage.ps1
```

## Desarrollo

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

## Pruebas Unitarias y Cobertura de Codigo

**332 tests implementados en 8 componentes backend y 13 tests frontend**

La suite cubre controladores, servicios, modelos, seguridad JWT, flujos BFF, frontend, notificaciones, coincidencias, geolocalización, reportes, proyectos y recursos humanos. Todos los componentes superan el mínimo solicitado de 60% de cobertura de línea.

### Resumen de Cobertura por Modulo

| Modulo | Tests | Lineas | Ramas | Instrucciones | Estado |
|--------|------:|-------:|------:|--------------:|--------|
| **API Gateway** | 11 | 87.7% | 62.5% | 84.9% | Cumple |
| **MS Coincidencias** | 10 | 65.4% | 84.4% | 70.3% | Cumple |
| **MS Geolocalizacion** | 24 | 98.2% | 76.5% | 98.1% | Cumple |
| **MS Notificaciones** | 12 | 98.0% | 85.7% | 98.6% | Cumple |
| **MS Proyectos** | 20 | 81.6% | 100.0% | 75.7% | Cumple |
| **MS Recursos Humanos** | 19 | 97.5% | 92.9% | 97.5% | Cumple |
| **MS Reportes** | 116 | 93.3% | 60.0% | 93.4% | Cumple |
| **MS Usuarios** | 120 | 86.6% | 61.8% | 87.0% | Cumple |
| **TOTAL BACKEND** | **332** | **89.0%** | **74.8%** | **88.7%** | Cumple |

### Cobertura Frontend

| Tests | Statements | Branch | Funcs | Lines | Estado |
|------:|-----------:|-------:|------:|------:|--------|
| **13** | **83.51%** | **65.95%** | **73.33%** | **83.51%** | Cumple |

### Detalle de Tests Implementados

**MS Usuarios (120 tests)**
- Controllers: AuthControllerTest, OrganizacionControllerTest, UsuarioControllerTest.
- Services: UsuarioServiceTest, OrganizacionServiceTest, RolServiceTest.
- Modelos y seguridad: UsuarioTest, OrganizacionTest, RolTest, JwtUtilTest.
- Cobertura de linea: **86.58%**.

**MS Reportes (116 tests)**
- Controllers: ReporteControllerTest, ReporteControllerExtraTest, CoincidenciaProxyControllerTest, CoincidenciaProxyControllerExtraTest.
- Services: ReporteServiceTest, MascotaServiceTest.
- Modelos: ReporteTest, MascotaTest, FotoMascotaTest, CaracteristicaMascotaTest, TipoReporteTest.
- Cobertura de linea: **93.28%**.

**MS Geolocalizacion (24 tests)**
- Controllers: UbicacionControllerTest, HistorialUbicacionControllerTest, ZonaIncidenciaControllerTest.
- Services: UbicacionServiceTest, HistorialUbicacionServiceTest, ZonaIncidenciaServiceTest.
- Modelos: GeolocalizacionModelTest.
- Cobertura de linea: **98.17%**.

**MS Proyectos (20 tests)**
- Controllers: ProyectoControllerTest, TareaControllerTest.
- Services: ProyectoServiceTest, TareaServiceTest.
- Modelos: ProyectoTareaModelTest.
- Cobertura de linea: **81.58%**.

**MS Recursos Humanos (19 tests)**
- Controllers: EmpleadoControllerTest, DepartamentoControllerTest, PermisoControllerTest.
- Services: EmpleadoServiceTest, DepartamentoPermisoServiceTest.
- Modelos: RhModelTest.
- Cobertura de línea: **97.5%**.

**MS Coincidencias (10 tests)**
- Services: CoincidenciaServiceTest.
- Modelos: PuntajeCoincidenciaTest.
- Cobertura de linea: **65.36%**.

**MS Notificaciones (12 tests)**
- Controllers: NotificacionControllerTest.
- Services: NotificacionServiceTest.
- Modelos: NotificacionModelTest.
- Cobertura de línea: **98.0%**.

**API Gateway (11 tests)**
- Controllers: BFFControllerTest.
- Services: BFFServiceTest.
- Cobertura de linea: **87.72%**.

### Caracteristicas de la Suite de Tests

- **Framework**: JUnit 5 + Mockito.
- **Base de datos de tests**: H2 in-memory con `@ActiveProfiles("test")`.
- **Tests totales**: 332 backend y 13 frontend.
- **Estado de ejecución**: 345/345 PASSED.
- **Cobertura minima requerida**: 60% por componente.
- **Herramienta de cobertura**: JaCoCo v0.8.10.
- **Reportes generados**: `{microservicio}/target/site/jacoco/index.html`.

### Ejecutar Tests

```powershell
# Ejecutar todos los tests backend desde la raíz
mvn test

# Ejecutar los tests frontend
cd frontend
npm test
```

### Generar Reportes de Cobertura

```powershell
# Mostrar cobertura backend y frontend ya generada
powershell -NoProfile -ExecutionPolicy Bypass -File .\scripts\show-coverage.ps1

# Regenerar todos los tests y después mostrar cobertura
powershell -NoProfile -ExecutionPolicy Bypass -File .\scripts\show-coverage.ps1 -RunTests
```

Los reportes HTML backend quedan en `{microservicio}/target/site/jacoco/index.html`. El frontend genera `frontend/coverage/coverage-summary.json`.

### Validaciones de Pruebas

- 332/332 tests backend y 13/13 tests frontend ejecutados exitosamente.
- 0 fallos, 0 errores, 0 skipped.
- Todos los microservicios Java superan el 60% de cobertura de linea.
- H2 configurado para pruebas unitarias sin depender de MySQL local.
- JaCoCo genera reportes HTML, XML y CSV por modulo.
- Se validan casos de exito, error, listas vacias, excepciones y reglas de negocio.

## Seguridad

- Las credenciales están en `.env` (gitignored)
- Las contraseñas están hasheadas con BCrypt
- JWT para autenticación stateless
- Variables de entorno para configuración sensible

## Base de Datos

Docker Compose crea automáticamente 5 bases de datos:

- `sanosysalvos_usuarios` - MS Usuarios
- `sanosysalvos` - MS Reportes, Geolocalizacion y Coincidencias
- `sanosysalvos_notificaciones` - MS Notificaciones
- `sanosysalvos_proyectos` - MS Proyectos
- `sanosysalvos_rh` - MS Recursos Humanos

El script `docker/init/01-create-databases.sql` se ejecuta automáticamente al crear el contenedor MySQL. Para una instalación local con XAMPP, utiliza `database/xampp_completo.sql`.

## Despliegue con Docker

```powershell
# Configurar variables de entorno la primera vez
Copy-Item .env.example .env

# Iniciar stack
docker compose up --build -d

# Ver logs
docker compose logs -f

# Detener stack
docker compose down
```

## Rutas API

### A través del API Gateway (`http://localhost:8080`)

```
POST   /api/users/register              - Registrar usuario
GET    /api/users/{id}                  - Obtener usuario
PUT    /api/users/{id}                  - Actualizar usuario

POST   /api/reports                     - Crear reporte
GET    /api/reports/{id}                - Obtener reporte
GET    /api/reports/tipo/perdidos       - Reportes de pérdidas

POST   /api/map/ubicacion                - Registrar ubicación
GET    /api/map/hotzones                - Zonas de incidencia

POST   /api/matches/analyze             - Analizar coincidencias*
GET    /api/matches/{id}                - Obtener coincidencia*
PATCH  /api/matches/{id}/confirmar      - Confirmar match*

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

*Endpoints de coincidencias: Aislados internamente. Se acceden a través de MS Reportes via proxy.

### Acceso Directo a Microservicios

**MS Reportes (Puerto 8083)** - Endpoints de coincidencias delegados:
```
GET    /matches/pendientes              - Obtener coincidencias pendientes
GET    /matches/confirmadas             - Obtener coincidencias confirmadas
GET    /matches/potenciales             - Obtener coincidencias potenciales
GET    /matches/recientes               - Obtener coincidencias recientes
```

**MS Coincidencias (Puerto 8082)** - Disponible directamente en desarrollo local/Docker; no enrutado por el API Gateway.

---

## Contribuir

### Desarrollo

1. Crea una rama descriptiva:
```bash
# Para nuevas features
git checkout -b feature/nombre-descriptivo

# Para bugfixes
git checkout -b bugfix/descripcion-bug

# Para releases
git checkout -b release/v1.0.0
```

2. Haz tus cambios y asegúrate que compilen:
```bash
mvn clean install -DskipTests
```

3. Ejecuta los tests:
```bash
mvn test
```

4. Verifica que el código siga los patrones existentes y utiliza `sanos-microservice-archetype` como referencia para nuevos servicios.

5. Push a tu rama:
```bash
git push origin nombre-rama
```

6. Crea un Pull Request

### Referencia de Documentos

- **repositorios.txt**: Enlaces y descripciones de repositorios
- **Componentes README**: Instrucciones específicas por componente
- **sanos-microservice-archetype/ARCHETYPE-USAGE.md**: Uso del arquetipo Maven

## Licencia

Este proyecto es parte de la iniciativa Sanos y Salvos para recuperación de mascotas perdidas.

## Contacto y Soporte

Para más información o reportar problemas:
- Consulta la documentación específica de cada componente
- Revisa los README.md en cada directorio
- Consulta los documentos de arquitectura y estrategia
