package it.unibo.agrobot;

public class GameLoop implements Runnable {
    private static final int UPS_SET = 60;
    private static final int FPS_SET = 60;
    
    private boolean running;
    private Thread logicThread;
    private Thread renderThread;

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
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        long previousTime = System.nanoTime();
        double timePerUpdate = 1000000000.0 / UPS_SET;
        double delta = 0;

        while (running) {
            long currentTime = System.nanoTime();
            delta += (currentTime - previousTime) / timePerUpdate;
            previousTime = currentTime;

            if (delta >= 1) {
                update();
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

    private void renderLoop() { // lo stesso ragionamento fatto per il thread principale, ma per il thread di rendering
        long previousTime = System.nanoTime();
        double timePerFrame = 1000000000.0 / FPS_SET;
        double delta = 0;

        while (running) {
            long currentTime = System.nanoTime();
            delta += (currentTime - previousTime) / timePerFrame;
            previousTime = currentTime;

            if (delta >= 1) {
                render();
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
        // serve per aggiornare i vari oggetticon le funzioni di ognuno
    }

    protected void render() {
        // serve per il rendering grafico
    }
}
