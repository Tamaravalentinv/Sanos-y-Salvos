# Microservicio de Usuarios

## Descripción

Microservicio responsable de gestionar usuarios, autenticación, autorización y perfiles de usuario. Implementa Spring Security con JWT para autenticación segura y permite gestionar roles y permisos.

**Puerto:** 8084
**Base de Datos:** sanos_usuarios
**Tecnología:** Spring Boot 3.x, MySQL, Spring Security, JWT

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
cd ms-usuarios
mvn clean install
```

### 2. Ejecutar en Desarrollo

```bash
# Opción 1: Con Maven
mvn spring-boot:run

# Opción 2: Con Java directamente
java -jar target/ms-usuarios-1.0.0.jar

# Opción 3: IDE Spring Boot
# Click derecho en MsUsuariosApplication.java → Run
```

El servicio estará disponible en: http://localhost:8084

### 3. Verificar Estado

```bash
curl http://localhost:8084/actuator/health
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
docker build -t sanos-usuarios:1.0.0 .
```

### 2. Ejecutar Contenedor

```bash
docker run -d \
  -p 8084:8084 \
  -e SPRING_PROFILES_ACTIVE=docker \
  -e SPRING_DATASOURCE_URL=jdbc:mysql://mysql:3306/sanos_usuarios \
  -e JWT_SECRET=your_secret_key_min_256_bits \
  --name ms-usuarios \
  --network sanos-network \
  sanos-usuarios:1.0.0
```

### 3. O usar Docker Compose (desde raíz del proyecto)

```bash
cd ..
docker-compose up -d ms-usuarios
```

---

## Configuración

### Variables de Entorno

| Variable | Desarrollo | Producción |
|----------|-----------|-----------|
| `SPRING_PROFILES_ACTIVE` | `dev` | `prod` |
| `SPRING_DATASOURCE_URL` | `localhost:3306` | `mysql:3306` |
| `JWT_SECRET` | `dev_secret` | `prod_secret_256bits` |
| `JWT_EXPIRATION` | `86400000` | `3600000` |

### application.properties

```properties
# Server
server.port=8084
spring.application.name=ms-usuarios

# Database
spring.datasource.url=jdbc:mysql://localhost:3306/sanos_usuarios
spring.datasource.username=root
spring.datasource.password=root
spring.jpa.hibernate.ddl-auto=update

# JWT Configuration
jwt.secret=${JWT_SECRET:your_super_secret_key_with_at_least_256_bits}
jwt.expiration=${JWT_EXPIRATION:86400000}

# Security
spring.security.user.name=admin
spring.security.user.password=admin

# Logging
logging.level.root=INFO
logging.level.com.sanosysalvos=DEBUG
logging.level.org.springframework.security=DEBUG
```

---

## Estructura del Código

```
ms-usuarios/
├── controller/
│   ├── AuthController.java                # Endpoints de autenticación
│   └── UsuarioController.java             # Endpoints de usuarios
├── service/
│   ├── UsuarioService.java                # Servicio de usuarios
│   ├── AuthService.java                   # Servicio de autenticación
│   └── JwtService.java                    # Servicio JWT
├── repository/
│   ├── UsuarioRepository.java             # Acceso a usuarios
│   └── RoleRepository.java                # Acceso a roles
├── model/
│   ├── Usuario.java                       # Entidad Usuario
│   ├── Role.java                          # Entidad Role
│   └── Permiso.java                       # Entidad Permiso
├── security/
│   ├── JwtAuthenticationFilter.java       # Filtro JWT
│   ├── JwtProvider.java                   # Proveedor JWT
│   └── SecurityConfig.java                # Configuración seguridad
├── dto/
│   ├── LoginRequest.java                  # Request login
│   ├── LoginResponse.java                 # Response login
│   ├── UsuarioRequest.java                # Request usuario
│   └── UsuarioResponse.java               # Response usuario
├── config/
│   └── DatabaseConfig.java                # Configuración BD
└── MsUsuariosApplication.java             # Main class
```

---

## API Endpoints

### Autenticación

#### 1. Login

```http
POST /api/auth/login
Content-Type: application/json

{
  "email": "usuario@example.com",
  "password": "password123"
}
```

**Respuesta:**
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "tipo": "Bearer",
  "expiracion": 86400,
  "usuario": {
    "id": 1,
    "nombre": "Juan Pérez",
    "email": "usuario@example.com",
    "roles": ["ROLE_USER"]
  }
}
```

#### 2. Registrar Usuario

```http
POST /api/auth/registro
Content-Type: application/json

{
  "nombre": "Juan Pérez",
  "email": "juan@example.com",
  "password": "securePassword123",
  "telefono": "+573001234567"
}
```

**Respuesta:**
```json
{
  "id": 1,
  "nombre": "Juan Pérez",
  "email": "juan@example.com",
  "telefono": "+573001234567",
  "estado": "ACTIVO",
  "fechaRegistro": "2026-05-07T10:30:00",
  "mensaje": "Usuario registrado exitosamente"
}
```

#### 3. Validar Token

```http
POST /api/auth/validar-token
Content-Type: application/json

{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
}
```

**Respuesta:**
```json
{
  "valido": true,
  "usuarioId": 1,
  "email": "usuario@example.com",
  "roles": ["ROLE_USER"]
}
```

#### 4. Refresh Token

```http
POST /api/auth/refresh-token
Content-Type: application/json

{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
}
```

### Gestión de Usuarios

#### 1. Obtener Perfil

```http
GET /api/usuarios/perfil
Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
```

**Respuesta:**
```json
{
  "id": 1,
  "nombre": "Juan Pérez",
  "email": "juan@example.com",
  "telefono": "+573001234567",
  "ciudad": "Medellín",
  "estado": "ACTIVO",
  "roles": ["ROLE_USER"],
  "permisos": [
    "crear_reportes",
    "ver_reportes",
    "ver_coincidencias"
  ],
  "fechaRegistro": "2026-05-07T10:30:00",
  "ultimoAcceso": "2026-05-07T14:45:00"
}
```

#### 2. Actualizar Perfil

```http
PUT /api/usuarios/perfil
Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
Content-Type: application/json

{
  "nombre": "Juan Pérez García",
  "telefono": "+573009876543",
  "ciudad": "Medellín"
}
```

#### 3. Cambiar Contraseña

```http
POST /api/usuarios/cambiar-password
Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
Content-Type: application/json

{
  "passwordActual": "securePassword123",
  "passwordNueva": "newSecurePassword456"
}
```

#### 4. Obtener Usuario (Admin)

```http
GET /api/usuarios/{id}
Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
```

#### 5. Listar Usuarios (Admin)

```http
GET /api/usuarios?pagina=0&tamaño=10&estado=ACTIVO
Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
```

#### 6. Desactivar Usuario (Admin)

```http
PUT /api/usuarios/{id}/desactivar
Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
```

---

## Seguridad

### Roles y Permisos

| Rol | Descripción | Permisos |
|-----|-------------|----------|
| `ROLE_USER` | Usuario estándar | crear_reportes, ver_reportes, ver_coincidencias |
| `ROLE_MODERADOR` | Moderador | validar_coincidencias, marcar_resueltos |
| `ROLE_ADMIN` | Administrador | gestionar_usuarios, ver_estadisticas |

### Spring Security Configuration

```java
@Configuration
@EnableWebSecurity
public class SecurityConfig {
    
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) {
        http
            .csrf().disable()
            .authorizeHttpRequests()
                .requestMatchers("/api/auth/**").permitAll()
                .requestMatchers("/api/usuarios/**").authenticated()
                .requestMatchers("/api/admin/**").hasRole("ADMIN")
                .anyRequest().authenticated()
            .and()
            .addFilterBefore(jwtAuthenticationFilter(), 
                UsernamePasswordAuthenticationFilter.class);
        
        return http.build();
    }
}
```

### JWT Token Structure

```
Header:
{
  "alg": "HS256",
  "typ": "JWT"
}

Payload:
{
  "sub": "usuario@example.com",
  "userId": 1,
  "roles": ["ROLE_USER"],
  "iat": 1620000000,
  "exp": 1620086400
}

Signature:
HMACSHA256(base64UrlEncode(header) + "." + base64UrlEncode(payload), secret)
```

---

## Pruebas con cURL

### Login
```bash
curl -X POST "http://localhost:8084/api/auth/login" \
  -H "Content-Type: application/json" \
  -d '{
    "email": "usuario@example.com",
    "password": "password123"
  }'
```

### Registrar Usuario
```bash
curl -X POST "http://localhost:8084/api/auth/registro" \
  -H "Content-Type: application/json" \
  -d '{
    "nombre": "Juan Pérez",
    "email": "juan@example.com",
    "password": "securePassword123"
  }'
```

### Obtener Perfil
```bash
curl -X GET "http://localhost:8084/api/usuarios/perfil" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"
```

### Cambiar Contraseña
```bash
curl -X POST "http://localhost:8084/api/usuarios/cambiar-password" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "passwordActual": "securePassword123",
    "passwordNueva": "newPassword456"
  }'
```

---

## Base de Datos

### Tablas Principales

**usuarios**
```sql
CREATE TABLE usuarios (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  nombre VARCHAR(100),
  email VARCHAR(100) UNIQUE,
  password VARCHAR(255),
  telefono VARCHAR(20),
  ciudad VARCHAR(100),
  estado VARCHAR(20),
  fecha_registro TIMESTAMP,
  ultimo_acceso TIMESTAMP
);
```

**roles**
```sql
CREATE TABLE roles (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  nombre VARCHAR(50) UNIQUE,
  descripcion VARCHAR(255)
);
```

**usuario_roles**
```sql
CREATE TABLE usuario_roles (
  usuario_id BIGINT,
  role_id BIGINT,
  PRIMARY KEY (usuario_id, role_id),
  FOREIGN KEY (usuario_id) REFERENCES usuarios(id),
  FOREIGN KEY (role_id) REFERENCES roles(id)
);
```

---

## Logs y Debugging

### Ver logs en tiempo real

```bash
# Archivo
tail -f logs/ms-usuarios.log

# Docker
docker logs -f ms-usuarios
```

### Debug Security

```properties
logging.level.org.springframework.security=DEBUG
logging.level.org.springframework.security.web=DEBUG
```

---

## Troubleshooting

### Problema: "JWT signature does not match"

**Solución:**
- Verificar JWT_SECRET es el mismo en todas las instancias
- Regenerar token con nuevo secret

```bash
export JWT_SECRET="new_secure_secret_with_256_bits_minimum"
```

### Problema: "Token expired"

**Solución:**
- Token tiene validez limitada
- Usar endpoint /refresh-token para generar nuevo token

```bash
curl -X POST "http://localhost:8084/api/auth/refresh-token" \
  -H "Content-Type: application/json" \
  -d '{"token":"old_token"}'
```

### Problema: "Unauthorized" en endpoints protegidos

**Solución:**
- Incluir header Authorization con token
- Verificar token es válido y no ha expirado

```bash
curl -H "Authorization: Bearer YOUR_TOKEN" http://localhost:8084/api/usuarios/perfil
```

---

## Performance

### Consideraciones

- Caché de usuarios frecuentes
- Índice en email para búsquedas rápidas
- Token JWT no requiere consulta a BD en cada request

### Benchmarks Esperados

| Operación | Tiempo |
|-----------|--------|
| Login | < 500ms |
| Validar token | < 10ms |
| Registrar usuario | < 1s |

---

## Roadmap

### v1.0 (Actual)
- [x] Autenticación JWT
- [x] CRUD de usuarios
- [x] Gestión de roles
- [x] Validación de tokens

### v2.0 (Planeado)
- [ ] OAuth2 / OIDC
- [ ] Two-factor authentication
- [ ] Biometric login
- [ ] Social login integration

---

## Recursos

- [Spring Security Documentation](https://spring.io/projects/spring-security)
- [JWT Introduction](https://jwt.io/introduction)
- [OWASP Authentication Cheat Sheet](https://cheatsheetseries.owasp.org/cheatsheets/Authentication_Cheat_Sheet.html)
- [Password Storage Guidelines](https://cheatsheetseries.owasp.org/cheatsheets/Password_Storage_Cheat_Sheet.html)

Documento creado: 2026-05-07
Versión: 1.0
Estado: Borrador
