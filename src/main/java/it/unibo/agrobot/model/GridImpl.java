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

        // calcolo della colonna di divisione tra le due aree (due terzi a sinistra, un terzo a destra)
        int splitCol = (width * 2) / 3;

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                if (x >= splitCol) {
                    // la parte destra solo tappeto erboso (GRASS)
                    this.tiles[x][y] = new TileImpl(new Position(x, y), TileType.GRASS);
                } else {
                    // i 2/3 a sinistra sono zolle colvibili
                    this.tiles[x][y] = new TileImpl(new Position(x, y), TileType.SOIL);
                }
            }
        }

        // posiziono l'hangar nell'area di destra
        int hangarX = width - 2;
        int hangarY = 1;
        if (hangarX >= splitCol && hangarY < height) {
            this.tiles[hangarX][hangarY] = new TileImpl(new Position(hangarX, hangarY), TileType.HANGAR);
        }

        // posiziono il pozzo d'acqua
        int wellX = width - 2;
        int wellY = 4;
        if (wellX >= splitCol && wellY < height && (wellX != hangarX || wellY != hangarY)) {
            this.tiles[wellX][wellY] = new TileImpl(new Position(wellX, wellY), TileType.WELL);
        }
    }

    @Override
    public synchronized int getWidth() {
        return this.width;
    }

    @Override
    public synchronized int getHeight() {
        return this.height;
    }

    @Override
    public synchronized boolean isInBounds(int x, int y) {
        return x >= 0 && x < this.width && y >= 0 && y < this.height;
    }

    @Override
    public synchronized boolean isInBounds(Position position) {
        if (position == null) {
            return false;
        }
        int x = (int) Math.round(position.getX());
        int y = (int) Math.round(position.getY());
        return isInBounds(x, y);
    }

    @Override
    public synchronized Optional<Tile> getTile(int x, int y) {
        if (!isInBounds(x, y)) {
            return Optional.empty();
        }
        return Optional.of(this.tiles[x][y]);
    }

    @Override
    public synchronized Optional<Tile> getTile(Position position) {
        if (position == null) {
            return Optional.empty();
        }
        int x = (int) Math.round(position.getX());
        int y = (int) Math.round(position.getY());
        return getTile(x, y);
    }

    @Override
    public synchronized boolean setTile(int x, int y, Tile tile) {
        Objects.requireNonNull(tile, "Tile cannot be null");
        if (!isInBounds(x, y)) {
            return false;
        }
        this.tiles[x][y] = tile;
        return true;
    }
}
