package com.sanosysalvos.ms_proyectos.service;

import com.sanosysalvos.ms_proyectos.model.Proyecto;
import com.sanosysalvos.ms_proyectos.repository.ProyectoRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProyectoServiceTest {

    @Mock
    private ProyectoRepository proyectoRepository;

    @InjectMocks
    private ProyectoService proyectoService;

    @Test
    void crearProyectoGuardaEntidad() {
        Proyecto proyecto = proyecto("Original");
        when(proyectoRepository.save(proyecto)).thenReturn(proyecto);

        Proyecto resultado = proyectoService.crearProyecto(proyecto);

        assertSame(proyecto, resultado);
        verify(proyectoRepository).save(proyecto);
    }

    @Test
    void consultasDeleganEnRepositorio() {
        Proyecto proyecto = proyecto("Busqueda");
        when(proyectoRepository.findById(7L)).thenReturn(Optional.of(proyecto));
        when(proyectoRepository.findAll()).thenReturn(List.of(proyecto));
        when(proyectoRepository.findByResponsableId(3L)).thenReturn(List.of(proyecto));
        when(proyectoRepository.findByEstado("ACTIVO")).thenReturn(List.of(proyecto));

        assertEquals(Optional.of(proyecto), proyectoService.obtenerProyectoPorId(7L));
        assertEquals(List.of(proyecto), proyectoService.obtenerTodosProyectos());
        assertEquals(List.of(proyecto), proyectoService.obtenerProyectosPorResponsable(3L));
        assertEquals(List.of(proyecto), proyectoService.obtenerProyectosPorEstado("ACTIVO"));
    }

    @Test
    void actualizarProyectoExistenteCopiaCamposPermitidos() {
        Proyecto existente = proyecto("Antes");
        existente.setId(1L);
        Proyecto actualizado = proyecto("Despues");
        actualizado.setDescripcion("Nueva descripcion");
        actualizado.setEstado("COMPLETADO");
        actualizado.setFechaFin(LocalDate.now().plusDays(3));
        actualizado.setPresupuesto(5000);
        actualizado.setResponsableId(22L);
        when(proyectoRepository.findById(1L)).thenReturn(Optional.of(existente));
        when(proyectoRepository.save(any(Proyecto.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Proyecto resultado = proyectoService.actualizarProyecto(1L, actualizado);

        assertNotNull(resultado);
        assertEquals("Despues", resultado.getNombre());
        assertEquals("Nueva descripcion", resultado.getDescripcion());
        assertEquals("COMPLETADO", resultado.getEstado());
        assertEquals(5000, resultado.getPresupuesto());
        assertEquals(22L, resultado.getResponsableId());
    }

    @Test
    void actualizarProyectoInexistenteRetornaNull() {
        when(proyectoRepository.findById(99L)).thenReturn(Optional.empty());

        assertNull(proyectoService.actualizarProyecto(99L, proyecto("No existe")));
        verify(proyectoRepository, never()).save(any());
    }

    @Test
    void eliminarProyectoSoloBorraSiExiste() {
        when(proyectoRepository.existsById(1L)).thenReturn(true);
        when(proyectoRepository.existsById(2L)).thenReturn(false);

        assertTrue(proyectoService.eliminarProyecto(1L));
        assertFalse(proyectoService.eliminarProyecto(2L));
        verify(proyectoRepository).deleteById(1L);
        verify(proyectoRepository, never()).deleteById(2L);
    }

    private Proyecto proyecto(String nombre) {
        Proyecto proyecto = new Proyecto();
        proyecto.setNombre(nombre);
        proyecto.setDescripcion("Descripcion");
        proyecto.setEstado("ACTIVO");
        proyecto.setFechaInicio(LocalDate.now());
        proyecto.setPresupuesto(1000);
        proyecto.setResponsableId(10L);
        return proyecto;
    }
}
