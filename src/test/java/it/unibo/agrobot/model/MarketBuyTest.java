package it.unibo.agrobot.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class MarketBuyTest {

    private Inventory inventory;
    private Wallet wallet;
    private PriceManager priceManager;
    private Market market;

    @BeforeEach
    @SuppressWarnings("unused")
    void setUp() {
        // inventario con 1 slot
        inventory = new Inventory(1);
        wallet = new Wallet(20.0); // settiamo i crediti iniziali per i test base
        priceManager = new PriceManager();
        priceManager.setBuyPrice("Wheat", ItemType.SEED, 5.0);
        market = new Market(inventory, wallet, priceManager);
    }

    @Test
    void testSuccessfulPurchase() {
        assertEquals(0, inventory.getItemCount("Wheat"));
        assertEquals(20.0, wallet.getBalance());
        boolean buySuccess = market.buySeed("Wheat");
        assertTrue(buySuccess, "L'acquisto dovrebbe avere successo se ci sono fondi e spazio");
        assertEquals(1, inventory.getItemCount("Wheat"), "Il seme deve aumentare in inventario");
        assertEquals(15.0, wallet.getBalance(), "I soldi devono diminuire in base al prezzo");
    }

    @Test
    void testFailedPurchaseNotEnoughFunds() {
        // svuotiamo il portafoglio
        wallet.deductFunds(20.0);
        assertEquals(0.0, wallet.getBalance());
        boolean buySuccess = market.buySeed("Wheat");
        assertFalse(buySuccess, "L'acquisto dovrebbe fallire se non ci sono fondi sufficienti");
        assertEquals(0, inventory.getItemCount("Wheat"), "L'inventario deve restare invariato");
        assertEquals(0.0, wallet.getBalance(), "Il saldo deve restare invariato");
    }

    @Test
    void testFailedPurchaseInventoryFull() {
        // riempiamo l'inventario. Uno slot di SEED tiene 5 unità (in base a ItemType.SEED).
        // aggiungiamo 5 semi per riempire lo slot.
        for (int i = 0; i < 5; i++) {
            assertTrue(inventory.addItem("Wheat", ItemType.SEED));
        }
        assertEquals(5, inventory.getItemCount("Wheat"));
        assertEquals(20.0, wallet.getBalance());
        // ora l'inventario è pieno, l'acquisto dovrebbe fallire
        boolean buySuccess = market.buySeed("Wheat");
        assertFalse(buySuccess, "L'acquisto dovrebbe fallire se l'inventario è pieno");
        assertEquals(5, inventory.getItemCount("Wheat"), "L'inventario non deve superare il limite");
        assertEquals(20.0, wallet.getBalance(), "I crediti non devono essere scalati se l'inventario è pieno");
    }
}
