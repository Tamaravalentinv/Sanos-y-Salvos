package com.sanosysalvos.ms_mascotas.model;

public enum EstadoReporte {
    ABIERTO("ABIERTO", "Reporte activo"),
    EN_PROGRESO("EN_PROGRESO", "Se están realizando búsquedas"),
    RESUELTO("RESUELTO", "Mascota encontrada"),
    CERRADO("CERRADO", "Reporte cerrado sin resolución");

    private final String codigo;
    private final String descripcion;

    EstadoReporte(String codigo, String descripcion) {
        this.codigo = codigo;
        this.descripcion = descripcion;
    }

    public String getCodigo() {
        return codigo;
    }

    public String getDescripcion() {
        return descripcion;
    }
}
