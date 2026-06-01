package com.sanosysalvos.ms_mascotas.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("CaracteristicaMascota Model Tests")
class CaracteristicaMascotaTest {

    private CaracteristicaMascota caracteristica;

    @BeforeEach
    void setUp() {
        caracteristica = new CaracteristicaMascota();
        caracteristica.setId(1L);
        caracteristica.setTamaño("MEDIANO");
        caracteristica.setSexo("MACHO");
        caracteristica.setEdadAproximada("2 años");
        caracteristica.setDescripcionFisica("Perro activo con marcas blancas");
        caracteristica.setPeso("25kg");
        caracteristica.setSenasParticulares("Cicatriz en oreja izquierda");
        caracteristica.setEsVacunado(true);
        caracteristica.setEsEsterilizado(false);
    }

    @Test
    @DisplayName("Debe crear característica con valores correctos")
    void testCrearCaracteristica() {
        assertNotNull(caracteristica);
        assertEquals(1L, caracteristica.getId());
        assertEquals("MEDIANO", caracteristica.getTamaño());
        assertEquals("MACHO", caracteristica.getSexo());
        assertEquals("2 años", caracteristica.getEdadAproximada());
    }

    @Test
    @DisplayName("Debe permitir actualizar tamaño")
    void testActualizarTamaño() {
        caracteristica.setTamaño("GRANDE");
        assertEquals("GRANDE", caracteristica.getTamaño());
    }

    @Test
    @DisplayName("Debe permitir actualizar sexo")
    void testActualizarSexo() {
        caracteristica.setSexo("HEMBRA");
        assertEquals("HEMBRA", caracteristica.getSexo());
    }

    @Test
    @DisplayName("Debe permitir actualizar edad")
    void testActualizarEdad() {
        caracteristica.setEdadAproximada("5 años");
        assertEquals("5 años", caracteristica.getEdadAproximada());
    }

    @Test
    @DisplayName("Debe permitir actualizar descripción física")
    void testActualizarDescripcion() {
        String desc = "Perro tranquilo, bien entrenado";
        caracteristica.setDescripcionFisica(desc);
        assertEquals(desc, caracteristica.getDescripcionFisica());
    }

    @Test
    @DisplayName("Debe permitir establecer peso")
    void testPeso() {
        caracteristica.setPeso("30kg");
        assertEquals("30kg", caracteristica.getPeso());
    }

    @Test
    @DisplayName("Debe permitir establecer señas particulares")
    void testSenasParticulares() {
        String senas = "Microchip: 123456789";
        caracteristica.setSenasParticulares(senas);
        assertEquals(senas, caracteristica.getSenasParticulares());
    }

    @Test
    @DisplayName("Debe permitir establecer vacunación")
    void testEsVacunado() {
        assertTrue(caracteristica.getEsVacunado());
        caracteristica.setEsVacunado(false);
        assertFalse(caracteristica.getEsVacunado());
    }

    @Test
    @DisplayName("Debe permitir establecer esterilización")
    void testEsEsterilizado() {
        assertFalse(caracteristica.getEsEsterilizado());
        caracteristica.setEsEsterilizado(true);
        assertTrue(caracteristica.getEsEsterilizado());
    }

    @Test
    @DisplayName("Debe validar tamaños válidos")
    void testTamañosValidos() {
        String[] tamaños = {"PEQUENO", "MEDIANO", "GRANDE"};
        for (String tamaño : tamaños) {
            caracteristica.setTamaño(tamaño);
            assertEquals(tamaño, caracteristica.getTamaño());
        }
    }

    @Test
    @DisplayName("Debe validar sexos válidos")
    void testSexosValidos() {
        String[] sexos = {"MACHO", "HEMBRA", "DESCONOCIDO"};
        for (String sexo : sexos) {
            caracteristica.setSexo(sexo);
            assertEquals(sexo, caracteristica.getSexo());
        }
    }

    @Test
    @DisplayName("Debe permitir características nulas")
    void testCaracteristicasNulas() {
        CaracteristicaMascota caracteristicaNula = new CaracteristicaMascota();
        assertNull(caracteristicaNula.getTamaño());
        assertNull(caracteristicaNula.getSexo());
        assertNull(caracteristicaNula.getEdadAproximada());
        assertNull(caracteristicaNula.getEsVacunado());
    }

    @Test
    @DisplayName("Debe permitir múltiples señas particulares")
    void testMultiplesSenas() {
        String senas = "Collar rojo, microchip, cicatriz frontal";
        caracteristica.setSenasParticulares(senas);
        assertTrue(senas.contains("Collar"));
        assertTrue(senas.contains("microchip"));
        assertTrue(senas.contains("cicatriz"));
    }
}
