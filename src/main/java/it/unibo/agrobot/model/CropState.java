package it.unibo.agrobot.model;

/**
 * rappresenta i possibili stati di vita di una coltura
 */
public enum CropState {
    /** la pianta è un seme appena piantato */
    SEED,
    /** la pianta sta crescendo */
    GROWING,
    /** la pianta è completamente matura e pronta per il raccolto */
    MATURE,
    /** la pianta è morta */
    DEAD
}
