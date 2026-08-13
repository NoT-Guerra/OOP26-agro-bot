package it.unibo.agrobot.model;

/**
 * rappresenta l'entità principale controllata dal giocatore, ovvero il drone
 * che si muove nel campo. definisce le funzionalità per interrogarne lo stato e
 * per muoverlo nello spazio.
 */
public interface Drone {

    /**
     * @return l'oggetto position che contiene le coordinate attuali del drone
     */
    Position getPosition();

    /**
     * @return il livello attuale di carica della batteria
     */
    double getBatteryLevel();

    /**
     * @return il livello attuale di acqua nel serbatoio del drone
     */
    double getWaterLevel();

    /**
     * prova ad avviare il movimento del drone in una specifica direzione il
     * drone si sposterà sempre esattamente di una casella (1.0) alla volta se
     * il drone si sta già muovendo, il comando viene ignorato.
     *
     * @param dir la direzione in cui spostarsi
     * @return true se il comando è stato accettato, false se era già in
     * movimento o senza batteria
     */
    boolean move(Direction dir);

    /**
     * @return true se il drone è attualmente in viaggio tra due caselle, false
     * se è fermo
     */
    boolean isMoving();

    /**
     * aggiorna lo stato fisico del drone nel tempo. verra chiamato
     * ripetutamente dal Game Loop
     *
     * @param deltaTime il tempo trascorso dall'ultimo frame(in secondi o
     * sottomultipli)
     */
    void updateState(double deltaTime);

    /**
     * ricarica completamente il serbatoio dell'acqua del drone chiamato quando
     * il drone si trova sul pozzo (well)
     */
    void rechargeWaterAtWell();

    /**
     * esegue azione di aratura del terreno questa azione consuma più energia
     * rispetto a un normale movimento
     */
    void plow();

    /**
     * raccoglie la risorsa presente nella posizione corrente esegurie questa
     * azione consuma energia(piu del movimento)
     */
    void harvest();

    /**
     * butta acqua sul terreno per irrigare consuma sia acqua dal
     * serbatoio(consuma 10 su max 50) che energia dalla batteria
     *
     * @return true se c'era abbastanza acqua ed energia per irrigare, false
     * altrimenti
     */
    boolean irrigate();

    /**
     * Verifica se il drone ha ancora batteria
     *
     * @return true se l'energia è 0, false altrimenti
     */
    boolean isDead();

    /**
     * Ripristina l'energia del drone ricaricando la batteria viene chiamata
     * quando il drone si trova sull hanggar
     */
    void rechargeAtHangar();

    /**
     * @return l'inventario del drone con i suoi slot
     */
    Inventory getInventory();

    /**
     * @return il portafoglio associato al drone
     */
    Wallet getWallet();

    /**
     * resetta lo stato del drone ai valori iniziali
     */
    void reset();
}
