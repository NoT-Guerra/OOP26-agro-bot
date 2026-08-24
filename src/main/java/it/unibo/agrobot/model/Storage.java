package it.unibo.agrobot.model;

/**
 * rappresenta il deposito fisso situato nella zona dell hangar
 * il drone puo appoggiare e ritirare oggetti dal suo inventario
 * interagendo con questo deposito
 */
public class Storage extends Inventory {

    private static final int DEFAULT_SLOTS = 6;

    /**
     * Costruisce un nuovo deposito vuoto con il numero di slot predefinito.
     */
    public Storage() {
        super(DEFAULT_SLOTS);
    }

    /**
     * trasferisce un oggetto dal deposito all inventario del drone rimuove l oggetto dal deposito e lo aggiunge all inventario
     * 
     * @param itemName il nome dell oggetto da trasferire
     * @param droneInventory l inventario del drone che ricevera l oggetto
     * @return true se il trasferimento e riuscito, false se l oggetto non c era o l inventario era pieno
     */
    public boolean transferToInventory(String itemName, Inventory droneInventory) {
        //prima controlliamo che l oggetto esista nel deposito
        if (this.getItemCount(itemName) <= 0) {
            return false;
        }
        //cerchiamo il tipo dell oggetto per poterlo aggiungere all inventario
        ItemType type = this.findItemType(itemName);
        if (type == null) {
            return false;
        }
        //proviamo ad aggiungerlo all inventario del drone
        if (droneInventory.addItem(itemName, type)) {
            this.removeItem(itemName);
            return true;
        }
        return false;
    }

    /**
     * trasferisce un oggetto dall inventario del drone al deposito
     * rimuove l oggetto dall inventario e lo aggiunge al deposito
     * 
     * @param itemName il nome dell oggetto da trasferire
     * @param droneInventory l inventario del drone da cui prelevare l oggetto
     * @return true se il trasferimento e riuscito, false se l oggetto non c era o il deposito era pieno
     */
    public boolean transferFromInventory(String itemName, Inventory droneInventory) {
        if (droneInventory.getItemCount(itemName) <= 0) {
            return false;
        }
        ItemType type = droneInventory.findItemType(itemName);
        if (type == null) {
            return false;
        }
        if (this.addItem(itemName, type)) {
            droneInventory.removeItem(itemName);
            return true;
        }
        return false;
    }
}
