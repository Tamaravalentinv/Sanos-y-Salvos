FROM openjdk:17-slim
WORKDIR /app
RUN apt-get update && apt-get install -y --no-install-recommends \
    curl \
    && rm -rf /var/lib/apt/lists/*
COPY target/ms-recursos-humanos-1.0.0.jar app.jar
EXPOSE 8087
CMD ["java", "-jar", "app.jar"]
