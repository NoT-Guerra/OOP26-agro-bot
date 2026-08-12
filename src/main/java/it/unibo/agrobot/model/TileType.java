package it.unibo.agrobot.model;

/**
 * rappresenta la tipologia di casella presente nella griglia di gioco
 */
public enum TileType {
    /** terreno agricolo coltivabile */
    SOIL,
    /** pozzo d'acqua per la ricarica del serbatoio del drone */
    WELL,
    /** hangar per la ricarica della batteria del drone */
    HANGAR,
    /** tappeto erboso */
    GRASS,
    /** mercato per comprare e vendere */
    MARKET
}
