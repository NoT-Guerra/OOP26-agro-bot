package it.unibo.agrobot.view;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.Random;

import it.unibo.agrobot.model.WeatherCondition;
import it.unibo.agrobot.model.WeatherManager;

public class WeatherView {

    private final WeatherManager weatherManager;
    private final Random random = new Random();
    private static final int MAX_PARTICLES = 100;

    // variabili per le gocce di pioggia    
    private final int[] rainX = new int[MAX_PARTICLES];
    private final int[] rainY = new int[MAX_PARTICLES];
    private final int[] rainSpeed = new int[MAX_PARTICLES];

    /**
     * costruttore
     *
     * @param weatherManager il weather manager
     */
    public WeatherView(WeatherManager weatherManager) {
        this.weatherManager = weatherManager;

        // inizializza le particelle
        for (int i = 0; i < MAX_PARTICLES; i++) {
            rainSpeed[i] = random.nextInt(15) + 10;
            // le coordinate verranno settate in fase di render se fuori schermo
        }
    }

    public void draw(Graphics2D g2d, int width, int height) {
        if (weatherManager == null || weatherManager.getCurrentCondition() != WeatherCondition.RAINY) {
            return;
        }

        // overlay per scurire leggermente l'ambiente
        g2d.setColor(new Color(0, 0, 40, 50));
        g2d.fillRect(0, 0, width, height);

        // disegna le particelle di pioggia
        g2d.setColor(new Color(150, 200, 255, 180));
        for (int i = 0; i < MAX_PARTICLES; i++) {
            // inizializza se è a (0,0) all'inizio, o muovi
            if (rainY[i] == 0 && rainX[i] == 0) {
                rainX[i] = random.nextInt(width);
                rainY[i] = random.nextInt(height);
            }

            rainY[i] += rainSpeed[i];
            rainX[i] -= rainSpeed[i] / 3; // cade in diagonale

            // se la goccia esce dallo schermo, resetta in alto
            if (rainY[i] > height || rainX[i] < 0) {
                rainY[i] = -random.nextInt(100);
                rainX[i] = random.nextInt(width + 200); // offset a destra per la diagonale
            }

            // disegna la goccia (una breve linea diagonale)
            g2d.drawLine(rainX[i], rainY[i], rainX[i] - 3, rainY[i] + 9);
        }
    }
}
