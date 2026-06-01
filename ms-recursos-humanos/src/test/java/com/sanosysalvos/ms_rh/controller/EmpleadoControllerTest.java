package com.sanosysalvos.ms_rh.controller;

import com.sanosysalvos.ms_rh.model.Empleado;
import com.sanosysalvos.ms_rh.service.EmpleadoService;
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
class EmpleadoControllerTest {

    @Mock
    private EmpleadoService empleadoService;

    @InjectMocks
    private EmpleadoController controller;

    @Test
    void crearYListarEmpleados() {
        Empleado empleado = empleado();
        when(empleadoService.crearEmpleado(empleado)).thenReturn(empleado);
        when(empleadoService.obtenerTodosEmpleados()).thenReturn(List.of(empleado));

        var creado = controller.crearEmpleado(empleado);

        assertEquals(HttpStatus.CREATED, creado.getStatusCode());
        assertSame(empleado, creado.getBody());
        assertEquals(1, controller.obtenerTodosEmpleados().getBody().size());
    }

    @Test
    void busquedasIndividualesRespondenOkONotFound() {
        Empleado empleado = empleado();
        when(empleadoService.obtenerEmpleadoPorId(1L)).thenReturn(Optional.of(empleado));
        when(empleadoService.obtenerEmpleadoPorId(2L)).thenReturn(Optional.empty());
        when(empleadoService.obtenerEmpleadoPorEmail("a@b.cl")).thenReturn(Optional.of(empleado));
        when(empleadoService.obtenerEmpleadoPorCedula("123")).thenReturn(Optional.empty());

        assertEquals(HttpStatus.OK, controller.obtenerEmpleadoPorId(1L).getStatusCode());
        assertEquals(HttpStatus.NOT_FOUND, controller.obtenerEmpleadoPorId(2L).getStatusCode());
        assertEquals(HttpStatus.OK, controller.obtenerEmpleadoPorEmail("a@b.cl").getStatusCode());
        assertEquals(HttpStatus.NOT_FOUND, controller.obtenerEmpleadoPorCedula("123").getStatusCode());
    }

    @Test
    void filtrosActualizacionYEliminacionMapeanEstadosHttp() {
        Empleado empleado = empleado();
        when(empleadoService.obtenerEmpleadosPorDepartamento(4L)).thenReturn(List.of(empleado));
        when(empleadoService.obtenerEmpleadosPorEstado("ACTIVO")).thenReturn(List.of(empleado));
        when(empleadoService.actualizarEmpleado(1L, empleado)).thenReturn(empleado);
        when(empleadoService.actualizarEmpleado(2L, empleado)).thenReturn(null);
        when(empleadoService.eliminarEmpleado(1L)).thenReturn(true);
        when(empleadoService.eliminarEmpleado(2L)).thenReturn(false);

        assertEquals(1, controller.obtenerEmpleadosPorDepartamento(4L).getBody().size());
        assertEquals(1, controller.obtenerEmpleadosPorEstado("ACTIVO").getBody().size());
        assertEquals(HttpStatus.OK, controller.actualizarEmpleado(1L, empleado).getStatusCode());
        assertEquals(HttpStatus.NOT_FOUND, controller.actualizarEmpleado(2L, empleado).getStatusCode());
        assertEquals(HttpStatus.NO_CONTENT, controller.eliminarEmpleado(1L).getStatusCode());
        assertEquals(HttpStatus.NOT_FOUND, controller.eliminarEmpleado(2L).getStatusCode());
    }

    private Empleado empleado() {
        Empleado empleado = new Empleado();
        empleado.setNombre("Ana");
        empleado.setApellido("Perez");
        empleado.setEmail("a@b.cl");
        empleado.setCedula("123");
        empleado.setDepartamentoId(4L);
        empleado.setCargo("Analista");
        empleado.setSalario(1000.0);
        empleado.setFechaContratacion(LocalDate.now());
        return empleado;
    }
}
