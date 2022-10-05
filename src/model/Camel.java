package model;

public class Camel {

    private final String type;

    /**
     * Camel speed
     */
    //@TODO Random generating
    private final double speed;

    /**
     * Camel maximal distance
     *
     */

    //@TODO Random generating
    private final double distance;

    /**
     * Time to drink water
     */
    private final int drinkTime;

    /**
     * Camel maximum possible load
     */
    private final int maxLoad;

    /**
     * What a hell is this???
     */
    //@TODO What a hell is this???
    private final double zastoupeni;

    public Camel(String type, int maxSpeed, int minSeed, int maxDistance, int minDistance, int drinkTime, int maxLoad, double zastoupeni) {
        this.type = type;
        this.speed = (minSeed+maxSpeed)/2.0;
        this.distance = (maxDistance+minDistance)/2.0;
        this.drinkTime = drinkTime;
        this.maxLoad = maxLoad;
        this.zastoupeni = zastoupeni;
    }

    @Override
    public String toString() {
        return "<CAMEL>[type="+this.type
                +", v="+this.speed
                +", d="+this.distance
                +", td="+this.drinkTime
                +", kd="+this.maxLoad
                +", pd="+zastoupeni+"]";
    }
}
