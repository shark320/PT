package model;

import helpers.Point;

public class Warehouse {

    private final Point location;

    /**
     * Current amount of goods
     */
    private int goodsAmount;

    /**
     * Amount of goods in one supply
     */
    private final int supplyAmount;

    /**
     * Timeout of supply
     */
    private final int supplyTimeout;

    /**
     * Time is needed for load one good
     */
    private final int loadingTime;

    /**
     * Previous supply time (first supply at the beginning of a simulation)
     */
    private final int previousSupply = 0;

    public Warehouse(int x, int y, int supplyAmount, int supplyTimeout, int loadingTime) {
        this.location = new Point(x, y);
        this.supplyAmount = supplyAmount;
        this.supplyTimeout = supplyTimeout;
        this.loadingTime = loadingTime;
        this.goodsAmount = supplyAmount;
    }

    public Point getLocation() {
        return location;
    }

    public int getGoodsAmount() {
        return goodsAmount;
    }

    public void setGoodsAmount(int goodsAmount) {
        this.goodsAmount = goodsAmount;
    }

    public int getSupplyAmount() {
        return supplyAmount;
    }

    public int getSupplyTimeout() {
        return supplyTimeout;
    }

    public int getLoadingTime() {
        return loadingTime;
    }

    public int getPreviousSupply() {
        return previousSupply;
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
                +"]";
    }
}
