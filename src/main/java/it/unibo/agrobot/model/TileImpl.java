package it.unibo.agrobot.model;

import java.util.Objects;

/**
 * implementazione della zolla di terreno
 */
public class TileImpl implements Tile {

    private final Position position;
    private final TileType type;
    private SoilState soilState;

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
    public Position getPosition() {
        return this.position;
    }

    @Override
    public TileType getType() {
        return this.type;
    }

    @Override
    public SoilState getSoilState() {
        return this.soilState;
    }

    @Override
    public void setSoilState(SoilState state) {
        this.soilState = Objects.requireNonNull(state, "SoilState cannot be null");
    }

    @Override
    public boolean plow() {
        if (this.type == TileType.SOIL && this.soilState == SoilState.UNPLOWED) {
            this.soilState = SoilState.PLOWED;
            return true;
        }
        return false;
    }

    @Override
    public boolean irrigate() {
        if (this.type == TileType.SOIL && (this.soilState == SoilState.PLOWED || this.soilState == SoilState.WATERED)) {
            this.soilState = SoilState.WATERED;
            return true;
        }
        return false;
    }
}
