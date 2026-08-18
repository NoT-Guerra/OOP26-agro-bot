package it.unibo.agrobot;

import java.awt.GridBagLayout;

import it.unibo.agrobot.controller.GameLoop;
import it.unibo.agrobot.controller.GameState;
import it.unibo.agrobot.controller.GameStateManager;
import it.unibo.agrobot.controller.InputHandler;
import it.unibo.agrobot.model.DroneImpl;
import it.unibo.agrobot.model.GridImpl;
import it.unibo.agrobot.model.Market;
import it.unibo.agrobot.model.Position;
import it.unibo.agrobot.model.PriceManager;
import it.unibo.agrobot.model.Storage;
import it.unibo.agrobot.model.Wallet;
import it.unibo.agrobot.view.DroneView;
import it.unibo.agrobot.view.GameOverView;
import it.unibo.agrobot.view.GamePanel;
import it.unibo.agrobot.view.GameWindow;
import it.unibo.agrobot.view.GridView;
import it.unibo.agrobot.view.HUDView;
import it.unibo.agrobot.view.MainMenuView;
import it.unibo.agrobot.view.ShopMenuUI;
import it.unibo.agrobot.view.StorageView;
import it.unibo.agrobot.view.PauseMenuUI;

public class App {

    public static void main(String[] args) {
        // creazione della griglia 10x10 e posizionamento iniziale del drone sulla base di ricarica (magazzino in 8,1)
        GridImpl grid = new GridImpl(10, 10);
        DroneImpl drone = new DroneImpl(new Position(8, 1));

        // inizializzazione della vista e della finestra di gioco
        int tileSize = 64; // dimensione in pixel di ogni tile della griglia
        GridView gridView = new GridView(grid, tileSize);
        DroneView droneView = new DroneView(drone);
        HUDView hudView = new HUDView(drone);

        // calcolo la dimensione della finestra in base alla griglia e alla dimensione delle tile
        int hudSpace = 220;
        int screenWidth = grid.getWidth() * tileSize + hudSpace;
        int screenHeight = grid.getHeight() * tileSize;

        GameStateManager stateManager = new GameStateManager();
        Storage storage = new Storage();
        Wallet wallet = drone.getWallet();
        wallet.addFunds(100.0);
        PriceManager priceManager = new PriceManager();
        
        // configurazione dei prezzi del mercato
        priceManager.setBuyPrice("Wheat", it.unibo.agrobot.model.ItemType.SEED, 10.0);
        priceManager.setSellPrice("Wheat", it.unibo.agrobot.model.ItemType.CROP, 20.0);
        priceManager.setBuyPrice("Corn", it.unibo.agrobot.model.ItemType.SEED, 15.0);
        priceManager.setSellPrice("Corn", it.unibo.agrobot.model.ItemType.CROP, 30.0);
        
        priceManager.setBuyPrice("Diserbante", it.unibo.agrobot.model.ItemType.CONSUMABLE, 5.0);
        
        Market market = new Market(drone.getInventory(), wallet, priceManager);
        Runnable onRestart = () -> {
            drone.reset();
            grid.reset();
            storage.clear();
            wallet.setBalance(100.0);
        };

        GamePanel gamePanel = new GamePanel(gridView, droneView, hudView, screenWidth, screenHeight, stateManager);
        
        ShopMenuUI shopMenuUI = new ShopMenuUI(market, stateManager);
        shopMenuUI.setVisible(false);
        
        PauseMenuUI pauseMenuUI = new PauseMenuUI(stateManager, onRestart);
        pauseMenuUI.setVisible(false);
        
        gamePanel.setLayout(new GridBagLayout());
        gamePanel.add(shopMenuUI);
        gamePanel.add(pauseMenuUI);

        GameWindow gameWindow = new GameWindow("Agro-Bot");
        MainMenuView mainMenuView = new MainMenuView(stateManager);
        GameOverView gameOverView = new GameOverView(stateManager, onRestart);
        StorageView storageView = new StorageView(stateManager, drone.getInventory(), storage);

        gameWindow.addPanel(mainMenuView, "MENU");
        gameWindow.addPanel(gamePanel, "PLAYING");
        gameWindow.addPanel(gameOverView, "GAME_OVER");
        gameWindow.addPanel(storageView, "STORAGE_MENU");

        stateManager.setOnStateChange(state -> {
            if (null != state) switch (state) {
                case MENU -> gameWindow.showPanel("MENU");
                case PLAYING -> {
                    gameWindow.showPanel("PLAYING");
                    shopMenuUI.setVisible(false);
                    pauseMenuUI.setVisible(false);
                    gamePanel.requestFocusInWindow(); // richiedo il focus per ricevere gli input
                }
                case PAUSED -> {
                    if (!shopMenuUI.isVisible()) {
                        pauseMenuUI.setVisible(true);
                    }
                    gameWindow.showPanel("PLAYING");
                }
                case GAME_OVER -> gameWindow.showPanel("GAME_OVER");
                case STORAGE_MENU -> {
                    storageView.refreshView();
                    gameWindow.showPanel("STORAGE_MENU");
                }
                default -> {
                }
            }
        });

        // aggiungo il controller per gestire l'input, passandogli anche la griglia per i limiti
        InputHandler inputHandler = new InputHandler(drone, grid, stateManager);
        inputHandler.setOpenShopAction(() -> {
            shopMenuUI.refreshView();
            shopMenuUI.setVisible(true);
            gamePanel.revalidate();
            gamePanel.repaint();
            stateManager.setState(GameState.PAUSED);
        });
        gamePanel.addKeyListener(inputHandler);

        // mostro la finestra di gioco
        gameWindow.show();
        stateManager.setState(GameState.MENU);
        
        // avvio il game loop per la logica e il rendering continuo
        GameLoop gameLoop = new GameLoop(gamePanel, grid, drone, stateManager);
        gameLoop.start();
    }
}
