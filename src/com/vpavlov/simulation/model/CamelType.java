package com.vpavlov.simulation.model;

/**
 * Class represents camel
 *
 * @author vpavlov
 */
public class CamelType implements Comparable<CamelType> {

    /**
     * Type of camel {camelType}
     */
    private final String name;


    /**
     * Minimal camel speed {vmin}
     */
    private final double minSpeed;

    /**
     * Maximal camel speed {vmax}
     */
    private final double maxSpeed;

    /**
     * Minimal distance {dmin}
     */
    private final double minDistance;

    /**
     * Maximal distance {dmax}
     */
    private final double maxDistance;

    /**
     * Time to drink water {td}
     */
    private final double drinkTime;

    /**
     * Camel maximum possible load {kd}
     */
    private final int maxLoad;

    /**
     * Camel proportion in a herd {pd}
     */
    private final double proportion;

    private final double distanceDeviation;

    private final double distanceMean;

    private final double effectiveDistance;

    private final double efficiency;

    /**
     * Constructor
     *
     * @param name        -camel camelType
     * @param maxSpeed    -camel max speed
     * @param minSpeed    -camel min seed
     * @param maxDistance -camel max distance
     * @param minDistance -camel min distance
     * @param drinkTime   -camel drink time
     * @param maxLoad     -camel max load
     * @param proportion  - camel proportion in a herd
     */
    public CamelType(String name, double minSpeed, double maxSpeed, double minDistance, double maxDistance, double drinkTime, int maxLoad, double proportion) {
        this.name = name;
        this.minSpeed = minSpeed;
        this.maxSpeed = maxSpeed;
        this.minDistance = minDistance;
        this.maxDistance = maxDistance;
        this.drinkTime = drinkTime;
        this.maxLoad = maxLoad;
        this.proportion = proportion;
        this.distanceDeviation = (maxDistance - minDistance) / 4;
        this.distanceMean = (maxDistance + minDistance) / 2;
        this.effectiveDistance = distanceMean + 2 * distanceDeviation;
        this.efficiency=setEfficiency();
    }

    private double setEfficiency(){
        return ((maxSpeed+minSpeed)/2)*effectiveDistance*maxLoad;
    }

    /**
     * Type title getter
     *
     * @return camel camelType title
     */
    public String getName() {
        return name;
    }

    /**
     * Minimal speed getter
     *
     * @return minimal speed
     */
    public double getMinSpeed() {
        return minSpeed;
    }

    /**
     * Maximal speed getter
     *
     * @return maximal speed
     */
    public double getMaxSpeed() {
        return maxSpeed;
    }

    /**
     * Distance generation deviation getter
     *
     * @return distance deviation
     */
    public double getDistanceDeviation() {
        return distanceDeviation;
    }

    /**
     * Distance generation mean
     *
     * @return distance mean
     */
    public double getDistanceMean() {
        return distanceMean;
    }

    /**
     * This camel camelType proportion getter
     *
     * @return proportion
     */
    public double getProportion() {
        return proportion;
    }

    /**
     * Gets effective distance for this camel camelType
     *
     * @return distanceMean + 2 * distanceDeviation
     */
    public double getEffectiveDistance() {
        return effectiveDistance;
    }

    public int getMaxLoad() {
        return maxLoad;
    }

    public double getDrinkTime() {
        return drinkTime;
    }

    @Override
    public String toString() {
        return "<CAMEL TYPE>[camelType=" + this.name
                + ", vmin=" + this.minSpeed
                + ", vmax=" + this.maxSpeed
                + ", dmin=" + this.minDistance
                + ", dmax=" + this.maxDistance
                + ", td=" + this.drinkTime
                + ", kd=" + this.maxLoad
                + ", pd=" + proportion + "]";
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof CamelType) {
            return this.name.equals(((CamelType)obj).name);
        }
        return false;
    }

    @Override
    public int compareTo(CamelType o) {
        return Double.compare(o.efficiency, this.efficiency);
    }
}
