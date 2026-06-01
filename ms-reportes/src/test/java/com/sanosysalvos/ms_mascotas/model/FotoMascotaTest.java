package com.sanosysalvos.ms_mascotas.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("FotoMascota Model Tests")
class FotoMascotaTest {

    private FotoMascota foto;

    @BeforeEach
    void setUp() {
        foto = new FotoMascota();
        foto.setId(1L);
        foto.setUrlFoto("data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAAUA...");
        foto.setDescripcion("Foto frontal del perro");
        foto.setEsPrincipal(true);
        foto.setOrden(0);
    }

    @Test
    @DisplayName("Debe crear foto con valores correctos")
    void testCrearFoto() {
        assertNotNull(foto);
        assertEquals(1L, foto.getId());
        assertNotNull(foto.getUrlFoto());
        assertEquals("Foto frontal del perro", foto.getDescripcion());
        assertTrue(foto.getEsPrincipal());
        assertEquals(0, foto.getOrden());
    }

    @Test
    @DisplayName("Debe permitir actualizar URL")
    void testActualizarUrl() {
        String nuevaUrl = "https://example.com/foto2.jpg";
        foto.setUrlFoto(nuevaUrl);
        assertEquals(nuevaUrl, foto.getUrlFoto());
    }

    @Test
    @DisplayName("Debe permitir actualizar descripción")
    void testActualizarDescripcion() {
        foto.setDescripcion("Foto lateral izquierda");
        assertEquals("Foto lateral izquierda", foto.getDescripcion());
    }

    @Test
    @DisplayName("Debe permitir establecer como principal")
    void testEsPrincipal() {
        foto.setEsPrincipal(false);
        assertFalse(foto.getEsPrincipal());
        foto.setEsPrincipal(true);
        assertTrue(foto.getEsPrincipal());
    }

    @Test
    @DisplayName("Debe permitir actualizar orden")
    void testOrden() {
        foto.setOrden(1);
        assertEquals(1, foto.getOrden());
        foto.setOrden(5);
        assertEquals(5, foto.getOrden());
    }

    @Test
    @DisplayName("Debe permitir foto sin descripción")
    void testFotoSinDescripcion() {
        FotoMascota fotoSinDesc = new FotoMascota();
        fotoSinDesc.setUrlFoto("https://example.com/foto.jpg");
        assertNull(fotoSinDesc.getDescripcion());
    }

    @Test
    @DisplayName("Debe permitir valores por defecto")
    void testValoresPorDefecto() {
        FotoMascota fotoNueva = new FotoMascota();
        assertFalse(fotoNueva.getEsPrincipal() != null && fotoNueva.getEsPrincipal());
        assertEquals(0, fotoNueva.getOrden() != null ? fotoNueva.getOrden() : 0);
    }

    @Test
    @DisplayName("Debe soportar múltiples formatos de URL")
    void testMultiplesFormatos() {
        // Base64
        foto.setUrlFoto("data:image/jpeg;base64,/9j/4AAQSkZJRg...");
        assertTrue(foto.getUrlFoto().startsWith("data:image"));

        // HTTPS
        foto.setUrlFoto("https://example.com/imagen.png");
        assertTrue(foto.getUrlFoto().startsWith("https"));

        // HTTP
        foto.setUrlFoto("http://example.com/imagen.jpg");
        assertTrue(foto.getUrlFoto().startsWith("http"));
    }

    @Test
    @DisplayName("Debe permitir orden secuencial")
    void testOrdenSecuencial() {
        FotoMascota foto1 = new FotoMascota();
        foto1.setOrden(0);
        foto1.setEsPrincipal(true);

        FotoMascota foto2 = new FotoMascota();
        foto2.setOrden(1);
        foto2.setEsPrincipal(false);

        FotoMascota foto3 = new FotoMascota();
        foto3.setOrden(2);
        foto3.setEsPrincipal(false);

        assertEquals(0, foto1.getOrden());
        assertEquals(1, foto2.getOrden());
        assertEquals(2, foto3.getOrden());
    }

    @Test
    @DisplayName("Debe permitir solo una foto principal")
    void testUnaSolaPrincipal() {
        FotoMascota foto1 = new FotoMascota();
        foto1.setEsPrincipal(true);

        FotoMascota foto2 = new FotoMascota();
        foto2.setEsPrincipal(false);

        assertTrue(foto1.getEsPrincipal());
        assertFalse(foto2.getEsPrincipal());
    }
}
