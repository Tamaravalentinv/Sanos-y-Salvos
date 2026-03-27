package com.sanosysalvos.api_gateway.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CrearReporteRequest {
    private String tipo;
    private String descripcion;
    private String nombreMascota;
    private String especie;
    private String raza;
    private String color;
    private String tamaño;
    private String sexo;
    private List<String> caracteristicas;
    private List<String> fotos;
    private String ciudad;
    private String barrio;
    private String direccion;
    private Double latitud;
    private Double longitud;
    private String telefono;
    private String email;
    private String nombreContacto;
}
