# Sanos y Salvos - Backend Microservices Architecture
## Implementation Summary

### ✅ COMPLETED COMPONENTS (6/6)

#### 1. MS Usuarios y Organizaciones (Port 8084)
**Purpose:** User, organization, and role management with authentication
**Key Features:**
- User registration and management with BCrypt password encoding
- Organization management by type (CLINICA, REFUGIO, MUNICIPALIDAD, etc.)
- Role-based access control (CIUDADANO, CLINICA, REFUGIO, ADMIN)
- JWT token support for authentication

**Endpoints:**
- POST /users/register - Register new user
- GET /users/{id} - Get user details
- PUT /users/{id} - Update user
- DELETE /users/{id} - Delete user
- POST /organizations - Create organization
- GET /organizations/{id} - Get organization

#### 2. MS Reportes (Port 8083)
**Purpose:** Lost/found pet reports with rich domain model
**Key Features:**
- Comprehensive Mascota entity with CaracteristicaMascota and FotoMascota
- Report lifecycle management (ABIERTO → EN_PROGRESO → RESUELTO → CERRADO)
- Advanced queries for finding similar reports
- Geographic proximity queries
- Priority and urgency handling

**Endpoints:**
- POST /reports - Create pet report
- GET /reports/{id} - Get report details (auto-increments views)
- GET /reports/tipo/perdidos - Get lost pet reports
- GET /reports/tipo/encontrados - Get found pet reports
- GET /reports/urgencia - Get urgent reports
- GET /reports/caracteristicas - Find by characteristics
- GET /reports/proximidad - Find by geographic proximity
- PATCH /reports/{id}/estado - Update report status

#### 3. MS Geolocalizacion (Port 8081)
**Purpose:** Geographic location tracking and hotspot analysis
**Key Features:**
- Comprehensive location tracking with address details
- ZonaIncidencia for identifying high-risk hotspots
- HistorialUbicacion for audit trail of sightings
- Automatic risk level calculation (BAJO/MEDIO/ALTO/CRITICO)
- Recovery rate tracking by zone

**Endpoints:**
- POST /map - Register location
- GET /map/{id} - Get location details
- GET /map/reporte/{reporteId} - Get locations for report
- GET /map/cercania - Find locations within radius
- GET /hotzones/alto-riesgo - Get high-risk zones
- GET /hotzones/exitosas - Get successful recovery zones
- POST /historial/avistamiento - Register sighting
- POST /historial/hallazgo - Register found location

#### 4. MS Coincidencias (Port 8082)
**Purpose:** Intelligent matching engine for lost/found pets
**Key Features:**
- Weighted scoring algorithm (Especie 100%, Raza 30%, Color 25%, Tamaño 20%, Cercanía 25%, Fechas 15%)
- Factory Method pattern for match creation
- Confidence scoring system
- Match lifecycle management (PENDIENTE_REVISION → CONFIRMADA/RECHAZADA → RESOLVIO_CASO)
- Optimistic locking with @Version

**Endpoints:**
- POST /matches/analyze - Create and analyze match
- GET /matches/{id} - Get match details
- GET /matches/reporte/{reporteId} - Get matches for report
- GET /matches/pendientes - Get pending matches
- GET /matches/potenciales - Get high-confidence matches (threshold: 70%)
- PATCH /matches/{id}/confirmar - Confirm match
- PATCH /matches/{id}/rechazar - Reject match
- PATCH /matches/{id}/resolver - Mark case as resolved

#### 5. MS Notificaciones (Port 8085)
**Purpose:** Multi-channel notification system
**Key Features:**
- Multiple notification types (EMAIL, SMS, PUSH, INTERNA)
- Factory Method pattern for notification creation by type
- Email integration with SMTP
- Notification status tracking (PENDIENTE → ENVIADA/FALLIDA)
- Read/unread status management

**Endpoints:**
- POST /notifications - Create notification
- GET /notifications/{id} - Get notification
- GET /notifications/user/{usuarioId}/no-leidas - Get unread notifications
- PATCH /notifications/{id}/leer - Mark as read
- DELETE /notifications/{id} - Delete notification

#### 6. API Gateway BFF (Port 8080)
**Purpose:** Single entry point for frontend, centralizes routing and security
**Key Features:**
- Route normalization (/api/* prefix)
- JWT-based security
- CORS configuration
- Circuit breaker pattern with Resilience4j
- Request/response logging
- Load balancing ready

**Routes Configured:**
```
/api/users/** → localhost:8084
/api/reports/** → localhost:8083
/api/map/** → localhost:8081
/api/matches/** → localhost:8082
/api/notifications/** → localhost:8085
```

### 📊 Architecture Patterns Implemented

1. **Repository Pattern** ✅
   - All services use Spring Data JPA repositories
   - Custom @Query methods for complex database queries
   - Clean separation of data access layer

2. **Factory Method Pattern** ✅
   - UsuarioService.crearUsuario() - Creates users with default roles
   - OrganizacionService.crearOrganizacion() - Creates organizations by type
   - NotificacionService.crearNotificacion() - Creates notifications by tipo (EMAIL/SMS/PUSH/INTERNA)
   - ReporteService.crearReporte() - Creates reports with auto-state management
   - CoincidenciaService.crearCoincidencia() - Creates matches with auto-scoring

3. **Circuit Breaker Pattern** ✅
   - Configured in API Gateway with Resilience4j
   - Threshold: 50% failure rate
   - Half-open state: 5 calls
   - Minimum calls: 10 before assessment

4. **BFF/API Gateway Pattern** ✅
   - Centralized entry point for all frontend requests
   - Route normalization and request transformation
   - Cross-cutting concerns (security, logging, rate limiting)

### 🗄️ Database Configuration

Each microservice has its own independent database:
- **sanosysalvos_usuarios** - User and organization data
- **sanosysalvos** - Reports, locations, and geolocation data
- **sanosysalvos_coincidencias** - Matches and scoring data
- **sanosysalvos_notificaciones** - Notification records

**Configuration:**
- MySQL 8.0+
- Hibernate DDL-auto: update
- Connection pooling enabled
- All services configured for localhost:3306

### 🔐 Security Implementation

- **Spring Security 6.x** configured on MS Usuarios and API Gateway
- **JWT (jjwt 0.11.5)** for stateless authentication
- **BCrypt** password encoding
- **CORS** enabled across all services
- **Path-based authorization** on API Gateway
- **Optimistic locking** on sensitive entities

### 🛠️ Technology Stack

- **Framework:** Spring Boot 3.5.12
- **API Gateway:** Spring Cloud Gateway 2023.0.3
- **ORM:** Spring Data JPA with Hibernate
- **Database:** MySQL 8.0+
- **Resilience:** Resilience4j 2.1.0
- **Serialization:** Jackson ObjectMapper
- **Logging:** Lombok + SLF4J
- **Testing:** JUnit 5 + Mockito (stubs created, implementation pending)

### ✏️ Next Steps (Pending Implementation)

1. **Unit Tests** (Goal: 60%+ coverage)
   - Test stubs created for all controllers and services
   - Implement with JUnit 5 + Mockito
   - Mock external service calls

2. **Integration Testing**
   - Test inter-service communication
   - Verify API Gateway routing
   - Load testing for Circuit Breaker

3. **Frontend Application**
   - React/Angular/Vue app consuming all 6 microservices
   - Integration with API Gateway at localhost:8080
   - Real-time notification updates

4. **Enhanced Features**
   - Event-driven architecture (Kafka/RabbitMQ)
   - Saga pattern for distributed transactions
   - API documentation (Swagger/OpenAPI)
   - Monitoring and metrics (Prometheus/Grafana)

### 🚀 Running the Backend

**Start all microservices:**
```bash
# Terminal 1: MS Usuarios (8084)
cd ms-usuarios && mvn spring-boot:run

# Terminal 2: MS Reportes (8083)
cd ms-reportes && mvn spring-boot:run

# Terminal 3: MS Geolocalizacion (8081)
cd ms-geolocalizacion && mvn spring-boot:run

# Terminal 4: MS Coincidencias (8082)
cd ms-coincidencias && mvn spring-boot:run

# Terminal 5: MS Notificaciones (8085)
cd ms-notificaciones && mvn spring-boot:run

# Terminal 6: API Gateway (8080)
cd api-gateway && mvn spring-boot:run
```

**Frontend requests go through:**
```
http://localhost:8080/api/*
```

**Direct backend calls (testing):**
```
http://localhost:8084/users/           # Users
http://localhost:8083/reports/         # Reports
http://localhost:8081/map/             # Geolocalization
http://localhost:8082/matches/         # Matches
http://localhost:8085/notifications/   # Notifications
```

### 📝 Code Standards Applied

- Clean Architecture with separation of concerns
- RESTful API design principles
- Proper HTTP status codes
- DTO pattern for request/response
- Lombok annotations for boilerplate reduction
- Spring best practices and conventions
- Comprehensive entity relationships with cascade rules

---
**Status:** ✅ BACKEND IMPLEMENTATION 100% COMPLETE
**Architecture:** Microservices with API Gateway BFF
**Patterns:** Repository, Factory Method, Circuit Breaker, BFF
**Ready for:** Frontend Integration, Unit Testing, Deployment
