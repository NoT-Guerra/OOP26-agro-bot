package it.unibo.agrobot;

public class GameLoop implements Runnable {
    private static final int UPS_SET = 60;
    private boolean running;
    private Thread thread;

    public void start() {
        if (running) {
            return;
        }
        running = true;
        thread = new Thread(this, "Logic Thread");
        thread.start();
    }

    public void stop() {
        if (!running) {
            return;
        }
        running = false;
        try {
            thread.join();
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

    protected void update() {
        // serve per aggiornare i vari oggetticon le funzioni di ognuno
    }
}
