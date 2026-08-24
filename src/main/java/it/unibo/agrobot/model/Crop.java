package it.unibo.agrobot.model;

/**
 * rappresenta una coltura coltivata
 */
public interface Crop {

    /**
     * Restituisce il nome della coltura.
     *
     * @return il nome della coltura
     */
    String getName();

    /**
     * Restituisce lo stato della coltura.
     *
     * @return lo stato della coltura (SEED, GROWING, MATURE, DEAD)
     */
    CropState getState();

    /**
     * aggiorna i timer interni della pianta (crescita, sete)
     * da chiamare nel game loop per gestire lo scorrere del tempo
     * 
     * @param deltaTime il tempo trascorso dall'ultimo aggiornamento
     */
    void update(double deltaTime);

    /**
     * aggiorna i timer interni della pianta (crescita, sete) specificando un moltiplicatore
     * da chiamare nel game loop per gestire lo scorrere del tempo
     * 
     * @param deltaTime il tempo trascorso dall ultimo aggiornamento
     * @param growthMultiplier il moltiplicatore di velocità della crescita
     */
    void update(double deltaTime, double growthMultiplier);

    /**
     * Verifica se la coltura è pronta per il raccolto.
     *
     * @return true se la pianta è matura e può essere raccolta, false altrimenti.
     */
    boolean isReadyToHarvest();

    /**
     * Verifica se la coltura è morta.
     *
     * @return true se la pianta è morta, false altrimenti.
     */
    boolean isDead();

    /**
     * idrata la pianta 
     */
    void water();
}
