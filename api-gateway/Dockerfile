FROM eclipse-temurin:17
WORKDIR /app
COPY api-gateway/target/api-gateway-*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
