package com.sanosysalvos.ms_coincidencias.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "coincidencias")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Coincidencia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long reportePerdidoId;

    @Column(nullable = false)
    private Long reporteEncontradoId;

    @Column(nullable = false)
    private Double puntajeTotal; // 0-100

    @Column(columnDefinition = "TEXT")
    private String detallesPuntaje; // JSON: {"especie": 100, "raza": 30, "color": 25, ...}

    @Column(columnDefinition = "TEXT")
    private String observaciones;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoCoincidencia estado; // PENDIENTE_REVISION, CONFIRMADA, RECHAZADA, RESOLVIO_CASO

    @Column(nullable = false)
    private LocalDateTime fechaAnalisis;

    private LocalDateTime fechaConfirmacion;

    private Long usuarioQuienConfirmo;

    private String motivoRechazo;

    private LocalDateTime fechaResolucion;

    @Column(nullable = false)
    @Version
    private Integer version; // Optimistic locking

    public enum EstadoCoincidencia {
        PENDIENTE_REVISION,
        CONFIRMADA,
        RECHAZADA,
        RESOLVIO_CASO
    }
}