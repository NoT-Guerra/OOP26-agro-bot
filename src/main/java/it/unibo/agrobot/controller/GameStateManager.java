package it.unibo.agrobot.controller;

import java.util.function.Consumer;

// gestisce lo stato attuale del gioco e notifica i listener dei cambiamenti di stato

public class GameStateManager {
    
    private GameState state = GameState.MENU;
    private Consumer<GameState> onStateChange;
    private String gameOverReason = "";

    /**
     * imposta il motivo del game over
     *
     * @param reason il motivo
     */
    public void setGameOverReason(String reason) {
        this.gameOverReason = reason;
    }

    /**
     * ottiene il motivo del game over
     *
     * @return il motivo
     */
    public String getGameOverReason() {
        return this.gameOverReason;
    }

    /**
     * imposta il listener da notificare quando lo stato del gioco cambia
     *
     * @param onStateChange il listener da impostare
     */
    public void setOnStateChange(Consumer<GameState> onStateChange) {
        this.onStateChange = onStateChange;
    }

    /**
     * aggiorna lo stato attuale del gioco e attiva il listener se presente
     *
     * @param state il nuovo stato del gioco
     */
    public void setState(GameState state) {
        this.state = state;
        if (this.onStateChange != null) {
            this.onStateChange.accept(this.state);
        }
    }

    /**
     * ottiene lo stato attuale del gioco
     *
     * @return lo stato attuale del gioco
     */
    public GameState getState() {
        return this.state;
    }
}
