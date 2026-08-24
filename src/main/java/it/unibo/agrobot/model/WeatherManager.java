package it.unibo.agrobot.model;

/**
 * gestisce i cambiamenti meteorologici nel gioco
 */
public interface WeatherManager {

    /**
     * Restituisce la condizione corrente.
     *
     * @return la condizione meteorologica attuale
     */
    WeatherCondition getCurrentCondition();

    /**
     * aggiorna lo stato del meteo nel tempo
     *
     * @param deltaTime il tempo trascorso dall'ultimo frame
     */
    void update(double deltaTime);

    /**
     * resetta il meteo allo stato iniziale
     */
    void reset();
}
