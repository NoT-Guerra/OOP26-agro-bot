package it.unibo.agrobot.model;

/**
 * rappresenta un singolo slot dell inventario del drone ogni slot puo contenere N oggetti dello stesso tipo e dello stesso nom
 * 
 */
public class InventorySlot {

    private String itemName;
    private ItemType type;
    private int quantity;

    /**
     * Costruisce uno slot inventario vuoto.
     */
    public InventorySlot() {
        this.itemName = null;
        this.type = null;
        this.quantity = 0;
    }

    /**
     * prova ad aggiungere un oggetto nello slot
     * se lo slot e vuoto lo inizializza, se contiene gia lo stesso oggetto incrementa la quantita
     * 
     * @param name il nome dell oggetto da inserire
     * @param type il tipo dell oggetto (CROP o SEED)
     * @return true se l oggetto e stato aggiunto, false se lo slot e pieno o contiene un oggetto diverso
     */
    public boolean addItem(String name, ItemType type) {
        if (this.isEmpty()) {
            //slot vuoto, lo inizializziamo col nuovo oggetto
            this.itemName = name;
            this.type = type;
            this.quantity = 1;
            return true;
        }
        //slot gia occupato, controlliamo che sia lo stesso oggetto
        if (this.itemName.equals(name) && this.type == type && !this.isFull()) {
            this.quantity++;
            return true;
        }
        return false;
    }

    /**
     * rimuove una unita dallo slot, se la quantita arriva a zero lo slot viene resettato e torna disponibile
     * 
     * @return true se c era almeno un oggetto da rimuovere, false se lo slot era gia vuoto
     */
    public boolean removeItem() {
        if (this.quantity > 0) {
            this.quantity--;
            if (this.quantity == 0) {
                //slot svuotato, lo resettiamo
                this.itemName = null;
                this.type = null;
            }
            return true;
        }
        return false;
    }

    /**
     * Verifica se lo slot è vuoto.
     *
     * @return true se lo slot non contiene nessun oggetto
     */
    public boolean isEmpty() {
        return this.quantity == 0;
    }

    /**
     * Verifica se lo slot è pieno.
     *
     * @return true se lo slot ha raggiunto il limite massimo per il suo tipo di oggetto
     */
    public boolean isFull() {
        if (this.type == null) {
            return false;
        }
        return this.quantity >= this.type.getMaxPerSlot();
    }

    /**
     * Restituisce il nome dell'oggetto.
     *
     * @return il nome dell oggetto contenuto nello slot, null se vuoto
     */
    public String getItemName() {
        return this.itemName;
    }

    /**
     * Restituisce il tipo dell'oggetto.
     *
     * @return il tipo dell oggetto contenuto nello slot, null se vuoto
     */
    public ItemType getType() {
        return this.type;
    }

    /**
     * Restituisce la quantità di oggetti.
     *
     * @return la quantita di oggetti presenti nello slot
     */
    public int getQuantity() {
        return this.quantity;
    }
}
