package it.unibo.agrobot.controller;

import it.unibo.agrobot.model.Drone;
import it.unibo.agrobot.model.Grid;
import it.unibo.agrobot.view.GamePanel;

/**
 * Gestisce il ciclo principale di esecuzione del gioco.
 */
public class GameLoop implements Runnable {

    private static final int UPS_SET = 60;
    private static final int FPS_SET = 60;

    private boolean running;
    private Thread logicThread;
    private Thread renderThread;

    private final GamePanel gamePanel;
    private final Grid grid;
    private final Drone drone;
    private final GameStateManager stateManager;
    private final it.unibo.agrobot.model.WeatherManager weatherManager;

    // lock per sincronizzare i thread evitando che grafica e logica accedano ai dati del gioco contemporaneamente
    private final Object stateLock = new Object();

    /**
     * Costruisce un GameLoop con il pannello di gioco specificato per il rendering.
     *
     * @param gamePanel il pannello da ridisegnare durante la fase di render
     * @param grid la griglia da aggiornare
     * @param drone il drone da aggiornare
     * @param stateManager il gestore degli stati
     * @param weatherManager il gestore del meteo
     */
    public GameLoop(GamePanel gamePanel, it.unibo.agrobot.model.Grid grid, Drone drone, GameStateManager stateManager, it.unibo.agrobot.model.WeatherManager weatherManager) {
        this.gamePanel = gamePanel;
        this.grid = grid;
        this.drone = drone;
        this.stateManager = stateManager;
        this.weatherManager = weatherManager;
    }

    /**
     * Costruisce un GameLoop senza vista (test).
     */
    public GameLoop() {
        this.gamePanel = null;
        this.grid = null;
        this.drone = null;
        this.stateManager = null;
        this.weatherManager = null;
    }

    private java.util.function.Supplier<String> bankruptcyChecker;

    /**
     * Imposta il controllo per il game over
     * @param checker funzione che ritorna il motivo del game over o null se non c'è
     */
    public void setBankruptcyChecker(java.util.function.Supplier<String> checker) {
        this.bankruptcyChecker = checker;
    }

    /**
     * Avvia i thread di logica e rendering del game loop.
     */
    public synchronized void start() {
        if (running) {
            return;
        }
        running = true;
        logicThread = new Thread(this, "Logic Thread");
        renderThread = new Thread(this::renderLoop, "Render Thread");
        logicThread.start();
        renderThread.start();
    }

    /**
     * Ferma il game loop e attende in modo sicuro che sia il thread logico
     * che quello di rendering terminino.
     */
    public void stop() {
        if (!running) {
            return;
        }
        running = false;
        try {
            if (logicThread != null) {
                logicThread.join();
            }
            if (renderThread != null) {
                renderThread.join();
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    /**
     * Aggiorna continuamente lo stato del gioco agli UPS definiti.
     */
    @Override
    @SuppressWarnings("BusyWait")
    public void run() {
        long previousTime = System.nanoTime();
        double timePerUpdate = 1000000000.0 / UPS_SET;
        double delta = 0;

        while (running) {
            long currentTime = System.nanoTime();
            delta += (currentTime - previousTime) / timePerUpdate;
            previousTime = currentTime;

            if (delta >= 1) {
                // sincronizziamo l'aggiornamento dello stato logico facendo in modo che il thread di rendering dovrà aspettare
                synchronized (stateLock) {
                    update();
                }
                delta--;
            }

            // sleep per evitare che il thread consumi il 100% della CPU
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
    }

    /**
     * Attiva continuamente il rendering dei frame agli FPS definiti.
     */
    @SuppressWarnings("BusyWait")
    private void renderLoop() { // lo stesso ragionamento fatto per il thread principale, ma per il thread di rendering
        long previousTime = System.nanoTime();
        double timePerFrame = 1000000000.0 / FPS_SET;
        double delta = 0;

        while (running) {
            long currentTime = System.nanoTime();
            delta += (currentTime - previousTime) / timePerFrame;
            previousTime = currentTime;

            if (delta >= 1) {
                // sincronizziamo la fase di rendering in modo che il thread logico non possa accedere ai dati del gioco
                synchronized (stateLock) {
                    render();
                }
                delta--;
            }

            // sleep per evitare che il thread consumi il 100% della CPU
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
    }

    /**
     * Aggiorna la logica di gioco.
     */
    protected void update() {
        if (this.drone == null || (this.stateManager != null && this.stateManager.getState() != GameState.PLAYING)) return;
        
        if (this.drone.isDead()) {
            if (this.stateManager != null) {
                this.stateManager.setGameOverReason("You've run out of battery!");
                this.stateManager.setState(GameState.GAME_OVER);
            }
            return;
        }

        if (this.bankruptcyChecker != null) {
            String reason = this.bankruptcyChecker.get();
            if (reason != null) {
                if (this.stateManager != null) {
                    this.stateManager.setGameOverReason(reason);
                    this.stateManager.setState(GameState.GAME_OVER);
                }
                return;
            }
        }

        // calcola deltaTime in secondi
        double deltaTime = 1.0 / UPS_SET;
        this.drone.updateState(deltaTime);
        if (this.grid != null) {
            this.grid.update(deltaTime);
        }
        if (this.weatherManager != null) {
            this.weatherManager.update(deltaTime);
        }
    }

    /**
     * Attiva il rendering della vista di gioco.
     */
    protected void render() {
        if (gamePanel != null) {
            gamePanel.repaint();
        }
    }
}
