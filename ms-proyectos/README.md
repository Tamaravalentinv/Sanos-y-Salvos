# Microservicio de Proyectos

## Descripción

Microservicio responsable de gestionar proyectos de rescate y cuidado de mascotas. Permite crear, organizar y rastrear proyectos comunitarios con múltiples participantes y recursos asignados.

**Puerto:** 8086
**Base de Datos:** sanos_proyectos
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
cd ms-proyectos
mvn clean install
```

### 2. Ejecutar en Desarrollo

```bash
# Opción 1: Con Maven
mvn spring-boot:run

# Opción 2: Con Java directamente
java -jar target/ms-proyectos-1.0.0.jar

# Opción 3: IDE Spring Boot
# Click derecho en MsProyectosApplication.java → Run
```

El servicio estará disponible en: http://localhost:8086

### 3. Verificar Estado

```bash
curl http://localhost:8086/actuator/health
```

---

## Ejecución con Docker

### 1. Construir Imagen

```bash
docker build -t sanos-proyectos:1.0.0 .
```

### 2. Ejecutar Contenedor

```bash
docker run -d \
  -p 8086:8086 \
  -e SPRING_PROFILES_ACTIVE=docker \
  -e SPRING_DATASOURCE_URL=jdbc:mysql://mysql:3306/sanos_proyectos \
  --name ms-proyectos \
  --network sanos-network \
  sanos-proyectos:1.0.0
```

### 3. O usar Docker Compose (desde raíz del proyecto)

```bash
cd ..
docker-compose up -d ms-proyectos
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
server.port=8086
spring.application.name=ms-proyectos

# Database
spring.datasource.url=jdbc:mysql://localhost:3306/sanos_proyectos
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
ms-proyectos/
├── controller/
│   └── ProyectoController.java            # Endpoints REST
├── service/
│   ├── ProyectoService.java               # Servicio proyectos
│   └── ParticipanteService.java           # Servicio participantes
├── repository/
│   ├── ProyectoRepository.java            # Acceso proyectos
│   └── ParticipanteRepository.java        # Acceso participantes
├── model/
│   ├── Proyecto.java                      # Entidad Proyecto
│   ├── Participante.java                  # Entidad Participante
│   └── Recurso.java                       # Entidad Recurso
├── config/
│   └── DatabaseConfig.java                # Configuración BD
├── dto/
│   ├── ProyectoRequest.java               # Request DTO
│   └── ProyectoResponse.java              # Response DTO
└── MsProyectosApplication.java            # Main class
```

---

## API Endpoints

### 1. Crear Proyecto

```http
POST /api/proyectos
Content-Type: application/json

{
  "nombre": "Proyecto de Rescate - Zona Centro",
  "descripcion": "Rescate y cuidado de mascotas perdidas en zona centro",
  "objetivo": "Rescatar 50 mascotas en 3 meses",
  "estado": "ACTIVO",
  "fechaInicio": "2026-05-07",
  "fechaFin": "2026-08-07",
  "coordinadorId": 1,
  "presupuesto": 5000000,
  "ciudad": "Medellín"
}
```

**Respuesta:**
```json
{
  "id": 1,
  "nombre": "Proyecto de Rescate - Zona Centro",
  "descripcion": "Rescate y cuidado de mascotas",
  "objetivo": "Rescatar 50 mascotas en 3 meses",
  "estado": "ACTIVO",
  "coordinador": {
    "id": 1,
    "nombre": "Juan Pérez"
  },
  "participantes": 0,
  "presupuesto": 5000000,
  "gastoActual": 0,
  "progreso": 0,
  "fechaCreacion": "2026-05-07T10:30:00"
}
```

### 2. Obtener Proyecto

```http
GET /api/proyectos/{id}
```

### 3. Listar Proyectos

```http
GET /api/proyectos?estado=ACTIVO&pagina=0&tamaño=10
```

**Parámetros:**
- `estado`: ACTIVO, PAUSADO, COMPLETADO, CANCELADO
- `ciudad`: Filtrar por ciudad
- `coordinadorId`: Filtrar por coordinador

### 4. Actualizar Proyecto

```http
PUT /api/proyectos/{id}
Content-Type: application/json

{
  "estado": "PAUSADO",
  "progreso": 45
}
```

### 5. Agregar Participante

```http
POST /api/proyectos/{id}/participantes
Content-Type: application/json

{
  "usuarioId": 2,
  "rol": "VOLUNTARIO",
  "especialidad": "Veterinario"
}
```

### 6. Listar Participantes

```http
GET /api/proyectos/{id}/participantes
```

### 7. Registrar Recurso Utilizado

```http
POST /api/proyectos/{id}/recursos
Content-Type: application/json

{
  "tipo": "MEDICAMENTOS",
  "descripcion": "Antibióticos",
  "cantidad": 10,
  "costo": 50000,
  "fecha": "2026-05-07"
}
```

### 8. Obtener Estadísticas

```http
GET /api/proyectos/{id}/estadisticas
```

**Respuesta:**
```json
{
  "proyectoId": 1,
  "totalParticipantes": 5,
  "mascotasRescatadas": 23,
  "mascotasAdoptadas": 15,
  "mascotasEnCuidado": 8,
  "gastototal": 1250000,
  "presupuestoPendiente": 3750000,
  "porcentajeProgreso": 46,
  "duracionDias": 45,
  "eficiencia": "85%"
}
```

---

## Pruebas con cURL

### Crear Proyecto
```bash
curl -X POST "http://localhost:8086/api/proyectos" \
  -H "Content-Type: application/json" \
  -d '{
    "nombre": "Proyecto de Rescate",
    "descripcion": "Rescate y cuidado de mascotas",
    "objetivo": "Rescatar 50 mascotas",
    "coordinadorId": 1,
    "presupuesto": 5000000
  }'
```

### Listar Proyectos
```bash
curl -X GET "http://localhost:8086/api/proyectos?estado=ACTIVO"
```

### Obtener Detalles
```bash
curl -X GET "http://localhost:8086/api/proyectos/1"
```

### Agregar Participante
```bash
curl -X POST "http://localhost:8086/api/proyectos/1/participantes" \
  -H "Content-Type: application/json" \
  -d '{
    "usuarioId": 2,
    "rol": "VOLUNTARIO"
  }'
```

### Ver Estadísticas
```bash
curl -X GET "http://localhost:8086/api/proyectos/1/estadisticas"
```

---

## Base de Datos

### Tablas Principales

**proyectos**
```sql
CREATE TABLE proyectos (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  nombre VARCHAR(100),
  descripcion TEXT,
  objetivo VARCHAR(255),
  estado VARCHAR(20),
  coordinador_id BIGINT,
  presupuesto DECIMAL(12, 2),
  gasto_actual DECIMAL(12, 2),
  progreso INT,
  fecha_inicio DATE,
  fecha_fin DATE,
  fecha_creacion TIMESTAMP,
  fecha_actualizacion TIMESTAMP
);
```

**participantes**
```sql
CREATE TABLE participantes (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  proyecto_id BIGINT,
  usuario_id BIGINT,
  rol VARCHAR(50),
  especialidad VARCHAR(100),
  fecha_incorporacion TIMESTAMP
);
```

---

## Roadmap

### v1.0 (Actual)
- [x] CRUD de proyectos
- [x] Gestión de participantes
- [x] Seguimiento de recursos
- [x] Estadísticas básicas

### v2.0 (Planeado)
- [ ] Reportes avanzados
- [ ] Asignación automática de tareas
- [ ] Integración con pagos

---

Documento creado: 2026-05-07
Versión: 1.0
Estado: Borrador
