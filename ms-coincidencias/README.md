# Microservicio de Coincidencias (Matching Engine)

## Descripción

Microservicio responsable de identificar coincidencias entre reportes de mascotas perdidas y encontradas utilizando algoritmos de similitud basados en características físicas, ubicación y tipo de mascota.

**Puerto:** 8082
**Base de Datos:** sanos_coincidencias
**Tecnología:** Spring Boot 3.x, MySQL, Resilience4j

---

## Requisitos Previos

- Java 17 o superior
- Maven 3.8+
- MySQL 8.0+
- Docker Desktop (opcional, para ejecución containerizada)

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
cd ms-coincidencias
mvn clean install
```

### 2. Ejecutar en Desarrollo

```bash
# Opción 1: Con Maven
mvn spring-boot:run

# Opción 2: Con Java directamente
java -jar target/ms-coincidencias-1.0.0.jar

# Opción 3: IDE Spring Boot
# Click derecho en MsCoincidenciasApplication.java → Run
```

El servicio estará disponible en: http://localhost:8082

### 3. Verificar Estado

```bash
curl http://localhost:8082/actuator/health
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
docker build -t sanos-coincidencias:1.0.0 .
```

### 2. Ejecutar Contenedor

```bash
docker run -d \
  -p 8082:8082 \
  -e SPRING_PROFILES_ACTIVE=docker \
  -e SPRING_DATASOURCE_URL=jdbc:mysql://mysql:3306/sanos_coincidencias \
  -e SPRING_DATASOURCE_USERNAME=root \
  -e SPRING_DATASOURCE_PASSWORD=root \
  --name ms-coincidencias \
  --network sanos-network \
  sanos-coincidencias:1.0.0
```

### 3. O usar Docker Compose (desde raíz del proyecto)

```bash
cd ..
docker-compose up -d ms-coincidencias
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
| `RESILIENCE4J_CIRCUITBREAKER_ENABLED` | `false` | `true` |

### application.properties

```properties
# Server
server.port=8082
spring.application.name=ms-coincidencias

# Database
spring.datasource.url=jdbc:mysql://localhost:3306/sanos_coincidencias
spring.datasource.username=root
spring.datasource.password=root
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=false

# JPA
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQL8Dialect

# Resilience4j
resilience4j.circuitbreaker.instances.coincidenciasService.registerHealthIndicator=true
resilience4j.circuitbreaker.instances.coincidenciasService.slidingWindowSize=100
resilience4j.circuitbreaker.instances.coincidenciasService.failureRateThreshold=50.0

# Logging
logging.level.root=INFO
logging.level.com.sanosysalvos=DEBUG
```

---

## Estructura del Código

```
ms-coincidencias/
├── controller/
│   └── CoincidenciaController.java        # Endpoints REST
├── service/
│   ├── CoincidenciaService.java           # Lógica de coincidencias
│   └── MatchingAlgorithmService.java      # Algoritmo de similitud
├── repository/
│   └── CoincidenciaRepository.java        # Acceso a datos
├── model/
│   ├── Coincidencia.java                  # Entidad Coincidencia
│   └── MatchingResult.java                # Resultado del matching
├── config/
│   ├── Resilience4jConfig.java            # Circuit breaker
│   └── DatabaseConfig.java                # Configuración BD
├── dto/
│   ├── CoincidenciaRequest.java           # Request DTO
│   └── CoincidenciaResponse.java          # Response DTO
└── MsCoincidenciasApplication.java        # Main class
```

---

## API Endpoints

### 1. Obtener Coincidencias por Reporte

```http
GET /api/coincidencias?reporteId=1
```

**Parámetros:**
- `reporteId` (required): ID del reporte de búsqueda

**Respuesta:**
```json
{
  "reporteId": 1,
  "coincidencias": [
    {
      "id": 5,
      "reporteCoincidencia": 2,
      "porcentajeMatch": 87.5,
      "motivosCoincidencia": [
        "Raza coincide",
        "Color similar",
        "Ubicación cercana"
      ],
      "fecha": "2026-05-07T10:30:00"
    },
    {
      "id": 8,
      "reporteCoincidencia": 3,
      "porcentajeMatch": 75.2,
      "motivosCoincidencia": [
        "Especie coincide",
        "Tamaño similar"
      ],
      "fecha": "2026-05-07T09:15:00"
    }
  ],
  "mejorMatch": 87.5,
  "totalCoincidencias": 2
}
```

### 2. Calcular Coincidencia entre Dos Reportes

```http
POST /api/coincidencias/calcular
Content-Type: application/json

{
  "reporteId1": 1,
  "reporteId2": 2
}
```

**Respuesta:**
```json
{
  "reporte1": 1,
  "reporte2": 2,
  "porcentajeMatch": 87.5,
  "detalles": {
    "coincidenciaRaza": true,
    "coincidenciaColor": true,
    "distanciaKm": 2.5,
    "tiempoMinutos": 120,
    "diferenciaCaracteristicas": 1
  },
  "recomendacion": "ALTA_PROBABILIDAD"
}
```

### 3. Obtener Detalles de Coincidencia

```http
GET /api/coincidencias/{id}
```

**Respuesta:**
```json
{
  "id": 5,
  "reportePerdida": 1,
  "reporteEncontrada": 2,
  "porcentajeMatch": 87.5,
  "estado": "ACTIVO",
  "fechaCreacion": "2026-05-07T10:30:00",
  "fechaActualizacion": "2026-05-07T14:45:00",
  "motivosCoincidencia": [
    "Raza Golden Retriever coincide",
    "Color dorado similar",
    "Ubicación en radio 3km",
    "Tamaño grande (25-30kg)"
  ],
  "notasInternas": "Revisado por moderador"
}
```

### 4. Actualizar Estado de Coincidencia

```http
PUT /api/coincidencias/{id}
Content-Type: application/json

{
  "estado": "VERIFICADA",
  "notasInternas": "Mascotas encontradas - contactos unificados"
}
```

### 5. Eliminar Coincidencia

```http
DELETE /api/coincidencias/{id}
```

---

## Algoritmo de Matching

El servicio utiliza un algoritmo ponderado que considera:

| Factor | Peso | Descripción |
|--------|------|-------------|
| Raza | 25% | Coincidencia exacta de raza |
| Color | 20% | Similitud de color principal |
| Tamaño | 15% | Rango de peso compatible |
| Características | 15% | Marcas, cicatrices, etc. |
| Ubicación | 15% | Distancia en km entre reportes |
| Tiempo | 10% | Diferencia temporal entre reportes |

**Fórmula:**
```
Match% = (Raza*0.25 + Color*0.20 + Tamaño*0.15 + Características*0.15 + 
          Ubicación*0.15 + Tiempo*0.10) * 100
```

**Umbrales:**
- Menos de 50%: Sin relevancia
- 50-70%: Baja probabilidad
- 70-85%: Media probabilidad
- Mayor a 85%: Alta probabilidad

---

## Pruebas con cURL

### Obtener Coincidencias
```bash
curl -X GET "http://localhost:8082/api/coincidencias?reporteId=1" \
  -H "Content-Type: application/json"
```

### Calcular Coincidencia
```bash
curl -X POST "http://localhost:8082/api/coincidencias/calcular" \
  -H "Content-Type: application/json" \
  -d '{
    "reporteId1": 1,
    "reporteId2": 2
  }'
```

### Obtener Detalle
```bash
curl -X GET "http://localhost:8082/api/coincidencias/5" \
  -H "Content-Type: application/json"
```

### Actualizar Estado
```bash
curl -X PUT "http://localhost:8082/api/coincidencias/5" \
  -H "Content-Type: application/json" \
  -d '{
    "estado": "VERIFICADA",
    "notasInternas": "Contactos unificados"
  }'
```

---

## Logs y Debugging

### Ver logs en tiempo real

```bash
# Archivo
tail -f logs/ms-coincidencias.log

# Docker
docker logs -f ms-coincidencias
```

### Configurar nivel de log

**application.properties:**
```properties
logging.level.root=INFO
logging.level.com.sanosysalvos=DEBUG
logging.level.org.springframework.web=DEBUG
logging.level.org.hibernate.SQL=DEBUG
```

### Endpoints de Debugging

```bash
# Health check
curl http://localhost:8082/actuator/health

# Métricas
curl http://localhost:8082/actuator/metrics

# Circuit breaker status
curl http://localhost:8082/actuator/circuitbreakers

# Environment
curl http://localhost:8082/actuator/env
```

---

## Base de Datos

### Tablas Principales

**coincidencias**
```sql
CREATE TABLE coincidencias (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  reporte_perdida_id BIGINT NOT NULL,
  reporte_encontrada_id BIGINT NOT NULL,
  porcentaje_match DECIMAL(5,2),
  estado VARCHAR(20),
  motivos VARCHAR(500),
  fecha_creacion TIMESTAMP,
  fecha_actualizacion TIMESTAMP
);
```

### Inicialización

```bash
# La aplicación crea las tablas automáticamente con:
# spring.jpa.hibernate.ddl-auto=update
```

---

## Monitoreo y Métricas

### Prometheus Metrics

El servicio expone métricas en: `http://localhost:8082/actuator/prometheus`

Métricas clave:
- `coincidencias_calculadas_total` - Total de coincidencias calculadas
- `porcentaje_match_promedio` - Promedio de similitud
- `tiempo_procesamiento_ms` - Tiempo de cálculo

### Health Checks

```bash
curl http://localhost:8082/actuator/health/db
curl http://localhost:8082/actuator/health/circuitBreaker
```

---

## Troubleshooting

### Problema: "Connection refused" a base de datos

**Solución:**
```bash
# Verificar MySQL está ejecutándose
mysql -u root -p -e "SELECT VERSION();"

# O revisar en Docker
docker ps | grep mysql
```

### Problema: Circuit Breaker abierto

**Síntomas:** Errores con "CircuitBreakerOpenException"

**Solución:**
```bash
# Ver estado
curl http://localhost:8082/actuator/circuitbreakers

# Resetear (esperar timeout o reiniciar)
docker restart ms-coincidencias
```

### Problema: Out of Memory

**Solución:**
```bash
# Aumentar memoria JVM
docker run ... -e JAVA_OPTS="-Xmx512m -Xms256m" ...
```

---

## Performance

### Optimizaciones Implementadas

- Índices en reporteIds (búsquedas rápidas)
- Paginación en resultados
- Caché de resultados recientes
- Circuit breaker para fallos en cascade

### Benchmarks Esperados

| Operación | Tiempo |
|-----------|--------|
| Calcular 1 coincidencia | < 100ms |
| Obtener 10 coincidencias | < 200ms |
| Bulk matching (100 reportes) | < 5s |

---

## Roadmap

### v1.0 (Actual)
- [x] Algoritmo matching básico
- [x] API REST endpoints
- [x] Resilience4j circuit breaker
- [x] Docker support

### v2.0 (Planeado)
- [ ] Machine Learning para matching
- [ ] Webhook notifications
- [ ] Batch processing
- [ ] Advanced analytics

---

## Recursos

- [Spring Boot Documentation](https://spring.io/projects/spring-boot)
- [Resilience4j](https://resilience4j.readme.io/)
- [MySQL Connector/J](https://dev.mysql.com/doc/connector-j/)
- [REST API Best Practices](https://restfulapi.net/)

Documento creado: 2026-05-07
Versión: 1.0
Estado: Borrador
