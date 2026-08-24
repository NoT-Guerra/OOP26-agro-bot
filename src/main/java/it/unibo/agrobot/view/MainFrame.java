package it.unibo.agrobot.view;

import javax.swing.JFrame;

/**
 * Rappresenta il frame principale dell'applicazione.
 */
public class MainFrame extends JFrame {

    /**
     * Costruisce il frame principale dell'applicazione.
     *
     * @param gamePanel il pannello responsabile del rendering della vista di gioco
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
