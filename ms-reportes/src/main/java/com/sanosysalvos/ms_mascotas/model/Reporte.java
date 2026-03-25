package com.sanosysalvos.ms_mascotas.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "reportes")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Reporte {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private TipoReporte tipo; // PERDIDA, ENCONTRADA

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private EstadoReporte estado = EstadoReporte.ABIERTO;

    // Usuario que crea el reporte
    @Column(nullable = false)
    private Long usuarioId;

    // Organización asociada (opcional)
    @Column
    private Long organizacionId;

    // Mascota
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "mascota_id", nullable = false)
    private Mascota mascota;

    // Ubicación del incidente
    @Column(length = 255)
    private String ubicacion;

    @Column(name = "latitud")
    private Double latitud;

    @Column(name = "longitud")
    private Double longitud;

    // Fechas
    @Column(name = "fecha_incidente")
    private LocalDateTime fechaIncidente; // Cuando se perdió o encontró

    @Column(name = "fecha_creacion")
    private LocalDateTime fechaCreacion = LocalDateTime.now();

    @Column(name = "fecha_resolucion")
    private LocalDateTime fechaResolucion;

    // Descripción y detalles
    @Column(columnDefinition = "TEXT")
    private String descripcion; // Detalles circunstancias del caso

    // Contacto del reportante
    @Column(length = 20)
    private String telefonoContacto;

    @Column(unique = true, length = 150)
    private String emailContacto;

    // Para mascotas encontradas
    @Column(name = "lugar_encontrado")
    private String lugarEncontrado;

    @Column(name = "detalles_hallazgo", columnDefinition = "TEXT")
    private String detallesHallazgo;

    // Relación con coincidencias (para vincular luego)
    @Column(name = "reportes_coincidentes")
    private String reportesCoincidentes; // JSON list de IDs

    // Contador de visualizaciones
    @Column(name = "num_visualizaciones")
    private Integer numVisualizaciones = 0;

    @Column(name = "prioridad")
    private Integer prioridad = 1; // 1-5, donde 5 es máxima prioridad

    @Column(name = "requiere_urgencia")
    private Boolean requiereUrgencia = false;

    // Para casos de mascotas adultas vs cachorro
    @Column(name = "es_cachorro")
    private Boolean esCachorro = false;
}
