package com.sanosysalvos.ms_rh.service;

import com.sanosysalvos.ms_rh.model.Empleado;
import com.sanosysalvos.ms_rh.repository.EmpleadoRepository;
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
class EmpleadoServiceTest {

    @Mock
    private EmpleadoRepository empleadoRepository;

    @InjectMocks
    private EmpleadoService empleadoService;

    @Test
    void crearYConsultarEmpleadoDeleganEnRepositorio() {
        Empleado empleado = empleado("Ana");
        when(empleadoRepository.save(empleado)).thenReturn(empleado);
        when(empleadoRepository.findById(1L)).thenReturn(Optional.of(empleado));
        when(empleadoRepository.findByEmail("ana@test.com")).thenReturn(Optional.of(empleado));
        when(empleadoRepository.findByCedula("123")).thenReturn(Optional.of(empleado));
        when(empleadoRepository.findAll()).thenReturn(List.of(empleado));
        when(empleadoRepository.findByDepartamentoId(4L)).thenReturn(List.of(empleado));
        when(empleadoRepository.findByEstado("ACTIVO")).thenReturn(List.of(empleado));

        assertSame(empleado, empleadoService.crearEmpleado(empleado));
        assertTrue(empleadoService.obtenerEmpleadoPorId(1L).isPresent());
        assertTrue(empleadoService.obtenerEmpleadoPorEmail("ana@test.com").isPresent());
        assertTrue(empleadoService.obtenerEmpleadoPorCedula("123").isPresent());
        assertEquals(1, empleadoService.obtenerTodosEmpleados().size());
        assertEquals(1, empleadoService.obtenerEmpleadosPorDepartamento(4L).size());
        assertEquals(1, empleadoService.obtenerEmpleadosPorEstado("ACTIVO").size());
    }

    @Test
    void actualizarEmpleadoExistenteCopiaCamposEditables() {
        Empleado existente = empleado("Antes");
        Empleado cambios = empleado("Despues");
        cambios.setApellido("Nuevo");
        cambios.setDepartamentoId(7L);
        cambios.setCargo("Lider");
        cambios.setSalario(2000.0);
        cambios.setEstado("LICENCIA");
        when(empleadoRepository.findById(9L)).thenReturn(Optional.of(existente));
        when(empleadoRepository.save(any(Empleado.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Empleado resultado = empleadoService.actualizarEmpleado(9L, cambios);

        assertEquals("Despues", resultado.getNombre());
        assertEquals("Nuevo", resultado.getApellido());
        assertEquals(7L, resultado.getDepartamentoId());
        assertEquals("Lider", resultado.getCargo());
        assertEquals(2000.0, resultado.getSalario());
        assertEquals("LICENCIA", resultado.getEstado());
    }

    @Test
    void actualizarEmpleadoInexistenteNoGuarda() {
        when(empleadoRepository.findById(9L)).thenReturn(Optional.empty());

        assertNull(empleadoService.actualizarEmpleado(9L, empleado("Nadie")));
        verify(empleadoRepository, never()).save(any());
    }

    @Test
    void eliminarEmpleadoValidaExistencia() {
        when(empleadoRepository.existsById(1L)).thenReturn(true);
        when(empleadoRepository.existsById(2L)).thenReturn(false);

        assertTrue(empleadoService.eliminarEmpleado(1L));
        assertFalse(empleadoService.eliminarEmpleado(2L));
        verify(empleadoRepository).deleteById(1L);
    }

    private Empleado empleado(String nombre) {
        Empleado empleado = new Empleado();
        empleado.setNombre(nombre);
        empleado.setApellido("Perez");
        empleado.setEmail(nombre.toLowerCase() + "@test.com");
        empleado.setCedula("123");
        empleado.setDepartamentoId(1L);
        empleado.setCargo("Analista");
        empleado.setSalario(1000.0);
        empleado.setFechaContratacion(LocalDate.now());
        empleado.setEstado("ACTIVO");
        return empleado;
    }
}
