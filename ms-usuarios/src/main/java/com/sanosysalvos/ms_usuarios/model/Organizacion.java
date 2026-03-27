package com.sanosysalvos.ms_usuarios.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "organizaciones")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Organizacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 200)
    private String nombre;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "tipo_organizacion_id", nullable = false)
    private TipoOrganizacion tipo; // CLINICA, REFUGIO, MUNICIPALIDAD, etc.

    @Column(length = 255)
    private String descripcion;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "contacto_id")
    private Contacto contacto;

    @Column(length = 50)
    private String rut; // En Chile

    @Column(nullable = false)
    private String estado = "ACTIVO"; // ACTIVO, INACTIVO

    @Column(name = "fecha_registro")
    private LocalDateTime fechaRegistro = LocalDateTime.now();

    @OneToMany(mappedBy = "organizacion", fetch = FetchType.LAZY)
    private Set<Usuario> usuarios = new HashSet<>();

    @Column(name = "numero_empleados")
    private Integer numeroEmpleados;

    @Column(length = 255)
    private String sitioWeb;

    @Column(name = "es_verificada")
    private Boolean esVerificada = false;
}
