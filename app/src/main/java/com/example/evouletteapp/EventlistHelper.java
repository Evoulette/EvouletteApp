package com.example.evouletteapp;

import android.app.Activity;
import android.content.Context;
import android.content.res.AssetManager;
import android.os.Build;
import android.os.Environment;
import android.support.annotation.RequiresApi;
import android.util.Log;

import com.example.evouletteapp.objects.Event;
import com.example.evouletteapp.objects.Events;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;

import static java.security.AccessController.getContext;

public class EventlistHelper {

    public static final String TAG = "EventlistHelper";
    static ArrayList<Event> elist = new ArrayList<>();
    static ArrayList<Event> loclist = new ArrayList<>();
    static ArrayList<Event> dateloclist = new ArrayList<>();
    static ArrayList<Event> eventlist = new ArrayList<>();
    static ArrayList<Event> dateloctimelist = new ArrayList<>();
    static ArrayList<Event> dateloctimebudlist = new ArrayList<>();
    //static String path = "File:///android_assets";
    static ObjectMapper objectMapper = new ObjectMapper();
    static File dir = new File("sdcard");
    static File evdir = new File(dir,"Events.json");

    public static boolean isEventlist() {
        boolean exists = false;
        if (evdir.exists()) {
            exists = true;
        }
        return exists;

    }

    public static String getEvents(String location, String date, String time, double price) throws IOException {
        Log.i(TAG, "getEventserreicht");
        elist = loadEventsDummy();
        loclist = filterEvent("Location", location, elist);
        dateloclist = filterEvent("Date", date, loclist);
        dateloctimelist = filterEvent("Time",time,dateloclist);
        dateloctimebudlist = filterEvent("Price", price, dateloclist);

        eventlist = dateloctimebudlist;

        Events evts = new Events();
        evts.setEvents(eventlist);
        String eventsjson = objectMapper.writeValueAsString(evts);
        Log.i(TAG,  eventlist.size()+" eventsjson "+eventsjson +" events "+evts.getEvents().size());
        return eventsjson;
    }

    private static ArrayList<Event> loadEventsDummy() throws IOException {
        ArrayList<Event> evlist;
        Events events = createEventlistDummy();
        //evlist = events.getEvents();
        evlist =events.getEvents();
        Log.i(TAG, "loadevents "+ events.getEvents().get(0).toString());
        return evlist;
    }


    public static ArrayList<Event> loadEvents() throws IOException {
        ArrayList<Event> evlist;
        Log.i(TAG, "fileort "+evdir.toString());
        Events events = objectMapper.readValue(evdir, Events.class);
        evlist =events.getEvents();
        Log.i(TAG, "loadevents "+ events.getEvents().get(0).toString());
        return evlist;


    }


    public static ArrayList<Event> filterEvent(String type, String filter, ArrayList<Event> list) {
        ArrayList<Event> evlist = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            Event todo = list.get(i);
            String anfrage;
            boolean fits = false;
            if (type == "Location") {
                anfrage = todo.getLocation();
                fits = anfrage.equals(filter);
            } else if (type == "Date") {
                anfrage = todo.getDate();
                fits = anfrage.equals(filter);
            }else if(type=="Time"){
                anfrage = todo.getTime();
                String[] anf = anfrage.split(":");
                String[] fil = filter.split(":");
                if(Integer.valueOf(anf[0])>=Integer.valueOf(fil[0])){
                    if(Integer.valueOf(anf[1])>=Integer.valueOf(fil[1])){
                        fits = true;
                    }
                }
            }else if(type=="Price"){
                double pr = todo.getPrice();
                double filt =Double.valueOf(filter);
                if(pr<=filt){
                    fits = true;
                }

            }

            if (fits) {
                evlist.add(todo);
            }
        }
        return evlist;
    }

    public static ArrayList<Event> filterEvent(String type, double filter, ArrayList<Event> list) {
        ArrayList<Event> evlist = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            Event todo = list.get(i);
            Double anfrage = null;
            if (type == "Price") {
                anfrage = todo.getPrice();
            }
            if (anfrage <= filter) {
                evlist.add(todo);
            }
        }
        return evlist;
    }

    public static Events createEventlistDummy() throws IOException {
        Events events = new Events();
        Event todo0 = new Event("1", "Kartfahren", "Frankfurt", "20.05.2019", "15:00",
                "action", "Coole Sache", 20);

        Event todo1 = new Event("2", "Saufen", "Frankfurt", "20.05.2019", "15:00",
                "action", "Coole Sache", 20);

        Event todo2 = new Event("3", "Lernen", "Frankfurt", "20.05.2019", "15:00",
                "action", "Coole Sache", 20);

        events.addEvent(todo0);
        events.addEvent(todo1);
        events.addEvent(todo2);

        return events;
    }

}
