package com.vpavlov.simulation.model;

import java.util.Random;

/**
 * Represents the exact camel
 *
 * @author vpavlov
 */
public class Camel implements Comparable<Camel> {

    /**
     * Global camel count
     */
    private static int camelCount = 0;

    /**
     * Camel id
     */
    private final int id = camelCount++;

    /**
     * Camel camelType {camelType}
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

    /**
     * Corresponding warehouse is
     */
    private final int warehouseId;

    /**
     * Return time to corresponding warehouse
     */
    private double returnTime = -1;

    /**
     * Current stamina
     */
    private double stamina;

    /**
     * Constructor
     *
     * @param type - camelType of Camel
     * @param warehouseId corresponding warehouse id
     */
    public Camel(CamelType type, int warehouseId) {
        Random rand = new Random();
        this.type = type;

        //speed random generating (Continuous uniform distribution)
        this.speed = type.getMinSpeed() + rand.nextDouble() * (type.getMaxSpeed() - type.getMinSpeed());

        //distance random generating (Normal distribution)
        double deviation = type.getDistanceDeviation();
        double mean = type.getDistanceMean();
        this.distance = rand.nextGaussian() * deviation + mean;
        this.stamina = distance;
        this.warehouseId = warehouseId;
    }

    /**
     * Return time setter
     *
     * @param returnTime return time
     */
    public void setReturnTime(double returnTime) {
        this.returnTime = returnTime;
    }

    /**
     * Return time getter
     *
     * @return return time
     */
    public double getReturnTime() {
        return returnTime;
    }

    /**
     * Corresponding warehouse id getter
     *
     * @return corresponding warehouse
     */
    public int getWarehouseId() {
        return warehouseId;
    }

    /**
     * Camel type getter
     *
     * @return camel type
     */
    public CamelType getType() {
        return type;
    }

    /**
     * Speed getter
     *
     * @return speed
     */
    public double getSpeed() {
        return speed;
    }

    /**
     * Distance getter
     *
     * @return distance
     */
    public double getDistance() {
        return distance;
    }

    /**
     * Camel id getter
     *
     * @return camel id
     */
    public int getId() {
        return id;
    }

    /**
     * Camel stamina getter
     *
     * @return camel stamina
     */
    public double getStamina() {
        return stamina;
    }

    /**
     * Camel drink method. Renew stamina.
     *
     * @return drink time
     */
    public double drink() {
        stamina = distance;
        return type.getDrinkTime();
    }

    /**
     * Remove specified stamina amount
     *
     * @param toRemove stamina to remove
     */
    public void removeStamina(double toRemove) {
        stamina -= toRemove;
    }

    @Override
    public String toString() {
        return "<CAMEL>[id=" + id + ", camelType=" + this.type.getName() + ", v=" + speed + ", d=" + distance + "]";
    }

    @Override
    public int compareTo(Camel o) {
        return Double.compare(this.returnTime, o.returnTime);
    }
}
