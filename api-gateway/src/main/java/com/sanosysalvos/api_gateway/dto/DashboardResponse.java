package com.sanosysalvos.api_gateway.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DashboardResponse {
    private DashboardStats stats;
    private java.util.List<ReporteSummary> reportesRecientes;
    private java.util.List<NotificacionSummary> notificacionesPendientes;
    private java.util.List<CoincidenciaAlert> coincidenciasActivas;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DashboardStats {
        private int totalReportes;
        private int reportesActivos;
        private int coincidenciasEncontradas;
        private int notificacionesPendientes;
        private int usuariosActivos;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ReporteSummary {
        private Long id;
        private String tipo;
        private String nombreMascota;
        private String especie;
        private String raza;
        private String color;
        private String ubicacion;
        private LocalDateTime fechaCreacion;
        private String estado;
        private String fotoPrincipal;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class NotificacionSummary {
        private Long id;
        private String titulo;
        private String descripcion;
        private String tipo;
        private LocalDateTime fecha;
        private boolean leida;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CoincidenciaAlert {
        private Long id;
        private String mascotaPrincipal;
        private String mascotaCoincidencia;
        private Double porcentajeMatch;
        private String ciudad;
        private LocalDateTime fecha;
    }
}
