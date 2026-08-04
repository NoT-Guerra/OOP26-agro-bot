package it.unibo.agrobot;

import it.unibo.agrobot.model.DroneImpl;
import it.unibo.agrobot.model.GridImpl;
import it.unibo.agrobot.model.Position;
import it.unibo.agrobot.view.DroneView;
import it.unibo.agrobot.view.GamePanel;
import it.unibo.agrobot.view.GameWindow;
import it.unibo.agrobot.view.GridView;

public class App {

    public static void main(String[] args) {
        // creazione della griglia 10x10 e posizionamento iniziale del drone in (0,0)
        GridImpl grid = new GridImpl(10, 10);
        DroneImpl drone = new DroneImpl(new Position(0, 0));

        // inizializzazione della vista e della finestra di gioco
        int tileSize = 64; // dimensione in pixel di ogni tile della griglia
        GridView gridView = new GridView(grid, tileSize);
        DroneView droneView = new DroneView(drone);

        // calcolo la dimensione della finestra in base alla griglia e alla dimensione delle tile
        int screenWidth = grid.getWidth() * tileSize;
        int screenHeight = grid.getHeight() * tileSize;

        GamePanel gamePanel = new GamePanel(gridView, droneView, screenWidth, screenHeight);
        GameWindow gameWindow = new GameWindow("Agro-Bot", gamePanel);

        // mostro la finestra di gioco
        gameWindow.show();
    }
}
