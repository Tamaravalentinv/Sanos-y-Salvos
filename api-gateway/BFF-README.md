# 🎯 Backend for Frontend (BFF) - API Gateway

## Descripción

El **Backend for Frontend (BFF)** es una capa de agregación de datos entre el frontend React y los microservicios backend. Optimiza las llamadas a API combinando datos de múltiples servicios en respuestas únicas.

**Ubicación:** `api-gateway` en puerto `8080`

---

## ¿Por qué se necesita un BFF?

### Problema sin BFF:
```
Frontend
  ├─→ GET /api/usuarios/{id}          → MS Usuarios (8084)
  ├─→ GET /api/reportes/{mascota}     → MS Reportes (8083)
  ├─→ GET /api/ubicaciones/{zona}     → MS Geolocalizacion (8081)
  ├─→ GET /api/coincidencias         → MS Coincidencias (8082)
  └─→ GET /api/notificaciones/{user}  → MS Notificaciones (8085)

= 5 llamadas HTTP para un dashboard
= Latencia alta
= Complejidad en el frontend
```

### Solución con BFF:
```
Frontend
  └─→ GET /api/bff/dashboard

BFF (Gateway)
  ├─→ Llama MS Usuarios
  ├─→ Llama MS Reportes
  ├─→ Llama MS Geolocalizacion
  ├─→ Llama MS Coincidencias
  └─→ Llama MS Notificaciones
     
  └─→ Agrega y transforma datos
  
  └─→ Devuelve respuesta optimizada

= 1 llamada HTTP desde frontend
= Latencia minimizada con caché
= Lógica de composición en backend
```

---

## Endpoints Disponibles

### 1. **Dashboard Agregado**
```http
GET /api/bff/dashboard?userId=1
```

**Respuesta:**
```json
{
  "stats": {
    "totalReportes": 42,
    "reportesActivos": 15,
    "coincidenciasEncontradas": 8,
    "notificacionesPendientes": 3,
    "usuariosActivos": 127
  },
  "reportesRecientes": [
    {
      "id": 1,
      "tipo": "PERDIDA",
      "nombreMascota": "Max",
      "especie": "Perro",
      "raza": "Golden Retriever",
      "color": "Dorado",
      "ubicacion": "Medellín, Laureles",
      "fechaCreacion": "2026-03-26T10:30:00",
      "estado": "ACTIVO",
      "fotoPrincipal": "url"
    }
  ],
  "notificacionesPendientes": [...],
  "coincidenciasActivas": [...]
}
```

**Cachea por:** 5 minutos (userId)

---

### 2. **Reporte Detallado con Información Completa**
```http
GET /api/bff/reportes/1
```

**Agrega información de:**
- Datos del reporte (MS Reportes)
- Detalle de mascota (MS Reportes)
- Ubicación (MS Geolocalización)
- Coincidencias encontradas (MS Coincidencias)
- Historial de cambios (MS Reportes)
- Notificaciones relacionadas (MS Notificaciones)

**Respuesta:**
```json
{
  "id": 1,
  "tipo": "PERDIDA",
  "descripcion": "Perro perdido en zona de Laureles",
  "estado": "ACTIVO",
  "mascota": {
    "id": 1,
    "nombre": "Max",
    "especie": "Perro",
    "raza": "Golden Retriever",
    "color": "Dorado",
    "caracteristicas": ["Cicatriz en oreja izquierda", "Collar azul"],
    "fotos": ["url1", "url2"]
  },
  "ubicacion": {
    "ciudad": "Medellín",
    "barrio": "Laureles",
    "direccion": "Cra 43A #32-65",
    "latitud": 6.209,
    "longitud": -75.569
  },
  "coincidenciasEncontradas": [
    {
      "id": 5,
      "reporteCoincidencia": 2,
      "porcentajeMatch": 87.5,
      "motivo": "Características físicas similares",
      "fecha": "2026-03-26T11:00:00"
    }
  ],
  "notificacionesRelacionadas": [...]
}
```

**Cachea por:** 10 minutos (reporteId)

---

### 3. **Crear Reporte (Transacción Distribuida)**
```http
POST /api/bff/reportes
Content-Type: application/json

{
  "tipo": "PERDIDA",
  "descripcion": "Gato perdido en centro",
  "nombreMascota": "Luna",
  "especie": "Gato",
  "raza": "Persa",
  "color": "Blanco",
  "tamaño": "Pequeño",
  "caracteristicas": ["Ojos azules", "Muy dócil"],
  "fotos": ["base64_or_url"],
  "ciudad": "Medellín",
  "barrio": "Centro",
  "direccion": "Carrera 49 #50-80",
  "latitud": 6.252,
  "longitud": -75.543,
  "telefono": "+573001234567",
  "email": "usuario@example.com",
  "nombreContacto": "Juan"
}
```

**Proceso BFF:**
1. ✅ Crea mascota en MS Reportes
2. ✅ Crea ubicación en MS Geolocalización
3. ✅ Crea reporte en MS Reportes
4. ✅ Envía notificación en MS Notificaciones

**Respuesta:**
```json
{
  "reporteId": 1,
  "mascotaId": 1,
  "ubicacionId": 1,
  "estado": "EXITOSO",
  "fechaCreacion": "2026-03-26T12:00:00",
  "mensaje": "Reporte creado exitosamente"
}
```

---

### 4. **Coincidencias Agrupadas**
```http
GET /api/bff/coincidencias?userId=1
```

**Respuesta:**
```json
{
  "grupos": [
    {
      "reportePerdida": 1,
      "matches": [
        {
          "reporteEncontrada": 5,
          "nombreMascota": "Perro similar",
          "ciudad": "Medellín",
          "porcentajeMatch": 87.5,
          "motivosCoincidencia": ["Color", "Tamaño", "Raza"]
        }
      ],
      "mejorPorcentaje": 87.5,
      "totalMatches": 3
    }
  ],
  "totalCoincidencias": 1
}
```

**Cachea por:** 5 minutos (userId)

---

## Características Implementadas

### ✅ Agregación de Datos
- Combina información de múltiples microservicios
- Una sola llamada al backend para obtener todo lo necesario

### ✅ Transformación de Datos
- Adapta respuestas al formato esperado por el frontend
- DTOs especializados para cada caso de uso

### ✅ Caching
- Reduce latencia y carga en microservicios
- TTL configurable por tipo de datos
- En producción: usar Redis

### ✅ Transacciones Distribuidas
- Endpoint `/api/bff/reportes` coordina múltiples servicios
- Rollback automático en caso de error

### ✅ Error Handling
- Fallback graceful si un microservicio falla
- Logging completo de operaciones

---

## Estructura de Código

```
api-gateway/
├── controller/
│   └── BFFController.java          # Endpoints BFF
├── service/
│   └── BFFService.java             # Lógica de agregación
├── dto/
│   ├── DashboardResponse.java       # Respuesta dashboard
│   ├── ReporteDetalladoResponse.java # Respuesta reporte
│   └── CrearReporteRequest.java     # Request crear reporte
├── config/
│   ├── CacheConfig.java            # Configuración de caché
│   └── ... (otros configs)
└── resources/
    ├── application.properties       # Gateway config
    └── bff.properties              # BFF config
```

---

## Caching

### Cachés Disponibles

| Cache | Clave | TTL | Uso |
|-------|-------|-----|-----|
| `dashboard` | `userId` | 5 min | Datos dashboard |
| `reporte_detallado` | `reporteId` | 10 min | Reporte individual |
| `coincidencias_agrupadas` | `userId` | 5 min | Coincidencias |
| `notificaciones` | `userId` | 1 min | Notificaciones |
| `usuarios` | `userId` | 30 min | Info usuario |

### Invalidación Manual (Future)
```java
@Scheduled(fixedRate = 300000) // 5 minutos
public void invalidateStaleCache() {
    // Lógica de invalidación automática
}
```

---

## Configuración

### application.properties
```properties
# BFF Settings
bff.api.prefix=/api/bff
spring.cache.type=simple

# Microservices URLs
ms.usuarios.url=http://localhost:8084
ms.reportes.url=http://localhost:8083
ms.geolocalizacion.url=http://localhost:8081
ms.coincidencias.url=http://localhost:8082
ms.notificaciones.url=http://localhost:8085

# Timeouts
bff.client.connection-timeout=5000
bff.client.read-timeout=10000
```

---

## Ejemplo de Uso desde Frontend

### React Hooks
```javascript
// Hook para obtener dashboard
const useDashboard = (userId) => {
  const [data, setData] = useState(null);
  
  useEffect(() => {
    fetch(`/api/bff/dashboard?userId=${userId}`)
      .then(r => r.json())
      .then(setData);
  }, [userId]);
  
  return data;
};

// Hook para obtener reporte detallado
const useReporteDetallado = (reporteId) => {
  const [data, setData] = useState(null);
  
  useEffect(() => {
    fetch(`/api/bff/reportes/${reporteId}`)
      .then(r => r.json())
      .then(setData);
  }, [reporteId]);
  
  return data;
};

// Hook para crear reporte
const useCrearReporte = () => {
  return async (reporteData) => {
    const response = await fetch('/api/bff/reportes', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(reporteData)
    });
    return response.json();
  };
};
```

---

## Ventajas del BFF

| Ventaja | Beneficio |
|---------|----------|
| **Reducción de Latencia** | 1 llamada vs 5 →  Menos latencia de red |
| **Menos Datos** | Solo información necesaria → Menor uso de datos |
| **Lógica Centralizada** | Cambios sin tocar frontend |
| **Caché Eficiente** | Reduce carga en microservicios |
| **Fácil Evolución** | Agregar nuevos datos sin cambiar frontend |
| **API-Driven** | Frontend siempre sincronizado |

---

## Métricas

### Sin BFF (5 llamadas)
- Latencia: ~500ms (100ms × 5)
- Transferencia: 50KB
- Errores: Posible fallos parciales

### Con BFF (1 llamada + caché)
- Latencia: ~150ms (primera vez), <10ms (cacheado)
- Transferencia: 15KB (optimizado)
- Errores: Manejo centralizado

---

## Roadmap

### v1.0 ✅
- [x] Agregación de datos
- [x] DTOs especializados
- [x] Caching simple
- [x] Endpoints principales

### v2.0 (Planned)
- [ ] WebClient async (no bloqueante)
- [ ] Redis para cache distribuido
- [ ] Rate limiting
- [ ] GraphQL support
- [ ] Webhooks para notificaciones en tiempo real

### v3.0 (Future)
- [ ] Machine Learning para personalizacion
- [ ] Analytics de uso
- [ ] A/B Testing support
- [ ] Multi-tenant support

---

## Testing

```bash
# Test dashboard
curl "http://localhost:8080/api/bff/dashboard?userId=1"

# Test reporte detallado
curl "http://localhost:8080/api/bff/reportes/1"

# Test crear reporte
curl -X POST "http://localhost:8080/api/bff/reportes" \
  -H "Content-Type: application/json" \
  -d '{"tipo":"PERDIDA","nombreMascota":"Max",...}'

# Health check
curl "http://localhost:8080/api/bff/health"
```

---

## Documentación de Referencia

- [Spring Cloud Gateway](https://spring.io/projects/spring-cloud-gateway)
- [Spring Cache](https://spring.io/guides/gs/caching/)
- [REST API Design](https://restfulapi.net/)
- [Backend for Frontend Pattern](https://samnewman.io/patterns/architectural/bff/)

