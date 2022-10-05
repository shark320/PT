package model;

public class Request {

    /**
     * Time when request came
     */
    private final int time;

    /**
     * The time for which the request must be processed
     */
    private final int timeout;

    /**
     * Oasis id
     */
    private final int oasisId;

    /**
     * Amount of goods
     */
    private final int goodsCount;

    public Request(int time, int timeout, int oasisId, int goodsCount) {
        this.time = time;
        this.timeout = timeout;
        this.oasisId = oasisId;
        this.goodsCount = goodsCount;
    }

    public int getTime() {
        return time;
    }

    public int getTimeout() {
        return timeout;
    }

    public int getOasisId() {
        return oasisId;
    }

    public int getGoodsCount() {
        return goodsCount;
    }

    @Override
    public String toString() {
        return "<REQUEST>[tz="+this.time
                +", tp="+this.timeout
                +", op="+this.oasisId
                +", kp="+this.goodsCount
                +"]";
    }
}
