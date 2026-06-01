package com.sanosysalvos.ms_notificaciones;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
public class MsNotificacionesApplicationTests {

    @Test
    void contextLoads() {
        assertNotNull(MsNotificacionesApplication.class);
    }
}
