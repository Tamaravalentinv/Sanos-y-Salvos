package com.sanosysalvos.ms_coincidencias.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PuntajeCoincidenciaTest {

    @Test
    void puntajeTotalConsideraTodosLosPesos() {
        PuntajeCoincidencia puntaje = new PuntajeCoincidencia(100.0, 100.0, 100.0, 100.0, 100.0, 100.0);

        assertEquals(100.0, puntaje.calcularPuntajeTotal());
    }

    @Test
    void puntajeTotalToleraCamposNulos() {
        PuntajeCoincidencia puntaje = new PuntajeCoincidencia();
        puntaje.setEspecie(100.0);

        assertEquals(46.5116, puntaje.calcularPuntajeTotal(), 0.0001);
    }
}
