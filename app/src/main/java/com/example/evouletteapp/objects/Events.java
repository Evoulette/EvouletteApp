package com.example.evouletteapp.objects;

import java.util.ArrayList;

public class Events {
    public ArrayList<Event> events = new ArrayList<>();

    public Events (){
    }
    public Events (ArrayList<Event> events){
        this.events = events;
    }

    public void addEvent(Event event){
        events.add(event);
    }

    public boolean deleteEvent(Event event){
        boolean succes = false;
        if(events.contains(event)){
            events.remove(event);
            succes = true;
        }
        return succes;
    }

    public ArrayList<Event> getEvents() {
        return events;
    }

    public void setEvents(ArrayList<Event> events) {
        this.events = events;
    }
}
