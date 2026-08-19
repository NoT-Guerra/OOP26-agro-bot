package it.unibo.agrobot.model;

/**
 * Gestisce la capacità e il livello del acqua trasportato dal drone.
 */
public class WaterTank {

    private double level;
    private double maxCapacity;
    private final double initialMaxCapacity;

    public WaterTank(double maxCapacity) {
        this.initialMaxCapacity = maxCapacity;
        this.maxCapacity = maxCapacity;
        this.level = 0.0; //parte sempre vuoto
    }

    public void reset() {
        this.maxCapacity = this.initialMaxCapacity;
        this.level = 0.0;
    }

    public void increaseMaxCapacity(double amount) {
        this.maxCapacity += amount;
    }

    public double getMaxCapacity() {
        return this.maxCapacity;
    }

    public double getLevel() {
        return this.level;
    }

    /**
     * aggiunge acqua al serbatoio senza superarne la capienza massima.
     *
     * @param amount quantità d'acqua da aggiungere
     */
    public void add(double amount) {
        this.level += amount;
        if (this.level > this.maxCapacity) {
            this.level = this.maxCapacity;
        }
    }

    /**
     * riempie il serbatoio fino alla sua capacità massima
     */
    public void fill() {
        this.level = this.maxCapacity;
    }

    /**
     * rimuove acqua dal serbatoio fermandosi a zero se necessario
     *
     * @param amount quantità d'acqua da rimuovere
     */
    public void remove(double amount) {
        this.level -= amount;
        if (this.level < 0) {
            this.level = 0.0;
        }
    }

    /**
     * svuota completamente il serbatoio
     */
    public void empty() {
        this.level = 0.0;
    }
}
