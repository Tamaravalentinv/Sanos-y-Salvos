package com.sanosysalvos.ms_mascotas.model;

public enum TipoReporte {
    PERDIDA("PERDIDA", "Mascota Perdida"),
    ENCONTRADA("ENCONTRADA", "Mascota Encontrada");

    private final String codigo;
    private final String descripcion;

    TipoReporte(String codigo, String descripcion) {
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
