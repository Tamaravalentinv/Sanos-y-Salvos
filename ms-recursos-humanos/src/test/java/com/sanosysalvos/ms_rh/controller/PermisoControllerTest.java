package com.sanosysalvos.ms_rh.controller;

import com.sanosysalvos.ms_rh.model.Permiso;
import com.sanosysalvos.ms_rh.service.PermisoService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PermisoControllerTest {

    @Mock
    private PermisoService permisoService;

    @InjectMocks
    private PermisoController controller;

    @Test
    void crearYListarPermisos() {
        Permiso permiso = permiso();
        when(permisoService.crearPermiso(permiso)).thenReturn(permiso);
        when(permisoService.obtenerTodosPermisos()).thenReturn(List.of(permiso));

        var creado = controller.crearPermiso(permiso);

        assertEquals(HttpStatus.CREATED, creado.getStatusCode());
        assertSame(permiso, creado.getBody());
        assertEquals(1, controller.obtenerTodosPermisos().getBody().size());
    }

    @Test
    void busquedaPorIdRespondeOkONotFound() {
        Permiso permiso = permiso();
        when(permisoService.obtenerPermisoPorId(1L)).thenReturn(Optional.of(permiso));
        when(permisoService.obtenerPermisoPorId(2L)).thenReturn(Optional.empty());

        assertEquals(HttpStatus.OK, controller.obtenerPermisoPorId(1L).getStatusCode());
        assertEquals(HttpStatus.NOT_FOUND, controller.obtenerPermisoPorId(2L).getStatusCode());
    }

    @Test
    void filtrosActualizacionYEliminacionMapeanEstadosHttp() {
        Permiso permiso = permiso();
        when(permisoService.obtenerPermisosPorEmpleado(3L)).thenReturn(List.of(permiso));
        when(permisoService.obtenerPermisosPorEstado("SOLICITADO")).thenReturn(List.of(permiso));
        when(permisoService.actualizarPermiso(1L, permiso)).thenReturn(permiso);
        when(permisoService.actualizarPermiso(2L, permiso)).thenReturn(null);
        when(permisoService.eliminarPermiso(1L)).thenReturn(true);
        when(permisoService.eliminarPermiso(2L)).thenReturn(false);

        assertEquals(1, controller.obtenerPermisosPorEmpleado(3L).getBody().size());
        assertEquals(1, controller.obtenerPermisosPorEstado("SOLICITADO").getBody().size());
        assertEquals(HttpStatus.OK, controller.actualizarPermiso(1L, permiso).getStatusCode());
        assertEquals(HttpStatus.NOT_FOUND, controller.actualizarPermiso(2L, permiso).getStatusCode());
        assertEquals(HttpStatus.NO_CONTENT, controller.eliminarPermiso(1L).getStatusCode());
        assertEquals(HttpStatus.NOT_FOUND, controller.eliminarPermiso(2L).getStatusCode());
    }

    private Permiso permiso() {
        Permiso permiso = new Permiso();
        permiso.setEmpleadoId(3L);
        permiso.setTipo("VACACIONES");
        permiso.setFechaInicio(LocalDate.now());
        permiso.setFechaFin(LocalDate.now().plusDays(3));
        permiso.setDescripcion("Descanso");
        permiso.setEstado("SOLICITADO");
        return permiso;
    }
}
