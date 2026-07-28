package it.unibo.agrobot.view;

import it.unibo.agrobot.model.Tile;
import it.unibo.agrobot.model.TileType;
import it.unibo.agrobot.model.Crop;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;

/**
 * gestisce la rappresentazione grafica di una singola Tile della griglia
 */
public class TileView {

    private BufferedImage unplowedImage;
    private BufferedImage plowedImage;
    private BufferedImage wateredImage;
    private BufferedImage hangarImage;
    private BufferedImage wellImage;

    public TileView() {
        this.loadSprites();
    }

    private void loadSprites() {
        // carica le diverse immagini per gli stati del terreno e gli edifici
        this.unplowedImage = loadAndCropImage("soil_unplowed.png");
        this.plowedImage = loadAndCropImage("soil_plowed.png");
        this.wateredImage = loadAndCropImage("soil_watered.png");
        
        this.hangarImage = loadAndCropImage("hangar.png");
        this.wellImage = loadAndCropImage("well.png");
    }

    private BufferedImage loadAndCropImage(String fileName) {
        try {
            InputStream stream = getClass().getClassLoader().getResourceAsStream(fileName);
            if (stream != null) {
                BufferedImage rawImage = ImageIO.read(stream);
                return autoCrop(rawImage);
            }
        } catch (IOException e) {
            System.err.println("Impossibile caricare l'immagine: " + fileName);
        }
        return null;
    }

    /**
     * ritaglia automaticamente l'immagine rimuovendo tutti i bordi trasparenti
     */
    private BufferedImage autoCrop(BufferedImage img) {
        int minX = img.getWidth(), minY = img.getHeight(), maxX = 0, maxY = 0;
        boolean found = false;

        for (int y = 0; y < img.getHeight(); y++) {
            for (int x = 0; x < img.getWidth(); x++) {
                int alpha = (img.getRGB(x, y) >> 24) & 0xff;
                if (alpha > 10) {
                    if (x < minX) minX = x;
                    if (x > maxX) maxX = x;
                    if (y < minY) minY = y;
                    if (y > maxY) maxY = y;
                    found = true;
                }
            }
        }

        if (found && minX <= maxX && minY <= maxY) {
            return img.getSubimage(minX, minY, maxX - minX + 1, maxY - minY + 1);
        }
        return img;
    }

    /**
     * disegna una Tile
     * 
     * @param g il contesto grafico su cui disegnare
     * @param tile la Tile da disegnare
     * @param x la coordinata x in pixel (angolo in alto a sinistra)
     * @param y la coordinata y in pixel (angolo in alto a sinistra)
     * @param size la dimensione in pixel della casella (larghezza e altezza)
     */
    public void draw(Graphics2D g, Tile tile, int x, int y, int size) {
        // disegna lo sfondo della tile in base al tipo e allo stato
        drawBackground(g, tile, x, y, size);

        // disegna l'eventuale coltura presente nella tile
        if (tile.getType() == TileType.SOIL) {
            Optional<Crop> optionalCrop = tile.getCrop();
            if (optionalCrop.isPresent()) {
                drawCrop(g, optionalCrop.get(), x, y, size);
            }
        }
    }

    private void drawBackground(Graphics2D g, Tile tile, int x, int y, int size) {
        BufferedImage imageToDraw = null;
        Color fallbackColor = Color.WHITE;

        switch (tile.getType()) {
            case WELL -> {
                imageToDraw = this.wellImage;
                fallbackColor = new Color(50, 150, 255);
            }
            case HANGAR -> {
                imageToDraw = this.hangarImage;
                fallbackColor = new Color(150, 150, 150);
            }
            case SOIL -> {
                switch (tile.getSoilState()) {
                    case UNPLOWED -> {
                        imageToDraw = this.unplowedImage;
                        fallbackColor = new Color(139, 69, 19);
                    }
                    case PLOWED -> {
                        imageToDraw = this.plowedImage;
                        fallbackColor = new Color(160, 82, 45);
                    }
                    case WATERED -> {
                        imageToDraw = this.wateredImage;
                        fallbackColor = new Color(101, 67, 33);
                    }
                }
            }
        }

        if (imageToDraw != null) {
            // se abbiamo un'immagine corrispondente, la disegniamo
            g.drawImage(imageToDraw, x, y, size, size, null);
        } else {
            // se l'img non è disponibile, disegniamo un rettangolo colorato come fallback
            g.setColor(fallbackColor);
            g.fillRect(x, y, size, size);
        }
    }

    private void drawCrop(Graphics2D g, Crop crop, int x, int y, int size) {
        Color cropColor = Color.GREEN;

        switch (crop.getState()) {
            case SEED -> cropColor = new Color(245, 222, 179);
            case GROWING -> cropColor = new Color(144, 238, 144);
            case MATURE -> cropColor = new Color(34, 139, 34);
            case DEAD -> cropColor = new Color(105, 105, 105);
        }

        g.setColor(cropColor);
        int cropSize = size / 2;
        int offsetX = x + (size - cropSize) / 2;
        int offsetY = y + (size - cropSize) / 2;
        g.fillOval(offsetX, offsetY, cropSize, cropSize);
    }
}
