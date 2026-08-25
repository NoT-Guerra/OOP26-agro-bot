package it.unibo.agrobot.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import it.unibo.agrobot.controller.GameState;
import it.unibo.agrobot.controller.GameStateManager;

/**
 * Interfaccia utente per il menu di pausa del gioco.
 */
public class PauseMenuUI extends JPanel {

    private final JButton continueButton;
    private final JButton restartButton;
    private final GameStateManager stateManager;

    private final KeyAdapter menuKeyListener = new KeyAdapter() {
        @Override
        public void keyPressed(KeyEvent e) {
            Component src = (Component) e.getSource();
            switch (e.getKeyCode()) {
                case KeyEvent.VK_ESCAPE -> {
                    if (stateManager != null) {
                        stateManager.setState(GameState.PLAYING);
                    }
                }
                case KeyEvent.VK_ENTER -> {
                    if (src instanceof JButton jButton) {
                        jButton.doClick();
                    }
                }
                case KeyEvent.VK_UP, KeyEvent.VK_DOWN -> {
                    java.util.List<Component> buttons = java.util.Arrays.asList(continueButton, restartButton);
                    int index = buttons.indexOf(src);
                    if (index != -1) {
                        if (e.getKeyCode() == KeyEvent.VK_UP) {
                            index = (index - 1 + buttons.size()) % buttons.size();
                        } else {
                            index = (index + 1) % buttons.size();
                        }
                        buttons.get(index).requestFocusInWindow();
                    }
                }
                default -> {
                }
            }
        }
    };

    /** Listener per il focus dei pulsanti. */
    private final FocusAdapter buttonFocusListener = new FocusAdapter() {
        @Override
        public void focusGained(FocusEvent e) {
            e.getComponent().setBackground(Color.LIGHT_GRAY);
        }
        @Override
        public void focusLost(FocusEvent e) {
            e.getComponent().setBackground(Color.WHITE);
        }
    };

    /**
     * Costruisce il menu di pausa.
     *
     * @param stateManager gestore dello stato del gioco
     * @param onRestart azione da eseguire al riavvio
     */
    public PauseMenuUI(GameStateManager stateManager, Runnable onRestart) {
        this.stateManager = stateManager;
        this.setLayout(new BorderLayout());
        this.setBackground(new Color(50, 50, 50, 240));
        this.setBorder(BorderFactory.createLineBorder(Color.WHITE, 2));
        this.setPreferredSize(new Dimension(400, 300));

        JLabel titleLabel = new JLabel("PAUSE", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 36));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        this.add(titleLabel, BorderLayout.NORTH);

        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.setOpaque(false);

        continueButton = new JButton("CONTINUE");
        continueButton.setFont(new Font("Arial", Font.BOLD, 20));
        continueButton.setPreferredSize(new Dimension(200, 50));
        continueButton.setFocusPainted(false);
        continueButton.setBackground(Color.WHITE);
        continueButton.setForeground(new Color(50, 50, 50));
        continueButton.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        
        continueButton.addActionListener(e -> {
            if (this.stateManager != null) {
                this.stateManager.setState(GameState.PLAYING);
            }
        });
        continueButton.addKeyListener(menuKeyListener);
        continueButton.addFocusListener(buttonFocusListener);

        restartButton = new JButton("RESTART");
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
            if (this.stateManager != null) {
                this.stateManager.setState(GameState.PLAYING);
            }
        });
        restartButton.addKeyListener(menuKeyListener);
        restartButton.addFocusListener(buttonFocusListener);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.gridx = 0;
        gbc.gridy = 0;
        centerPanel.add(continueButton, gbc);

        gbc.gridy = 1;
        centerPanel.add(restartButton, gbc);

        this.add(centerPanel, BorderLayout.CENTER);
    }

    @Override
    public void setVisible(boolean b) {
        super.setVisible(b);
        if (b) {
            javax.swing.SwingUtilities.invokeLater(() -> continueButton.requestFocusInWindow());
        }
    }
}
