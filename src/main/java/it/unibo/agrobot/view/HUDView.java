package it.unibo.agrobot.view;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;

import it.unibo.agrobot.model.Drone;

/**
 * gestisce la visualizzazione dell'HUD con le informazioni del drone:
 * soldi, batteria e serbatoio dell'acqua.
 */
public class HUDView {

    private final Drone drone;

    /**
     * crea l'HUD associato al drone.
     * 
     * @param drone il drone di cui visualizzare le informazioni
     */
    public HUDView(Drone drone) {
        this.drone = drone;
    }

    /**
     * disegna l'HUD sullo schermo.
     * 
     * @param g2d il contesto grafico
     * @param screenWidth larghezza dello schermo
     * @param screenHeight altezza dello schermo
     */
    public void draw(Graphics2D g2d, int screenWidth, int screenHeight) {
        g2d.setFont(new Font("Arial", Font.BOLD, 18));

        g2d.setColor(new Color(0, 0, 0, 150));
        g2d.fillRoundRect(10, 10, 200, 100, 15, 15);
        
        g2d.setColor(Color.WHITE);
        double money = this.drone.getWallet().getBalance();
        g2d.drawString(String.format("Money: $%.2f", money), 20, 35);
        
        double battery = this.drone.getBatteryLevel();
        g2d.drawString(String.format("Battery: %.0f", battery), 20, 65);
        
        double water = this.drone.getWaterLevel();
        g2d.drawString(String.format("Water Tank: %.0f", water), 20, 95);
    }
}
