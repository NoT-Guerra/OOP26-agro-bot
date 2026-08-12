package it.unibo.agrobot.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PriceManagerTest {

    private PriceManager priceManager;

    @BeforeEach
    public void setUp() {
        priceManager = new PriceManager();
    }

    @Test
    void testInitialPrices() {
        assertEquals(0.0, priceManager.getBuyPrice("Wheat", ItemType.CROP), "Initial buy price should be 0.0");
        assertEquals(0.0, priceManager.getSellPrice("Wheat", ItemType.CROP), "Initial sell price should be 0.0");
        assertEquals(0.0, priceManager.getBuyPrice("Corn", ItemType.SEED), "Initial buy price should be 0.0");
    }

    @Test
    void testSetAndGetPrices() {
        priceManager.setBuyPrice("Wheat", ItemType.CROP, 10.0);
        priceManager.setSellPrice("Wheat", ItemType.CROP, 8.0);

        priceManager.setBuyPrice("Corn", ItemType.SEED, 5.0);
        priceManager.setSellPrice("Corn", ItemType.SEED, 4.0);

        assertEquals(10.0, priceManager.getBuyPrice("Wheat", ItemType.CROP), "Buy price for Wheat CROP should be 10.0");
        assertEquals(8.0, priceManager.getSellPrice("Wheat", ItemType.CROP), "Sell price for Wheat CROP should be 8.0");

        assertEquals(5.0, priceManager.getBuyPrice("Corn", ItemType.SEED), "Buy price for Corn SEED should be 5.0");
        assertEquals(4.0, priceManager.getSellPrice("Corn", ItemType.SEED), "Sell price for Corn SEED should be 4.0");

        // Controlla che altri item non siano influenzati
        assertEquals(0.0, priceManager.getBuyPrice("Wheat", ItemType.SEED), "Buy price for Wheat SEED should be 0.0");
    }

    @Test
    void testNoEconomicExploits() {
        // impostiamo dei prezzi validi
        priceManager.setBuyPrice("Wheat", ItemType.CROP, 10.0);
        priceManager.setSellPrice("Wheat", ItemType.CROP, 8.0);

        priceManager.setBuyPrice("Wheat", ItemType.SEED, 4.0);
        priceManager.setSellPrice("Wheat", ItemType.SEED, 2.0);

        priceManager.setBuyPrice("Corn", ItemType.CROP, 12.0);
        priceManager.setSellPrice("Corn", ItemType.CROP, 9.0);

        priceManager.setBuyPrice("Corn", ItemType.SEED, 5.0);
        priceManager.setSellPrice("Corn", ItemType.SEED, 4.0);

        // nomi dei raccolti da testare
        String[] crops = {"Wheat", "Corn"};

        // verifica che per ogni elemento, il prezzo di vendita sia <= al prezzo di acquisto
        for (String cropName : crops) {
            for (ItemType type : ItemType.values()) {
                double buyPrice = priceManager.getBuyPrice(cropName, type);
                double sellPrice = priceManager.getSellPrice(cropName, type);
                assertTrue(sellPrice <= buyPrice,
                        "Rilevato exploit economico per " + cropName + " " + type + ": prezzo di vendita (" + sellPrice + ") maggiore del prezzo di acquisto (" + buyPrice + ")");
            }
        }
    }

    @Test
    void testNegativePricesThrowException() {
        IllegalArgumentException e1 = assertThrows(IllegalArgumentException.class, () -> priceManager.setBuyPrice("Wheat", ItemType.CROP, -1.0), "Impostare un prezzo di acquisto negativo deve lanciare eccezione");
        assertEquals("Il prezzo non può essere negativo.", e1.getMessage());
        
        IllegalArgumentException e2 = assertThrows(IllegalArgumentException.class, () -> priceManager.setSellPrice("Corn", ItemType.SEED, -1.0), "Impostare un prezzo di vendita negativo deve lanciare eccezione");
        assertEquals("Il prezzo non può essere negativo.", e2.getMessage());
    }
}
