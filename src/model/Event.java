package model;

/**
 * Simulation event class
 *
 * @author vpavlov
 */
public class Event implements Comparable<Event> {

    /**
     * Simulation event time
     */
    private final double time;

    /**
     * Event timestamp (real world)
     */
    private final long timestamp;

    /**
     * Event message
     */
    private final String message;

    /**
     * Constructor
     *
     * @param time      simulation event time
     * @param timestamp real world timestamp
     * @param message   event message
     * @throws IllegalArgumentException if time < 0 or message is empty or null
     */
    public Event(double time, long timestamp, String message) throws IllegalArgumentException {
        if (time < 0) {
            throw new IllegalArgumentException("Time must be positive");
        }
        if (message == null || message.length() == 0) {
            throw new IllegalArgumentException("Message must not be null or empty");
        }
        this.time = time;
        this.message = message;
        this.timestamp = timestamp;
    }

    /**
     * Simulation event time getter
     *
     * @return simulation time
     */
    public double getTime() {
        return time;
    }

    /**
     * Message getter
     *
     * @return message
     */
    public String getMessage() {
        return message;
    }

    /**
     * Compare events timestamps
     *
     * @param event event to compare
     * @return 1 if this event.timestamp less than event.timestamp
     * -1 if this.timestamp greater than event.timestamp
     * 0 if timestamps are equal
     */
    private int cmpTimestamp(Event event) {
        return Long.compare(this.timestamp, event.timestamp);
    }

    @Override
    public String toString() {
        return "[time=" + time + ", message=" + message + "]";
    }


    @Override
    public int compareTo(Event o) {
        int cmpTime = Double.compare(time, o.getTime());
        return cmpTime == 0 ? cmpTimestamp(o) : cmpTime;
    }
}
