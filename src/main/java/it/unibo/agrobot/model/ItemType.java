package it.unibo.agrobot.model;

/**
 * tipi di oggetto che possono essere inseriti nell inventario del drone
 * ogni tipo ha un limite massimo di unita per slot
 */
public enum ItemType {
    CROP(10),  //colture raccolte, massimo 10 per slot
    SEED(5),   //semi da piantare, massimo 5 per slot
    CONSUMABLE(10); //oggetti consumabili (es. diserbante), massimo 10 per slot

    private final int maxPerSlot;

    ItemType(int maxPerSlot) {
        this.maxPerSlot = maxPerSlot;
    }

    /**
     * Restituisce il numero massimo per slot.
     *
     * @return il numero massimo di oggetti di questo tipo che possono stare in un singolo slot
     */
    public int getMaxPerSlot() {
        return this.maxPerSlot;
    }
}
