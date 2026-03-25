# Configuración de ejemplo para Spring Boot en Docker

## MS Usuarios - application.properties
```properties
spring.application.name=ms-usuarios
server.port=8084
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=false
spring.jpa.database=mysql

# Docker profile - use container hostname
spring.datasource.url=jdbc:mysql://mysql:3306/sanosysalvos_usuarios
spring.datasource.username=sanosuser
spring.datasource.password=sanospass123
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

# JWT Configuration
jwt.secret=your-secret-key-here
jwt.expiration=86400000
```

## MS Reportes - application.properties
```properties
spring.application.name=ms-reportes
server.port=8083
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=false
spring.jpa.database=mysql

spring.datasource.url=jdbc:mysql://mysql:3306/sanosysalvos
spring.datasource.username=sanosuser
spring.datasource.password=sanospass123
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
```

## MS Geolocalizacion - application.properties
```properties
spring.application.name=ms-geolocalizacion
server.port=8081
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=false
spring.jpa.database=mysql

spring.datasource.url=jdbc:mysql://mysql:3306/sanosysalvos
spring.datasource.username=sanosuser
spring.datasource.password=sanospass123
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
```

## MS Coincidencias - application.properties
```properties
spring.application.name=ms-coincidencias
server.port=8082
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=false
spring.jpa.database=mysql

spring.datasource.url=jdbc:mysql://mysql:3306/sanosysalvos
spring.datasource.username=sanosuser
spring.datasource.password=sanospass123
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
```

## MS Notificaciones - application.properties
```properties
spring.application.name=ms-notificaciones
server.port=8085
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=false
spring.jpa.database=mysql

spring.datasource.url=jdbc:mysql://mysql:3306/sanosysalvos_notificaciones
spring.datasource.username=sanosuser
spring.datasource.password=sanospass123
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

# Email Configuration (example with Gmail)
spring.mail.host=smtp.gmail.com
spring.mail.port=587
spring.mail.username=your-email@gmail.com
spring.mail.password=your-app-password
spring.mail.properties.mail.smtp.auth=true
spring.mail.properties.mail.smtp.starttls.enable=true
spring.mail.properties.mail.smtp.starttls.required=true
```

## API Gateway - application.properties
```properties
spring.application.name=api-gateway
server.port=8080

spring.cloud.gateway.routes[0].id=ms-usuarios
spring.cloud.gateway.routes[0].uri=http://ms-usuarios:8084
spring.cloud.gateway.routes[0].predicates[0]=Path=/api/users/**
spring.cloud.gateway.routes[0].filters[0]=RewritePath=/api/users(?<segment>/?.*), /users$\{segment}

spring.cloud.gateway.routes[1].id=ms-reportes
spring.cloud.gateway.routes[1].uri=http://ms-reportes:8083
spring.cloud.gateway.routes[1].predicates[0]=Path=/api/reports/**
spring.cloud.gateway.routes[1].filters[0]=RewritePath=/api/reports(?<segment>/?.*), /reports$\{segment}

spring.cloud.gateway.routes[2].id=ms-geolocalizacion
spring.cloud.gateway.routes[2].uri=http://ms-geolocalizacion:8081
spring.cloud.gateway.routes[2].predicates[0]=Path=/api/map/**
spring.cloud.gateway.routes[2].filters[0]=RewritePath=/api/map(?<segment>/?.*), /map$\{segment}

spring.cloud.gateway.routes[3].id=ms-coincidencias
spring.cloud.gateway.routes[3].uri=http://ms-coincidencias:8082
spring.cloud.gateway.routes[3].predicates[0]=Path=/api/matches/**
spring.cloud.gateway.routes[3].filters[0]=RewritePath=/api/matches(?<segment>/?.*), /matches$\{segment}

spring.cloud.gateway.routes[4].id=ms-notificaciones
spring.cloud.gateway.routes[4].uri=http://ms-notificaciones:8085
spring.cloud.gateway.routes[4].predicates[0]=Path=/api/notifications/**
spring.cloud.gateway.routes[4].filters[0]=RewritePath=/api/notifications(?<segment>/?.*), /notifications$\{segment}

# CORS Configuration
spring.web.cors.allowed-origins=*
spring.web.cors.allowed-methods=GET,POST,PUT,DELETE,OPTIONS
spring.web.cors.allowed-headers=*
```
