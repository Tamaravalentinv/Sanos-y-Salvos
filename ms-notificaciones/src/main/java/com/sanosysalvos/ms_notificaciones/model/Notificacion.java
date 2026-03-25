package com.sanosysalvos.ms_notificaciones.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "notificaciones")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Notificacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long usuarioId;

    @Column(nullable = false, length = 50)
    private String tipo; // EMAIL, SMS, PUSH, INTERNA

    @Column(nullable = false, length = 100)
    private String asunto;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String contenido;

    @Column(name = "destinatario", length = 150)
    private String destinatario; // email o teléfono

    @Column(nullable = false)
    private String estado = "PENDIENTE"; // PENDIENTE, ENVIADA, LEIDA, FALLIDA

    @Column(name = "fecha_creacion")
    private LocalDateTime fechaCreacion = LocalDateTime.now();

    @Column(name = "fecha_envio")
    private LocalDateTime fechaEnvio;

    @Column(name = "fecha_lectura")
    private LocalDateTime fechaLectura;

    @Column(name = "evento_tipo", length = 100)
    private String eventoTipo; // REPORTE_CREADO, COINCIDENCIA_DETECTADA, etc.

    @Column(name = "evento_id")
    private Long eventoId;

    @Column(name = "intentos_envio")
    private Integer intentosEnvio = 0;

    @Column(columnDefinition = "TEXT")
    private String mensajeError;
}
