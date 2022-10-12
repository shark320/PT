package model;

/**
 * Class represents camel
 *
 * @author vpavlov
 */
public class Camel {

    /**
     * Type of camel
     */
    private final String type;

    /**
     * Camel speed {v}
     */
    //@TODO Random generating
    private final double speed;

    /**
     * Camel maximal distance {d}
     */

    //@TODO Random generating
    private final double distance;

    /**
     * Time to drink water {td}
     */
    private final int drinkTime;

    /**
     * Camel maximum possible load {kd}
     */
    private final int maxLoad;

    /**
     * Camel proportion in a herd {pd}
     */
    private final double proportion;

    /**
     * Constructor
     *
     * @param type        -camel type
     * @param maxSpeed    -camel max speed
     * @param minSeed     -camel min seed
     * @param maxDistance -camel max distance
     * @param minDistance -camel min distance
     * @param drinkTime   -camel drink time
     * @param maxLoad     -camel max load
     * @param proportion  - camel proportion in a herd
     */
    public Camel(String type, int maxSpeed, int minSeed, int maxDistance, int minDistance, int drinkTime, int maxLoad, double proportion) {
        this.type = type;
        this.speed = (minSeed + maxSpeed) / 2.0;
        this.distance = (maxDistance + minDistance) / 2.0;
        this.drinkTime = drinkTime;
        this.maxLoad = maxLoad;
        this.proportion = proportion;
    }

    @Override
    public String toString() {
        return "<CAMEL>[type=" + this.type
                + ", v=" + this.speed
                + ", d=" + this.distance
                + ", td=" + this.drinkTime
                + ", kd=" + this.maxLoad
                + ", pd=" + proportion + "]";
    }
}
