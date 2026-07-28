package it.unibo.agrobot.view;

import javax.swing.JFrame;

public class MainFrame extends JFrame {
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
