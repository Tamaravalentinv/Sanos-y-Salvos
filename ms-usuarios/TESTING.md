# Pruebas Unitarias - MS Usuarios

## Descripción
Este documento describe las pruebas unitarias implementadas para el microservicio MS Usuarios. El objetivo es alcanzar una cobertura de código mínima del **60%** en todos los componentes del sistema.

## Estructura de Pruebas

Las pruebas están organizadas en los siguientes directorios:

```
src/test/java/com/sanosysalvos/ms_usuarios/
├── controller/          # Pruebas de controladores REST
│   ├── UsuarioControllerTest.java
│   ├── OrganizacionControllerTest.java
│   └── AuthControllerTest.java
├── service/             # Pruebas de servicios de negocio
│   ├── UsuarioServiceTest.java
│   ├── OrganizacionServiceTest.java
│   └── RolServiceTest.java
├── security/            # Pruebas de utilidades de seguridad
│   └── JwtUtilTest.java
└── model/               # Pruebas de modelos de entidad
    ├── UsuarioTest.java
    ├── RolTest.java
    └── OrganizacionTest.java
```

## Clases de Prueba

### 1. **UsuarioServiceTest** (25 pruebas)
Pruebas exhaustivas del servicio de usuarios:
- Creación de usuarios con validaciones
- Búsqueda por ID, username y email
- Actualización de información de usuario
- Eliminación de usuarios
- Validación de contraseñas
- Actualización de fecha de última conexión

**Cobertura esperada:** ~90% de UsuarioService

### 2. **RolServiceTest** (7 pruebas)
Pruebas del servicio de roles:
- Creación de roles
- Búsqueda de roles
- Validación de duplicación
- Eliminación de roles

**Cobertura esperada:** ~95% de RolService

### 3. **OrganizacionServiceTest** (15 pruebas)
Pruebas del servicio de organizaciones:
- Creación con validación de tipo
- Búsqueda por múltiples criterios
- Actualización y verificación
- Eliminación de organizaciones

**Cobertura esperada:** ~90% de OrganizacionService

### 4. **UsuarioControllerTest** (13 pruebas)
Pruebas de endpoints REST para usuarios:
- Registro de usuarios (POST /users/register)
- Obtención de usuarios (GET /users/{id})
- Búsqueda por username y email
- Actualización de usuarios (PUT /users/{id})
- Eliminación de usuarios (DELETE /users/{id})
- Actualización de último acceso

**Cobertura esperada:** ~85% de UsuarioController

### 5. **OrganizacionControllerTest** (14 pruebas)
Pruebas de endpoints REST para organizaciones:
- CRUD completo de organizaciones
- Búsqueda por diferentes criterios
- Verificación de organizaciones
- Manejo de errores

**Cobertura esperada:** ~85% de OrganizacionController

### 6. **AuthControllerTest** (10 pruebas)
Pruebas de endpoints de autenticación:
- Login exitoso y con errores
- Registro de nuevos usuarios
- Obtención de información del usuario autenticado
- Validación de tokens JWT
- Logout

**Cobertura esperada:** ~90% de AuthController

### 7. **JwtUtilTest** (10 pruebas)
Pruebas de utilidad JWT:
- Generación de tokens
- Extracción de datos del token
- Validación de tokens
- Rechazo de tokens inválidos
- Tokens manipulados

**Cobertura esperada:** ~95% de JwtUtil

### 8. **Pruebas de Modelos** (~25 pruebas)
Pruebas de getters/setters de los modelos:
- UsuarioTest
- RolTest
- OrganizacionTest

**Cobertura esperada:** ~100% de getters/setters

## Total de Pruebas: ~109 pruebas unitarias

## Ejecución de Pruebas

### Ejecutar todas las pruebas:
```bash
mvn clean test
```

### Ejecutar una clase de prueba específica:
```bash
mvn test -Dtest=UsuarioServiceTest
```

### Ejecutar con reporte de cobertura:
```bash
mvn clean test jacoco:report
```

El reporte de cobertura se genera en: `target/site/jacoco/index.html`

## Herramientas y Dependencias

- **JUnit 5**: Framework de pruebas
- **Mockito**: Mock de dependencias
- **MockMvc**: Pruebas de controladores Spring
- **JaCoCo**: Análisis de cobertura de código
- **H2 Database**: Base de datos en memoria para pruebas

## Configuración de Cobertura

En el `pom.xml` se ha configurado JaCoCo con la siguiente regla:

- **Mínimo de cobertura:** 60% (LINE COVERAGE)
- **Exclusiones:** Clases de prueba
- **Elemento:** Paquetes del proyecto

La configuración valida automáticamente que se alcance el 60% de cobertura en cada ejecución de `mvn clean test`.

## Patrones de Prueba Utilizados

### 1. **AAA Pattern (Arrange-Act-Assert)**
Cada prueba sigue la estructura:
- **Arrange**: Preparar datos de prueba
- **Act**: Ejecutar la funcionalidad a probar
- **Assert**: Verificar los resultados

### 2. **Mocking con Mockito**
Se utilizan mocks para aislar la lógica bajo prueba:
```java
@Mock
private UsuarioRepository usuarioRepository;

@InjectMocks
private UsuarioService usuarioService;
```

### 3. **Testing de Controladores con MockMvc**
Pruebas de endpoints REST sin levantar servidor:
```java
mockMvc.perform(get("/users/1"))
    .andExpect(status().isOk())
    .andExpect(jsonPath("$.username", equalTo("testuser")));
```

## Casos de Prueba Clave

### Validaciones
- Validación de campos únicos (username, email)
- Validación de existencia de recursos
- Validación de credenciales
- Validación de tokens JWT

### Casos de Error
- Recurso no encontrado (404)
- Datos inválidos (400)
- No autorizado (401)
- Conflicto de datos (409)

### Casos Exitosos
- Creación de recursos
- Modificación de recursos
- Eliminación de recursos
- Búsqueda de recursos

## Métricas Esperadas

| Componente | Líneas | Cobertura Esperada |
|-----------|--------|-------------------|
| UsuarioService | ~120 | 90% |
| OrganizacionService | ~100 | 90% |
| RolService | ~50 | 95% |
| UsuarioController | ~80 | 85% |
| OrganizacionController | ~90 | 85% |
| AuthController | ~100 | 90% |
| JwtUtil | ~80 | 95% |
| **Total** | **~620** | **~60%** |

## Próximos Pasos

1. Ejecutar: `mvn clean test`
2. Revisar reporte en: `target/site/jacoco/index.html`
3. Implementar pruebas adicionales según sea necesario
4. Mantener cobertura >= 60% en futuras modificaciones

## Notas Importantes

- Las pruebas usan `@SpringBootTest` para contexto completo de Spring
- Se utilizan `@MockBean` para mock de servicios
- Se configura `application-test.properties` si es necesario
- Las pruebas son independientes y pueden ejecutarse en cualquier orden
- Se recomenda ejecutar pruebas localmente antes de commit

## Recursos

- [JUnit 5 Documentation](https://junit.org/junit5/docs/current/user-guide/)
- [Mockito Documentation](https://javadoc.io/doc/org.mockito/mockito-core/latest/org/mockito/Mockito.html)
- [Spring Boot Test Documentation](https://spring.io/guides/gs/testing-web/)
- [JaCoCo Plugin Documentation](https://www.jacoco.org/jacoco/trunk/doc/maven.html)

