package it.unibo.agrobot.controller;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class GameLoopTest {

    @Test
    void testUpdateRate() throws InterruptedException {
        // array per contare le chiamate ad update dal thread del GameLoop
        final int[] updateCount = { 0 };

        GameLoop gameLoop = new GameLoop() {
            @Override
            protected void update() {
                updateCount[0]++;
                super.update();
            }
        };

        // avviamo il loop di gioco
        gameLoop.start();

        // mettiamo in attesa il thread principale per 1 secondo
        Thread.sleep(1000);

        // fermiamo il loop
        gameLoop.stop();

        // verifichiamo il numero di update eseguiti
        int actualCount = updateCount[0];

        // ci aspettiamo che il metodo update sia stato chiamato circa 60 volte
        // (UPS=60).
        // diamo un margine di tolleranza (es. 50-70) per la precisione di Thread.sleep
        assertTrue(actualCount >= 50 && actualCount <= 70,
                "L'update dovrebbe essere chiamato circa 60 volte in un secondo. Volte effettive: " + actualCount);
    }
}
