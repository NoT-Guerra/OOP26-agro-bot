package it.unibo.agrobot.model;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

class ConcurrencyTest {

    @Test
    void testInventoryConcurrency() throws InterruptedException {
        Inventory inventory = new Inventory(3);
        int iterations = 10000;

        CountDownLatch startLatch = new CountDownLatch(1); //per sincronizzare l'avvio dei thread
        CountDownLatch endLatch = new CountDownLatch(2); //per aspettare la fine totale dei thread

        AtomicBoolean hasException = new AtomicBoolean(false); //flag atomico per rilevare eccezioni in modo thread-safe

        // thread 1: Modifica l'inventario
        Thread writer = new Thread(() -> {
            try {
                startLatch.await();
                for (int i = 0; i < iterations; i++) {
                    if (i % 2 == 0) {
                        inventory.addItem("Seed", ItemType.SEED);
                    } else {
                        inventory.removeItem("Seed");
                    }
                    if (i % 1000 == 0) {
                        inventory.addSlot();
                    }
                }
            } catch (Exception e) {
                hasException.set(true);
                e.printStackTrace();
            } finally {
                endLatch.countDown();
            }
        });

        // thread 2: Legge l'inventario
        Thread reader = new Thread(() -> {
            try {
                startLatch.await();
                for (int i = 0; i < iterations; i++) {
                    inventory.getItemCount("Seed");
                    inventory.findItemType("Seed");
                    inventory.getSlotCount();
                }
            } catch (Exception e) {
                hasException.set(true);
                e.printStackTrace();
            } finally {
                endLatch.countDown();
            }
        });

        writer.start();
        reader.start();

        // per far partire entrambi i thread simultaneamente
        startLatch.countDown();

        // aspettiamo che finiscano
        boolean completed = endLatch.await(5, TimeUnit.SECONDS);

        assertTrue(completed, "I thread non hanno terminato in tempo (possibile deadlock)");
        assertFalse(hasException.get(), "E' stata lanciata un'eccezione (es. ConcurrentModificationException)");
    }

    @Test
    void testGridConcurrency() throws InterruptedException {
        GridImpl grid = new GridImpl(10, 10);
        int iterations = 10000;

        CountDownLatch startLatch = new CountDownLatch(1);
        CountDownLatch endLatch = new CountDownLatch(2);
        AtomicBoolean hasException = new AtomicBoolean(false);

        // thread 1: Modifica le celle della griglia
        Thread writer = new Thread(() -> {
            try {
                startLatch.await();
                for (int i = 0; i < iterations; i++) {
                    Tile newTile = new TileImpl(new Position(5, 5), TileType.SOIL);
                    grid.setTile(5, 5, newTile);
                }
            } catch (Exception e) {
                hasException.set(true);
                e.printStackTrace();
            } finally {
                endLatch.countDown();
            }
        });

        // thread 2: Legge le celle della griglia
        Thread reader = new Thread(() -> {
            try {
                startLatch.await();
                for (int i = 0; i < iterations; i++) {
                    grid.getTile(5, 5).ifPresent(t -> t.getType());
                    grid.isInBounds(5, 5);
                }
            } catch (Exception e) {
                hasException.set(true);
                e.printStackTrace();
            } finally {
                endLatch.countDown();
            }
        });

        writer.start();
        reader.start();

        startLatch.countDown();

        boolean completed = endLatch.await(5, TimeUnit.SECONDS);

        assertTrue(completed, "I thread non hanno terminato in tempo su grid");
        assertFalse(hasException.get(), "E' stata lanciata un'eccezione in lettura/scrittura su grid");
    }
}
