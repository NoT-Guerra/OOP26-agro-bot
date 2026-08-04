package it.unibo.agrobot;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class AppTest {
    @Test
    void appLoads() {
        // test di base per assicurarsi che la suite JUnit funzioni
        App app = new App();
        assertNotNull(app, "L'app deve poter essere istanziata");
    }
}
