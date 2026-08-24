package it.unibo.agrobot.model;

/**
 * Gestisce le coordinate spaziali all'interno della simulazione.
 */
public class Position {

    private double x;
    private double y;

    /**
     * Costruisce una nuova posizione.
     *
     * @param x la coordinata x
     * @param y la coordinata y
     */
    public Position(double x, double y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Restituisce la coordinata x.
     *
     * @return la coordinata x
     */
    public double getX() {
        return this.x;
    }

    /**
     * Restituisce la coordinata y.
     *
     * @return la coordinata y
     */
    public double getY() {
        return this.y;
    }

    /**
     * Imposta la coordinata x.
     *
     * @param x la nuova coordinata x
     */
    public void setX(double x) {
        this.x = x;
    }

    /**
     * Imposta la coordinata y.
     *
     * @param y la nuova coordinata y
     */
    public void setY(double y) {
        this.y = y;
    }
}
