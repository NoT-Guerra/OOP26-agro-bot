package it.unibo.agrobot.model;

/**
 * rappresenta l'interfaccia per una singola casella della griglia di gioco.
 */
public interface Tile {

    /**
     * @return la posizione della casella nella griglia
     */
    Position getPosition();

    /**
     * @return il tipo di casella, ovvero SOIL, WELL o HANGAR
     */
    TileType getType();

    /**
     * @return lo stato attuale del terreno
     */
    SoilState getSoilState();

    /**
     * modifica lo stato del terreno della zolla
     * 
     * @param state il nuovo stato del terreno
     */
    void setSoilState(SoilState state);

    /**
     * esegue l'aratura sulla zolla se è di tipo SOIL
     * 
     * @return true se la zolla è stata arata con successo, false altrimenti
     */
    boolean plow();

    /**
     * esegue l'irrigazione sulla zolla se è di tipo SOIL ed è stata arata
     * 
     * @return true se l'irrigazione ha avuto successo, false altrimenti
     */
    boolean irrigate();
}
