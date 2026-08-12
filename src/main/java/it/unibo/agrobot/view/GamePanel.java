package it.unibo.agrobot.view;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.JPanel;

import it.unibo.agrobot.controller.GameState;
import it.unibo.agrobot.controller.GameStateManager;

/**
 * gestisce la rappresentazione grafica del pannello di gioco
 * estende JPanel e e ridefinisce paintComponent per disegnare la griglia e il drone
 */
public class GamePanel extends JPanel {
    
    private final GridView gridView;
    private final DroneView droneView;
    private final HUDView hudView;
    private final GameStateManager stateManager;

    /**
     * costruisce il pannello di gioco
     * 
     * @param gridView vista dedicata alla griglia
     * @param droneView vista dedicata al drone
     * @param hudView vista dedicata all'HUD
     * @param width larghezza in pixel della finestra
     * @param height altezza in pixel della finestra
     * @param stateManager gestore degli stati di gioco
     */
    public GamePanel(GridView gridView, DroneView droneView, HUDView hudView, int width, int height, GameStateManager stateManager) {
        this.gridView = gridView;
        this.droneView = droneView;
        this.hudView = hudView;
        this.stateManager = stateManager;
        
        // dimensioni preferite del pannello
        this.setPreferredSize(new Dimension(width, height));
        // pannello focusable per ricevere input da tastiera
        this.setFocusable(true);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g); // puliamo il pannello prima di disegnare

        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // disegna la griglia di gioco
        g2d.setColor(new java.awt.Color(124, 204, 76));
        g2d.fillRect(0, 0, this.getWidth(), this.getHeight());

        int offsetX = 0;
        int offsetY = 0;

        // Calcoliamo la grandezza di una singola tile in base alla grandezza attuale della finestra
        if (this.gridView != null) {
            int hudSpace = 220; // spazio riservato a sinistra per l'HUD
            int availableWidthForGrid = this.getWidth() - hudSpace;
            
            int dynamicTileSize = Math.min(
                availableWidthForGrid / this.gridView.getCols(),
                this.getHeight() / this.gridView.getRows()
            );
            this.gridView.setTileSize(dynamicTileSize);

            // Calcoliamo di quanto spostare la griglia per centrarla nello spazio rimanente a destra dell'HUD
            int totalGridWidth = dynamicTileSize * this.gridView.getCols();
            int totalGridHeight = dynamicTileSize * this.gridView.getRows();
            
            offsetX = hudSpace + (availableWidthForGrid - totalGridWidth) / 2;
            offsetY = (this.getHeight() - totalGridHeight) / 2;
        }

        // Spostiamo il "pennello" per centrare tutto quello che stiamo per disegnare
        g2d.translate(offsetX, offsetY);

        if (this.gridView != null) {
            this.gridView.draw(g2d);
        }

        // disegna il drone sopra la griglia
        if (this.droneView != null && this.gridView != null) {
            this.droneView.draw(g2d, this.gridView.getTileSize());
        }
        
        // riportiamo il pennello alla posizione originale
        g2d.translate(-offsetX, -offsetY);
        // disegna l'HUD in sovraimpressione
        if (this.hudView != null) {
            this.hudView.draw(g2d, getWidth(), getHeight());
        }

        // overlay per lo stato di pausa
        if (this.stateManager != null && this.stateManager.getState() == GameState.PAUSED) {
            g2d.setColor(new Color(0, 0, 0, 150));
            g2d.fillRect(0, 0, getWidth(), getHeight());
            
            g2d.setColor(Color.WHITE);
            g2d.setFont(new Font("Arial", Font.BOLD, 64));
            String pauseText = "PAUSED";
            int stringWidth = g2d.getFontMetrics().stringWidth(pauseText);
            g2d.drawString(pauseText, (getWidth() - stringWidth) / 2, getHeight() / 2);
        }
    }
}
