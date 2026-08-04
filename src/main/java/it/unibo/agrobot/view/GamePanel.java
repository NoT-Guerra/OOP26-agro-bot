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

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g); // puliamo il pannello prima di disegnare

        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // disegna la griglia di gioco
        if (this.gridView != null) {
            this.gridView.draw(g2d);
        }

        // disegna il drone sopra la griglia
        if (this.droneView != null && this.gridView != null) {
            this.droneView.draw(g2d, this.gridView.getTileSize());
        }
        
        // rilascia le risorse grafiche, per evitare memory leak
        g2d.dispose();
    }
}
