# Guía de Uso del Arquetipo de Microservicios

Referencia rápida para usar el arquetipo Maven de Sanos y Salvos.

Ver [README.md](README.md) para documentación completa.

## Instalación Rápida

```bash
cd sanos-microservice-archetype
mvn clean install
```

## Generar Microservicio (Comando Rápido)

```bash
mvn archetype:generate \
  -DarchetypeGroupId=com.sanosysalvos \
  -DarchetypeArtifactId=sanos-microservice-archetype \
  -DarchetypeVersion=1.0.0 \
  -DgroupId=com.sanosysalvos \
  -DartifactId=ms-mi-servicio \
  -Dname="Mi Servicio" \
  -Ddescription="Descripcion del servicio" \
  -DdbName=sanosysalvos_mi_servicio \
  -DserverPort=8088 \
  -Dpackage=mi_servicio \
  -DinteractiveMode=false
```

## Parámetros Requeridos

| Parámetro | Descripción | Ejemplo |
|-----------|-------------|---------|
| `-DgroupId` | Grupo del proyecto | `com.sanosysalvos` |
| `-DartifactId` | ID del proyecto | `ms-mi-servicio` |
| `-Dname` | Nombre legible | `Mi Servicio` |
| `-Ddescription` | Descripción | `Descripcion del servicio` |
| `-DdbName` | Nombre base de datos | `sanosysalvos_mi_servicio` |
| `-DserverPort` | Puerto del servidor | `8088` |
| `-Dpackage` | Paquete Java | `mi_servicio` |

## Ejemplos

### Microservicio de Pagos
```bash
mvn archetype:generate \
  -DarchetypeGroupId=com.sanosysalvos \
  -DarchetypeArtifactId=sanos-microservice-archetype \
  -DarchetypeVersion=1.0.0 \
  -DgroupId=com.sanosysalvos \
  -DartifactId=ms-pagos \
  -Dname="Servicio de Pagos" \
  -Ddescription="Gestión de pagos y transacciones" \
  -DdbName=sanosysalvos_pagos \
  -DserverPort=8088 \
  -Dpackage=pagos \
  -DinteractiveMode=false
```

### Microservicio de Analytics
```bash
mvn archetype:generate \
  -DarchetypeGroupId=com.sanosysalvos \
  -DarchetypeArtifactId=sanos-microservice-archetype \
  -DarchetypeVersion=1.0.0 \
  -DgroupId=com.sanosysalvos \
  -DartifactId=ms-analytics \
  -Dname="Servicio de Analytics" \
  -Ddescription="Análisis de datos y reportes" \
  -DdbName=sanosysalvos_analytics \
  -DserverPort=8089 \
  -Dpackage=analytics \
  -DinteractiveMode=false
```

## Estructura Generada

```
ms-mi-servicio/
├── pom.xml
├── Dockerfile
├── README.md
├── .gitignore
├── mvnw / mvnw.cmd
└── src/
    ├── main/
    │   ├── java/com/sanosysalvos/mi_servicio/
    │   │   ├── MsMiServicioApplication.java
    │   │   ├── controller/
    │   │   ├── service/
    │   │   ├── repository/
    │   │   ├── model/
    │   │   ├── config/
    │   │   ├── dto/
    │   │   └── exception/
    │   └── resources/
    │       ├── application.properties
    │       ├── application-dev.properties
    │       ├── application-prod.properties
    │       └── application-docker.properties
    └── test/
│   │       └── application.properties
│   └── test/
│       └── java/
├── .gitignore
├── README.md
└── Dockerfile
```

## Características Incluidas

✅ Clase Application configurada con @SpringBootApplication
✅ Eureka Client automáticamente habilitado
✅ Configuración de MySQL predefinida
✅ Controller de Health Check
✅ application.properties configurado
✅ Dependencias de Spring Cloud, JPA, Resilience4j
✅ Build plugins configurados
✅ Gitignore y README

## Personalización

Después de generar un microservicio, puedes:

1. Añadir controllers adicionales en `controller/`
2. Crear modelos en `model/` o `entity/`
3. Crear servicios en `service/`
4. Configurar repositorios en `repository/`
5. Personalizar `application.properties`

## Integración con el Proyecto Principal

El archetype generado está configurado para ser hijo del pom.xml principal:

```xml
<parent>
    <groupId>com.sanosysalvos</groupId>
    <artifactId>sanos-y-salvos</artifactId>
    <version>1.0.0</version>
    <relativePath>../pom.xml</relativePath>
</parent>
```

Recuerda agregarlo al módulo principal en `pom.xml`:

```xml
<modules>
    <!-- ... otros módulos ... -->
    <module>ms-nuevo-servicio</module>
</modules>
```

## Ventajas del Archetype

1. **Consistencia**: Todos los microservicios tienen la misma estructura
2. **Reutilización**: Código base común para todos los servicios
3. **Rapidez**: Generar nuevos servicios en segundos
4. **Mantenibilidad**: Cambios en el archetype se pueden aplicar a nuevos servicios
5. **Mejores prácticas**: Incorpora estándares y buenas prácticas del equipo

## Versiones Soportadas

- Java 17+
- Spring Boot 3.5.12
- Maven 3.8.1+

## Después de Generar

### Compilar
```bash
cd ms-mi-servicio
mvn clean install
```

### Ejecutar
```bash
# Opción 1: Maven
mvn spring-boot:run

# Opción 2: Java
java -jar target/ms-mi-servicio-1.0-SNAPSHOT.jar
```

### Verificar
```bash
curl http://localhost:8088/health
```

**Respuesta esperada:**
```
Service is up
```

## Puertos Asignados

Mantén el registro de puertos asignados:

| Servicio | Puerto |
|----------|--------|
| Gateway/BFF | 8080 |
| Geolocalizacion | 8081 |
| Coincidencias | 8082 |
| Reportes | 8083 |
| Usuarios | 8084 |
| Notificaciones | 8085 |
| Proyectos | 8086 |
| Recursos Humanos | 8087 |
| Disponible | 8088+ |

## Troubleshooting

Si el comando genera error "Archetype not found":
```bash
# Asegurate de instalar primero
cd sanos-microservice-archetype
mvn clean install

# Verifica que se instaló en local
mvn archetype:list | grep sanos-microservice
```

Si el puerto está en uso:
```bash
# Windows
netstat -ano | findstr :8088

# Linux/Mac
lsof -i :8088
```

## Convenciones

- Nombres: ms-nombre-servicio
- Paquetes: com.sanosysalvos.nombre
- BD: sanosysalvos_nombre
- Puertos: 8088+

Ver [README.md](README.md) para documentación completa y ejemplos adicionales.
