package com.example.bookinghotel.entity;

import android.util.Log;

import java.util.ArrayList;

public class Hotel {
    double lat;
    double longitude;
    String name;
    Room room;
    ArrayList<Integer> rating;
    public Hotel() {
    }


    public Hotel(double lat, double longitude, String name, Room room, ArrayList<Integer> rating) {
        this.lat = lat;
        this.longitude = longitude;
        this.name = name;
        this.room = room;
        this.rating = rating;
    }
    public ArrayList<Integer> getRating() {
        return rating;
    }

    public void setRating(ArrayList<Integer> rating) {
        this.rating = rating;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
    }
    public float getAveRating(){
        Integer total = 0 ;
        Integer numberRating=0;
        for(int i=1;i<=this.rating.size()-1;i++){
            numberRating += this.rating.get(i);
            total += i * this.rating.get(i);
        }
        return  (float) total / (numberRating);
    }
    @Override
    public String toString() {
        return "Hotel{" +
                "lat=" + lat +
                ", longitude=" + longitude +
                ", rating=" + getAveRating() +
                ", name='" + name + '\'' +
                ", romm='" + room.toString() + '\'' +
                ", avage rating='" + room.toString() + '\'' +
                '}';
    }
    public boolean isEmpty(String string) {
        return (string != null && string.isEmpty());
    }
}