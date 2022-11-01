package model;

import console.LogType;
import console.Logger;

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

    //TODO: comments
    public void addCamelArriveEvent(Object... values) throws IllegalArgumentException{
        try{
            double time = (double)values[0];
            int camelId = (int)values[1];
            int oasisId = (int)values[2];
            int goods = (int)values[3];
            double prepareTime = (double)values[4];
            double timeout = (double)values[5];
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
        }catch(Exception e){
            throw new IllegalArgumentException("Bad event arguments: "+ Arrays.toString(values));
        }
    }

    //TODO: comments
    public void addCamelIgnoreEvent(Object... values) throws IllegalArgumentException{
        try{
            double time = (double)values[0];
            int camelId = (int)values[1];
            int pointId = (int)values[2];
            this.addEvent(
                    time,
                    String.format(
                            "Cas: %d, Velbloud: %d, oasa: %d, Kuk na velblouda",
                            Math.round(time),
                            camelId,
                            pointId
                    )
            );
        }catch(Exception e){
            throw new IllegalArgumentException("Bad event arguments: "+ Arrays.toString(values));
        }
    }

    public void addCamelReturnEvent(Object... values) throws IllegalArgumentException{
        try{
            double time = (double)values[0];
            int camelId = (int)values[1];
            int warehouseId = (int)values[2];
            this.addEvent(
                    time,
                    String.format(
                            "Cas: %d, Velbloud: %d, Navrat do skladu: %d",
                            Math.round(time),
                            camelId,
                            warehouseId
                    ));
        }catch(Exception e){
            throw new IllegalArgumentException("Bad event arguments: "+ Arrays.toString(values));
        }
    }

    //TODO: comments
    public void addCamelDrinkEvent(DrinkType type,Object... values) throws IllegalArgumentException{
        try{
            String drinkPlace = "";
            double time = (double)values[0];
            int camelId = (int)values[1];
            int pointId = (int)values[2];
            String camelType = (String)values[3];
            double drinkTime = (double)values[4];
            switch(type){
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
        }catch(Exception e){
            throw new IllegalArgumentException("Bad event arguments: "+ Arrays.toString(values));
        }
    }

    //TODO: comments
    public void addCamelLoadEvent(Object... values) throws IllegalArgumentException{
        try{
            double time = (double)values[0];
            int camelId = (int)values[1];
            int warehouseId = (int)values[2];
            int goods = (int)values[3];
            double prepareTime = (double)values[4];
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
        }catch(Exception e){
            throw new IllegalArgumentException("Bad event arguments: "+ Arrays.toString(values));
        }
    }

    //TODO: comments
    public void addWarehouseSupplyEvent(Object... values) throws IllegalArgumentException{
        try{
            double time = (double)values[0];
            int warehouseId = (int)values[1];
            int goods = (int)values[2];
            this.addEvent(
                    time,
                    String.format(
                            "Cas: %d, Sklad: %d, Doslo k doplneni skladu o %d kosu",
                            Math.round(time),
                            warehouseId,
                            goods
                    ));
        }catch(Exception e){
            throw new IllegalArgumentException("Bad event arguments: "+ Arrays.toString(values));
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
