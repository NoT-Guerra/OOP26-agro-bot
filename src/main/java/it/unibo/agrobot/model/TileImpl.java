package it.unibo.agrobot.model;

import java.util.Objects;
import java.util.Optional;

/**
 * implementazione della zolla di terreno
 */
public class TileImpl implements Tile {

    private final Position position;
    private TileType type;
    private SoilState soilState;
    private Optional<Crop> crop = Optional.empty();
    private boolean weed = false;

    /**
     * costruisce una nuova casella impostando lo stato iniziale del terreno a UNPLOWED
     * 
     * @param position la posizione della casella
     * @param type il tipo di casella
     */
    public TileImpl(Position position, TileType type) {
        this(position, type, SoilState.UNPLOWED);
    }

    /**
     * costruisce una nuova casella con uno stato del terreno specificato
     * 
     * @param position la posizione della casella
     * @param type il tipo di casella
     * @param initialState lo stato iniziale del terreno
     */
    public TileImpl(Position position, TileType type, SoilState initialState) {
        this.position = Objects.requireNonNull(position, "Position cannot be null");
        this.type = Objects.requireNonNull(type, "TileType cannot be null");
        this.soilState = Objects.requireNonNull(initialState, "SoilState cannot be null");
    }

    @Override
    public synchronized Position getPosition() { // synchronized serve per far si che un solo thread alla volta possa accedere a questo metodo
        return this.position;
    }

    @Override
    public synchronized TileType getType() {
        return this.type;
    }

    @Override
    public synchronized SoilState getSoilState() {
        return this.soilState;
    }

    @Override
    public synchronized void setSoilState(SoilState state) {
        this.soilState = Objects.requireNonNull(state, "SoilState cannot be null");
    }

    @Override
    public synchronized boolean plow() {
        if (this.type == TileType.SOIL && this.soilState == SoilState.UNPLOWED) {
            this.soilState = SoilState.PLOWED;
            return true;
        }
        return false;
    }

    @Override
    public synchronized boolean irrigate() {
        if (this.type == TileType.SOIL && (this.soilState == SoilState.PLOWED || this.soilState == SoilState.WATERED)) {
            this.soilState = SoilState.WATERED;
            this.crop.ifPresent(Crop::water);
            return true;
        }
        return false;
    }

    @Override
    public synchronized boolean plant(Crop crop) {
        if (this.type == TileType.SOIL && 
            (this.soilState == SoilState.PLOWED || this.soilState == SoilState.WATERED) && 
            this.crop.isEmpty() && !this.weed) {
            
            this.crop = Optional.of(Objects.requireNonNull(crop, "Crop cannot be null"));
            return true;
        }
        return false;
    }

    @Override
    public synchronized Optional<Crop> harvest() {
        if (this.crop.isPresent() && this.crop.get().isReadyToHarvest()) {
            Optional<Crop> harvested = this.crop;
            this.crop = Optional.empty();
            // in seguito al raccolto il terreno torna nello stato iniziale non arato
            this.soilState = SoilState.UNPLOWED;
            return harvested;
        }
        // se la pianta è morta possiamo permettere di "pulire" la zolla
        if (this.crop.isPresent() && this.crop.get().isDead()) {
            this.crop = Optional.empty();
            this.soilState = SoilState.UNPLOWED;
            return Optional.empty(); // ritorna vuoto perché è morta e non c'è raccolto
        }
        
        return Optional.empty();
    }

    @Override
    public synchronized Optional<Crop> getCrop() {
        return this.crop;
    }

    @Override
    public synchronized void update(double deltaTime) {
        double multiplier = (this.soilState == SoilState.WATERED) ? 2.0 : 1.0;
        this.crop.ifPresent(c -> c.update(deltaTime, multiplier));

        if (this.type == TileType.SOIL && !this.weed && this.crop.isEmpty() && Math.random() < 0.001 * deltaTime) {
            spawnWeed();
        }
    }

    @Override
    public synchronized boolean hasWeed() {
        return this.weed;
    }

    @Override
    public synchronized boolean spawnWeed() {
        if (this.type == TileType.SOIL && !this.weed && this.crop.isEmpty()) {
            this.weed = true;
            return true;
        }
        return false;
    }

    @Override
    public synchronized boolean removeWeed() {
        if (this.weed) {
            this.weed = false;
            return true;
        }
        return false;
    }
}
