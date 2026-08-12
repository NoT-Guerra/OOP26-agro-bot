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

    public InputHandler(Drone drone, Grid grid, GameStateManager stateManager) {
        this.drone = drone;
        this.grid = grid;
        this.stateManager = stateManager;
    }

    public void setOpenShopAction(Runnable action) {
        this.openShopAction = action;
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (stateManager != null) {
            if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                if (stateManager.getState() == GameState.PLAYING) {
                    stateManager.setState(GameState.PAUSED);
                } else if (stateManager.getState() == GameState.PAUSED) {
                    stateManager.setState(GameState.PLAYING);
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
            default -> {
            }
        }
    }
}
