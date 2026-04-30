package com.sanosysalvos.ms_mascotas.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * Configuración de RestTemplate para comunicación inter-microservicios
 * 
 * Permite que ms-reportes se comunique con:
 * - ms-coincidencias (internamente)
 * - Otros microservicios si es necesario
 */
@Configuration
public class RestTemplateConfig {

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
