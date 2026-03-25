package com.sanosysalvos.ms_mascotas.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "caracteristicas_mascota")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CaracteristicaMascota {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 50)
    private String tamaño; // PEQUENO, MEDIANO, GRANDE

    @Column(length = 20)
    private String sexo; // MACHO, HEMBRA, DESCONOCIDO

    @Column(length = 50)
    private String edadAproximada; // ejemplo: "2 años", "6 meses"

    @Column(columnDefinition = "TEXT")
    private String descripcionFisica; // Detalles especiales, marcas, etc.

    @Column(length = 50)
    private String peso; // Ej: "5kg", "Ligero"

    @Column(length = 100)
    private String senasParticulares; // Cicatrices, collar, microchip, etc.

    @Column(name = "es_vacunado")
    private Boolean esVacunado;

    @Column(name = "es_esterilizado")
    private Boolean esEsterilizado;
}
