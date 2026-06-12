package it.unibo.agrobot.view;

import it.unibo.agrobot.model.Drone;
import it.unibo.agrobot.model.DroneImpl;
import it.unibo.agrobot.model.Position;
import it.unibo.agrobot.model.Direction;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

/**
 * demo visiva per testare il rendering del drone
 * apre una finestra con una griglia e permette di muovere
 * il drone con i tasti W/A/S/D.
 * non e un test JUnit, e un programmino di verifica visiva
 */
public class DroneViewDemo extends JPanel {

    private static final int TILE_SIZE = 64;
    private static final int GRID_COLS = 8;
    private static final int GRID_ROWS = 8;

    private Drone drone;
    private DroneView droneView;

    public DroneViewDemo() {
        this.drone = new DroneImpl(new Position(3.0, 3.0));
        this.droneView = new DroneView(this.drone);

        setPreferredSize(new Dimension(GRID_COLS * TILE_SIZE, GRID_ROWS * TILE_SIZE));
        setBackground(new Color(120, 180, 80));

        //timer che simula il game loop a circa 60 fps
        Timer gameLoop = new Timer(16, e -> {
            this.drone.updateState(0.016);
            repaint();
        });
        gameLoop.start();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        //disegna la griglia
        g2d.setColor(new Color(100, 160, 60));
        for (int x = 0; x < GRID_COLS; x++) {
            for (int y = 0; y < GRID_ROWS; y++) {
                g2d.drawRect(x * TILE_SIZE, y * TILE_SIZE, TILE_SIZE, TILE_SIZE);
            }
        }

        //disegna il drone
        this.droneView.draw(g2d, TILE_SIZE);
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Demo Drone");
        DroneViewDemo demo = new DroneViewDemo();

        //cattura i tasti wasd
        frame.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_W: demo.drone.move(Direction.UP); break;
                    case KeyEvent.VK_S: demo.drone.move(Direction.DOWN); break;
                    case KeyEvent.VK_A: demo.drone.move(Direction.LEFT); break;
                    case KeyEvent.VK_D: demo.drone.move(Direction.RIGHT); break;
                }
            }
        });

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(demo);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
