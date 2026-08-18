package it.unibo.agrobot.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

class DroneTest {

    @Test
    void testInitialPosition() {
        Position initial = new Position(0.0, 0.0);
        Drone drone = new DroneImpl(initial);
        
        assertEquals(0.0, drone.getPosition().getX(), 0.001);
        assertEquals(0.0, drone.getPosition().getY(), 0.001);
        System.out.println("testInitialPosition: PASSATO");
    }

    @Test
    void testSmoothMovement() {
        Position initial = new Position(0.0, 0.0);
        Drone drone = new DroneImpl(initial);
        
        //diamo il comando di muoversi verso il basso perche ora sopra non puo piu andare
        assertTrue(drone.move(Direction.DOWN));
        assertTrue(drone.isMoving());
        
        //Simuliamo un quarto di secondo (Velocità 2.0 * 0.25s = 0.5 spazio percorso)
        drone.updateState(0.25);
        assertEquals(0.0, drone.getPosition().getX(), 0.001);
        assertEquals(0.5, drone.getPosition().getY(), 0.001);
        assertTrue(drone.isMoving()); // È ancora in viaggio
        
        //simuliamo un altro quarto di secondo (raggiunge 1.0 esatto)
        drone.updateState(0.25);
        assertEquals(0.0, drone.getPosition().getX(), 0.001);
        assertEquals(1.0, drone.getPosition().getY(), 0.001);
        
        //deve essersi fermato e allineato
        assertFalse(drone.isMoving());
        
        System.out.println("testSmoothMovement: PASSATO");
    }

    @Test
    void testMovementBatteryConsumption() {
        Position initial = new Position(0.0, 0.0);
        Drone drone = new DroneImpl(initial);
        double initialBattery = drone.getBatteryLevel();
        
        //avviamo il movimento
        drone.move(Direction.RIGHT);
        
        //la batteria viene scalata all'avvio del movimento
        assertTrue(drone.getBatteryLevel() < initialBattery);
        System.out.println("testMovementBatteryConsumption: PASSATO");
    }

    @Test
    void testNoMovementWhenDead() {
        Position initial = new Position(0.0, 0.0);
        Drone drone = new DroneImpl(initial);
        
        //scarichiamo la batteria forzando 100 movimenti (100 * 1.0 = 100)
        for (int i = 0; i < 100; i++) {
            drone.move(Direction.UP);
            drone.updateState(0.5); // per farlo arrivare subito a destinazione (2.0 * 0.5 = 1.0)
        }
        
        //orra la batteria è 0, il movimento deve essere rifiutato
        assertFalse(drone.move(Direction.UP)); 
        assertFalse(drone.isMoving());
        
        System.out.println("testNoMovementWhenDead: PASSATO");
    }

    @Test
    void testAgriculturalActionsConsumption() {
        Position initial = new Position(0.0, 0.0);
        Drone drone = new DroneImpl(initial);
        double initialBattery = drone.getBatteryLevel();
        
        drone.plow();
        assertTrue(drone.getBatteryLevel() < initialBattery);
        
        double batteryAfterPlow = drone.getBatteryLevel();
        drone.harvest();
        assertTrue(drone.getBatteryLevel() < batteryAfterPlow);
        
        System.out.println("testAgriculturalActionsConsumption: PASSATO");
    }

    @Test
    void testDeathAndRecharge() {
        Position initial = new Position(0.0, 0.0);
        Drone drone = new DroneImpl(initial);
        
        for (int i = 0; i < 100; i++) {
            drone.move(Direction.UP);
            drone.updateState(0.5);
        }
        
        assertTrue(drone.isDead());
        
        drone.rechargeAtHangar();
        assertFalse(drone.isDead());
        assertEquals(100.0, drone.getBatteryLevel(), 0.001);
        
        System.out.println("testDeathAndRecharge: PASSATO");
    }

    @Test
    void testInitialWaterLevel() {
        Position initial = new Position(0.0, 0.0);
        Drone drone = new DroneImpl(initial);
        
        assertEquals(0.0, drone.getWaterLevel(), 0.001);
        System.out.println("testInitialWaterLevel: PASSATO");
    }

    @Test
    void testRechargeWater() {
        Position initial = new Position(0.0, 0.0);
        Drone drone = new DroneImpl(initial);
        
        drone.rechargeWaterAtWell(); 
        
        assertEquals(50.0, drone.getWaterLevel(), 0.001);
        System.out.println("testRechargeWater: PASSATO");
    }

    @Test
    void testIrrigate() {
        Position initial = new Position(0.0, 0.0);
        Drone drone = new DroneImpl(initial);
        
        assertFalse(drone.irrigate());
        
        drone.rechargeWaterAtWell();
        
        double batteryBefore = drone.getBatteryLevel();
        double waterBefore = drone.getWaterLevel();
        
        assertTrue(drone.irrigate());
        
        assertEquals(waterBefore - 10.0, drone.getWaterLevel(), 0.001);
        assertTrue(drone.getBatteryLevel() < batteryBefore);
        
        System.out.println("testIrrigate: PASSATO");
    }
}
