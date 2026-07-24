package it.unibo.agrobot;

public class App {
    public String getGreeting() {
        return "Welcome to Agro-Bot!";
    }

    public static void main(String[] args) {
        System.out.println(new App().getGreeting());
        
        GameLoop gameLoop = new GameLoop();
        gameLoop.start();
    }
}
