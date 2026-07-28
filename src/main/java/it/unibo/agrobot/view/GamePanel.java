package it.unibo.agrobot.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JPanel;

public class GamePanel extends JPanel {

    private static final int TILE_SIZE = 64;
    private static final int GRID_COLS = 8;
    private static final int GRID_ROWS = 8;

    private DroneView droneView;

    public GamePanel(DroneView droneView) {
        this.droneView = droneView;
        setPreferredSize(new Dimension(GRID_COLS * TILE_SIZE, GRID_ROWS * TILE_SIZE));
        setBackground(new Color(120, 180, 80));
        setDoubleBuffered(true);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        g2d.setColor(new Color(100, 160, 60));
        for (int x = 0; x < GRID_COLS; x++) {
            for (int y = 0; y < GRID_ROWS; y++) {
                g2d.drawRect(x * TILE_SIZE, y * TILE_SIZE, TILE_SIZE, TILE_SIZE);
            }
        }

        if (droneView != null) {
            droneView.draw(g2d, TILE_SIZE);
        }
    }
}
