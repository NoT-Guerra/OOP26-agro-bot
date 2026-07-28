package it.unibo.agrobot.view;

import javax.swing.JPanel;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

/**
 * gestisce la rappresentazione grafica del pannello di gioco
 * estende JPanel e e ridefinisce paintComponent per disegnare la griglia e il drone
 */
public class GamePanel extends JPanel {
    
    private final GridView gridView;
    private final DroneView droneView;

    /**
     * costruisce il pannello di gioco
     * 
     * @param gridView vista dedicata alla griglia
     * @param droneView vista dedicata al drone
     * @param width larghezza in pixel della finestra
     * @param height altezza in pixel della finestra
     */
    public GamePanel(GridView gridView, DroneView droneView, int width, int height) {
        this.gridView = gridView;
        this.droneView = droneView;
        
        // dimensioni preferite del pannello
        this.setPreferredSize(new Dimension(width, height));
        // pannello focusable per ricevere input da tastiera
        this.setFocusable(true);
    }
}
