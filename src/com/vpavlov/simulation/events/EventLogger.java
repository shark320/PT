package com.vpavlov.simulation.events;

import com.vpavlov.console.LogType;
import com.vpavlov.console.Logger;

import java.util.Arrays;
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
     * Add new camel arrive to the destination event to the log queue
     *
     * @param values event arguments with strong specified types and order <br>
     *               arguments count - 6 <br>
     *               [0] - time (double) <br>
     *               [1] - camel id (int) <br>
     *               [2] - oasis id (int) <br>
     *               [3] - goods amount (int) <br>
     *               [4] - goods prepare time (double) <br>
     *               [5] - request timeout (double) <br>
     * @throws IllegalArgumentException if arguments count, order or types are wrong
     */
    public void addCamelArriveEvent(Object... values) throws IllegalArgumentException {
        try {
            double time = (double) values[0];
            int camelId = (int) values[1];
            int oasisId = (int) values[2];
            int goods = (int) values[3];
            double prepareTime = (double) values[4];
            double timeout = (double) values[5];
            this.addEvent(
                    time,
                    String.format(
                            "Cas: %d, Velbloud: %d, oasa: %d, Vylozeno kosu: %d, Vylozeno v: %d, Casova rezerva: %d",
                            Math.round(time),
                            camelId,
                            oasisId,
                            goods,
                            Math.round(time + prepareTime),
                            Math.round(timeout - (time + prepareTime))
                    )
            );
        } catch (Exception e) {
            throw new IllegalArgumentException("Bad event arguments: " + Arrays.toString(values));
        }
    }

    /**
     * Add new camel ignore event to the log queue
     *
     * @param values event arguments with strong specified types and order <br>
     *               arguments count - 3 <br>
     *               [0] - time (double) <br>
     *               [1] - camel id (int) <br>
     *               [2] - point id (int) <br>
     * @throws IllegalArgumentException if arguments count, order or types are wrong
     */
    public void addCamelIgnoreEvent(Object... values) throws IllegalArgumentException {
        try {
            double time = (double) values[0];
            int camelId = (int) values[1];
            int pointId = (int) values[2];
            this.addEvent(
                    time,
                    String.format(
                            "Cas: %d, Velbloud: %d, oasa: %d, Kuk na velblouda",
                            Math.round(time),
                            camelId,
                            pointId
                    )
            );
        } catch (Exception e) {
            throw new IllegalArgumentException("Bad event arguments: " + Arrays.toString(values));
        }
    }

    /**
     * Add new camel return to warehouse event to the log queue
     *
     * @param values event arguments with strong specified types and order <br>
     *               arguments count - 3 <br>
     *               [0] - time (double) <br>
     *               [1] - camel id (int) <br>
     *               [2] - warehouse id (int) <br>
     * @throws IllegalArgumentException if arguments count, order or types are wrong
     */
    public void addCamelReturnEvent(Object... values) throws IllegalArgumentException {
        try {
            double time = (double) values[0];
            int camelId = (int) values[1];
            int warehouseId = (int) values[2];
            this.addEvent(
                    time,
                    String.format(
                            "Cas: %d, Velbloud: %d, Navrat do skladu: %d",
                            Math.round(time),
                            camelId,
                            warehouseId
                    ));
        } catch (Exception e) {
            throw new IllegalArgumentException("Bad event arguments: " + Arrays.toString(values));
        }
    }

    /**
     * Add new camel drink event to the log queue
     *
     * @param place  specifies the drink place (oasis or warehouse)
     * @param values event arguments with strong specified types and order <br>
     *               arguments count - 5 <br>
     *               [0] - time (double) <br>
     *               [1] - camel id (int) <br>
     *               [2] - point id (int) <br>
     *               [3] - camel type (string) <br>
     *               [4] - drink time (double) <br>
     * @throws IllegalArgumentException if arguments count, order or types are wrong
     */
    public void addCamelDrinkEvent(DrinkPlace place, Object... values) throws IllegalArgumentException {
        try {
            String drinkPlace = "";
            double time = (double) values[0];
            int camelId = (int) values[1];
            int pointId = (int) values[2];
            String camelType = (String) values[3];
            double drinkTime = (double) values[4];
            switch (place) {
                case OASIS -> drinkPlace = "Oasa";
                case WAREHOUSE -> drinkPlace = "Sklad";
            }
            this.addEvent(
                    time,
                    String.format(
                            "Cas: %d, Velbloud: %d, %s: %d, Ziznivy: %s, Pokracovani mozne v: %d",
                            Math.round(time),
                            camelId,
                            drinkPlace,
                            pointId,
                            camelType,
                            Math.round(time + drinkTime)
                    )
            );
        } catch (Exception e) {
            throw new IllegalArgumentException("Bad event arguments: " + Arrays.toString(values));
        }
    }

    /**
     * Add new camel load event to the log queue
     *
     * @param values event arguments with strong specified types and order <br>
     *               arguments count - 5 <br>
     *               [0] - time (double) <br>
     *               [1] - camel id (int) <br>
     *               [2] - warehouse id (int) <br>
     *               [3] - goods amount (int) <br>
     *               [4] - prepare time (double) <br>
     * @throws IllegalArgumentException if arguments count, order or types are wrong
     */
    public void addCamelLoadEvent(Object... values) throws IllegalArgumentException {
        try {
            double time = (double) values[0];
            int camelId = (int) values[1];
            int warehouseId = (int) values[2];
            int goods = (int) values[3];
            double prepareTime = (double) values[4];
            this.addEvent(
                    time,
                    String.format(
                            "Cas: %d, Velbloud: %d, Sklad: %d, Nalozeno kosu: %d, Odchod v: %d",
                            Math.round(time),
                            camelId,
                            warehouseId,
                            goods,
                            Math.round(time + prepareTime)
                    )
            );
        } catch (Exception e) {
            throw new IllegalArgumentException("Bad event arguments: " + Arrays.toString(values));
        }
    }

    /**
     * Add new warehouse supply event to the log queue
     *
     * @param values event arguments with strong specified types and order <br>
     *               arguments count - 3 <br>
     *               [0] - time (double) <br>
     *               [1] - warehouse id (int) <br>
     *               [2] - goods amount (int) <br>
     * @throws IllegalArgumentException if arguments count, order or types are wrong
     */
    public void addWarehouseSupplyEvent(Object... values) throws IllegalArgumentException {
        try {
            double time = (double) values[0];
            int warehouseId = (int) values[1];
            int goods = (int) values[2];
            this.addEvent(
                    time,
                    String.format(
                            "Cas: %d, Sklad: %d, Doslo k doplneni skladu o %d kosu",
                            Math.round(time),
                            warehouseId,
                            goods
                    ));
        } catch (Exception e) {
            throw new IllegalArgumentException("Bad event arguments: " + Arrays.toString(values));
        }
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
                logger.log(event.getMessage(), LogType.EVENT);
            } else {
                break;
            }
        }
    }

    /**
     * Logs all events from queue
     */
    public void logAll() {
        Event event;
        while (!events.isEmpty()) {
            event = events.poll();
            logger.log(event.getMessage(), LogType.EVENT);
        }
    }
}
