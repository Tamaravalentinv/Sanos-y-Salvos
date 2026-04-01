# 🔄 Guía de Configuración - CI/CD Pipeline

## ¿Qué es el CI/CD Pipeline?

**CI/CD** = Continuous Integration / Continuous Deployment

- **Continuous Integration**: Cada vez que haces commit, se ejecutan automáticamente:
  - ✅ Tests unitarios
  - ✅ Análisis de cobertura
  - ✅ Análisis de seguridad

- **Continuous Deployment**: Si todo pasa, se puede automáticamente:
  - 🐳 Construir imágenes Docker
  - 📤 Pushear a Docker Hub
  - 🚀 Deployar a producción

---

## 📦 Paso a Paso de Instalación

### 1. Crear el repositorio en GitHub

```bash
# Si no tienes repositorio aún
git init
git add .
git commit -m "Initial commit: Sanos y Salvos project"
git branch -M main
git remote add origin https://github.com/tu-usuario/sanos-y-salvos.git
git push -u origin main
```

### 2. Crear carpeta y archivo de workflow

El archivo `.github/workflows/ci-cd.yml` ya fue creado en tu proyecto.

### 3. Configurar Secretos en GitHub

Ve a: **Settings → Secrets and variables → Actions**

Agrega estos secretos:

```
DOCKER_USERNAME = tu-usuario-docker
DOCKER_PASSWORD = tu-token-docker
CODECOV_TOKEN = tu-token-codecov (opcional)
```

**Cómo obtenerlos:**

#### Docker Hub Token:
1. Ve a https://hub.docker.com/settings/security
2. Crea nuevo token (Read & Write)
3. Cópialo y pégalo en GitHub Secrets

#### Codecov Token:
1. Ve a https://app.codecov.io
2. Conecta tu repositorio
3. Obtén el token

---

## 🎯 ¿Qué hace el Pipeline?

### **Job 1: TEST** ✅
```
✓ Inicia servidor MySQL
✓ Crea bases de datos
✓ Ejecuta tests JUnit (50+)
✓ Genera reporte JaCoCo
✓ Sube cobertura a Codecov
```
**Tiempo:** ~3-5 minutos

### **Job 2: BUILD** 🔨
```
✓ Compila todos los módulos Maven
✓ Crea JAR files
✓ Sube artifacts a GitHub
```
**Tiempo:** ~2-3 minutos

### **Job 3: DOCKER** 🐳
```
✓ Solo en rama 'main'
✓ Construye imágenes Docker
✓ Las sube a Docker Hub
```
**Tiempo:** ~5-10 minutos

### **Job 4: SECURITY** 🔒
```
✓ Escanea vulnerabilidades (Trivy)
✓ Analiza dependencias (OWASP)
✓ Reporta en Security tab
```
**Tiempo:** ~2-3 minutos

---

## 📊 Flujo Completo

```
┌─────────────────────────────────────┐
│   Git Push a main o develop         │
└────────────────┬────────────────────┘
                 │
        ┌────────▼────────┐
        │  TEST Job       │
        │ (50+ tests)     │
        └────────┬────────┘
                 │
         ✅ PASS / ❌ FAIL
                 │
        ┌────────▼────────┐
        │  BUILD Job      │
        │  (Maven compile)│
        └────────┬────────┘
                 │
         ✅ PASS / ❌ FAIL
                 │
        ┌────────▼────────┐
        │  DOCKER Job     │
        │  (Si main)      │
        └────────┬────────┘
                 │
         ✅ Push Docker Hub
                 │
        ┌────────▼────────┐
        │  SECURITY Job   │
        │  (Vulnerabs)    │
        └────────────────┘
                 │
         ✅ Reporte en GitHub
```

---

## 🚀 Cómo Usarlo

### Opción 1: Con GitHub Actions (Recomendado)
```bash
git add .
git commit -m "Add CI/CD pipeline"
git push origin main
```

Luego ve a: **tu-repo/Actions** y mira el pipeline ejecutándose

### Opción 2: Ejecutar localmente
```bash
# Para simular el pipeline en tu máquina:
mvn clean test            # Job 1: Tests
mvn package -DskipTests   # Job 2: Build
mvn jacoco:report         # Coverage
```

---

## 📈 Métricas y Reportes

### En GitHub Actions Dashboard:
- ✅ Status de cada job (verde/rojo)
- ⏱️ Tiempo de ejecución
- 📊 Histórico de builds
- 🔍 Logs detallados

### En GitHub Security Tab:
- 🔒 Vulnerabilidades detectadas
- 📋 OWASP Dependency Check results
- 🏆 Badge de seguridad

### En Codecov (si lo configuras):
- 📊 Cobertura de código
- 📈 Tendencias históricas
- 🎯 Targets de cobertura

---

## ⚙️ Configuración Avanzada

### Agregar más microservicios al Docker build:

En `.github/workflows/ci-cd.yml`, agrega después de API Gateway:

```yaml
    - name: Build and push MS [nombre]
      uses: docker/build-push-action@v4
      with:
        context: .
        file: ./ms-[nombre]/Dockerfile
        push: true
        tags: ${{ secrets.DOCKER_USERNAME }}/sanos-ms-[nombre]:latest
```

### Agregar notificaciones a Slack:

```yaml
    - name: Notify Slack on failure
      if: failure()
      uses: slackapi/slack-github-action@v1
      with:
        webhook-url: ${{ secrets.SLACK_WEBHOOK }}
        payload: |
          {
            "text": "❌ Build failed for Sanos y Salvos",
            "blocks": [
              {
                "type": "section",
                "text": {
                  "type": "mrkdwn",
                  "text": "*Build Status*: ❌ FAILED\n*Repository*: ${{ github.repository }}\n*Commit*: ${{ github.sha }}"
                }
              }
            ]
          }
```

### Requerir que tests pasen para hacer merge:

En GitHub: **Settings → Branches → Branch protection rules**

- ✅ Require status checks to pass before merging
- ✅ Selecciona: `test`, `build`, `security`

---

## 🐛 Troubleshooting

### "Database connection error"
```
→ GitHub Actions demora en iniciar MySQL
→ Aumenta el timeout en ci-cd.yml
```

### "Docker login failed"
```
→ Verifica que DOCKER_USERNAME y DOCKER_PASSWORD sean correctos
→ Que tengas acceso con ese token
```

### "Tests pass localmente pero fallan en CI"
```
→ Probablemente issue de MySQL
→ Asegúrate que SPRING_DATASOURCE_URL sea correcto
```

---

## 📚 Recursos Adicionales

- [GitHub Actions Docs](https://docs.github.com/en/actions)
- [Docker Setup Action](https://github.com/docker/setup-buildx-action)
- [Codecov Setup](https://docs.codecov.io/docs)
- [Trivy Security Scanner](https://github.com/aquasecurity/trivy-action)

---

## ✅ Checklist Final

- [ ] Archivo `.github/workflows/ci-cd.yml` creado
- [ ] Repositorio en GitHub
- [ ] Secretos configurados (DOCKER_USERNAME, DOCKER_PASSWORD)
- [ ] Primer push a main/develop
- [ ] Actions ejecutándose exitosamente
- [ ] Tests pasando en la nube
- [ ] Docker images siendo buildeadas
- [ ] Codecov/Security reportes visibles

---

**Próximo paso:** Una vez que el pipeline esté funcionando, configura el **Paso 3: Monitoring y Logging** 📊
