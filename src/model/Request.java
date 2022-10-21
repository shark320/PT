package model;

/**
 * Class represents oasis request
 *
 * @author vpavlov
 */
public class Request implements Comparable<Request>{

    /**
     * Global requests count
     */
    private static int requestCounter = 0;

    /**
     * Request number {p}
     */
    private final int id = ++requestCounter;

    /**
     * Request arrival time {tz}
     */
    //TODO: datatype double
    private final int time;

    /**
     * The time for which the request must be processed {tp}
     */
    private final int timeout;

    /**
     * Oasis id {op}
     */
    private final int oasisId;

    /**
     * Amount of goods {kp}
     */
    private final int goodsCount;

    /**
     * Constructor
     *
     * @param time       - request arrival time {ts}
     * @param timeout    - request timeout {tp}
     * @param oasisId    - oasis id {op}
     * @param goodsCount - amount of goods needed {kp}
     */
    public Request(int time,int oasisId, int goodsCount ,   int timeout) {
        this.time = time;
        this.timeout = timeout;
        this.oasisId = oasisId;
        this.goodsCount = goodsCount;
    }

    /**
     * Request arrival time getter
     *
     * @return request arrival time {ts}
     */
    public int getTime() {
        return time;
    }

    /**
     * Request timeout getter
     *
     * @return request timeout
     */
    public int getTimeout() {
        return timeout;
    }

    /**
     * Oasis id getter
     *
     * @return oasis id
     */
    public int getOasisId() {
        return oasisId;
    }

    /**
     * Goods count getter
     *
     * @return goods count
     */
    public int getGoodsCount() {
        return goodsCount;
    }

    @Override
    public String toString() {
        return "<REQUEST>[id="+this.id
                + ", tz=" + this.time
                + ", tp=" + this.timeout
                + ", op=" + this.oasisId
                + ", kp=" + this.goodsCount
                + "]";
    }

    @Override
    public int compareTo(Request o) {
        return this.time - o.time;
    }
}
