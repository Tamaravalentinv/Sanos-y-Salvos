# Microservicio de Recursos Humanos

## Descripción

Microservicio responsable de gestionar personal, voluntarios y recursos humanos de la organización. Permite gestionar perfiles profesionales, experiencia, disponibilidad y asignaciones a proyectos.

**Puerto:** 8087
**Base de Datos:** sanos_rh
**Tecnología:** Spring Boot 3.x, MySQL

---

## Requisitos Previos

- Java 17 o superior
- Maven 3.8+
- MySQL 8.0+
- Docker Desktop (opcional)

Verifica tu entorno:
```bash
java -version
mvn -version
mysql --version
```

---

## Instalación y Ejecución

### 1. Construir el Proyecto

```bash
cd ms-recursos-humanos
mvn clean install
```

### 2. Ejecutar en Desarrollo

```bash
# Opción 1: Con Maven
mvn spring-boot:run

# Opción 2: Con Java directamente
java -jar target/ms-recursos-humanos-1.0.0.jar

# Opción 3: IDE Spring Boot
# Click derecho en MsRecursosHumanosApplication.java → Run
```

El servicio estará disponible en: http://localhost:8087

### 3. Verificar Estado

```bash
curl http://localhost:8087/actuator/health
```

---

## Ejecución con Docker

### 1. Construir Imagen

```bash
docker build -t sanos-recursos-humanos:1.0.0 .
```

### 2. Ejecutar Contenedor

```bash
docker run -d \
  -p 8087:8087 \
  -e SPRING_PROFILES_ACTIVE=docker \
  -e SPRING_DATASOURCE_URL=jdbc:mysql://mysql:3306/sanos_rh \
  --name ms-recursos-humanos \
  --network sanos-network \
  sanos-recursos-humanos:1.0.0
```

### 3. O usar Docker Compose (desde raíz del proyecto)

```bash
cd ..
docker-compose up -d ms-recursos-humanos
```

---

## Configuración

### Variables de Entorno

| Variable | Desarrollo | Producción |
|----------|-----------|-----------|
| `SPRING_PROFILES_ACTIVE` | `dev` | `prod` |
| `SPRING_DATASOURCE_URL` | `localhost:3306` | `mysql:3306` |

### application.properties

```properties
# Server
server.port=8087
spring.application.name=ms-recursos-humanos

# Database
spring.datasource.url=jdbc:mysql://localhost:3306/sanos_rh
spring.datasource.username=root
spring.datasource.password=root
spring.jpa.hibernate.ddl-auto=update

# Logging
logging.level.root=INFO
logging.level.com.sanosysalvos=DEBUG
```

---

## Estructura del Código

```
ms-recursos-humanos/
├── controller/
│   ├── EmpleadoController.java            # Endpoints empleados
│   └── VoluntarioController.java          # Endpoints voluntarios
├── service/
│   ├── EmpleadoService.java               # Servicio empleados
│   ├── VoluntarioService.java             # Servicio voluntarios
│   └── AsignacionService.java             # Servicio asignaciones
├── repository/
│   ├── EmpleadoRepository.java            # Acceso empleados
│   └── VoluntarioRepository.java          # Acceso voluntarios
├── model/
│   ├── Empleado.java                      # Entidad Empleado
│   ├── Voluntario.java                    # Entidad Voluntario
│   └── Experiencia.java                   # Entidad Experiencia
├── config/
│   └── DatabaseConfig.java                # Configuración BD
├── dto/
│   ├── EmpleadoRequest.java               # Request DTO
│   └── EmpleadoResponse.java              # Response DTO
└── MsRecursosHumanosApplication.java      # Main class
```

---

## API Endpoints

### Empleados

#### 1. Crear Empleado

```http
POST /api/empleados
Content-Type: application/json

{
  "nombre": "María García",
  "email": "maria@sanosysalvos.com",
  "telefono": "+573001234567",
  "cargo": "Veterinaria",
  "departamento": "Cuidado",
  "salario": 2500000,
  "fechaContratacion": "2026-05-01"
}
```

#### 2. Obtener Empleado

```http
GET /api/empleados/{id}
```

#### 3. Listar Empleados

```http
GET /api/empleados?departamento=Cuidado&pagina=0&tamaño=10
```

#### 4. Actualizar Empleado

```http
PUT /api/empleados/{id}
Content-Type: application/json

{
  "cargo": "Veterinaria Senior",
  "salario": 3000000
}
```

#### 5. Eliminar Empleado

```http
DELETE /api/empleados/{id}
```

### Voluntarios

#### 1. Registrar Voluntario

```http
POST /api/voluntarios
Content-Type: application/json

{
  "nombre": "Carlos López",
  "email": "carlos@example.com",
  "telefono": "+573009876543",
  "especialidad": "Médico",
  "experiencia": "5 años",
  "disponibilidad": "FINES_SEMANA",
  "horas_mes": 40
}
```

#### 2. Obtener Voluntario

```http
GET /api/voluntarios/{id}
```

#### 3. Listar Voluntarios

```http
GET /api/voluntarios?especialidad=Médico&estado=ACTIVO
```

#### 4. Desactivar Voluntario

```http
PUT /api/voluntarios/{id}/desactivar
```

---

## Pruebas con cURL

### Crear Empleado
```bash
curl -X POST "http://localhost:8087/api/empleados" \
  -H "Content-Type: application/json" \
  -d '{
    "nombre": "María García",
    "email": "maria@sanosysalvos.com",
    "cargo": "Veterinaria",
    "salario": 2500000
  }'
```

### Registrar Voluntario
```bash
curl -X POST "http://localhost:8087/api/voluntarios" \
  -H "Content-Type: application/json" \
  -d '{
    "nombre": "Carlos López",
    "email": "carlos@example.com",
    "especialidad": "Médico",
    "disponibilidad": "FINES_SEMANA"
  }'
```

### Listar Empleados
```bash
curl -X GET "http://localhost:8087/api/empleados"
```

---

## Base de Datos

### Tablas Principales

**empleados**
```sql
CREATE TABLE empleados (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  nombre VARCHAR(100),
  email VARCHAR(100) UNIQUE,
  telefono VARCHAR(20),
  cargo VARCHAR(100),
  departamento VARCHAR(100),
  salario DECIMAL(10, 2),
  fecha_contratacion DATE,
  estado VARCHAR(20)
);
```

**voluntarios**
```sql
CREATE TABLE voluntarios (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  nombre VARCHAR(100),
  email VARCHAR(100),
  telefono VARCHAR(20),
  especialidad VARCHAR(100),
  experiencia VARCHAR(255),
  disponibilidad VARCHAR(50),
  horas_mes INT,
  estado VARCHAR(20),
  fecha_registro TIMESTAMP
);
```

---

## Roadmap

### v1.0 (Actual)
- [x] CRUD empleados
- [x] CRUD voluntarios
- [x] Gestión de experiencia
- [x] Disponibilidad

### v2.0 (Planeado)
- [ ] Sistema de evaluación
- [ ] Capacitación y certificaciones
- [ ] Seguimiento de desempeño

---

Documento creado: 2026-05-07
Versión: 1.0
Estado: Borrador
