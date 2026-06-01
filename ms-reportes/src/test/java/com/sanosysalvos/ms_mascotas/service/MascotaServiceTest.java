package com.sanosysalvos.ms_mascotas.service;

import com.sanosysalvos.ms_mascotas.model.CaracteristicaMascota;
import com.sanosysalvos.ms_mascotas.model.Mascota;
import com.sanosysalvos.ms_mascotas.repository.MascotaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@DisplayName("MascotaService Tests")
@ExtendWith(MockitoExtension.class)
class MascotaServiceTest {

    @Mock
    private MascotaRepository mascotaRepository;

    @InjectMocks
    private MascotaService mascotaService;

    private Mascota mascota;
    private CaracteristicaMascota caracteristica;

    @BeforeEach
    void setUp() {
        caracteristica = new CaracteristicaMascota();
        caracteristica.setId(1L);
        caracteristica.setTamaño("GRANDE");
        caracteristica.setSexo("HEMBRA");
        caracteristica.setEdadAproximada("5 años");
        caracteristica.setDescripcionFisica("Gato tranquilo");
        caracteristica.setEsVacunado(true);
        caracteristica.setEsEsterilizado(true);

        mascota = new Mascota();
        mascota.setId(1L);
        mascota.setNombre("Whiskers");
        mascota.setTipo("GATO");
        mascota.setRaza("Persa");
        mascota.setColor("Blanco");
        mascota.setCaracteristica(caracteristica);
    }

    @Test
    @DisplayName("Debe guardar mascota correctamente")
    void testGuardar() {
        when(mascotaRepository.save(any(Mascota.class))).thenReturn(mascota);

        Mascota resultado = mascotaService.guardar(mascota);

        assertNotNull(resultado);
        assertEquals("Whiskers", resultado.getNombre());
        assertEquals("GATO", resultado.getTipo());
        assertEquals("Persa", resultado.getRaza());
        verify(mascotaRepository, times(1)).save(any(Mascota.class));
    }

    @Test
    @DisplayName("Debe listar todas las mascotas")
    void testListar() {
        List<Mascota> mascotas = new ArrayList<>();
        mascotas.add(mascota);
        when(mascotaRepository.findAll()).thenReturn(mascotas);

        List<Mascota> resultado = mascotaService.listar();

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals("Whiskers", resultado.get(0).getNombre());
    }

    @Test
    @DisplayName("Debe retornar lista vacía cuando no hay mascotas")
    void testListarVacio() {
        when(mascotaRepository.findAll()).thenReturn(new ArrayList<>());

        List<Mascota> resultado = mascotaService.listar();

        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());
    }

    @Test
    @DisplayName("Debe guardar múltiples mascotas")
    void testGuardarMultiples() {
        List<Mascota> mascotas = new ArrayList<>();
        
        Mascota mascota2 = new Mascota();
        mascota2.setId(2L);
        mascota2.setNombre("Firulais");
        mascota2.setTipo("PERRO");
        mascota2.setRaza("Pastor Alemán");
        mascota2.setColor("Marrón");

        mascotas.add(mascota);
        mascotas.add(mascota2);

        when(mascotaRepository.save(any(Mascota.class))).thenReturn(mascota, mascota2);
        when(mascotaRepository.findAll()).thenReturn(mascotas);

        mascotaService.guardar(mascota);
        mascotaService.guardar(mascota2);
        List<Mascota> resultado = mascotaService.listar();

        assertEquals(2, resultado.size());
    }

    @Test
    @DisplayName("Debe guardar mascota con características vacías")
    void testGuardarMascotaSinCaracteristicas() {
        Mascota mascotaSinCarac = new Mascota();
        mascotaSinCarac.setId(3L);
        mascotaSinCarac.setNombre("Rocky");
        mascotaSinCarac.setTipo("PERRO");
        mascotaSinCarac.setRaza("Bulldog");
        mascotaSinCarac.setColor("Gris");

        when(mascotaRepository.save(any(Mascota.class))).thenReturn(mascotaSinCarac);

        Mascota resultado = mascotaService.guardar(mascotaSinCarac);

        assertNotNull(resultado);
        assertEquals("Rocky", resultado.getNombre());
        assertNull(resultado.getCaracteristica());
    }
}
