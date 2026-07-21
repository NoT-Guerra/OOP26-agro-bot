package it.unibo.agrobot.model;

/**
 * rappresenta la griglia logica di gioco, ovvero le zolle
 */
public interface Grid {

    /**
     * @return larghezza della griglia
     */
    int getWidth();

    /**
     * @return altezza della griglia
     */
    int getHeight();

    /**
     * verifica se una coordinata (x, y) rientra nei limiti della griglia
     * 
     * @param x coordinata X
     * @param y coordinata Y
     * @return true se nei limiti, false altrimenti
     */
    boolean isInBounds(int x, int y);
}
