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
     * restituisce il listino prezzi.
     * 
     * @return il listino prezzi
     */
    public PriceManager getPriceManager() {
        return this.priceManager;
    }

    /**
     * vende un'unità di raccolto dall'inventario e aggiunge i fondi corrispondenti al portafoglio.
     *
     * @param cropName il nome del raccolto da vendere
     * @return true se la vendita ha successo, false se il raccolto non è in inventario o non è di tipo CROP
     */
    public boolean sellCrop(String cropName) {
        // tenta di rimuovere direttamente l'oggetto specificando che deve essere un CROP
        if (this.inventory.removeItem(cropName, ItemType.CROP)) {
            double sellPrice = this.priceManager.getSellPrice(cropName, ItemType.CROP);
            this.wallet.addFunds(sellPrice);
            return true;
        }
        return false;
    }

    /**
     * acquista un seme e lo aggiunge all'inventario, deducendo il costo dal portafoglio.
     *
     * @param seedName il nome del seme da acquistare
     * @return true se l'acquisto ha successo, false se i fondi sono insufficienti o l'inventario è pieno
     */
    public boolean buySeed(String seedName) {
        double buyPrice = this.priceManager.getBuyPrice(seedName, ItemType.SEED);

        // verifica se il giocatore ha abbastanza fondi
        if (!this.wallet.hasEnoughFunds(buyPrice)) {
            return false;
        }

        // tenta di aggiungere il seme all'inventario
        if (this.inventory.addItem(seedName, ItemType.SEED)) {
            // se c'è spazio e l'aggiunta ha successo, deduce i fondi
            this.wallet.deductFunds(buyPrice);
            return true;
        }

        return false;
    }

    private double batteryUpgradeCost = 25.0;
    private double waterTankUpgradeCost = 25.0;

    /**
     * Resetta i costi dei potenziamenti ai valori predefiniti.
     */
    public void reset() {
        this.batteryUpgradeCost = 25.0;
        this.waterTankUpgradeCost = 25.0;
    }

    /**
     * restituisce il costo del prossimo potenziamento batteria.
     *
     * @return il costo del potenziamento
     */
    public double getBatteryUpgradeCost() {
        return this.batteryUpgradeCost;
    }

    /**
     * acquista un potenziamento batteria e ne aumenta il costo per la prossima volta.
     *
     * @param drone il drone a cui applicare il potenziamento
     * @return true se l'acquisto ha successo, false se i fondi sono insufficienti
     */
    public boolean buyBatteryUpgrade(Drone drone) {
        if (this.wallet.hasEnoughFunds(this.batteryUpgradeCost)) {
            this.wallet.deductFunds(this.batteryUpgradeCost);
            drone.upgradeBatteryMaxCapacity(25.0);
            this.batteryUpgradeCost += 25.0;
            return true;
        }
        return false;
    }

    /**
     * restituisce il costo del prossimo potenziamento serbatoio acqua.
     *
     * @return il costo del potenziamento
     */
    public double getWaterTankUpgradeCost() {
        return this.waterTankUpgradeCost;
    }

    /**
     * acquista un potenziamento serbatoio acqua e ne aumenta il costo per la prossima volta.
     *
     * @param drone il drone a cui applicare il potenziamento
     * @return true se l'acquisto ha successo, false se i fondi sono insufficienti
     */
    public boolean buyWaterTankUpgrade(Drone drone) {
        if (this.wallet.hasEnoughFunds(this.waterTankUpgradeCost)) {
            this.wallet.deductFunds(this.waterTankUpgradeCost);
            drone.upgradeWaterTankMaxCapacity(25.0);
            this.waterTankUpgradeCost += 25.0;
            return true;
        }
        return false;
    }

    /**
     * acquista un consumabile e lo aggiunge all'inventario, rimuovendo il costo dal portafoglio
     *
     * @param consumableName il nome del consumabile da acquistare
     * @return true se l'acquisto ha successo, false se i fondi sono insufficienti o l'inventario è pieno
     */
    public boolean buyConsumable(String consumableName) {
        double buyPrice = this.priceManager.getBuyPrice(consumableName, ItemType.CONSUMABLE);

        //verifica se il giocatore ha abbastanza fondi
        if (!this.wallet.hasEnoughFunds(buyPrice)) {
            return false;
        }

        //tenta di aggiungere il consumabile all'inventario
        if (this.inventory.addItem(consumableName, ItemType.CONSUMABLE)) {
            // se c'è spazio e l'aggiunta ha successo, deduce i fondi
            this.wallet.deductFunds(buyPrice);
            return true;
        }

        return false;
    }
}
