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
public class NotificacionesPersonalizadasResponse {
    private int totalNotificaciones;
    private int noLeidas;
    private List<NotificacionPersonalizada> notificaciones;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class NotificacionPersonalizada {
        private Long id;
        private String tipo;
        private String titulo;
        private String descripcion;
        private LocalDateTime fecha;
        private boolean leida;
        private String icono;
        private String colorEtiqueta;
        private String linkAccion;
    }
}
