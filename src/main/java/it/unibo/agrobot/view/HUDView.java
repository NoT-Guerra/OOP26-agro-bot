package it.unibo.agrobot.view;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;

import it.unibo.agrobot.model.Drone;
import it.unibo.agrobot.model.Inventory;
import it.unibo.agrobot.model.InventorySlot;

/**
 * gestisce la visualizzazione dell'HUD con le informazioni del drone:
 * soldi, batteria e serbatoio dell'acqua.
 */
public class HUDView {

    private final Drone drone;
    private boolean showControls = false;
    private final java.awt.Rectangle infoButtonBounds = new java.awt.Rectangle(20, 200, 30, 30);

    /**
     * crea l'HUD associato al drone.
     * 
     * @param drone il drone di cui visualizzare le informazioni
     */
    public HUDView(Drone drone) {
        this.drone = drone;
    }

    public boolean isInfoButtonClicked(int x, int y) {
        return infoButtonBounds.contains(x, y);
    }

    public void toggleControls() {
        showControls = !showControls;
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

        drawInventory(g2d);

        //disegna il pulsante informazioni
        g2d.setColor(new Color(0, 0, 0, 150));
        g2d.fillRoundRect(infoButtonBounds.x, infoButtonBounds.y, infoButtonBounds.width, infoButtonBounds.height, 15, 15);
        g2d.setColor(Color.WHITE);
        g2d.setFont(new Font("Arial", Font.BOLD, 18));
        g2d.drawString("i", infoButtonBounds.x + 12, infoButtonBounds.y + 21);

        //Disegna lo spazio verde con i comandi se showControls è true
        if (showControls) {
            g2d.setColor(new Color(34, 139, 34, 220)); // Verde per i comandi
            g2d.fillRoundRect(20, 240, 190, 160, 15, 15);
            g2d.setColor(Color.WHITE);
            g2d.setFont(new Font("Arial", Font.BOLD, 12));
            int startY = 260;
            g2d.drawString("w: andare su", 30, startY);
            g2d.drawString("s: andare giù", 30, startY + 20);
            g2d.drawString("a: andare a sinistra", 30, startY + 40);
            g2d.drawString("d: andare a destra", 30, startY + 60);
            g2d.drawString("spazio: raccogliere", 30, startY + 80);
            g2d.drawString("f: annaffiare", 30, startY + 100);
            g2d.drawString("e: ricaricarsi", 30, startY + 120);
        }
    }

    private void drawInventory(Graphics2D g2d) {
        Inventory inventory = this.drone.getInventory();
        if (inventory == null) {
            return;
        }

        int slotCount = inventory.getSlotCount();
        int slotSize = 50;
        int spacing = 10;

        
        int startX = 10;
        int startY = 120;

        g2d.setFont(new Font("Arial", Font.PLAIN, 12));
        FontMetrics fm = g2d.getFontMetrics();

        for (int i = 0; i < slotCount; i++) {
            InventorySlot slot = inventory.getSlot(i);
            int x = startX + (i * (slotSize + spacing));

            // Sfondo dello slot
            if (i == inventory.getSelectedSlotIndex()) {
                g2d.setColor(new Color(100, 200, 100, 200)); // slot selezionato
            } else {
                g2d.setColor(new Color(0, 0, 0, 150));
            }
            g2d.fillRoundRect(x, startY, slotSize, slotSize, 10, 10);
            
            // Bordo dello slot
            if (i == inventory.getSelectedSlotIndex()) {
                g2d.setColor(Color.GREEN);
            } else {
                g2d.setColor(Color.WHITE);
            }
            g2d.drawRoundRect(x, startY, slotSize, slotSize, 10, 10);

            String itemName = slot.getItemName();
            int quantity = slot.getQuantity();

            // Contenuto dello slot
            if (itemName != null && quantity > 0) {
                String quantityStr = String.valueOf(quantity);

                String shortName = itemName.length() > 6 ? itemName.substring(0, 6) + "." : itemName;
                int textX = x + (slotSize - fm.stringWidth(shortName)) / 2;
                g2d.drawString(shortName, textX, startY + 20);

                int qX = x + (slotSize - fm.stringWidth(quantityStr)) / 2;
                g2d.drawString(quantityStr, qX, startY + 40);
            }
        }
    }
}
