package it.unibo.agrobot.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
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

    private final ArrayList<JButton> focusableButtons = new ArrayList<>();
    private int selectedIndex = 0;

    /**
     * Costruisce la vista del magazzino.
     *
     * @param stateManager gestore dello stato del gioco
     * @param droneInventory inventario del drone
     * @param storage deposito magazzino
     */
    public StorageView(GameStateManager stateManager, Inventory droneInventory, Storage storage) {
        this.stateManager = stateManager;
        this.droneInventory = droneInventory;
        this.storage = storage;

        this.setLayout(new BorderLayout());
        this.setBackground(new Color(50, 50, 50));

        JLabel titleLabel = new JLabel("WAREHOUSE", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 40));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        this.add(titleLabel, BorderLayout.NORTH);

        JPanel centerPanel = new JPanel(new GridLayout(1, 2, 20, 0));
        centerPanel.setOpaque(false);
        centerPanel.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));

        dronePanel = new JPanel(new GridLayout(10, 1, 0, 5));
        dronePanel.setOpaque(false);
        dronePanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.WHITE), "Drone inventory"));
        ((javax.swing.border.TitledBorder) dronePanel.getBorder()).setTitleColor(Color.WHITE);

        storagePanel = new JPanel(new GridLayout(10, 1, 0, 5));
        storagePanel.setOpaque(false);
        storagePanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.WHITE), "Warehouse storage"));
        ((javax.swing.border.TitledBorder) storagePanel.getBorder()).setTitleColor(Color.WHITE);

        centerPanel.add(dronePanel);
        centerPanel.add(storagePanel);

        this.add(centerPanel, BorderLayout.CENTER);

        JButton closeButton = new JButton("CLOSE");
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

        // aggiunto key binding per chiudere con ESC
        this.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), "closeMenu");
        this.getActionMap().put("closeMenu", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (stateManager != null) {
                    stateManager.setState(GameState.PLAYING);
                }
            }
        });

        // aggiunti key binding per navigare con le frecce su/giu e invio
        this.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_UP, 0), "moveUp");
        this.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, 0), "moveDown");
        this.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), "selectItem");

        this.getActionMap().put("moveUp", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (focusableButtons.isEmpty()) {
                    return;
                }
                updateSelection((selectedIndex - 1 + focusableButtons.size()) % focusableButtons.size());
            }
        });

        this.getActionMap().put("moveDown", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (focusableButtons.isEmpty()) {
                    return;
                }
                updateSelection((selectedIndex + 1) % focusableButtons.size());
            }
        });

        this.getActionMap().put("selectItem", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (focusableButtons.isEmpty()) {
                    return;
                }
                focusableButtons.get(selectedIndex).doClick();
            }
        });

        refreshView();
    }

    private void updateSelection(int newIndex) {
        if (!focusableButtons.isEmpty() && selectedIndex >= 0 && selectedIndex < focusableButtons.size()) {
            JButton oldBtn = focusableButtons.get(selectedIndex);
            oldBtn.setBackground(Color.WHITE);
            oldBtn.setForeground(new Color(50, 50, 50));
        }

        selectedIndex = newIndex;

        if (!focusableButtons.isEmpty() && selectedIndex >= 0 && selectedIndex < focusableButtons.size()) {
            JButton newBtn = focusableButtons.get(selectedIndex);
            newBtn.setBackground(new Color(100, 200, 100)); // highlight verde
            newBtn.setForeground(Color.BLACK);
        }
    }

    /**
     * aggiorna la vista leggendo nuovamente gli inventari deve essere chiamato
     * ogni volta che si apre la vista o si fa un trasferimento
     */
    public final void refreshView() {
        dronePanel.removeAll();
        storagePanel.removeAll();
        focusableButtons.clear();

        // popola il pannello drone
        for (int i = 0; i < droneInventory.getSlotCount(); i++) {
            InventorySlot slot = droneInventory.getSlot(i);
            if (!slot.isEmpty()) {
                String displayName = slot.getItemName();
                if (slot.getType() == it.unibo.agrobot.model.ItemType.SEED) {
                    displayName = displayName + " Seed";
                }
                String text = displayName + " (x" + slot.getQuantity() + ")";
                JButton itemBtn = new JButton(text + " -> Deposit");
                styleButton(itemBtn);
                itemBtn.addActionListener(e -> {
                    storage.transferFromInventory(slot.getItemName(), droneInventory);
                    refreshView(); // ridisegna dopo il trasferimento
                });
                dronePanel.add(itemBtn);
                focusableButtons.add(itemBtn);
            }
        }

        // popola il pannello deposito magazzino
        for (int i = 0; i < storage.getSlotCount(); i++) {
            InventorySlot slot = storage.getSlot(i);
            if (!slot.isEmpty()) {
                String displayName = slot.getItemName();
                if (slot.getType() == it.unibo.agrobot.model.ItemType.SEED) {
                    displayName = displayName + " Seed";
                }
                String text = displayName + " (x" + slot.getQuantity() + ")";
                JButton itemBtn = new JButton("Withdraw <- " + text);
                styleButton(itemBtn);
                itemBtn.addActionListener(e -> {
                    storage.transferToInventory(slot.getItemName(), droneInventory);
                    refreshView(); // ridisegna dopo il trasferimento
                });
                storagePanel.add(itemBtn);
                focusableButtons.add(itemBtn);
            }
        }

        if (selectedIndex >= focusableButtons.size()) {
            selectedIndex = Math.max(0, focusableButtons.size() - 1);
        }

        updateSelection(selectedIndex);

        dronePanel.revalidate();
        dronePanel.repaint();
        storagePanel.revalidate();
        storagePanel.repaint();
    }

    private void styleButton(JButton btn) {
        btn.setFont(new Font("Arial", Font.BOLD, 14));
        btn.setBackground(Color.WHITE);
        btn.setForeground(new Color(50, 50, 50));
        btn.setFocusPainted(false);
    }
}
