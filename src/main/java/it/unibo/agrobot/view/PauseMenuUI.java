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

public class PauseMenuUI extends JPanel {

    public PauseMenuUI(GameStateManager stateManager, Runnable onRestart) {
        this.setLayout(new BorderLayout());
        this.setBackground(new Color(50, 50, 50, 240));
        this.setBorder(BorderFactory.createLineBorder(Color.WHITE, 2));
        this.setPreferredSize(new Dimension(400, 300));

        JLabel titleLabel = new JLabel("PAUSA", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 36));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        this.add(titleLabel, BorderLayout.NORTH);

        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.setOpaque(false);

        JButton continueButton = new JButton("CONTINUE");
        continueButton.setFont(new Font("Arial", Font.BOLD, 20));
        continueButton.setPreferredSize(new Dimension(200, 50));
        continueButton.setFocusPainted(false);
        continueButton.setBackground(Color.WHITE);
        continueButton.setForeground(new Color(50, 50, 50));
        continueButton.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        
        continueButton.addActionListener(e -> {
            if (stateManager != null) {
                stateManager.setState(GameState.PLAYING);
            }
        });

        JButton restartButton = new JButton("RESTART");
        restartButton.setFont(new Font("Arial", Font.BOLD, 20));
        restartButton.setPreferredSize(new Dimension(200, 50));
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
        centerPanel.add(continueButton, gbc);

        gbc.gridy = 1;
        centerPanel.add(restartButton, gbc);

        this.add(centerPanel, BorderLayout.CENTER);
    }
}
