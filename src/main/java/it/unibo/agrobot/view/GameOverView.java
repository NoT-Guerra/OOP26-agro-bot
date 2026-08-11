package it.unibo.agrobot.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import it.unibo.agrobot.controller.GameState;
import it.unibo.agrobot.controller.GameStateManager;

/**
 * rappresenta la schermata di game over
 */
public class GameOverView extends JPanel {

    /**
     * costruttore della schermata di game over
     *
     * @param stateManager lo state manager
     * @param onRestart runnable da chiamare quando si riavvia il gioco
     */
    public GameOverView(GameStateManager stateManager, Runnable onRestart) {
        this.setLayout(new BorderLayout());
        this.setBackground(new Color(139, 34, 34)); // rosso scuro

        JLabel titleLabel = new JLabel("GAME OVER", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 80));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(150, 0, 0, 0));
        this.add(titleLabel, BorderLayout.NORTH);

        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.setOpaque(false);

        JButton restartButton = new JButton("RESTART");
        restartButton.setFont(new Font("Arial", Font.BOLD, 30));
        restartButton.setPreferredSize(new Dimension(250, 80));
        restartButton.setFocusPainted(false);
        restartButton.setBackground(Color.WHITE);
        restartButton.setForeground(new Color(139, 34, 34));
        restartButton.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));

        restartButton.addActionListener(e -> {
            if (onRestart != null) {
                onRestart.run();
            }
            if (stateManager != null) {
                stateManager.setState(GameState.PLAYING);
            }
        });

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.gridx = 0;
        gbc.gridy = 0;
        centerPanel.add(restartButton, gbc);

        this.add(centerPanel, BorderLayout.CENTER);
    }
}
