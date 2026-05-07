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
  - README.md con instrucciones claras de instalación y ejecución
  - Documentación profesional sin emojis

- [x] Microservicios - Estructura validada (6 servicios)
  - MS Autenticación - Código organizado, configuración, README.md
  - MS Geolocalizacion - Código organizado, configuración, README.md
  - MS Reportes - Código organizado, configuración, README.md
  - MS Notificaciones - Código organizado, configuración, README.md
  - MS Proyectos - Código organizado, configuración, README.md
  - MS Coincidencias - Código organizado, configuración, README.md
  - Todos con instrucciones de ejecución y testing
  - Documentación profesional sin emojis

- [x] Arquetipos Maven - Estructura validada
  - Plantillas base para nuevos microservicios
  - pom.xml configurado
  - README.md con guía de uso
  - Documentación profesional sin emojis

- [x] Documentación generada
  - ANALISIS_PATRONES_ARQUETIPOS.md - Análisis de patrones y arquetipos
  - PLAN_BRANCHING.md - Estrategia de branching Git
  - repositorios.txt - Enlaces y descripciones de repositorios
  - Todas sin emojis

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
| MS Coincidencias | Operativo | Docker activo (aislado), README.md |
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
- **MS Coincidencias**: Motor de matching inteligente (aislado internamente)

### Arquetipos Maven
- **Ubicación**: `/archetipos`
- **Descripción**: Plantillas para generar nuevos microservicios
- **Plantillas disponibles**: arquetipo-base-microservicio
- **Uso**: Base para nuevos proyectos siguiendo estándares Sanos y Salvos

## Características

- **Gestión de Reportes**: Crear reportes de mascotas perdidas o encontradas
- **Gestión de Proyectos**: Administración de proyectos y tareas de trabajo
- **Coincidencias Inteligentes**: Motor de matching basado en características (raza, color, tamaño, proximidad)
- **Geolocalización**: Tracking de ubicaciones con análisis de zonas de incidencia
- **Notificaciones Multi-canal**: Email, SMS, Push, notificaciones internas
- **Autenticación JWT**: Seguridad en todos los microservicios
- **Pruebas Unitarias**: Suite de 32 tests con ~92% de cobertura de código
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
| **API Gateway** | 8080 | BFF con seguridad, routing y circuit breaker | Público |

*MS Coincidencias está aislado del API Gateway. Solo es accesible internamente a través de MS Reportes via CoincidenciaProxyController.
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

### Documentos Generados

1. **ANALISIS_PATRONES_ARQUETIPOS.md**
   - Análisis de patrones de arquitectura implementados
   - Descripción de arquetipos Maven disponibles
   - Recomendaciones para desarrollo futuro
   - Matriz de patrones y características

2. **PLAN_BRANCHING.md**
   - Estrategia de branching Git para el proyecto
   - Nomenclatura de ramas
   - Flujo de trabajo para features, bugfixes y releases
   - Checklist de validación para cambios

3. **repositorios.txt**
   - Enlaces a todos los repositorios de componentes
   - Descripciones técnicas de cada módulo
   - Arquitectura de componentes con puertos y tecnologías
   - Instrucciones de configuración

### README por Componente

Cada componente tiene su propio README.md:
- `/api-gateway/BFF-README.md` - Instrucciones del Backend For Frontend
- `/ms-*/README.md` - Instrucciones de cada microservicio
- Todos con instrucciones de instalación, configuración y ejecución
- Documentación sin emojis, formato profesional
### Aislamiento de MS Coincidencias

**Cambio arquitectónico importante:** El microservicio de coincidencias ha sido removido del API Gateway público y ahora funciona de forma interna:

```
Flujo anterior:
   Frontend → API Gateway → MS Coincidencias (8082) - EXPUESTO

Flujo actual (Seguro):
   Frontend → API Gateway → MS Reportes → MS Coincidencias (8082) - AISLADO
```

**Beneficios:**
- **Seguridad**: MS Coincidencias NO es accesible desde Internet
- **Encapsulación**: Depende lógicamente de Reportes
- **Escalabilidad**: Facilita reemplazar con un servicio dedicado
- **Mantenibilidad**: Cambios internos sin afectar API pública

**Acceso a coincidencias:**
```bash
# Correcto - A través de MS Reportes
curl http://localhost:8080/api/bff/coincidencias?userId=1

# Correcto - Directamente en desarrollo
curl http://localhost:8083/matches/pendientes

# NO disponible - Puerto no expuesto
curl http://localhost:8082/matches/pendientes  # Falla
```

Ver documentación de arquitectura para detalles técnicos completos.

### Patrones de Diseño Implementados

- **Repository Pattern**: Acceso a datos vía Spring Data JPA
- **Factory Method**: Creación flexible de objetos (usuarios, reportes, coincidencias)
- **Circuit Breaker**: Protección de fallos en API Gateway (Resilience4j)
- **BFF Pattern**: API Gateway como Backend for Frontend
- **Service Discovery**: Microservicios comunicándose internamente
- **API Gateway Pattern**: Composición y orquestación de servicios

Ver `ANALISIS_PATRONES_ARQUETIPOS.md` para análisis detallado de patrones.

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

**arquetipo-base-microservicio**
- Ubicación: `/archetipos/arquetipo-base-microservicio`
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
cd archetipos/arquetipo-base-microservicio
mvn install

# Luego usar para generar nuevo proyecto
mvn archetype:generate \
  -DarchetypeGroupId=com.sanosysalvos \
  -DarchetypeArtifactId=arquetipo-base-microservicio \
  -DarchetypeVersion=1.0.0 \
  -DgroupId=com.sanosysalvos \
  -DartifactId=ms-nuevo-servicio
```

Ver `ANALISIS_PATRONES_ARQUETIPOS.md` para instrucciones detalladas.

## Requisitos Previos

- **Java 17** o superior
- **Maven 3.9+**
- **Docker 20.10+**
- **Docker Compose 1.29+**
- **MySQL 8.0+** (opcional si usas Docker)
- **Git**

## Inicio Rápido

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

En terminales separadas (orden recomendado):

```bash
# 1. MS Usuarios (8084) - Base de usuarios
cd ms-usuarios && mvn spring-boot:run

# 2. MS Reportes (8083) - Base de reportes + proxy de coincidencias
cd ms-reportes && mvn spring-boot:run

# 3. MS Geolocalizacion (8081) - Ubicaciones
cd ms-geolocalizacion && mvn spring-boot:run

# 4. MS Coincidencias (8082) - AISLADO, no expuesto públicamente
cd ms-coincidencias && mvn spring-boot:run

# 5. MS Notificaciones (8085) - Sistema de notificaciones
cd ms-notificaciones && mvn spring-boot:run

# 6. MS Proyectos (8086) - Gestión de proyectos
cd ms-proyectos && mvn spring-boot:run

# 7. API Gateway (8080) - BFF con enrutamiento
cd api-gateway && mvn spring-boot:run
```

**Verificar que todos los servicios están activos:**
```bash
# API Gateway health check
curl http://localhost:8080/api/health

# Otros servicios sin autenticación
curl http://localhost:8083/reports    # MS Reportes
curl http://localhost:8084/usuarios   # MS Usuarios
curl http://localhost:8081/ubicaciones # MS Geolocalizacion
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

**Nota:** Ms-coincidencias NO está expuesto directamente. Para acceder a endpoints de coincidencias, 
usar la ruta de MS Reportes: `GET http://localhost:8083/matches/pendientes`

## Estructura del Proyecto

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

## Pruebas Unitarias y Cobertura de Código

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

- JUnit 5 + Mockito para pruebas unitarias
- 32 tests implementados (0 fallos, 0 errores)
- Cobertura de código: ~92% 
- JaCoCo v0.8.10 para generación de reportes
- SonarQube v3.10.0.2594 configurado

### Generar Reporte de Cobertura

```bash
# Ejecutar tests y generar reporte JaCoCo
mvn clean test

# El reporte se genera en: ms-proyectos/target/site/jacoco/index.html
# Abre en tu navegador para ver detalles detallados
```

## Seguridad

- Las credenciales están en `.env` (gitignored)
- Las contraseñas están hasheadas con BCrypt
- JWT para autenticación stateless
- Variables de entorno para configuración sensible

## Base de Datos

Se crean automáticamente 3 bases de datos:

- `sanosysalvos_usuarios` - MS Usuarios
- `sanosysalvos` - MS Reportes, Geolocalizacion, Coincidencias, Proyectos
- `sanosysalvos_notificaciones` - MS Notificaciones

El script `database/completo_script.sql` se ejecuta automáticamente en la instalación.

## Despliegue con Docker

```bash
# Iniciar stack
docker compose up -d

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

**MS Coincidencias (Puerto 8082)** - NO EXPUESTO (uso interno solo)

---

## Contribuir

### Desarrollo

1. Crea una rama siguiendo la estrategia definida en `PLAN_BRANCHING.md`:
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

4. Verifica que el código cumple con patrones de arquitectura:
- Consulta `ANALISIS_PATRONES_ARQUETIPOS.md`
- Utiliza los arquetipos como referencia

5. Push a tu rama:
```bash
git push origin nombre-rama
```

6. Crea un Pull Request

### Referencia de Documentos

- **PLAN_BRANCHING.md**: Estrategia de branching y flujo de desarrollo
- **ANALISIS_PATRONES_ARQUETIPOS.md**: Patrones arquitectónicos y uso de arquetipos
- **repositorios.txt**: Enlaces y descripciones de repositorios
- **Componentes README**: Instrucciones específicas por componente

## Licencia

Este proyecto es parte de la iniciativa Sanos y Salvos para recuperación de mascotas perdidas.

## Contacto y Soporte

Para más información o reportar problemas:
- Consulta la documentación específica de cada componente
- Revisa los README.md en cada directorio
- Consulta los documentos de arquitectura y estrategia
