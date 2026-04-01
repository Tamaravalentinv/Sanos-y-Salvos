FROM eclipse-temurin:17
WORKDIR /app
COPY ms-notificaciones/target/ms-notificaciones-*.jar app.jar
EXPOSE 8085
ENTRYPOINT ["java", "-jar", "app.jar"]
