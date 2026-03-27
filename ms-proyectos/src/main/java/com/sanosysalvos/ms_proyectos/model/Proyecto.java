package com.sanosysalvos.ms_proyectos.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "proyectos")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Proyecto {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String nombre;
    
    @Column(columnDefinition = "LONGTEXT")
    private String descripcion;
    
    @Column(nullable = false)
    private String estado; // ACTIVO, EN_PROGRESO, COMPLETADO, CANCELADO
    
    @Column(nullable = false)
    private LocalDate fechaInicio;
    
    private LocalDate fechaFin;
    
    @Column(nullable = false)
    private Integer presupuesto;
    
    @Column(nullable = false)
    private Long responsableId;
    
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
