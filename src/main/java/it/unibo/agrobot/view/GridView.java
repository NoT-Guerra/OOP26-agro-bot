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

    /**
     * metodo per disegnare l'intera griglia
     * itera su tutte le caselle della griglia e usa TileView per disegnarle
     * 
     * @param g contesto grafico su cui disegnare la griglia
     */
    public void draw(Graphics2D g) {
        for (int y = 0; y < grid.getHeight(); y++) {
            for (int x = 0; x < grid.getWidth(); x++) {
                Optional<Tile> tileOpt = grid.getTile(x, y);
                if (tileOpt.isPresent()) {
                    // calcolo delle coordinate in pixel
                    int pixelX = x * tileSize;
                    int pixelY = y * tileSize;
                    
                    // disegna la tile usando TileView
                    tileView.draw(g, tileOpt.get(), pixelX, pixelY, tileSize);
                }
            }
        }
    }

    /**
     * ritorna la dimensione di una tile in pixel
     * 
     * @return tileSize
     */
    public int getTileSize() {
        return this.tileSize;
    }
}
