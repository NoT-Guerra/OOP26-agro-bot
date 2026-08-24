package it.unibo.agrobot.model;

/**
 * gestisce l'energia disponibile per le azioni del drone
 * ongi azione comporta un decremetno della batteria
 */
public class Battery {

    private double level;
    private double maxCapacity;
    private final double initialMaxCapacity;

    /**
     * Crea una batteria con la capacità massima specificata.
     *
     * @param maxCapacity la capacità massima
     */
    public Battery(double maxCapacity) {
        this.initialMaxCapacity = maxCapacity;
        this.maxCapacity = maxCapacity;
        this.level = maxCapacity;
    }

    /**
     * Resetta la batteria ai valori iniziali.
     */
    public void reset() {
        this.maxCapacity = this.initialMaxCapacity;
        this.level = this.maxCapacity;
    }

    /**
     * Restituisce il livello attuale della batteria.
     *
     * @return il livello attuale
     */
    public double getLevel() {
        return this.level;
    }

    /**
     * riduce il livello della batteria dell'ammontare specificato.
     * il livello non può mai scendere sotto lo zero
     * 
     * @param amount quantità di energia da consumare
     */
    public void decrease(double amount) {
        this.level -= amount;
        if (this.level < 0) {
            this.level = 0;
        }
    }

    /**
     * ripristina l'energia al valore massimo consentito.
     */
    public void recharge() {
        this.level = this.maxCapacity;
    }

    /**
     * Verifica se il drone ha esaurito l'energia
     * 
     * @return true se l'energia è pari a 0, false altrimenti
     */
    public boolean isDead() {
        return this.level <= 0;
    }

    /**
     * Aumenta la capacità massima della batteria.
     *
     * @param amount l'ammontare di incremento
     */
    public void increaseMaxCapacity(double amount) {
        this.maxCapacity += amount;
        this.level += amount;
    }

    /**
     * Restituisce la capacità massima attuale.
     *
     * @return la capacità massima
     */
    public double getMaxCapacity() {
        return this.maxCapacity;
    }
}
