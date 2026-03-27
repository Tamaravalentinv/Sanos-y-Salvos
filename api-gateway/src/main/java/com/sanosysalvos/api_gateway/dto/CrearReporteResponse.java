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
public class CrearReporteResponse {
    private Long reporteId;
    private Long mascotaId;
    private Long ubicacionId;
    private String estado;
    private LocalDateTime fechaCreacion;
    private String mensaje;
    private List<String> errores;
}
