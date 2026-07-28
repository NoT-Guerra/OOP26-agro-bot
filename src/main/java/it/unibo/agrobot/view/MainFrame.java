package it.unibo.agrobot.view;

import javax.swing.JFrame;

public class MainFrame extends JFrame {

    /**
     * Constructs the main frame of the application.
     *
     * @param gamePanel the panel responsible for rendering the game view
     */
    public MainFrame(GamePanel gamePanel) {
        setTitle("Agro-Bot");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        add(gamePanel);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }
}
