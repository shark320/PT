package model;

public class Event implements Comparable<Event> {
    private final long time;

    private final String message;

    public Event(long time, String message) throws IllegalArgumentException {
        if (time<0){
            throw new IllegalArgumentException("Time must be positive");
        }
        if (message == null || message.length() == 0){
            throw new IllegalArgumentException("Message must not be null or empty");
        }
        this.time = time;
        this.message = message;
    }

    public long getTime() {
        return time;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public int compareTo(Event o) {
        return 0;
    }
}
