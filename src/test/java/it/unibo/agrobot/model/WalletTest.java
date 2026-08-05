package it.unibo.agrobot.model;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class WalletTest {

    private Wallet wallet;

    @BeforeEach
    @SuppressWarnings("unused")
    void setUp() {
        wallet = new Wallet(100.0);
    }

    @Test
    void testInitialBalance() {
        assertEquals(100.0, wallet.getBalance(), "Il saldo iniziale deve corrispondere a quanto impostato");
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () -> new Wallet(-50.0), "Deve lanciare eccezione per saldo negativo");
        assertEquals("Il saldo iniziale non può essere negativo.", e.getMessage());
    }

    @Test
    void testAddFunds() {
        wallet.addFunds(50.0);
        assertEquals(150.0, wallet.getBalance(), "Il saldo dopo l'aggiunta deve essere incrementato correttamente");
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () -> wallet.addFunds(-10.0), "Non deve essere possibile aggiungere importi negativi");
        assertEquals("Impossibile aggiungere un ammontare negativo.", e.getMessage());
    }

    @Test
    void testDeductFunds() {
        assertTrue(wallet.deductFunds(40.0), "La detrazione con fondi sufficienti deve restituire true");
        assertEquals(60.0, wallet.getBalance(), "Il saldo deve essere ridotto dell'importo detratto");

        // deve fallire se i fondi vanno sotto zero (fondi non sufficienti)
        assertFalse(wallet.deductFunds(100.0), "La detrazione con fondi insufficienti deve restituire false");
        assertEquals(60.0, wallet.getBalance(), "Il saldo non deve cambiare se la detrazione fallisce");
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () -> wallet.deductFunds(-10.0), "Non deve essere possibile detrarre importi negativi");
        assertEquals("Impossibile dedurre un ammontare negativo.", e.getMessage());
    }

    @Test
    void testHasEnoughFunds() {
        assertTrue(wallet.hasEnoughFunds(100.0), "Deve confermare la presenza di fondi per importi <= saldo");
        assertTrue(wallet.hasEnoughFunds(50.0), "Deve confermare la presenza di fondi per importi < saldo");
        assertFalse(wallet.hasEnoughFunds(150.0), "Deve negare la presenza di fondi per importi > saldo");
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () -> wallet.hasEnoughFunds(-10.0), "Deve lanciare eccezione per importi negativi");
        assertEquals("L'ammontare da verificare non può essere negativo.", e.getMessage());
    }

    @Test
    void testThreadSafetyMultipleTransactions() throws InterruptedException {
        // partiamo da un saldo di 1000.0
        Wallet concurrentWallet = new Wallet(1000.0);
        int threadCount = 100;

        // creiamo un pool di thread per simulare la concorrenza
        ExecutorService executor = Executors.newFixedThreadPool(threadCount);
        CountDownLatch latch = new CountDownLatch(threadCount);

        // lanciamo 100 thread in parallelo: metà aggiungono 10, metà deducono 5
        // il risultato atteso è 1000 + (50 * 10) - (50 * 5) = 1000 + 500 - 250 = 1250.0
        for (int i = 0; i < threadCount; i++) {
            final int index = i;
            executor.execute(() -> {
                try {
                    if (index % 2 == 0) {
                        concurrentWallet.addFunds(10.0);
                    } else {
                        concurrentWallet.deductFunds(5.0);
                    }
                } finally {
                    latch.countDown();
                }
            });
        }

        // aspettiamo che tutti i thread finiscano
        latch.await(5, TimeUnit.SECONDS);
        executor.shutdown();

        // se wallet non fosse thread-safe, ci sarebbero race conditions e il saldo finale risulterebbe errato
        assertEquals(1250.0, concurrentWallet.getBalance(), "Il saldo finale deve essere corretto nonostante le transazioni concorrenti simultanee");
    }
}
