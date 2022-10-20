package model;

/**
 * Class represents camel
 *
 * @author vpavlov
 */
public class CamelType {

    /**
     * Type of camel {type}
     */
    private final String type;


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
    private final double maxLoad;

    /**
     * Camel proportion in a herd {pd}
     */
    private final double proportion;

    private final double distanceDeviation;

    private final double distanceMean;

    /**
     * Constructor
     *
     * @param type        -camel type
     * @param maxSpeed    -camel max speed
     * @param minSpeed    -camel min seed
     * @param maxDistance -camel max distance
     * @param minDistance -camel min distance
     * @param drinkTime   -camel drink time
     * @param maxLoad     -camel max load
     * @param proportion  - camel proportion in a herd
     */
    public CamelType(String type, double minSpeed, double maxSpeed, double minDistance, double maxDistance, double drinkTime, double maxLoad, double proportion) {
        this.type = type;
        this.minSpeed = minSpeed;
        this.maxSpeed = maxSpeed;
        this.minDistance = minDistance;
        this.maxDistance = maxDistance;
        this.drinkTime = drinkTime;
        this.maxLoad = maxLoad;
        this.proportion = proportion;
        this.distanceDeviation = (maxDistance - minDistance) / 4;
        this.distanceMean = (maxDistance + minDistance) / 2;
    }

    /**
     * Type title getter
     *
     * @return camel type title
     */
    public String getType() {
        return type;
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
     * Minimal distance getter
     *
     * @return minimal distance
     */
    public double getMinDistance() {
        return minDistance;
    }

    /**
     * Maximal distance getter
     *
     * @return maximal distance
     */
    public double getMaxDistance() {
        return maxDistance;
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

    @Override
    public String toString() {
        return "<CAMEL TYPE>[type=" + this.type
                + ", vmin=" + this.minSpeed
                + ", vmax=" + this.maxSpeed
                + ", dmin=" + this.minDistance
                + ", dmax=" + this.maxDistance
                + ", td=" + this.drinkTime
                + ", kd=" + this.maxLoad
                + ", pd=" + proportion + "]";
    }
}
