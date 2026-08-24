package it.unibo.agrobot.model;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;

// listino prezzi globale di compravendita per ogni ItemType.
public class PriceManager {

    private final Map<String, Map<ItemType, Double>> buyPrices;
    private final Map<String, Map<ItemType, Double>> sellPrices;

    /**
     * Costruisce un nuovo listino prezzi vuoto.
     */
    public PriceManager() {
        this.buyPrices = new HashMap<>();
        this.sellPrices = new HashMap<>();
    }

    /**
     * imposta il prezzo di acquisto per un determinato item name e ItemType.
     *
     * @param itemName il nome dell'oggetto
     * @param type il tipo di oggetto
     * @param price il nuovo prezzo di acquisto
     */
    public void setBuyPrice(String itemName, ItemType type, double price) {
        if (price < 0) {
            throw new IllegalArgumentException("Il prezzo non può essere negativo.");
        }
        buyPrices.computeIfAbsent(itemName, k -> new EnumMap<>(ItemType.class)).put(type, price);
    }

    /**
     * imposta il prezzo di vendita per un determinato item name e ItemType.
     *
     * @param itemName il nome dell'oggetto
     * @param type il tipo di oggetto
     * @param price il nuovo prezzo di vendita
     */
    public void setSellPrice(String itemName, ItemType type, double price) {
        if (price < 0) {
            throw new IllegalArgumentException("Il prezzo non può essere negativo.");
        }
        sellPrices.computeIfAbsent(itemName, k -> new EnumMap<>(ItemType.class)).put(type, price);
    }

    /**
     * restituisce il prezzo di acquisto per un determinato item name e
     * ItemType.
     * 
     *
     * @param itemName il nome dell'oggetto
     * @param type il tipo di oggetto
     * @return il prezzo di acquisto
     */
    public double getBuyPrice(String itemName, ItemType type) {
        return buyPrices.getOrDefault(itemName, java.util.Collections.emptyMap()).getOrDefault(type, 0.0);
    }

    /**
     * restituisce il prezzo di vendita per un determinato item name e ItemType.
     *
     * @param itemName il nome dell'oggetto
     * @param type il tipo di oggetto
     * @return il prezzo di vendita
     */
    public double getSellPrice(String itemName, ItemType type) {
        return sellPrices.getOrDefault(itemName, java.util.Collections.emptyMap()).getOrDefault(type, 0.0);
    }

    /**
     * restituisce un set di nomi di oggetti acquistabili per un dato tipo.
     * 
     * @param type il tipo di oggetto
     * @return un set contenente i nomi degli oggetti acquistabili
     */
    public java.util.Set<String> getBuyableItems(ItemType type) {
        return buyPrices.entrySet().stream()
                .filter(e -> e.getValue().containsKey(type))
                .map(Map.Entry::getKey)
                .collect(java.util.stream.Collectors.toSet());
    }

    /**
     * restituisce un set di nomi di oggetti vendibili per un dato tipo.
     * 
     * @param type il tipo di oggetto
     * @return un set contenente i nomi degli oggetti vendibili
     */
    public java.util.Set<String> getSellableItems(ItemType type) {
        return sellPrices.entrySet().stream()
                .filter(e -> e.getValue().containsKey(type))
                .map(Map.Entry::getKey)
                .collect(java.util.stream.Collectors.toSet());
    }
}
