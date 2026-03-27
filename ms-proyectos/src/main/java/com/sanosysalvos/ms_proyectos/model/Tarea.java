package com.sanosysalvos.ms_proyectos.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "tareas")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Tarea {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String titulo;
    
    @Column(columnDefinition = "LONGTEXT")
    private String descripcion;
    
    @Column(nullable = false)
    private Long proyectoId;
    
    @Column(nullable = false)
    private Long asignadoId;
    
    @Column(nullable = false)
    private String estado; // PENDIENTE, EN_PROGRESO, COMPLETADA, CANCELADA
    
    @Column(nullable = false)
    private Integer prioridad; // 1 BAJA, 2 MEDIA, 3 ALTA
    
    private LocalDate fechaVencimiento;
    
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }
    
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
