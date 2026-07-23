package it.unibo.agrobot.model;

/**
 * gestisce la logica comune a tutte le colture per i timer di crescita e ifratazione
 */
public abstract class AbstractCrop implements Crop {
    
    private final String name;
    
    private final double totalGrowthTime;
    private final double maxHydrationTime;

    private CropState state;
    private double currentGrowthTimer;
    private double currentHydrationTimer;

    /**
     * @param name nome della coltura
     * @param totalGrowthTime tempo totale per raggiungere lo stato MATURE
     * @param maxHydrationTime tempo massimo che la pianta può resistere senza acqua prima di morire
     */
    public AbstractCrop(String name, double totalGrowthTime, double maxHydrationTime) {
        this.name = name;
        this.totalGrowthTime = totalGrowthTime;
        this.maxHydrationTime = maxHydrationTime;
        
        this.state = CropState.SEED;
        this.currentGrowthTimer = 0.0;
        this.currentHydrationTimer = maxHydrationTime; 
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public CropState getState() {
        return this.state;
    }

    @Override
    public boolean isReadyToHarvest() {
        return this.state == CropState.MATURE;
    }

    @Override
    public boolean isDead() {
        return this.state == CropState.DEAD;
    }

    @Override
    public void water() {
        if (!isDead()) {
            this.currentHydrationTimer = this.maxHydrationTime;
        }
    }

    @Override
    public void update(double deltaTime) {
        if (isDead() || isReadyToHarvest()) {
            return;
        }

        // timer della sete decresce
        this.currentHydrationTimer -= deltaTime;
        if (this.currentHydrationTimer <= 0) {
            this.state = CropState.DEAD;
            return;
        }

        // timer della crescita avanza
        this.currentGrowthTimer += deltaTime;

        // passaggi di stato basati sul timer di crescita
        if (this.currentGrowthTimer >= this.totalGrowthTime) {
            this.state = CropState.MATURE;
        } else if (this.currentGrowthTimer >= this.totalGrowthTime / 2.0 && this.state == CropState.SEED) {
            this.state = CropState.GROWING;
        }
    }
}
