package it.unibo.agrobot.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import it.unibo.agrobot.controller.GameState;
import it.unibo.agrobot.controller.GameStateManager;
import it.unibo.agrobot.model.Market;

public class ShopMenuUI extends JPanel {

    private final Market market;
    private final GameStateManager stateManager;

    private final JPanel itemsPanel;
    private final JButton closeButton;

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
                    java.util.List<Component> buttons = new java.util.ArrayList<>();
                    for (Component c : itemsPanel.getComponents()) {
                        if (c instanceof JButton) buttons.add(c);
                    }   buttons.add(closeButton);
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

    public ShopMenuUI(Market market, GameStateManager stateManager) {
        this.market = market;
        this.stateManager = stateManager;

        this.setLayout(new BorderLayout());
        this.setBackground(new Color(50, 50, 50, 240));
        this.setBorder(BorderFactory.createLineBorder(Color.WHITE, 2));
        this.setPreferredSize(new Dimension(400, 300));

        JLabel titleLabel = new JLabel("MERCATO", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        this.add(titleLabel, BorderLayout.NORTH);

        itemsPanel = new JPanel(new GridLayout(0, 1, 0, 5));
        itemsPanel.setOpaque(false);
        itemsPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        
        this.add(itemsPanel, BorderLayout.CENTER);

        closeButton = new JButton("CHIUDI");
        closeButton.setFont(new Font("Arial", Font.BOLD, 16));
        closeButton.setBackground(Color.WHITE);
        closeButton.setForeground(new Color(50, 50, 50));
        closeButton.setFocusPainted(false);
        closeButton.addActionListener(e -> {
            if (this.stateManager != null) {
                this.stateManager.setState(GameState.PLAYING);
            }
        });
        closeButton.addKeyListener(menuKeyListener);

        JPanel bottomPanel = new JPanel();
        bottomPanel.setOpaque(false);
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        bottomPanel.add(closeButton);
        this.add(bottomPanel, BorderLayout.SOUTH);

        refreshView();
    }

    @Override
    public void setVisible(boolean b) {
        super.setVisible(b);
        if (b && itemsPanel.getComponentCount() > 0) {
            itemsPanel.getComponent(0).requestFocusInWindow();
        } else if (b) {
            closeButton.requestFocusInWindow();
        }
    }

    public final void refreshView() {
        int focusedIndex = -1;
        Component focusOwner = java.awt.KeyboardFocusManager.getCurrentKeyboardFocusManager().getFocusOwner();
        if (focusOwner != null && focusOwner.getParent() == itemsPanel) {
            Component[] comps = itemsPanel.getComponents();
            for (int i = 0; i < comps.length; i++) {
                if (comps[i] == focusOwner) {
                    focusedIndex = i;
                    break;
                }
            }
        }

        itemsPanel.removeAll();

        // fetch dinamico degli oggetti acquistabili
        java.util.Set<String> seedsToBuy = market.getPriceManager().getBuyableItems(it.unibo.agrobot.model.ItemType.SEED);
        for (String seed : seedsToBuy) {
            JButton buyBtn = new JButton("Compra Seme " + seed);
            buyBtn.addKeyListener(menuKeyListener);
            buyBtn.addActionListener(e -> {
                if (market.buySeed(seed)) {
                    refreshView();
                } else {
                    java.awt.Toolkit.getDefaultToolkit().beep();
                    buyBtn.setBackground(Color.RED);
                    javax.swing.Timer timer = new javax.swing.Timer(200, evt -> refreshView());
                    timer.setRepeats(false);
                    timer.start();
                }
            });
            itemsPanel.add(buyBtn);
        }

        //fetch dinamico dei consumabili (diserbante)
        java.util.Set<String> consumablesToBuy = market.getPriceManager().getBuyableItems(it.unibo.agrobot.model.ItemType.CONSUMABLE);
        for (String consumable : consumablesToBuy) {
            JButton buyBtn = new JButton("Compra " + consumable);
            buyBtn.addKeyListener(menuKeyListener);
            buyBtn.addActionListener(e -> {
                if (market.buyConsumable(consumable)) {
                    refreshView();
                } else {
                    java.awt.Toolkit.getDefaultToolkit().beep();
                    buyBtn.setBackground(Color.RED);
                    javax.swing.Timer timer = new javax.swing.Timer(200, evt -> refreshView());
                    timer.setRepeats(false);
                    timer.start();
                }
            });
            itemsPanel.add(buyBtn);
        }

        // fetch dinamico degli oggetti vendibili
        java.util.Set<String> cropsToSell = market.getPriceManager().getSellableItems(it.unibo.agrobot.model.ItemType.CROP);
        for (String crop : cropsToSell) {
            JButton sellBtn = new JButton("Vendi " + crop);
            sellBtn.addKeyListener(menuKeyListener);
            sellBtn.addActionListener(e -> {
                if (market.sellCrop(crop)) {
                    refreshView();
                } else {
                    java.awt.Toolkit.getDefaultToolkit().beep();
                    sellBtn.setBackground(Color.RED);
                    javax.swing.Timer timer = new javax.swing.Timer(200, evt -> refreshView());
                    timer.setRepeats(false);
                    timer.start();
                }
            });
            itemsPanel.add(sellBtn);
        }

        this.revalidate();
        this.repaint();
        
        if (itemsPanel.getComponentCount() > 0 && this.isVisible()) {
            if (focusedIndex >= 0 && focusedIndex < itemsPanel.getComponentCount()) {
                itemsPanel.getComponent(focusedIndex).requestFocusInWindow();
            }
        }
    }
}
