package it.unibo.agrobot.model;

/**
 * rappresenta lo stato di lavorazione del terreno per una zolla agricola
 */
public enum SoilState {
    /** terreno non arato */
    UNPLOWED,
    /** terreno arato e pronto per la semina */
    PLOWED,
    /** terreno arato e irrigato */
    WATERED
}
