# Stage 1: Build
FROM maven:3.8-openjdk-17 AS builder
WORKDIR /build
# Copiar pom padre y todos los pom de módulos
COPY pom.xml .
COPY api-gateway/pom.xml ./api-gateway/
COPY ms-usuarios/pom.xml ./ms-usuarios/
COPY ms-reportes/pom.xml ./ms-reportes/
COPY ms-geolocalizacion/pom.xml ./ms-geolocalizacion/
COPY ms-coincidencias/pom.xml ./ms-coincidencias/
COPY ms-notificaciones/pom.xml ./ms-notificaciones/
# Descargar dependencias
RUN mvn -pl ms-reportes clean dependency:go-offline
# Copiar código fuente de ese módulo
COPY ms-reportes/src ./ms-reportes/src
# Compilar el módulo específico
RUN mvn -pl ms-reportes clean package -DskipTests

# Stage 2: Runtime
FROM eclipse-temurin:17
WORKDIR /app
COPY --from=builder /build/ms-reportes/target/*.jar app.jar
EXPOSE 8083
ENTRYPOINT ["java", "-jar", "app.jar"]
