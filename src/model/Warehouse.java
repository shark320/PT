package model;

import helpers.Point;

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
    private final int supplyTimeout;

    /**
     * Time is needed for load one good {tn}
     */
    private final int loadingTime;

    /**
     * Previous supply time (first supply at the beginning of a simulation) {ps}
     */
    private final int previousSupply = 0;

    /**
     * Constructor
     *
     * @param x             - X-coordinate
     * @param y             - Y-coordinate
     * @param supplyAmount  - supply amount
     * @param supplyTimeout - supply timeout
     * @param loadingTime   - loading time
     */
    public Warehouse(int x, int y, int supplyAmount, int supplyTimeout, int loadingTime) {
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
