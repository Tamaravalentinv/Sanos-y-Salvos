# Stage 1: Build
FROM maven:3.9 AS builder
WORKDIR /build
COPY . .
WORKDIR /build/ms-recursos-humanos
RUN mvn dependency:go-offline
RUN mvn clean package -DskipTests

# Stage 2: Runtime
FROM eclipse-temurin:17
WORKDIR /app
COPY --from=builder /build/ms-recursos-humanos/target/*.jar app.jar
EXPOSE 8087
ENTRYPOINT ["java", "-jar", "app.jar"]
