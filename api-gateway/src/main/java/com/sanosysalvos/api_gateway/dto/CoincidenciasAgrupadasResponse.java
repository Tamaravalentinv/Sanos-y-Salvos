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
public class CoincidenciasAgrupadasResponse {
    private List<CoincidenciaGrupo> grupos;
    private int totalCoincidencias;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CoincidenciaGrupo {
        private Long reportePerdida;
        private List<MatchEncontrado> matches;
        private Double mejorPorcentaje;
        private int totalMatches;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor  
    public static class MatchEncontrado {
        private Long reporteEncontrada;
        private String nombreMascota;
        private String ciudad;
        private Double porcentajeMatch;
        private LocalDateTime fecha;
        private List<String> motivosCoincidencia;
    }
}
