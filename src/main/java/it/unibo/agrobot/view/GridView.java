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
    private int tileSize;

    /**
     * costruisce un oggetto GridView per visualizzare la griglia di gioco
     * 
     * @param grid griglia da visualizzare
     * @param tileSize dimensione in pixel del lato di ogni singola tile
     */
    public GridView(Grid grid, int tileSize) {
        this.grid = grid;
        this.tileSize = tileSize;
        this.tileView = new TileView(grid);
    }

    /**
     * Imposta la dimensione della tile in pixel.
     *
     * @param newSize la nuova dimensione
     */
    public void setTileSize(int newSize) {
        this.tileSize = newSize;
    }
    
    /**
     * Restituisce il numero di colonne della griglia.
     *
     * @return il numero di colonne
     */
    public int getCols() {
        return this.grid.getWidth();
    }
    
    /**
     * Restituisce il numero di righe della griglia.
     *
     * @return il numero di righe
     */
    public int getRows() {
        return this.grid.getHeight();
    }

    /**
     * metodo per disegnare l'intera griglia
     * itera su tutte le caselle della griglia e usa TileView per disegnarle
     * 
     * @param g contesto grafico su cui disegnare la griglia
     */
    public void draw(Graphics2D g) {
        // disegno staccionata (bordo esterno)
        for (int x = -1; x <= grid.getWidth(); x++) {
            // Riga superiore
            String typeTop = "horizontal";
            if (x == -1) typeTop = "corner_tl";
            else if (x == grid.getWidth()) typeTop = "corner_tr";
            tileView.drawFence(g, x * tileSize, -tileSize, tileSize, typeTop);
            
            // Riga inferiore
            String typeBottom = "horizontal";
            if (x == -1) typeBottom = "corner_bl";
            else if (x == grid.getWidth()) typeBottom = "corner_br";
            tileView.drawFence(g, x * tileSize, grid.getHeight() * tileSize, tileSize, typeBottom);
        }
        for (int y = 0; y < grid.getHeight(); y++) {
            // Colonna sinistra
            tileView.drawFence(g, -tileSize, y * tileSize, tileSize, "vertical");
            // Colonna destra
            tileView.drawFence(g, grid.getWidth() * tileSize, y * tileSize, tileSize, "vertical");
        }

        // disegno il terreno (sfondo) per ogni tile
        for (int y = 0; y < grid.getHeight(); y++) {
            for (int x = 0; x < grid.getWidth(); x++) {
                Optional<Tile> tileOpt = grid.getTile(x, y);
                if (tileOpt.isPresent()) {
                    int pixelX = x * tileSize;
                    int pixelY = y * tileSize;
                    tileView.drawGround(g, tileOpt.get(), pixelX, pixelY, tileSize);
                }
            }
        }

        // disegnogli oggetti
        for (int y = 0; y < grid.getHeight(); y++) {
            for (int x = 0; x < grid.getWidth(); x++) {
                Optional<Tile> tileOpt = grid.getTile(x, y);
                if (tileOpt.isPresent()) {
                    int pixelX = x * tileSize;
                    int pixelY = y * tileSize;
                    tileView.drawObject(g, tileOpt.get(), pixelX, pixelY, tileSize);
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
