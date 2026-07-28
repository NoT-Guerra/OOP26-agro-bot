package it.unibo.agrobot;

import it.unibo.agrobot.model.Drone;
import it.unibo.agrobot.model.DroneImpl;
import it.unibo.agrobot.model.Position;
import it.unibo.agrobot.view.DroneView;
import it.unibo.agrobot.view.GamePanel;
import it.unibo.agrobot.view.MainFrame;

public class App {
    public String getGreeting() {
        return "Welcome to Agro-Bot!";
    }

    public static void main(String[] args) {
        System.out.println(new App().getGreeting());
        
        Drone drone = new DroneImpl(new Position(3.0, 3.0));
        DroneView droneView = new DroneView(drone);
        
        GamePanel gamePanel = new GamePanel(droneView);
        MainFrame mainFrame = new MainFrame(gamePanel);

        GameLoop gameLoop = new GameLoop(gamePanel);
        gameLoop.start();
    }
}
