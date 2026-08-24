package it.unibo.agrobot.controller;

// rappresenta lo stato attuale del gioco
public enum GameState {
    /**
     * Il gioco è nel menu principale
     */
    MENU,
    
    /**
     * Il gioco è attualmente in corso
     */
    PLAYING,
    
    /**
     * Il gioco è in pausa
     */
    PAUSED,

    /**
     * Il gioco è terminato
     */
    GAME_OVER,

    /**
     * Il gioco è nel menu del magazzino
     */
    STORAGE_MENU
}
