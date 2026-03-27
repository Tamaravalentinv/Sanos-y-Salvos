# Sanos y Salvos Microservice Archetype

Archetype Maven personalizado para generar microservicios reutilizables en el proyecto Sanos y Salvos.

## ¿Qué es?

Un Maven Archetype es una plantilla de proyecto que permite generar nuevos proyectos con una estructura predefinida, configuración y código base.

Este archetype está diseñado para crear microservicios Spring Boot que:
- Se integran con Eureka Discovery
- Usan Spring Data JPA para acceso a datos
- Incluyen configuración de MySQL
- Tienen endpoints de health check
- Siguen estándares del proyecto

## Instalación del Archetype

### Opción 1: Instalar localmente
```bash
# Desde la carpeta del archetype
cd sanos-microservice-archetype
mvn clean install
```

### Opción 2: Instalar en repositorio corporativo
```bash
mvn deploy
```

## Uso

### Generar un nuevo microservicio

```bash
mvn archetype:generate \
  -DarchetypeGroupId=com.sanosysalvos \
  -DarchetypeArtifactId=sanos-microservice-archetype \
  -DarchetypeVersion=1.0.0 \
  -DgroupId=com.sanosysalvos \
  -DartifactId=ms-nuevo-servicio \
  -Dname="Nuevo Microservicio" \
  -Ddescription="Descripción del nuevo microservicio" \
  -DdbName=sanosysalvos_nuevo \
  -DserverPort=8086 \
  -Dpackage=nuevo
```

### Parámetros:
- `name`: Nombre del microservicio (ej: "Servicio de Pagos")
- `description`: Descripción del servicio
- `dbName`: Nombre de la base de datos MySQL (ej: sanosysalvos_pagos)
- `serverPort`: Puerto donde corre el servicio (ej: 8086)
- `package`: Nombre del paquete Java (ej: pagos)

## Ejemplos de Generación

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
  -DserverPort=8086 \
  -Dpackage=pagos
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
  -DserverPort=8087 \
  -Dpackage=analytics
```

## Estructura Generada

Cada proyecto creado con este archetype tendrá:

```
ms-nuevo-servicio/
├── pom.xml
├── src/
│   ├── main/
│   │   ├── java/com/sanosysalvos/nuevo/
│   │   │   ├── NuevoApplication.java
│   │   │   └── controller/
│   │   │       └── HealthController.java
│   │   └── resources/
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


