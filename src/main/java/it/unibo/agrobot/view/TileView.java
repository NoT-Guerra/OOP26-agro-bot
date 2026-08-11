package it.unibo.agrobot.view;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import javax.imageio.ImageIO;

import it.unibo.agrobot.model.Crop;
import it.unibo.agrobot.model.Tile;
import it.unibo.agrobot.model.TileType;

/**
 * gestisce la rappresentazione grafica di una singola Tile della griglia
 */
public class TileView {

    private BufferedImage unplowedImage;
    private BufferedImage plowedImage;
    private BufferedImage wateredImage;
    private BufferedImage hangarImage;
    private BufferedImage wellImage;
    
    // mappa per memorizzare le immagini delle colture in base al loro stato
    private final Map<String, BufferedImage> cropImages = new HashMap<>();

    public TileView() {
        this.loadSprites();
    }

    private void loadSprites() {
        // carica le diverse immagini per gli stati del terreno e gli edifici
        this.unplowedImage = loadImage("soil_unplowed.png", true);
        this.plowedImage = loadImage("soil_plowed.png", true);
        this.wateredImage = loadImage("soil_watered.png", true);
        
        this.hangarImage = loadImage("hangar.png", true);
        this.wellImage = loadImage("well.png", true);
        
        // carica le immagini delle colture per ogni stato (seed, growing, mature, dead)
        String[] cropNames = {"wheat", "corn"};
        String[] cropStates = {"seed", "growing", "mature", "dead"};
        
        for (String name : cropNames) {
            for (String state : cropStates) {
                String key = name + "_" + state;
                String fileName = key + ".png";
                BufferedImage img = loadImage(fileName, false);
                if (img != null) {
                    this.cropImages.put(key, img);
                }
            }
        }
    }

    private BufferedImage loadImage(String fileName, boolean shouldCrop) {
        try {
            InputStream stream = getClass().getClassLoader().getResourceAsStream(fileName);
            if (stream != null) {
                BufferedImage rawImage = ImageIO.read(stream);
                return shouldCrop ? autoCrop(rawImage) : rawImage;
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
     * disegna il terreno (zolle o prato) della tile
     * @param g il contesto grafico su cui disegnare
     * @param tile la Tile da disegnare
     * @param x la coordinata x in pixel (angolo in alto a sinistra)
     * @param y la coordinata y in pixel (angolo in alto a sinistra)
     * @param size la dimensione in pixel della casella (larghezza e altezza)
     */
    public void drawGround(Graphics2D g, Tile tile, int x, int y, int size) {
        // disegna l'erba di sfondo su tutte le celle
        g.setColor(new Color(124, 204, 76)); // Un bel verde per l'erba
        g.fillRect(x, y, size, size);

        // disegna l'eventuale coltura presente nella tile
        if (tile.getType() == TileType.SOIL) {
            BufferedImage soilImage = null;
            Color fallbackColor = new Color(139, 69, 19);

            switch (tile.getSoilState()) {
                case UNPLOWED -> { soilImage = this.unplowedImage; fallbackColor = new Color(139, 69, 19); }
                case PLOWED -> { soilImage = this.plowedImage; fallbackColor = new Color(160, 82, 45); }
                case WATERED -> { soilImage = this.wateredImage; fallbackColor = new Color(101, 67, 33); }
            }

            int padding = 1; // margine ridotto al minimo tra le varie zolle di terreno
            int innerSize = size - (padding * 2);
            int innerX = x + padding;
            int innerY = y + padding;

            if (soilImage != null) {
                g.drawImage(soilImage, innerX, innerY, innerSize, innerSize, null);
            } else {
                g.setColor(fallbackColor);
                g.fillRect(innerX, innerY, innerSize, innerSize);
            }
        }
    }

    /**
     * disegna l'oggetto presente nella tile (hangar, pozzo, coltura)
     * @param g il contesto grafico su cui disegnare
     * @param tile la Tile da disegnare
     * @param x la coordinata x in pixel (angolo in alto a sinistra)
     * @param y la coordinata y in pixel (angolo in alto a sinistra)
     * @param size la dimensione in pixel della casella (larghezza e altezza)
     */
    public void drawObject(Graphics2D g, Tile tile, int x, int y, int size) {
        if (null != tile.getType()) switch (tile.getType()) {
            case WELL -> {
                if (this.wellImage != null) {
                    // faccio occupare al pozzo più spazio
                    double scale = 1.8;
                    int newSize = (int)(size * scale);
                    int offsetX = (newSize - size) / 2;
                    int offsetY = newSize - size;
                    g.drawImage(this.wellImage, x - offsetX, y - offsetY, newSize, newSize, null);
                } else {
                    g.setColor(new Color(50, 150, 255));
                    g.fillOval(x - size/4, y - size/4, (int)(size*1.5), (int)(size*1.5));
                }
            }
            case HANGAR -> {
                if (this.hangarImage != null) {
                    // aumento le dimensioni dell'hangar
                    int hangarSize = size * 2;
                    int offsetY = hangarSize - size; // offset per farlo crescere in alto
                    g.drawImage(this.hangarImage, x, y - offsetY, hangarSize, hangarSize, null);
                } else {
                    g.setColor(new Color(150, 150, 150));
                    g.fillRect(x, y - size, size * 2, size * 2);
                }
            }
            case SOIL -> {
                // se ho una coltura, la disegno sopra il terreno
                Optional<Crop> optionalCrop = tile.getCrop();
                if (optionalCrop.isPresent()) {
                    drawCrop(g, optionalCrop.get(), x, y, size);
                }
            }
            default -> {
            }
        }
    }

    private void drawCrop(Graphics2D g, Crop crop, int x, int y, int size) {
        // troviamo l'immagine corrispondente alla coltura e al suo stato
        String cropName = crop.getName().toLowerCase();
        String cropState = crop.getState().name().toLowerCase();
        String imageKey = cropName + "_" + cropState;
        
        BufferedImage cropImg = this.cropImages.get(imageKey);
        
        if (cropImg != null) {
            // se esiste l'immagine della coltura, la disegniamo centrata nella tile
            g.drawImage(cropImg, x, y, size, size, null);
        } else {
            // se manca l'immagine, disegniamo un cerchio colorato come fallback
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
}
