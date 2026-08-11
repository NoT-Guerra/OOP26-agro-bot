package it.unibo.agrobot;

import it.unibo.agrobot.model.DroneImpl;
import it.unibo.agrobot.model.GridImpl;
import it.unibo.agrobot.model.Position;
import it.unibo.agrobot.view.DroneView;
import it.unibo.agrobot.view.GamePanel;
import it.unibo.agrobot.view.GameWindow;
import it.unibo.agrobot.view.GridView;
import it.unibo.agrobot.view.HUDView;
import it.unibo.agrobot.controller.InputHandler;
import it.unibo.agrobot.controller.GameLoop;

public class App {

    public static void main(String[] args) {
        // creazione della griglia 10x10 e posizionamento iniziale del drone in (0,0)
        GridImpl grid = new GridImpl(10, 10);
        DroneImpl drone = new DroneImpl(new Position(0, 0));

        // inizializzazione della vista e della finestra di gioco
        int tileSize = 64; // dimensione in pixel di ogni tile della griglia
        GridView gridView = new GridView(grid, tileSize);
        DroneView droneView = new DroneView(drone);
        HUDView hudView = new HUDView(drone);

        // calcolo la dimensione della finestra in base alla griglia e alla dimensione delle tile
        int hudSpace = 220;
        int screenWidth = grid.getWidth() * tileSize + hudSpace;
        int screenHeight = grid.getHeight() * tileSize;

        GamePanel gamePanel = new GamePanel(gridView, droneView, hudView, screenWidth, screenHeight);
        GameWindow gameWindow = new GameWindow("Agro-Bot", gamePanel);

        // aggiungo il controller per gestire l'input, passandogli anche la griglia per i limiti
        InputHandler inputHandler = new InputHandler(drone, grid);
        gamePanel.addKeyListener(inputHandler);

        // mostro la finestra di gioco
        gameWindow.show();
        gamePanel.requestFocusInWindow(); // richiedo il focus per ricevere gli input
        
        // avvio il game loop per la logica e il rendering continuo
        GameLoop gameLoop = new GameLoop(gamePanel, drone);
        gameLoop.start();
    }
}
