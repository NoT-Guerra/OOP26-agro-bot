package it.unibo.agrobot.controller;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import it.unibo.agrobot.model.Direction;
import it.unibo.agrobot.model.Drone;
import it.unibo.agrobot.model.Grid;
import it.unibo.agrobot.model.Position;

// gestisce input da tastiera per controllo drone
public class InputHandler extends KeyAdapter {

    private final Drone drone;
    private final Grid grid;
    private final GameStateManager stateManager;
    private Runnable openShopAction;

    /**
     * Costruisce il gestore dell'input.
     *
     * @param drone il drone da controllare
     * @param grid la griglia di gioco
     * @param stateManager il gestore dello stato del gioco
     */
    public InputHandler(Drone drone, Grid grid, GameStateManager stateManager) {
        this.drone = drone;
        this.grid = grid;
        this.stateManager = stateManager;
    }

    /**
     * Imposta l'azione da eseguire per aprire il negozio.
     *
     * @param action l'azione
     */
    public void setOpenShopAction(Runnable action) {
        this.openShopAction = action;
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (stateManager != null) {
            if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                if (null != stateManager.getState()) switch (stateManager.getState()) {
                    case PLAYING -> stateManager.setState(GameState.PAUSED);
                    case PAUSED -> stateManager.setState(GameState.PLAYING);
                    case STORAGE_MENU -> stateManager.setState(GameState.PLAYING);
                    default -> {
                    }
                }
            }

            if (stateManager.getState() != GameState.PLAYING) {
                return;
            }
        }

        if (drone == null || drone.isMoving()) return;

        Position pos = drone.getPosition();
        int currentX = (int) Math.round(pos.getX());
        int currentY = (int) Math.round(pos.getY());

        switch (e.getKeyCode()) {
            case KeyEvent.VK_W, KeyEvent.VK_UP -> {
                if (grid.isInBounds(currentX, currentY - 1)) {
                    drone.move(Direction.UP);
                }
            }
            case KeyEvent.VK_S, KeyEvent.VK_DOWN -> {
                if (grid.isInBounds(currentX, currentY + 1)) {
                    drone.move(Direction.DOWN);
                }
            }
            case KeyEvent.VK_A, KeyEvent.VK_LEFT -> {
                if (grid.isInBounds(currentX - 1, currentY)) {
                    drone.move(Direction.LEFT);
                }
            }
            case KeyEvent.VK_D, KeyEvent.VK_RIGHT -> {
                if (grid.isInBounds(currentX + 1, currentY)) {
                    drone.move(Direction.RIGHT);
                }
            }
            case KeyEvent.VK_R -> {
                grid.getTile(drone.getPosition()).ifPresent(tile -> {
                    if (tile.getType() == it.unibo.agrobot.model.TileType.HANGAR) {
                        drone.rechargeAtHangar();
                    } else if (tile.getType() == it.unibo.agrobot.model.TileType.WELL) {
                        drone.rechargeWaterAtWell();
                    }
                });
            }
            case KeyEvent.VK_M -> {
                grid.getTile(drone.getPosition()).ifPresent(tile -> {
                    if (tile.getType() == it.unibo.agrobot.model.TileType.HANGAR) {
                        if (stateManager != null) {
                            stateManager.setState(GameState.STORAGE_MENU);
                        }
                    } else if (tile.getType() == it.unibo.agrobot.model.TileType.MARKET) {
                        if (openShopAction != null) {
                            openShopAction.run();
                        }
                    }
                });
            }
            case KeyEvent.VK_H -> {
                grid.getTile(drone.getPosition()).ifPresent(tile -> {
                    if (tile.getType() == it.unibo.agrobot.model.TileType.SOIL) {
                        if (!drone.isDead()) {
                            // controlla se c'è un raccolto maturo da raccogliere e spazio nell'inventario
                            java.util.Optional<it.unibo.agrobot.model.Crop> cropOpt = tile.getCrop();
                            if (cropOpt.isPresent()) {
                                it.unibo.agrobot.model.Crop crop = cropOpt.get();
                                if (crop.isReadyToHarvest()) {
                                    if (drone.getInventory().canAddItem(crop.getName(), it.unibo.agrobot.model.ItemType.CROP)) {
                                        java.util.Optional<it.unibo.agrobot.model.Crop> harvested = tile.harvest();
                                        if (harvested.isPresent()) {
                                            drone.harvest(); // consuma batteria
                                            drone.getInventory().addItem(harvested.get().getName(), it.unibo.agrobot.model.ItemType.CROP);
                                        }
                                    } else {
                                        java.awt.Toolkit.getDefaultToolkit().beep();
                                    }
                                } else if (crop.isDead()) {
                                    tile.harvest(); // pulisci terreno
                                    drone.harvest();
                                } else {
                                    java.awt.Toolkit.getDefaultToolkit().beep();
                                }
                            } else {
                                java.awt.Toolkit.getDefaultToolkit().beep();
                            }
                        } else {
                            java.awt.Toolkit.getDefaultToolkit().beep();
                        }
                    }
                });
            }
            case KeyEvent.VK_I -> {
                grid.getTile(drone.getPosition()).ifPresent(tile -> {
                    if (tile.getType() == it.unibo.agrobot.model.TileType.SOIL) {
                        if (tile.getSoilState() == it.unibo.agrobot.model.SoilState.PLOWED || 
                            tile.getSoilState() == it.unibo.agrobot.model.SoilState.WATERED) {
                            if (drone.irrigate()) {
                                tile.irrigate();
                            } else {
                                java.awt.Toolkit.getDefaultToolkit().beep();
                            }
                        } else {
                            java.awt.Toolkit.getDefaultToolkit().beep();
                        }
                    }
                });
            }
            case KeyEvent.VK_C -> {
                grid.getTile(drone.getPosition()).ifPresent(tile -> {
                    if (tile.getType() == it.unibo.agrobot.model.TileType.SOIL) {
                        if (tile.getSoilState() == it.unibo.agrobot.model.SoilState.UNPLOWED) {
                            if (!drone.isDead()) {
                                drone.plow();
                                tile.plow();
                            } else {
                                java.awt.Toolkit.getDefaultToolkit().beep();
                            }
                        } else {
                            java.awt.Toolkit.getDefaultToolkit().beep();
                        }
                    }
                });
            }
            case KeyEvent.VK_1, KeyEvent.VK_2, KeyEvent.VK_3 -> {
                int index = e.getKeyCode() - KeyEvent.VK_1;
                if (drone != null && drone.getInventory() != null && index < drone.getInventory().getSlotCount()) {
                    drone.getInventory().setSelectedSlotIndex(index);
                }
            }
            case KeyEvent.VK_P -> {
                grid.getTile(drone.getPosition()).ifPresent(tile -> {
                    if (tile.getType() == it.unibo.agrobot.model.TileType.SOIL) {
                        if (tile.getSoilState() == it.unibo.agrobot.model.SoilState.PLOWED || 
                            tile.getSoilState() == it.unibo.agrobot.model.SoilState.WATERED) {
                            if (!drone.isDead()) {
                                it.unibo.agrobot.model.Inventory inventory = drone.getInventory();
                                int selectedIndex = inventory.getSelectedSlotIndex();
                                it.unibo.agrobot.model.InventorySlot selectedSlot = inventory.getSlot(selectedIndex);
                                
                                if (!selectedSlot.isEmpty() && selectedSlot.getType() == it.unibo.agrobot.model.ItemType.SEED) {
                                    String seedToPlant = selectedSlot.getItemName();
                                    it.unibo.agrobot.model.Crop crop = null;
                                    
                                    if ("Wheat".equals(seedToPlant)) {
                                        crop = new it.unibo.agrobot.model.Wheat();
                                    } else if ("Corn".equals(seedToPlant)) {
                                        crop = new it.unibo.agrobot.model.Corn();
                                    }
                                    
                                    if (crop != null && tile.plant(crop)) {
                                        inventory.removeItem(seedToPlant, it.unibo.agrobot.model.ItemType.SEED);
                                    } else {
                                        java.awt.Toolkit.getDefaultToolkit().beep();
                                    }
                                } else {
                                    java.awt.Toolkit.getDefaultToolkit().beep();
                                }
                            } else {
                                java.awt.Toolkit.getDefaultToolkit().beep();
                            }
                        } else {
                            java.awt.Toolkit.getDefaultToolkit().beep();
                        }
                    }
                });
            }
            case KeyEvent.VK_U -> {
                grid.getTile(drone.getPosition()).ifPresent(tile -> {
                    if (tile.getType() == it.unibo.agrobot.model.TileType.SOIL && tile.hasWeed()) {
                        if (!drone.isDead()) {
                            it.unibo.agrobot.model.Inventory inventory = drone.getInventory();
                            int selectedIndex = inventory.getSelectedSlotIndex();
                            it.unibo.agrobot.model.InventorySlot selectedSlot = inventory.getSlot(selectedIndex);
                            
                            if (!selectedSlot.isEmpty() && selectedSlot.getType() == it.unibo.agrobot.model.ItemType.CONSUMABLE && "Diserbante".equals(selectedSlot.getItemName())) {
                                tile.removeWeed();
                                inventory.removeItem("Diserbante", it.unibo.agrobot.model.ItemType.CONSUMABLE);
                            } else {
                                java.awt.Toolkit.getDefaultToolkit().beep();
                            }
                        } else {
                            java.awt.Toolkit.getDefaultToolkit().beep();
                        }
                    } else {
                        java.awt.Toolkit.getDefaultToolkit().beep();
                    }
                });
            }
            default -> {
            }
        }
    }
}
