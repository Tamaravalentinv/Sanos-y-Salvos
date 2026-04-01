FROM eclipse-temurin:17
WORKDIR /app
COPY ms-geolocalizacion/target/ms-geolocalizacion-*.jar app.jar
EXPOSE 8081
ENTRYPOINT ["java", "-jar", "app.jar"]
