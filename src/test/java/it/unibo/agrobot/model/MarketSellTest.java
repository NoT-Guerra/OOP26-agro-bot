package it.unibo.agrobot.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class MarketSellTest {

    private Inventory inventory;
    private Wallet wallet;
    private PriceManager priceManager;
    private Market market;

    @BeforeEach
    public void setUp() {
        inventory = new Inventory(5);
        wallet = new Wallet(0.0);
        priceManager = new PriceManager();
        priceManager.setSellPrice("Wheat", ItemType.CROP, 5.0);
        market = new Market(inventory, wallet, priceManager);
    }

    @Test
    void testSuccessfulSale() {
        inventory.addItem("Wheat", ItemType.CROP);
        assertEquals(1, inventory.getItemCount("Wheat"));
        assertEquals(0.0, wallet.getBalance());
        boolean saleSuccess = market.sellCrop("Wheat");
        assertTrue(saleSuccess, "La vendita dovrebbe avere successo se l'oggetto è nell'inventario");
        assertEquals(0, inventory.getItemCount("Wheat"), "Il raccolto deve diminuire in inventario");
        assertEquals(5.0, wallet.getBalance(), "I soldi devono aumentare nel wallet in base al prezzo");
    }

    @Test
    void testFailedSale() {
        assertEquals(0, inventory.getItemCount("Corn"));
        assertEquals(0.0, wallet.getBalance());
        boolean saleSuccess = market.sellCrop("Corn");
        assertFalse(saleSuccess, "La vendita dovrebbe fallire se l'oggetto non è in inventario");
        assertEquals(0, inventory.getItemCount("Corn"), "L'inventario deve restare invariato");
        assertEquals(0.0, wallet.getBalance(), "Il saldo deve restare invariato dopo una vendita fallita");
    }
}
