package model;

import java.util.PriorityQueue;

public class EventLogger {

    private final PriorityQueue<Event> events = new PriorityQueue<Event>();

    public EventLogger() {
    }

    public void addEvent(long time, String message) throws IllegalStateException{
        events.add(new Event(time, message));
    }

    public void log(long time){
        Event event;
        for(;;){
            event = events.peek();
            if(event == null){
                break;
            }
            if (event.getTime()<=time){
                events.poll();
                System.out.println(event.getMessage());
            }else{
                break;
            }
        }
    }
}
