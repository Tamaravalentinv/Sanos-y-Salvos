# ${name} - Generated from Sanos y Salvos Microservice Archetype

## Descripción
${description}

## Configuración

### Variables de entorno
- `DB_USER`: Usuario de base de datos (default: root)
- `DB_PASSWORD`: Contraseña de base de datos

### Puerto
El microservicio corre en el puerto `${serverPort}` (configurable en `application.properties`)

### Base de Datos
- Nombre: `${dbName}`
- URL: `jdbc:mysql://localhost:3306/${dbName}`

## Ejecución

### Desarrollo
```bash
mvn spring-boot:run
```

### Compilación
```bash
mvn clean package
```

### Docker
```bash
docker build -t sanosysalvos/${artifactId}:1.0.0 .
docker run -p ${serverPort}:${serverPort} sanosysalvos/${artifactId}:1.0.0
```

## Endpoints Disponibles

### Health Check
- `GET /api/health` - Verificar que el servicio está activo
- `GET /api/health/status` - Obtener estado del servicio

## Integración con Eureka
Este microservicio se registra automáticamente en Eureka Discovery Server.

## Logging
- Nivel de log: DEBUG para `com.sanosysalvos.${package}`
- Nivel general: INFO

## Dependencias Principales
- Spring Boot 3.5.12
- Spring Data JPA
- MySQL Connector
- Spring Cloud Netflix Eureka
- Resilience4j

## Estructura del Proyecto
```
src/
├── main/
│   ├── java/
│   │   └── com/sanosysalvos/${package}/
│   │       ├── ${capitalizedName}Application.java
│   │       └── controller/
│   │           └── HealthController.java
│   └── resources/
│       └── application.properties
└── test/
    └── java/
```

## Para más información
Ver documentación en: https://github.com/sanosysalvos/Sanos-y-Salvos
