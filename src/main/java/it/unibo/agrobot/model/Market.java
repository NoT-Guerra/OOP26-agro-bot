package it.unibo.agrobot.model;

// gestisce la logica del mercato: vendita e acquisto di oggetti.

public class Market {

    private final Inventory inventory;
    private final Wallet wallet;
    private final PriceManager priceManager;

    /**
     * inizializza il mercato con le dipendenze necessarie.
     *
     * @param inventory    l'inventario del drone
     * @param wallet       il portafoglio del giocatore
     * @param priceManager il listino prezzi
     */
    public Market(Inventory inventory, Wallet wallet, PriceManager priceManager) {
        this.inventory = inventory;
        this.wallet = wallet;
        this.priceManager = priceManager;
    }

    /**
     * vende un'unità di raccolto dall'inventario e aggiunge i fondi corrispondenti al portafoglio.
     *
     * @param cropName il nome del raccolto da vendere
     * @return true se la vendita ha successo, false se il raccolto non è in inventario o non è di tipo CROP
     */
    public boolean sellCrop(String cropName) {
        ItemType type = this.inventory.findItemType(cropName);

        // verifica se l'oggetto è presente e se è un raccolto
        if (type != ItemType.CROP) {
            return false;
        }

        // ottiene il prezzo di vendita
        double sellPrice = this.priceManager.getSellPrice(cropName, type);

        // se riesco a rimuoverlo dall'inventario, aggiungo i soldi
        if (this.inventory.removeItem(cropName)) {
            this.wallet.addFunds(sellPrice);
            return true;
        }

        return false;
    }
}
