package com.sanosysalvos.ms_coincidencias.model;

public class Coincidencia {

    private Mascota perdido;
    private Mascota encontrado;

    public Coincidencia(Mascota perdido, Mascota encontrado) {
        this.perdido = perdido;
        this.encontrado = encontrado;
    }

    public Mascota getPerdido() {
        return perdido;
    }

    public Mascota getEncontrado() {
        return encontrado;
    }
}