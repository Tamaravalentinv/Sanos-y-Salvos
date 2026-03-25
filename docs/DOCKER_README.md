# Docker Implementation Guide - Sanos y Salvos

## Overview
This Docker setup provides complete containerization of the Sanos y Salvos microservices architecture, enabling easy deployment and scaling across environments.

## Prerequisites
- Docker >= 20.10
- Docker Compose >= 1.29
- 4GB+ RAM available
- ~2GB disk space for images and volumes

## Configuration

### 1. Create `.env` file from template
```bash
cp .env.example .env
```

Edit `.env` with your actual credentials (DO NOT commit this file):
```env
MYSQL_ROOT_PASSWORD=your-secure-password
MYSQL_USER=sanosuser
MYSQL_PASSWORD=your-secure-password
JWT_SECRET=your-jwt-secret
MAIL_USERNAME=your-email@gmail.com
MAIL_PASSWORD=your-app-password
```

### 2. Build All Docker Images
```bash
./build.sh
```
This will build all 6 microservice images and the MySQL image.

### 2. Start the Stack
```bash
./up.sh
```
This will start all containers and initialize the databases.

### 3. Verify Services are Running
```bash
./health-check.sh
```
This verifies all services are responding to health checks.
## Security Best Practices

### Environment Variables
- Never commit `.env` file (add to .gitignore ✓)
- Use `.env.example` as template documentation
- Different values for development/production
- Rotate secrets regularly in production

### Database Security
For production, use:
- Strong MySQL passwords
- Separate database user per application
- Enable SSL/TLS for database connections
- Regular backups with encryption
- Minimal privileges per user role
## Architecture

### Containers
- **mysql** (MySQL 8.0) - Shared database server
- **api-gateway** (Port 8080) - API Gateway / BFF
- **ms-usuarios** (Port 8084) - User Management Service
- **ms-reportes** (Port 8083) - Reports Service
- **ms-geolocalizacion** (Port 8081) - Geolocation Service
- **ms-coincidencias** (Port 8082) - Matching Service
- **ms-notificaciones** (Port 8085) - Notifications Service

### Network
All containers communicate via a bridge network named `sanos-network`.

### Databases
- `sanosysalvos_usuarios` - MS Usuarios database
- `sanosysalvos` - MS Reportes, MS Geolocalizacion, MS Coincidencias database
- `sanosysalvos_notificaciones` - MS Notificaciones database

### Volumes
- `mysql_data` - Persistent MySQL data storage

## Service Endpoints

| Service | URL | Health Check |
|---------|-----|--------------|
| API Gateway | http://localhost:8080 | http://localhost:8080/actuator/health |
| MS Usuarios | http://localhost:8084 | http://localhost:8084/actuator/health |
| MS Reportes | http://localhost:8083 | http://localhost:8083/actuator/health |
| MS Geolocalizacion | http://localhost:8081 | http://localhost:8081/actuator/health |
| MS Coincidencias | http://localhost:8082 | http://localhost:8082/actuator/health |
| MS Notificaciones | http://localhost:8085 | http://localhost:8085/actuator/health |
| MySQL | localhost:3306 | - |

## Useful Commands

### View all container logs
```bash
./logs.sh
```

### View logs for a specific service
```bash
docker-compose logs -f ms-usuarios
```

### Stop the stack
```bash
./down.sh
```

### Check running containers
```bash
docker-compose ps
```

### Access MySQL database
```bash
# Create .env first if not exists
docker exec -it sanos-mysql mysql -u ${MYSQL_USER} -p${MYSQL_PASSWORD} sanosysalvos
```

### Execute docker-compose manually
```bash
docker-compose up -d      # Start stack
docker-compose down -v    # Stop and remove volumes
docker-compose restart    # Restart all services
```

## Configuration

### Database Credentials
Update `.env` file with secure values (do NOT commit):
- **Root User**: user + password from .env
- **Application User**: ${MYSQL_USER} / ${MYSQL_PASSWORD}

### Spring Profiles
When running in Docker, the `docker` Spring profile is active, which configures services to use MySQL container hostname.

### Environment Variables
All services are configured with environment variables in `docker-compose.yml`:
- `SPRING_DATASOURCE_URL` - Database connection
- `SPRING_DATASOURCE_USERNAME` - DB username
- `SPRING_DATASOURCE_PASSWORD` - DB password
- `SPRING_JPA_HIBERNATE_DDL_AUTO` - Auto schema creation (update)
- `SPRING_PROFILES_ACTIVE` - Active Spring profile (docker)

## Troubleshooting

### Services won't start
1. Check Docker is running: `docker ps`
2. View logs: `./logs.sh`
3. Ensure ports 8080-8085, 3306 are not in use: `docker-compose ps`

### MySQL connection refused
1. Wait for MySQL health check to pass (~30 seconds)
2. Check MySQL container: `docker-compose logs mysql`
3. Verify database initialization: `docker exec sanos-mysql mysql -u root -proot -e "SHOW DATABASES;"`

### Slow startup on first run
- First run builds Maven dependencies (~5-10 minutes depending on internet speed)
- Subsequent runs use cached layers (~30 seconds)

### Out of disk space
```bash
docker system prune -a    # Remove unused Docker resources
./down.sh                 # Remove volumes
```

## Development Workflow

### Make code changes
```bash
# Edit source code in your IDE
```

### Rebuild specific service
```bash
docker-compose build --no-cache ms-usuarios
docker-compose up -d ms-usuarios
```

### Rebuild all services
```bash
./build.sh
./down.sh
./up.sh
```

## Production Considerations

For production deployment:
1. Use external database instead of container
2. Implement proper secret management (credentials not in docker-compose)
3. Add resource limits to `docker-compose.yml`
4. Configure persistent volumes on stable storage
5. Implement proper logging aggregation (ELK, Splunk)
6. Add load balancing for multiple instances
7. Use private Docker registry for images
8. Implement health checks with automatic restart policies

## Multi-stage Build Benefits
- **Reduced Image Size**: Maven dependencies downloaded in build stage, not shipped in final image
- **Security**: Build tools not included in runtime image
- **Performance**: Faster container startup with smaller images
- **Base Image**: OpenJDK 17 slim (~400MB vs 1GB+ for full JDK)
