package it.unibo.agrobot.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import it.unibo.agrobot.controller.GameState;
import it.unibo.agrobot.controller.GameStateManager;
import it.unibo.agrobot.model.Inventory;
import it.unibo.agrobot.model.InventorySlot;
import it.unibo.agrobot.model.Storage;

/**
 * vista per la gestione dello storage del drone
 */
public class StorageView extends JPanel {

    private final GameStateManager stateManager;
    private final Inventory droneInventory;
    private final Storage storage;

    private final JPanel dronePanel;
    private final JPanel storagePanel;

    /**
     * 
     * @param stateManager
     * @param droneInventory
     * @param storage
     */
    public StorageView(GameStateManager stateManager, Inventory droneInventory, Storage storage) {
        this.stateManager = stateManager;
        this.droneInventory = droneInventory;
        this.storage = storage;

        this.setLayout(new BorderLayout());
        this.setBackground(new Color(50, 50, 50));

        JLabel titleLabel = new JLabel("MAGAZZINO", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 40));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        this.add(titleLabel, BorderLayout.NORTH);

        JPanel centerPanel = new JPanel(new GridLayout(1, 2, 20, 0));
        centerPanel.setOpaque(false);
        centerPanel.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));

        dronePanel = new JPanel(new GridLayout(10, 1, 0, 5));
        dronePanel.setOpaque(false);
        dronePanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.WHITE), "Inventario Drone"));
        ((javax.swing.border.TitledBorder) dronePanel.getBorder()).setTitleColor(Color.WHITE);

        storagePanel = new JPanel(new GridLayout(10, 1, 0, 5));
        storagePanel.setOpaque(false);
        storagePanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.WHITE), "Deposito Magazzino"));
        ((javax.swing.border.TitledBorder) storagePanel.getBorder()).setTitleColor(Color.WHITE);

        centerPanel.add(dronePanel);
        centerPanel.add(storagePanel);

        this.add(centerPanel, BorderLayout.CENTER);

        JButton closeButton = new JButton("CHIUDI");
        closeButton.setFont(new Font("Arial", Font.BOLD, 24));
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
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        bottomPanel.add(closeButton);
        this.add(bottomPanel, BorderLayout.SOUTH);

        refreshView();
    }

    /**
     * aggiorna la vista leggendo nuovamente gli inventari deve essere chiamato
     * ogni volta che si apre la vista o si fa un trasferimento
     */
    public void refreshView() {
        dronePanel.removeAll();
        storagePanel.removeAll();

        // popola il pannello drone
        for (int i = 0; i < droneInventory.getSlotCount(); i++) {
            InventorySlot slot = droneInventory.getSlot(i);
            if (!slot.isEmpty()) {
                String text = slot.getItemName() + " (x" + slot.getQuantity() + ")";
                JButton itemBtn = new JButton(text + " -> Deposita");
                itemBtn.addActionListener(e -> {
                    storage.transferFromInventory(slot.getItemName(), droneInventory);
                    refreshView(); // ridisegna dopo il trasferimento
                });
                dronePanel.add(itemBtn);
            }
        }

        // popola il pannello deposito magazzino
        for (int i = 0; i < storage.getSlotCount(); i++) {
            InventorySlot slot = storage.getSlot(i);
            if (!slot.isEmpty()) {
                String text = slot.getItemName() + " (x" + slot.getQuantity() + ")";
                JButton itemBtn = new JButton("Preleva <- " + text);
                itemBtn.addActionListener(e -> {
                    storage.transferToInventory(slot.getItemName(), droneInventory);
                    refreshView(); // ridisegna dopo il trasferimento
                });
                storagePanel.add(itemBtn);
            }
        }

        dronePanel.revalidate();
        dronePanel.repaint();
        storagePanel.revalidate();
        storagePanel.repaint();
    }
}
