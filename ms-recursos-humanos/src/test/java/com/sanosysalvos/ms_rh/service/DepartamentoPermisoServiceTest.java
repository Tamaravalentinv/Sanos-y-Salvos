package com.sanosysalvos.ms_rh.service;

import com.sanosysalvos.ms_rh.model.Departamento;
import com.sanosysalvos.ms_rh.model.Permiso;
import com.sanosysalvos.ms_rh.repository.DepartamentoRepository;
import com.sanosysalvos.ms_rh.repository.PermisoRepository;
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
class DepartamentoPermisoServiceTest {

    @Mock
    private DepartamentoRepository departamentoRepository;

    @Mock
    private PermisoRepository permisoRepository;

    @InjectMocks
    private DepartamentoService departamentoService;

    @InjectMocks
    private PermisoService permisoService;

    @Test
    void departamentoCubreConsultasActualizacionYEliminacion() {
        Departamento departamento = departamento("TI");
        Departamento cambios = departamento("Operaciones");
        cambios.setDescripcion("Gestion");
        cambios.setGerentId(8L);
        cambios.setEstado("INACTIVO");
        when(departamentoRepository.findById(1L)).thenReturn(Optional.of(departamento));
        when(departamentoRepository.findById(2L)).thenReturn(Optional.empty());
        when(departamentoRepository.findByNombre("TI")).thenReturn(Optional.of(departamento));
        when(departamentoRepository.findAll()).thenReturn(List.of(departamento));
        when(departamentoRepository.save(any(Departamento.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(departamentoRepository.existsById(1L)).thenReturn(true);

        assertSame(departamento, departamentoService.crearDepartamento(departamento));
        assertTrue(departamentoService.obtenerDepartamentoPorId(1L).isPresent());
        assertTrue(departamentoService.obtenerDepartamentoPorNombre("TI").isPresent());
        assertEquals(1, departamentoService.obtenerTodosDepartamentos().size());
        Departamento actualizado = departamentoService.actualizarDepartamento(1L, cambios);
        assertEquals("Operaciones", actualizado.getNombre());
        assertEquals("Gestion", actualizado.getDescripcion());
        assertEquals(8L, actualizado.getGerentId());
        assertEquals("INACTIVO", actualizado.getEstado());
        assertNull(departamentoService.actualizarDepartamento(2L, cambios));
        assertTrue(departamentoService.eliminarDepartamento(1L));
        verify(departamentoRepository).deleteById(1L);
    }

    @Test
    void permisoCubreConsultasActualizacionYEliminacion() {
        Permiso permiso = permiso("VACACIONES");
        Permiso cambios = permiso("LICENCIA_MEDICA");
        cambios.setDescripcion("Reposo");
        cambios.setEstado("APROBADO");
        when(permisoRepository.findById(1L)).thenReturn(Optional.of(permiso));
        when(permisoRepository.findById(2L)).thenReturn(Optional.empty());
        when(permisoRepository.findAll()).thenReturn(List.of(permiso));
        when(permisoRepository.findByEmpleadoId(3L)).thenReturn(List.of(permiso));
        when(permisoRepository.findByEstado("SOLICITADO")).thenReturn(List.of(permiso));
        when(permisoRepository.save(any(Permiso.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(permisoRepository.existsById(1L)).thenReturn(true);

        assertSame(permiso, permisoService.crearPermiso(permiso));
        assertTrue(permisoService.obtenerPermisoPorId(1L).isPresent());
        assertEquals(1, permisoService.obtenerTodosPermisos().size());
        assertEquals(1, permisoService.obtenerPermisosPorEmpleado(3L).size());
        assertEquals(1, permisoService.obtenerPermisosPorEstado("SOLICITADO").size());
        Permiso actualizado = permisoService.actualizarPermiso(1L, cambios);
        assertEquals("LICENCIA_MEDICA", actualizado.getTipo());
        assertEquals("Reposo", actualizado.getDescripcion());
        assertEquals("APROBADO", actualizado.getEstado());
        assertNull(permisoService.actualizarPermiso(2L, cambios));
        assertTrue(permisoService.eliminarPermiso(1L));
        verify(permisoRepository).deleteById(1L);
    }

    private Departamento departamento(String nombre) {
        Departamento departamento = new Departamento();
        departamento.setNombre(nombre);
        departamento.setDescripcion("Descripcion");
        departamento.setGerentId(1L);
        departamento.setEstado("ACTIVO");
        return departamento;
    }

    private Permiso permiso(String tipo) {
        Permiso permiso = new Permiso();
        permiso.setEmpleadoId(3L);
        permiso.setTipo(tipo);
        permiso.setFechaInicio(LocalDate.now());
        permiso.setFechaFin(LocalDate.now().plusDays(2));
        permiso.setDescripcion("Descripcion");
        permiso.setEstado("SOLICITADO");
        return permiso;
    }
}
