package com.sanosysalvos.ms_mascotas.service;

import com.sanosysalvos.ms_mascotas.model.*;
import com.sanosysalvos.ms_mascotas.repository.ReporteRepository;
import com.sanosysalvos.ms_mascotas.repository.MascotaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@DisplayName("ReporteService Tests")
@ExtendWith(MockitoExtension.class)
class ReporteServiceTest {

    @Mock
    private ReporteRepository reporteRepository;

    @Mock
    private MascotaRepository mascotaRepository;

    @InjectMocks
    private ReporteService reporteService;

    private Reporte reporte;
    private Mascota mascota;
    private CaracteristicaMascota caracteristica;

    @BeforeEach
    void setUp() {
        caracteristica = new CaracteristicaMascota();
        caracteristica.setId(1L);
        caracteristica.setTamaño("MEDIANO");
        caracteristica.setSexo("MACHO");
        caracteristica.setEdadAproximada("2 años");
        caracteristica.setDescripcionFisica("Perro activo");

        mascota = new Mascota();
        mascota.setId(1L);
        mascota.setNombre("Rex");
        mascota.setTipo("PERRO");
        mascota.setRaza("Labrador");
        mascota.setColor("Negro");
        mascota.setCaracteristica(caracteristica);

        reporte = new Reporte();
        reporte.setId(1L);
        reporte.setTipo(TipoReporte.PERDIDA);
        reporte.setEstado(EstadoReporte.ABIERTO);
        reporte.setUsuarioId(1L);
        reporte.setMascota(mascota);
        reporte.setUbicacion("Parque Central");
        reporte.setLatitud(40.7128);
        reporte.setLongitud(-74.0060);
        reporte.setFechaIncidente(LocalDateTime.now().minusHours(2));
        reporte.setDescripcion("Perro desaparecido en el parque");
        reporte.setTelefonoContacto("3001234567");
        reporte.setEmailContacto("user@example.com");
        reporte.setNumVisualizaciones(0);
        reporte.setPrioridad(3);
    }

    @Test
    @DisplayName("Debe crear un reporte correctamente")
    void testCrearReporte() {
        when(mascotaRepository.save(any(Mascota.class))).thenReturn(mascota);
        when(reporteRepository.save(any(Reporte.class))).thenReturn(reporte);

        Reporte resultado = reporteService.crearReporte(
            TipoReporte.PERDIDA, 1L, mascota, "Parque Central", 40.7128, -74.0060,
            LocalDateTime.now(), "Perro perdido", "3001234567", "user@example.com"
        );

        assertNotNull(resultado);
        assertEquals(TipoReporte.PERDIDA, resultado.getTipo());
        assertEquals(EstadoReporte.ABIERTO, resultado.getEstado());
        assertEquals(1L, resultado.getUsuarioId());
        verify(mascotaRepository, times(1)).save(any(Mascota.class));
        verify(reporteRepository, times(1)).save(any(Reporte.class));
    }

    @Test
    @DisplayName("Debe obtener reporte por ID")
    void testObtenerPorId() {
        when(reporteRepository.findById(1L)).thenReturn(Optional.of(reporte));

        Optional<Reporte> resultado = reporteService.obtenerPorId(1L);

        assertTrue(resultado.isPresent());
        assertEquals(1L, resultado.get().getId());
        assertEquals(TipoReporte.PERDIDA, resultado.get().getTipo());
    }

    @Test
    @DisplayName("Debe retornar vacío cuando reporte no existe")
    void testObtenerPorIdNoExiste() {
        when(reporteRepository.findById(999L)).thenReturn(Optional.empty());

        Optional<Reporte> resultado = reporteService.obtenerPorId(999L);

        assertFalse(resultado.isPresent());
    }

    @Test
    @DisplayName("Debe obtener todos los reportes")
    void testObtenerTodos() {
        List<Reporte> reportes = new ArrayList<>();
        reportes.add(reporte);
        when(reporteRepository.findAll()).thenReturn(reportes);

        List<Reporte> resultado = reporteService.obtenerTodos();

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals(TipoReporte.PERDIDA, resultado.get(0).getTipo());
    }

    @Test
    @DisplayName("Debe obtener reportes perdidos")
    void testObtenerPerdidos() {
        List<Reporte> reportes = new ArrayList<>();
        reportes.add(reporte);
        when(reporteRepository.findByTipo(TipoReporte.PERDIDA)).thenReturn(reportes);

        List<Reporte> resultado = reporteService.obtenerPerdidos();

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertTrue(resultado.stream().allMatch(r -> r.getTipo() == TipoReporte.PERDIDA));
    }

    @Test
    @DisplayName("Debe obtener reportes encontrados")
    void testObtenerEncontrados() {
        Reporte reporteEncontrado = new Reporte();
        reporteEncontrado.setId(2L);
        reporteEncontrado.setTipo(TipoReporte.ENCONTRADA);
        reporteEncontrado.setEstado(EstadoReporte.ABIERTO);

        List<Reporte> reportes = new ArrayList<>();
        reportes.add(reporteEncontrado);
        when(reporteRepository.findByTipo(TipoReporte.ENCONTRADA)).thenReturn(reportes);

        List<Reporte> resultado = reporteService.obtenerEncontrados();

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertTrue(resultado.stream().allMatch(r -> r.getTipo() == TipoReporte.ENCONTRADA));
    }

    @Test
    @DisplayName("Debe obtener reportes activos")
    void testObtenerActivos() {
        List<Reporte> reportes = new ArrayList<>();
        reportes.add(reporte);
        when(reporteRepository.findByTipoAndEstado(TipoReporte.PERDIDA, EstadoReporte.ABIERTO))
            .thenReturn(reportes);

        List<Reporte> resultado = reporteService.obtenerActivos();

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertTrue(resultado.stream().allMatch(r -> r.getEstado() == EstadoReporte.ABIERTO));
    }

    @Test
    @DisplayName("Debe obtener reportes por usuario")
    void testObtenerPorUsuario() {
        List<Reporte> reportes = new ArrayList<>();
        reportes.add(reporte);
        when(reporteRepository.findByUsuarioId(1L)).thenReturn(reportes);

        List<Reporte> resultado = reporteService.obtenerPorUsuario(1L);

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals(1L, resultado.get(0).getUsuarioId());
    }

    @Test
    @DisplayName("Debe obtener reportes por organización")
    void testObtenerPorOrganizacion() {
        reporte.setOrganizacionId(5L);
        List<Reporte> reportes = new ArrayList<>();
        reportes.add(reporte);
        when(reporteRepository.findByOrganizacionId(5L)).thenReturn(reportes);

        List<Reporte> resultado = reporteService.obtenerPorOrganizacion(5L);

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals(5L, resultado.get(0).getOrganizacionId());
    }

    @Test
    @DisplayName("Debe obtener reportes por características")
    void testObtenerPorCaracteristicas() {
        List<Reporte> reportes = new ArrayList<>();
        reportes.add(reporte);
        when(reporteRepository.findByCaracteristicas(TipoReporte.PERDIDA, "Negro", "Labrador"))
            .thenReturn(reportes);

        List<Reporte> resultado = reporteService.obtenerPorCaracteristicas(
            TipoReporte.PERDIDA, "Negro", "Labrador"
        );

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
    }

    @Test
    @DisplayName("Debe obtener reportes por proximidad geográfica")
    void testObtenerPorCercaniaGeografica() {
        List<Reporte> reportes = new ArrayList<>();
        reportes.add(reporte);
        when(reporteRepository.findPorCercaniaGeografica(40.7128, -74.0060)).thenReturn(reportes);

        List<Reporte> resultado = reporteService.obtenerPorCercaniaGeografica(40.7128, -74.0060);

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
    }

    @Test
    @DisplayName("Debe obtener reportes urgentes")
    void testObtenerUrgentes() {
        reporte.setRequiereUrgencia(true);
        List<Reporte> reportes = new ArrayList<>();
        reportes.add(reporte);
        when(reporteRepository.findByRequiereUrgencia(true)).thenReturn(reportes);

        List<Reporte> resultado = reporteService.obtenerPorUrgencia();

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
    }

    @Test
    @DisplayName("Debe actualizar reporte exitosamente")
    void testActualizarReporte() {
        when(reporteRepository.findById(1L)).thenReturn(Optional.of(reporte));
        when(reporteRepository.save(any(Reporte.class))).thenReturn(reporte);

        Reporte reporteActualizado = new Reporte();
        reporteActualizado.setDescripcion("Descripción actualizada");
        reporteActualizado.setEstado(EstadoReporte.EN_PROGRESO);
        reporteActualizado.setPrioridad(5);

        Reporte resultado = reporteService.actualizarReporte(1L, reporteActualizado);

        assertNotNull(resultado);
        verify(reporteRepository, times(1)).findById(1L);
        verify(reporteRepository, times(1)).save(any(Reporte.class));
    }

    @Test
    @DisplayName("Debe lanzar excepción si reporte no existe al actualizar")
    void testActualizarReporteNoExiste() {
        when(reporteRepository.findById(999L)).thenReturn(Optional.empty());

        Reporte reporteActualizado = new Reporte();
        assertThrows(RuntimeException.class, () -> reporteService.actualizarReporte(999L, reporteActualizado));
    }

    @Test
    @DisplayName("Debe cambiar estado de reporte")
    void testCambiarEstado() {
        when(reporteRepository.findById(1L)).thenReturn(Optional.of(reporte));
        when(reporteRepository.save(any(Reporte.class))).thenReturn(reporte);

        reporteService.cambiarEstado(1L, EstadoReporte.RESUELTO);

        verify(reporteRepository, times(1)).findById(1L);
        verify(reporteRepository, times(1)).save(any(Reporte.class));
    }

    @Test
    @DisplayName("Debe incrementar visualizaciones")
    void testIncrementarVisualizaciones() {
        when(reporteRepository.findById(1L)).thenReturn(Optional.of(reporte));
        when(reporteRepository.save(any(Reporte.class))).thenReturn(reporte);

        int vizActuales = reporte.getNumVisualizaciones();
        reporteService.incrementarVisualizaciones(1L);

        verify(reporteRepository, times(1)).findById(1L);
        verify(reporteRepository, times(1)).save(any(Reporte.class));
    }

    @Test
    @DisplayName("Debe eliminar reporte")
    void testEliminarReporte() {
        reporteService.eliminarReporte(1L);
        verify(reporteRepository, times(1)).deleteById(1L);
    }

    @Test
    @DisplayName("Debe obtener reportes por tipo")
    void testObtenerPorTipo() {
        List<Reporte> reportes = new ArrayList<>();
        reportes.add(reporte);
        when(reporteRepository.findByTipo(TipoReporte.PERDIDA)).thenReturn(reportes);

        List<Reporte> resultado = reporteService.obtenerPorTipo(TipoReporte.PERDIDA);

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
    }

    @Test
    @DisplayName("Debe obtener reportes por estado")
    void testObtenerPorEstado() {
        List<Reporte> reportes = new ArrayList<>();
        reportes.add(reporte);
        when(reporteRepository.findByEstado(EstadoReporte.ABIERTO)).thenReturn(reportes);

        List<Reporte> resultado = reporteService.obtenerPorEstado(EstadoReporte.ABIERTO);

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
    }
}
