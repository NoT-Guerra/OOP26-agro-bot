package it.unibo.agrobot.view;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;

import it.unibo.agrobot.model.Drone;
import it.unibo.agrobot.model.Inventory;
import it.unibo.agrobot.model.InventorySlot;
import it.unibo.agrobot.model.ItemType;

/**
 * gestisce la visualizzazione dell'HUD con le informazioni del drone: soldi,
 * batteria e serbatoio dell'acqua.
 */
public class HUDView {

    private final Drone drone;
    private it.unibo.agrobot.model.WeatherManager weatherManager;
    private boolean showControls = false;
    private boolean showHelp = false;
    private final java.awt.Rectangle infoButtonBounds = new java.awt.Rectangle(20, 230, 30, 30);
    private final java.awt.Rectangle helpButtonBounds = new java.awt.Rectangle(60, 230, 30, 30);
    private double currentScale = 1.0;

    /**
     * crea l'HUD associato al drone.
     *
     * @param drone il drone di cui visualizzare le informazioni
     */
    public HUDView(Drone drone) {
        this.drone = drone;
    }

    /**
     * Imposta il gestore del meteo per visualizzarlo nell'HUD.
     *
     * @param weatherManager il gestore meteo
     */
    public void setWeatherManager(it.unibo.agrobot.model.WeatherManager weatherManager) {
        this.weatherManager = weatherManager;
    }

    /**
     * Controlla se il pulsante delle info è stato cliccato.
     *
     * @param x la coordinata x del clic
     * @param y la coordinata y del clic
     * @return true se è stato cliccato
     */
    public boolean isInfoButtonClicked(int x, int y) {
        return infoButtonBounds.contains(x / currentScale, y / currentScale);
    }

    /**
     * Controlla se il pulsante di aiuto è stato cliccato.
     *
     * @param x la coordinata x del clic
     * @param y la coordinata y del clic
     * @return true se è stato cliccato
     */
    public boolean isHelpButtonClicked(int x, int y) {
        return helpButtonBounds.contains(x / currentScale, y / currentScale);
    }

    /**
     * Mostra o nasconde i controlli.
     */
    public void toggleControls() {
        showControls = !showControls;
        if (showControls) {
            showHelp = false; // chiude l'altro pannello

            }}

    /**
     * Mostra o nasconde la schermata di aiuto.
     */
    public void toggleHelp() {
        showHelp = !showHelp;
        if (showHelp) {
            showControls = false; // chiude l'altro pannello

            }}

    /**
     * disegna l'HUD sullo schermo.
     *
     * @param g2d il contesto grafico
     * @param screenWidth larghezza dello schermo
     * @param screenHeight altezza dello schermo
     */
    public void draw(Graphics2D g2d, int screenWidth, int screenHeight) {
        java.awt.geom.AffineTransform oldTransform = g2d.getTransform();

        double scaleY = (double) screenHeight / 640.0;
        double scaleX = (double) screenWidth / 860.0;
        currentScale = Math.min(scaleX, scaleY);

        g2d.scale(currentScale, currentScale);

        double logicalScreenHeight = screenHeight / currentScale;

        g2d.setFont(new Font("Arial", Font.BOLD, 18));

        g2d.setColor(new Color(0, 0, 0, 150));
        g2d.fillRoundRect(10, 10, 200, 130, 15, 15);

        g2d.setColor(Color.WHITE);
        double money = this.drone.getWallet().getBalance();
        g2d.drawString(String.format("Money: $%.2f", money), 20, 35);

        double battery = this.drone.getBatteryLevel();
        double maxBattery = this.drone.getMaxBatteryCapacity();
        g2d.drawString(String.format("Battery: %.0f/%.0f", battery, maxBattery), 20, 65);

        double water = this.drone.getWaterLevel();
        double maxWater = this.drone.getMaxWaterTankCapacity();
        g2d.drawString(String.format("Water Tank: %.0f/%.0f", water, maxWater), 20, 95);

        if (this.weatherManager != null) {
            String weatherText = "Weather: " + (this.weatherManager.getCurrentCondition() == it.unibo.agrobot.model.WeatherCondition.SUNNY ? "Sunny" : "Rainy");
            g2d.drawString(weatherText, 20, 125);
        }

        drawInventory(g2d);

        //disegna il pulsante informazioni
        g2d.setColor(new Color(0, 0, 0, 150));
        g2d.fillRoundRect(infoButtonBounds.x, infoButtonBounds.y, infoButtonBounds.width, infoButtonBounds.height, 15, 15);
        g2d.setColor(Color.WHITE);
        g2d.setFont(new Font("Arial", Font.BOLD, 18));
        g2d.drawString("i", infoButtonBounds.x + 12, infoButtonBounds.y + 21);

        //disegna il pulsante "?" per scopo del gioco
        g2d.setColor(new Color(0, 0, 0, 150));
        g2d.fillRoundRect(helpButtonBounds.x, helpButtonBounds.y, helpButtonBounds.width, helpButtonBounds.height, 15, 15);
        g2d.setColor(Color.WHITE);
        g2d.setFont(new Font("Arial", Font.BOLD, 18));
        g2d.drawString("?", helpButtonBounds.x + 10, helpButtonBounds.y + 21);

        //Disegna lo spazio verde con i comandi se showControls è true
        if (showControls) {
            String[] cmds = {
                "w: move up",
                "s: move down",
                "a: move left",
                "d: move right",
                "h: harvest",
                "c: plough",
                "i: irrigate",
                "p: plant",
                "1, 2, 3: choose specific slot",
                "u: use herbicide",
                "r: recharge",
                "m: warehouse/shop"
            };
            int lineHeight = 20;
            int fontSize = 12;

            int boxHeight = (cmds.length * lineHeight) + 15;
            int startYBox = 270;

            // Logica responsive: adatta posizione e dimensione in base allo schermo
            if (startYBox + boxHeight > logicalScreenHeight - 10) {
                startYBox = (int) logicalScreenHeight - boxHeight - 10;
                if (startYBox < 10) {
                    startYBox = 10;
                    int availableHeight = (int) logicalScreenHeight - 20;
                    double scale = (double) availableHeight / boxHeight;
                    fontSize = Math.max(8, (int) (fontSize * scale));
                    lineHeight = Math.max(10, (int) (20 * scale));
                    boxHeight = (cmds.length * lineHeight) + 15;
                }
            }

            g2d.setFont(new Font("Arial", Font.BOLD, fontSize));
            FontMetrics fm = g2d.getFontMetrics();
            int maxWidth = 0;
            for (String cmd : cmds) {
                int w = fm.stringWidth(cmd);
                if (w > maxWidth) {
                    maxWidth = w;
                }
            }
            int boxWidth = maxWidth + 20;

            g2d.setColor(new Color(34, 139, 34, 220));
            g2d.fillRoundRect(20, startYBox, boxWidth, boxHeight, 15, 15);

            g2d.setColor(Color.WHITE);
            int currentY = startYBox + lineHeight;
            for (String cmd : cmds) {
                g2d.drawString(cmd, 30, currentY);
                currentY += lineHeight;
            }
        } else if (showHelp) {
            String title = "GUIDE AND PURPOSE OF THE GAME:";
            String[] helpLines = {
                "Manage the agricultural drone,",
                "cultivate the fields and make profits!",
                "",
                "At the top left is the status",
                "of the drone and its inventory",
                "Inside the map on the right you will find:",
                "hangar  --> recharge battery and warehouse",
                "well  --> recharge water",
                "markets  --> buying and selling seeds and plants",
                "",
                "Crop growing steps:",
                "1. Plough (C) uncultivated land.",
                "2. Plant (P) seeds (choose slot with 1,2,3).",
                "3. Irrigate (I) to grow plants.",
                "4. Harvest (H) when mature.",
                "5. Sell (M) at the Shop to earn money.",
                "",
                "Vital and extra resources:",
                "- Battery: recharge it (R) at the Hangar.",
                "- Water: recharge it (R) at the Well.",
                "- If weeds grow you can't plant!",
                "- Buy the Herbicide (M) and use it (U) to clean.",
                "- If the battery runs out, you lose!"
            };

            int lineHeight = 18;
            int titleSize = 13;
            int plainSize = 12;

            int boxHeight = 20 + (helpLines.length * lineHeight) + 15;
            int startYBox = 270;

            // Logica responsive: adatta posizione e dimensione in base allo schermo
            if (startYBox + boxHeight > logicalScreenHeight - 10) {
                startYBox = (int) logicalScreenHeight - boxHeight - 10;
                if (startYBox < 10) {
                    startYBox = 10;
                    int availableHeight = (int) logicalScreenHeight - 20;
                    double scale = (double) availableHeight / boxHeight;
                    titleSize = Math.max(9, (int) (titleSize * scale));
                    plainSize = Math.max(8, (int) (plainSize * scale));
                    lineHeight = Math.max(10, (int) (18 * scale));
                    boxHeight = 20 + (helpLines.length * lineHeight) + 15;
                }
            }

            g2d.setFont(new Font("Arial", Font.BOLD, titleSize));
            FontMetrics fmBold = g2d.getFontMetrics();
            int maxWidth = fmBold.stringWidth(title);

            g2d.setFont(new Font("Arial", Font.PLAIN, plainSize));
            FontMetrics fmPlain = g2d.getFontMetrics();
            for (String line : helpLines) {
                int w = fmPlain.stringWidth(line);
                if (w > maxWidth) {
                    maxWidth = w;
                }
            }

            int boxWidth = maxWidth + 20;

            g2d.setColor(new Color(70, 130, 180, 220));
            g2d.fillRoundRect(20, startYBox, boxWidth, boxHeight, 15, 15);

            g2d.setColor(Color.WHITE);
            int currentY = startYBox + 20;
            g2d.setFont(new Font("Arial", Font.BOLD, titleSize));
            g2d.drawString(title, 30, currentY);

            currentY += lineHeight;
            g2d.setFont(new Font("Arial", Font.PLAIN, plainSize));
            for (String line : helpLines) {
                g2d.drawString(line, 30, currentY);
                currentY += lineHeight;
            }
        }

        g2d.setTransform(oldTransform);
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
        int startY = 150;

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

                String displayName = itemName;
                if (slot.getType() == ItemType.SEED) {
                    displayName = "Seme " + itemName;
                }
                String shortName = displayName.length() > 9 ? displayName.substring(0, 9) + "." : displayName;
                int textX = x + (slotSize - fm.stringWidth(shortName)) / 2;
                g2d.drawString(shortName, textX, startY + 20);

                int qX = x + (slotSize - fm.stringWidth(quantityStr)) / 2;
                g2d.drawString(quantityStr, qX, startY + 40);
            }
        }
    }
}
