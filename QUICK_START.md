# ⚡ Quick Start Guide - Sanos y Salvos

Guía rápida para ejecutar el proyecto en minutos.

## 🚀 Inicio Rápido (5 minutos)

### Opción 1: Con Docker (RECOMENDADO) ⭐

```bash
# 1. Clonar o descargar el proyecto
cd sanos-y-salvos

# 2. Levantar todos los servicios
docker-compose up -d

# 3. Esperar a que se complete (8-15 minutos la primera vez)
# Verificar estado:
docker ps

# 4. Acceder a la aplicación
# Frontend:  http://localhost:3000
# API:       http://localhost:8080
# Adminer:   http://localhost:8888 (BD)
```

### Opción 2: Con XAMPP + Local Development

```bash
# 1. Iniciar XAMPP
# Abre C:\xampp\xampp-control.exe
# Haz clic en "Start" para Apache y MySQL

# 2. Importar la BD
mysql -u root -p < database/completo_script.sql
# Contraseña por defecto: (vacía)

# 3. Iniciar Backend (cada microservicio por separado)
cd ms-usuarios
mvn spring-boot:run

# 4. En otra terminal, iniciar Frontend
cd frontend
npm install
npm run dev

# 5. Acceder a http://localhost:5173
```

---

## 📋 Requisitos Previos

### Docker (Opción 1)
- ✅ Docker Desktop ([Descargar](https://www.docker.com/products/docker-desktop))
- ✅ Docker Compose (incluido con Docker Desktop)

### Desarrollo Local (Opción 2)
- ✅ Java 17+ ([Descargar JDK](https://www.oracle.com/java/technologies/downloads/))
- ✅ Maven 3.9+ ([Descargar](https://maven.apache.org/download.cgi))
- ✅ Node.js 18+ ([Descargar](https://nodejs.org/))
- ✅ MySQL 8.0 o XAMPP ([Descargar XAMPP](https://www.apachefriends.org/))

---

## 🔐 Credenciales de Prueba

```
Usuario (Ciudadano):
  Username: juan
  Password: juan123

Administrador:
  Username: admin
  Password: admin123

Clínica:
  Username: clinica
  Password: clinica123

Refugio:
  Username: refugio
  Password: refugio123
```

---

## 🌐 URLs Principales

### Producción (Docker)

| Servicio | URL | Descripción |
|----------|-----|-------------|
| **Frontend** | http://localhost:3000 | Aplicación web |
| **API Gateway** | http://localhost:8080 | Endpoint principal |
| **API Health** | http://localhost:8080/actuator/health | Estado del sistema |
| **Swagger API** | http://localhost:8080/swagger-ui.html | Documentación API |
| **Adminer (BD)** | http://localhost:8888 | Gestor de BD |

### Microservicios (si se ejecutan localmente)

| Servicio | Puerto | URL |
|----------|--------|-----|
| MS Usuarios | 8084 | http://localhost:8084 |
| MS Reportes | 8083 | http://localhost:8083 |
| MS Geolocalizacion | 8081 | http://localhost:8081 |
| MS Coincidencias | 8082 | http://localhost:8082 |
| MS Notificaciones | 8085 | http://localhost:8085 |

---

## 🛠️ Comandos Útiles

### Docker

```bash
# Ver estado de servicios
docker-compose ps

# Ver logs de un servicio específico
docker-compose logs ms-reportes -f

# Ver todos los logs
docker-compose logs -f

# Detener servicios
docker-compose down

# Detener y eliminar volúmenes
docker-compose down -v

# Reconstruir imágenes
docker-compose build --no-cache

# Ejecutar comando en contenedor
docker-compose exec ms-usuarios bash
```

### Maven

```bash
# Compilar proyecto
mvn clean package

# Compilar sin tests
mvn clean package -DskipTests

# Ejecutar tests
mvn test

# Ejecutar servidor Spring Boot
mvn spring-boot:run
```

### Frontend

```bash
# Instalar dependencias
npm install

# Servidor de desarrollo
npm run dev

# Build para producción
npm run build

# Preview del build
npm run preview

# Ejecutar tests
npm test
```

---

## 🐛 Solución de Problemas

### Docker no inicia

```bash
# Verificar que Docker Desktop está corriendo
docker ps

# Si da error, reiniciar Docker:
# 1. Abre Docker Desktop
# 2. Settings > Resources > Restart Docker
```

### Puerto ya en uso

```bash
# Si el puerto 3000 está en uso (Frontend):
lsof -i :3000
kill -9 <PID>

# Si el puerto 8080 está en uso (API Gateway):
lsof -i :8080
kill -9 <PID>
```

### BD no se conecta

```bash
# Verificar que MySQL está corriendo
docker-compose ps mysql

# Reiniciar MySQL
docker-compose restart mysql

# Verificar logs de MySQL
docker-compose logs mysql
```

### Frontend no carga

```bash
# Verificar que hay red entre contenedores
docker-compose exec frontend ping api-gateway

# Verificar que el API Gateway está respondiendo
curl http://localhost:8080/actuator/health
```

---

## 📊 Estado Esperado

Una vez que todo esté corriendo, deberías ver esto:

```
$ docker-compose ps

NAME                           STATUS           PORTS
sanos-mysql                    Up 2 minutes     3306/tcp
sanos-api-gateway              Up 2 minutes     0.0.0.0:8080->8080/tcp
sanos-ms-usuarios              Up 2 minutes     0.0.0.0:8084->8084/tcp
sanos-ms-reportes              Up 2 minutes     0.0.0.0:8083->8083/tcp
sanos-ms-geolocalizacion       Up 2 minutes     0.0.0.0:8081->8081/tcp
sanos-ms-coincidencias         Up 2 minutes     0.0.0.0:8082->8082/tcp
sanos-ms-notificaciones        Up 2 minutes     0.0.0.0:8085->8085/tcp
sanos-frontend                 Up 2 minutes     0.0.0.0:3000->3000/tcp
```

---

## ✅ Checklist de Verificación

- [ ] Docker Desktop está corriendo
- [ ] `docker-compose up -d` completó exitosamente
- [ ] Todos los contenedores están en estado "Up"
- [ ] Puedo acceder a http://localhost:3000
- [ ] Puedo hacer login con juan / juan123
- [ ] Puedo ver el dashboard
- [ ] Puedo crear un reporte

---

## 📖 Documentación Adicional

- [README.md](README.md) - Documentación completa del proyecto
- [PROJECT_STRUCTURE.md](PROJECT_STRUCTURE.md) - Estructura del proyecto
- [CONTRIBUTING.md](CONTRIBUTING.md) - Guía de contribución

---

## 🤝 Soporte

Si tienes problemas:

1. Revisa los logs: `docker-compose logs -f`
2. Revisa [Solución de Problemas](#-solución-de-problemas)
3. Abre un issue en GitHub
4. Contacta al equipo de desarrollo

---

**¡A disfrutar desarrollando!** 🚀
