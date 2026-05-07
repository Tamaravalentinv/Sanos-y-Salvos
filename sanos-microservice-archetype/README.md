# Arquetipo Maven de Microservicios Sanos y Salvos

## Descripción

Arquetipo Maven personalizado que proporciona una plantilla base para generar nuevos microservicios Spring Boot que se integran con la arquitectura de Sanos y Salvos.

El arquetipo incluye:
- Configuración de Spring Boot 3.x
- Integración con Spring Data JPA y MySQL
- Configuración de Eureka Discovery
- Circuit Breaker con Resilience4j
- Health checks automáticos
- Estructura de carpetas estandarizada
- Configuración de bases de datos
- Dockerización automática

**Ubicación:** `sanos-microservice-archetype/`
**Versión:** 1.0.0
**Grupo:** com.sanosysalvos

---

## Requisitos Previos

- Java 17 o superior
- Maven 3.8+
- Git
- Docker Desktop (opcional)

Verifica tu entorno:
```bash
java -version
mvn -version
git --version
```

---

## Instalación del Arquetipo

### Opción 1: Instalar Localmente

Instala el arquetipo en tu repositorio Maven local:

```bash
# Desde el directorio raíz del proyecto
cd sanos-microservice-archetype
mvn clean install
```

**Resultado:**
```
Building jar: .../sanos-microservice-archetype-1.0.0.jar
[INFO] BUILD SUCCESS
```

### Opción 2: Instalar en Repositorio Corporativo

Si tu organización tiene un repositorio Maven privado:

```bash
mvn clean deploy
```

---

## Uso del Arquetipo

### Generar Nuevo Microservicio

Ejecuta el comando Maven archetype:generate desde cualquier directorio:

```bash
mvn archetype:generate \
  -DarchetypeGroupId=com.sanosysalvos \
  -DarchetypeArtifactId=sanos-microservice-archetype \
  -DarchetypeVersion=1.0.0 \
  -DgroupId=com.sanosysalvos \
  -DartifactId=ms-nombre-servicio \
  -Dname="Nombre del Servicio" \
  -Ddescription="Descripción del servicio" \
  -DdbName=sanosysalvos_nombre \
  -DserverPort=8088 \
  -Dpackage=nombre \
  -DinteractiveMode=false
```

### Parámetros Disponibles

| Parámetro | Requerido | Ejemplo | Descripción |
|-----------|-----------|---------|-------------|
| `archetypeGroupId` | SI | `com.sanosysalvos` | Grupo del arquetipo |
| `archetypeArtifactId` | SI | `sanos-microservice-archetype` | ID del arquetipo |
| `archetypeVersion` | SI | `1.0.0` | Versión del arquetipo |
| `groupId` | SI | `com.sanosysalvos` | Grupo del nuevo proyecto |
| `artifactId` | SI | `ms-nuevo-servicio` | ID del nuevo proyecto |
| `name` | SI | `Nuevo Microservicio` | Nombre del servicio |
| `description` | SI | `Descripción del servicio` | Descripción |
| `dbName` | SI | `sanosysalvos_nuevo` | Nombre BD (sin prefijo `jdbc`) |
| `serverPort` | SI | `8088` | Puerto del servidor |
| `package` | SI | `nuevo` | Nombre del paquete Java |
| `version` | NO | `1.0.0` | Versión del proyecto (default: 1.0-SNAPSHOT) |

---

## Ejemplos de Generación

### Ejemplo 1: Microservicio de Pagos

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

**Resultado generado:**
```
ms-pagos/
├── pom.xml
├── Dockerfile
├── README.md
├── .gitignore
└── src/
    ├── main/
    │   ├── java/com/sanosysalvos/pagos/
    │   │   ├── MsPagosApplication.java
    │   │   ├── controller/
    │   │   │   └── HealthController.java
    │   │   ├── service/
    │   │   ├── repository/
    │   │   └── model/
    │   └── resources/
    │       └── application.properties
    └── test/
        └── java/com/sanosysalvos/pagos/
```

### Ejemplo 2: Microservicio de Auditoría

```bash
mvn archetype:generate \
  -DarchetypeGroupId=com.sanosysalvos \
  -DarchetypeArtifactId=sanos-microservice-archetype \
  -DarchetypeVersion=1.0.0 \
  -DgroupId=com.sanosysalvos \
  -DartifactId=ms-auditoria \
  -Dname="Servicio de Auditoría" \
  -Ddescription="Registro y auditoría de eventos" \
  -DdbName=sanosysalvos_auditoria \
  -DserverPort=8089 \
  -Dpackage=auditoria \
  -DinteractiveMode=false
```

### Ejemplo 3: Microservicio de Validación

```bash
mvn archetype:generate \
  -DarchetypeGroupId=com.sanosysalvos \
  -DarchetypeArtifactId=sanos-microservice-archetype \
  -DarchetypeVersion=1.0.0 \
  -DgroupId=com.sanosysalvos \
  -DartifactId=ms-validacion \
  -Dname="Servicio de Validación" \
  -Ddescription="Validación de datos y reglas de negocio" \
  -DdbName=sanosysalvos_validacion \
  -DserverPort=8090 \
  -Dpackage=validacion \
  -DinteractiveMode=false
```

---

## Estructura Generada

Cada microservicio generado sigue esta estructura estándar:

### Estructura de Carpetas

```
ms-nuevo-servicio/
│
├── pom.xml
│   Dependencias Maven (Spring Boot, JPA, MySQL, Resilience4j)
│
├── Dockerfile
│   Configuración para containerización
│
├── README.md
│   Documentación del microservicio
│
├── .gitignore
│   Archivos ignorados por Git
│
├── mvnw / mvnw.cmd
│   Maven wrapper (portabilidad entre sistemas)
│
└── src/
    │
    ├── main/
    │   │
    │   ├── java/com/sanosysalvos/nombre/
    │   │   │
    │   │   ├── MsNombreApplication.java
    │   │   │   Main class con @SpringBootApplication
    │   │   │
    │   │   ├── controller/
    │   │   │   ├── HealthController.java
    │   │   │   └── (Tus controladores aquí)
    │   │   │
    │   │   ├── service/
    │   │   │   └── (Tus servicios aquí)
    │   │   │
    │   │   ├── repository/
    │   │   │   └── (Tus interfaces Repository aquí)
    │   │   │
    │   │   ├── model/
    │   │   │   └── (Tus entidades JPA aquí)
    │   │   │
    │   │   ├── config/
    │   │   │   └── (Configuraciones Spring)
    │   │   │
    │   │   ├── dto/
    │   │   │   └── (Data Transfer Objects)
    │   │   │
    │   │   └── exception/
    │   │       └── (Excepciones personalizadas)
    │   │
    │   └── resources/
    │       ├── application.properties
    │       │   Configuración base
    │       │
    │       ├── application-dev.properties
    │       │   Configuración desarrollo
    │       │
    │       ├── application-prod.properties
    │       │   Configuración producción
    │       │
    │       └── application-docker.properties
    │           Configuración Docker
    │
    └── test/
        └── java/com/sanosysalvos/nombre/
            └── MsNombreApplicationTests.java
```

### Archivos Generados Clave

**pom.xml**
```xml
- Dependencia parent: sanos-y-salvos
- Spring Boot Starter Web
- Spring Data JPA
- MySQL Connector
- Eureka Client
- Resilience4j
- Lombok (opcional)
- Testing dependencies
```

**MsNombreApplication.java**
```java
@SpringBootApplication
@EnableEurekaClient
public class MsNombreApplication {
    public static void main(String[] args) {
        SpringApplication.run(MsNombreApplication.class, args);
    }
}
```

**application.properties**
```properties
server.port=8088
spring.application.name=ms-nombre-servicio
spring.datasource.url=jdbc:mysql://localhost:3306/sanosysalvos_nombre
spring.jpa.hibernate.ddl-auto=update
```

**HealthController.java**
```java
@RestController
@RequestMapping("/health")
public class HealthController {
    @GetMapping
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("Service is up");
    }
}
```

---

## Paso a Paso: Generar e Instalar Nuevo Microservicio

### Paso 1: Generar Proyecto

```bash
# Desde directorio de trabajo
mvn archetype:generate \
  -DarchetypeGroupId=com.sanosysalvos \
  -DarchetypeArtifactId=sanos-microservice-archetype \
  -DarchetypeVersion=1.0.0 \
  -DgroupId=com.sanosysalvos \
  -DartifactId=ms-mi-servicio \
  -Dname="Mi Servicio" \
  -Ddescription="Descripcion de mi servicio" \
  -DdbName=sanosysalvos_mi_servicio \
  -DserverPort=8088 \
  -Dpackage=mi_servicio \
  -DinteractiveMode=false
```

### Paso 2: Acceder al Proyecto

```bash
cd ms-mi-servicio
```

### Paso 3: Verificar Estructura

```bash
tree src/
# o
dir /s src\
```

### Paso 4: Compilar

```bash
mvn clean install
```

### Paso 5: Ejecutar

```bash
# Con Maven
mvn spring-boot:run

# O con Java
java -jar target/ms-mi-servicio-1.0-SNAPSHOT.jar
```

### Paso 6: Verificar Salud

```bash
curl http://localhost:8088/health
```

**Respuesta esperada:**
```
Service is up
```

---

## Configuración Automática Incluida

### Spring Boot

- Versión: 3.x (definida en pom.xml padre)
- Servidor: Tomcat (puerto configurable)
- Logging: SLF4J con Logback

### Base de Datos

- Motor: MySQL 8.0+
- Conexión: JDBC
- ORM: Spring Data JPA
- Migración: Hibernate (ddl-auto=update)

### Eureka Discovery

- Client automático
- Registro en servidor central
- Health checks periódicos

### Resilience4j

- Circuit Breaker
- Retry policies
- Fallback mechanisms

### Monitoreo

- Actuator endpoints
- Health checks
- Metrics collection

---

## Personalización Después de Generación

### Agregar Nuevas Dependencias

Edita `pom.xml` en tu proyecto generado:

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-security</artifactId>
</dependency>
```

Luego ejecuta:
```bash
mvn clean install
```

### Crear Nuevos Controllers

```bash
# Opción 1: Crear archivo directamente
# src/main/java/com/sanosysalvos/mi_servicio/controller/MiController.java

@RestController
@RequestMapping("/api/mi-recurso")
public class MiController {
    @GetMapping
    public ResponseEntity<List<?>> obtener() {
        return ResponseEntity.ok(new ArrayList<>());
    }
}
```

### Crear Entidades JPA

```bash
# src/main/java/com/sanosysalvos/mi_servicio/model/MiEntidad.java

@Entity
@Table(name = "mi_entidad")
@Data
public class MiEntidad {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String nombre;
}
```

---

## Dockerización

Cada microservicio generado incluye `Dockerfile`:

### Construir Imagen

```bash
docker build -t sanos-mi-servicio:1.0.0 .
```

### Ejecutar Contenedor

```bash
docker run -d \
  -p 8088:8088 \
  -e SPRING_PROFILES_ACTIVE=docker \
  --name ms-mi-servicio \
  --network sanos-network \
  sanos-mi-servicio:1.0.0
```

---

## Troubleshooting

### Problema: "Archetype not found"

**Causa:** El arquetipo no está instalado localmente

**Solución:**
```bash
# Instalar primero
cd sanos-microservice-archetype
mvn clean install

# Luego generar proyecto
mvn archetype:generate ...
```

### Problema: "Invalid groupId or artifactId"

**Causa:** Parámetros invalidos

**Solución:**
- groupId y artifactId deben contener solo letras, números, guiones
- Ejemplo correcto: `ms-nuevo-servicio` (no `ms_nuevo_servicio`)

### Problema: "Parent project not found"

**Causa:** Archivo pom.xml padre no está disponible

**Solución:**
```bash
# Asegurate que el pom.xml raíz está instalado
cd .. (raíz del proyecto)
mvn clean install

# Luego intenta generar de nuevo
```

### Problema: Puerto ya está en uso

**Solución:**
- Usar puerto diferente: `-DserverPort=8089`
- O terminar proceso que está usando el puerto

```bash
# Windows
netstat -ano | findstr :8088
taskkill /PID <PID> /F

# Linux/Mac
lsof -i :8088
kill -9 <PID>
```

---

## Checklist de Validación

Después de generar un nuevo microservicio, verifica:

- [ ] Proyecto generado correctamente
- [ ] `mvn clean install` ejecuta sin errores
- [ ] Health endpoint accesible: `curl http://localhost:PORT/health`
- [ ] Base de datos se crea automáticamente
- [ ] Dockerfile está presente
- [ ] README.md incluye instrucciones
- [ ] .gitignore está configurado

---

## Convenciones del Proyecto

### Nombres de Artefactos

```
Formato: ms-nombre-servicio
Ejemplos:
- ms-pagos
- ms-auditoria
- ms-validacion
- ms-notificaciones
- ms-reportes
```

### Nombres de Paquetes

```
Formato: com.sanosysalvos.nombre
Ejemplos:
- com.sanosysalvos.pagos
- com.sanosysalvos.auditoria
- com.sanosysalvos.validacion
```

### Nombres de Bases de Datos

```
Formato: sanosysalvos_nombre
Ejemplos:
- sanosysalvos_pagos
- sanosysalvos_auditoria
- sanosysalvos_validacion
```

### Puertos

```
Rango: 8081-8100
Asignados:
- 8080: API Gateway (BFF)
- 8081: Geolocalizacion
- 8082: Coincidencias
- 8083: Reportes
- 8084: Usuarios
- 8085: Notificaciones
- 8086: Proyectos
- 8087: Recursos Humanos
- 8088+: Nuevos servicios
```

---

## Integración con Proyecto Existente

### Agregar Nuevo Microservicio al Repo

```bash
# 1. Generar microservicio
mvn archetype:generate \
  -DarchetypeGroupId=com.sanosysalvos \
  -DarchetypeArtifactId=sanos-microservice-archetype \
  -DarchetypeVersion=1.0.0 \
  -DgroupId=com.sanosysalvos \
  -DartifactId=ms-nuevo \
  -Dname="Nuevo Servicio" \
  -Ddescription="Descripcion" \
  -DdbName=sanosysalvos_nuevo \
  -DserverPort=8088 \
  -Dpackage=nuevo

# 2. Mover a carpeta del proyecto
mv ms-nuevo /ruta/del/proyecto/

# 3. Actualizar docker-compose.yml
# Agregar servicio nuevo

# 4. Actualizar pom.xml padre
# Agregar modulo:
# <module>ms-nuevo</module>

# 5. Construir todo
cd /ruta/del/proyecto/
mvn clean install

# 6. Confirmar cambios
git add .
git commit -m "feat: add new microservice ms-nuevo"
```

---

## Recursos

- [Maven Archetype Documentation](https://maven.apache.org/plugins/maven-archetype-plugin/)
- [Spring Boot Documentation](https://spring.io/projects/spring-boot)
- [Spring Data JPA Guide](https://spring.io/guides/gs/accessing-data-jpa/)
- [Eureka Service Discovery](https://cloud.spring.io/spring-cloud-netflix/)
- [Resilience4j Circuit Breaker](https://resilience4j.readme.io/)

---

## Próximos Pasos

1. Instalar el arquetipo: `cd sanos-microservice-archetype && mvn clean install`
2. Generar tu primer microservicio con los parámetros requeridos
3. Implementar tus controladores, servicios y modelos
4. Agregar tests unitarios
5. Dockerizar y desplegar

---

Documento creado: 2026-05-07
Versión: 1.0
Estado: Borrador
