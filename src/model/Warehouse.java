package model;

import helpers.Point;

import java.util.*;

/**
 * Class represents warehouse
 *
 * @author vpavlov
 */
public class Warehouse {

    /**
     * Location of the warehouse {x , y}
     */
    private final Point location;

    /**
     * Camels for the warehouse
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
    //TODO datatype double
    private final long supplyTimeout;

    /**
     * Time is needed for load one good {tn}
     */
    //TODO datatype double
    private final long loadingTime;

    /**
     * Previous supply time (first supply at the beginning of a simulation) {ps}
     */
    private double previousSupply = 0;

    /**
     * Constructor
     *
     * @param x             - X-coordinate
     * @param y             - Y-coordinate
     * @param supplyAmount  - supply amount
     * @param supplyTimeout - supply timeout
     * @param loadingTime   - loading time
     */
    public Warehouse(int x, int y, int supplyAmount, long supplyTimeout, int loadingTime) {
        this.location = new Point(x, y);
        this.supplyAmount = supplyAmount;
        this.supplyTimeout = supplyTimeout;
        this.loadingTime = loadingTime;
        this.goodsAmount = supplyAmount;
    }

    /**
     * Location getter
     *
     * @return warehouse location
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

    public void supply(double currentTime) {
        int supplyCount = (int) Math.floor(currentTime - previousSupply);
        goodsAmount += supplyCount * supplyCount;
    }

    /**
     * Helper method <br>
     * Generates given amount of camels with specified type
     *
     * @param type   type of camels to generate
     * @param amount amount of camels to generate
     * @return set of generated camels
     */
    private Set<Camel> generateCamels(CamelType type, long amount) {
        Set<Camel> result = new HashSet<>();
        for (long i = 0; i < amount; i++) {
            result.add(new Camel(type));
        }
        return result;
    }

    /**
     * Generate camels according to their proportion <br>
     * Adds generated camels to the warehouse
     *
     * @param type  type of camels to return
     * @param types types of camels
     * @return set of camels with specified type
     */
    public Set<Camel> generateCamels(CamelType type, List<CamelType> types) {
        double minProportion = types.stream().min(Comparator.comparingDouble(CamelType::getProportion)).get().getProportion();
        long amount = Math.round(1 / minProportion + 0.5) * 2;
        Set<Camel> result = new HashSet<>();

        for (CamelType camelType : types) {
            if (camelType.getType().equals(type.getType())) {
                result = generateCamels(camelType, Math.round(camelType.getProportion() * amount));
                camels.addAll(result);
            } else {
                camels.addAll(generateCamels(camelType, Math.round(camelType.getProportion() * amount)));
            }
        }

        return result;
    }

    @Override
    public String toString() {
        return "<WAREHOUSE>[x=" + this.location.getX()
                + ", y=" + this.location.getY()
                + ", ks=" + this.supplyAmount
                + ", ts=" + this.supplyTimeout
                + ", tn=" + this.loadingTime
                + ", tc=" + this.goodsAmount
                + ", ps=" + this.previousSupply
                + "]";
    }
}
