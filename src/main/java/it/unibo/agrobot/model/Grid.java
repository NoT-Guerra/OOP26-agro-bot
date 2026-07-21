package it.unibo.agrobot.model;

import java.util.Optional;

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
     * restituisce la casella alla coordinata
     * 
     * @param x coordinata X
     * @param y coordinata Y
     * @return un optional contenente la Tile se le coordinate sono valide, Optional.empty() altrimenti
     */
    Optional<Tile> getTile(int x, int y);

    /**
     * restituisce la casella in corrispondenza di una position
     * 
     * @param position la posizione
     * @return un optional con la casella se all'interno della griglia, Optional.empty() altrimenti
     */
    Optional<Tile> getTile(Position position);

    /**
     * verifica se una coordinata (x, y) rientra nei limiti della griglia
     * 
     * @param x coordinata X
     * @param y coordinata Y
     * @return true se nei limiti, false altrimenti
     */
    boolean isInBounds(int x, int y);

    /**
     * Verifica se una Position rientra nei limiti della griglia di gioco
     * 
     * @param position la posizione da verificare
     * @return true se nei limiti, false altrimenti
     */
    boolean isInBounds(Position position);
}
