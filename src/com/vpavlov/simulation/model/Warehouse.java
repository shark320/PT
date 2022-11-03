package com.vpavlov.simulation.model;

import com.vpavlov.map.Point;

import java.util.*;

/**
 * Class represents path
 *
 * @author vpavlov
 */
public class Warehouse implements Comparable<Warehouse> {

    /**
     * Magic number for camels generation
     */
    private static final int MAGIC_NUMBER = 2;

    /**
     * This warehouse id
     */
    private final int id;

    /**
     * Location of the path {x , y}
     */
    private final Point location;

    /**
     * Camels for the path
     */
    private final Set<Camel> camels = new HashSet<>();

    /**
     * Current amount of goods {tc}
     */
    private int goodsAmount;

    /**
     * Amount of goods in one supply {ks}
     */
    private final int supplyAmount;

    /**
     * Timeout of supply {ts}
     */
    private final double supplyTimeout;

    /**
     * Time is needed for load one good {tn}
     */
    private final double loadingTime;

    /**
     * Previous supply time (first supply at the beginning of a simulation) {ps}
     */
    private double nextSupply;

    /**
     * Warehouse priority
     */
    private double priority;

    /**
     * Constructor
     *
     * @param x             - X-coordinate
     * @param y             - Y-coordinate
     * @param supplyAmount  - supply amount
     * @param supplyTimeout - supply timeout
     * @param loadingTime   - loading time
     */
    public Warehouse(int id, double x, double y, int supplyAmount, double supplyTimeout, double loadingTime) {
        this.id = id;
        this.location = new Point(x, y, id);
        this.supplyAmount = supplyAmount;
        this.supplyTimeout = supplyTimeout;
        this.loadingTime = loadingTime;
        this.goodsAmount = supplyAmount;
        this.nextSupply = supplyTimeout;
        setPriority();
    }

    /**
     * Sets warehouse priority according to loading time and goods amount
     */
    private void setPriority() {
        priority = goodsAmount / loadingTime;
    }

    /**
     * Location getter
     *
     * @return path location
     */
    public Point getLocation() {
        return location;
    }

    /**
     * Goods amount getter
     *
     * @return current goods amount
     */
    public int getGoodsAmount() {
        return goodsAmount;
    }

    /**
     * Loading time getter
     *
     * @return loading time
     */
    public double getLoadingTime() {
        return loadingTime;
    }

    /**
     * Id getter
     *
     * @return warehouse id
     */
    public int getId() {
        return id;
    }

    /**
     * Camels count getter
     *
     * @return all types of camels count
     */
    public int getCamelsCount() {
        return camels.size();
    }

    /**
     * Camels count getter by type
     *
     * @param type type to count
     * @return count of camels with specified type
     */
    public long getCamelsCount(CamelType type) {
        return camels.stream().filter(c -> c.getType().equals(type)).count();
    }

    /**
     * Next supply time getter
     *
     * @return next supply time
     */
    public double getNextSupply() {
        return nextSupply;
    }

    /**
     * Process warehouse supply
     *
     * @return amount of goods that have been supplied
     */
    public int supply() {
        nextSupply = nextSupply + supplyTimeout;
        goodsAmount += this.supplyAmount;
        setPriority();
        return this.supplyAmount;
    }

    /**
     * Helper method <br>
     * Generates given amount of camels with specified camelType
     *
     * @param type   camelType of camels to generate
     * @param amount amount of camels to generate
     * @return set of generated camels
     */
    private Set<Camel> generateCamels(CamelType type, long amount) {
        Set<Camel> result = new HashSet<>();
        for (long i = 0; i < amount; i++) {
            result.add(new Camel(type, this.id));
        }
        setPriority();
        return result;
    }

    /**
     * Generate camels according to their proportion <br>
     * Adds generated camels to the path
     *
     * @param type  camelType of camels to return
     * @param types types of camels
     * @return set of camels with specified camelType
     */
    public List<Camel> generateCamels(CamelType type, PriorityQueue<CamelType> types) {
        double minProportion = types.stream().min(Comparator.comparingDouble(CamelType::getProportion)).get().getProportion();
        long amount = Math.round(1 / minProportion + 0.5) * MAGIC_NUMBER;
        Set<Camel> result = new HashSet<>();

        for (CamelType camelType : types) {
            if (camelType.getName().equals(type.getName())) {
                result = generateCamels(camelType, Math.round(camelType.getProportion() * amount));
                camels.addAll(result);
            } else {
                camels.addAll(generateCamels(camelType, Math.round(camelType.getProportion() * amount)));
            }
        }

        return result.stream().toList();
    }

    /**
     * Specified type camels getter
     *
     * @param type camels type to het
     * @return camels with specified type
     */
    public List<Camel> getCamelsByType(CamelType type) {
        return camels.stream().filter(c -> c.getType().equals(type)).toList();
    }

    /**
     * Remove specified camel from the warehouse
     *
     * @param camel camel to remove
     */
    public void removeCamel(Camel camel) {
        camels.remove(camel);
    }

    /**
     * Return specified camel to the warehouse
     *
     * @param camel camel to return
     */
    public void returnCamel(Camel camel) {
        camels.add(camel);
    }

    /**
     * Removes specified amount of goods from the warehouse
     *
     * @param goodsAmount goods amount to remove
     */
    public void removeGoods(int goodsAmount) {
        this.goodsAmount -= goodsAmount;
        setPriority();
    }


    @Override
    public String toString() {
        return "<WAREHOUSE>[x=" + this.location.x()
                + ", y=" + this.location.y()
                + ", ks=" + this.supplyAmount
                + ", ts=" + this.supplyTimeout
                + ", tn=" + this.loadingTime
                + ", tc=" + this.goodsAmount
                + ", ps=" + this.nextSupply
                + "]";
    }

    @Override
    public int compareTo(Warehouse o) {
        return Double.compare(o.priority, this.priority);
    }

}
