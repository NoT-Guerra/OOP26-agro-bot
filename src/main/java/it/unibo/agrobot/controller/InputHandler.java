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

    public InputHandler(Drone drone, Grid grid) {
        this.drone = drone;
        this.grid = grid;
    }

    @Override
    public void keyPressed(KeyEvent e) {
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
            default -> {
            }
        }
    }
}
