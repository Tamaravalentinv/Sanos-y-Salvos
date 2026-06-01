package com.sanosysalvos.ms_rh.model;

import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class RhModelTest {

    @Test
    void empleadoAsignaEstadoActivoPorDefectoYFechas() {
        Empleado empleado = new Empleado();

        ReflectionTestUtils.invokeMethod(empleado, "onCreate");

        assertEquals("ACTIVO", empleado.getEstado());
        assertNotNull(empleado.getCreatedAt());
        assertNotNull(empleado.getUpdatedAt());
    }

    @Test
    void departamentoAsignaEstadoActivoPorDefectoYActualizaFecha() {
        Departamento departamento = new Departamento();
        ReflectionTestUtils.invokeMethod(departamento, "onCreate");
        LocalDateTime creado = departamento.getUpdatedAt();

        ReflectionTestUtils.invokeMethod(departamento, "onUpdate");

        assertEquals("ACTIVO", departamento.getEstado());
        assertTrue(!departamento.getUpdatedAt().isBefore(creado));
    }

    @Test
    void permisoInicializaYActualizaFechas() {
        Permiso permiso = new Permiso();
        ReflectionTestUtils.invokeMethod(permiso, "onCreate");
        LocalDateTime creado = permiso.getUpdatedAt();

        ReflectionTestUtils.invokeMethod(permiso, "onUpdate");

        assertNotNull(permiso.getCreatedAt());
        assertTrue(!permiso.getUpdatedAt().isBefore(creado));
    }
}
