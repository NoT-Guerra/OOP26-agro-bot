package it.unibo.agrobot.view;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import org.junit.jupiter.api.Test;

import it.unibo.agrobot.model.Drone;
import it.unibo.agrobot.model.DroneImpl;
import it.unibo.agrobot.model.ItemType;
import it.unibo.agrobot.model.Position;

class HUDViewConcurrencyTest {

    @Test
    void testConcurrentModificationAndRender() throws InterruptedException {
        // fase iniziale
        Drone drone = new DroneImpl(new Position(0, 0));
        HUDView hudView = new HUDView(drone);
        BufferedImage image = new BufferedImage(800, 600, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = image.createGraphics();

        int durationMillis = 2000;
        long startTime = System.currentTimeMillis();
        
        AtomicBoolean isRunning = new AtomicBoolean(true);
        AtomicReference<Exception> threadException = new AtomicReference<>();

        // thread di modifica continua dell'inventario
        Thread modifierThread = new Thread(() -> {
            while (isRunning.get() && (System.currentTimeMillis() - startTime) < durationMillis) {
                // aggiunge e rimuove rapidamente oggetti
                drone.getInventory().addItem("Wheat", ItemType.CROP);
                drone.getInventory().removeItem("Wheat");
            }
        });

        // thread di rendering continuo dell'HUD
        Thread renderThread = new Thread(() -> {
            while (isRunning.get() && (System.currentTimeMillis() - startTime) < durationMillis) {
                try {
                    hudView.draw(g2d, 800, 600);
                } catch (Exception e) {
                    threadException.set(e);
                    isRunning.set(false);
                }
            }
        });

        modifierThread.start();
        renderThread.start();

        modifierThread.join();
        renderThread.join();
        
        g2d.dispose();

        // verifichiamo che non ci siano state eccezioni
        assertDoesNotThrow(() -> {
            if (threadException.get() != null) {
                throw threadException.get();
            }
        }, "L'UI ha subito un crash durante l'aggiornamento concorrente dell'inventario!");
    }
}