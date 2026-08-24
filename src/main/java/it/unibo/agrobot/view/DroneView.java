package it.unibo.agrobot.view;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

import it.unibo.agrobot.model.Drone;

/**
 * si occupa di disegnare il drone sullo schermo carica la img da file e lo
 * disegna nella posizione corretta convertendo le coordinate del model in pixel
 */
public class DroneView {

    private final Drone drone;
    private BufferedImage spriteImage;

    /**
     * crea la view del drone caricando lo sprite dal file drone.png se il file
     * non viene trovato si usera un placeholder colorato a caso
     *
     * @param drone il drone del model da disegnare
     */
    public DroneView(Drone drone) {
        this.drone = drone;
        this.loadSprite();
    }

    //carica l immagine del drone dalle risorse
    private void loadSprite() {
        try {
            InputStream stream = getClass().getClassLoader().getResourceAsStream("drone.png");
            if (stream != null) {
                this.spriteImage = ImageIO.read(stream);
            }
        } catch (IOException e) {
            //se non riesce a caricare l immagine resta null e si usa il placeholder
            this.spriteImage = null;
        }
    }

    /**
     * disegna il drone sullo schermo converte le coordinate del model in pixel
     * moltiplicandole per la tileSize
     *
     * @param g il contesto grafico su cui disegnare
     * @param tileSize la dimensione in pixel di una singola casella della
     * griglia
     */
    public void draw(Graphics2D g, int tileSize) {
        int pixelX = (int) (this.drone.getPosition().getX() * tileSize);
        int pixelY = (int) (this.drone.getPosition().getY() * tileSize);

        if (this.spriteImage != null) {
            g.drawImage(this.spriteImage, pixelX, pixelY, tileSize, tileSize, null);
        } else {
            //placeholder:un quadrato azzurro
            drawPlaceholder(g, pixelX, pixelY, tileSize);
        }
    }

    //disegna un placeholder quando lo sprite non e disponibile
    private void drawPlaceholder(Graphics2D g, int x, int y, int size) {
        //corpo del drone
        g.setColor(new Color(70, 130, 220));
        g.fillRect(x + 2, y + 2, size - 4, size - 4);
    }
}
