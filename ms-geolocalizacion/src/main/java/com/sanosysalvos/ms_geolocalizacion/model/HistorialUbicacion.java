package com.sanosysalvos.ms_geolocalizacion.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "historial_ubicacion")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class HistorialUbicacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "reporte_id", nullable = false)
    private Long reporteId;

    @Column(nullable = false)
    private Double latitud;

    @Column(nullable = false)
    private Double longitud;

    @Column(length = 255)
    private String descripcion;

    @Column(name = "tipo_evento")
    private String tipoEvento; // AVISTAMIENTO, BUSQUEDA, HALLAZGO, etc.

    @Column(name = "fuente_informacion") // USUARIO, ORGANIZACION, CIUDADANO, etc.
    private String fuenteInformacion;

    @Column(name = "confiabilidad") // 1-5
    private Integer confiabilidad;

    @Column(name = "quien_reporta")
    private Long quienReportaId; // usuarioId de quien reporta

    @Column(name = "fecha_evento")
    private LocalDateTime fechaEvento = LocalDateTime.now();

    @Column(columnDefinition = "TEXT")
    private String detalles;

    @Column(name = "comprobado")
    private Boolean comprobado = false;
}
