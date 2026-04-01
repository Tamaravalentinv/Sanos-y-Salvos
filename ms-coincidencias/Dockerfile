FROM eclipse-temurin:17
WORKDIR /app
COPY ms-coincidencias/target/ms-coincidencias-*.jar app.jar
EXPOSE 8082
ENTRYPOINT ["java", "-jar", "app.jar"]
