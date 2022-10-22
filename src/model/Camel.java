package model;

import java.util.Random;

/**
 * Represents the exact camel
 *
 * @author vpavlov
 */
public class Camel implements Comparable<Camel>{

    /**
     * Global camel count
     */
    private static int camelCount = 0;

    /**
     * Camel id
     */
    private final int id = camelCount++;

    /**
     * Camel type {type}
     */
    private final CamelType type;

    /**
     * Camel speed {v}
     */
    private final double speed;

    /**
     * Camel distance {d}
     */
    private final double distance;

    private final int warehouseId;

    private double returnTime = -1;

    /**
     * Constructor
     * @param type - type of Camel
     */
    public Camel(CamelType type, int warehouseId) {
        Random rand = new Random();
        this.type = type;

        //speed random generating (Continuous uniform distribution)
        this.speed = type.getMinSpeed() + rand.nextDouble() * (type.getMaxSpeed() - type.getMinSpeed());

        //distance random generating (Normal distribution)
        double deviation = type.getDistanceDeviation();
        double mean = type.getDistanceMean();
        this.distance = rand.nextDouble() * deviation + mean;
        this.warehouseId = warehouseId;
    }

    public void setReturnTime(double returnTime) {
        this.returnTime = returnTime;
    }

    public int getWarehouseId() {
        return warehouseId;
    }

    public double getReturnTime() {
        return returnTime;
    }

    public CamelType getType() {
        return type;
    }

    @Override
    public String toString() {
        return "<CAMEL>[id=" + id +", type="+ this.type.getType() + ", v=" + speed + ", d=" + distance + "]";
    }

    @Override
    public int compareTo(Camel o) {
        return Double.compare(this.returnTime, o.returnTime);
    }
}
