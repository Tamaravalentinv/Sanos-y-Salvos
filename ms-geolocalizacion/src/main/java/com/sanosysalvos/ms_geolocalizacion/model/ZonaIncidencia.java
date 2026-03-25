package com.sanosysalvos.ms_geolocalizacion.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "zonas_incidencia")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ZonaIncidencia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String nombre; // Ej: "Centro de Santiago", "Barrio Brasil"

    @Column(nullable = false)
    private Double latitudCentro;

    @Column(nullable = false)
    private Double longitudCentro;

    @Column(nullable = false)
    private Double radioKm; // Radio en kilómetros

    @Column(nullable = false)
    private Integer numIncidencias = 0;

    @Column(nullable = false)
    private Integer numPerdidas = 0;

    @Column(nullable = false)
    private Integer numEncontradas = 0;

    @Column(nullable = false)
    private Double tasaRecuperacion = 0.0; // Porcentaje

    @Column(name = "nivel_riesgo") // BAJO, MEDIO, ALTO, CRITICO
    private String nivelRiesgo = "BAJO";

    @Column(name = "fecha_ultima_actualizacion")
    private LocalDateTime fechaUltimaActualizacion = LocalDateTime.now();

    @Column(columnDefinition = "TEXT")
    private String observaciones;

    @Column(name = "es_activa")
    private Boolean esActiva = true;
}
