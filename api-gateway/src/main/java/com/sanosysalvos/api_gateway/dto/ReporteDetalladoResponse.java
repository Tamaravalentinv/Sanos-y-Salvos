package com.sanosysalvos.api_gateway.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReporteDetalladoResponse {
    private Long id;
    private String tipo;
    private String descripcion;
    private LocalDateTime fechaCreacion;
    private String estado;
    private MascotaDetalle mascota;
    private UbicacionDetalle ubicacion;
    private List<CoincidenciaDetalle> coincidenciasEncontradas;
    private List<CambioHistorial> historialCambios;
    private List<NotificacionReporte> notificacionesRelacionadas;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MascotaDetalle {
        private Long id;
        private String nombre;
        private String especie;
        private String raza;
        private String color;
        private int edad;
        private String tamaño;
        private String sexo;
        private List<String> caracteristicas;
        private List<String> fotos;
        private String ultimaVacuna;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UbicacionDetalle {
        private Long id;
        private String ciudad;
        private String barrio;
        private String direccion;
        private Double latitud;
        private Double longitud;
        private String codigoPostal;
        private LocalDateTime fecha;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CoincidenciaDetalle {
        private Long id;
        private Long reporteCoincidencia;
        private Double porcentajeMatch;
        private String motivo;
        private LocalDateTime fecha;
        private String estado;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CambioHistorial {
        private Long id;
        private String campo;
        private String valorAnterior;
        private String valorNuevo;
        private LocalDateTime fecha;
        private String usuarioQueCambio;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class NotificacionReporte {
        private Long id;
        private String tipo;
        private String mensaje;
        private LocalDateTime fecha;
        private boolean leida;
    }
}
