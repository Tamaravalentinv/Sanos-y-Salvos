package com.sanosysalvos.ms_proyectos.model;

import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class ProyectoTareaModelTest {

    @Test
    void proyectoInicializaFechasDeAuditoria() {
        Proyecto proyecto = new Proyecto(null, "Proyecto", "Desc", "ACTIVO", LocalDate.now(),
                null, 1000, 1L, null, null);

        ReflectionTestUtils.invokeMethod(proyecto, "onCreate");

        assertNotNull(proyecto.getCreatedAt());
        assertNotNull(proyecto.getUpdatedAt());
    }

    @Test
    void proyectoActualizaMarcaTemporal() {
        Proyecto proyecto = new Proyecto();
        LocalDateTime anterior = LocalDateTime.now().minusDays(1);
        proyecto.setUpdatedAt(anterior);

        ReflectionTestUtils.invokeMethod(proyecto, "onUpdate");

        assertTrue(proyecto.getUpdatedAt().isAfter(anterior));
    }

    @Test
    void tareaInicializaYActualizaFechasDeAuditoria() {
        Tarea tarea = new Tarea();

        ReflectionTestUtils.invokeMethod(tarea, "onCreate");
        LocalDateTime creada = tarea.getUpdatedAt();
        ReflectionTestUtils.invokeMethod(tarea, "onUpdate");

        assertNotNull(tarea.getCreatedAt());
        assertTrue(!tarea.getUpdatedAt().isBefore(creada));
    }
}
