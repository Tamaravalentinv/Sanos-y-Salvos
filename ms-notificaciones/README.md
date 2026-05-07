# Microservicio de Notificaciones

## Descripción

Microservicio responsable de enviar notificaciones a usuarios por múltiples canales: correo electrónico, SMS y notificaciones push. Gestiona eventos del sistema como reportes nuevos, coincidencias encontradas y actualizaciones de mascotas.

**Puerto:** 8085
**Base de Datos:** sanos_notificaciones
**Tecnología:** Spring Boot 3.x, MySQL, JavaMail, Twilio

---

## Requisitos Previos

- Java 17 o superior
- Maven 3.8+
- MySQL 8.0+
- Docker Desktop (opcional)
- Cuenta SendGrid o Gmail (para correos)
- Cuenta Twilio (para SMS - opcional)

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
cd ms-notificaciones
mvn clean install
```

### 2. Ejecutar en Desarrollo

```bash
# Opción 1: Con Maven
mvn spring-boot:run

# Opción 2: Con Java directamente
java -jar target/ms-notificaciones-1.0.0.jar

# Opción 3: IDE Spring Boot
# Click derecho en MsNotificacionesApplication.java → Run
```

El servicio estará disponible en: http://localhost:8085

### 3. Verificar Estado

```bash
curl http://localhost:8085/actuator/health
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
docker build -t sanos-notificaciones:1.0.0 .
```

### 2. Ejecutar Contenedor

```bash
docker run -d \
  -p 8085:8085 \
  -e SPRING_PROFILES_ACTIVE=docker \
  -e SPRING_DATASOURCE_URL=jdbc:mysql://mysql:3306/sanos_notificaciones \
  -e MAIL_USERNAME=your_email@gmail.com \
  -e MAIL_PASSWORD=your_app_password \
  --name ms-notificaciones \
  --network sanos-network \
  sanos-notificaciones:1.0.0
```

### 3. O usar Docker Compose (desde raíz del proyecto)

```bash
cd ..
docker-compose up -d ms-notificaciones
```

---

## Configuración

### Variables de Entorno

| Variable | Desarrollo | Producción |
|----------|-----------|-----------|
| `SPRING_PROFILES_ACTIVE` | `dev` | `prod` |
| `MAIL_HOST` | `smtp.gmail.com` | `smtp.sendgrid.net` |
| `MAIL_PORT` | `587` | `587` |
| `MAIL_USERNAME` | `your_email@gmail.com` | `apikey` |
| `MAIL_PASSWORD` | `app_password` | `sendgrid_api_key` |
| `MAIL_FROM` | `noreply@sanosysalvos.com` | `noreply@sanosysalvos.com` |

### application.properties

```properties
# Server
server.port=8085
spring.application.name=ms-notificaciones

# Database
spring.datasource.url=jdbc:mysql://localhost:3306/sanos_notificaciones
spring.datasource.username=root
spring.datasource.password=root
spring.jpa.hibernate.ddl-auto=update

# Mail Configuration
spring.mail.host=smtp.gmail.com
spring.mail.port=587
spring.mail.username=${MAIL_USERNAME}
spring.mail.password=${MAIL_PASSWORD}
spring.mail.properties.mail.smtp.auth=true
spring.mail.properties.mail.smtp.starttls.enable=true
spring.mail.properties.mail.smtp.starttls.required=true

# Custom Mail Settings
mail.from=noreply@sanosysalvos.com
mail.from.name=Sanos y Salvos

# SMS (Twilio - Opcional)
twilio.account.sid=${TWILIO_ACCOUNT_SID:}
twilio.auth.token=${TWILIO_AUTH_TOKEN:}
twilio.from.number=${TWILIO_FROM_NUMBER:}

# Logging
logging.level.root=INFO
logging.level.com.sanosysalvos=DEBUG
```

---

## Estructura del Código

```
ms-notificaciones/
├── controller/
│   └── NotificacionController.java        # Endpoints REST
├── service/
│   ├── NotificacionService.java           # Servicio principal
│   ├── EmailService.java                  # Envío de correos
│   ├── SmsService.java                    # Envío de SMS
│   └── PushNotificationService.java       # Notificaciones push
├── repository/
│   ├── NotificacionRepository.java        # Acceso a datos
│   └── EventoRepository.java              # Repositorio eventos
├── model/
│   ├── Notificacion.java                  # Entidad Notificacion
│   ├── Evento.java                        # Entidad Evento
│   └── Preferencia.java                   # Preferencias usuario
├── config/
│   ├── MailConfig.java                    # Configuración mail
│   └── DatabaseConfig.java                # Configuración BD
├── dto/
│   ├── NotificacionRequest.java           # Request DTO
│   └── NotificacionResponse.java          # Response DTO
└── MsNotificacionesApplication.java       # Main class
```

---

## API Endpoints

### 1. Enviar Notificación por Correo

```http
POST /api/notificaciones/email
Content-Type: application/json

{
  "usuarioId": 1,
  "email": "usuario@example.com",
  "asunto": "Coincidencia encontrada para tu mascota",
  "templateId": "coincidencia_encontrada",
  "parametros": {
    "nombreMascota": "Max",
    "porcentajeMatch": 87.5,
    "ciudad": "Medellín"
  }
}
```

**Respuesta:**
```json
{
  "id": 1,
  "usuarioId": 1,
  "tipo": "EMAIL",
  "estado": "ENVIADO",
  "email": "usuario@example.com",
  "asunto": "Coincidencia encontrada para tu mascota",
  "fechaEnvio": "2026-05-07T10:30:00",
  "mensaje": "Notificación enviada exitosamente"
}
```

### 2. Enviar Notificación por SMS

```http
POST /api/notificaciones/sms
Content-Type: application/json

{
  "usuarioId": 1,
  "telefono": "+573001234567",
  "mensaje": "Tu mascota Max tiene una coincidencia al 87.5%"
}
```

### 3. Enviar Notificación Push

```http
POST /api/notificaciones/push
Content-Type: application/json

{
  "usuarioId": 1,
  "titulo": "Coincidencia Encontrada",
  "cuerpo": "Max tiene una coincidencia al 87.5%",
  "imagenUrl": "https://...",
  "datos": {
    "reporteId": 1,
    "coincidenciaId": 5
  }
}
```

### 4. Obtener Notificaciones de Usuario

```http
GET /api/notificaciones/usuario/{usuarioId}?pagina=0&tamaño=10
```

**Respuesta:**
```json
{
  "content": [
    {
      "id": 1,
      "tipo": "EMAIL",
      "asunto": "Coincidencia encontrada",
      "estado": "LEIDO",
      "fechaEnvio": "2026-05-07T10:30:00",
      "fechaLectura": "2026-05-07T10:45:00"
    }
  ],
  "totalElements": 15,
  "totalPages": 2,
  "pagina": 0
}
```

### 5. Marcar Notificación como Leída

```http
PUT /api/notificaciones/{id}/marcar-leida
```

### 6. Obtener Preferencias de Notificación

```http
GET /api/notificaciones/preferencias/{usuarioId}
```

**Respuesta:**
```json
{
  "usuarioId": 1,
  "notificacionesEmail": true,
  "notificacionesSms": true,
  "notificacionesPush": true,
  "frecuenciaEmail": "INMEDIATO",
  "horarioNoMolestar": "22:00-08:00",
  "tiposNotificacion": [
    "COINCIDENCIA_ENCONTRADA",
    "REPORTE_NUEVO",
    "MENSAJE_RECIBIDO"
  ]
}
```

### 7. Actualizar Preferencias

```http
PUT /api/notificaciones/preferencias/{usuarioId}
Content-Type: application/json

{
  "notificacionesEmail": true,
  "notificacionesSms": false,
  "notificacionesPush": true,
  "frecuenciaEmail": "DIARIA",
  "horarioNoMolestar": "22:00-08:00"
}
```

---

## Templates de Correo

### Disponibles

| Template | Descripción | Destinatario |
|----------|-------------|-------------|
| `coincidencia_encontrada` | Nueva coincidencia | Dueño mascota |
| `reporte_nuevo` | Alerta reporte nuevo | Suscriptores zona |
| `mensaje_recibido` | Nuevo mensaje | Usuario |
| `bienvenida` | Bienvenida usuario | Nuevo usuario |
| `confirmacion_email` | Confirmación correo | Usuario |

---

## Eventos Soportados

El servicio escucha eventos del sistema:

| Evento | Descripción | Acción |
|--------|-------------|--------|
| `ReporteCreado` | Nuevo reporte publicado | Notificar suscriptores |
| `CoincidenciaEncontrada` | Coincidencia detectada | Notificar usuarios |
| `MensajeRecibido` | Nuevo mensaje | Notificar destinatario |
| `UsuarioRegistrado` | Nuevo usuario | Enviar bienvenida |

---

## Pruebas con cURL

### Enviar Correo
```bash
curl -X POST "http://localhost:8085/api/notificaciones/email" \
  -H "Content-Type: application/json" \
  -d '{
    "usuarioId": 1,
    "email": "usuario@example.com",
    "asunto": "Coincidencia encontrada",
    "templateId": "coincidencia_encontrada",
    "parametros": {
      "nombreMascota": "Max"
    }
  }'
```

### Obtener Notificaciones
```bash
curl -X GET "http://localhost:8085/api/notificaciones/usuario/1?pagina=0&tamaño=10"
```

### Marcar como Leída
```bash
curl -X PUT "http://localhost:8085/api/notificaciones/1/marcar-leida"
```

### Obtener Preferencias
```bash
curl -X GET "http://localhost:8085/api/notificaciones/preferencias/1"
```

---

## Base de Datos

### Tablas Principales

**notificaciones**
```sql
CREATE TABLE notificaciones (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  usuario_id BIGINT,
  tipo VARCHAR(50),
  asunto VARCHAR(255),
  cuerpo TEXT,
  email VARCHAR(100),
  telefono VARCHAR(20),
  estado VARCHAR(50),
  fecha_envio TIMESTAMP,
  fecha_lectura TIMESTAMP
);
```

**preferencias**
```sql
CREATE TABLE preferencias (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  usuario_id BIGINT UNIQUE,
  notificaciones_email BOOLEAN,
  notificaciones_sms BOOLEAN,
  notificaciones_push BOOLEAN,
  frecuencia VARCHAR(50)
);
```

---

## Logs y Debugging

### Ver logs en tiempo real

```bash
# Archivo
tail -f logs/ms-notificaciones.log

# Docker
docker logs -f ms-notificaciones
```

### Debugging Email

```properties
logging.level.org.springframework.mail=DEBUG
logging.level.org.springframework.mail.javamail=DEBUG
```

---

## Troubleshooting

### Problema: "Authentication failed" en Gmail

**Solución:**
1. Habilitar "Apps menos seguras" en Gmail
2. Generar "Contraseña de aplicación"
3. Usar contraseña de aplicación, no la de cuenta

```bash
export MAIL_PASSWORD="your_app_password"
```

### Problema: Email no se envía

**Solución:**
- Verificar credenciales
- Verificar firewall permite puerto 587
- Ver logs detallados con DEBUG

### Problema: SMS no llega

**Solución:**
- Verificar cuenta Twilio activa
- Verificar número teléfono en formato correcto
- Verificar saldo en cuenta Twilio

---

## Performance

### Consideraciones

- Envíos asíncronos para no bloquear respuesta
- Cola de correos pendientes
- Reintentos automáticos de fallos

### Benchmarks Esperados

| Operación | Tiempo |
|-----------|--------|
| Enviar correo | < 5s |
| Enviar SMS | < 2s |
| Guardar preferencias | < 100ms |

---

## Roadmap

### v1.0 (Actual)
- [x] Envío de correos
- [x] Envío de SMS (Twilio)
- [x] Notificaciones push
- [x] Preferencias usuario
- [x] Templates de email

### v2.0 (Planeado)
- [ ] Webhooks
- [ ] Notificaciones en tiempo real
- [ ] Analytics de notificaciones
- [ ] A/B testing templates

---

## Recursos

- [Spring Boot Mail Documentation](https://spring.io/guides/gs/sending-email/)
- [Twilio SMS API](https://www.twilio.com/docs/sms)
- [Gmail App Passwords](https://support.google.com/accounts/answer/185833)
- [Email Best Practices](https://litmus.com/blog)

Documento creado: 2026-05-07
Versión: 1.0
Estado: Borrador
