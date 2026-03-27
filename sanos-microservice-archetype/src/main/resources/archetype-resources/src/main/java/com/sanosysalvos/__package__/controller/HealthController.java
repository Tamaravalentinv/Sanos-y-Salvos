package com.sanosysalvos.${package}.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Health check controller for ${name}
 */
@RestController
@RequestMapping("/health")
public class HealthController {

    @GetMapping
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("${name} is running");
    }

    @GetMapping("/status")
    public ResponseEntity<HealthStatus> status() {
        HealthStatus status = HealthStatus.builder()
            .service("${artifactId}")
            .status("UP")
            .build();
        return ResponseEntity.ok(status);
    }

    // Inner class for response structure
    public static class HealthStatus {
        private String service;
        private String status;

        public HealthStatus(String service, String status) {
            this.service = service;
            this.status = status;
        }

        public static HealthStatusBuilder builder() {
            return new HealthStatusBuilder();
        }

        public static class HealthStatusBuilder {
            private String service;
            private String status;

            public HealthStatusBuilder service(String service) {
                this.service = service;
                return this;
            }

            public HealthStatusBuilder status(String status) {
                this.status = status;
                return this;
            }

            public HealthStatus build() {
                return new HealthStatus(service, status);
            }
        }

        public String getService() {
            return service;
        }

        public String getStatus() {
            return status;
        }
    }
}
