package it.unibo.agrobot.controller;

import java.util.function.Consumer;

// manages the current state of the game and notifies listeners of state changes

public class GameStateManager {
    
    private GameState state = GameState.MENU;
    private Consumer<GameState> onStateChange;

    /**
     * sets the listener to be notified when the game state changes
     *
     * @param onStateChange the listener to set
     */
    public void setOnStateChange(Consumer<GameState> onStateChange) {
        this.onStateChange = onStateChange;
    }

    /**
     * updates the current game state and triggers the listener if present
     *
     * @param state the new game state
     */
    public void setState(GameState state) {
        this.state = state;
        if (this.onStateChange != null) {
            this.onStateChange.accept(this.state);
        }
    }

    /**
     * gets the current game state
     *
     * @return the current game state
     */
    public GameState getState() {
        return this.state;
    }
}
