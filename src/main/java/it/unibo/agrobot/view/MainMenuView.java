package it.unibo.agrobot.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagLayout;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import it.unibo.agrobot.controller.GameState;
import it.unibo.agrobot.controller.GameStateManager;

// represents the main menu screen of the game
public class MainMenuView extends JPanel {

    /**
     * constructs the main menu view
     *
     * @param stateManager the state manager to update when the user clicks play
     */
    public MainMenuView(GameStateManager stateManager) {
        this.setLayout(new BorderLayout());
        this.setBackground(new Color(34, 139, 34)); // colore dello sfondo per il menu

        JLabel titleLabel = new JLabel("Agro-Bot", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 80));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(150, 0, 0, 0));
        this.add(titleLabel, BorderLayout.NORTH);

        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.setOpaque(false);
        
        JButton playButton = new JButton("PLAY");
        playButton.setFont(new Font("Arial", Font.BOLD, 40));
        playButton.setPreferredSize(new Dimension(250, 100));
        playButton.setFocusPainted(false);
        playButton.setBackground(Color.WHITE);
        playButton.setForeground(new Color(34, 139, 34));
        playButton.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        
        playButton.addActionListener(e -> {
            if (stateManager != null) {
                stateManager.setState(GameState.PLAYING);
            }
        });

        playButton.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    playButton.doClick();
                }
            }
        });

        centerPanel.add(playButton);
        this.add(centerPanel, BorderLayout.CENTER);
    }
}
