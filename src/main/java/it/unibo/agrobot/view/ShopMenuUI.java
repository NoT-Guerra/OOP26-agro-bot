package it.unibo.agrobot.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;

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

        JButton closeButton = new JButton("CHIUDI");
        closeButton.setFont(new Font("Arial", Font.BOLD, 16));
        closeButton.setBackground(Color.WHITE);
        closeButton.setForeground(new Color(50, 50, 50));
        closeButton.setFocusPainted(false);
        closeButton.addActionListener(e -> {
            if (this.stateManager != null) {
                this.stateManager.setState(GameState.PLAYING);
            }
        });

        JPanel bottomPanel = new JPanel();
        bottomPanel.setOpaque(false);
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        bottomPanel.add(closeButton);
        this.add(bottomPanel, BorderLayout.SOUTH);

        refreshView();
    }

    public final void refreshView() {
        itemsPanel.removeAll();

        // fetch dinamico degli oggetti acquistabili
        java.util.Set<String> seedsToBuy = market.getPriceManager().getBuyableItems(it.unibo.agrobot.model.ItemType.SEED);
        for (String seed : seedsToBuy) {
            JButton buyBtn = new JButton("Compra Seme " + seed);
            buyBtn.addActionListener(e -> {
                market.buySeed(seed);
                refreshView();
            });
            itemsPanel.add(buyBtn);
        }

        // fetch dinamico degli oggetti vendibili
        java.util.Set<String> cropsToSell = market.getPriceManager().getSellableItems(it.unibo.agrobot.model.ItemType.CROP);
        for (String crop : cropsToSell) {
            JButton sellBtn = new JButton("Vendi " + crop);
            sellBtn.addActionListener(e -> {
                market.sellCrop(crop);
                refreshView();
            });
            itemsPanel.add(sellBtn);
        }

        this.revalidate();
        this.repaint();
    }
}
