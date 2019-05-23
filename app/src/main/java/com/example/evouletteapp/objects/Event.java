package com.example.evouletteapp.objects;

public class Event {

    private String id;
    private String name;
    private String location;
    private String date;
    private String time;
    private String category;
    private String description;
    private double price;

    public Event(String id, String name, String location, String date, String time, String categroy,
                 String description, double price) {
        super();
        this.id = id;
        this.name = name;
        this.location = location;
        this.date = date;
        this.time = time;
        this.category = categroy;
        this.description = description;
        this.price = price;
    }

    public Event(){

    }
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getLocation() {
        return location;
    }
    public void setLocation(String location) {
        this.location = location;
    }
    public String getDate() {
        return date;
    }
    public void setDate(String date) {
        this.date = date;
    }
    public String getTime() {
        return time;
    }
    public void setTime(String time) {
        this.time = time;
    }
    public String getCategory() {
        return category;
    }
    public void setCategory(String category) {
        this.category = category;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public double getPrice() {
        return price;
    }
    public void setPrice(double price) {
        this.price = price;
    }
}
