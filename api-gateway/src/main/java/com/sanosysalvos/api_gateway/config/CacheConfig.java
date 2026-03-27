package com.sanosysalvos.api_gateway.config;

import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * Configuración de Caché para BFF
 * Implementa caching de respuestas para optimizar performance del frontend
 */
@Configuration
@EnableCaching
public class CacheConfig {

    /**
     * Define los cachés disponibles
     * - dashboard: Cache de dashboard (userId)
     * - reporte_detallado: Cache de reportes individuales (reporteId)
     * - coincidencias_agrupadas: Cache de coincidencias (userId)
     * 
     * Nota: En producción usar Redis o Memcached
     */
    @Bean
    public CacheManager cacheManager() {
        return new ConcurrentMapCacheManager(
            "dashboard",
            "reporte_detallado",
            "coincidencias_agrupadas",
            "notificaciones",
            "usuarios"
        );
    }

    /**
     * RestTemplate para comunicación con microservicios
     * En producción usar WebClient para llamadas reactivas
     */
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
