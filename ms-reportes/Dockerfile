FROM eclipse-temurin:17
WORKDIR /app
COPY ms-reportes/target/ms-reportes-*.jar app.jar
EXPOSE 8083
ENTRYPOINT ["java", "-jar", "app.jar"]
