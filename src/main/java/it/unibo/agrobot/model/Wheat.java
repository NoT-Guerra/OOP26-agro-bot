package it.unibo.agrobot.model;

/**
 * rappresenta una pianta di grano
 */
public class Wheat extends AbstractCrop {

    private static final double GROWTH_TIME = 20.0;
    private static final double HYDRATION_TIME = 25.0;

    public Wheat() {
        super("Wheat", GROWTH_TIME, HYDRATION_TIME);
    }
}
