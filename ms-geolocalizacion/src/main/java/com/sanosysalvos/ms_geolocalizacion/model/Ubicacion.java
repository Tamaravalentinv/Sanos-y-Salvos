package com.sanosysalvos.ms_geolocalizacion.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "ubicaciones")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Ubicacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Double latitud;

    @Column(nullable = false)
    private Double longitud;

    @Column(length = 255)
    private String descripcion;

    @Column(length = 100)
    private String calle;

    @Column(length = 100)
    private String numero;

    @Column(length = 100)
    private String comuna;

    @Column(length = 100)
    private String ciudad;

    @Column(length = 100)
    private String region;

    @Column(length = 20)
    private String codigoPostal;

    @Column(name = "reporte_id")
    private Long reporteId; // Relación con reporte de MS Reportes

    @Column(name = "fecha_registro")
    private LocalDateTime fechaRegistro = LocalDateTime.now();

    @Column(name = "tipo_evento") // PERDIDA, ENCONTRADA
    private String tipoEvento;

    @Column(name = "accuracy")
    private Double accuracy; // Precisión en metros
}
