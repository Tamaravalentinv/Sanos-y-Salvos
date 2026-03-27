package com.sanosysalvos.ms_usuarios.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "tipos_organizacion")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TipoOrganizacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false, length = 100)
    private String nombre;

    @Column(length = 255)
    private String descripcion;

    public TipoOrganizacion(String nombre) {
        this.nombre = nombre;
    }
}
