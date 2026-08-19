package it.unibo.agrobot.controller;

import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

import it.unibo.agrobot.model.Direction;
import it.unibo.agrobot.model.Drone;
import it.unibo.agrobot.model.Inventory;
import it.unibo.agrobot.model.Position;
import it.unibo.agrobot.model.Wallet;

class GameStateTest {

    @Test
    void testStateTransitionAndScreenChange() {
        GameStateManager stateManager = new GameStateManager();
        AtomicReference<GameState> notifiedState = new AtomicReference<>();
        stateManager.setOnStateChange(state -> notifiedState.set(state));
        
        assertEquals(GameState.MENU, stateManager.getState(), "Lo stato iniziale dovrebbe essere MENU");
        
        // passaggio a PLAYING
        stateManager.setState(GameState.PLAYING);
        assertEquals(GameState.PLAYING, stateManager.getState(), "Lo stato dovrebbe essere aggiornato a PLAYING");
        assertEquals(GameState.PLAYING, notifiedState.get(), "Il listener dovrebbe aver ricevuto lo stato PLAYING");
        
        // passaggio a PAUSED
        stateManager.setState(GameState.PAUSED);
        assertEquals(GameState.PAUSED, stateManager.getState(), "Lo stato dovrebbe essere aggiornato a PAUSED");
        assertEquals(GameState.PAUSED, notifiedState.get(), "Il listener dovrebbe aver ricevuto lo stato PAUSED");
    }

    @Test
    void testLogicThreadConsumption() throws InterruptedException {
        //per contare chiamate a updateState del drone
        final int[] droneUpdateCount = { 0 };
        
        Drone dummyDrone = new Drone() {
            @Override public void updateState(double deltaTime) {
                droneUpdateCount[0]++;
            }
            @Override public Position getPosition() { return null; }
            @Override public double getBatteryLevel() { return 0; }
            @Override public double getWaterLevel() { return 0; }
            @Override public boolean move(Direction dir) { return false; }
            @Override public boolean isMoving() { return false; }
            @Override public void rechargeWaterAtWell() {}
            @Override public void plow() {}
            @Override public void harvest() {}
            @Override public boolean irrigate() { return false; }
            @Override public boolean isDead() { return false; }
            @Override public void rechargeAtHangar() {}
            @Override public Inventory getInventory() { return null; }
            @Override public Wallet getWallet() { return null; }
            @Override public void reset() {}
            @Override public void upgradeBatteryMaxCapacity(double amount) {}
            @Override public double getMaxBatteryCapacity() { return 100.0; }
            @Override public void upgradeWaterTankMaxCapacity(double amount) {}
            @Override public double getMaxWaterTankCapacity() { return 50.0; }
        };

        GameStateManager stateManager = new GameStateManager();
        // impostiamo lo stato inziale a MENU (NON dovrebbe consumare tick)
        stateManager.setState(GameState.MENU);
        GameLoop gameLoop = new GameLoop(null, null, dummyDrone, stateManager);
        gameLoop.start();
        // attendiamo per vedere se in stato MENU il drone viene aggiornato
        Thread.sleep(500);
        assertEquals(0, droneUpdateCount[0], "Il Logic Thread non dovrebbe consumare tick (aggiornare il drone) mentre lo stato è MENU");
        // cambiamo stato in PLAYING
        stateManager.setState(GameState.PLAYING);
        //attendiamo per assicurarci che i tick vengano consumati
        Thread.sleep(500);
        //fermiamo il loop
        gameLoop.stop();
        assertTrue(droneUpdateCount[0] > 0, "Il Logic Thread dovrebbe iniziare a consumare tick (aggiornare il drone) quando lo stato è PLAYING. "
                + "Chiamate effettive: " + droneUpdateCount[0]);
    }
}
