package com.sanosysalvos.${package};

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

/**
 * Main Spring Boot Application class for ${name}
 *
 * This microservice is part of the Sanos y Salvos platform.
 * It provides ${description}
 */
@SpringBootApplication
@EnableEurekaClient
public class ${capitalizedName}Application {

    public static void main(String[] args) {
        SpringApplication.run(${capitalizedName}Application.class, args);
    }

}
