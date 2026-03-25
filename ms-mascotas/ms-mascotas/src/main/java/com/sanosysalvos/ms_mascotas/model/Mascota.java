package com.sanosysalvos.ms_mascotas.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "mascotas_reporte")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Mascota {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String nombre;

    @Column(nullable = false, length = 50)
    private String tipo; // PERRO, GATO, AVE, ROEDOR, etc.

    @Column(nullable = false, length = 100)
    private String raza;

    @Column(nullable = false, length = 50)
    private String color;

    // Relación con características
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "caracteristica_id")
    private CaracteristicaMascota caracteristica;

    // Relación con fotos
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    @JoinColumn(name = "mascota_id")
    private java.util.List<FotoMascota> fotos;

    // Relación con reporte
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reporte_id")
    private Reporte reporte;
}
