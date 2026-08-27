package it.unibo.agrobot.model;

import java.util.Optional;
import java.util.Random;

/**
 * WeatherManagerImpl e la classe che implementa l'interfaccia WeatherManager
 * e che si occupa di gestire il meteo del gioco
 */

public class WeatherManagerImpl implements WeatherManager {

    private WeatherCondition currentCondition;
    private double timeSinceLastChange;
    private double rainTickTimer;
    private final Random random = new Random();
    private final Grid grid;

    // definisco i parametri per la pioggia
    private static final double MIN_TIME_BETWEEN_CHANGES = 40.0; // secondi minimi tra cambi di tempo
    private static final double CHANGE_PROBABILITY = 0.15; // 15% di probabilità
    private static final double RAIN_DURATION = 15.0; // durata della pioggia
    private static final double RAIN_WATERING_INTERVAL = 0.5; // ogni 0.5 secondi piove su alcune zolle
    private static final int TILES_WATERED_PER_TICK = 5; // zolle annaffiate per ogni tick di pioggia

    /**
     * Costruisce il gestore meteo associato a una griglia.
     *
     * @param grid la griglia di gioco da gestire per gli eventi atmosferici
     */
    public WeatherManagerImpl(Grid grid) {
        this.grid = grid;
        this.currentCondition = WeatherCondition.SUNNY;
        this.timeSinceLastChange = 0;
        this.rainTickTimer = 0;
    }

    /**
     * @return la condizione meteorologica attuale
     */
    @Override
    public WeatherCondition getCurrentCondition() {
        return this.currentCondition;
    }

    /**
     * aggiorna lo stato del meteo nel tempo
     *
     * @param deltaTime il tempo trascorso dall'ultimo frame
     */
    @Override
    public void update(double deltaTime) {
        this.timeSinceLastChange += deltaTime;

        if (this.currentCondition == WeatherCondition.SUNNY) {
            if (this.timeSinceLastChange >= MIN_TIME_BETWEEN_CHANGES) {
                if (random.nextDouble() <= CHANGE_PROBABILITY) {
                    this.currentCondition = WeatherCondition.RAINY;
                    this.timeSinceLastChange = 0;
                } else {
                    // riprova più tardi resettando un po' il timer
                    this.timeSinceLastChange = MIN_TIME_BETWEEN_CHANGES - 5.0;
                }
            }
        } else if (this.currentCondition == WeatherCondition.RAINY) {
            this.rainTickTimer += deltaTime;

            // annaffia gradualmente il terreno
            if (this.rainTickTimer >= RAIN_WATERING_INTERVAL) {
                waterRandomTiles();
                this.rainTickTimer = 0;
            }

            if (this.timeSinceLastChange >= RAIN_DURATION) {
                this.currentCondition = WeatherCondition.SUNNY;
                this.timeSinceLastChange = 0;
                this.rainTickTimer = 0;
            }
        }
    }

    private void waterRandomTiles() {
        if (grid == null) {
            return;
        }

        for (int i = 0; i < TILES_WATERED_PER_TICK; i++) {
            int x = random.nextInt(grid.getWidth());
            int y = random.nextInt(grid.getHeight());

            Optional<Tile> optTile = grid.getTile(x, y);
            if (optTile.isPresent()) {
                Tile tile = optTile.get();
                // irrigate() ha successo solo se il terreno è arato
                if (tile.getType() == TileType.SOIL) {
                    tile.irrigate();
                }
            }
        }
    }

    /**
     * resetta il meteo allo stato iniziale
     */
    @Override
    public void reset() {
        this.currentCondition = WeatherCondition.SUNNY;
        this.timeSinceLastChange = 0;
        this.rainTickTimer = 0;
    }
}
