package com.sanosysalvos.ms_mascotas.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Mascota Model Tests")
class MascotaTest {

    private Mascota mascota;
    private CaracteristicaMascota caracteristica;

    @BeforeEach
    void setUp() {
        caracteristica = new CaracteristicaMascota();
        caracteristica.setId(1L);
        caracteristica.setTamaño("MEDIANO");
        caracteristica.setSexo("MACHO");
        caracteristica.setEdadAproximada("3 años");
        caracteristica.setDescripcionFisica("Perro activo");

        mascota = new Mascota();
        mascota.setId(1L);
        mascota.setNombre("Rex");
        mascota.setTipo("PERRO");
        mascota.setRaza("Labrador");
        mascota.setColor("Negro");
        mascota.setCaracteristica(caracteristica);
    }

    @Test
    @DisplayName("Debe crear mascota con valores correctos")
    void testCrearMascota() {
        assertNotNull(mascota);
        assertEquals(1L, mascota.getId());
        assertEquals("Rex", mascota.getNombre());
        assertEquals("PERRO", mascota.getTipo());
        assertEquals("Labrador", mascota.getRaza());
        assertEquals("Negro", mascota.getColor());
    }

    @Test
    @DisplayName("Debe permitir actualizar nombre")
    void testActualizarNombre() {
        mascota.setNombre("Firulais");
        assertEquals("Firulais", mascota.getNombre());
    }

    @Test
    @DisplayName("Debe permitir actualizar tipo")
    void testActualizarTipo() {
        mascota.setTipo("GATO");
        assertEquals("GATO", mascota.getTipo());
    }

    @Test
    @DisplayName("Debe permitir actualizar raza")
    void testActualizarRaza() {
        mascota.setRaza("Cocker Spaniel");
        assertEquals("Cocker Spaniel", mascota.getRaza());
    }

    @Test
    @DisplayName("Debe permitir actualizar color")
    void testActualizarColor() {
        mascota.setColor("Marrón");
        assertEquals("Marrón", mascota.getColor());
    }

    @Test
    @DisplayName("Debe permitir establecer características")
    void testCaracteristicas() {
        assertNotNull(mascota.getCaracteristica());
        assertEquals("MEDIANO", mascota.getCaracteristica().getTamaño());
        assertEquals("MACHO", mascota.getCaracteristica().getSexo());
    }

    @Test
    @DisplayName("Debe permitir establecer fotos")
    void testFotos() {
        FotoMascota foto = new FotoMascota();
        foto.setId(1L);
        foto.setUrlFoto("base64string");
        foto.setEsPrincipal(true);

        List<FotoMascota> fotos = new ArrayList<>();
        fotos.add(foto);
        mascota.setFotos(fotos);

        assertNotNull(mascota.getFotos());
        assertEquals(1, mascota.getFotos().size());
        assertTrue(mascota.getFotos().get(0).getEsPrincipal());
    }

    @Test
    @DisplayName("Debe permitir múltiples fotos")
    void testMultiplesFotos() {
        List<FotoMascota> fotos = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            FotoMascota foto = new FotoMascota();
            foto.setId((long) (i + 1));
            foto.setUrlFoto("foto" + i);
            foto.setOrden(i);
            fotos.add(foto);
        }
        mascota.setFotos(fotos);

        assertEquals(3, mascota.getFotos().size());
    }

    @Test
    @DisplayName("Debe permitir establecer reporte asociado")
    void testReporteAsociado() {
        Reporte reporte = new Reporte();
        reporte.setId(1L);
        reporte.setTipo(TipoReporte.PERDIDA);
        mascota.setReporte(reporte);

        assertNotNull(mascota.getReporte());
        assertEquals(TipoReporte.PERDIDA, mascota.getReporte().getTipo());
    }

    @Test
    @DisplayName("Debe permitir mascota sin características")
    void testMascotaSinCaracteristicas() {
        Mascota mascotaSinCarac = new Mascota();
        mascotaSinCarac.setId(2L);
        mascotaSinCarac.setNombre("Gato");
        mascotaSinCarac.setTipo("GATO");

        assertNull(mascotaSinCarac.getCaracteristica());
    }

    @Test
    @DisplayName("Debe permitir mascota sin fotos")
    void testMascotaSinFotos() {
        Mascota mascotaSinFotos = new Mascota();
        mascotaSinFotos.setNombre("Canario");

        assertNull(mascotaSinFotos.getFotos());
    }

    @Test
    @DisplayName("Debe validar tipos de mascota comunes")
    void testTiposMascota() {
        String[] tiposValidos = {"PERRO", "GATO", "AVE", "ROEDOR", "REPTIL"};
        for (String tipo : tiposValidos) {
            mascota.setTipo(tipo);
            assertEquals(tipo, mascota.getTipo());
        }
    }

    @Test
    @DisplayName("Debe manejar colores variados")
    void testColoresVariados() {
        String[] colores = {"Negro", "Blanco", "Marrón", "Gris", "Rojo", "Moteado"};
        for (String color : colores) {
            mascota.setColor(color);
            assertEquals(color, mascota.getColor());
        }
    }
}
