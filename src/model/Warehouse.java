package model;

public class Warehouse {

    /**
     * X-coordinate of the warehouse
     */
    private final int x;

    /**
     * Y-coordinate of the warehouse
     */
    private final int y;

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
        this.x = x;
        this.y = y;
        this.supplyAmount = supplyAmount;
        this.supplyTimeout = supplyTimeout;
        this.loadingTime = loadingTime;
        this.goodsAmount = supplyAmount;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
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
        return "<WAREHOUSE>[x=" + this.x
                + ", y=" + this.y
                + ", ks=" + this.supplyAmount
                + ", ts=" + this.supplyTimeout
                + ", tn=" + this.loadingTime
                + ", tc=" + this.goodsAmount
                + ", ps=" + this.previousSupply
                +"]";
    }
}
