package it.unibo.agrobot.model;

import java.util.Objects;
import java.util.Optional;

/**
 * implementazione griglia
 */
public class GridImpl implements Grid {

    private final int width;
    private final int height;
    private final Tile[][] tiles;

    /**
     * crea una griglia di dimensioni width x height popolata di default con zolle SOIL unplowed
     * 
     * @param width larghezza griglia
     * @param height altezza griglia
     */
    public GridImpl(int width, int height) {
        if (width <= 0 || height <= 0) {
            throw new IllegalArgumentException("Width and height must be greater than zero");
        }
        this.width = width;
        this.height = height;
        this.tiles = new Tile[width][height];

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                this.tiles[x][y] = new TileImpl(new Position(x, y), TileType.SOIL);
            }
        }
    }

    @Override
    public int getWidth() {
        return this.width;
    }

    @Override
    public int getHeight() {
        return this.height;
    }

    @Override
    public boolean isInBounds(int x, int y) {
        return x >= 0 && x < this.width && y >= 0 && y < this.height;
    }

    @Override
    public boolean isInBounds(Position position) {
        if (position == null) {
            return false;
        }
        int x = (int) Math.round(position.getX());
        int y = (int) Math.round(position.getY());
        return isInBounds(x, y);
    }

        @Override
    public Optional<Tile> getTile(int x, int y) {
        if (!isInBounds(x, y)) {
            return Optional.empty();
        }
        return Optional.of(this.tiles[x][y]);
    }

    @Override
    public Optional<Tile> getTile(Position position) {
        if (position == null) {
            return Optional.empty();
        }
        int x = (int) Math.round(position.getX());
        int y = (int) Math.round(position.getY());
        return getTile(x, y);
    }

    @Override
    public boolean setTile(int x, int y, Tile tile) {
        Objects.requireNonNull(tile, "Tile cannot be null");
        if (!isInBounds(x, y)) {
            return false;
        }
        this.tiles[x][y] = tile;
        return true;
    }
}
