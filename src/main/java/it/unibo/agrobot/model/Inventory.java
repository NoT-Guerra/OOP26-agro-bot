package it.unibo.agrobot.model;

import java.util.ArrayList;
import java.util.List;

/**
 * gestisce l inventario completo del drone. l inventario e composto da piu
 * slot, ognuno dei quali puo contenere un certo numero di oggetti dello stesso
 * tipo.
 */
public class Inventory {

    private final List<InventorySlot> slots;
    private int selectedSlotIndex = 0;

    /**
     * crea un inventario con il numero di slot specificato.
     *
     * @param numSlots il numero di slot iniziali
     */
    public Inventory(int numSlots) {
        this.slots = new ArrayList<>();
        for (int i = 0; i < numSlots; i++) {
            this.slots.add(new InventorySlot());
        }
    }

    /**
     * Imposta l'indice dello slot selezionato.
     *
     * @param index il nuovo indice
     */
    public synchronized void setSelectedSlotIndex(int index) {
        if (index >= 0 && index < slots.size()) {
            this.selectedSlotIndex = index;
        }
    }

    /**
     * Restituisce l'indice dello slot selezionato.
     *
     * @return l'indice dello slot selezionato
     */
    public synchronized int getSelectedSlotIndex() {
        return this.selectedSlotIndex;
    }

    /**
     * aggiunge un oggetto nell inventario. prima cerca uno slot che contiene
     * gia lo stesso oggetto e ha spazio, poi cerca uno slot vuoto. se non trova
     * nulla restituisce false.
     *
     * @param itemName il nome dell oggetto da inserire
     * @param type il tipo dell oggetto (CROP o SEED)
     * @return true se l oggetto e stato inserito, false se l inventario e pieno
     */
    public synchronized boolean addItem(String itemName, ItemType type) {
        //prima cerchiamo uno slot che ha gia lo stesso oggetto e non e pieno
        for (InventorySlot slot : this.slots) {
            if (!slot.isEmpty() && slot.getItemName().equals(itemName) && slot.getType() == type && !slot.isFull()) {
                return slot.addItem(itemName, type);
            }
        }
        //se non lo troviamo cerchiamo uno slot vuoto
        for (InventorySlot slot : this.slots) {
            if (slot.isEmpty()) {
                return slot.addItem(itemName, type);
            }
        }
        //inventario pieno
        return false;
    }

    /**
     * rimuove una unita dell oggetto specificato dall inventario.
     *
     * @param itemName il nome dell oggetto da rimuovere
     * @return true se l oggetto e stato trovato e rimosso, false altrimenti
     */
    public synchronized boolean removeItem(String itemName) {
        for (InventorySlot slot : this.slots) {
            if (!slot.isEmpty() && slot.getItemName().equals(itemName)) {
                return slot.removeItem();
            }
        }
        return false;
    }

    /**
     * rimuove una unita dell oggetto specificato dall inventario verificandone anche il tipo.
     *
     * @param itemName il nome dell oggetto da rimuovere
     * @param type il tipo dell oggetto
     * @return true se l oggetto e stato trovato e rimosso, false altrimenti
     */
    public synchronized boolean removeItem(String itemName, ItemType type) {
        for (InventorySlot slot : this.slots) {
            if (!slot.isEmpty() && slot.getItemName().equals(itemName) && slot.getType() == type) {
                return slot.removeItem();
            }
        }
        return false;
    }

    /**
     * conta quanti oggetti con quel nome ci sono in tutto l inventario sommando
     * le quantita di tutti gli slot.
     *
     * @param itemName il nome dell oggetto da contare
     * @return il numero totale di oggetti con quel nome
     */
    public synchronized int getItemCount(String itemName) {
        int count = 0;
        for (InventorySlot slot : this.slots) {
            if (!slot.isEmpty() && slot.getItemName().equals(itemName)) {
                count += slot.getQuantity();
            }
        }
        return count;
    }

    /**
     * aggiunge un nuovo slot vuoto all inventario. utile per futuri upgrade
     * acquistabili al mercato.
     */
    public synchronized void addSlot() {
        this.slots.add(new InventorySlot());
    }

    /**
     * Restituisce il numero totale di slot.
     *
     * @return il numero totale di slot nell inventario
     */
    public synchronized int getSlotCount() {
        return this.slots.size();
    }

    /**
     * Restituisce lo slot alla posizione specificata.
     *
     * @param index l indice dello slot da ottenere
     * @return lo slot alla posizione specificata
     */
    public synchronized InventorySlot getSlot(int index) {
        return this.slots.get(index);
    }

    /**
     * cerca il tipo di un oggetto presente nell inventario dato il suo nome
     *
     * @param itemName il nome dell oggetto da cercare
     * @return il tipo dell oggetto se trovato, null altrimenti
     */
    public synchronized ItemType findItemType(String itemName) {
        for (InventorySlot slot : this.slots) {
            if (!slot.isEmpty() && slot.getItemName().equals(itemName)) {
                return slot.getType();
            }
        }
        return null;
    }

    /**
     * svuota completamente l'inventario.
     */
    public synchronized void clear() {
        for (InventorySlot slot : this.slots) {
            while (!slot.isEmpty()) {
                slot.removeItem();
            }
        }
    }
}
