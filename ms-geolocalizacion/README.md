# Microservicio de Geolocalización

## Descripción

Microservicio responsable de gestionar ubicaciones geográficas, zonas de búsqueda y cálculos de distancia para mascotas reportadas. Proporciona funcionalidades de mapeo, geofencing y análisis de patrones de ubicación.

**Puerto:** 8081
**Base de Datos:** sanos_geolocalizacion
**Tecnología:** Spring Boot 3.x, MySQL, Google Maps API

---

## Requisitos Previos

- Java 17 o superior
- Maven 3.8+
- MySQL 8.0+
- Docker Desktop (opcional)
- Google Maps API Key (para funcionalidades avanzadas)

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
cd ms-geolocalizacion
mvn clean install
```

### 2. Ejecutar en Desarrollo

```bash
# Opción 1: Con Maven
mvn spring-boot:run

# Opción 2: Con Java directamente
java -jar target/ms-geolocalizacion-1.0.0.jar

# Opción 3: IDE Spring Boot
# Click derecho en MsGeolocalizacionApplication.java → Run
```

El servicio estará disponible en: http://localhost:8081

### 3. Verificar Estado

```bash
curl http://localhost:8081/actuator/health
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
docker build -t sanos-geolocalizacion:1.0.0 .
```

### 2. Ejecutar Contenedor

```bash
docker run -d \
  -p 8081:8081 \
  -e SPRING_PROFILES_ACTIVE=docker \
  -e SPRING_DATASOURCE_URL=jdbc:mysql://mysql:3306/sanos_geolocalizacion \
  -e GOOGLE_MAPS_API_KEY=your_api_key \
  --name ms-geolocalizacion \
  --network sanos-network \
  sanos-geolocalizacion:1.0.0
```

### 3. O usar Docker Compose (desde raíz del proyecto)

```bash
cd ..
docker-compose up -d ms-geolocalizacion
```

---

## Configuración

### Variables de Entorno

| Variable | Desarrollo | Producción |
|----------|-----------|-----------|
| `SPRING_PROFILES_ACTIVE` | `dev` | `prod` |
| `SPRING_DATASOURCE_URL` | `localhost:3306` | `mysql:3306` |
| `SPRING_DATASOURCE_USERNAME` | `root` | `(variable)` |
| `SPRING_DATASOURCE_PASSWORD` | `root` | `(variable)` |
| `GOOGLE_MAPS_API_KEY` | `dev_key` | `(production_key)` |

### application.properties

```properties
# Server
server.port=8081
spring.application.name=ms-geolocalizacion

# Database
spring.datasource.url=jdbc:mysql://localhost:3306/sanos_geolocalizacion
spring.datasource.username=root
spring.datasource.password=root
spring.jpa.hibernate.ddl-auto=update

# Google Maps
google.maps.api.key=${GOOGLE_MAPS_API_KEY:dev_key}

# Logging
logging.level.root=INFO
logging.level.com.sanosysalvos=DEBUG
```

---

## Estructura del Código

```
ms-geolocalizacion/
├── controller/
│   └── UbicacionController.java           # Endpoints REST
├── service/
│   ├── UbicacionService.java              # Gestión de ubicaciones
│   ├── DistanciaService.java              # Cálculos de distancia
│   └── GeoFencingService.java             # Geofencing
├── repository/
│   └── UbicacionRepository.java           # Acceso a datos
├── model/
│   ├── Ubicacion.java                     # Entidad Ubicacion
│   ├── Zona.java                          # Entidad Zona
│   └── Geofence.java                      # Geofence
├── config/
│   ├── GoogleMapsConfig.java              # Configuración Maps
│   └── DatabaseConfig.java                # Configuración BD
├── dto/
│   ├── UbicacionRequest.java              # Request DTO
│   └── UbicacionResponse.java             # Response DTO
└── MsGeolocalizacionApplication.java      # Main class
```

---

## API Endpoints

### 1. Crear Ubicación

```http
POST /api/ubicaciones
Content-Type: application/json

{
  "ciudad": "Medellín",
  "barrio": "Laureles",
  "direccion": "Cra 43A #32-65",
  "latitud": 6.209,
  "longitud": -75.569,
  "radio": 500
}
```

**Respuesta:**
```json
{
  "id": 1,
  "ciudad": "Medellín",
  "barrio": "Laureles",
  "direccion": "Cra 43A #32-65",
  "latitud": 6.209,
  "longitud": -75.569,
  "radio": 500,
  "fechaCreacion": "2026-05-07T10:30:00"
}
```

### 2. Obtener Ubicación

```http
GET /api/ubicaciones/{id}
```

**Respuesta:**
```json
{
  "id": 1,
  "ciudad": "Medellín",
  "barrio": "Laureles",
  "direccion": "Cra 43A #32-65",
  "latitud": 6.209,
  "longitud": -75.569,
  "radio": 500,
  "fechaCreacion": "2026-05-07T10:30:00"
}
```

### 3. Calcular Distancia entre Dos Puntos

```http
GET /api/ubicaciones/distancia?lat1=6.209&lon1=-75.569&lat2=6.215&lon2=-75.575
```

**Respuesta:**
```json
{
  "punto1": {
    "latitud": 6.209,
    "longitud": -75.569
  },
  "punto2": {
    "latitud": 6.215,
    "longitud": -75.575
  },
  "distanciaKm": 0.87,
  "distanciaMetros": 870,
  "duracionMinutos": 12,
  "tiempoEstimado": "12 minutos a pie"
}
```

### 4. Buscar Ubicaciones Cercanas

```http
GET /api/ubicaciones/cercanas?latitud=6.209&longitud=-75.569&radiokm=2
```

**Respuesta:**
```json
{
  "centro": {
    "latitud": 6.209,
    "longitud": -75.569
  },
  "radioKm": 2,
  "ubicacionesCercanas": [
    {
      "id": 2,
      "ciudad": "Medellín",
      "barrio": "Estadio",
      "distanciaKm": 1.2,
      "latitud": 6.220,
      "longitud": -75.560
    },
    {
      "id": 3,
      "ciudad": "Medellín",
      "barrio": "San Alejo",
      "distanciaKm": 1.8,
      "latitud": 6.205,
      "longitud": -75.585
    }
  ],
  "totalEncontradas": 2
}
```

### 5. Crear Zona de Búsqueda

```http
POST /api/zonas
Content-Type: application/json

{
  "nombre": "Zona Centro",
  "descripcion": "Centro de Medellín",
  "latitud": 6.252,
  "longitud": -75.543,
  "radioKm": 5
}
```

### 6. Verificar Geofence

```http
POST /api/geofence/verificar
Content-Type: application/json

{
  "geofenceId": 1,
  "latitud": 6.209,
  "longitud": -75.569
}
```

**Respuesta:**
```json
{
  "geofenceId": 1,
  "dentroDelArea": true,
  "distanciaAlBorde": 150,
  "zona": "Laureles",
  "timestamp": "2026-05-07T10:30:00"
}
```

---

## Algoritmo de Distancia

### Fórmula Haversine (Distancia Ortodrómica)

Calcula la distancia más corta entre dos puntos en una esfera:

```
a = sin(Δφ/2) * sin(Δφ/2) + cos φ1 * cos φ2 * sin(Δλ/2) * sin(Δλ/2)
c = 2 * atan2(√a, √(1−a))
d = R * c
```

Donde:
- φ: Latitud
- λ: Longitud
- R: Radio terrestre (6,371 km)

---

## Pruebas con cURL

### Crear Ubicación
```bash
curl -X POST "http://localhost:8081/api/ubicaciones" \
  -H "Content-Type: application/json" \
  -d '{
    "ciudad": "Medellín",
    "barrio": "Laureles",
    "direccion": "Cra 43A #32-65",
    "latitud": 6.209,
    "longitud": -75.569,
    "radio": 500
  }'
```

### Calcular Distancia
```bash
curl -X GET "http://localhost:8081/api/ubicaciones/distancia?lat1=6.209&lon1=-75.569&lat2=6.215&lon2=-75.575"
```

### Ubicaciones Cercanas
```bash
curl -X GET "http://localhost:8081/api/ubicaciones/cercanas?latitud=6.209&longitud=-75.569&radiokm=2"
```

### Verificar Geofence
```bash
curl -X POST "http://localhost:8081/api/geofence/verificar" \
  -H "Content-Type: application/json" \
  -d '{
    "geofenceId": 1,
    "latitud": 6.209,
    "longitud": -75.569
  }'
```

---

## Base de Datos

### Tablas Principales

**ubicaciones**
```sql
CREATE TABLE ubicaciones (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  ciudad VARCHAR(100),
  barrio VARCHAR(100),
  direccion VARCHAR(255),
  latitud DECIMAL(10, 6),
  longitud DECIMAL(10, 6),
  radio INT,
  fecha_creacion TIMESTAMP
);
```

**zonas**
```sql
CREATE TABLE zonas (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  nombre VARCHAR(100),
  descripcion VARCHAR(255),
  latitud DECIMAL(10, 6),
  longitud DECIMAL(10, 6),
  radio_km INT,
  fecha_creacion TIMESTAMP
);
```

---

## Logs y Debugging

### Ver logs en tiempo real

```bash
# Archivo
tail -f logs/ms-geolocalizacion.log

# Docker
docker logs -f ms-geolocalizacion
```

### Health Checks

```bash
curl http://localhost:8081/actuator/health
curl http://localhost:8081/actuator/health/db
```

---

## Troubleshooting

### Problema: "Invalid Latitude/Longitude"

**Solución:**
- Latitude debe estar entre -90 y 90
- Longitude debe estar entre -180 y 180
- Medellín: lat 6.209, lon -75.569

### Problema: Google Maps API Key inválida

**Solución:**
```bash
export GOOGLE_MAPS_API_KEY="your_valid_key"
```

---

## Performance

### Optimizaciones

- Índices espaciales en latitud/longitud
- Caché de cálculos recientes
- Queries optimizadas para búsquedas por radio

### Benchmarks Esperados

| Operación | Tiempo |
|-----------|--------|
| Crear ubicación | < 50ms |
| Calcular distancia | < 10ms |
| Buscar en radio 2km | < 200ms |

---

## Roadmap

### v1.0 (Actual)
- [x] CRUD de ubicaciones
- [x] Cálculos de distancia
- [x] Búsquedas por radio
- [x] Geofencing básico

### v2.0 (Planeado)
- [ ] Integración Google Maps
- [ ] Heat maps
- [ ] Route optimization
- [ ] Real-time tracking

---

## Recursos

- [Spring Boot Documentation](https://spring.io/projects/spring-boot)
- [Google Maps API](https://developers.google.com/maps)
- [Haversine Formula](https://en.wikipedia.org/wiki/Haversine_formula)

Documento creado: 2026-05-07
Versión: 1.0
Estado: Borrador
