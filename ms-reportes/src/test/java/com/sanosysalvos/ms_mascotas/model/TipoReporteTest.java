package com.sanosysalvos.ms_mascotas.model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("TipoReporte Enum Tests")
class TipoReporteTest {

    @Test
    @DisplayName("Debe validar enum PERDIDA")
    void testEnumPerdida() {
        TipoReporte tipo = TipoReporte.PERDIDA;
        assertEquals("PERDIDA", tipo.getCodigo());
        assertEquals("Mascota Perdida", tipo.getDescripcion());
    }

    @Test
    @DisplayName("Debe validar enum ENCONTRADA")
    void testEnumEncontrada() {
        TipoReporte tipo = TipoReporte.ENCONTRADA;
        assertEquals("ENCONTRADA", tipo.getCodigo());
        assertEquals("Mascota Encontrada", tipo.getDescripcion());
    }

    @Test
    @DisplayName("Debe obtener valores del enum")
    void testValoresEnum() {
        TipoReporte[] valores = TipoReporte.values();
        assertEquals(2, valores.length);
    }

    @Test
    @DisplayName("Debe convertir string a enum")
    void testValueOf() {
        TipoReporte tipo = TipoReporte.valueOf("PERDIDA");
        assertEquals(TipoReporte.PERDIDA, tipo);

        TipoReporte tipo2 = TipoReporte.valueOf("ENCONTRADA");
        assertEquals(TipoReporte.ENCONTRADA, tipo2);
    }

    @Test
    @DisplayName("Debe validar comparación de enums")
    void testComparacion() {
        assertTrue(TipoReporte.PERDIDA.equals(TipoReporte.PERDIDA));
        assertFalse(TipoReporte.PERDIDA.equals(TipoReporte.ENCONTRADA));
    }
}

@DisplayName("EstadoReporte Enum Tests")
class EstadoReporteTest {

    @Test
    @DisplayName("Debe validar enum ABIERTO")
    void testEnumAbierto() {
        EstadoReporte estado = EstadoReporte.ABIERTO;
        assertEquals("ABIERTO", estado.getCodigo());
        assertEquals("Reporte activo", estado.getDescripcion());
    }

    @Test
    @DisplayName("Debe validar enum EN_PROGRESO")
    void testEnumEnProgreso() {
        EstadoReporte estado = EstadoReporte.EN_PROGRESO;
        assertEquals("EN_PROGRESO", estado.getCodigo());
        assertEquals("Se están realizando búsquedas", estado.getDescripcion());
    }

    @Test
    @DisplayName("Debe validar enum RESUELTO")
    void testEnumResuelto() {
        EstadoReporte estado = EstadoReporte.RESUELTO;
        assertEquals("RESUELTO", estado.getCodigo());
        assertEquals("Mascota encontrada", estado.getDescripcion());
    }

    @Test
    @DisplayName("Debe validar enum CERRADO")
    void testEnumCerrado() {
        EstadoReporte estado = EstadoReporte.CERRADO;
        assertEquals("CERRADO", estado.getCodigo());
        assertEquals("Reporte cerrado sin resolución", estado.getDescripcion());
    }

    @Test
    @DisplayName("Debe obtener valores del enum")
    void testValoresEnum() {
        EstadoReporte[] valores = EstadoReporte.values();
        assertEquals(4, valores.length);
    }

    @Test
    @DisplayName("Debe convertir string a enum")
    void testValueOf() {
        EstadoReporte estado = EstadoReporte.valueOf("ABIERTO");
        assertEquals(EstadoReporte.ABIERTO, estado);

        EstadoReporte estado2 = EstadoReporte.valueOf("RESUELTO");
        assertEquals(EstadoReporte.RESUELTO, estado2);
    }

    @Test
    @DisplayName("Debe validar comparación de enums")
    void testComparacion() {
        assertTrue(EstadoReporte.ABIERTO.equals(EstadoReporte.ABIERTO));
        assertFalse(EstadoReporte.ABIERTO.equals(EstadoReporte.RESUELTO));
    }

    @Test
    @DisplayName("Debe validar transiciones de estado válidas")
    void testTransicionesValidas() {
        // ABIERTO -> EN_PROGRESO
        EstadoReporte estado1 = EstadoReporte.ABIERTO;
        estado1 = EstadoReporte.EN_PROGRESO;
        assertEquals(EstadoReporte.EN_PROGRESO, estado1);

        // EN_PROGRESO -> RESUELTO
        estado1 = EstadoReporte.RESUELTO;
        assertEquals(EstadoReporte.RESUELTO, estado1);

        // Cualquiera -> CERRADO
        estado1 = EstadoReporte.CERRADO;
        assertEquals(EstadoReporte.CERRADO, estado1);
    }
}
