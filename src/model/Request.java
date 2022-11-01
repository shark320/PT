package model;

/**
 * Class represents oasis request
 *
 * @author vpavlov
 */
public class Request implements Comparable<Request> {

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
    private final double time;

    /**
     * The time for which the request must be processed {tp}
     */
    private final double timeout;

    /**
     * Postpone request processing time
     */
    private double postponeTime;

    /**
     * Oasis id {op}
     */
    private final int oasisId;

    /**
     * Amount of goods {kp}
     */
    private int goodsCount;

    /**
     * Constructor
     *
     * @param time       - request arrival time {ts}
     * @param timeout    - request timeout {tp}
     * @param oasisId    - oasis id {op}
     * @param goodsCount - amount of goods needed {kp}
     */
    public Request(double time, int oasisId, int goodsCount, double timeout) {
        this.time = time;
        this.timeout = timeout;
        this.oasisId = oasisId;
        this.goodsCount = goodsCount;
        this.postponeTime = time;
    }

    /**
     * Request arrival time getter
     *
     * @return request arrival time {ts}
     */
    public double getTime() {
        return time;
    }

    /**
     * Request timeout getter
     *
     * @return request timeout
     */
    public double getTimeout() {
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

    public void setGoodsCount(int goodsCount) {
        this.goodsCount = goodsCount;
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
        return "<REQUEST>[id=" + this.id
                + ", tz=" + this.time
                + ", tp=" + this.timeout
                + ", op=" + this.oasisId
                + ", kp=" + this.goodsCount
                + "]";
    }

    /**
     * Postpone request
     *
     * @param postponeTime postpone time
     * @return true if request can be postponed, false if there is request timeout
     */
    public boolean postpone(double postponeTime) {
        this.postponeTime = postponeTime;
        return timeout > (time + postponeTime);
    }

    /**
     * Postpone time getter
     *
     * @return postpone time
     */
    public double getPostponeTime() {
        return postponeTime;
    }

    /**
     * Sorting method<br>
     * Sorts requests by their remaining time to process
     *
     * @param r1 first request
     * @param r2 second request
     * @return -1 if the first request has less time to process <br>
     * 1 if the second request has less time to process <br>
     * 0 if the times to process are equal
     */
    public static int sortByTimeout(Request r1, Request r2) {
        return Double.compare((r1.time + r1.timeout) - r1.postponeTime, (r2.time + r2.timeout) - r2.postponeTime);
    }

    @Override
    public int compareTo(Request o) {
        return Double.compare(this.postponeTime, o.postponeTime);
    }
}
