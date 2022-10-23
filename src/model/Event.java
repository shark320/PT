package model;

public class Event implements Comparable<Event> {
    private final double time;

    private final String message;

    public Event(double time, String message) throws IllegalArgumentException {
        if (time<0){
            throw new IllegalArgumentException("Time must be positive");
        }
        if (message == null || message.length() == 0){
            throw new IllegalArgumentException("Message must not be null or empty");
        }
        this.time = time;
        this.message = message;
    }

    public double getTime() {
        return time;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return "[time=" + time + ", message=" + message + "]";
    }

    @Override
    public int compareTo(Event o) {
        return Double.compare(time, o.getTime());
    }
}
