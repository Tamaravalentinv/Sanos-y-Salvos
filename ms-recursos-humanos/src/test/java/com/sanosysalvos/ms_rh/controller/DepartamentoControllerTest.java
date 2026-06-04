package com.sanosysalvos.ms_rh.controller;

import com.sanosysalvos.ms_rh.model.Departamento;
import com.sanosysalvos.ms_rh.service.DepartamentoService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DepartamentoControllerTest {

    @Mock
    private DepartamentoService departamentoService;

    @InjectMocks
    private DepartamentoController controller;

    @Test
    void crearYListarDepartamentos() {
        Departamento departamento = departamento();
        when(departamentoService.crearDepartamento(departamento)).thenReturn(departamento);
        when(departamentoService.obtenerTodosDepartamentos()).thenReturn(List.of(departamento));

        var creado = controller.crearDepartamento(departamento);

        assertEquals(HttpStatus.CREATED, creado.getStatusCode());
        assertSame(departamento, creado.getBody());
        assertEquals(1, controller.obtenerTodosDepartamentos().getBody().size());
    }

    @Test
    void busquedasIndividualesRespondenOkONotFound() {
        Departamento departamento = departamento();
        when(departamentoService.obtenerDepartamentoPorId(1L)).thenReturn(Optional.of(departamento));
        when(departamentoService.obtenerDepartamentoPorId(2L)).thenReturn(Optional.empty());
        when(departamentoService.obtenerDepartamentoPorNombre("TI")).thenReturn(Optional.of(departamento));
        when(departamentoService.obtenerDepartamentoPorNombre("Legal")).thenReturn(Optional.empty());

        assertEquals(HttpStatus.OK, controller.obtenerDepartamentoPorId(1L).getStatusCode());
        assertEquals(HttpStatus.NOT_FOUND, controller.obtenerDepartamentoPorId(2L).getStatusCode());
        assertEquals(HttpStatus.OK, controller.obtenerDepartamentoPorNombre("TI").getStatusCode());
        assertEquals(HttpStatus.NOT_FOUND, controller.obtenerDepartamentoPorNombre("Legal").getStatusCode());
    }

    @Test
    void actualizacionYEliminacionMapeanEstadosHttp() {
        Departamento departamento = departamento();
        when(departamentoService.actualizarDepartamento(1L, departamento)).thenReturn(departamento);
        when(departamentoService.actualizarDepartamento(2L, departamento)).thenReturn(null);
        when(departamentoService.eliminarDepartamento(1L)).thenReturn(true);
        when(departamentoService.eliminarDepartamento(2L)).thenReturn(false);

        assertEquals(HttpStatus.OK, controller.actualizarDepartamento(1L, departamento).getStatusCode());
        assertEquals(HttpStatus.NOT_FOUND, controller.actualizarDepartamento(2L, departamento).getStatusCode());
        assertEquals(HttpStatus.NO_CONTENT, controller.eliminarDepartamento(1L).getStatusCode());
        assertEquals(HttpStatus.NOT_FOUND, controller.eliminarDepartamento(2L).getStatusCode());
    }

    private Departamento departamento() {
        Departamento departamento = new Departamento();
        departamento.setNombre("TI");
        departamento.setDescripcion("Tecnologia");
        departamento.setGerentId(3L);
        departamento.setEstado("ACTIVO");
        return departamento;
    }
}
