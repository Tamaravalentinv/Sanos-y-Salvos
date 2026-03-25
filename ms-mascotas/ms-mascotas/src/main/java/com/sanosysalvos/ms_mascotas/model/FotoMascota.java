package com.sanosysalvos.ms_mascotas.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "fotos_mascota")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FotoMascota {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, columnDefinition = "LONGTEXT")
    private String urlFoto; // Base64 o URL a storage

    @Column(length = 255)
    private String descripcion;

    @Column(name = "es_principal")
    private Boolean esPrincipal = false;

    @Column(name = "orden")
    private Integer orden = 0;
}
