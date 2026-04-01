FROM eclipse-temurin:17
WORKDIR /app
COPY ms-usuarios/target/ms-usuarios-*.jar app.jar
EXPOSE 8084
ENTRYPOINT ["java", "-jar", "app.jar"]
