package it.unibo.agrobot.model;

/**
 * rappresenta una pianta di mais
 */
public class Corn extends AbstractCrop {

    private static final double GROWTH_TIME = 35.0;
    private static final double HYDRATION_TIME = 10.0;

    public Corn() {
        super("Corn", GROWTH_TIME, HYDRATION_TIME);
    }
}
