package model;

import console.Logger;

import java.util.PriorityQueue;

/**
 * Logs simulation events
 *
 * @author vpavlov
 */
public class EventLogger {

    /**
     * Simulation events priority queue
     */
    private final PriorityQueue<Event> events = new PriorityQueue<>();

    /**
     * File and console logger
     */
    private final Logger logger;

    /**
     * Constructor
     *
     * @param logger file and console logger
     */
    public EventLogger(Logger logger) {
        this.logger = logger;
    }

    /**
     * Add new simulation event to log
     *
     * @param time    simulation time
     * @param message event message
     */
    public void addEvent(double time, String message) {
        events.add(new Event(time, System.currentTimeMillis(), message));
    }

    /**
     * Logs all events with specified time or earlier
     *
     * @param time event time to log
     */
    public void log(double time) {
        Event event;
        for (; ; ) {
            event = events.peek();
            if (event == null) {
                break;
            }
            if (event.getTime() <= time) {
                events.poll();
                //logger.log(event.getMessage(), LogType.EVENT);
                System.out.println(event.getMessage());
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

    /**
     * Logs all events from queue
     */
    public void logAll() {
        Event event;
        while (!events.isEmpty()) {
            event = events.poll();
            //logger.log(event.getMessage(), LogType.EVENT);
            System.out.println(event.getMessage());
        }
    }
}
