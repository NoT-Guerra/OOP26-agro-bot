package it.unibo.agrobot.controller;

import it.unibo.agrobot.model.Drone;
import it.unibo.agrobot.view.GamePanel;

public class GameLoop implements Runnable {

    private static final int UPS_SET = 60;
    private static final int FPS_SET = 60;

    private boolean running;
    private Thread logicThread;
    private Thread renderThread;

    private final GamePanel gamePanel;
    private final Drone drone;

    // lock per sincronizzare i thread evitando che grafica e logica accedano ai dati del gioco contemporaneamente
    private final Object stateLock = new Object();

    /**
     * Constructs a GameLoop with the specified game panel for rendering.
     *
     * @param gamePanel the panel to repaint during the render phase
     * @param drone the drone to update
     */
    public GameLoop(GamePanel gamePanel, Drone drone) {
        this.gamePanel = gamePanel;
        this.drone = drone;
    }

    /**
     * Constructs a GameLoop without a view (test).
     */
    public GameLoop() {
        this.gamePanel = null;
        this.drone = null;
    }

    /**
     * Starts the game loop logic and rendering threads.
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
     * Stops the game loop and safely waits for both the logic and rendering
     * threads to terminate.
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
     * Continuously updates the game state at the defined UPS.
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
     * Continuously triggers the rendering of frames at the defined FPS.
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

    protected void update() {
        if (this.drone == null) return;
        
        // calcola deltaTime in secondi
        double deltaTime = 1.0 / UPS_SET;
        this.drone.updateState(deltaTime);
    }

    /**
     * Triggers the rendering of the game view.
     */
    protected void render() {
        if (gamePanel != null) {
            gamePanel.repaint();
        }
    }
}
