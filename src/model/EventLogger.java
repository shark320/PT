package model;

import console.LogType;
import console.Logger;

import java.util.PriorityQueue;

public class EventLogger {

    private final PriorityQueue<Event> events = new PriorityQueue<>();

    private final Logger logger;

    public EventLogger(Logger logger) {
        this.logger = logger;
    }

    public void addEvent(double time, String message) throws IllegalStateException {
        events.add(new Event(time, message));
    }

    public void log(double time) {
        Event event;
        for (; ; ) {
            event = events.peek();
            if (event == null) {
                break;
            }
            if (event.getTime() <= time) {
                events.poll();
                logger.log(event.getMessage(), LogType.EVENT);
                //System.out.println(event.getMessage());
            } else {
                break;
            }
        }
    }

    public void print() {
        Event event;
        while (!events.isEmpty()) {
            event = events.poll();
            System.out.println(event);
        }
    }

    public void logAll() {
        Event event;
        while (!events.isEmpty()) {
            event = events.poll();
            logger.log(event.getMessage(), LogType.EVENT);
            //System.out.println(event.getMessage());
        }
    }
}
