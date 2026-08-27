package it.unibo.agrobot.model;

/**
 * rappresenta una pianta di grano
 */
public class Wheat extends AbstractCrop {

    private static final double GROWTH_TIME = 40.0;
    private static final double HYDRATION_TIME = 50.0;

    /**
     * Costruisce una pianta di grano.
     */
    public Wheat() {
        super("Wheat", GROWTH_TIME, HYDRATION_TIME);
    }
}
