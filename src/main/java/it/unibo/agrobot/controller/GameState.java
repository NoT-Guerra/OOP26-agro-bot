package it.unibo.agrobot.controller;

// represents the current state of the game
public enum GameState {
    /**
     * The game is in the main menu
     */
    MENU,
    
    /**
     * The game is currently playing
     */
    PLAYING,
    
    /**
     * The game is paused
     */
    PAUSED,

    /**
     * The game is over
     */
    GAME_OVER
}
