package it.unibo.agrobot.model;

import java.util.EnumMap;
import java.util.Map;

// listino prezzi globale di compravendita per ogni ItemType.
public class PriceManager {

    private final Map<ItemType, Double> buyPrices;
    private final Map<ItemType, Double> sellPrices;

    public PriceManager() {
        this.buyPrices = new EnumMap<>(ItemType.class);
        this.sellPrices = new EnumMap<>(ItemType.class);

        for (ItemType type : ItemType.values()) {
            this.buyPrices.put(type, 0.0);
            this.sellPrices.put(type, 0.0);
        }
    }

    /**
     * imposta il prezzo di acquisto per un determinato ItemType.
     *
     * @param type il tipo di oggetto
     * @param price il nuovo prezzo di acquisto
     */
    public void setBuyPrice(ItemType type, double price) {
        if (price < 0) {
            throw new IllegalArgumentException("Il prezzo non può essere negativo.");
        }
        this.buyPrices.put(type, price);
    }

    /**
     * imposta il prezzo di vendita per un determinato ItemType.
     *
     * @param type il tipo di oggetto
     * @param price il nuovo prezzo di vendita
     */
    public void setSellPrice(ItemType type, double price) {
        if (price < 0) {
            throw new IllegalArgumentException("Il prezzo non può essere negativo.");
        }
        this.sellPrices.put(type, price);
    }

    /**
     * restituisce il prezzo di acquisto per un determinato ItemType.
     *
     * @param type il tipo di oggetto
     * @return il prezzo di acquisto
     */
    public double getBuyPrice(ItemType type) {
        return this.buyPrices.getOrDefault(type, 0.0);
    }

    /**
     * restituisce il prezzo di vendita per un determinato ItemType.
     *
     * @param type il tipo di oggetto
     * @return il prezzo di vendita
     */
    public double getSellPrice(ItemType type) {
        return this.sellPrices.getOrDefault(type, 0.0);
    }
}
