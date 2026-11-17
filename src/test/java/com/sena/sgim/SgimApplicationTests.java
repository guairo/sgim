package com.sena.sgim;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

/**
 * Smoke test: verifica que el contexto de Spring arranca correctamente.
 */
@SpringBootTest
@ActiveProfiles("dev")
class SgimApplicationTests {

    @Test
    void contextLoads() { }
}
