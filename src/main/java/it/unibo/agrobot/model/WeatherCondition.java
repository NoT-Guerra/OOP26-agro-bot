package it.unibo.agrobot.model;

/**
 * enum che rappresenta le condizioni meteorologiche del gioco
 */
public enum WeatherCondition {
    /**
     * condizione normale: soleggiato
     */
    SUNNY,
    /**
     * condizione di pioggia: annaffia gradualmente il terreno ma scarica più in
     * fretta il drone
     */
    RAINY
}
