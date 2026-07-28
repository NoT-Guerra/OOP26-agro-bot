package it.unibo.agrobot.view;

import it.unibo.agrobot.model.Grid;
import it.unibo.agrobot.model.Tile;
import java.awt.Graphics2D;
import java.util.Optional;

/**
 * gestisce la rappresentazione grafica della griglia di gioco
 */
public class GridView {
    
    private final Grid grid;
    private final TileView tileView;
    private final int tileSize;

    /**
     * costruisce un oggetto GridView per visualizzare la griglia di gioco
     * 
     * @param grid griglia da visualizzare
     * @param tileSize dimensione in pixel del lato di ogni singola tile
     */
    public GridView(Grid grid, int tileSize) {
        this.grid = grid;
        this.tileSize = tileSize;
        this.tileView = new TileView();
    }
}
