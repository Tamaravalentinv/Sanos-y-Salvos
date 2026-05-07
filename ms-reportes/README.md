# Microservicio de Reportes

## Descripción

Microservicio responsable de gestionar reportes de mascotas perdidas y encontradas. Permite crear, actualizar, buscar y eliminar reportes con información completa de mascotas incluyendo fotos, características y ubicaciones.

**Puerto:** 8083
**Base de Datos:** sanos_reportes (también maneja sanos_mascotas)
**Tecnología:** Spring Boot 3.x, MySQL, AWS S3 (para fotos)

---

## Requisitos Previos

- Java 17 o superior
- Maven 3.8+
- MySQL 8.0+
- Docker Desktop (opcional)
- AWS S3 Bucket (para almacenar fotos - opcional)

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
cd ms-reportes
mvn clean install
```

### 2. Ejecutar en Desarrollo

```bash
# Opción 1: Con Maven
mvn spring-boot:run

# Opción 2: Con Java directamente
java -jar target/ms-reportes-1.0.0.jar

# Opción 3: IDE Spring Boot
# Click derecho en MsMascotasApplication.java → Run
```

El servicio estará disponible en: http://localhost:8083

### 3. Verificar Estado

```bash
curl http://localhost:8083/actuator/health
```

**Respuesta esperada:**
```json
{
  "status": "UP"
}
```

---

## Ejecución con Docker

### 1. Construir Imagen

```bash
docker build -t sanos-reportes:1.0.0 .
```

### 2. Ejecutar Contenedor

```bash
docker run -d \
  -p 8083:8083 \
  -e SPRING_PROFILES_ACTIVE=docker \
  -e SPRING_DATASOURCE_URL=jdbc:mysql://mysql:3306/sanos_reportes \
  --name ms-reportes \
  --network sanos-network \
  sanos-reportes:1.0.0
```

### 3. O usar Docker Compose (desde raíz del proyecto)

```bash
cd ..
docker-compose up -d ms-reportes
```

---

## Configuración

### Variables de Entorno

| Variable | Desarrollo | Producción |
|----------|-----------|-----------|
| `SPRING_PROFILES_ACTIVE` | `dev` | `prod` |
| `SPRING_DATASOURCE_URL` | `localhost:3306` | `mysql:3306` |
| `AWS_S3_BUCKET` | `dev-bucket` | `prod-bucket` |
| `AWS_REGION` | `us-east-1` | `us-east-1` |

### application.properties

```properties
# Server
server.port=8083
spring.application.name=ms-reportes

# Database
spring.datasource.url=jdbc:mysql://localhost:3306/sanos_reportes
spring.datasource.username=root
spring.datasource.password=root
spring.jpa.hibernate.ddl-auto=update

# AWS S3 (Opcional)
aws.s3.bucket=sanos-fotos-dev
aws.region=us-east-1

# File Upload
spring.servlet.multipart.max-file-size=10MB
spring.servlet.multipart.max-request-size=10MB

# Logging
logging.level.root=INFO
logging.level.com.sanosysalvos=DEBUG
```

---

## Estructura del Código

```
ms-reportes/
├── controller/
│   ├── ReporteController.java             # Endpoints reportes
│   └── MascotaController.java             # Endpoints mascotas
├── service/
│   ├── ReporteService.java                # Servicio reportes
│   ├── MascotaService.java                # Servicio mascotas
│   └── FileUploadService.java             # Carga de archivos
├── repository/
│   ├── ReporteRepository.java             # Acceso reportes
│   └── MascotaRepository.java             # Acceso mascotas
├── model/
│   ├── Reporte.java                       # Entidad Reporte
│   ├── Mascota.java                       # Entidad Mascota
│   └── Foto.java                          # Entidad Foto
├── config/
│   ├── S3Config.java                      # Configuración AWS
│   └── DatabaseConfig.java                # Configuración BD
├── dto/
│   ├── ReporteRequest.java                # Request DTO
│   ├── ReporteResponse.java               # Response DTO
│   ├── MascotaRequest.java                # Request DTO
│   └── MascotaResponse.java               # Response DTO
└── MsMascotasApplication.java             # Main class
```

---

## API Endpoints

### Reportes

#### 1. Crear Reporte

```http
POST /api/reportes
Content-Type: multipart/form-data

{
  "tipo": "PERDIDA",
  "descripcion": "Perro perdido en zona de Laureles",
  "nombreMascota": "Max",
  "especie": "Perro",
  "raza": "Golden Retriever",
  "color": "Dorado",
  "tamaño": "Grande",
  "caracteristicas": ["Cicatriz en oreja izquierda", "Collar azul"],
  "ciudad": "Medellín",
  "barrio": "Laureles",
  "direccion": "Cra 43A #32-65",
  "latitud": 6.209,
  "longitud": -75.569,
  "telefono": "+573001234567",
  "email": "usuario@example.com",
  "fotos": [file1, file2]
}
```

**Respuesta:**
```json
{
  "id": 1,
  "tipo": "PERDIDA",
  "estado": "ACTIVO",
  "descripcion": "Perro perdido en zona de Laureles",
  "mascota": {
    "id": 1,
    "nombre": "Max",
    "especie": "Perro",
    "raza": "Golden Retriever"
  },
  "ubicacion": {
    "ciudad": "Medellín",
    "barrio": "Laureles"
  },
  "fotos": [
    {
      "id": 1,
      "url": "https://s3.amazonaws.com/bucket/foto1.jpg"
    }
  ],
  "fechaCreacion": "2026-05-07T10:30:00"
}
```

#### 2. Obtener Reporte

```http
GET /api/reportes/{id}
```

**Respuesta:**
```json
{
  "id": 1,
  "tipo": "PERDIDA",
  "estado": "ACTIVO",
  "descripcion": "Perro perdido en zona de Laureles",
  "mascota": {
    "id": 1,
    "nombre": "Max",
    "especie": "Perro",
    "raza": "Golden Retriever",
    "color": "Dorado",
    "caracteristicas": ["Cicatriz en oreja izquierda"]
  },
  "ubicacion": {
    "ciudad": "Medellín",
    "barrio": "Laureles",
    "latitud": 6.209,
    "longitud": -75.569
  },
  "fotos": [
    {
      "id": 1,
      "url": "https://s3.amazonaws.com/bucket/foto1.jpg"
    }
  ],
  "propietario": {
    "nombre": "Juan",
    "email": "usuario@example.com",
    "telefono": "+573001234567"
  },
  "fechaCreacion": "2026-05-07T10:30:00"
}
```

#### 3. Listar Reportes

```http
GET /api/reportes?tipo=PERDIDA&estado=ACTIVO&ciudad=Medellín&pagina=0&tamaño=10
```

**Parámetros:**
- `tipo`: PERDIDA o ENCONTRADA
- `estado`: ACTIVO, RESUELTO, CERRADO
- `ciudad`: Filtrar por ciudad
- `pagina`: Número de página (0-indexado)
- `tamaño`: Elementos por página

**Respuesta:**
```json
{
  "content": [
    {
      "id": 1,
      "tipo": "PERDIDA",
      "estado": "ACTIVO",
      "nombreMascota": "Max",
      "ciudad": "Medellín",
      "fotoPrincipal": "https://...",
      "fechaCreacion": "2026-05-07T10:30:00"
    }
  ],
  "totalElements": 42,
  "totalPages": 5,
  "pagina": 0,
  "ultimaPagina": false
}
```

#### 4. Actualizar Reporte

```http
PUT /api/reportes/{id}
Content-Type: application/json

{
  "estado": "RESUELTO",
  "descripcion": "Mascota encontrada y retornada al dueño"
}
```

#### 5. Eliminar Reporte

```http
DELETE /api/reportes/{id}
```

### Mascotas

#### 1. Crear Mascota

```http
POST /api/mascotas
Content-Type: application/json

{
  "nombre": "Max",
  "especie": "Perro",
  "raza": "Golden Retriever",
  "color": "Dorado",
  "edad": 5,
  "tamaño": "Grande",
  "caracteristicas": ["Cicatriz en oreja", "Collar azul"],
  "usuarioId": 1
}
```

#### 2. Obtener Mascota

```http
GET /api/mascotas/{id}
```

#### 3. Listar Mascotas de Usuario

```http
GET /api/mascotas/usuario/{usuarioId}
```

---

## Búsqueda Avanzada

### Filtros Disponibles

```http
GET /api/reportes/buscar?
  tipo=PERDIDA&
  especie=Perro&
  raza=Golden&
  ciudad=Medellín&
  radio=5&
  desde=2026-05-01&
  hasta=2026-05-07
```

---

## Carga de Fotos

### Suportados

- Formatos: JPG, PNG, GIF
- Tamaño máximo: 10MB
- Se almacenan en AWS S3 o sistema de archivos local

### Ejemplo Upload

```bash
curl -X POST "http://localhost:8083/api/reportes" \
  -F "fotos=@/path/to/photo1.jpg" \
  -F "fotos=@/path/to/photo2.jpg" \
  -F "nombreMascota=Max" \
  -F "tipo=PERDIDA"
```

---

## Pruebas con cURL

### Crear Reporte
```bash
curl -X POST "http://localhost:8083/api/reportes" \
  -H "Content-Type: application/json" \
  -d '{
    "tipo": "PERDIDA",
    "descripcion": "Perro perdido",
    "nombreMascota": "Max",
    "especie": "Perro",
    "raza": "Golden Retriever",
    "ciudad": "Medellín",
    "barrio": "Laureles"
  }'
```

### Listar Reportes
```bash
curl -X GET "http://localhost:8083/api/reportes?tipo=PERDIDA&pagina=0&tamaño=10"
```

### Obtener Reporte
```bash
curl -X GET "http://localhost:8083/api/reportes/1"
```

### Actualizar Reporte
```bash
curl -X PUT "http://localhost:8083/api/reportes/1" \
  -H "Content-Type: application/json" \
  -d '{
    "estado": "RESUELTO"
  }'
```

---

## Base de Datos

### Tablas Principales

**reportes**
```sql
CREATE TABLE reportes (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  tipo VARCHAR(20),
  estado VARCHAR(20),
  descripcion TEXT,
  ciudad VARCHAR(100),
  barrio VARCHAR(100),
  latitud DECIMAL(10, 6),
  longitud DECIMAL(10, 6),
  usuario_id BIGINT,
  fecha_creacion TIMESTAMP,
  fecha_actualizacion TIMESTAMP
);
```

**mascotas**
```sql
CREATE TABLE mascotas (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  nombre VARCHAR(100),
  especie VARCHAR(50),
  raza VARCHAR(100),
  color VARCHAR(50),
  tamaño VARCHAR(20),
  usuario_id BIGINT,
  fecha_creacion TIMESTAMP
);
```

---

## Logs y Debugging

### Ver logs en tiempo real

```bash
# Archivo
tail -f logs/ms-reportes.log

# Docker
docker logs -f ms-reportes
```

---

## Troubleshooting

### Problema: "File size exceeds maximum"

**Solución:**
```properties
spring.servlet.multipart.max-file-size=10MB
spring.servlet.multipart.max-request-size=10MB
```

### Problema: Fotos no se guardan en S3

**Solución:**
- Verificar credenciales AWS
- Verificar bucket existe y tiene permisos
- Verificar región configurada correctamente

---

## Performance

### Índices de Base de Datos

```sql
CREATE INDEX idx_reportes_estado ON reportes(estado);
CREATE INDEX idx_reportes_ciudad ON reportes(ciudad);
CREATE INDEX idx_mascotas_usuario ON mascotas(usuario_id);
```

---

## Roadmap

### v1.0 (Actual)
- [x] CRUD de reportes
- [x] CRUD de mascotas
- [x] Carga de fotos
- [x] Búsqueda avanzada

### v2.0 (Planeado)
- [ ] Computer vision para análisis de fotos
- [ ] Recomendaciones automáticas
- [ ] Historiales de mascota

---

## Recursos

- [Spring Boot Documentation](https://spring.io/projects/spring-boot)
- [AWS S3 SDK for Java](https://docs.aws.amazon.com/sdk-for-java/)
- [File Upload Best Practices](https://owasp.org/www-community/vulnerabilities/Unrestricted_File_Upload)

Documento creado: 2026-05-07
Versión: 1.0
Estado: Borrador
