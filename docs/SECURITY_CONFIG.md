# Security & Environment Configuration Guide

## Directory Structure (Secure)

```
Sanos-y-Salvos/
├── .env                    ← LOCAL ONLY (gitignored) - actual credentials
├── .env.example            ← IN REPO - template for reference
├── .gitignore              ← Excludes .env and mysql_data/
├── docker-compose.yml      ← Uses ${VARIABLE} references
├── init-databases.sql      ← Base schema (safe to commit)
├── ms-usuarios/
├── ms-reportes/
├── ms-geolocalizacion/
├── ms-coincidencias/
├── ms-notificaciones/
├── api-gateway/
└── mysql_data/             ← gitignored - never commit
```

## Setup Steps

### 1. Initialize Environment File
```bash
# First time only
cp .env.example .env

# Edit with your secrets
nano .env  # or notepad .env on Windows
```

### 2. Generate Secure Passwords

```bash
# On Linux/macOS
openssl rand -base64 32

# On Windows PowerShell
[Convert]::ToBase64String([System.Security.Cryptography.RandomNumberGenerator]::GetBytes(32))
```

### 3. Update .env with Generated Values

```env
MYSQL_ROOT_PASSWORD=wj7X9K2mL8nQxRpZ4vB1cD6eF9gJ3hM5=   # From generation above
MYSQL_PASSWORD=aB9cD2eF5gJ8kL1mN4pQ7rS0tU3vW6xY9Z2=     # From generation above
JWT_SECRET=sG3hJ6kM9nP2qR5sT8uV1wX4yZ7aB0cD3eF6gH9jK2= # From generation above
```

## Environment Variables Reference

### MySQL
```env
MYSQL_ROOT_PASSWORD      # Root access password
MYSQL_USER              # Application database user
MYSQL_PASSWORD          # Application user password
```

### Microservices (Database URLs)
All services use variables to avoid hardcoding:
```env
USUARIOS_DB_URL         # jdbc:mysql://mysql:3306/sanosysalvos_usuarios
USUARIOS_DB_USER        # Database user
USUARIOS_DB_PASSWORD    # Database password

REPORTES_DB_URL         # jdbc:mysql://mysql:3306/sanosysalvos
REPORTES_DB_USER        # Database user
REPORTES_DB_PASSWORD    # Database password
```

### Authentication
```env
JWT_SECRET              # Secret key for JWT token signing
JWT_EXPIRATION         # Token expiration time in milliseconds (86400000 = 24 hours)
```

### Email (Optional - MS Notificaciones)
```env
MAIL_HOST              # SMTP server (e.g., smtp.gmail.com)
MAIL_PORT              # SMTP port (e.g., 587)
MAIL_USERNAME          # Email sending account
MAIL_PASSWORD          # Email account password or app-specific password
```

## Security Best Practices

### ✅ DO
- [x] Use strong, randomly generated passwords
- [x] Store .env locally only (never commit)
- [x] Use different passwords for dev/test/prod
- [x] Rotate secrets regularly in production
- [x] Use .env.example as documentation
- [x] Add .env to .gitignore
- [x] Keep docker-compose.yml in repo (with ${VARIABLES})

### ❌ DON'T
- [ ] Hardcode credentials in docker-compose.yml
- [ ] Commit .env file to repository
- [ ] Use same passwords across environments
- [ ] Share .env file via email/chat
- [ ] Use weak/default passwords (like "password123")
- [ ] Log sensitive values in application

## Docker Secrets for Production

For production deployments, use Docker secrets:

```yaml
secrets:
  db_password:
    file: ./secrets/db_password.txt

services:
  mysql:
    environment:
      MYSQL_PASSWORD_FILE: /run/secrets/db_password
```

See Docker documentation: https://docs.docker.com/engine/swarm/secrets/

## CI/CD Integration

### GitHub Actions Example
```yaml
- name: Deploy with docker-compose
  env:
    MYSQL_ROOT_PASSWORD: ${{ secrets.MYSQL_ROOT_PASSWORD }}
    MYSQL_PASSWORD: ${{ secrets.MYSQL_PASSWORD }}
    JWT_SECRET: ${{ secrets.JWT_SECRET }}
  run: docker-compose up -d
```

Store secrets in:
- GitHub: Settings → Secrets and variables → Actions
- GitLab: Settings → Pipelines → Secrets
- Jenkins: Credentials store

## Troubleshooting

### "Access denied for user 'sanosuser'"
- Check `.env` file exists
- Verify MYSQL_USER and MYSQL_PASSWORD are set
- Restart services: `./down.sh && ./up.sh`

### Services can't connect to MySQL
- Ensure mysql container is healthy: `docker-compose ps`
- Check .env variables are loaded: `docker-compose config`
- View logs: `./logs.sh`

### Forgot password
1. Stop stack: `./down.sh`
2. Update .env with new password
3. Delete mysql volume: `docker volume rm sanos-mysql-data`
4. Start again: `./up.sh`

## Reference Files

- `.env` - Local copy with actual secrets (GITIGNORED)
- `.env.example` - Template for team reference (IN REPO)
- `docker-compose.yml` - Uses ${VARIABLES} (IN REPO)
- `.gitignore` - Excludes .env, mysql_data/ (IN REPO)
